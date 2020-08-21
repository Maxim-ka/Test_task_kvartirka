package com.reschikov.kvartirka.testtask.presentation.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.snackbar.Snackbar
import com.reschikov.kvartirka.testtask.*
import com.reschikov.kvartirka.testtask.di.scopelistflats.ScopeListFlats
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.domain.enteries.NoPermissionException
import com.reschikov.kvartirka.testtask.domain.enteries.SizePx
import com.reschikov.kvartirka.testtask.presentation.ui.MainActivity
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.FootAdapter
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.ListFlatsAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.block_progress.*
import kotlinx.android.synthetic.main.block_recycler.*
import javax.inject.Inject

private const val HALF = 0.5f

@ScopeListFlats
class ListFlatsFragment : BaseFlatFragment<Ad>(R.layout.fragment_list_flats){

    @Inject lateinit var flatsAdapter : ListFlatsAdapter

    private val observerListFlats by lazy {
        Observer<PagingData<Ad>>{ flatsAdapter.submitData(lifecycle, it) }
    }
    private val observerNameCity by lazy {
        Observer<String>{ (activity as MainActivity).supportActionBar?.title = it}
    }
    private val loadStateListener = {combined : CombinedLoadStates ->
        pb_circle.visibility = if(combined.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
        if (combined.source.refresh is LoadState.Error){
            renderError((combined.source.refresh as LoadState.Error).error)
        }
    }
    private lateinit var sizePx: SizePx

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState ?: activity?.let {
            sizePx = it.getSizeDisplay()
            updateable.setRequestParameters(sizePx, DEFAULT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateable.getFlats().observe(viewLifecycleOwner, observerListFlats)
        updateable.getNameCity().observe(viewLifecycleOwner, observerNameCity)
    }

    private fun Activity.getSizeDisplay() : SizePx {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            val width = (displayMetrics.widthPixels * HALF).toInt()
            return SizePx(width, width)
        }
        val height = (displayMetrics.heightPixels * HALF).toInt()
        return SizePx(height, height)
    }

    override fun createRecycler(){
        val mergeAdapter = flatsAdapter.withLoadStateHeaderAndFooter(FootAdapter{flatsAdapter.retry()}, FootAdapter{flatsAdapter.retry()})
        rv_flats.layoutManager = LinearLayoutManager(rv_flats.context)
        rv_flats.adapter = mergeAdapter
        rv_flats.setHasFixedSize(false)
    }

    override fun onItemClick(item : Ad) {
        updateable.toChoose(item)
        findNavController().navigate(R.id.action_listFlatsFragment_to_flatFragment)
    }

    override fun onStart() {
        super.onStart()
        flatsAdapter.addLoadStateListener(loadStateListener)
    }

    private fun renderError(e : Throwable){
        when (e) {
            is ResolvableApiException ->{
                activity?.let { e.startResolutionForResult(it, REQUEST_GOOGLE_COORDINATE) }
            }
            is NoPermissionException -> {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_MY_PERMISSIONS_ACCESS_LOCATION)
            }
            else -> {
                view?.let { val msg = e.message ?: e.toString()
                    it.showAlertMessage(msg){ flatsAdapter.refresh() }
                }
            }
        }
    }

    private fun View.showAlertMessage(message: String, action : (() -> Unit)?){
        val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.view.setBackgroundColor(Color.RED)
        action?.let { act ->  snackBar.setAction(R.string.to_retry) {
            act.invoke()
            snackBar.dismiss() }
        }
        snackBar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GOOGLE_COORDINATE) {
            reload(resultCode == RESULT_OK)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_MY_PERMISSIONS_ACCESS_LOCATION) {
            reload(permissions.size == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                grantResults[1] == PackageManager.PERMISSION_GRANTED))
        }
    }

    private fun reload(isLocal : Boolean){
        updateable.getFlats().removeObserver(observerListFlats)
        updateable.setRequestParameters(sizePx, isLocal)
        updateable.getFlats().observe(viewLifecycleOwner, observerListFlats)
    }

    override fun onStop() {
        super.onStop()
        flatsAdapter.removeLoadStateListener(loadStateListener)
    }
}
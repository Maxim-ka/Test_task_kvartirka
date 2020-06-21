package com.reschikov.kvartirka.testtask.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.reschikov.kvartirka.testtask.*
import com.reschikov.kvartirka.testtask.di.scopelistflats.ScopeListFlats
import com.reschikov.kvartirka.testtask.domain.Ad
import com.reschikov.kvartirka.testtask.domain.NoPermissionException
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.FootAdapter
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.ListFlatsAdapter
import kotlinx.android.synthetic.main.fragment_flat.*
import javax.inject.Inject

@ScopeListFlats
class ListFlatsFragment : BaseFlatFragment<Ad>(R.layout.fragment_list_flats){

    @Inject lateinit var flatsAdapter : ListFlatsAdapter

    private val observerListFlats by lazy {
        Observer<PagingData<Ad>>{
            flatsAdapter.submitData(lifecycle, it)
        }
    }
    private val loadStateListener = {combined : CombinedLoadStates ->
        if (combined.refresh is LoadState.Error){
            renderError((combined.refresh as LoadState.Error).error)
        }
        if (combined.source.refresh is LoadState.Error){
            renderError((combined.source.refresh as LoadState.Error).error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppTestTask.getAppDagger().getFlatsComponent(this).inject(this)
        super.onViewCreated(view, savedInstanceState)
        updateable.getFlats().observe(viewLifecycleOwner, observerListFlats)
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
                updateable.setError(e)
                flatsAdapter.refresh()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_GOOGLE_COORDINATE) loadData(resultCode == RESULT_OK)
    }

    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_MY_PERMISSIONS_ACCESS_LOCATION -> {
                if (permissions.size == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    loadData(true)
                } else {
                    loadData(false)
                }
            }
        }
    }

    private fun loadData(isLocal: Boolean){
        activity?.let {
            val size = it.getSizeDisplay()
            updateable.getListOfAbs(size.first, size.second, isLocal)
            flatsAdapter.refresh()
        }
    }

    override fun onStop() {
        super.onStop()
        flatsAdapter.removeLoadStateListener(loadStateListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppTestTask.getAppDagger().clearFlatsComponent()
    }
}
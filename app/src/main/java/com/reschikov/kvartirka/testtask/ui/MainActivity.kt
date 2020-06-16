package com.reschikov.kvartirka.testtask.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.reschikov.kvartirka.testtask.AppTestTask
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.domain.NoPermissionException
import com.reschikov.kvartirka.testtask.ui.viewmodel.MainViewModel
import com.reschikov.kvartirka.testtask.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val REQUEST_GOOGLE_COORDINATE = 1
private const val REQUEST_MY_PERMISSIONS_ACCESS_LOCATION = 10
private const val HALF = 0.5f

class MainActivity : AppCompatActivity() {

    @Inject lateinit var factory: ViewModelFactory

    private val observerVisibleProgress by lazy {
        Observer<Boolean>{  pb_circle.visibility = if(it) View.VISIBLE else View.GONE }
    }
    private val observerError by lazy {
        Observer<Throwable?>{ it?.let { renderError(it) } }
    }
    private val observerNameCity by lazy {
        Observer<String>{ supportActionBar?.title = it}
    }
    private lateinit var updateable: Updateable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppTestTask.getAppDagger().getAppComponent().inject(this)
        updateable = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        updateable.isVisibleProgress().observe(this, observerVisibleProgress)
        updateable.getError().observe(this, observerError)
        updateable.getNameCity().observe(this, observerNameCity)
        if(savedInstanceState == null) {
            loadData(true)
        }
    }

    private fun renderError(e : Throwable){
        when (e) {
            is ResolvableApiException ->{
                e.startResolutionForResult(this, REQUEST_GOOGLE_COORDINATE)
            }
            is NoPermissionException -> {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_MY_PERMISSIONS_ACCESS_LOCATION)
            }
            else -> e.message?.let { showAlertDialog(it) }
        }
    }

    private fun showAlertDialog(message: String){
        AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            .setTitle(R.string.warning)
            .setIcon(R.drawable.ic_warning)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.but_ok)){ dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
        val size = getSizeDisplay()
        updateable.getListOfAbs(size.first, size.second, isLocal)
    }

    private fun getSizeDisplay() : Pair<Int, Int>{
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * HALF).toInt()
        val height = (displayMetrics.heightPixels * HALF).toInt()
        return Pair(width, height)
    }
}

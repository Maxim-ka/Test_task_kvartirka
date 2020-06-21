package com.reschikov.kvartirka.testtask.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.reschikov.kvartirka.testtask.*
import com.reschikov.kvartirka.testtask.ui.fragments.ListFlatsFragment
import com.reschikov.kvartirka.testtask.ui.viewmodel.MainViewModel
import com.reschikov.kvartirka.testtask.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var factory: ViewModelFactory

    private val observerVisibleProgress by lazy {
        Observer<Boolean>{  pb_circle.visibility = if(it) View.VISIBLE else View.GONE }
    }
    private val observerError by lazy {
        Observer<Throwable?>{ it?.let {  it.message?.let {errMsg -> showAlertDialog(errMsg) } } }
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
            loadData()
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
        if(requestCode == REQUEST_GOOGLE_COORDINATE) {
            val navController = Navigation.findNavController(this, R.id.frame_master)
            if (navController.currentDestination?.id == R.id.listFlatsFragment){
                (supportFragmentManager
                    .primaryNavigationFragment
                    ?.childFragmentManager
                    ?.primaryNavigationFragment as ListFlatsFragment).onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun loadData(){
        val size = getSizeDisplay()
        updateable.getListOfAbs(size.first, size.second, true)
    }
}

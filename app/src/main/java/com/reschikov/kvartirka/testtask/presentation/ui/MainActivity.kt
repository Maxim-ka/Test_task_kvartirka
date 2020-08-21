package com.reschikov.kvartirka.testtask.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.reschikov.kvartirka.testtask.*
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.ListFlatsFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

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
}
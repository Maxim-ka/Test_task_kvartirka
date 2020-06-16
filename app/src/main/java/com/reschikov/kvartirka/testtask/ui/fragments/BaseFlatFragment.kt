package com.reschikov.kvartirka.testtask.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.ui.Updateable
import com.reschikov.kvartirka.testtask.ui.viewmodel.MainViewModel
import com.reschikov.kvartirka.testtask.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

abstract class BaseFlatFragment : Fragment(R.layout.fragment_list), OnItemClickListener{

    @Inject lateinit var factory: ViewModelFactory
    protected lateinit var updateable: Updateable

    abstract fun createRecycler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            updateable = ViewModelProvider(it, factory).get(MainViewModel::class.java)
        }
        createRecycler()
    }
}
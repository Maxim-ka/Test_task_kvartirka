package com.reschikov.kvartirka.testtask.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.reschikov.kvartirka.testtask.presentation.ui.Updateable
import com.reschikov.kvartirka.testtask.presentation.ui.viewmodel.MainViewModel
import com.reschikov.kvartirka.testtask.presentation.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

abstract class BaseFlatFragment<T>(resId : Int) : Fragment(resId), OnItemClickListener<T>{

    @Inject lateinit var factory: ViewModelFactory
    protected lateinit var updateable: Updateable

    abstract fun createRecycler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        takeUnless { this::updateable.isInitialized }?.let {  activity?.let {
            updateable = ViewModelProvider(it, factory).get(MainViewModel::class.java) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecycler()
    }
}
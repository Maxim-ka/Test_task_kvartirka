package com.reschikov.kvartirka.testtask.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reschikov.kvartirka.testtask.AppTestTask
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.data.network.model.Flat
import com.reschikov.kvartirka.testtask.di.scopelistflats.ScopeListFlats
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.ListFlatsAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

@ScopeListFlats
class ListFlatsFragment : BaseFlatFragment(){

    @Inject lateinit var flatsAdapter : ListFlatsAdapter

    private val observerListFlats by lazy {
        Observer<List<Flat>>{ flatsAdapter.list = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppTestTask.getAppDagger().getFlatsComponent(this).inject(this)
        super.onViewCreated(view, savedInstanceState)
        updateable.getFlats().observe(viewLifecycleOwner, observerListFlats)
    }

    override fun createRecycler(){
        rv_flats.layoutManager = LinearLayoutManager(rv_flats.context)
        rv_flats.adapter = flatsAdapter
        rv_flats.setHasFixedSize(true)
    }

    override fun onItemClick(position : Int) {
        updateable.toChoose(position)
        findNavController().navigate(R.id.action_listFlatsFragment_to_flatFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppTestTask.getAppDagger().clearFlatsComponent()
    }
}
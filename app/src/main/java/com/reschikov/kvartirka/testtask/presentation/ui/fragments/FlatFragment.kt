package com.reschikov.kvartirka.testtask.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.reschikov.kvartirka.testtask.KEY_PHOTOS
import com.reschikov.kvartirka.testtask.KEY_POSITION
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.di.scopelistphoto.ScopeListPhotos
import com.reschikov.kvartirka.testtask.domain.enteries.Ad
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.ListPhotoAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.block_recycler.*
import kotlinx.android.synthetic.main.fragment_flat.*
import java.util.*
import javax.inject.Inject

private const val SPAN_COUNT = 2

@ScopeListPhotos
@SuppressLint("SetTextI18n")
class FlatFragment : BaseFlatFragment<String>(R.layout.fragment_flat){

    @Inject lateinit var photoAdapter : ListPhotoAdapter

    private val observerFlat by lazy {
        Observer<Ad>{
            tv_description.text = "${it.address}\n${it.type}\n${getString(R.string.room)} ${it.rooms}\n${it.price}"
            photoAdapter.list = it.urls
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateable.getFlat().observe(viewLifecycleOwner, observerFlat)
    }

    override fun createRecycler() {
        rv_flats.layoutManager = GridLayoutManager(rv_flats.context, SPAN_COUNT)
        rv_flats.adapter = photoAdapter
        rv_flats.setHasFixedSize(true)
    }

    override fun onItemClick(item : String) {
        val position = photoAdapter.list.indexOf(item)
        findNavController().navigate(R.id.action_flatFragment_to_photoFragment, Bundle().apply {
            putInt(KEY_POSITION, position)
            putStringArrayList(KEY_PHOTOS, photoAdapter.list as ArrayList)
        })
    }
}
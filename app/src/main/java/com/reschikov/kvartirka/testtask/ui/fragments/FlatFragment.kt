package com.reschikov.kvartirka.testtask.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.reschikov.kvartirka.testtask.AppTestTask
import com.reschikov.kvartirka.testtask.KEY_PHOTOS
import com.reschikov.kvartirka.testtask.KEY_POSITION
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.data.network.model.Photo
import com.reschikov.kvartirka.testtask.di.scopelistphoto.ScopeListPhotos
import com.reschikov.kvartirka.testtask.domain.Ad
import com.reschikov.kvartirka.testtask.ui.fragments.adapters.ListPhotoAdapter
import kotlinx.android.synthetic.main.fragment_flat.*
import java.util.*
import javax.inject.Inject

private const val SPAN_COUNT = 2

@ScopeListPhotos
@SuppressLint("SetTextI18n")
class FlatFragment : BaseFlatFragment<Photo>(R.layout.fragment_flat){

    @Inject lateinit var photoAdapter : ListPhotoAdapter

    private val observerFlat by lazy {
        Observer<Ad>{
            with(it.flat){
                tv_description.text = "${address}\n${type}\n${getString(R.string.room)} ${rooms}\n${prices.getPrice(tv_description.context, it.currency)}"
                photoAdapter.list = photos
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppTestTask.getAppDagger().getPhotosComponent(this).inject(this)
        super.onViewCreated(view, savedInstanceState)
        updateable.getFlat().observe(viewLifecycleOwner, observerFlat)
    }

    override fun createRecycler() {
        rv_flats.layoutManager = GridLayoutManager(rv_flats.context, SPAN_COUNT)
        rv_flats.adapter = photoAdapter
        rv_flats.setHasFixedSize(true)
    }

    override fun onItemClick(item : Photo) {
        val position = photoAdapter.list.indexOf(item)
        findNavController().navigate(R.id.action_flatFragment_to_photoFragment, Bundle().apply {
            putInt(KEY_POSITION, position)
            putParcelableArrayList(KEY_PHOTOS, photoAdapter.list as ArrayList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppTestTask.getAppDagger().clearPhotosComponent()
    }
}
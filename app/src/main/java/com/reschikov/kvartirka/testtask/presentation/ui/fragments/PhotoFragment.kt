package com.reschikov.kvartirka.testtask.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.reschikov.kvartirka.testtask.KEY_PHOTOS
import com.reschikov.kvartirka.testtask.KEY_POSITION
import com.reschikov.kvartirka.testtask.R
import com.reschikov.kvartirka.testtask.di.scopephoto.ScopePhoto
import com.reschikov.kvartirka.testtask.presentation.ui.fragments.adapters.PhotoAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photos.*
import javax.inject.Inject

@ScopePhoto
class PhotoFragment : Fragment(R.layout.fragment_photos) {

    @Inject lateinit var photoAdapter: PhotoAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp2_photo.adapter = photoAdapter
        arguments?.let {
            it.getStringArrayList(KEY_PHOTOS)?.let { arrayList -> photoAdapter.list = arrayList  }
            vp2_photo.setCurrentItem(it.getInt(KEY_POSITION), false)
        }
        vp2_photo.orientation = ORIENTATION_HORIZONTAL
    }
}
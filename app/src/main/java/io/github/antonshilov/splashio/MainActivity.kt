package io.github.antonshilov.splashio

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var vm: PhotoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = ViewModelProviders.of(this).get(PhotoListViewModel::class.java)
        loadImages()
    }


    private fun loadImages() {
        vm.photos.observe(this, Observer { showImages(it!!) })
        vm.loadPhotos()
    }

    private fun showImages(images: List<Photo>) {
        Timber.d(images.toString())
        imageGrid.adapter = PhotoAdapter(this, images)
    }


}
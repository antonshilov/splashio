package io.github.antonshilov.splashio


import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        savedInstanceState?.let {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_nav_host, ImageListFragment.newInstance())
//                    .commitNow()
//
//        }
    }


}
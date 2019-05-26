package io.github.antonshilov.splashio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import io.github.antonshilov.splashio.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setUpNavigation()
  }

  private fun setUpNavigation() {
    navigationView.setupWithNavController(findNavController(R.id.fragment_nav_host))
  }
}

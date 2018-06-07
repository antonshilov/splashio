package io.github.antonshilov.splashio


import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.view.WindowInsetsCompat
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  val statusBarHeight: MutableLiveData<WindowInsetsCompat> = MutableLiveData()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
      statusBarHeight.postValue(insets)
      insets.consumeSystemWindowInsets()
    }
  }

  override fun onSupportNavigateUp() = findNavController(R.id.fragment_nav_host).navigateUp()


}
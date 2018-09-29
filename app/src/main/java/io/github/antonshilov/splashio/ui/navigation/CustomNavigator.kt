package io.github.antonshilov.splashio.ui.navigation

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

@Navigator.Name("bottom_fragment") // Use as custom tag at navigation.xml
class CustomNavigator(
  private val context: Context,
  private val manager: FragmentManager,
  private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

  override fun navigate(
    destination: Destination,
    args: Bundle?,
    navOptions: NavOptions?,
    navigatorExtras: Navigator.Extras?
  ) {
    val tag = destination.id.toString()
    val transaction = manager.beginTransaction()

    val currentFragment = manager.primaryNavigationFragment
    if (currentFragment != null) {
      transaction.detach(currentFragment)
    }

    var fragment = manager.findFragmentByTag(tag)
    if (fragment == null) {
      fragment = destination.createFragment(args)
      transaction.add(containerId, fragment, tag)
    } else {
      transaction.attach(fragment)
      // if we already on the current screen and trying to navigate on it we just scroll to top to implement
      // behaviour of bottom tab bar according to material guidelines
      if (fragment is Scrollable && currentFragment == fragment) fragment.scrollTop()
    }
    if (navigatorExtras is Extras) {
      val extras = navigatorExtras as Extras?
      for ((key, value) in extras!!.sharedElements) {
        transaction.addSharedElement(key, value)
      }
    }

    transaction.setPrimaryNavigationFragment(fragment)
    transaction.setReorderingAllowed(true)
    transaction.commit()

    dispatchOnNavigatorNavigated(destination.id, BACK_STACK_DESTINATION_ADDED)
  }
}

class CustomNavHostFragment : NavHostFragment() {
  override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
    return CustomNavigator(requireContext(), childFragmentManager, id)
  }
}

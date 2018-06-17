package io.github.antonshilov.splashio

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

/**
 * Fragment container that propagates window insets to its children.
 *
 * Used to make android:fitsSystemWindows="true" work for fragments.
 */
class InsetFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    return insets.consumeSystemWindowInsets()
  }
}
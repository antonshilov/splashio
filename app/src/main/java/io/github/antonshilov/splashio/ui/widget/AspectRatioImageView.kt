package io.github.antonshilov.splashio.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class AspectRatioImageView : AppCompatImageView {

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    super(context, attrs, defStyleAttr)

  var ratio: Float = INVALID_RATIO
    set(value) {
      field = value
      requestLayout()
    }

  fun setAspectRatio(width: Int, height: Int) {
    ratio = height.toFloat() / width
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    if (ratio != INVALID_RATIO) {
      applyAspectRatioDimensions(widthMeasureSpec)
    }
  }

  // TODO: extract dimension measuring logic to the separate unit testable class
  private fun applyAspectRatioDimensions(widthMeasureSpec: Int) {
    val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
    var width = measuredWidth
    var height = measuredHeight

    if (width == 0 && height == 0) {
      return
    }

    if (widthSpecMode == MeasureSpec.EXACTLY) {
      height = (width * ratio).toInt()
    } else {
      width = (height / ratio).toInt()
    }

    setMeasuredDimension(width, height)
  }

  companion object {
    const val INVALID_RATIO = -1F
  }
}

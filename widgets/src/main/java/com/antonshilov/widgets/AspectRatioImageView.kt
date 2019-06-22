package com.antonshilov.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.antonshilov.widgets.AspectRatioDimensionsCalculator.Companion.INVALID_RATIO

class AspectRatioImageView : AppCompatImageView {

  private val dimensionsCalculator = AspectRatioDimensionsCalculator()

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    super(context, attrs, defStyleAttr)

  var ratio: Float = INVALID_RATIO
    set(value) {
      dimensionsCalculator.checkAspectRatio(value)
      field = value
      requestLayout()
    }

  fun setAspectRatio(width: Int, height: Int) {
    dimensionsCalculator.checkAspectRatio(width, height)
    ratio = dimensionsCalculator.getAspectRatio(width, height)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (ratio != INVALID_RATIO) {
      val (width, height) = dimensionsCalculator.measureDimensions(widthMeasureSpec, heightMeasureSpec, ratio)
      setMeasuredDimension(width, height)
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
  }
}

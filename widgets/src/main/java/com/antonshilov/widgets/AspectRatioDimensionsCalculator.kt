package com.antonshilov.widgets

import android.view.View.MeasureSpec
import android.view.View.MeasureSpec.EXACTLY
import androidx.annotation.Px

class AspectRatioDimensionsCalculator {

  fun measureDimensions(widthMeasureSpec: Int, heightMeasureSpec: Int, ratio: Float): Pair<Int, Int> {
    val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
    val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
    var width = MeasureSpec.getSize(widthMeasureSpec)
    var height = MeasureSpec.getSize(heightMeasureSpec)

    when {
      widthSpecMode == EXACTLY && heightSpecMode == EXACTLY -> { // do not change any dimension}
      }
      widthSpecMode == EXACTLY -> height = (width / ratio).toInt() // scale height
      heightSpecMode == EXACTLY -> width = (height * ratio).toInt() // scale width
    }
    return width to height
  }

  fun getAspectRatio(@Px width: Int, @Px height: Int): Float {
    checkAspectRatio(width, height)
    return width.toFloat() / height
  }

  fun checkAspectRatio(@Px width: Int, @Px height: Int) {
    if (width < 1 || height < 1) {
      throw IllegalArgumentException("Aspect ratio dimensions cannot be smaller than 1")
    }
  }

  fun checkAspectRatio(ratio: Float) {
    if (ratio <= 0)
      throw IllegalArgumentException("Aspect ratio should be greater than 0")
  }

  companion object {
    const val INVALID_RATIO = -1F
  }
}

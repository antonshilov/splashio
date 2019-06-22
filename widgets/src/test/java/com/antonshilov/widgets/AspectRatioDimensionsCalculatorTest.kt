package com.antonshilov.widgets

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AspectRatioDimensionsCalculatorTest {

  private val calculator = AspectRatioDimensionsCalculator()

  @Test
  fun measureDimensionsInvalidRatio() {
//    val widthSpec = MeasureSpec.makeMeasureSpec(10, MeasureSpec.EXACTLY)
//    val heigthSpec = MeasureSpec.makeMeasureSpec(15, MeasureSpec.EXACTLY)
//    val (w,h) = calculator.measureDimensions(widthSpec,heigthSpec,AspectRatioDimensionsCalculator.INVALID_RATIO)
//    assertEquals(10,w)
//    assertEquals(15,h)
  }

  @Test
  fun checkAspectRatioWithDimensions() {
    assertThrows<IllegalArgumentException> { calculator.checkAspectRatio(-1, -1) }
    assertThrows<IllegalArgumentException> { calculator.checkAspectRatio(0, 0) }
    calculator.checkAspectRatio(1, 1)
  }

  @Test
  fun checkAspectRatio() {
    calculator.checkAspectRatio(0.5f)
    calculator.checkAspectRatio(1f)
    assertThrows<IllegalArgumentException> { calculator.checkAspectRatio(0f) }
    assertThrows<IllegalArgumentException> { calculator.checkAspectRatio(-10f) }
  }
}

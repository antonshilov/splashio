package io.github.antonshilov.splashio.ui

import android.annotation.TargetApi
import android.os.Build
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DetailsTransition : TransitionSet() {
  init {
    ordering = ORDERING_TOGETHER
    addTransition(ChangeBounds())
    addTransition(ChangeTransform())
    addTransition(ChangeImageTransform())
  }
}
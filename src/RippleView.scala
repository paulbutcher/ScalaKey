package com.paulbutcher.scalakey

import android.content.Context
import android.view.{View, WindowManager}

class RippleView(context: Context) extends View(context) with Logger {

  val (width, height) = getSize
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("onMeasure")
    setMeasuredDimension(width, height)
  }
  
  private def getSize(): (Int, Int) = {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager]
    val display = windowManager.getDefaultDisplay
    val width = display.getWidth
    (width, 100)//! TODO - sort out height
  }
}

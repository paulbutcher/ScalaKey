package com.paulbutcher.scalakey

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.WindowManager

class SquareView(context: Context) extends GLSurfaceView(context) with Logger {

  val (width, height) = getSize
  
  setRenderer(new SquareRenderer(context))
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("onMeasure")
    setMeasuredDimension(width, height)
  }
  
  private def getSize(): (Int, Int) = {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager]
    val display = windowManager.getDefaultDisplay
    val width = display.getWidth
    (width, 400)//! TODO - sort out height
  }
}

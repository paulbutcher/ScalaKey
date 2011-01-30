package com.paulbutcher.scalakey

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.{MotionEvent, WindowManager}

class RippleView(context: Context) extends GLSurfaceView(context) with Logger {

  val (width, height) = getSize
  
  val renderer = new RippleRenderer(context)
  setRenderer(renderer)
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("onMeasure")
    setMeasuredDimension(width, height)
  }
  
  override def onTouchEvent(event: MotionEvent) = {
    if (event.getAction == MotionEvent.ACTION_DOWN) {
      queueEvent(new Runnable() {
          def run() {
            renderer.startRipple
          }
        })
      true
    } else {
      false
    }
  }
  
  private def getSize(): (Int, Int) = {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager]
    val display = windowManager.getDefaultDisplay
    val width = display.getWidth
    (width, 400)//! TODO - sort out height
  }
}

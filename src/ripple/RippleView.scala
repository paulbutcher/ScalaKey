package com.paulbutcher.scalakey

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.{MotionEvent, WindowManager}

class RippleView(context: Context) extends GLSurfaceView(context) with Logger {
  
  val renderer = new RippleRenderer(context)
  setRenderer(renderer)
  
  val (width, height) = getSize
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("onMeasure")
    setMeasuredDimension(width, height)
  }
  
  override def onTouchEvent(event: MotionEvent) = {
    if (event.getAction == MotionEvent.ACTION_DOWN) {
      val actionIndex = event.getActionIndex
      val x = event.getX(actionIndex)
      val y = event.getY(actionIndex)
      
      val (worldX, worldY) = renderer.toWorldCoordinates(x, y)
      
      val character = KeyLayout.closest(worldX, worldY)
      
      d("Character: " + character)

      queueEvent(new Runnable() {
          def run() {
            renderer.startRipple(worldX, worldY)
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
    (width, (width / renderer.aspectRatio).toInt)
  }
}

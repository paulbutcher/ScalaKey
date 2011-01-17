package com.paulbutcher.scalakey

import android.content.Context
import android.graphics.{BitmapFactory, Canvas}
import android.view.{Display, MotionEvent, View, WindowManager}

class RippleView(context: Context) extends View(context) with Logger {

  val bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water)
  val (width, height) = getSize
  
  val unwarpedMesh = new Mesh(width, height)
  
  override def onDraw(canvas: Canvas) {
    d("onDraw")
    val warpedMesh = new Mesh(width, height, rippleX, rippleY, rippleStartTime)
    val mesh = if (warpedMesh.done) unwarpedMesh else warpedMesh
    canvas.drawBitmapMesh(bitmap, mesh.width, mesh.height, mesh.vertices, 0, null, 0, null)
    if (!warpedMesh.done) invalidate
  }
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("onMeasure")
    setMeasuredDimension(width, height)
  }
  
  override def onTouchEvent(event: MotionEvent): Boolean = {
    d("onTouchEvent")
    if (event.getAction == MotionEvent.ACTION_DOWN) {
      val (x, y) = (event.getX, event.getY)
      startRipple(x, y)
    }
    true
  }
  
  private var rippleX: Float = _
  private var rippleY: Float = _
  private var rippleStartTime: Long = _
  
  private def startRipple(x: Float, y: Float) {
    d("startRipple: "+ x +", "+ y)
    rippleX = x
    rippleY = y
    rippleStartTime = System.currentTimeMillis
    invalidate
  }
  
  private def getSize(): (Int, Int) = {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager]
    val display = windowManager.getDefaultDisplay
    val width = display.getWidth
    (width, width * bitmap.getHeight / bitmap.getWidth)
  }
}

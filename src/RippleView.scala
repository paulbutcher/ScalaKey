package com.paulbutcher.scalakey

import android.content.Context
import android.graphics.{BitmapFactory, Canvas}
import android.view.{Display, View, WindowManager}

class RippleView(context: Context) extends View(context) with Logger {

  val bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water)
  val (width, height) = getSize
  val mesh = new Mesh(width, height)
  
  override def onDraw(canvas: Canvas) {
    d("onDraw")
    canvas.drawBitmapMesh(bitmap, mesh.width, mesh.height, mesh.vertices, 0, null, 0, null)
  }
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("onMeasure")
    setMeasuredDimension(width, height)
  }
  
  private def getSize(): (Int, Int) = {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager]
    val display = windowManager.getDefaultDisplay
    val width = display.getWidth
    (width, width * bitmap.getHeight / bitmap.getWidth)
  }
}

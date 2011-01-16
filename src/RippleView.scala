package com.paulbutcher.scalakey

import android.content.Context
import android.graphics.{BitmapFactory, Canvas}
import android.view.View

class RippleView(context: Context) extends View(context) with Logger {

  val bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water)
  val mesh = new Mesh(400, 240)
  
  override def onDraw(canvas: Canvas) {
    d("onDraw")
    canvas.drawBitmapMesh(bitmap, mesh.width, mesh.height, mesh.vertices, 0, null, 0, null)
  }
  
  override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    d("setMeasuredDimension: "+ widthMeasureSpec +", "+ heightMeasureSpec)
    setMeasuredDimension(100, 100)
  }
}

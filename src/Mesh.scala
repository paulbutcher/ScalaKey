package com.paulbutcher.scalakey

import scala.math.{abs, atan, sin, cos}

class Mesh(viewWidth: Int, viewHeight: Int, centreX: Float = 0.0f, centreY: Float = 0.0f, startTime: Long = 0) {

  val width = 100
  val height = width * viewHeight / viewWidth
  
  val speed = 0.05f
  val wavelength = viewWidth.asInstanceOf[Float] / width * 5
  val amplitude = 3.0f

  val interval = System.currentTimeMillis - startTime
  val radius = interval * speed
  
  val done = interval > 5 * 1000.0

  val vertices = new Array[Float]((width + 1) * (height + 1) * 2)
  initVertices
  
  def initVertices() {
    var index = 0
    for (y <- 0 to height) {
      val yval = viewHeight.toFloat * y / height
      for (x <- 0 to width) {
        val xval = viewWidth.toFloat * x / width
        val (xwarped, ywarped) = warp(xval, yval, x, y)
        vertices(index) = xwarped
        vertices(index + 1) = ywarped
        index += 2
      }
    }
  }
  
  // val vertices = {
  //     for {
  //       y <- 0 to height
  //       yval = viewHeight.toFloat * y / height
  //       x <- 0 to width
  //       xval = viewWidth.toFloat * x / width
  //       (xwarp, ywarp) = warp(xval, yval, x, y)
  //       r <- List(xwarp, ywarp)
  //     } yield r
  //   }.toArray
    
  def warp(x: Float, y: Float, ix: Int, iy: Int): (Float, Float) = {
    val (dx, dy) = (x - centreX, y - centreY)
    
    if (abs(radius * radius - (dx * dx + dy * dy)) < wavelength * wavelength) {
      val theta = atan(dx / dy)
      val (xx, yy) = (x + amplitude * sin(theta), y + cos(theta))
      (xx.asInstanceOf[Float], yy.asInstanceOf[Float])
    } else {
      (x, y)
    }
  }
}

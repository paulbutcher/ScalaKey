package com.paulbutcher.scalakey

class Mesh(bitmapWidth: Int, bitmapHeight: Int) {
  val width = 100
  val height = 100 * bitmapWidth / bitmapHeight
  val vertices = (
      for {
        y <- 0 to height
        yval = bitmapHeight.toFloat * y / height
        x <- 0 to width
        xval = bitmapWidth.toFloat * x / width
        r <- List(xval, yval)
      } yield r
    ).toArray
}

package com.paulbutcher.scalakey

import java.nio.{ByteBuffer, ByteOrder, FloatBuffer, ShortBuffer}

class Mesh(width: Float, height: Float, columns: Int, rows: Int) {
  
  val vertexCount = columns * rows

  val vertexBuffer = {
    val sizeofFloat = 4
    val dimensions = 3
    val byteBuffer = ByteBuffer.allocateDirect(vertexCount * sizeofFloat * dimensions)
    byteBuffer.order(ByteOrder.nativeOrder)
    byteBuffer.asFloatBuffer
  }
  
  // Generate vertices in the range [-width/2 ... 0 ... width/2]
  // and similarly for height
  for {
    ypos <- -height / 2 to height / 2 by height / (rows - 1)
    xpos <- -width / 2 to width / 2 by width / (columns - 1)
  }
    vertexBuffer.put(Array(xpos.toFloat, ypos.toFloat, 0.0f))
  vertexBuffer.position(0)
  
  val indexCount = {
    val vertices = 2 * columns * (rows - 1)
    val degenerateVertices = (rows - 2) * 2
    vertices + degenerateVertices
  }
  
  val indexBuffer = {
    val sizeofShort = 2
    val byteBuffer = ByteBuffer.allocateDirect(indexCount * sizeofShort)
    byteBuffer.order(ByteOrder.nativeOrder)
    byteBuffer.asShortBuffer
  }
  
  // Indices for a single triangle strip. See:
  // http://marc.blog.atpurpose.com/2009/10/24/programatically-generating-a-rectangular-mesh-using-single-gl_triangle_strip/
  for (row <- 0 until rows - 1) {
    for (column <- 0 until columns) {
      indexBuffer.put((row * columns + column).toShort)
      indexBuffer.put(((row + 1) * columns + column).toShort)
    }
    
    // Extra vertices (of degenerate triangles) at the end of this row connecting it to the start of the next one
    if (row < (rows - 2)) {
      indexBuffer.put(((row + 1) * columns + (columns - 1)).toShort)
      indexBuffer.put(((row + 1) * columns).toShort)
    }
  }
  indexBuffer.position(0)
}

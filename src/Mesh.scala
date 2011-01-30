package com.paulbutcher.scalakey

import java.nio.{ByteBuffer, ByteOrder, FloatBuffer, ShortBuffer}

class Mesh private (width_ : Float, height_ : Float, columns_ : Int, rows_ : Int) {
  
  val width = width_
  val height = height_
  val columns = columns_
  val rows = rows_
  
  val vertexCount = columns * rows
  val vertexBuffer = allocateDirectFloatBuffer(vertexCount * 3)
  
  val indexCount = {
    val vertices = 2 * columns * (rows - 1)
    val degenerateVertices = (rows - 2) * 2
    vertices + degenerateVertices
  }
  val indexBuffer = allocateDirectShortBuffer(indexCount)
  
  val textureBuffer = allocateDirectFloatBuffer(vertexCount * 2)
  
  initializeBuffers
  
  private def allocateDirectFloatBuffer(size: Int) = {
    val sizeofFloat = 4
    val byteBuffer = ByteBuffer.allocateDirect(size * sizeofFloat)
    byteBuffer.order(ByteOrder.nativeOrder)
    byteBuffer.asFloatBuffer
  }
  
  private def allocateDirectShortBuffer(size: Int) = {
    val sizeofShort = 2
    val byteBuffer = ByteBuffer.allocateDirect(size * sizeofShort)
    byteBuffer.order(ByteOrder.nativeOrder)
    byteBuffer.asShortBuffer
  }
  
  @native private def initializeBuffers()
  
  @native private def ripple(now: Long)
}

object Mesh {
  System.loadLibrary("ripple")
  
  def create(width: Float, height: Float, columns: Int, rows: Int) =
    new Mesh(width, height, columns, rows)
}

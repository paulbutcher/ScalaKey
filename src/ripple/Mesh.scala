package com.paulbutcher.scalakey

import java.nio.{ByteBuffer, ByteOrder, FloatBuffer, ShortBuffer}

class Mesh private (val width : Float, val height : Float, val columns : Int, val rows : Int) {
  
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
  
  val rippleCount = 3
  var rippleIndex = 0
  val rippleXs = new Array[Float](rippleCount)
  val rippleYs = new Array[Float](rippleCount)
  val rippleTimes = new Array[Long](rippleCount)
  
  def startRipple(x: Float, y: Float) {
    rippleXs(rippleIndex) = x
    rippleYs(rippleIndex) = y
    rippleTimes(rippleIndex) = System.currentTimeMillis
    
    rippleIndex = (rippleIndex + 1) % rippleCount
  }
  
  def update() {
    ripple(System.currentTimeMillis)
  }

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
  
  @native private def ripple(elapsed: Long)
  
  @native private def initializeBuffers()
}

object Mesh {
  System.loadLibrary("ripple")
  
  def create(width: Float, height: Float, columns: Int, rows: Int) =
    new Mesh(width, height, columns, rows)
}

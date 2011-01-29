package com.paulbutcher.scalakey

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLUtils

import java.nio.{ByteBuffer, ByteOrder, FloatBuffer, ShortBuffer}
import javax.microedition.khronos.opengles.GL10

class Square(context: Context) {
  val vertexBuffer = {
      val vertices = Array(
          -1.0f,  1.0f, 0.0f,
          -1.0f, -1.0f, 0.0f,
           1.0f, -1.0f, 0.0f,
           1.0f,  1.0f, 0.5f
        )

      val vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
      vertexByteBuffer.order(ByteOrder.nativeOrder)
      val vertexBuffer_ = vertexByteBuffer.asFloatBuffer
      vertexBuffer_.put(vertices)
      vertexBuffer_.position(0)
      vertexBuffer_
    }
  
  val indexBuffer = {
    val indices = Array[Short](0, 1, 2, 0, 2, 3)

    val indexByteBuffer = ByteBuffer.allocateDirect(indices.length * 2)
    indexByteBuffer.order(ByteOrder.nativeOrder)
    val indexBuffer_ = indexByteBuffer.asShortBuffer
    indexBuffer_.put(indices)
    indexBuffer_.position(0)
  }
  
  val textureBuffer = {
    val textureCoordinates = Array[Float](
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
      )

    val textureByteBuffer = ByteBuffer.allocateDirect(textureCoordinates.length * 4)
    textureByteBuffer.order(ByteOrder.nativeOrder)
    val textureBuffer_ = textureByteBuffer.asFloatBuffer
    textureBuffer_.put(textureCoordinates)
    textureBuffer_.position(0)
  }
  
  val textures = new Array[Int](1)
  
  def init(gl: GL10) {
    val bitmap = BitmapFactory.decodeResource(context.getResources, R.drawable.keys)

    gl.glGenTextures(1, textures, 0)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textures(0))
    
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
  	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)

    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
  }

  def draw(gl: GL10) {
    gl.glFrontFace(GL10.GL_CCW)
    gl.glEnable(GL10.GL_CULL_FACE)
    gl.glCullFace(GL10.GL_BACK)
    
    gl.glEnable(GL10.GL_TEXTURE_2D)
    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textures(0))
    
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
    gl.glDrawElements(GL10.GL_TRIANGLES, indexBuffer.capacity,
      GL10.GL_UNSIGNED_SHORT, indexBuffer)
      
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
    gl.glDisable(GL10.GL_CULL_FACE)
    
    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    gl.glDisable(GL10.GL_TEXTURE_2D)
  }
}

package com.paulbutcher.scalakey

import java.nio.{ByteBuffer, ByteOrder, FloatBuffer, ShortBuffer}

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView.Renderer
import android.opengl.GLU
import android.opengl.GLUtils

class RippleRenderer(context: Context) extends Renderer with Logger {
  
  val aspectRatio = 1.5f
  val meshHeight = 2.0f
  val meshWidth = (meshHeight * aspectRatio).toFloat
  val rows = 65
  val columns = ((rows - 1) * aspectRatio + 1).toInt
  val mesh = Mesh.create(meshWidth, meshHeight, columns, rows)
  
  var windowWidth: Int = _
  var windowHeight: Int = _
  
  val planeDistance = (1.0 / -math.tan(math.Pi / 8)).toFloat
  
  val textures = new Array[Int](1)
  
  def onSurfaceCreated(gl: GL10, config: EGLConfig) {
    d("onSurfaceCreated")
    
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    gl.glShadeModel(GL10.GL_SMOOTH)
    gl.glClearDepthf(1.0f)
    gl.glEnable(GL10.GL_DEPTH_TEST)
    gl.glDepthFunc(GL10.GL_LEQUAL)
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
    
    val bitmap = BitmapFactory.decodeResource(context.getResources, R.drawable.keys)
    
    gl.glGenTextures(1, textures, 0)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textures(0))
        
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
    
    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)

    gl.glFrontFace(GL10.GL_CW)
    gl.glEnable(GL10.GL_CULL_FACE)
    gl.glCullFace(GL10.GL_BACK)
    
    gl.glEnable(GL10.GL_TEXTURE_2D)
    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mesh.textureBuffer)

    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mesh.vertexBuffer)
  }
  
  def onDrawFrame(gl: GL10) {
    d("onDrawFrame")
    
    mesh.update
    
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT)
    gl.glLoadIdentity

    gl.glTranslatef(0.0f, 0.0f, planeDistance)

    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, mesh.indexCount,
      GL10.GL_UNSIGNED_SHORT, mesh.indexBuffer)
  }
  
  def onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    d("onSurfaceChanged: "+ width +", "+ height)
    
    windowWidth = width
    windowHeight = height
    
    gl.glViewport(0, 0, width, height)
    gl.glMatrixMode(GL10.GL_PROJECTION)
    gl.glLoadIdentity
    GLU.gluPerspective(gl, 45.0f, aspectRatio, 1.0f, 10.0f)
    gl.glMatrixMode(GL10.GL_MODELVIEW)
    gl.glLoadIdentity
  }
  
  def startRipple(x: Float, y: Float) {
    val worldX = x / windowWidth * meshWidth - meshWidth / 2
    val worldY = y / windowHeight * meshHeight - meshHeight / 2
    
    mesh.startRipple(worldX, worldY)
  }
}

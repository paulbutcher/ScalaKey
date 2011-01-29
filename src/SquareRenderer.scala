package com.paulbutcher.scalakey

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.content.Context
import android.opengl.GLU
import android.opengl.GLSurfaceView.Renderer

class SquareRenderer(context: Context) extends Renderer {
  
  val square = new Square(context)

  def onSurfaceCreated(gl: GL10, config: EGLConfig) {
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f)
    gl.glShadeModel(GL10.GL_SMOOTH)
    gl.glClearDepthf(1.0f)
    gl.glEnable(GL10.GL_DEPTH_TEST)
    gl.glDepthFunc(GL10.GL_LEQUAL)
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
    
    square.init(gl)
  }
  
  def onDrawFrame(gl: GL10) {
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT)
    gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -4); 
    square.draw(gl)
  }
  
  def onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    gl.glViewport(0, 0, width, height)
    gl.glMatrixMode(GL10.GL_PROJECTION)
    gl.glLoadIdentity
    GLU.gluPerspective(gl, 45.0f, width.asInstanceOf[Float] / height, 0.1f, 100.0f)
    gl.glMatrixMode(GL10.GL_MODELVIEW)
    gl.glLoadIdentity
  }
}

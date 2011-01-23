package com.paulbutcher.scalakey

import android.opengl.GLSurfaceView

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class RippleRenderer extends GLSurfaceView.Renderer {
  
  RippleRenderer
  
  def onSurfaceCreated(gl: GL10, config: EGLConfig) {
    // do nothing
  }
  
  @native
  def onSurfaceChanged(gl: GL10, width: Int, height: Int)
  
  @native
  def onDrawFrame(gl: GL10)
}

object RippleRenderer extends Logger {
  d("Loading ripple library")
  System.loadLibrary("ripple")
}

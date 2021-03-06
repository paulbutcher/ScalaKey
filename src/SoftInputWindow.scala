package com.paulbutcher.scalakey

import android.app.Dialog
import android.content.Context
import android.os.IBinder
import android.view.{Gravity, ViewGroup, Window, WindowManager}

class SoftInputWindow(context: Context, theme: Int) extends Dialog(context, theme) with Logger {
  
  def this(context: Context) {
    this(context, android.R.style.Theme_InputMethod)
  }
  
  initDockWindow()

  // See android.inputmethodservice.SoftInputWindow.initDockWindow
  def initDockWindow() {
    d("initDockWindow")

    val window = getWindow
    val lp = window.getAttributes
    
    lp.`type` = WindowManager.LayoutParams.TYPE_INPUT_METHOD
    lp.setTitle("InputMethod")
    
    lp.gravity = Gravity.BOTTOM
    lp.width = -1
    
    window.setAttributes(lp)
    window.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
      WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
      WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
      WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    
    window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }
  
  def setToken(token: IBinder) {
    val window = getWindow
    val lp = window.getAttributes
    lp.token = token
    window.setAttributes(lp)
  }
}

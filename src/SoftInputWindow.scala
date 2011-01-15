package com.paulbutcher.scalakey

import android.app.Dialog
import android.content.Context
import android.os.IBinder
import android.view.{Gravity, Window, WindowManager}

class SoftInputWindow(context: Context) extends Dialog(context) {
  initDockWindow()

  // See android.inputmethodservice.SoftInputWindow.initDockWindow
  def initDockWindow() {
    var window = getWindow
    var lp = window.getAttributes
    
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
      WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
      
    setContentView(R.layout.hello)
  }
  
  def setToken(token: IBinder) {
    var window = getWindow
    var lp = window.getAttributes
    lp.token = token
    window.setAttributes(lp)
  }
}

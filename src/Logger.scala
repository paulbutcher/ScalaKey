package com.paulbutcher.scalakey

import android.util.Log

trait Logger {
  val tag = getClass.getName
  
  def d(m: String) { Log.d(tag, m) }
}

package com.paulbutcher.scalakey

import android.graphics.Rect
import android.inputmethodservice.AbstractInputMethodService
import android.os.{Bundle, IBinder, ResultReceiver}
import android.view.KeyEvent
import android.view.inputmethod.{CompletionInfo, EditorInfo, ExtractedText, InputBinding, InputConnection}

class ScalaKey extends AbstractInputMethodService with KeyListener with Logger {
  
  override def onCreate() {
    d("OnCreate")
    
    val view = new RippleView(this)
    view.setKeyListener(this)

    softInputWindow = new SoftInputWindow(this)
    softInputWindow.setContentView(view)
  }

  class InputMethodImpl extends AbstractInputMethodImpl {

    def attachToken(token: IBinder) {
      d("attachToken")
      softInputWindow.setToken(token)
    }

    def showSoftInput(flags: Int, resultReceiver: ResultReceiver) {
      d("showSoftInput")
      softInputWindow.show
    }

    def hideSoftInput(flags: Int, resultReceiver: ResultReceiver) {
      d("hideSoftInput")
      softInputWindow.hide
    }
    
    def startInput(inputConnection: InputConnection, info: EditorInfo) {
      d("startInput")
      
      startedInputConnection = inputConnection
    }

    def restartInput(inputConnection: InputConnection, attribute: EditorInfo) {
      d("restartInput")
      
      startedInputConnection = inputConnection
    }

    def bindInput(binding: InputBinding) {
      d("bindInput")
      
      boundInputConnection = binding.getConnection
    }

    def unbindInput() {
      d("unbindInput")
      
      boundInputConnection = null
    }
  }
  
  class InputMethodSessionImpl extends AbstractInputMethodSessionImpl {
    def toggleSoftInput(showFlags: Int, hideFlags: Int) { d("toggleSoftInput") }
    def appPrivateCommand(action: String, data: Bundle) { d("appPrivateCommand") }
    def updateExtractedText(token: Int, text: ExtractedText) { d("updateExtractedText") }
    def displayCompletions(completions: Array[CompletionInfo]) { d("displayCompletions") }
    def updateCursor(newCursor: Rect) { d("updateCursor") }
    def updateSelection(oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int, candidatesStart: Int, candidatesEnd: Int) { d("updateSelection") }

    def finishInput() {
      d("finishInput")
      
      startedInputConnection = null
    }
  }
  
  def onCreateInputMethodInterface(): AbstractInputMethodImpl = new InputMethodImpl
  
  def onCreateInputMethodSessionInterface(): AbstractInputMethodSessionImpl = new InputMethodSessionImpl
  
  def onKeyDown(keyCode: Int, event: KeyEvent) = false
  def onKeyLongPress(keyCode: Int, event: KeyEvent) = false
  def onKeyMultiple(keyCode: Int, count: Int, event: KeyEvent) = false
  def onKeyUp(keyCode: Int, event: KeyEvent) = false
  
  def onKey(character: Char) {
    currentInputConnection.commitText(character.toString, 1)
  }
  
  private def currentInputConnection =
    if (startedInputConnection != null)
      startedInputConnection
    else
      boundInputConnection
  
  private var softInputWindow: SoftInputWindow = _

  private var startedInputConnection: InputConnection = _
  private var boundInputConnection: InputConnection = _
}

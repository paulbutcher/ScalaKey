package com.paulbutcher.scalakey

import android.graphics.Rect
import android.inputmethodservice.AbstractInputMethodService
import android.os.{Bundle, IBinder, ResultReceiver}
import android.view.KeyEvent
import android.view.inputmethod.{CompletionInfo, EditorInfo, ExtractedText, InputBinding, InputConnection}

class ScalaKey extends AbstractInputMethodService with Logger {
  
  override def onCreate() {
    d("OnCreate")
    softInputWindow = new SoftInputWindow(this)
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

    def hideSoftInput(flags: Int, resultReceiver: ResultReceiver) { d("hideSoftInput") }
    def restartInput(inputConnection: InputConnection, attribute: EditorInfo) { d("restartInput") }
    def startInput(inputConnection: InputConnection, info: EditorInfo) { d("startInput") }
    def unbindInput() { d("unbindInput") }
    def bindInput(binding: InputBinding) { d("bindInput") }
  }
  
  class InputMethodSessionImpl extends AbstractInputMethodSessionImpl {
    def toggleSoftInput(showFlags: Int, hideFlags: Int) { d("toggleSoftInput") }
    def appPrivateCommand(action: String, data: Bundle) { d("appPrivateCommand") }
    def updateExtractedText(token: Int, text: ExtractedText) { d("updateExtractedText") }
    def displayCompletions(completions: Array[CompletionInfo]) { d("displayCompletions") }
    def updateCursor(newCursor: Rect) { d("updateCursor") }
    def updateSelection(oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int, candidatesStart: Int, candidatesEnd: Int) { d("updateSelection") }
    def finishInput() { d("finishInput") }
  }
  
  def onCreateInputMethodInterface(): AbstractInputMethodImpl = new InputMethodImpl
  
  def onCreateInputMethodSessionInterface(): AbstractInputMethodSessionImpl = new InputMethodSessionImpl
  
  def onKeyDown(keyCode: Int, event: KeyEvent) = false
  def onKeyLongPress(keyCode: Int, event: KeyEvent) = false
  def onKeyMultiple(keyCode: Int, count: Int, event: KeyEvent) = false
  def onKeyUp(keyCode: Int, event: KeyEvent) = false
  
  private var softInputWindow: SoftInputWindow = _
}

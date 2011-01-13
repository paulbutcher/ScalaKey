package com.paulbutcher.scalakey

import android.graphics.Rect
import android.inputmethodservice.AbstractInputMethodService
import android.os.{Bundle, IBinder, ResultReceiver}
import android.view.KeyEvent
import android.view.inputmethod.{CompletionInfo, EditorInfo, ExtractedText, InputBinding, InputConnection}

class ScalaKey extends AbstractInputMethodService {

  class InputMethodImpl extends AbstractInputMethodImpl {
    def hideSoftInput(flags: Int, resultReceiver: ResultReceiver) {}
    def showSoftInput(flags: Int, resultReceiver: ResultReceiver) {}
    def restartInput(inputConnection: InputConnection, attribute: EditorInfo) {}
    def startInput(inputConnection: InputConnection, info: EditorInfo) {}
    def unbindInput() {}
    def bindInput(binding: InputBinding) {}
    def attachToken(token: IBinder) {}
  }
  
  class InputMethodSessionImpl extends AbstractInputMethodSessionImpl {
    def toggleSoftInput(showFlags: Int, hideFlags: Int) {}
    def appPrivateCommand(action: String, data: Bundle) {}
    def updateExtractedText(token: Int, text: ExtractedText) {}
    def displayCompletions(completions: Array[CompletionInfo]) {}
    def updateCursor(newCursor: Rect) {}
    def updateSelection(oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int, candidatesStart: Int, candidatesEnd: Int) {}
    def finishInput() {}
  }
  
  def onCreateInputMethodInterface(): AbstractInputMethodImpl = new InputMethodImpl
  
  def onCreateInputMethodSessionInterface(): AbstractInputMethodSessionImpl = new InputMethodSessionImpl
  
  def onKeyDown(keyCode: Int, event: KeyEvent) = false
  def onKeyLongPress(keyCode: Int, event: KeyEvent) = false
  def onKeyMultiple(keyCode: Int, count: Int, event: KeyEvent) = false
  def onKeyUp(keyCode: Int, event: KeyEvent) = false
}
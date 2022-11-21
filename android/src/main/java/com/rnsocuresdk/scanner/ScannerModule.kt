package com.rnsocuresdk.scanner

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext

const val SCAN_PASSPORT_CODE = 200
const val SCAN_LICENSE_CODE = 300
const val SCAN_SELFIE_CODE = 400

interface ScanModuleResult {

  fun onSuccess(requestCode: Int)
  fun onError(requestCode: Int, message: String)
}


open class BaseActivityEventListener : ActivityEventListener {

  @Deprecated("use {@link #onActivityResult(Activity, int, int, Intent)} instead. ")
  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
  }

  override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {}
  override fun onNewIntent(intent: Intent?) {}
}

abstract class ScannerModule(private val context: ReactApplicationContext): BaseActivityEventListener(), ScanModuleResult {

  protected var promise: Promise? = null

  abstract val scanRequestCode: Int
  abstract fun buildScannerIntent(): Intent
  abstract fun getNormalImageResponse(promise: Promise)

  fun startScanner(promise: Promise, activity: Activity?) {
    try {
      this.promise = promise
      context.addActivityEventListener(this)

      val intent = buildScannerIntent()
      activity?.startActivityForResult(intent, scanRequestCode)
    } catch(error: Exception) {
      println(error)
    }
  }


  override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
      Log.d("[SOCURE]", "onActivityResult $requestCode")

      if(requestCode == scanRequestCode) {
        println("ON ACTIVITY RESULT MODULE $scanRequestCode")
        when (resultCode) {
          Activity.RESULT_OK -> {
            data?.getStringExtra("error")?.let {
              onError(requestCode, it)
            } ?: run {
              onSuccess(requestCode)
            }
          }
          Activity.RESULT_CANCELED -> onError(requestCode, "DOC_SCAN_CANCELLED")
          else -> onError(requestCode, "DOC_SCAN_ERROR")
        }
      }

      Log.d("[SOCURE]", "onActivityResult FINSHED")
      context.removeActivityEventListener(this)
  }

}

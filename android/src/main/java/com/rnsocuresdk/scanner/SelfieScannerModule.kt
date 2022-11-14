package com.rnsocuresdk.scanner

import android.content.Intent
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.scanner.selfie.SelfieActivity

class SelfieScannerModule(private val context: ReactApplicationContext): ScannerModule(context) {

  override val scanRequestCode: Int
    get() = SCAN_SELFIE_CODE

  override fun buildScannerIntent(): Intent = Intent(context, SelfieActivity::class.java)

  override fun onSuccess(requestCode: Int) {
    Log.d("[SOCURE]", "onSuccess")
    val selfieResult = SDKAppDataPublic.selfieScanResult
    val selfieResponse: WritableMap = Arguments.createMap()

    selfieResponse.putString("type", "SELFIE")
    selfieResponse.putString("image", Base64.encodeToString(selfieResult?.imageData, Base64.DEFAULT)  ?: "")

    Log.d("[SOCURE]", selfieResponse.toString())
    promise?.resolve(selfieResponse)
  }

 override fun onError(requestCode: Int, message: String) {
    promise?.reject(Throwable(message))
  }

  override fun getNormalImageResponse(promise: Promise) {
    promise.reject(Throwable("Not implemented"))
  }
}

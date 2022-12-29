package com.rnsocuresdk

import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.rnsocuresdk.scanner.GET_CONSENT_CODE
import com.rnsocuresdk.scanner.ScannerModule
import com.socure.idplus.scanner.consent.ConsentActivity
import com.socure.idplus.util.KEY_MESSAGE
import com.socure.idplus.util.KEY_SESSION_ID
import com.socure.idplus.util.KEY_SESSION_TOKEN

class ConsentModule(private val context: ReactApplicationContext): ScannerModule(context) {

    override val scanRequestCode: Int
        get() = GET_CONSENT_CODE

    override fun buildScannerIntent(): Intent = Intent(context, ConsentActivity::class.java)

    override fun getNormalImageResponse(promise: Promise) {
        promise.reject(Throwable("Not implemented"))
    }

    override fun onSuccess(requestCode: Int, data: Intent?) {
        Log.d("[SOCURE]", "onSuccess")
        
        val consentResponse: WritableMap = Arguments.createMap()
        consentResponse.putString("type", "CONSENT")
        consentResponse.putString("message", data?.getStringExtra(KEY_MESSAGE))
        consentResponse.putString("sessionId", data?.getStringExtra(KEY_SESSION_ID))
        consentResponse.putString("sessionToken", data?.getStringExtra(KEY_SESSION_TOKEN))

        Log.d("[SOCURE]", consentResponse.toString())
        promise?.resolve(consentResponse)
    }

    override fun onError(requestCode: Int, message: String) {
        promise?.reject(Throwable(message))
    }
}
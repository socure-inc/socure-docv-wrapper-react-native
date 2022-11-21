package com.rnsocuresdk

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.rnsocuresdk.scanner.LicenseScannerModule
import com.rnsocuresdk.scanner.PassportScannerModule
import com.rnsocuresdk.scanner.SelfieScannerModule
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.util.Constants

@ExperimentalStdlibApi
class RnSocureSdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val licenseModule: LicenseScannerModule = LicenseScannerModule(reactContext)
    private val passportModule: PassportScannerModule = PassportScannerModule(reactContext)
    private val selfieModule: SelfieScannerModule = SelfieScannerModule(reactContext)
    private val uploadDocument: InformationUploadModule = InformationUploadModule(reactContext)

    override fun getName(): String {
        return "RnSocureSdk"
    }

    @ReactMethod
    fun scanLicense(promise: Promise) {
      licenseModule.startScanner(promise, currentActivity)
    }

    @ReactMethod
    fun getScannedLicense(promise: Promise) {
        licenseModule.getNormalImageResponse(promise)
    }

    @ReactMethod
    fun getScannedPassport(promise: Promise) {
        passportModule.getNormalImageResponse(promise)
    }

    @ReactMethod
    fun scanPassport(promise: Promise) {
      passportModule.startScanner(promise, currentActivity)
    }

    @ReactMethod
    fun captureSelfie(promise: Promise) {
      selfieModule.startScanner(promise, currentActivity)
    }

    @ReactMethod
    fun uploadScannedInfo(promise: Promise) {
        uploadDocument.upload(promise)
    }

    @ReactMethod
    fun setSocureSdkKey(publicKey: String) {
        SDKAppDataPublic.setSocureSdkKey(publicKey)
    }
/*
    @ReactMethod
    fun uploadSelfie(selfiePath: String, documentType: Int, promise: Promise) {
        val dType = when(documentType) {
            0 -> Constants.DOCUMENTTYPE_LICENSE
            1 -> Constants.DOCUMENTTYPE_PASSPORT
            else -> "error"
        }
        uploadDocument.uploadSelfie(dType, promise)
    }
*/
}

package com.rnsocuresdk.scanner

import android.content.Intent
import android.util.Base64.DEFAULT
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.model.BarcodeData
import com.socure.idplus.model.MrzData

import com.socure.idplus.scanner.license.LicenseScannerActivity
import java.lang.Exception

@ExperimentalStdlibApi
class LicenseScannerModule(private val context: ReactApplicationContext): ScannerModule(context) {

  override val scanRequestCode: Int
    get() = SCAN_LICENSE_CODE

  override fun buildScannerIntent(): Intent = Intent(context, LicenseScannerActivity::class.java)

  override fun onSuccess(requestCode: Int) {
    val licenseResult = SDKAppDataPublic.successfulScanningResult
    val response: WritableMap = Arguments.createMap()
    val mapper = LicenseResultMapper()

    licenseResult?.barcodeData?.let{ barcodeContent ->
      val barcode: WritableMap = Arguments.createMap()
      mapper.parseBarcode(barcodeContent, barcode)
      response.putMap("barcode", barcode)
    }

    licenseResult?.idmrzData?.let { mrzContent ->
      val mrz: WritableMap = Arguments.createMap()
      mapper.parseMrz(mrzContent, mrz)
      response.putMap("mrz", mrz)
    }

    response.putString("type", licenseResult?.documentType?.name ?: "UNKWON")
    promise?.resolve(response)
  }

  override fun onError(requestCode: Int, message: String) {
    promise?.reject(Throwable(message))
  }

  override fun getNormalImageResponse(promise: Promise) {
    Log.d("[SOCURE]", "getNormalImageResponse")
    val licenseResult = SDKAppDataPublic.successfulScanningResult
    val response: WritableMap = Arguments.createMap()

    try {
      licenseResult?.licenseFrontImage?.let {
        response.putString("frontImage", Base64.encodeToString(it, DEFAULT))
      }
      licenseResult?.licenseBackImage?.let {
        response.putString("backImage", Base64.encodeToString(it, DEFAULT))
      }
      response.putString("type", "normal_license_image")

      promise.resolve(response)
    } catch (error: Exception) {
      promise.reject(error)
    }
  }

}

class LicenseResultMapper {

  fun parseBarcode(barcodeContent: BarcodeData, destination: WritableMap) {
    destination.putString("address", barcodeContent.address ?: "")
    destination.putString("city", barcodeContent.city ?: "")
    destination.putString("country", barcodeContent.country ?: "")
    destination.putString("documentNumber", barcodeContent.documentNumber ?: "")
    destination.putString("firstName", barcodeContent.firstName ?: "")
    destination.putString("surName", barcodeContent.surName ?: "")
    destination.putString("fullName", barcodeContent.fullName)
    destination.putString("phone", barcodeContent.phone ?: "")
    destination.putString("postalCode", barcodeContent.postalCode ?: "")
    destination.putString("state", barcodeContent.state ?: "")
    destination.putString("dob", barcodeContent.DOB.toString() ?: "")
  }

  fun parseMrz(mrzContent: MrzData, destination: WritableMap) {
    destination.putString("address", mrzContent.address ?: "")
    destination.putString("city", mrzContent.city ?: "")
    destination.putString("country", mrzContent.issuingCountry ?: "")
    destination.putString("documentNumber", mrzContent.documentNumber ?: "")
    destination.putString("firstName", mrzContent.firstName ?: "")
    destination.putString("surName", mrzContent.surName ?: "")
    destination.putString("fullName", mrzContent.fullName ?: "")
    destination.putString("phone", mrzContent.phone ?: "")
    destination.putString("postalCode", mrzContent.postalCode ?: "")
    destination.putString("state", mrzContent.state ?: "")
    destination.putString("nationality", mrzContent.nationality ?: "")
    destination.putString("sex", mrzContent.sex ?: "")
  }
}
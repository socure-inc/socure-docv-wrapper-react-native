package com.rnsocuresdk

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.Socure
import com.socure.idplus.error.SocureSdkError
import com.socure.idplus.interfaces.Interfaces
import com.socure.idplus.model.ScanResult
import com.socure.idplus.model.SelfieScanResult
import com.socure.idplus.model.UploadResult
import com.socure.idplus.upload.ImageUploader
import com.socure.idplus.util.Constants.DOCUMENTTYPE_LICENSE
import com.socure.idplus.util.Constants.DOCUMENTTYPE_PASSPORT

class InformationUploadModule(private val context: ReactApplicationContext) : Interfaces.UploadCallback {
  private var promise: Promise? = null

  fun upload(promise: Promise) {
    this.promise = promise
    val uploader = buildImageUploader()

    Socure.getSuccessfulResult()?.let { documentResult ->
      val selfieResult = Socure.getSelfieResult()
      startUpload(uploader, documentResult, selfieResult)
    } ?: run {
      promise.reject(Throwable("Nothing to upload!"))
    }
  }
/*
  fun uploadSelfie(documentType: String, promise: Promise) {
    val uploadResult = SDKAppDataPublic.uploadResult
    if(uploadResult != null) {
      if (Socure.getSelfieResult()?.imageData != null) {
        if (documentType == DOCUMENTTYPE_LICENSE || documentType == DOCUMENTTYPE_PASSPORT) {
          val uploader = buildImageUploader()
          uploader.uploadSelfie(this,
              uploadResult.uuid,
              Socure.getSelfieResult()?.imageData,
              documentType)
        } else {
          promise.reject(Throwable("Unknown document type, you have to use 0: license, 1: passport"))
        }
      } else {
        promise.reject(Throwable("No Selfie image found"))
      }
    } else {
      promise.reject(Throwable("You must to upload a document first"))
    }
  }
*/
  private fun buildImageUploader(): ImageUploader = ImageUploader(context)

  private fun startUpload(uploader: ImageUploader, docResult: ScanResult, selfieResult: SelfieScanResult?) {
    if (docResult.documentType == ScanResult.DocumentType.PASSPORT) {
      docResult.passportImage?.let { 
        uploader.uploadPassport(this, it, selfieResult?.imageData)
      } ?: run {
        promise?.reject(Throwable("Passport image can not be empty!"))
      }
    } else {
      docResult.licenseFrontImage?.let { 
        uploader.uploadLicense(this, it, docResult.licenseBackImage, selfieResult?.imageData)
      } ?: run {
        promise?.reject(Throwable("ID Front image can not be empty!"))
      }
    }
  }

  override fun documentUploadFinished(result: UploadResult?) {
    //todo: create a function
    SDKAppDataPublic.uploadResult = result
    val uploadResponse: WritableMap = Arguments.createMap()
    val mapper = UploadResultMapper()

    result?.let {
      mapper.parseUpload(it, uploadResponse)
    }
    uploadResponse.putString("type", "upload_success")

    promise?.resolve(uploadResponse)
  }

  override fun onDocumentUploadError(error: SocureSdkError?) {
    promise?.reject(Throwable(error?.message ?: "Unknown upload error"))
  }

  override fun onSocurePublicKeyError(error: SocureSdkError?) {
    promise?.reject(Throwable(error?.message ?: "Unknown public key error"))
  }
}

class UploadResultMapper {

  fun parseUpload(uploadContent: UploadResult, destination: WritableMap) {
    destination.putString("referenceId", uploadContent.referenceId)
    destination.putString("uuid", uploadContent.uuid)
  }
}
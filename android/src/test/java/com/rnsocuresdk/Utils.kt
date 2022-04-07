package com.rnsocuresdk

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableMap
import com.socure.idplus.model.DocumentDateBarcode
import org.junit.Assert

class PromiseValidator(private val validateBarcode: Boolean, private val validateMrz: Boolean, private val expectedType: String) : Promise {
  override fun resolve(value: Any?) {
    if (value is WritableMap) {
      if (validateBarcode) {
        val address = value.getString("address")
        val city = value.getString("city")
        val country = value.getString("country")
        val documentNumber = value.getString("documentNumber")
        val firstName = value.getString("firstName")
        val surName = value.getString("surName")
        val fullName = value.getString("fullName")
        val phone = value.getString("phone")
        val postalCode = value.getString("postalCode")
        val state = value.getString("state")
        val dob = value.getString("dob")
        Assert.assertEquals("Don", firstName)
        Assert.assertEquals("Smith", surName)
        Assert.assertEquals("Fake Street 123", address)
        Assert.assertEquals("Arkansas", city)
        Assert.assertEquals("Arkansas", country)
        Assert.assertEquals("71630", postalCode)
        Assert.assertEquals("Arkansas", state)
        Assert.assertEquals("Don Smith", fullName)
        Assert.assertEquals("", phone)
        Assert.assertEquals("111111111", documentNumber)
        if(expectedType == "LICENSE") {
          Assert.assertEquals(DocumentDateBarcode(
              year = "2021",
              month = "12",
              day = "1"
          ).toString(), dob)
        }
      }
      if (validateMrz) {
        val address = value.getString("address")
        val city = value.getString("city")
        val country = value.getString("country")
        val documentNumber = value.getString("documentNumber")
        val firstName = value.getString("firstName")
        val surName = value.getString("surName")
        val fullName = value.getString("fullName")
        val phone = value.getString("phone")
        val postalCode = value.getString("postalCode")
        val state = value.getString("state")
        val nationality = value.getString("nationality")
        val sex = value.getString("sex")
        Assert.assertEquals("Don", firstName)
        Assert.assertEquals("Smith", surName)
        Assert.assertEquals("Fake Street 123", address)
        Assert.assertEquals("Arkansas", city)
        Assert.assertEquals("Arkansas", country)
        Assert.assertEquals("71630", postalCode)
        Assert.assertEquals("Arkansas", state)
        Assert.assertEquals("Don Smith", fullName)
        Assert.assertEquals("", phone)
        Assert.assertEquals("111111111", documentNumber)
        Assert.assertEquals("US", nationality)
        Assert.assertEquals("Male", sex)
      }
      val type = value.getString("type")
      Assert.assertEquals(expectedType, type)
    } else {
      throw UnsupportedClassVersionError("Promise received the wrong type")
    }
  }

  override fun reject(code: String?, message: String?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, userInfo: WritableMap) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, userInfo: WritableMap) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(message: String?) {
    TODO("Not yet implemented")
  }

}

class SelfieValidator : Promise {
  override fun resolve(value: Any?) {
    if(value is WritableMap) {
      val image = value.getString("image")

      Assert.assertEquals(0, image?.length)
      val type = value.getString("type")
      Assert.assertEquals("SELFIE", type)
    }
  }

  override fun reject(code: String?, message: String?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, userInfo: WritableMap) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, userInfo: WritableMap) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(message: String?) {
    TODO("Not yet implemented")
  }
}

class ImageUploaderValidator : Promise {
  override fun resolve(value: Any?) {
    if(value is WritableMap) {
      val currentType = value.getString("type")
      val currentReferenceId = value.getString("referenceId")
      val currentUuid = value.getString("uuid")

      Assert.assertEquals("upload_success", currentType)
      Assert.assertEquals("111111", currentReferenceId)
      Assert.assertEquals("222222", currentUuid)
    }
  }

  override fun reject(code: String?, message: String?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(throwable: Throwable?) {
    TODO("Not yet implemented")
  }

  override fun reject(throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, userInfo: WritableMap) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, userInfo: WritableMap) {
    TODO("Not yet implemented")
  }

  override fun reject(code: String?, message: String?, throwable: Throwable?, userInfo: WritableMap?) {
    TODO("Not yet implemented")
  }

  override fun reject(message: String?) {
    TODO("Not yet implemented")
  }

}
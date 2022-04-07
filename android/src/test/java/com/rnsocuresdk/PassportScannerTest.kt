package com.rnsocuresdk

import com.facebook.react.bridge.JavaOnlyMap
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableMap
import com.rnsocuresdk.scanner.PassportResultMapper
import com.rnsocuresdk.scanner.PassportScannerModule
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.model.BarcodeData
import com.socure.idplus.model.DocumentDateBarcode
import com.socure.idplus.model.MrzData
import com.socure.idplus.model.ScanResult
import org.junit.Assert
import org.junit.Test

class PassportScannerTest {

  @Test
  fun `Barcode mapping`() {
    val mapper = JavaOnlyMap()
    val result = buildScanResult(withBarcode = true, withMrzData = false)
    val validator = PromiseValidator(validateBarcode = true, validateMrz = false, expectedType = "PASSPORT")

    PassportResultMapper().parseBarcode(result.barcodeData!!, mapper)
    mapper.putString("type", result.documentType.name)

    validator.resolve(mapper)
  }

  @Test
  fun `MRZ mapping`() {
    val mapper = JavaOnlyMap()
    val result = buildScanResult(withBarcode = false, withMrzData = true)
    val validator = PromiseValidator(validateBarcode = false, validateMrz = true, expectedType = "PASSPORT")

    PassportResultMapper().parseMrz(result.mrzData!!, mapper)
    mapper.putString("type", result.documentType.name)

    validator.resolve(mapper)
  }

  private fun buildScanResult(withBarcode: Boolean, withMrzData: Boolean): ScanResult {
    val result = ScanResult(ScanResult.DocumentType.PASSPORT)
    if(withBarcode) {
      val barcode = BarcodeData(
          firstName = "Don",
          surName = "Smith",
          address = "Fake Street 123",
          city = "Arkansas",
          country = "Arkansas",
          postalCode = "71630",
          state = "Arkansas",
          documentNumber = "111111111",
          issueDate = DocumentDateBarcode(
              year = "2021",
              month = "12",
              day = "1"
          ),
          DOB = DocumentDateBarcode(
              year = "2021",
              month = "12",
              day = "1"
          ),
          expirationDate = DocumentDateBarcode(
              year = "2021",
              month = "12",
              day = "1"
          ),
          phone = ""
      )
      result.barcodeData = barcode
    }
    if (withMrzData) {
      val mrz = MrzData(
          firstName = "Don",
          surName = "Smith",
          address = "Fake Street 123",
          city = "Arkansas",
          issuingCountry = "Arkansas",
          postalCode = "71630",
          state = "Arkansas",
          documentNumber = "111111111",
          nationality = "US",
          sex = "Male",
          dob = DocumentDateBarcode(
              year = "2021",
              month = "12",
              day = "1"
          ),
          expirationDate = DocumentDateBarcode(
              year = "2021",
              month = "12",
              day = "1"
          ),
          phone = ""
      )
      result.mrzData = mrz
    }
    return result
  }

}
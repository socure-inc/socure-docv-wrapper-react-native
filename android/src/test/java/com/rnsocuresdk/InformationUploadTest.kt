package com.rnsocuresdk

import com.facebook.react.bridge.JavaOnlyMap
import com.socure.idplus.model.UploadResult
import org.junit.Test

class InformationUploadTest {

  @Test
  fun `Document upload finished`() {
    val mapper = JavaOnlyMap()
    val validator = ImageUploaderValidator()
    val result = buildScanResult()

    UploadResultMapper().parseUpload(result, mapper)
    mapper.putString("type", "upload_success")

    validator.resolve(mapper)
  }

  private fun buildScanResult(): UploadResult {
    return UploadResult()
        .apply {
          referenceId = "111111"
          uuid = "222222"
        }
  }
}
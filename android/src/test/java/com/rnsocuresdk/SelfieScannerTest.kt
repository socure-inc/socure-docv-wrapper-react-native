package com.rnsocuresdk

import com.rnsocuresdk.scanner.SelfieScannerModule
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.model.SelfieScanResult
import org.junit.Assert
import org.junit.Test

class SelfieScannerTest {

  private fun buildSelfieResult(): SelfieScanResult {
    return SelfieScanResult(
        imageData = ByteArray(10)
    )
  }
}
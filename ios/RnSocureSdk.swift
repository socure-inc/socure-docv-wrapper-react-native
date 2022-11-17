import Foundation
import UIKit
import SocureSdk

@objc enum DocumentType: Int {
  case passport, license
  public typealias RawValue = String

  public var rawValue: RawValue {
    switch self {
      case .passport:
        return "passport"
      case .license:
        return "license"
    }
  }

  public init?(rawValue: RawValue) {
    switch rawValue {
      case "passport":
        self = .passport
      case "license":
        self = .license
      default:
        return nil
    }
  }
}

@objc(RnSocureSdk)
class RnSocureSdk: NSObject, RCTBridgeModule {
  
  let imageUploader = ImageUploader()
  
  var selfieResult: SelfieScanResult?
  var passportResult: DocScanResult?
  var licenseFrontResult: DocScanResult?
  var licenseBackResult: DocScanResult?

  var selfieImageUrl: String?
  var documentBackImageUrl: String?
  var documentFrontImageUrl: String?
  var passportImageUrl: String?

  var selfieCaptureResolve: RCTPromiseResolveBlock?
  var selfieCaptureReject: RCTPromiseRejectBlock?

  var resolveUpload: RCTPromiseResolveBlock?
  var rejectUpload: RCTPromiseRejectBlock?
  
  var docScanResolve: RCTPromiseResolveBlock?
  var docScanReject: RCTPromiseRejectBlock?
  
  var usingPassport:Bool = false
  var scanType:DocumentType = .license
  var referenceViewController: UIViewController?
    
  var scanInfoResult: [String:Any] = [:]
    
    static func moduleName() -> String! {
        return "RnSocureSdk"
    }

    @objc(captureSelfie:rejecter:)
  func captureSelfie(
    _ resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    self.selfieCaptureResolve = resolve
    self.selfieCaptureReject = reject
    
    DispatchQueue.main.async {
        
        let selfieVC = SelfieScannerViewController(ImageCallback: self)
        selfieVC.modalPresentationStyle = .fullScreen
        
        self.referenceViewController = selfieVC
        
        SelfieScannerViewController.requestCameraPermissions { (permissionsGranted) in
            DispatchQueue.main.async {
                let root = RCTPresentedViewController();

                if (permissionsGranted) {
                    
                root?.present(selfieVC, animated: true, completion: nil)
        
                } else {
                    let alertController = UIAlertController(title:
                                "Permission Error", message: "This application requires access to the camera to fuction. Please grant camera permission for the application", preferredStyle: .alert)
                            alertController.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action) in
                                
                                UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!, options: [:], completionHandler: nil)
                    
                            }))
                            
                    root?.present(alertController, animated: true, completion: nil)
                }
            }
        }
    }
  }
  

    @objc(scanLicense:rejecter:)
  func scanLicense(
    _ resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    usingPassport = false

    self.scanDocument(type: .license, resolve: resolve, reject: reject)
  }
  
    @objc(scanPassport:rejecter:)
  func scanPassport(
    _ resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    usingPassport = true

    self.scanDocument(type: .passport, resolve: resolve, reject: reject)
  }
  
    
  func scanDocument(
    type: DocumentType,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    self.docScanResolve = resolve
    self.docScanReject = reject
    self.scanType = type
    self.scanInfoResult = [:]
    
    DispatchQueue.main.async {
        let vc = DocumentScannerViewController(ImageCallback: self, BarcodeCallback: self, MRZCallback: self, mode:(type == .passport) ? .Passport : .LicenseFront)
        vc.modalPresentationStyle = .fullScreen
        self.referenceViewController = vc
        DocumentScannerViewController.requestCameraPermissions { (permissionsGranted) in
            DispatchQueue.main.async {
                let root = RCTPresentedViewController();

                if(permissionsGranted) {
                    root?.present(vc, animated: true, completion: nil)
                } else {
                    let alertController = UIAlertController(title:
                                "Permission Error", message: "This application requires access to the camera to fuction. Please grant camera permission for the application", preferredStyle: .alert)
                            alertController.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action) in
                                
                                UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!, options: [:], completionHandler: nil)
                    
                            }))
                            
                    root?.present(alertController, animated: true, completion: nil)
                }
            }
        }
    }
  }
    
    @objc(uploadScannedInfo:rejecter:)
  func uploadScannedInfo(
    resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
   
    if let selfieUrl = self.selfieImageUrl {
        if let passportUrl = self.passportImageUrl {
            uploadPassportAndSelfie(selfieUrl, front: passportUrl, resolver: resolve, rejecter: reject)
            return
        }
        if let frontUrl = self.documentFrontImageUrl,
            let backUrl = self.documentBackImageUrl {
            uploadLicenseAndSelfie(selfieUrl, front: frontUrl, back: backUrl, resolver: resolve, rejecter: reject)
        } else {
            reject("UPLOAD_ERROR", "Failed to read document from URL", nil)
        }
    } else {
      reject("UPLOAD_ERROR", "Failed to read selfie from URL", nil)
    }
  }
  
  // @objc(uploadSelfie:uploadType:resolve:rejecter:)
  // func uploadSelfie(
  //   selfieUrl: String,
  //   uploadType: Int,
  //   resolve: @escaping RCTPromiseResolveBlock,
  //   rejecter reject: @escaping RCTPromiseRejectBlock
  // ) {
  //   self.resolveUpload = resolve
  //   self.rejectUpload = reject
  //   if let docType = SocureSdk.DocumentTypes.init(rawValue: uploadType) {
  //       if let image = UIImage(contentsOfFile: selfieUrl), let data = image.jpegData(compressionQuality: 1) {
  //         let uploadResult = UploadResult()
  //           imageUploader.uploadSelfie(UploadCallback: self, docUploadResult: uploadResult, docUploadType: docType, selfie: data)
  //       } else {
  //         reject("IMAGE_NOT_FOUND", "Failed to read image from URL", nil)
  //       }
  //   } else {
  //       reject("UPLOAD_TYPE_ERROR", "Upload type not found, you have to send 0: license or 1: passport", nil)
  //   }
  // }
  
    @objc(uploadLicenseAndSelfie:front:back:resolve:rejecter:)
  func uploadLicenseAndSelfie(
    _ selfieUrl: String,
    front frontUrl: String,
    back backUrl: String,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    self.resolveUpload = resolve
    self.rejectUpload = reject
    
    if let selfieData = self.dataFromUrl(url: selfieUrl),
       let backData = self.dataFromUrl(url: backUrl),
       let frontData = self.dataFromUrl(url: frontUrl)
    {
      imageUploader.uploadLicense(UploadCallback: self, front: frontData, back: backData, selfie: selfieData)
    } else {
      reject("IMAGE_NOT_FOUND", "Error getting image data", nil)
    }
  }
    
   
    @objc(uploadPassportAndSelfie:front:resolve:rejecter:)
  func uploadPassportAndSelfie(
    _ selfieUrl: String,
    front frontUrl: String,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    self.resolveUpload = resolve
    self.rejectUpload = reject
    
    if let selfieData = self.dataFromUrl(url: selfieUrl),
       let frontData = self.dataFromUrl(url: frontUrl)
    {
      imageUploader.uploadPassport(UploadCallback: self, front: frontData, selfie: selfieData)
    } else {
      reject("IMAGE_NOT_FOUND", "Error getting image data", nil)
    }
  }
  
  @objc(getScannedLicense:rejecter:)
  func getScannedLicense(
    _ resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    guard let frontImageUrl = self.documentFrontImageUrl else {
        reject("FRONT_IMAGE_NOT_FOUND", "Error getting front image data", nil)
        return
    }
    guard let backImageUrl = self.documentBackImageUrl else {
        reject("BACK_IMAGE_NOT_FOUND", "Error getting back image data", nil)
        return
    }
    
    if let frontImageData = self.dataFromUrl(url: frontImageUrl),
       let backImageData = self.dataFromUrl(url: backImageUrl)
    {
        resolve([
            "frontImage": frontImageData.base64EncodedString(options: .lineLength64Characters),
            "backImage": backImageData.base64EncodedString(options: .lineLength64Characters),
            "type": "normal_license_image"
        ])
    } else {
      reject("IMAGE_NOT_FOUND", "Error getting image data", nil)
    }
  }

  @objc(getScannedPassport:rejecter:)
  func getScannedPassport(
    _ resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    guard let passportUrl = self.passportImageUrl else {
        reject("PASSPORT_IMAGE_NOT_FOUND", "Error getting passport image data", nil)
        return
    }

    if let passportData = self.dataFromUrl(url: passportUrl) {
        resolve([
            "frontImage": passportData.base64EncodedString(options: .lineLength64Characters),
            "type": "normal_passport_image"
        ])
    } else {
      reject("IMAGE_NOT_FOUND", "Error getting image data", nil)
    }
  }
   
    @objc(setSocureSdkKey:)
  func setSocureSdkKey(_ publicKey: String) {
      SDKAppDataPublic.setSocureSdkKey(publicKey)
  }

  // TODO: Move to util
  func saveImage(imageData: Data, name: String) throws -> URL {
    let docsUrl = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    let fileUrl = docsUrl.appendingPathComponent(name)
    try imageData.write(to: fileUrl, options: .atomic)
    return fileUrl
  }
    
  func dataFromUrl(url: String) -> Data? {
    let image = UIImage(contentsOfFile: url)
    return image?.jpegData(compressionQuality: 1)
  }

  @objc static func requiresMainQueueSetup() -> Bool {
    return true
  }
}

extension RnSocureSdk: UploadCallback {
    func documentUploadFinished(uploadResult: UploadResult) {
        self.resolveUpload?([
            "uuid":uploadResult.uuid,
            "referenceId": uploadResult.referenceId,
            "type": "upload_success"
        ])
    }
    
    func onUploadError(errorType: SocureSDKErrorType, errorMessage: String) {
        self.rejectUpload?("IMAGE_UPLOAD_FAILED", errorMessage, nil)
    }

}

extension RnSocureSdk: BarcodeCallback {
  func handleBarcodeData(barcodeData: BarcodeData?) {
    var barcodeDic = [String:String]()
    
    if let barcodeContent = barcodeData {
        barcodeDic["address"] = barcodeContent.address ?? ""
        barcodeDic["city"] =  barcodeContent.city ?? ""
        barcodeDic["documentNumber"] =  barcodeContent.documentNumber ?? ""
        barcodeDic["firstName"] =  barcodeContent.firstName ?? ""
        barcodeDic["surName"] =  barcodeContent.lastName ?? ""
        barcodeDic["fullName"] =  barcodeContent.fullName ?? ""
        barcodeDic["postalCode"] =  barcodeContent.postalCode ?? ""
        barcodeDic["state"] =  barcodeContent.state ?? ""
        barcodeDic["dob"] =  barcodeContent.dob ?? ""
    }
    self.scanInfoResult["barcode"] = barcodeDic
    
    guard let barcodeData = barcodeData else {
      print("Barcode data not found")
      return
    }
    print("Barcode data is \(barcodeData)")
  }
}

extension RnSocureSdk: MRZCallback {
    func handleMRZData(mrzData: MrzData?) {
        var mrzDic = [String:String]()
        
        if let mrzContent = mrzData {
            mrzDic["country"] = mrzContent.issuingCountry
            mrzDic["documentNumber"] = mrzContent.documentNumber
            mrzDic["firstName"] = mrzContent.firstName
            mrzDic["surName"] = mrzContent.surName
            mrzDic["fullName"] = mrzContent.fullName
            mrzDic["nationality"] = mrzContent.nationality
            mrzDic["sex"] = mrzContent.sex ?? ""
            mrzDic["dob"] = mrzContent.dob ?? ""
        }
        self.scanInfoResult["mrz"] = mrzDic
        self.scanInfoResult["type"] = scanType.rawValue
    }
}

extension RnSocureSdk:ImageCallback {

  func selfieCallBack(selfieScanResult: SelfieScanResult) {
    
    if let imageData = selfieScanResult.imageData {
      do  {
        let url = try saveImage(imageData: imageData, name: "selfie.jpg")
        self.selfieImageUrl = url.path
        self.selfieResult = selfieScanResult
        self.selfieCaptureResolve?(url.path)
        self.selfieCaptureReject = nil
        referenceViewController?.dismiss(animated: true, completion: nil)
      } catch {
        self.onError(errorType: SocureSDKErrorType.Error, errorMessage: "Failed to save image data")
      }
    }
  }
  
   func onScanCancelled() {
    referenceViewController?.dismiss(animated: true, completion: {
        self.referenceViewController = nil
        self.docScanReject?("DOC_SCAN_CANCELLED", "DOC_SCAN_CANCELLED", nil)
        self.selfieCaptureReject?("SELFIE_SCAN_CANCELLED", "SELFIE_SCAN_CANCELLED", nil)
        self.docScanReject = nil
        self.docScanResolve = nil
        self.selfieCaptureReject = nil
        self.selfieCaptureResolve = nil
    })
  }
  
  
  func documentBackCallBack(docScanResult: DocScanResult) {
    self.licenseBackResult = docScanResult

    if let backData = docScanResult.imageData, let frontData = licenseFrontResult?.imageData
    {
        do {
            let backUrl = try saveImage(imageData: backData, name: "license-back.jpg")
            let frontUrl = try saveImage(imageData: frontData, name: "license-front.jpg")
            self.documentBackImageUrl = backUrl.path
            self.documentFrontImageUrl = frontUrl.path
            self.docScanResolve?(self.scanInfoResult)
            self.docScanReject = nil
            referenceViewController?.dismiss(animated: true, completion: nil)
          } catch {
            self.onError(errorType: SocureSDKErrorType.Error, errorMessage: "Failed to save image data")
          }
    }
}
  
  func documentFrontCallBack(docScanResult: DocScanResult) {
    self.licenseFrontResult = docScanResult
    if (scanType == .passport) {
      if let imageData = docScanResult.imageData {
        do  {
          let url = try saveImage(imageData: imageData, name: "license-front.jpg")
            self.passportImageUrl = url.path
            self.docScanResolve?(self.scanInfoResult)
            self.docScanReject = nil
            referenceViewController?.dismiss(animated: true, completion: nil)
          
        } catch {
          self.onError(errorType: SocureSDKErrorType.Error, errorMessage: "Failed to save image data")
        }
      }
    }
  }

    
  func onError(errorType: SocureSDKErrorType, errorMessage: String) {
    self.docScanReject?("DOC_SCAN_ERROR", errorMessage, nil)
    self.selfieCaptureReject?("SELFIE_SCAN_ERROR", errorMessage, nil)
    self.referenceViewController?.dismiss(animated: true, completion: nil)
  }
}

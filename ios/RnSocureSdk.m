#import <React/RCTBridgeModule.h>
//#import "React/RCTViewManager.h"
@interface RCT_EXTERN_MODULE(RnSocureSdk, NSObject)

RCT_EXTERN_METHOD(captureSelfie: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)
RCT_EXTERN_METHOD(scanLicense: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)
RCT_EXTERN_METHOD(scanPassport: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)
RCT_EXTERN_METHOD(
                  uploadLicenseAndSelfie: (NSString)selfieUrl
                  front: (NSString)front
                  back: (NSString)back
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  uploadPassportAndSelfie: (NSString)selfieUrl
                  front: (NSString)front
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
// RCT_EXTERN_METHOD(
//                   uploadSelfie: (NSString)selfieUrl
//                   uploadType: (NSString)uploadType
//                   resolver: (RCTPromiseResolveBlock)resolve
//                   rejecter: (RCTPromiseRejectBlock)reject
//                   )                  
RCT_EXTERN_METHOD(getScannedLicense: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)
RCT_EXTERN_METHOD(getScannedPassport: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)
RCT_EXTERN_METHOD(uploadScannedInfo: (RCTPromiseResolveBlock *)resolve rejecter: (RCTPromiseRejectBlock *)reject)
RCT_EXTERN_METHOD(setSocureSdkKey:(NSString)name)

@end

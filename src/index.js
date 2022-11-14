import { NativeEventEmitter, NativeModules } from 'react-native';
const { RnSocureSdk } = NativeModules;

const Socure = {
  scanLicense: () => {
    return RnSocureSdk.scanLicense();
  },
  scanPassport: () => {
    return RnSocureSdk.scanPassport();
  },
  captureSelfie: () => {
    return RnSocureSdk.captureSelfie();
  },
  uploadScannedInfo: () => {
    return RnSocureSdk.uploadScannedInfo();
  },
  getScannedLicense: () => {
    return RnSocureSdk.getScannedLicense();
  },
  getScannedPassport: () => {
    return RnSocureSdk.getScannedPassport();
  },
  setSocureSdkKey: (publicKey) => {
    if (publicKey && typeof publicKey === 'string' && publicKey.trim().length > 0){
      RnSocureSdk.setSocureSdkKey(publicKey);
    } else {
      throw "Invalid public key."
    }
  }
};

export default Socure;

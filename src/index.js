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
};

export default Socure;

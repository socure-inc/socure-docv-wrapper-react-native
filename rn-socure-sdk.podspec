require "json"
package = JSON.parse(File.read(File.join(__dir__, "package.json")))
Pod::Spec.new do |s|
  s.name         = "rn-socure-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]
  s.platforms    = { :ios => "12.0" }
  s.source       = { :git => "https://github.com/socure-inc/socure-docv-wrapper-react-native.git", :tag => "#{s.version}" }
  s.swift_version = "5.0"
  s.source_files = "ios/**/*.{h,m,mm,swift}"
  s.ios.deployment_target = '12.0'

  s.dependency 'React'
  s.dependency 'TrustKit'
  s.dependency 'SocureSdk'

  s.pod_target_xcconfig = { 'ONLY_ACTIVE_ARCH' => 'YES', 'OTHER_LDFLAGS' => '$(inherited) -ObjC' }
end

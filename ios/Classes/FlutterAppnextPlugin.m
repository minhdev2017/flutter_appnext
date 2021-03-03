#import "FlutterAppnextPlugin.h"
#if __has_include(<flutter_appnext/flutter_appnext-Swift.h>)
#import <flutter_appnext/flutter_appnext-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_appnext-Swift.h"
#endif

@implementation FlutterAppnextPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterAppnextPlugin registerWithRegistrar:registrar];
}
@end

#import "FlutterAppnextPlugin.h"

@implementation FlutterAppnextPlugin {
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName: @"appnext_flutter_plugin"
            binaryMessenger:[registrar messenger]];
  FlutterAppnextPlugin* instance = [[FlutterAppnextPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];

}

- (id)init {
  self = [super init];
  if (!self) return nil;

  return self;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {


    if ([@"init" isEqualToString:call.method]) {


    } else {
        result(FlutterMethodNotImplemented);
    }


}

@end



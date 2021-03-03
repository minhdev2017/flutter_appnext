
import 'dart:async';

import 'package:flutter/services.dart';

import 'ad/ad_interstitial.dart';
import 'ad/ad_rewarded.dart';
import 'constants.dart';

class AppnextFlutterPlugin {

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static const _channel = const MethodChannel(MAIN_CHANNEL);

  /// Initializes the AppNext Audience Network. [testingId] can be used to
  /// obtain test Ads.
  ///
  /// [testingId] can be obtained by running the app once without the testingId.
  /// Check the log to obtain the [testingId] for your device.
  static Future<bool> init({String id}) async {
    Map<String, String> initValues = {
      "appKey": id,
    };

    try {
      final result = await _channel.invokeMethod(INIT_METHOD, initValues);
      return result;
    } on PlatformException {
      return false;
    }
  }
  static Future<Null> validateIntegration() async {
    await _channel.invokeMethod('validateIntegration');
  }

  static Future<Null> activityResumed() async {
    await _channel.invokeMethod('onResume');
  }

  static Future<Null> activityPaused() async {
    await _channel.invokeMethod('onPause');
  }

  /// Loads an Interstitial Ad in background. Replace the default [placementId]
  /// with the one which you obtain by signing-up for AppNext Audience Network.
  ///
  /// [listener] passes [InterstitialAdResult] and information associated with
  /// the result to the implemented callback.
  ///
  /// Information will generally be of type Map with details such as:
  ///
  /// ```dart
  /// {
  ///   'placement_id': "YOUR_PLACEMENT_ID",
  ///   'invalidated': false,
  ///   'error_code': 2,
  ///   'error_message': "No internet connection",
  /// }
  /// ```
  static Future<bool> loadInterstitialAd({
    String placementId = "YOUR_PLACEMENT_ID",
    Function(InterstitialAdResult, dynamic) listener,
  }) async {
    return await AppNextInterstitialAd.loadInterstitialAd(
      placementId: placementId,
      listener: listener,
    );
  }



  /// Shows an Interstitial Ad after it has been loaded. (This needs to be called
  /// only after calling [loadInterstitialAd] function). [delay] is in
  /// milliseconds.
  ///
  /// Example:
  ///
  /// ```dart
  /// AppNextAudienceNetwork.loadInterstitialAd(
  ///   listener: (result, value) {
  ///     if (result == InterstitialAdResult.LOADED)
  ///       AppNextAudienceNetwork.showInterstitialAd(delay: 5000);
  ///   },
  /// );
  /// ```
  static Future<bool> showInterstitialAd({int delay}) async {
    return await AppNextInterstitialAd.showInterstitialAd(delay: delay);
  }

  /// Removes the Ad.
  static Future<bool> destroyInterstitialAd() async {
    return await AppNextInterstitialAd.destroyInterstitialAd();
  }

  /// Loads a rewarded video Ad in background. Replace the default [placementId]
  /// with the one which you obtain by signing-up for AppNext Audience Network.
  ///
  /// [listener] passes [RewardedVideoAdResult] and information associated with
  /// the result to the implemented callback.
  ///
  /// Information will generally be of type Map with details such as:
  ///
  /// ```dart
  /// {
  ///   'placement\_id': "YOUR\_PLACEMENT\_ID",
  ///   'invalidated': false,
  ///   'error\_code': 2,
  ///   'error\_message': "No internet connection",
  /// }
  /// ```
  static Future<bool> loadRewardedVideoAd({
    String placementId = "YOUR_PLACEMENT_ID",
    Function(RewardedVideoAdResult, dynamic) listener,
  }) async {
    return await AppNextRewardedVideoAd.loadRewardedVideoAd(
      placementId: placementId,
      listener: listener,
    );
  }

  /// Shows a rewarded video Ad after it has been loaded. (This needs to be
  /// called only after calling [loadRewardedVideoAd] function). [delay] is in
  /// milliseconds.
  ///
  /// Example:
  ///
  /// ```dart
  /// AppNextAudienceNetwork.loadRewardedVideoAd(
  ///   listener: (result, value) {
  ///     if(result == RewardedVideoAdResult.LOADED)
  ///       AppNextAudienceNetwork.showRewardedVideoAd();
  ///   },
  /// );
  /// ```
  static Future<bool> showRewardedVideoAd({int delay = 0}) async {
    return await AppNextRewardedVideoAd.showRewardedVideoAd(delay: delay);
  }

  /// Removes the rewarded video Ad.
  static Future<bool> destroyRewardedVideoAd() async {
    return await AppNextRewardedVideoAd.destroyRewardedVideoAd();
  }
}


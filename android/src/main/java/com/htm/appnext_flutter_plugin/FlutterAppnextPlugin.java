package com.htm.appnext_flutter_plugin;

import android.app.Activity;

import androidx.annotation.NonNull;


import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;

import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterAppnextPlugin */
public class FlutterAppnextPlugin implements  MethodCallHandler, FlutterPlugin, ActivityAware {//
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Activity mActivity;
  private MethodChannel channel;
  private FlutterPluginBinding flutterPluginBinding;


  /*private FlutterAppnextPlugin(Activity activity){
    this.mActivity = activity;
  }*/

  private MethodChannel rewardedAdChannel;
  private MethodChannel interstitialAdChannel;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), AppnextConstants.MAIN_CHANNEL);
    channel.setMethodCallHandler(this);
    this.flutterPluginBinding = flutterPluginBinding;
    /*flutterPluginBinding.getPlatformViewRegistry().registerViewFactory(AppnextConstants.NATIVE_AD_CHANNEL,
            new AppnextNativeAdPlugin(flutterPluginBinding.getBinaryMessenger()));*/



  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    rewardedAdChannel.setMethodCallHandler(null);
    interstitialAdChannel.setMethodCallHandler(null);
    this.flutterPluginBinding = null;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to an Activity
    if(this.mActivity == null){
      this.mActivity = activityPluginBinding.getActivity();
      flutterPluginBinding.getPlatformViewRegistry().registerViewFactory(AppnextConstants.BANNER_AD_CHANNEL,
        new AppnextBannerAdPlugin(flutterPluginBinding.getBinaryMessenger(),mActivity));
      interstitialAdChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),
        AppnextConstants.INTERSTITIAL_AD_CHANNEL);
      interstitialAdChannel
        .setMethodCallHandler(new AppnextInterstitialAdPlugin(this.mActivity,
          interstitialAdChannel));
      // Rewarded video Ad channel
      rewardedAdChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),
        AppnextConstants.REWARDED_VIDEO_CHANNEL);
      rewardedAdChannel
        .setMethodCallHandler(new AppnextRewardedVideoAdPlugin(this.mActivity,
          rewardedAdChannel));
    }

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO: the Activity your plugin was attached to was destroyed to change configuration.
    // This call will be followed by onReattachedToActivityForConfigChanges().
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to a new Activity after a configuration change.
  }

  @Override
  public void onDetachedFromActivity() {
    // TODO: your plugin is no longer associated with an Activity. Clean up references.
  }
  public static void registerWith(Registrar registrar) {

    // Main channel for initialization
    /*final MethodChannel channel = new MethodChannel(registrar.messenger(),
            AppnextConstants.MAIN_CHANNEL);
    channel.setMethodCallHandler(new FlutterAppnextPlugin(registrar.activity()));

    // Interstitial Ad channel
    final MethodChannel interstitialAdChannel = new MethodChannel(registrar.messenger(),
            AppnextConstants.INTERSTITIAL_AD_CHANNEL);
    interstitialAdChannel
            .setMethodCallHandler(new AppnextInterstitialAdPlugin(registrar.context(),
                    interstitialAdChannel));

    // Rewarded video Ad channel
    final MethodChannel rewardedAdChannel = new MethodChannel(registrar.messenger(),
            AppnextConstants.REWARDED_VIDEO_CHANNEL);
    rewardedAdChannel
            .setMethodCallHandler(new AppnextRewardedVideoAdPlugin(registrar.context(),
                    rewardedAdChannel));

    // Banner Ad PlatformView channel
    registrar.
            platformViewRegistry().
            registerViewFactory(AppnextConstants.BANNER_AD_CHANNEL,
                    new AppnextBannerAdPlugin(registrar.messenger()));

    // Native Ad PlatformView channel
    registrar.
            platformViewRegistry().
            registerViewFactory(AppnextConstants.NATIVE_AD_CHANNEL,
                    new AppnextNativeAdPlugin(registrar.messenger()));*/

  }


  @Override
  public void onMethodCall(MethodCall call, Result result) {

    if (call.method.equals(AppnextConstants.INIT_METHOD))
      result.success(init(call.<String>argument("appKey")));
    else if (call.method.equals("validateIntegration")) {
      IntegrationHelper.validateIntegration(mActivity);
      result.success(null);
    }
    else if (call.method.equals("onResume")) {
      IronSource.onResume(mActivity);
      result.success(null);
    } else if (call.method.equals("onPause")) {
      IronSource.onPause(mActivity);
      result.success(null);
    }
    else
      result.notImplemented();
  }

  private boolean init(String key) {
    SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
    IronSource.init(mActivity, key,IronSource.AD_UNIT.BANNER,IronSource.AD_UNIT.INTERSTITIAL,IronSource.AD_UNIT.REWARDED_VIDEO);
    return true;
  }
}

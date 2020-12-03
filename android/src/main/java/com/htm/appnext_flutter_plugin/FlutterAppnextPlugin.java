package com.htm.appnext_flutter_plugin;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.appnext.base.Appnext;

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


  /*private FlutterAppnextPlugin(Activity activity){
    this.mActivity = activity;
  }*/

  private MethodChannel rewardedAdChannel;
  private MethodChannel interstitialAdChannel;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), AppnextConstants.MAIN_CHANNEL);
    channel.setMethodCallHandler(this);
    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory(AppnextConstants.BANNER_AD_CHANNEL,
            new AppnextBannerAdPlugin(flutterPluginBinding.getBinaryMessenger()));
    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory(AppnextConstants.NATIVE_AD_CHANNEL,
            new AppnextNativeAdPlugin(flutterPluginBinding.getBinaryMessenger()));

    interstitialAdChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),
            AppnextConstants.INTERSTITIAL_AD_CHANNEL);
    interstitialAdChannel
            .setMethodCallHandler(new AppnextInterstitialAdPlugin(flutterPluginBinding.getApplicationContext(),
                    interstitialAdChannel));

    // Rewarded video Ad channel
    rewardedAdChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),
            AppnextConstants.REWARDED_VIDEO_CHANNEL);
    rewardedAdChannel
            .setMethodCallHandler(new AppnextRewardedVideoAdPlugin(flutterPluginBinding.getApplicationContext(),
                    rewardedAdChannel));
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    rewardedAdChannel.setMethodCallHandler(null);
    interstitialAdChannel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to an Activity
    this.mActivity = activityPluginBinding.getActivity();
    Appnext.init(mActivity.getApplicationContext());
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
      result.success(init(null));
    else
      result.notImplemented();
  }

  private boolean init(HashMap initValues) {
    Appnext.init(mActivity.getApplicationContext());
    return true;
  }
}

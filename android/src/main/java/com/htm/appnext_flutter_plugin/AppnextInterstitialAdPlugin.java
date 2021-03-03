package com.htm.appnext_flutter_plugin;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class AppnextInterstitialAdPlugin implements MethodChannel.MethodCallHandler, InterstitialListener {


    private Activity context;
    private MethodChannel channel;

    AppnextInterstitialAdPlugin(Context context, MethodChannel channel) {
      this.context = (Activity) context;
      this.channel = channel;


      IronSource.setInterstitialListener(this);
    }


    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

        switch (methodCall.method) {
            case AppnextConstants.SHOW_INTERSTITIAL_METHOD:
                result.success(showAd((HashMap) methodCall.arguments));
                break;
            case AppnextConstants.LOAD_INTERSTITIAL_METHOD:
                result.success(loadAd((HashMap) methodCall.arguments));
                break;
            case AppnextConstants.DESTROY_INTERSTITIAL_METHOD:
                result.success(destroyAd());
                break;
            default:
                result.notImplemented();
        }
    }

  private boolean loadAd(HashMap args) {
    IronSource.loadInterstitial();
    return true;
  }

  private boolean showAd(HashMap args) {
    IronSource.showInterstitial();
    return true;
  }

  private boolean destroyAd() {

    return true;
  }

  @Override
  public void onInterstitialAdClicked() {

  }

  @Override
  public void onInterstitialAdReady() {
    context.runOnUiThread(
      new Runnable() {
        public void run() {
          HashMap<String, Object> args = new HashMap<>();
          args.put("message", "");

          channel.invokeMethod(AppnextConstants.LOADED_METHOD, args);
        }
      }
    );


  }

  @Override
  public void onInterstitialAdLoadFailed(final IronSourceError ironSourceError) {
    context.runOnUiThread(
      new Runnable() {
        public void run() {
          HashMap<String, Object> args = new HashMap<>();
          args.put("error_message", "");

          channel.invokeMethod(AppnextConstants.ERROR_METHOD, args);
        }
      }
    );


    //channel.invokeMethod(AppnextConstants.ERROR_METHOD, args);
  }

  @Override
  public void onInterstitialAdOpened() {


  }

  @Override
  public void onInterstitialAdClosed() {
    context.runOnUiThread(
      new Runnable() {
        public void run() {
          channel.invokeMethod(AppnextConstants.DISMISSED_METHOD, true);
        }
      }
    );

  }

  @Override
  public void onInterstitialAdShowSucceeded() {


  }

  @Override
  public void onInterstitialAdShowFailed(final IronSourceError ironSourceError) {
    /*Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("errorCode", ironSourceError.getErrorCode());
    arguments.put("errorMessage", ironSourceError.getErrorMessage());
    iChannel.invokeMethod(Constants.INTERSTITIAL_SHOW_FAILED, arguments);*/
  }

}

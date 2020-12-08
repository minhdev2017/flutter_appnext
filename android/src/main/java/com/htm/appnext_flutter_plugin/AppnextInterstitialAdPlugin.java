package com.htm.appnext_flutter_plugin;


import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.appnext.ads.fullscreen.FullScreenVideo;
import com.appnext.ads.fullscreen.RewardedVideo;
import com.appnext.ads.interstitial.InterstitialAd;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;
import com.appnext.core.callbacks.OnAdLoaded;
import com.appnext.core.callbacks.OnVideoEnded;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class AppnextInterstitialAdPlugin implements MethodChannel.MethodCallHandler {

    private FullScreenVideo interstitialAd = null;

    private Context context;
    private MethodChannel channel;


    AppnextInterstitialAdPlugin(Context context, MethodChannel channel) {
        this.context = context;
        this.channel = channel;
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
    final String placementId = (String) args.get("id");

    if (interstitialAd == null) {
      interstitialAd = new FullScreenVideo(context, placementId);
    }
    try {
      if (!interstitialAd.isAdLoaded()) {
        interstitialAd.loadAd();
        interstitialAd.setOnAdLoadedCallback(new OnAdLoaded() {
          @Override
          public void adLoaded(String s) {
            HashMap<String, Object> args = new HashMap<>();
            args.put("message", s);

            channel.invokeMethod(AppnextConstants.LOADED_METHOD, args);
          }
        });
        interstitialAd.setOnAdClosedCallback(new OnAdClosed() {
          @Override
          public void onAdClosed() {
            channel.invokeMethod(AppnextConstants.DISMISSED_METHOD, true);
          }
        });

// Get callback for ad error
        interstitialAd.setOnAdErrorCallback(new OnAdError() {
          @Override
          public void adError(String error) {
            HashMap<String, Object> args = new HashMap<>();
            args.put("error_message", error);

            channel.invokeMethod(AppnextConstants.ERROR_METHOD, args);
          }
        });

// Get callback when the user saw the video until the end (video ended)
        interstitialAd.setOnVideoEndedCallback(new OnVideoEnded() {
          @Override
          public void videoEnded() {
            //channel.invokeMethod(AppnextConstants.REWARDED_VIDEO_COMPLETE_METHOD, true);
          }
        });
      }
    } catch (Exception e) {
      Log.e("RewardedVideoAdError", e.getMessage());
      return false;
    }

    return true;
  }

  private boolean showAd(HashMap args) {
    final int delay = (int) args.get("delay");

    if (interstitialAd == null || !interstitialAd.isAdLoaded())
      return false;
    interstitialAd.showAd();
    return true;
  }

  private boolean destroyAd() {
    if (interstitialAd == null)
      return false;
    else {
      interstitialAd.destroy();
      interstitialAd = null;
    }
    return true;
  }


}

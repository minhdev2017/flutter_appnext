package com.htm.appnext_flutter_plugin;


import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.appnext.ads.interstitial.InterstitialAd;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class AppnextInterstitialAdPlugin implements MethodChannel.MethodCallHandler {

    private InterstitialAd interstitialAd = null;

    private Context context;
    private MethodChannel channel;

    private Handler _delayHandler;

    AppnextInterstitialAdPlugin(Context context, MethodChannel channel) {
        this.context = context;
        this.channel = channel;

        _delayHandler = new Handler();
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
        
        return true;
    }

    private boolean showAd(HashMap args) {
        
        return true;
    }

    private boolean destroyAd() {
        
        return true;
    }

    
}

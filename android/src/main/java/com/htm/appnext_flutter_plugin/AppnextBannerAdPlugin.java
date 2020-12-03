package com.htm.appnext_flutter_plugin;

import android.content.Context;
import android.view.View;


import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerListener;
import com.appnext.banners.BannerSize;
import com.appnext.banners.BannerView;
import com.appnext.core.AppnextError;

import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class AppnextBannerAdPlugin extends PlatformViewFactory {

    private final BinaryMessenger messenger;


    AppnextBannerAdPlugin(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        return new AppnextBannerAdView(context, id, (HashMap) args, this.messenger);
    }
}

class AppnextBannerAdView extends BannerListener implements PlatformView {
    private final BannerView adView;
    private final MethodChannel channel;

//    private final boolean isDisposable;

    AppnextBannerAdView(Context context, int id, HashMap args, BinaryMessenger messenger) {

        channel = new MethodChannel(messenger,
                AppnextConstants.BANNER_AD_CHANNEL + "_" + id);
        //4f4ba404-0215-42c0-acff-77de7a45fdc9
        adView = new BannerView(context);
        adView.setPlacementId((String) args.get("id"));
        adView.setBannerSize(getBannerSize(args));
        adView.loadAd(new BannerAdRequest());
        adView.setBannerListener(new BannerListener());
    }

    private BannerSize getBannerSize(HashMap args) {
//        final int width = (int) args.get("width");
        final int height = (int) args.get("height");

        if (height >= 250)
            return BannerSize.MEDIUM_RECTANGLE;
        if (height >= 90)
            return BannerSize.LARGE_BANNER;
        else
            return BannerSize.BANNER;
    }

    @Override
    public View getView() {
        return adView;
    }

    @Override
    public void dispose() {
//        if (adView != null && isDisposable)
//        {
//            Log.d("AppnextBannerAdPlugin", "Banner Ad disposed");
//            adView.destroy();
//        }
    }
    @Override
    public void onError(AppnextError error) {
        super.onError(error);
        HashMap<String, Object> args = new HashMap<>();
        args.put("error_code", "1");
        args.put("error_message", error.getErrorMessage());

        channel.invokeMethod(AppnextConstants.ERROR_METHOD, args);
    }

    @Override
    public void onAdLoaded(String s) {
        super.onAdLoaded(s);
        HashMap<String, Object> args = new HashMap<>();

        channel.invokeMethod(AppnextConstants.LOADED_METHOD, args);
    }

    @Override
    public void adImpression() {
        super.adImpression();
        HashMap<String, Object> args = new HashMap<>();

        channel.invokeMethod(AppnextConstants.LOGGING_IMPRESSION_METHOD, args);
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
        HashMap<String, Object> args = new HashMap<>();
        channel.invokeMethod(AppnextConstants.CLICKED_METHOD, args);
    }


}



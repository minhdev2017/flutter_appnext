package com.htm.appnext_flutter_plugin;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;

import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class AppnextBannerAdPlugin extends PlatformViewFactory {

    private final BinaryMessenger messenger;
  public final Activity iActivity;

    AppnextBannerAdPlugin(BinaryMessenger messenger, Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.iActivity = activity;

    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        return new AppnextBannerAdView(context, id, (HashMap) args, this.messenger, this.iActivity);
    }
}

class AppnextBannerAdView  implements PlatformView, BannerListener {
    private FrameLayout bannerView;
    private final MethodChannel channel;
    private IronSourceBannerLayout bannerLayout;
  private Activity activity;
  private final HashMap args;
//    private final boolean isDisposable;

    AppnextBannerAdView(Context context, int id, HashMap args, BinaryMessenger messenger, Activity activity) {
      this.activity = activity;
        channel = new MethodChannel(messenger,
                AppnextConstants.BANNER_AD_CHANNEL + "_" + id);
      this.args = args;
      bannerView = new FrameLayout(context);
      // choose banner size
      ISBannerSize size = ISBannerSize.BANNER;
      final int height = (int) args.get("height");
      final int width = (int) args.get("height");
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
      // instantiate IronSourceBanner object, using the IronSource.createBanner API
      bannerLayout = IronSource.createBanner(activity, size);
      bannerLayout.setBannerListener(this);
      loadBanner();

    }

    private void loadBanner() {
      if (bannerView.getChildCount() > 0)
        bannerView.removeAllViews();
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT);
      bannerView.addView(
        bannerLayout,0,layoutParams
      );
      bannerView.setVisibility(View.VISIBLE);

      IronSource.loadBanner(bannerLayout);

    }

    @Override
    public View getView() {
        return bannerView;
    }

    @Override
    public void dispose() {
      bannerView.setVisibility(View.INVISIBLE);
      bannerLayout.removeBannerListener();
      IronSource.destroyBanner(bannerLayout);
      channel.setMethodCallHandler(null);
    }
    @Override
    public void onBannerAdLoadFailed(final IronSourceError ironSourceError) {
      activity.runOnUiThread(
        new Runnable() {
          public void run() {
            HashMap<String, Object> args = new HashMap<>();
            args.put("error_code", "1");
            args.put("error_message", ironSourceError.getErrorMessage());

            channel.invokeMethod(AppnextConstants.ERROR_METHOD, args);
          }
        }
      );
    }

    @Override
    public void onBannerAdLoaded() {
      activity.runOnUiThread(
        new Runnable() {
          public void run() {
            HashMap<String, Object> args = new HashMap<>();

            channel.invokeMethod(AppnextConstants.LOADED_METHOD, args);
          }
        }
      );

    }

    @Override
    public void onBannerAdScreenDismissed() {
      activity.runOnUiThread(
        new Runnable() {
          public void run() {
            HashMap<String, Object> args = new HashMap<>();

            channel.invokeMethod(AppnextConstants.LOGGING_IMPRESSION_METHOD, args);
          }
        }
      );
    }

    @Override
    public void onBannerAdClicked() {
      activity.runOnUiThread(
        new Runnable() {
          public void run() {
            HashMap<String, Object> args = new HashMap<>();

            channel.invokeMethod(AppnextConstants.CLICKED_METHOD, args);
          }
        }
      );
    }

  @Override
  public void onBannerAdScreenPresented() {

  }
  @Override
  public void onBannerAdLeftApplication() {

  }


}



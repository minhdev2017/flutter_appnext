package com.htm.appnext_flutter_plugin;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class AppnextRewardedVideoAdPlugin implements MethodChannel.MethodCallHandler, RewardedVideoListener {


    private Activity context;
    private MethodChannel channel;


    AppnextRewardedVideoAdPlugin(Activity context, MethodChannel channel) {
        this.context = context;
        this.channel = channel;
      IronSource.setRewardedVideoListener(this);
    }


    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

        switch (methodCall.method) {
            case AppnextConstants.SHOW_REWARDED_VIDEO_METHOD:
                result.success(showAd((HashMap) methodCall.arguments));
                break;
            case AppnextConstants.LOAD_REWARDED_VIDEO_METHOD:
                result.success(loadAd((HashMap) methodCall.arguments));
                break;
            case AppnextConstants.DESTROY_REWARDED_VIDEO_METHOD:
                result.success(destroyAd());
                break;
            default:
                result.notImplemented();
        }
    }
    private boolean loadAd(HashMap args) {
      /*if(IronSource.isRewardedVideoAvailable()){
        HashMap<String, Object> args1 = new HashMap<>();
        args.put("message", "");

        channel.invokeMethod(AppnextConstants.LOADED_METHOD, args);
      }else{
        HashMap<String, Object> args1 = new HashMap<>();
        args.put("error_message", "");

        channel.invokeMethod(AppnextConstants.ERROR_METHOD, args1);
      }*/

        return true;
    }

    private boolean showAd(HashMap args) {
      if(IronSource.isRewardedVideoAvailable()) {
        IronSource.showRewardedVideo();
        return true;
      }else{
        return false;
      }
    }

    private boolean destroyAd() {

        return true;
    }

  @Override
  public void onRewardedVideoAdOpened() {
  }
  /*Invoked when the RewardedVideo ad view is about to be closed.
  Your activity will now regain its focus.*/
  @Override
  public void onRewardedVideoAdClosed() {
    context.runOnUiThread(
      new Runnable() {
        public void run() {
          channel.invokeMethod(AppnextConstants.REWARDED_VIDEO_CLOSED_METHOD, true);
        }
      }
    );

  }
  /**
   * Invoked when there is a change in the ad availability status.
   *
   * @param - available - value will change to true when rewarded videos are *available.
   *          You can then show the video by calling showRewardedVideo().
   *          Value will change to false when no videos are available.
   */
  @Override
  public void onRewardedVideoAvailabilityChanged(boolean available) {
    //Change the in-app 'Traffic Driver' state according to availability.
  }
  /**
   /**
   * Invoked when the user completed the video and should be rewarded.
   * If using server-to-server callbacks you may ignore this events and wait *for the callback from the ironSource server.
   *
   * @param - placement - the Placement the user completed a video from.
   */
  @Override
  public void onRewardedVideoAdRewarded(Placement placement) {
    context.runOnUiThread(
      new Runnable() {
        public void run() {
          channel.invokeMethod(AppnextConstants.REWARDED_VIDEO_COMPLETE_METHOD, true);
        }
      }
    );
  }
  /* Invoked when RewardedVideo call to show a rewarded video has failed
   * IronSourceError contains the reason for the failure.
   */
  @Override
  public void onRewardedVideoAdShowFailed(IronSourceError error) {
  }
  /*Invoked when the end user clicked on the RewardedVideo ad
   */
  @Override
  public void onRewardedVideoAdClicked(Placement placement){
  }

  @Override
  public void onRewardedVideoAdStarted(){
  }
  /* Invoked when the video ad finishes plating. */
  @Override
  public void onRewardedVideoAdEnded(){
    //channel.invokeMethod(AppnextConstants.REWARDED_VIDEO_E, true);
  }

}

package com.htm.appnext_flutter_plugin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

class AppnextNativeAdView  implements PlatformView {
    private AppnextNativeView adView;



    @Override
    public View getView() {
        return adView;
    }

    @Override
    public void dispose() {

    }

}
class AppnextNativeView extends LinearLayout {
    private ImageView imageView;
    private TextView textView, rating, description;
    private Button button;
    private ArrayList<View> viewArrayList;

    private Context mContext;
    AppnextNativeView(Context context, HashMap args, int typeDef){
        super(context, null, typeDef);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        if ((boolean) args.get("banner_ad")) {
            inflater.inflate(R.layout.native_banner_ads, this, true);
        }else{
            inflater.inflate(R.layout.native_ads, this, true);
        }

    }





}

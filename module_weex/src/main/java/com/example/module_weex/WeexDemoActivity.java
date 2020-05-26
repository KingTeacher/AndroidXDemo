package com.example.module_weex;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.weex.IWXRenderListener;
import org.apache.weex.WXSDKInstance;
import org.apache.weex.common.WXRenderStrategy;

public class WeexDemoActivity extends AppCompatActivity implements IWXRenderListener {
    private static final String TAG = "WeexDemoActivity";
    WXSDKInstance mWXSDKInstance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weexdemo);
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
        String pageName = "WXSample";
        String bundleUrl = "http://dotwe.org/raw/dist/11dd80b1ebc9b7dfbda67a2da971a1c9.bundle.wx";
        mWXSDKInstance.renderByUrl(pageName, bundleUrl, null, null, WXRenderStrategy.APPEND_ASYNC);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        Log.d(TAG,"Week:onViewCreated");
        setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        Log.d(TAG,"Week:onRenderSuccess");
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        Log.d(TAG,"Week:onRefreshSuccess");
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        Log.d(TAG,"Week:onException");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityPause();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityDestroy();
        }
    }
}

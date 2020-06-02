package com.example.module_weex.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.module_weex.adapter.ImageAdapter;

import org.apache.weex.BuildConfig;
import org.apache.weex.InitConfig;
import org.apache.weex.WXEnvironment;
import org.apache.weex.WXSDKEngine;
import org.apache.weex.WXSDKManager;
import org.apache.weex.adapter.DefaultWXHttpAdapter;
import org.apache.weex.bridge.WXBridgeManager;
import org.apache.weex.common.WXException;
import org.apache.weex.performance.WXAnalyzerDataTransfer;

import java.lang.reflect.Method;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        WXBridgeManager.updateGlobalConfig("wson_on");
//        WXEnvironment.setOpenDebugLog(true);
//        WXEnvironment.setApkDebugable(true);
//        WXSDKEngine.addCustomOptions("appName", "WXSample");
//        WXSDKEngine.addCustomOptions("appGroup", "WXApp");
        InitConfig.Builder builder = new InitConfig.Builder()
                .setImgAdapter(new ImageAdapter())
//                .setDrawableLoader(new PicassoBasedDrawableLoader(getApplicationContext()))
//                .setWebSocketAdapterFactory(new DefaultWebSocketAdapterFactory())
//                .setJSExceptionAdapter(new JSExceptionAdapter())
                .setHttpAdapter(new DefaultWXHttpAdapter())
//                .setApmGenerater(new ApmGenerator())
                ;
//        if(!TextUtils.isEmpty(BuildConfig.externalLibraryName)){
//            builder.addNativeLibrary(BuildConfig.externalLibraryName);
//        }
//        WXSDKManager.getInstance().setWxConfigAdapter(new DefaultConfigAdapter());
        WXSDKEngine.initialize(this, builder.build());
//        WXSDKManager.getInstance().addWXAnalyzer(new WXAnalyzerDemoListener());
//        WXAnalyzerDataTransfer.isOpenPerformance = false;
//
//        WXSDKManager.getInstance().setAccessibilityRoleAdapter(new DefaultAccessibilityRoleAdapter());
//
//        try {
//            Fresco.initialize(this);
//            WXSDKEngine.registerComponent("synccomponent", WXComponentSyncTest.class);
//            WXSDKEngine.registerComponent(WXParallax.PARALLAX, WXParallax.class);
//
//            WXSDKEngine.registerModule("render", RenderModule.class);
//            WXSDKEngine.registerModule("event", WXEventModule.class);
//            WXSDKEngine.registerModule("syncTest", SyncTestModule.class);
//
//            WXSDKEngine.registerComponent("mask",WXMask.class);
//            WXSDKEngine.registerModule("myModule", MyModule.class);
//            WXSDKEngine.registerModule("geolocation", GeolocationModule.class);
//
//            WXSDKEngine.registerModule("titleBar", WXTitleBar.class);
//
//            WXSDKEngine.registerModule("wsonTest", WXWsonTestModule.class);
//
//            BindingX.register();

            /**
             * override default image tag
             * WXSDKEngine.registerComponent("image", FrescoImageComponent.class);
             */

            //Typeface nativeFont = Typeface.createFromAsset(getAssets(), "font/native_font.ttf");
            //WXEnvironment.setGlobalFontFamily("bolezhusun", nativeFont);

//            startHeron();

//        } catch (WXException e) {
//            e.printStackTrace();
//        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // The demo code of calling 'notifyTrimMemory()'
                if (false) {
                    // We assume that the application is on an idle time.
                    WXSDKManager.getInstance().notifyTrimMemory();
                }
                // The demo code of calling 'notifySerializeCodeCache()'
                if (false) {
                    WXSDKManager.getInstance().notifySerializeCodeCache();
                }
            }
        });

    }

    /**
     *@param connectable debug server is connectable or not.
     *               if true, sdk will try to connect remote debug server when init WXBridge.
     *
     * @param debuggable enable remote debugger. valid only if host not to be "DEBUG_SERVER_HOST".
     *               true, you can launch a remote debugger and inspector both.
     *               false, you can  just launch a inspector.
     * @param host the debug server host, must not be "DEBUG_SERVER_HOST", a ip address or domain will be OK.
     *             for example "127.0.0.1".
     */
    private void initDebugEnvironment(boolean connectable, boolean debuggable, String host) {
        if (!"DEBUG_SERVER_HOST".equals(host)) {
            WXEnvironment.sDebugServerConnectable = connectable;
            WXEnvironment.sRemoteDebugMode = debuggable;
            WXEnvironment.sRemoteDebugProxyUrl = "ws://" + host + ":8088/debugProxy/native";
        }
    }

    private void startHeron(){
        try{
            Class<?> heronInitClass = getClassLoader().loadClass("com/taobao/weex/heron/picasso/RenderPicassoInit");
            Method method = heronInitClass.getMethod("initApplication", Application.class);
            method.setAccessible(true);
            method.invoke(null,this);
            Log.e("Weex", "Weex Heron Render Init Success");
        }catch (Exception e){
            Log.e("Weex", "Weex Heron Render Mode Not Found", e);
        }
    }
}

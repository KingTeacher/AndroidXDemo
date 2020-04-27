package com.example.module_webview;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

public class JsBridgeActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView111;
    private TextView textView2;
    private BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsbridge);
        initView();
    }

    /**
     * 初始化各种View
     */

    private void initView() {
        textView = findViewById(R.id.activity_jsbridge_textview1);
        textView111 = findViewById(R.id.activity_jsbridge_textview111);
        textView2 = findViewById(R.id.activity_jsbridge_textview2);
        bridgeWebView = findViewById(R.id.activity_jsbridge_bridgewebview);
        bridgeWebView.setWebChromeClient(new myWebChromeClient());
        bridgeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        bridgeWebView.loadUrl("file:///android_asset/myh5.html");

        //Android 通过 JSBridge 调用 JS
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * callHandler方法 Android 调用JS
                 * 参数1 handlerName：JS中规定的方法 Android JS 关于该方法名要一致
                 * 参数2 data：Android传递给JS的参数
                 * 参数3 callBack：回调 JS返回给Android的返回值
                 * */

                bridgeWebView.callHandler("functionInJs", "JS你好，这是我Android传递给你的数据呀！！！", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(JsBridgeActivity.this, data, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //Android 通过 JSBridge 调用 JS 111
        textView111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bridgeWebView.callHandler("functionInJs111", "JS你好，这是我Android传递给你的数据呀！！！111", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(JsBridgeActivity.this, data, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //JS 通过 JSBridge 调用 Android
        bridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //JS传递给Android
                Toast.makeText(JsBridgeActivity.this, data, Toast.LENGTH_LONG).show();
                //Android返回给JS的消息
                function.onCallBack("JS你好，这是我Android传递给你的数据呀！！！");
            }
        });

        //Android 通过 JSBridge 调用 默认JS bridge.init (不需要配置handlerName)
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bridgeWebView.send("Android端通过调用JS默认方法,传递给JS的参数", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        //JS传递给Android
                        Toast.makeText(JsBridgeActivity.this, data, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //JS 通过 JSBridge 调用 Android默认
        bridgeWebView.setDefaultHandler(new DefaultHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                super.handler(data, function);
                Toast.makeText(JsBridgeActivity.this, data, Toast.LENGTH_SHORT).show();
                function.onCallBack("JS 通过 JSBridge 调用 Android默认");
            }
        });
    }

    /**
     * WebChromeClient 实现类
     */

    public class myWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            //创建一个Builder来显示网页中的对话框
            new AlertDialog.Builder(JsBridgeActivity.this)
                    .setTitle("Alert对话框")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setCancelable(false).show();
            return true;
        }
    }

}
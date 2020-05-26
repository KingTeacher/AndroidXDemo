package com.example.module_weex.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.apache.weex.WXSDKManager;
import org.apache.weex.adapter.IWXImgLoaderAdapter;
import org.apache.weex.common.WXImageStrategy;
import org.apache.weex.dom.WXImageQuality;

public class ImageAdapter implements IWXImgLoaderAdapter {
    public ImageAdapter() {
    }

    @Override
    public void setImage(final String url, final ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        WXSDKManager.getInstance().postOnUiThread(new Runnable() {
            public void run() {
                if (view != null && view.getLayoutParams() != null) {
                    if (TextUtils.isEmpty(url)) {
                        view.setImageBitmap((Bitmap)null);
                    } else {
                        String temp = url;
                        if (url.startsWith("//")) {
                            temp = "http:" + url;
                        }

                        if (view.getLayoutParams().width > 0 && view.getLayoutParams().height > 0) {
                            if (view != null && view.getContext() != null) {
                                try {
                                    Glide.with(view.getContext()).load(temp).into(view);
                                } catch (Exception var3) {
                                    var3.printStackTrace();
                                }
                            }

                        }
                    }
                }
            }
        }, 0L);
    }
}

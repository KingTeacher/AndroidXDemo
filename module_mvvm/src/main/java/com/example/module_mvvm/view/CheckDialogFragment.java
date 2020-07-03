package com.example.module_mvvm.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.module_mvvm.databinding.DialogFragmentLayoutBinding;
import com.example.module_mvvm.model.bean.Callback;

public class CheckDialogFragment extends DialogFragment {

    private DialogFragmentLayoutBinding layoutBinding;
    private Callback<String> callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = DialogFragmentLayoutBinding.inflate(inflater,container,false);
        return layoutBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutBinding.btDialogSure.setOnClickListener(view ->{
            String s = layoutBinding.etDialogValue.getText().toString();
            if (callback != null){
                callback.resultCallback(s);
            }
            dismiss();
        });
    }

    public void setCallback(Callback<String> callback) {
        this.callback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            //设置 window 的背景色为透明色.
            //如果通过 window 设置宽高时，想要设置宽为屏宽，就必须调用下面这行代码。
//            window.setBackgroundDrawableResource(R.color.transparent);
            WindowManager.LayoutParams attributes = window.getAttributes();
            //在这里我们可以设置 DialogFragment 弹窗的位置
            attributes.gravity = Gravity.CENTER;
            //我们可以在这里指定 window的宽高
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //设置 DialogFragment 的进出动画
//            attributes.windowAnimations = R.style.DialogAnimation;
            window.setAttributes(attributes);
        }

    }
}

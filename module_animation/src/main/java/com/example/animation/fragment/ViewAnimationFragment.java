package com.example.animation.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animation.R;

public class ViewAnimationFragment extends Fragment implements View.OnClickListener {

    private TextView tvAnimTranslate;
    private TextView tvAnimTranslateCode;
    private TextView tvAnimScale;
    private TextView tvAnimScaleCode;
    private TextView tvAnimRotate;
    private TextView tvAnimRotateCode;
    private TextView tvAnimAlpha;
    private TextView tvAnimAlphaCode;
    private ImageView ivAnimList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_animation,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        //平移动画
        LinearLayout llLayout = view.findViewById(R.id.ll_layout);
        tvAnimTranslate = view.findViewById(R.id.tv_anim_translate);
        tvAnimTranslateCode = view.findViewById(R.id.tv_anim_translate_code);
        tvAnimScale = view.findViewById(R.id.tv_anim_scale);
        tvAnimScaleCode = view.findViewById(R.id.tv_anim_scale_code);
        tvAnimRotate = view.findViewById(R.id.tv_anim_rotate);
        tvAnimRotateCode = view.findViewById(R.id.tv_anim_rotate_code);
        tvAnimAlpha = view.findViewById(R.id.tv_anim_alpha);
        tvAnimAlphaCode = view.findViewById(R.id.tv_anim_alpha_code);
        ivAnimList = view.findViewById(R.id.iv_anim_list);
        tvAnimTranslate.setOnClickListener(this);
        tvAnimTranslateCode.setOnClickListener(this);
        tvAnimScale.setOnClickListener(this);
        tvAnimScaleCode.setOnClickListener(this);
        tvAnimRotate.setOnClickListener(this);
        tvAnimRotateCode.setOnClickListener(this);
        tvAnimAlpha.setOnClickListener(this);
        tvAnimAlphaCode.setOnClickListener(this);
        ivAnimList.setOnClickListener(this);
        //代码实现layoutanimation
        Animation animation=AnimationUtils.loadAnimation(getContext(), R.anim.anim_layout);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);  //设置控件显示的顺序；
        controller.setDelay(0.3f);   //设置控件显示间隔时间；
        //为ViewGroup设置LayoutAnimationController属性；
        llLayout.setLayoutAnimation(controller);
        llLayout.startLayoutAnimation();


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_anim_translate){
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_translate);
            tvAnimTranslate.startAnimation(animation);
        }else if (id == R.id.tv_anim_translate_code){
            TranslateAnimation translateAnimation = new TranslateAnimation(-100f,0f,-100f,0f);
            translateAnimation.setDuration(1000);
            tvAnimTranslateCode.startAnimation(translateAnimation);
        }else if (id == R.id.tv_anim_scale){
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);
            tvAnimScale.startAnimation(animation);
        }else if (id == R.id.tv_anim_scale_code){
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,1f,0.2f,1.5f,20f,20f);
            scaleAnimation.setDuration(1000);
            scaleAnimation.setRepeatCount(3);
            scaleAnimation.setRepeatMode(ScaleAnimation.REVERSE);
            tvAnimScaleCode.startAnimation(scaleAnimation);
        }else if (id == R.id.tv_anim_rotate){
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_rotate);
            tvAnimRotate.startAnimation(animation);
        }else if (id == R.id.tv_anim_rotate_code){
            RotateAnimation rotateAnimation = new RotateAnimation(0f,120f,0f,0f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setFillBefore(true);
            rotateAnimation.setRepeatCount(3);
            rotateAnimation.setRepeatMode(RotateAnimation.RESTART);
            tvAnimRotateCode.startAnimation(rotateAnimation);
        }else if (id == R.id.tv_anim_alpha){
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_alpha);
            tvAnimAlpha.startAnimation(animation);
        }else if (id == R.id.tv_anim_alpha_code){
            AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setFillBefore(true);
            tvAnimAlphaCode.startAnimation(alphaAnimation);
        }else if (id == R.id.iv_anim_list){
            ivAnimList.setImageResource(R.drawable.anim_list);
            AnimationDrawable animationDrawable = (AnimationDrawable)ivAnimList.getDrawable();
            animationDrawable.unscheduleSelf(new Runnable() {
                @Override
                public void run() {
                    Log.d("aaaaa","unscheduleSelf");
                }
            });
            animationDrawable.scheduleSelf(new Runnable() {
                @Override
                public void run() {
                    Log.d("aaaaa","scheduleSelf");
                }
            },0);
            animationDrawable.start();
        }

    }
}

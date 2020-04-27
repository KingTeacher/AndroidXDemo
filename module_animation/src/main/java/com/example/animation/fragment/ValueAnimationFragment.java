package com.example.animation.fragment;

import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animation.R;


public class ValueAnimationFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "ValueAnimationFragment";
    private TextView tvAnimSetWidth2;
    private TextView tvAnimSetWidth3;
    private TextView tvAnimatorSet;
    private TextView tvAnimatorSet2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_value_animation,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        tvAnimSetWidth2 = view.findViewById(R.id.tv_obj_anim_set_width2);
        tvAnimSetWidth3 = view.findViewById(R.id.tv_obj_anim_set_width3);
        tvAnimatorSet = view.findViewById(R.id.tv_obj_anim_animator_set);
        tvAnimatorSet2 = view.findViewById(R.id.tv_obj_animator_set);
        tvAnimSetWidth2.setOnClickListener(this);
        tvAnimSetWidth3.setOnClickListener(this);
        tvAnimatorSet.setOnClickListener(this);
        tvAnimatorSet2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_obj_anim_set_width2){
            /*
            想实现TextView宽度变小的动画遇到的问题，如下：
            由于textview中getWidth()和setWidth()方法是TextView新加的方法，View中没有，而这两个方法不是设置控件的宽度，对应的是xml中的Android：width,不是Android：layout_width，
            所以一下方法没有效果
            ValueAnimator valueAnimator = ObjectAnimator.ofInt(tvAnimSetWidth2,"width",30);

            * 此时可以用一下3中方法解决：
            * 1.给你的对象加上set和get方法，如果你有权限的话；
            * 2.用一个类来包装原始对象，间接提供get和set方法；
            * 3.采用ValueAnimator,监听动画过程，自己实现属性的改变；

            鉴于以上三种方法：使用第二种方法
            * */
//            ObjectAnimator.ofFloat(tvAnimSetWidth2,"rotation",0,360).setDuration(3000).start();
            Log.d(TAG,"getWidth2 value:" + tvAnimSetWidth2.getWidth());
            ObjectAnimator.ofInt(new ViewWrapper(tvAnimSetWidth2),"width",tvAnimSetWidth2.getWidth(),tvAnimSetWidth2.getWidth()+100)
            .setDuration(1000)
            .start();
        } else if (id == R.id.tv_obj_anim_set_width3){
//            鉴于以上三种方法：使用第三种方法

            Log.d(TAG,"getWidth3 value:" + tvAnimSetWidth3.getWidth());
            performAnimate(tvAnimSetWidth3,tvAnimSetWidth3.getWidth(),tvAnimSetWidth3.getWidth() + 100);
        }else if(id == R.id.tv_obj_anim_animator_set){
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(tvAnimatorSet,"rotationX",0,360),//绕x轴旋转
                    ObjectAnimator.ofFloat(tvAnimatorSet,"rotationY",0,360),//绕Y轴旋转
                    ObjectAnimator.ofFloat(tvAnimatorSet,"rotation",0,-360),//绕布局中心点平面旋转
                    ObjectAnimator.ofFloat(tvAnimatorSet,"translationX",0,90,0),
                    ObjectAnimator.ofFloat(tvAnimatorSet,"translationY",0,90,0),
                    ObjectAnimator.ofFloat(tvAnimatorSet,"scaleX",1,1.5f,1),
                    ObjectAnimator.ofFloat(tvAnimatorSet,"scaleY",1,0.5f,1),
                    ObjectAnimator.ofFloat(tvAnimatorSet,"alpha",0.5f,1,0,5,1)

            );
            animatorSet.setDuration(5*1000)
                    .start();

        }else if (id == R.id.tv_obj_animator_set){
            ObjectAnimator rotationX = ObjectAnimator.ofFloat(tvAnimatorSet2, "rotationX", 0, 360);
            rotationX.setRepeatCount(ValueAnimator.INFINITE);
            rotationX.setRepeatMode(ValueAnimator.REVERSE);
            ObjectAnimator rotationY = ObjectAnimator.ofFloat(tvAnimatorSet2, "rotationY", 0, 360);
            rotationY.setRepeatCount(ValueAnimator.INFINITE);
            rotationY.setRepeatMode(ValueAnimator.REVERSE);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(rotationX,rotationY);
            animatorSet.setDuration(3000).start();
        }
    }

    private void performAnimate(final View target, final int start, final int end){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //持有一个IntEvaluator对象，方便下边估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前动画进度值，整形，1~100之间
                int currentValue = (int) animation.getAnimatedValue();
                Log.d(TAG,"current value:" + currentValue);
                //获得当前进度占整个动画过程的比例，浮点型，0~1之间
                float animatedFraction = animation.getAnimatedFraction();
                Log.d(TAG,"animatedFraction value:" + animatedFraction);
                //直接调用整型估值器，通过比例计算出宽度，然后再给view设置
                Integer evaluate = mEvaluator.evaluate(animatedFraction, start, end);
                Log.d(TAG,"width value:" + evaluate);
                target.getLayoutParams().width = evaluate;
                target.requestLayout();
            }
        });
        valueAnimator.setDuration(1000).start();
    }

    class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View view){
            mTarget = view;
        }

        public int getWidth(){
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width){
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}

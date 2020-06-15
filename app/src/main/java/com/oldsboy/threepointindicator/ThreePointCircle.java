package com.oldsboy.threepointindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @ProjectName: Monitor5.0
 * @Package: com.medicine.monitor.view
 * @ClassName: MyCircleIndicator
 * @Description: java类作用描述
 * @Author: 作者名 oldsboy
 * @CreateDate: 2020/6/15 9:17
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/6/15 9:17
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ThreePointCircle extends LinearLayout {
    protected static final String TAG = "myCircleIndicator";

    /**
     * setting
     */
    private int max = 0;
    private float leftRightMargins = 2.5f;
    private float topBottomMargins = 5f;
    private float indicatorSize = 9f;
    private float indicatorChangeRange = 3f;

    private OnIndicatorClickListener onIndicatorClickListener;

    /**
     * interface
     */
    public void setMax(int max){
        this.max = max;
    }

    public void setLeftRightMargins(float leftRightMargins) {
        this.leftRightMargins = leftRightMargins;
    }

    public void setTopBottomMargins(float topBottomMargins) {
        this.topBottomMargins = topBottomMargins;
    }

    public void setIndicatorSize(float indicatorSize) {
        this.indicatorSize = indicatorSize;
    }

    public void setIndicatorChangeRange(float indicatorChangeRange) {
        this.indicatorChangeRange = indicatorChangeRange;
    }

    public void setOnIndicatorClickListener(OnIndicatorClickListener onIndicatorClickListener) {
        this.onIndicatorClickListener = onIndicatorClickListener;
    }

    /**
     * constructor
     */
    public ThreePointCircle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setGravity(Gravity.CENTER);
        this.setPadding(px2dp(18), px2dp(5), px2dp(18), px2dp(5));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值
            width = widthSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            float indicatorWidth = indicatorSize + px2dp(indicatorChangeRange*2) + px2dp(leftRightMargins*2);
            width = (int) (getPaddingLeft() + indicatorWidth + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //  本体 + 幅度*2 + margin
            float indicatorHeight = indicatorSize + px2dp(indicatorChangeRange*2) + px2dp(topBottomMargins*2);
            height = (int) (getPaddingTop() + indicatorHeight + getPaddingBottom());
        }
        //保存测量宽度和测量高度
        setMeasuredDimension(width, height);
    }

    public void select(int index){
        if (index > (max-1)){
            throw new IndexOutOfBoundsException("选择的索引超过了最大值");
        }

        this.removeAllViews();

        if (max == 1){
            ImageView selectView = getSelectView();
            this.addView(selectView);
            startAnimation(selectView);
        }else if (max == 2){
            if (index == 0){
                ImageView selectView = getSelectView();
                this.addView(selectView);
                startAnimation(selectView);

                ImageView normalView1 = getNormalView();
                this.addView(normalView1);
                setLastClickListener(normalView1);
            }else {
                ImageView normalView2 = getNormalView();
                this.addView(normalView2);
                setFirstClickListener(normalView2);

                ImageView selectView = getSelectView();
                this.addView(selectView);
                startAnimation(selectView);
            }
        }else if (max > 2){
            if (index == 0){
                ImageView selectView = getSelectView();
                this.addView(selectView);
                startAnimation(selectView);

                ImageView normalView1 = getNormalView();
                this.addView(normalView1);

                ImageView normalView2 = getNormalView();
                this.addView(normalView2);
                setLastClickListener(normalView2);
            }else if (index == (max-1)){
                ImageView normalView1 = getNormalView();
                this.addView(normalView1);
                setFirstClickListener(normalView1);

                ImageView normalView2 = getNormalView();
                this.addView(normalView2);

                ImageView selectView = getSelectView();
                this.addView(selectView);
                startAnimation(selectView);
            }else {
                ImageView normalView1 = getNormalView();
                this.addView(normalView1);
                setFirstClickListener(normalView1);

                ImageView selectView = getSelectView();
                this.addView(selectView);
                startAnimation(selectView);

                ImageView normalView2 = getNormalView();
                this.addView(normalView2);
                setLastClickListener(normalView2);
            }
        }
    }

    protected void setLastClickListener(ImageView normalView1) {
        if (this.onIndicatorClickListener != null) {
            normalView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIndicatorClickListener.onLastIndicatorClick();
                }
            });
        }
    }

    protected void setFirstClickListener(ImageView selectView){
        if (this.onIndicatorClickListener != null) {
            selectView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIndicatorClickListener.onFirstIndicatorClick();
                }
            });
        }
    }

    protected void startAnimation(final ImageView selectView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(px2dp(indicatorSize), px2dp(indicatorSize)+px2dp(indicatorChangeRange*2), px2dp(indicatorSize)+px2dp(indicatorChangeRange));
        valueAnimator.setRepeatCount(0);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = selectView.getLayoutParams();
                layoutParams.width  = (int)animatedValue;
                layoutParams.height = (int)animatedValue;
                selectView.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    protected ImageView getSelectView() {
        ImageView selectView = getBaseView();
        ViewGroup.LayoutParams layoutParams = selectView.getLayoutParams();
        layoutParams.width += px2dp(indicatorChangeRange);
        layoutParams.height += px2dp(indicatorChangeRange);
        selectView.setImageResource(R.drawable.indicator_select_circle);
        return selectView;
    }

    protected ImageView getNormalView() {
        ImageView selectView = getBaseView();
        selectView.setImageResource(R.drawable.indicator_un_select_circle);
        return selectView;
    }

    protected ImageView getBaseView(){
        ImageView imageView = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(px2dp(indicatorSize), px2dp(indicatorSize));
        layoutParams.setMargins(px2dp(leftRightMargins), px2dp(topBottomMargins), px2dp(leftRightMargins), px2dp(topBottomMargins));
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    protected int px2dp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getContext().getResources().getDisplayMetrics());
    }

    public interface OnIndicatorClickListener{
        void onFirstIndicatorClick();
        void onLastIndicatorClick();
    }
}

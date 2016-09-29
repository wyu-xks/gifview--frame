package com.wy.gifview.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

/**
 * Created by User on 2016/8/15.
 */
public class FrameUtil {

    private static final String TAG = "Main"+FrameUtil.class.getSimpleName();
    private int data[][];
    private Context context;

    private AnimationDrawable currentAnimationDrawable;
    private AnimationDrawable nextAnimationDrawable;
    private ImageView imageview;

    private Drawable drawables[];

    private int duration;
    private Integer totalDrawable;

    private int animatedTiems;
    private int totalAnimationTimes;

    private Boolean canStartNextAnimation;

    private boolean needStopAnimation;

    private  boolean isAnimating;

    /**
     *  初始化帧动画的参数
     * @param context
     * @param data 帧动画的图片资源
     * @param imageView 播放帧动画挂载的view
     * @param duration  每帧延时的时间
     * @param totalTimes 播放的次数
     */
    public FrameUtil(Activity context, int[][] data, ImageView imageView, int duration, int totalTimes){
        this.context = context;
        this.data = data;
        this.imageview = imageView;
        this.duration = duration;
        this.totalAnimationTimes = totalTimes;
        drawables = new Drawable[data[0].length];
        setNextAnimationDrawable(0);
    }


    /**
     * 开始播放动画
     */
    public void startAnimation() {
        isAnimating = true;
        totalDrawable = 0;
        needStopAnimation = false;
        if(null==nextAnimationDrawable){
            canStartNextAnimation = false;
            setNextAnimationDrawable(0);
        }else{
            canStartNextAnimation = true;
        }
        startFrameAnimation(0);
    }
    private void startFrameAnimation(final int currentPosition) {
        if (currentAnimationDrawable != null && currentAnimationDrawable.isRunning()) {
            currentAnimationDrawable.stop();
        }
        if(canStartNextAnimation) {
            currentAnimationDrawable = null;
            currentAnimationDrawable = nextAnimationDrawable;
            nextAnimationDrawable = null;
            if(null!=currentAnimationDrawable)
            imageview.setImageDrawable(currentAnimationDrawable);
            if (needStopAnimation) {
                return;
            }
            currentAnimationDrawable.start();
            canStartNextAnimation = false;

            if (currentPosition + 1 >= data.length) {
                animatedTiems++;
                if (totalAnimationTimes != 0 && totalAnimationTimes <= animatedTiems) {
                    stopAnimation();
                    return;
                }
            }
            final int nextPosition = (currentPosition + 1) % data.length;
            setNextAnimationDrawable(nextPosition);
            imageview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (canStartNextAnimation) {
                        startFrameAnimation(nextPosition);
                    } else {
                        canStartNextAnimation = true;
                    }
                }
            }, data[0].length * duration);
        }else{
            canStartNextAnimation = true;
        }
    }


    private void setNextAnimationDrawable(final int nextPosition) {
        nextAnimationDrawable = new AnimationDrawable();
        nextAnimationDrawable.setOneShot(true);
        totalDrawable = 0;
        for (int i = 0; i < drawables.length; i++) {
            final int position = i;
            ThreadPoolUtils.getInstance().getThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Drawable drawable;
                        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT_WATCH) {
                            drawable = context.getResources().getDrawable(data[nextPosition][position], null);
                        }else{
                            drawable = context.getResources().getDrawable(data[nextPosition][position]);
                        }
                        drawables[position] = drawable;
                        synchronized (totalDrawable) {
                            totalDrawable++;
                            if (totalDrawable >= drawables.length) {
                                setDrawableToAnimationDrawable(nextPosition);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setDrawableToAnimationDrawable(final int nextPosition) {
        for (int i = 0; i < drawables.length; i++) {
            if(null!=drawables[i]) {
                nextAnimationDrawable.addFrame(drawables[i], duration);
            }
        }
        if(canStartNextAnimation){
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startFrameAnimation(nextPosition);
                }
            });
        }else{
            canStartNextAnimation = true;
        }
        clearNext();
    }

    private void clearNext() {
        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = null;
        }
        totalDrawable = 0;
    }

    /**
     * 停止播放动画
     */
    public void stopAnimation(){
        isAnimating = false;
        needStopAnimation = true;
        if(currentAnimationDrawable!=null){
            if(currentAnimationDrawable.isRunning()){
                currentAnimationDrawable.stop();
            }
        }
        currentAnimationDrawable = null;
        nextAnimationDrawable = null;
        totalDrawable = 0;
        animatedTiems = 0;
    }


    public boolean isAnimating() {
        return isAnimating;
    }
}

package com.giousa.catchcrazycat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/7
 * Time:上午9:03
 */

public class Playground extends SurfaceView {

    public Playground(Context context) {
        this(context,null);
    }

    public Playground(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Playground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(mCallback);//将callback对象指定给getHodler
    }


    private void redRaw(){

        //在canvas里面可以绘制
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.CYAN);
        getHolder().unlockCanvasAndPost(canvas);//取消canvas的锁定并将绘图内容更新到界面

    }

    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            redRaw();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };
}

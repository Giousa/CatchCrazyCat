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

    private static final int COL = 10;//行数
    private static final int ROW = 10;//列数
    private static final int BLOCKS = 10;//默认添加的路障数量
    private Dot matrix[][];
    private Dot cat;

    public Playground(Context context) {
        this(context,null);
    }

    public Playground(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Playground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(mCallback);//将callback对象指定给getHodler
        matrix = new Dot[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                matrix[i][j] = new Dot(j,i);
            }
        }

        initGame();
    }

    private Dot getDot(int x,int y){

        return matrix[y][x];
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

    private void initGame(){

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                matrix[i][j].setStatus(Dot.STATUS_OFF);
            }
        }

        cat = new Dot(4,5);
        getDot(4,5).setStatus(Dot.STATUS_IN);
        for (int i = 0; i < BLOCKS;) {

            int x = (int) ((Math.random() * 1000)%COL);
            int y = (int) ((Math.random() * 1000)%ROW);
            System.out.println("x = "+x+" y = "+y );
            if(getDot(x,y).getStatus() == Dot.STATUS_OFF){
                getDot(x,y).setStatus(Dot.STATUS_ON);
                i++;
                System.out.println("Block = "+i);
            }
        }
    }
}

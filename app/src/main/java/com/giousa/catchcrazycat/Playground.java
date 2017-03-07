package com.giousa.catchcrazycat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.Vector;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/7
 * Time:上午9:03
 */

public class Playground extends SurfaceView implements View.OnTouchListener{

    private static int WIDTH = 40;//宽度
    private static final int COL = 10;//行数
    private static final int ROW = 10;//列数
    private static final int BLOCKS = 20;//默认添加的路障数量
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

        setOnTouchListener(this);
        initGame();
    }

    /**
     * 判断是否处于边界
     * @param dot
     * @return
     */
    private boolean isAtEdge(Dot dot){

        if(dot.getX()*dot.getY() == 0 || dot.getX()+1 == COL || dot.getY()+1 == ROW){
            //边界
            return true;
        }

        return false;
    }

    private Dot getNeighbour(Dot one,int dir){

        switch (dir){
            case 1:
                //左边的点
                return getDot(one.getX()-1,one.getY());

            case 2:
                //左上角的点
                if(one.getY() % 2 == 0){
                    return getDot(one.getX()-1,one.getY()-1);

                }else{
                    return getDot(one.getX(),one.getY()-1);
                }

            case 3:
                //右上角的点
                if(one.getY() % 2 == 0){
                    return getDot(one.getX(),one.getY()-1);

                }else{
                    return getDot(one.getX()+1,one.getY()-1);
                }

            case 4:
                //右边的点
                return getDot(one.getX()+1,one.getY());

            case 5:
                //右下角的点
                if(one.getY() % 2 == 0){
                    return getDot(one.getX(),one.getY()+1);

                }else{
                    return getDot(one.getX()+1,one.getY()+1);
                }

            case 6:
                //左下角的点
                if(one.getY() % 2 == 0){
                    return getDot(one.getX()-1,one.getY()+1);

                }else{
                    return getDot(one.getX(),one.getY()+1);
                }

        }

        return null;
    }

    private int getDistance(Dot one,int dir){

        int distance = 0;
        Dot ori = one,next;
        while (true){
            next = getNeighbour(ori, dir);
            if(next.getStatus() == Dot.STATUS_ON){
                return distance*(-1);
            }

            if(isAtEdge(next)){
                distance++;
                return distance;
            }

            distance++;
            ori = next;
        }

    }

    private void moveTo(Dot one){
        one.setStatus(Dot.STATUS_IN);
        getDot(cat.getX(),cat.getY()).setStatus(Dot.STATUS_OFF);
        cat.setXY(one.getX(),one.getY());
    }

    private void move(){
        if(isAtEdge(cat)){
            lose();
            return;
        }

        Vector<Dot> avaliable = new Vector<>();
        for (int i = 1; i < 7; i++) {
            Dot n = getNeighbour(cat, i);
            if(n.getStatus() == Dot.STATUS_OFF){
                avaliable.add(n);
            }
        }

        if(avaliable.size() == 0){
            win();
        }else{
            moveTo(avaliable.get(0));
        }
    }

    private void lose(){
        Toast.makeText(getContext(),"--Lose--",Toast.LENGTH_SHORT).show();
        System.out.println("---lose---");
    }

    private void win(){
        Toast.makeText(getContext(),"--Win--",Toast.LENGTH_SHORT).show();
        System.out.println("---win---");
    }

    private Dot getDot(int x,int y){

        return matrix[y][x];
    }


    private void readRaw(){

        //在canvas里面可以绘制
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.LTGRAY);//浅灰色
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        for (int i = 0; i < COL; i++) {

            int offset = 0;
            if(i % 2 != 0){
                //偶数行
                offset = WIDTH/2;
            }

            for (int j = 0; j < ROW; j++) {
                Dot one = getDot(j, i);
                switch (one.getStatus()){
                    case Dot.STATUS_OFF:
                        paint.setColor(0xffeeeeee);
                        break;

                    case Dot.STATUS_ON:
                        paint.setColor(0xffffaa00);
                        break;

                    case Dot.STATUS_IN:
                        paint.setColor(0xffff0000);//红色
                        break;
                }

                canvas.drawOval(new RectF(one.getX()*WIDTH+offset,one.getY()*WIDTH,
                        (one.getX()+1)*WIDTH+offset,(one.getY()+1)*WIDTH),paint);
            }
        }
        
        getHolder().unlockCanvasAndPost(canvas);//取消canvas的锁定并将绘图内容更新到界面

    }

    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            readRaw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            System.out.println("i = "+i+"  i1 = "+i1+" i2 = "+i2);
            //适配屏幕
            WIDTH = i1/(COL+1);
            readRaw();
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){

            case MotionEvent.ACTION_DOWN:
//                System.out.println("DOWN x = "+motionEvent.getX()+"  y="+motionEvent.getY());
                int x,y;
                y = (int)(motionEvent.getY()/WIDTH);
//                System.out.println("onTouch y = "+y);
                if(y%2 == 0){
                    //奇数行
                    x = (int) (motionEvent.getX()/WIDTH);
//                    System.out.println("onTocuh x = "+x);
                }else{
                    //偶数行
                    x = (int) ((motionEvent.getX() - WIDTH/2)/WIDTH);
                }

                if(x+1 > COL || y+1 > ROW){
                    //超出区域,不能点击,重新初始化
                    initGame();
                }else if(getDot(x,y).getStatus() == Dot.STATUS_OFF){
                    getDot(x,y).setStatus(Dot.STATUS_ON);
                    move();

                }
                readRaw();//不要忘记调用此方法,否则无法更改到主界面
                break;

            case MotionEvent.ACTION_MOVE:
//                System.out.println("MOVE");
                break;

            case MotionEvent.ACTION_UP:
//                System.out.println("UP");
                break;

        }

        return true;
    }
}

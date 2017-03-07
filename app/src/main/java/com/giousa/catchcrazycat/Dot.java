package com.giousa.catchcrazycat;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/7
 * Time:上午9:16
 */

public class Dot {

    private int x,y;
    private int status;

    public static final int STATUS_ON = 1;//红色,路障
    public static final int STATUS_OFF = 2;//灰色,可走
    public static final int STATUS_IN = 3;//黄色,目标

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
        status = STATUS_OFF;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

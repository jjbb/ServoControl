package com.zju.servo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by jjbb_1 on 2016/4/20.
 */
public class ServoView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    public interface DownListener{
        void down(float x, float y);
    }

    public interface DragListener{
        void drag(float x, float y);
    }

    public  interface UpListener{
        void up(float x, float y);
    }
    private DragListener dragListener;
    private DownListener downListener;
    private UpListener upListener;

    private SurfaceHolder holder;
    private float mLsatTouchX;
    private float mLsatTouchY;
    private float destX;
    private float destY;
    public ServoView(Context context){
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread(new ServoPaintThread(surfaceHolder)).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mLsatTouchX = motionEvent.getX();
                mLsatTouchY = motionEvent.getY();
                if(null != downListener) downListener.down(mLsatTouchX,mLsatTouchY);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:   //for multi touch
                break;
            case MotionEvent.ACTION_MOVE:
                destX = (motionEvent.getX() - mLsatTouchX);
                destY = (motionEvent.getY() - mLsatTouchY);
                if(null != dragListener){
                    dragListener.drag(destX, destY);
                }
                return true;
            case MotionEvent.ACTION_UP:
                destX = (motionEvent.getX() - mLsatTouchX);
                destY = (motionEvent.getY() - mLsatTouchY);
                if(null != upListener) upListener.up(destX,destY );
                return true;
        }
        return false;
    }

    public void setDragListener(DragListener listener){
        dragListener = listener;
    }

    public void setDownListener(DownListener listener){
        downListener = listener;
    }

    public void setUpListener(UpListener listener){
        upListener = listener;
    }

    class ServoPaintThread implements Runnable{
        private SurfaceHolder holder;
        public ServoPaintThread(SurfaceHolder holder){
            this.holder = holder;
        }

        @Override
        public void run() {
            Canvas c = holder.lockCanvas();
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            Paint p = new Paint();
            p.setColor(Color.GREEN);
            p.setStyle(Paint.Style.FILL);
           c.drawCircle(15,15,15,p);
            holder.unlockCanvasAndPost(c);
        }
    }

}

package com.zju.Modules;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zju.servo.Coordinate;
import com.zju.servo.ServoAxisPaintThread;
import com.zju.servo.ServoView;

import java.text.DecimalFormat;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class ServoAxisModule implements SurfaceHolder.Callback {
    private Context context;
    private int width = 800;
    private int height = 800;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private double perPX;
    private double perPY;
    private double dataX;
    private double dataY;
    private int pxDataX;
    private int pxDataY;
    private Coordinate coordinate;
    private SurfaceView servoAxisPanel;
    private SurfaceView servo;
    private TextView servoPos;
    RelativeLayout.LayoutParams servoFL;
    private ServoAxisPaintThread axisPaintThread;

    private DecimalFormat df = new DecimalFormat("######0.00");

    public ServoAxisModule(Context context, SurfaceView servoAxisPanel, SurfaceView servo, TextView servoPos) {
        this.context = context;
        this.servoAxisPanel = servoAxisPanel;
        this.servo = servo;
        this.servoPos = servoPos;
        coordinate = new Coordinate();
        coordinate.setMaxX(40);
        coordinate.setMinX(0);
        coordinate.setMaxY(40);
        coordinate.setMinY(0);
        coordinate.setScaleNum(11);
        init();
    }

    public void init(){
        servoAxisPanel.getHolder().addCallback(this);
        RelativeLayout.LayoutParams axisFL = new RelativeLayout.LayoutParams(width, height);
        axisFL.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        axisFL.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        servoAxisPanel.setLayoutParams(axisFL);
        axisPaintThread = new ServoAxisPaintThread(servoAxisPanel.getHolder(), width, height,coordinate);
        int wordLenX = axisPaintThread.getWordLenX();
        int wordLenY = axisPaintThread.getWordLenY();
        minX = wordLenX * 3;
        maxX = width - wordLenX;
        minY = wordLenY * 3;
        maxY = height - wordLenY * 3;
        perPX = (maxX - minX)/(coordinate.getMaxX() - coordinate.getMinX());
        perPY = (maxY - minY)/(coordinate.getMaxY() - coordinate.getMinY());

        servoFL = new RelativeLayout.LayoutParams(50, 50);
        servoFL.setMargins(100,100,21,21);
        servo.setLayoutParams(servoFL);
        servo.setZOrderOnTop(true);
        SurfaceHolder sfHolder =  servo.getHolder() ;
        sfHolder.setFormat(PixelFormat.TRANSLUCENT);
        ServoView sv = new ServoView(context);
        sfHolder.addCallback(new ServoView(context));
        sv.setDragListener(new ServoView.DragListener() {
           @Override
           public void drag(float disX, float disY) {
               int x =(int) (servo.getX() + disX);
               int y = (int) (servo.getY() + disY);
               if( x < minX || x > maxX || y < minY || y > maxY ) {
                   return;
               }
               RelativeLayout.LayoutParams servoFLn = new  RelativeLayout.LayoutParams(50, 50);
               servoFLn.setMargins(x,y,21,21);
               servo.setLayoutParams(servoFLn);
               pxDataX = x;
               pxDataY = y;
               String text = "X:"  + getDataX() + ", Y:" +getDataY();
               servoPos.setText(text);
           }
        });
        sv.setUpListener(new ServoView.UpListener() {
            @Override
            public void up(float disX, float disY) {
                int x =(int) (servo.getX() + disX);
                int y = (int) (servo.getY() + disY);
                if( x < minX || x > maxX || y < minY || y > maxY ) {
                    return;
                }
                pxDataX = x;
                pxDataY = y;
            }
        });
        servo.setOnTouchListener(sv);
    }

    public String getDataX() {
        dataX = (pxDataX - minX) / perPX;
        return df.format(dataX);
    }

    public String getDataY() {
        dataY = (maxY - pxDataY) / perPY;
        return df.format(dataY);
    }


        @Override
    public void surfaceCreated(SurfaceHolder holder) {

        new Thread(axisPaintThread).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

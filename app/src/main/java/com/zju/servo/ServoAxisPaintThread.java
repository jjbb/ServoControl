package com.zju.servo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.zju.servo.Coordinate;

import java.text.DecimalFormat;

/**
 * Created by jjbb_1 on 2016/4/22.
 */
public class ServoAxisPaintThread implements  Runnable{
    private int Width;
    private int Height;
    private int CanWithX = 200;
    private int CanWithY = 180;
    private int wordLenX = 33;
    private int wordLenY = 15;
    private String Title = "机械臂坐标";
    private String xLabel = "(x)";
    private String yLabel = "(y)";
    private int scaleNum = 11;
    private double[] xScale = new double[scaleNum];
    private double[] yScale = new double[scaleNum];
    private float wordTabX;
    private float wordTabY;

    private Bitmap ImageBuffer;

    private DecimalFormat df = new DecimalFormat("######0.00");
    private SurfaceHolder holder;
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;

    public ServoAxisPaintThread(SurfaceHolder holder, int Width, int Height, Coordinate cd){
        this.holder = holder;
        this.Width = Width;
        this.Height = Height;
        CanWithX = Width - wordLenX * 4; // 里面的画布的大小，需要根据外面框的大小与字体的大小进行自动调整
        CanWithY = Height - wordLenY * 6;
        wordTabX = (Width - 4 * wordLenX) / (scaleNum - 1);
        wordTabY = (Height - 6 * wordLenY) / (scaleNum - 1);
        getPrepare(cd);
    }

    {
        for (int i = 0; i < 11; i++) {
            xScale[i] = i;
            yScale[i] = i;
        }
    }

    private void getPrepare(Coordinate cd) {
        this.maxX = cd.getMaxX();
        this.minX = cd.getMinX();
        this.maxY = cd.getMaxY();
        this.minY = cd.getMinY();
        this.scaleNum = cd.getScaleNum();
        double tabX = (maxX - minX) / (scaleNum - 1); // 计算x轴每个刻度的单位偏移量
        double tabY = (maxY - minY) / (scaleNum - 1); // 计算y轴每个刻度的单位偏移量
        for (int i = 0; i < scaleNum; i++) {
            xScale[i] = (minX + i * tabX) ;
            yScale[i] = (minY + i * tabY) ;
        }
    }

    public int getWordLenX(){
        return wordLenX;
    }

    public int getWordLenY(){
        return wordLenY;
    }
    @Override
    public void run() {
        Canvas c = holder.lockCanvas(new Rect(0,0,Width,Height));
        c.drawColor(Color.rgb(0xED,0xED,0xED));
        Paint p = new Paint();
        p.setColor(Color.rgb(0,0,0));
        c.drawText(Title, Width/2,2 * wordLenY, p);
        c.drawText(xLabel, CanWithX / 2 + wordLenX * 3, Height
                - wordLenY,p); // x坐标
        c.drawText(yLabel, wordLenX, CanWithY / 2 + 3 * wordLenY,p); // y坐标
        for (int i = 0; i < scaleNum; i++) {
            c.drawText(df.format((xScale[i])), (int) (wordLenX * 3 + i
                    * wordTabX * 0.97), Height - (int) 2 * wordLenY,p);
            c.drawText(df.format((yScale[i])), (int) 2 * wordLenX, Height
                    - (int) (i * wordTabY  * 0.986 + 3 * wordLenY), p);
        }
        if(null == ImageBuffer) paintAxis();
        Paint mPaint = new Paint();
//            mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        c.drawBitmap(ImageBuffer,3*wordLenX,3*wordLenY,mPaint);
        holder.unlockCanvasAndPost(c);
    }

    public void paintAxis(){
        ImageBuffer = Bitmap.createBitmap(CanWithX,CanWithY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ImageBuffer);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0x03,0x03,0x03));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,CanWithX,CanWithY,paint);
        paint.setColor(Color.RED);
        paint.setColor(Color.rgb(0x94,0x94,0x94));
        paint.setStrokeWidth(1.3f);
        PathEffect effect = new DashPathEffect(new float[]{10, 5, 2, 5},1);
        paint.setPathEffect(effect);
        for (int i = 1; i < scaleNum - 1; i++) {
            canvas.drawLine(i * wordTabX, 0,  i * wordTabX,
                    CanWithY,paint);
            canvas.drawLine(0,  (CanWithY- i * wordTabY),
                    CanWithX, (CanWithY - i * wordTabY),paint);
        }
    }
}

package com.zju.servocontrol;

import android.app.Application;
import android.content.Context;

import com.zju.ConfigTool.ConfigTool;
import com.zju.datatrans.DataTransManager;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class ServoControl extends Application{
    private Application application;
    private Context context;
    private static ServoControl servoControl;
    private static AtomicInteger globalNum = new AtomicInteger(0);  //这个是全局包计数
    private ConfigTool cfgTool;
    private DataTransManager datatrans  ;//数据的传输的管理
    private Thread datatransT;
    private Thread cfgDaemon;
    public final HashMap<String, String> servoControlInfo = new HashMap<String, String>();
    synchronized public static int getGolbalNum(){return globalNum.getAndIncrement();}

    @Override
    public void onCreate(){
        super.onCreate();
        application = this;
        servoControl = this;
        context = getApplicationContext();
        init();
    }

    private void init() {
        servoControlInfo.put("IP","192.168.1.100");
        this.datatrans =  new DataTransManager();  //数据的传输的管理
        this.datatransT = new Thread(this.datatrans,"DataTrans");
        this.datatransT.start();
        this.cfgTool = new ConfigTool();
        this.cfgDaemon = new Thread(this.cfgTool,"ConfigDaemon");
        this.cfgDaemon.setDaemon(true);
		this.cfgDaemon.start();        //开启配置工具
        this.cfgDaemon.setPriority(Thread.MAX_PRIORITY);
    }

    static public ServoControl getInstance(){
        return servoControl;
    }

    public void setServoControlInfo(String key, String value){
        servoControlInfo.put(key,value);
    }

    public String getServoControlInfo(String key){
        return servoControlInfo.get(key);
    }

    public void modifyServoControlInfo(String key, String vlaue){
        servoControlInfo.remove(key);
        servoControlInfo.put(key,vlaue);
    }

    public ConfigTool getCfgTool(){
        return cfgTool;
    }

    public Context getContext(){
        return context;
    }

}

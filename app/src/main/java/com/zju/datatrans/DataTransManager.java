package com.zju.datatrans;

import java.io.IOException;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class DataTransManager implements Runnable{
    private DataManager dataManager = DataManager.getDataManager();
    private SelectConnector seleConn = new SelectConnector(dataManager);

    public DataTransManager(){

    }

    public void run(){
        try {
            seleConn.init();  //初始化，完成连接等工作
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while(true)
        {
            try {
                Thread.sleep(50);                  //时间可以改动,每过500毫秒进行一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                seleConn.doConfiguring();     //完成连接和与服务器交互的功能
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

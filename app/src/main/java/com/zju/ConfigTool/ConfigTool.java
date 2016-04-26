package com.zju.ConfigTool;


import java.io.IOException;
/**
 * 2015-05-21
 * 这个是配置工具，用于配置下位机
 * @author Ake
 *
 */
public class ConfigTool implements Runnable{

    private SelectConnector selChConnector ; //负责连接和网络功能
    private ConfigureHandler cfgHandler;     //负责数据的分发
    private ConfigRspHandle cRspHandle;      //处理返回的数据
    public ConfigTool(){

        cfgHandler = new ConfigureHandler(); //处理配置事物的类

        selChConnector =  new SelectConnector(cfgHandler);

    }

    /**
     * 发送配置文件
     */
    public void config()
    {
        cfgHandler.putWriteBuffer(ConfigOrder.getConfigByte());
    }



    /**
     * 发送配置文件
     */
    public void config(String url)
    {
        cfgHandler.putWriteBuffer(ConfigOrder.getConfigByte(url));

    }
    /**
     * 开始
     */
    public void start()
    {
        cfgHandler.putWriteBuffer(ConfigOrder.StartComm());
    }
    /**
     * 重启
     */
    public void reboot()
    {
        cfgHandler.putWriteBuffer(ConfigOrder.RebootComm());
    }
    /**
     * 暂停
     */
    public void pause()
    {
        cfgHandler.putWriteBuffer(ConfigOrder.PasueComm());
    }
    /**
     * 重新开始（暂停之后）
     */
    public void resume()
    {
        cfgHandler.putWriteBuffer(ConfigOrder.ResumeComm());
    }
    /**
     * 结束
     */
    public void shutdown(){
        cfgHandler.putWriteBuffer(ConfigOrder.ShutDownComm());
    }
    /**
     * 平台状态
     */
    public void sysState(){
        cfgHandler.putWriteBuffer(ConfigOrder.SysStateComm());
    }
    /**
     * 系统状态
     */
    public void osState(){
        cfgHandler.putWriteBuffer(ConfigOrder.OsStateComm());
    }

    //=====================================================================
    //     返回状态处理
    //=====================================================================

    public void handleRsp(byte[] data){
        cRspHandle.handle(data);
    }

    //配置工具
    public void run(){

        //	config();
        //start();


        //shutdown();
        try {
            this.selChConnector.init();  //初始化，完成连接等工作
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
                selChConnector.doConfiguring();     //完成连接和与服务器交互的功能
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

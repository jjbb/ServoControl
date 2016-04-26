package com.zju.datatrans;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class DataSocket {
    private String source ; //本地的地址
    private String target ; //目的的地址
    private DataManager dataManager; //数据管理器
    public DataSocket(String source, String target)
    {
        this.source = source;
        this.target = target;
        this.dataManager = DataManager.getDataManager();
    }
    public String getSource(){return this.source;}
    public String getTarget(){return this.target;}
    //读数据
    public byte[] read(){
        return dataManager.readData(this.source);
    }
    //写数据

    //写数据
    public void write(byte[]data){

        dataManager.writeData(data);
    }
}

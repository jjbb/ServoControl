package com.zju.datatrans;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class DataManager {
    private List writeDataPool = new LinkedList();
    private Map readDataPool = new ConcurrentHashMap(); //线程同步的哈希表

    private static DataManager instance = new DataManager();
    //私有构造函数，使得其不能从外部进行构造
    private DataManager() {
    }

    public static DataManager getDataManager(){
        return instance;
    }

    //Socket写数据

    public void writeData(byte[] data){
        synchronized(writeDataPool){
            writeDataPool.add(data);
        }
    }


    public byte[] getWriteData()
    {
        synchronized (writeDataPool) {
            if(writeDataPool.isEmpty())return null;
            else{
                byte[] data = (byte[])writeDataPool.get(0);//从偏移量为0的地方开始获取数据
                writeDataPool.remove(0);
                return data;
            }


        }
    }

    public void putReadData(String source,byte[] data){
        if(readDataPool.get(source) == null)
        {
            ConcurrentLinkedQueue<byte[]> cq = new ConcurrentLinkedQueue<byte[]>(); //并发队列
            //Stack<byte[]> sk = new Stack<byte[]>();
            //sk.push(data);
            cq.add(data);
            readDataPool.put(source,cq);
            return;
        }
        ConcurrentLinkedQueue<byte[]> cq2 = (ConcurrentLinkedQueue<byte[]>)(readDataPool.get(source));
        cq2.add(data);
        //sk.push(data);
    }

    public byte[] readData(String source){
        if(readDataPool.isEmpty()){return null;}
        if(readDataPool.get(source) != null)
        {

            ConcurrentLinkedQueue<byte[]> cq2 = (ConcurrentLinkedQueue<byte[]>)(readDataPool.get(source));
            return cq2.poll();

        }
        return null;
    }
}

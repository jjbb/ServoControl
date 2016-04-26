package com.zju.PipeDataType;

import java.util.HashMap;

/**
 * Created by jjbb_1 on 2016/4/25.
 */

public class pString extends pipeData{
//    data = new HashMap<String, String>();
    public pString(String portName, String value){
        data.put(portName,value);
    }
    public void addPipeData(String portName, String value){
        data.put(portName,value);
    }

    public Object getPipeData(){
        return data;
    }
    public String getValue(String portName){
        //return ((Double)data).doubleValue();
        return (String)data.get(portName);
    }
}

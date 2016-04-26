package com.zju.PipeDataType;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by jjbb_1 on 2016/4/25.
 */
public abstract class pipeData {
    protected HashMap<String, Object> data = new HashMap<String, Object>();

    public void setPipeData(String portName, Object d){
        data.put(portName,d);
    }
    public Object getPipeData(String portName){
        return data.get(portName);
    }
}

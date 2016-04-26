package com.zju.ReqAndRsp;

import com.zju.ReqAndRsp.partion.DataPackage;
import com.zju.ReqAndRsp.partion.Head;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public abstract class InstrRequest extends DataPackage {


    static public int config = 0;
    static public int comm = 1;              //不同的报文类型在这里改，不使用枚举类。
    static public int get = 2;
    static public int post = 3;
    protected int reqType ;
    public Head head = new Head();

    public byte[] getByteArray(){ return null;};
    public void parseByte2Req(Byte[] data, int offset){};

}

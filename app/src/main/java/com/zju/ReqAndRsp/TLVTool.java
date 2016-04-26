package com.zju.ReqAndRsp;

public class TLVTool extends InstrRequest{


	//这个函数为了得到数据包中的目的地址，但是可能有对于的计算
	public String getDataTarget(byte[] data, int offset){

		offset = this.tagNum.parseTag4TLV(data, offset); //翻译Tag
		offset = parseLengthByte(data,offset);   //取得长度
		int len = lengthNum;                    //保存一下长度，因为只要调用这个方法的时候这个值会被覆盖
		offset = this.head.parseHeadByte(data, offset);   //获取报文头
		return head.RequstTarget;
	}



}

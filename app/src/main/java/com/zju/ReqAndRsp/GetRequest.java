package com.zju.ReqAndRsp;

import com.zju.ReqAndRsp.partion.*;

public class GetRequest extends InstrRequest {

	public byte[] getDoc = new byte[0];   //初始化

	public GetRequest(){
		this.reqType = InstrRequest.get;  //构造函数当中决定了这个请求类型为请求收数据
	}

	public void setGetRequest(Head head, byte[] get){
		this.head = head;
		this.getDoc = get;
	}

	/**
	 * 对请求收数据的字节流进行TLV包装,即添加tag和length
	 * @return
	 */
	public byte[] setGetsTLV(){
		Tag4TLV tag = new Tag4TLV((byte) 2,false,1);
		DataPackage getpkg = new DataPackage(tag,getDoc.length,getDoc);   //对请求收数据的字节流进行T L V包装,即添加tag和length
		return getpkg.getAllDataByte();
	}

	/**
	 * @func 获得请求中的Tlv方法
	 * @return 返回的是转换请求后的字节数组
	 */
	public byte[] getByteArray(){

		int offset = 0;
		//类型不用打包，可以根据数据包的tag进行判断
		byte [] bhead = head.packageHead2Byte();
		byte [] get =setGetsTLV();

		tagNum.setTag4TLV((byte) 2, true, 2);  //请求数据的时候，tagNum为2

		Value = bhead;
		//Value = DataPackage.arrayConcat(bhead, 0, bhead.length, Value);
		//System.arraycopy(bhead, 0,Value, offset, bhead.length);
		offset+= bhead.length;
		Value = DataPackage.arrayConcat(get, 0, get.length, Value);
		this.lengthNum = Value.length;
		//System.arraycopy(get, 0, Value,offset,get.length);
		return getAllDataByte();
	}

	/*
	 * 把字节流数据转换成请求
	 * */
	public void parseByte2Req(byte[] data, int offset){

		offset = this.tagNum.parseTag4TLV(data, offset); //翻译Tag
		offset = parseLengthByte(data,offset);   //取得长度
		int len = lengthNum;                    //保存一下长度，因为只有调用这个方法的时候这个值会被覆盖
		offset = this.head.parseHeadByte(data, offset);   //获取报文头

		DataPackage getPKG = new DataPackage();
		getPKG.praseDataPackage(data, offset);
		this.getDoc = getPKG.Value;
	}

}


	
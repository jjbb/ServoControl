package com.zju.ReqAndRsp;

import com.zju.ReqAndRsp.partion.DataPackage;
import com.zju.ReqAndRsp.partion.Head;

/**
 * @since 2015.4.15
 * @author Fire Lord
 *
 */
public class ConfigResponse extends InstrResponse {

	public byte[] cfgStaCodeDoc = new byte[0];  //初始化
	public byte[] cfgReasonDoc = new byte[0];  //初始化

	public ConfigResponse(){
		this.reqType = InstrResponse.config;  //构造函数当中决定了这个应答类型为配置
	}

	public void setCfgResponse(Head head, int staCode, String reason){
		this.head = head;
		this.cfgStaCodeDoc = Integer.toString(staCode).getBytes();
		this.cfgReasonDoc = reason.getBytes();
	}

	/**
	 * @func 这是将回应的字节流打包成T l v的方法
	 * @return 返回的是转换回应后的T L V字节数组
	 */
	public byte[] getByteArray(){

		//类型不用打包，可以根据数据包的tag进行判断
		byte [] bhead = head.packageHead2Byte();
		byte [] stacode =setStaCodeTLV(cfgStaCodeDoc);
		byte [] reason = setReasonTLV(cfgReasonDoc);

		tagNum.setTag4TLV((byte) 2, true, 10);  //配置回应的时候，tagNum为10

		Value = bhead;
		//Value = DataPackage.arrayConcat(bhead, 0, bhead.length, Value);
		Value = DataPackage.arrayConcat(stacode, 0 , stacode.length, Value);
		Value = DataPackage.arrayConcat(reason, 0, reason.length, Value);
		this.lengthNum = Value.length;

		return getAllDataByte();
	}

	/**
	 * @func 把接收到的字节流解析成应答指令
	 * @param data 字节流数据
	 * @param offset 要解析的数据相对于字节流数据的偏移位置
	 */
	public void parseByte2Res(byte[] data, int offset){

		offset = this.tagNum.parseTag4TLV(data, offset); //翻译Tag
		offset = parseLengthByte(data,offset);   //取得长度
		int len = lengthNum;                    //保存一下长度，因为只要调用这个方法的时候这个值会被覆盖
		offset = this.head.parseHeadByte(data, offset);   //获取报文头

		DataPackage stacodePKG = new DataPackage();
		offset = stacodePKG.praseDataPackage(data, offset);
		this.cfgStaCodeDoc = stacodePKG.Value;

		DataPackage reasonPKG = new DataPackage();
		reasonPKG.praseDataPackage(data, offset);
		this.cfgReasonDoc = reasonPKG.Value;

	}
}
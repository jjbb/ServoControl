package com.zju.ReqAndRsp;

import com.zju.ReqAndRsp.partion.*;



public class ConfigRequest extends InstrRequest
{

	public byte[] xmlDoc = new byte[0];  //初始化

	public ConfigRequest(){
		this.reqType = InstrRequest.config;  //构造函数当中决定了这个请求类型为配置
	}

	public void setCfgRequest(Head head, byte[] xml){
		this.head =head;
		this.xmlDoc =xml;
	}


	/**
	 * 对xml文档中的字节流进行TLV包装,即添加tag和length
	 * @return
	 */
	public byte[] setXMLsTLV(){
		Tag4TLV tag = new Tag4TLV((byte) 2,false,1);
		DataPackage xmlpkg = new DataPackage(tag,xmlDoc.length,xmlDoc);   //对 xml文档进行t l v包装,即添加tag和length
		return xmlpkg.getAllDataByte();

	}


	/**
	 * @func 获得请求中的Tlv方法
	 * @return 返回的是转换请求后的字节数组
	 */
	public byte[] getByteArray(){

		//int offset = 0;
		//类型不用打包，可以根据数据包的tag进行判断
		byte [] bhead = head.packageHead2Byte();
		byte [] xml =setXMLsTLV();

		tagNum.setTag4TLV((byte) 2, true, 0);  //配置请求的时候，tagNum为0

		//Value = DataPackage.arrayConcat(bhead, 0, bhead.length, Value);
		Value = bhead;
		//System.arraycopy(bhead, 0,Value, offset, bhead.length);
		//offset+= bhead.length;
		Value = DataPackage.arrayConcat(xml, 0, xml.length, Value);
		this.lengthNum = Value.length;
		//System.arraycopy(xml, 0, Value,offset,xml.length);
		return getAllDataByte();
	};

	/*
	 * 把字节流数据转换成请求
	 * */
	public void parseByte2Req(byte[] data, int offset){


		offset = this.tagNum.parseTag4TLV(data, offset); //翻译Tag
		offset = parseLengthByte(data,offset);   //取得长度
		int len = lengthNum;                    //保存一下长度，因为只有调用这个方法的时候这个值会被覆盖
		offset = this.head.parseHeadByte(data, offset);   //获取报文头

		DataPackage xmlPKG = new DataPackage();
		xmlPKG.praseDataPackage(data, offset);
		this.xmlDoc = xmlPKG.Value;

	};


}

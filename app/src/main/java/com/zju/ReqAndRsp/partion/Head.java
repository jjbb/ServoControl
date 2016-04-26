package com.zju.ReqAndRsp.partion;

import java.util.Calendar;
/**
 *
 * @author Ake
 *
 */

public class Head extends DataPackage {


	public String RequstSource; //The ID of module
	public String RequstTarget; //The URL of target
	public long Time; //UTC时间
	public int GNumber;  //the number of InstrRequest
	public int PNumber; //the nubmber of InstrRequest in this module;
	public String OtherIfno;

	/**
	 * 构造函数
	 * @param source 报文的来源
	 * @param target 报文的目的地
	 * @param pNum   该报文的编号
	 * @param othr   其他的信息
	 */
	public Head(String source, String target, int pNum, String othr)
	{

		this.RequstSource = source;
		this.RequstTarget = target;
		this.PNumber = pNum;
		this.GNumber = 0;
		Calendar now = Calendar.getInstance();
		this.Time = now.getTimeInMillis();
		this.OtherIfno = othr;
	}
	public Head() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 把信息头的数据都打包成字节数组
	 * @return
	 */

	public byte[] packageHead2Byte(){

		//打包源参数(这个数据包来自哪里)
		Tag4TLV tag =new Tag4TLV((byte) 0,false,29);
		byte[] src = RequstSource.getBytes();
		this.setDataPackage(tag,src.length,src);
		src = this.getAllDataByte();
		//打包目的参数（这个数据包的目的地）
		byte[] tgt = RequstTarget.getBytes();
		this.setDataPackage(tag,tgt.length,tgt);
		tgt = this.getAllDataByte();

		//打包Time时间
		tag.setTag4TLV((byte) 0, false, 23);
		byte[] tm = ByteXData.long2ByteArray(this.Time);
		this.setDataPackage(tag, 8,tm );
		tm = this.getAllDataByte();
		//打包全局计数成TLV格式
		tag.setTag4TLV((byte) 0, false, 2);
		byte[] gn = ByteXData.int2ByteArray(GNumber);
		this.setDataPackage(tag, 4,gn );
		gn = this.getAllDataByte();
		//打包成局部计数秤TLV格式
		byte[] pn = ByteXData.int2ByteArray(PNumber);
		this.setDataPackage(tag,4,pn);
		pn = this.getAllDataByte();
		tag.setTag4TLV((byte) 2, true, 0);
		if(OtherIfno != null)
		{
			tag.setTag4TLV((byte) 0, false, 29);
			byte[] info = OtherIfno.getBytes();
			this.setDataPackage(tag,info.length,info);
			info = this.getAllDataByte();
			Value =packageData(src,tgt,tm,gn,pn,info);
			tag.setTag4TLV((byte) 2, true, 0);
			this.setDataPackage(tag,Value.length);
			return this.getAllDataByte();
		}

		Value =packageData(src,tgt,tm,gn,pn);
		tag.setTag4TLV((byte) 2, true, 0);  //head的tagNum为0
		this.setDataPackage(tag,Value.length);
		return this.getAllDataByte();

	}

	public int parseHeadByte(byte[] data,int offset){

		int start = offset;

		offset = tagNum.parseTag4TLV(data, offset);
		offset = parseLengthByte(data,offset);  //跳过head头的Tag与length
		int len = lengthNum;     //保存一下该数据包的的长度，之后用来判断是否还有其他的信息
		Tag4TLV tag = new Tag4TLV();
		byte[] ans =new byte[0];

		offset = tag.parseTag4TLV(data, offset);
		offset = parseLengthByte(data,offset); //跳过RequestSource的tag和length

		ans = DataPackage.arrayConcat(data, offset, lengthNum, ans);
		offset+=lengthNum;
		RequstSource = new String(ans);    //读取请求

		ans =new byte[0];
		offset = tag.parseTag4TLV(data, offset);
		offset = parseLengthByte(data,offset); //跳过RequstTarget的tag和length
		ans = DataPackage.arrayConcat(data, offset, lengthNum, ans);
		offset+=lengthNum;
		RequstTarget = new String(ans);    //读取请求

		ans =new byte[0];
		offset = tag.parseTag4TLV(data, offset);
		offset = parseLengthByte(data,offset); //跳过Time的tag和length
		ans = DataPackage.arrayConcat(data, offset, lengthNum, ans);
		Time = ByteXData.ByteArray2long(data,offset);
		offset+=lengthNum;

		ans =new byte[0];
		offset = tag.parseTag4TLV(data, offset);
		offset = parseLengthByte(data,offset); //跳过GNumber的tag和length
		ans = DataPackage.arrayConcat(data, offset, lengthNum, ans);
		GNumber = ByteXData.ByteArray2int(data,offset);
		offset+=lengthNum;


		ans =new byte[0];
		offset = tag.parseTag4TLV(data, offset);
		offset = parseLengthByte(data,offset); //跳过PNumber的tag和length
		ans = DataPackage.arrayConcat(data, offset, lengthNum, ans);
		PNumber = ByteXData.ByteArray2int(data,offset);
		offset+=lengthNum;

		if(offset < (start+len))
		{
			ans =new byte[0];
			offset = tag.parseTag4TLV(data, offset);
			offset = parseLengthByte(data,offset); //跳过OtherIfno的tag和length
			ans = DataPackage.arrayConcat(data, offset, lengthNum, ans);
			offset+=lengthNum;
			OtherIfno = new String(ans);    //读取请求
		}
		lengthNum = len;
		return offset;

	}

}

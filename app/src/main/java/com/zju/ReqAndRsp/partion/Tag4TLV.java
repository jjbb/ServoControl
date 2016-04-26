package com.zju.ReqAndRsp.partion;

import java.util.ArrayList;

public class Tag4TLV {

	private byte cls;
	private boolean pc;
	private int tagNum;

	public Tag4TLV()
	{}

	public Tag4TLV(byte cls, boolean pc, int tagNum)
	{
		this.cls = cls;
		this.pc = pc;
		this.tagNum = tagNum;
	}

	public void setTag4TLV(byte cls, boolean pc, int tagNum)
	{
		this.cls = cls;
		this.pc = pc;
		this.tagNum = tagNum;
	}
	/**
	 * @func 把tag4tlv对象序列化成一个字节数组
	 * @param cls class
	 * @param pc p or c
	 * @param tagNum Number of Tags
	 * @return
	 */

	public byte[] getByteTag(byte cls, boolean pc, int tagNum){

		cls<<=6;
		cls|=0x3F;  //0011 1111 to cls11 1111
		if(tagNum <= 30 ){
			byte temp =(byte)tagNum;  //把tag的Num写入，后面会对前面三位进行操作
			temp|=0xE0;               //1110 0000
			if(!pc){temp = (byte)(temp&0xDF);} //1101 1111，把pc位写入
			temp &= cls;  //把class写入
			byte[] ans =new byte[1] ;
			ans[0] = temp;
			return ans;
		}else{
			ArrayList<Byte> ans = new ArrayList<Byte>();
			byte temp =(byte)0xFF;  //把首字节的tag的Num写入 ，也就是固定的11111；后面会对前面三位进行操作
			if(!pc){temp = (byte)(temp&0xDF);} //1101 1111，把pc位写入
			temp &= cls;  //把class写入
			ans.add(new Byte(temp));
			for(;tagNum != 0;)  //改变大小端模式，依次写入TagNum
			{
				temp = (byte)(tagNum&0x7F|0X80);
				ans.add(new Byte(temp));
				tagNum = tagNum>>7;
			}
			byte[] res = new byte[ans.size()];
			int i = 0;
			for(Byte a:ans)res[i++] = a.byteValue();	//遍历ans数组把值给a，然后赋值给res字节数组
			res[i] =(byte) (res[i]&0x7E);   //最后一个字节的最高位变为0
			return res;
		}

	}

	public byte[] getByteTag(){

		byte tmp =cls;
		cls<<=6;
		cls|=0x3F;  //0011 1111 to cls11 1111
		if(tagNum <= 30 ){
			byte temp =(byte)tagNum;  //把tag的Num写入，后面会对前面三位进行操作
			temp|=0xE0;               //1110 0000
			if(!pc){temp = (byte)(temp&0xDF);} //1101 1111，把pc位写入
			temp &= cls;  //把class写入
			byte[] ans =new byte[1] ;
			ans[0] = temp;
			cls =tmp;
			return ans;
		}else{
			ArrayList<Byte> ans = new ArrayList<Byte>();
			byte temp =(byte)0xFF;  //把首字节的tag的Num写入 ，也就是固定的11111；后面会对前面三位进行操作
			if(!pc){temp = (byte)(temp&0xDF);}   //1101 1111，把pc位写入
			temp &= cls;   //把class写入
			ans.add(new Byte(temp));
			for(;tagNum != 0;)   //改变大小端模式，依次写入TagNum
			{
				temp = (byte)(tagNum&0x7F|0X80);
				ans.add(new Byte(temp));
				tagNum = tagNum>>7;
			}
			byte[] res = new byte[ans.size()];
			int i = 0;
			for(Byte a:ans)res[i++] = a.byteValue();		//遍历ans数组把值给a，然后赋值给res字节数组
			res[i] =(byte) (res[i]&0x7E);   //最后一个字节的最高位变为0
			cls=tmp;
			return res;
		}

	}

	/**
	 * 从数据的offset位置开始解析出一个数据的Tag
	 * @param data 输入的数组
	 * @param offset
	 * @return 输出数据的索引往后面偏移了多少
	 */
	public int parseTag4TLV(byte[] data, int offset)
	{
		byte temp = data[offset++];  //先处理第一一个字节
		this.tagNum = temp&0x1F; //0001 1111
		if((temp&0x20) ==0){this.pc = false;}else{this.pc = true;};
		temp&=0xC0;  //1100 0000
		temp= (byte) ((temp&0xFF)>>>6);
		this.cls =temp;
		if(this.tagNum !=0x1F){//0001 11111
			return offset;   //tag只占有一位
		}else{
			temp = data[offset++];
			this.tagNum = 0;
			int i = 0;
			for(;(temp&0x80)!=0;i++) //1000 0000,只要最高位1表示，这个tagNum还没结束
			{
				this.tagNum |= ((int)(temp&0x7E))<<i*8;   // 0111 1111
				temp = data[offset++];
			}
			this.tagNum |= ((int)(temp&0x7E))<<i*8;
			offset++;
			return offset;
		}

	}

	//得到tagNum，为了得到消息的类型以选择不同的解析方式
	public int getTagNum(){
		return tagNum;
	}

}

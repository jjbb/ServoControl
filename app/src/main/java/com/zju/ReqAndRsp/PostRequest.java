package com.zju.ReqAndRsp;

import com.zju.ReqAndRsp.partion.DataPackage;
import com.zju.ReqAndRsp.partion.Head;
import com.zju.ReqAndRsp.partion.Tag4TLV;

public class PostRequest extends InstrRequest {
	public byte[] postDataDoc = new byte[0];   //初始化

	public PostRequest(){
		this.reqType = InstrRequest.post;  //构造函数当中决定了这个请求类型为发送数据
	}

	public void setPostRequest(Head head, byte[] postdata){
		this.head = head;
		this.postDataDoc = postdata;
	}

	/**
	 * 对请求收数据的字节流进行TLV包装,即添加tag和length
	 * @return
	 */
	public byte[] setPostsTLV(){
		Tag4TLV tag = new Tag4TLV((byte) 2,false,1);
		DataPackage postpkg = new DataPackage(tag,postDataDoc.length,postDataDoc);   //对请求收数据的字节流进行T L V包装,即添加tag和length
		return postpkg.getAllDataByte();
	}

	/**
	 * @func 获得请求中的Tlv方法
	 * @return 返回的是转换请求后的字节数组
	 */
	public byte[] getByteArray(){

		int offset = 0;
		//类型不用打包，可以根据数据包的tag进行判断
		byte [] bhead = head.packageHead2Byte();
		byte [] post =setPostsTLV();

		tagNum.setTag4TLV((byte) 2, true, 3);  //发送数据请求的时候，tagNum为3

		Value = bhead;
		//Value = DataPackage.arrayConcat(bhead, 0, bhead.length, Value);
		//System.arraycopy(bhead, 0,Value, offset, bhead.length);
		offset+= bhead.length;
		Value = DataPackage.arrayConcat(post, 0, post.length, Value);
		this.lengthNum = Value.length;
		//System.arraycopy(post, 0, Value,offset,post.length);
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

		DataPackage postPKG = new DataPackage();
		postPKG.praseDataPackage(data, offset);
		this.postDataDoc = postPKG.Value;
	}

}

package com.zju.ReqAndRsp;


import com.zju.ReqAndRsp.partion.DataPackage;
import com.zju.ReqAndRsp.partion.Head;
import com.zju.ReqAndRsp.partion.Tag4TLV;

public class CommRequest extends InstrRequest{

	public byte[] commDoc = new byte[0];  //初始化

	public CommRequest(){
		this.reqType = InstrRequest.comm;  //构造函数当中决定了这个请求类型为控制命令
	}

	public void setCommRequest(Head head, byte[] comm){
		this.head =head;
		this.commDoc =comm;
	};


	/**
	 * 对控制命令请求的字节流进行TLV包装,即添加tag和length
	 * @return
	 */
	public byte[] setCommsTLV(){
		Tag4TLV tag = new Tag4TLV((byte) 2,false,1);
		DataPackage commpkg = new DataPackage(tag,commDoc.length,commDoc);   //对控制命令请求的字节流进行T L V包装,即添加tag和length
		return commpkg.getAllDataByte();

	}


	/**
	 * @func 获得请求中的Tlv方法
	 * @return 返回的是转换请求后的字节数组
	 */
	public byte[] getByteArray(){

		int offset = 0;
		//类型不用打包，可以根据数据包的tag进行判断
		byte [] bhead = head.packageHead2Byte();
		byte [] comm =setCommsTLV();

		tagNum.setTag4TLV((byte) 2, true, 1);  //控制请求的时候，tagNum为1

		Value = bhead;
		//Value = DataPackage.arrayConcat(bhead, 0, bhead.length, Value);
		//System.arraycopy(bhead, 0,Value, offset, bhead.length);
		offset+= bhead.length;
		Value = DataPackage.arrayConcat(comm, 0, comm.length, Value);
		this.lengthNum = Value.length;
		//System.arraycopy(comm, 0, Value,offset,comm.length);
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

		DataPackage commPKG = new DataPackage();
		commPKG.praseDataPackage(data, offset);
		this.commDoc = commPKG.Value;

	};

}

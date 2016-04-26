package com.zju.ReqAndRsp;

import com.zju.ReqAndRsp.partion.DataPackage;
import com.zju.ReqAndRsp.partion.Head;
import com.zju.ReqAndRsp.partion.Tag4TLV;

public abstract class InstrResponse extends DataPackage{

	static public int config = 0;
	static public int comm = 1;              //不同的报文类型在这里改，不使用枚举类。
	static public int get = 2;
	static public int post = 3;
	protected int reqType ;
	public Head head;

	protected  InstrResponse()
	{
		this.head = new Head();
	}
	/**
	 * 对配置应答的状态码的字节流进行TLV包装,即添加tag和length
	 * @return
	 */

	public byte[] setStaCodeTLV(byte[] staCodeDoc){
		Tag4TLV tag = new Tag4TLV((byte) 2,false,1);
		DataPackage stacodepkg = new DataPackage(tag,staCodeDoc.length,staCodeDoc);   //对 配置应答的字节流进行t l v包装,即添加tag和length
		return stacodepkg.getAllDataByte();
	}

	/**
	 * 对配置应答的原因的字节流进行TLV包装,即添加tag和length
	 * @return
	 */
	public byte[] setReasonTLV(byte[] reasonDoc){
		Tag4TLV tag = new Tag4TLV((byte) 2,false,2);
		DataPackage reasonpkg = new DataPackage(tag,reasonDoc.length,reasonDoc);   //对 配置应答的字节流进行t l v包装,即添加tag和length
		return reasonpkg.getAllDataByte();
	}

}

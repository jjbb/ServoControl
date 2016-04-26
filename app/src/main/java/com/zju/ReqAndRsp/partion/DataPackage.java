package com.zju.ReqAndRsp.partion;

import java.util.ArrayList;
import java.util.InputMismatchException;


/**
 *
 * @author Ake
 * @class 该类主要做一些打包与解包TLV数据的作用
 * @统一采用小端模式！
 */
public class DataPackage {

	protected byte[] Tag ;
	protected Tag4TLV tagNum; //用于保存得到类型的编号
	protected byte[] Length;
	protected int lengthNum;
	public byte[] Value = new byte[0];



	public DataPackage(){

		tagNum = new Tag4TLV(); //用于保存得到类型的编号

	};

	public  DataPackage(Tag4TLV tag, int len,byte[] val )
	{
		this.tagNum = tag;
		this.lengthNum = len;
		this.Value = val;
		this.Length = this.getByteLengh();
		this.Tag =this.tagNum.getByteTag();
	}


	/**
	 *
	 * @param arrayBytes 传递给byte数组组合成一个长数组
	 * @return
	 */
	public byte[] packageData(byte[]...arrayBytes){
		byte [] res = new byte[0];
		int len = 0;
		for(byte[] arrayByte:arrayBytes)
		{
			//System.arraycopy(arrayByte,0,res,len,arrayByte.length);
			res = arrayConcat(arrayByte,0,arrayByte.length,res);
			//len += arrayByte.length;
		}
		return res;
	}

	/**
	 * @func 该函数的功能是将源a数组中从offsetA起的lenA个数连接到b数组后面
	 * @param a
	 * @param offsetA
	 * @param b
	 * @param lenA
	 * @return 返回连接后的b数组的长度
	 */

	public static byte[] arrayConcat(byte[] a,int offsetA,int lenA,byte[]b)
	{
		int len = lenA + b.length;
		byte [] c =new byte[len];
		System.arraycopy(b,0,c,0,b.length);
		System.arraycopy(a,offsetA,c,b.length,lenA);
		return c;
	}

	/**
	 * 将TLV中的长度按BER编码
	 * @param len 长度信息
	 * @return将长度信息进行BER编码
	 */
	public byte[] getByteLengh(int len){

		if (len < 127){
			byte[] res =new byte[1];
			System.arraycopy(ByteXData.int2ByteArray(len), 0,res,0,1);
			return res ;}
		else
		{
			ArrayList<Byte> res =  new ArrayList<Byte>();
			byte temp;
			for(;len != 0;)
			{
				temp = (byte)(len&0x7F|0X80);  //0111 1111
				res.add(new Byte(temp));
				len = len>>7;
			}
			byte[] ans = new byte[res.size()];
			int i = -1;                                //2015/05/28修复
			for(Byte a:res)ans[++i] = a.byteValue();
			ans[i] =(byte) (ans[i]&0x7F);   //最后一个字节的最高位变为0
			return ans;
		}
	}

	/**
	 * 将一个字节数组中的length信息读出来
	 * @param data
	 * @param offset
	 * @return
	 */
	public  int parseLengthByte(byte[] data, int offset)
	{
		byte temp = data[offset++];
		int i = 0;
		lengthNum = 0;
		for(;(temp&0x80)!=0;i++)
		{
			lengthNum|= ((int)(temp&0x7F))<<i*7;   // 0111 1111
			temp = data[offset++];
		}
		lengthNum |= ((int)(temp&0x7F))<<i*7;

		return offset;
	}

	/**
	 * 重载函数,用来得到将对象属性中的byte数组
	 * @return
	 */

	public byte[] getByteLengh() {
		return getByteLengh(lengthNum);
	}

	/**
	 * @func将数据T，L，V组合成一个数组。
	 * @return
	 */
	public byte[] getAllDataByte(){

		this.Length = this.getByteLengh();
		this.Tag =this.tagNum.getByteTag();

		if(Value==null)
		{
			throw(new InputMismatchException() );
		}

		return packageData(Tag,Length,Value);
	}







	/**
	 * 将一个TLV的字节数组转换成tag4tlv的对象
	 * @param data
	 * @param offset
	 * @return
	 */

	public int praseDataPackage(byte[] data, int offset){
		offset = this.tagNum.parseTag4TLV(data, offset);
		offset =parseLengthByte(data,offset);
		this.Value = new byte[0];
		//System.arraycopy(data,offset,this.Value, 0, this.lengthNum);
		this.Value =arrayConcat(data,offset,this.lengthNum,this.Value);
		return(offset+this.lengthNum);

	}


	public void setDataPackage(Tag4TLV tag, int len,byte[] val )
	{
		this.tagNum = tag;
		this.lengthNum = len;
		this.Value = val;
		this.Tag = tagNum.getByteTag();
		this.Length = this.getByteLengh();

	}
	public void  setDataPackage(Tag4TLV tag, int len)
	{
		this.tagNum = tag;
		this.lengthNum = len;
	}


	public void setTag(byte[]tag){
		this.Tag = tag;
	}
	public void setLength(byte[]length)
	{
		this.Length = length;
	}
	public void setValue(byte[]value)
	{
		this.Value = value;
	}



}

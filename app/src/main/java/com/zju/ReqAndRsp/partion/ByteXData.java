package com.zju.ReqAndRsp.partion;
/**
 *
 * @author Ake
 *BER编码是大端模式的,但是为了方便Java编程，采用小端模式。
 */
/*该类是为了能够将字节与其他数据的转换*/
public class ByteXData {

	/**
	 * @func 将整数转化为字节数组
	 * @param data 传入需要转成byte数组的整数
	 * @return 转换好的数据
	 */
	public static byte[] int2ByteArray(int data){
		byte[] num ={0,0,0,0};
		for(int i = 0; i <4; i++)
		{
			num[i] = (byte) (0xFF&(data>>i*8));
		}
		return num;
	}

	/**
	 *@func 将字节数组转换为整数
	 * @param src byte数组，
	 * @param offset 数组的偏移量
	 * @return 转换好的整数
	 */
	public static int ByteArray2int(byte[] src,int offset){

		int value = (int) ((src[offset] & 0xFF)
				| ((src[offset+1] & 0xFF)<<8)
				| ((src[offset+2] & 0xFF)<<16)
				| ((src[offset+3] & 0xFF)<<24));

		return value;
	}

	/**
	 * @func 将long整数转化为字节数组
	 * @param data 传入需要转成byte数组long整数
	 * @return 转换好的数据
	 */
	public static byte[] long2ByteArray(long data){
		byte[] num = new byte[8];
		for(int i = 0; i <8; i++)
		{
			num[i] = (byte) (0xFF&(data>>(i*8)));
		}
		return num;
	}





	/**
	 *@func 将字节数组转换为整数
	 * @param src byte数组，
	 * @param offset 数组的偏移量
	 * @return 转换好的整数
	 */
	public static long ByteArray2long(byte[] src,int offset){

		return               (((long) src[offset]) & 0xFF)
				| ((((long) src[offset+1]) & 0xFF)<<8)
				| ((((long) src[offset+2])& 0xFF)<<16)
				| ((((long) src[offset+3]) & 0xFF)<<24)
				| ((((long) src[offset+4]) & 0xFF)<<32)
				| ((((long) src[offset+5]) & 0xFF)<<40)
				| ((((long) src[offset+6])& 0xFF)<<48)
				| ((((long) src[offset+7]) & 0xFF)<<56);

	}


	/**
	 * @func 将double整数转化为字节数组
	 * @param data 传入需要转成byte数组double整数
	 * @return 转换好的数据
	 */
	public static byte[] double2ByteArray(double data){

		return long2ByteArray(Double.doubleToLongBits(data));
	}


	/**
	 *@func 将字节数组转换为整数
	 * @param src byte数组，
	 * @param offset 数组的偏移量
	 * @return 转换好的整数
	 */
	public static double ByteArray2Double(byte[] src,int offset){
		byte [] db = new byte[8];
		System.arraycopy(src, offset, db, 0, 8);
		return Double.longBitsToDouble(  ByteArray2long(db,0));
	}


	/**
	 * byte数组 与boolean之间的转换
	 * @param bl
	 * @return
	 */
	public static boolean Byte2Boolean(byte bl)
	{
		if(bl == 0){return false;}else{return true;}
	}

	public static byte Boolean2Byte(boolean bl)
	{
		if(true == bl)return (byte)5;   //任意不等于的数值就可以了
		return 0;

	}




}
	
	

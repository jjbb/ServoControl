package com.zju.ConfigTool;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ake
 * @since 2015-05-21
 * @class 该类主要是为了处理一些配置的动作
 */
public class ConfigureHandler {


	private List writeBuffer = new LinkedList();   //读写的
	private List readBuffer = new LinkedList();




	//TestHello  测试自发自收的时候使用
	public void sendHello(){
		writeBuffer.add(new String("Hello").getBytes());
	}



	public void putWriteBuffer(byte[] data)
	{
		writeBuffer.add(data);
	}



	//往读缓存中写数据
	public void putReadBuffer(byte[] data)
	{
		readBuffer.add(data);
	}
	//使用这个函数之间必须是需要先调用isWrting函数来检查是否是可以对这个缓存进行读写
	public byte[] getNextWriteBytes()
	{

		byte[] wbuffer = (byte[])writeBuffer.get(0);
		writeBuffer.remove(0);
		return wbuffer;
	}
	//判断buffer是否是有数据要写
	public boolean isWriting(){
		return writeBuffer.isEmpty();
	}
	//判断buffer是否有数据可以读取了
	public boolean isReading(){
		return readBuffer.isEmpty();
	}




}

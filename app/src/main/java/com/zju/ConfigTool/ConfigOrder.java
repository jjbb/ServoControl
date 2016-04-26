package com.zju.ConfigTool;

import java.util.Calendar;

import com.zju.FileIO.xmlFileIO;
import com.zju.ReqAndRsp.CommRequest;
import com.zju.ReqAndRsp.ConfigRequest;
import com.zju.ReqAndRsp.partion.Head;
import com.zju.servocontrol.ServoControl;

/**
 *
 * @author Ake
 * @copyright zju
 */
public final class ConfigOrder {


	private static String url = null;
	private static String comm = "Start";
	private static int partNum = 0;    //这个模块用来计数设置平台这个模块是第几包数据
	private static Head head = new Head();          //配置 工具与守护程序的源地址与目的地址是不变的
	private static Calendar c = Calendar.getInstance();
	static{
		head.RequstSource = "sys";
		head.RequstTarget = "sys";
		head.PNumber = partNum;
		head.Time = c.getTimeInMillis();
	}
	//静态类型的初始化

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//       以下是配置
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * @func:这个类型是获得配置文件，发送
	 * @param l: 指定配置文件的路径，默认的路径是根目录下的 INSTR_INFO文件下。
	 * @return
	 */
	public static byte[] getConfigByte(String l){
		url = l;
		return getConfigByte();
	}

	/**
	 * 这个类是getConfigByte()的实现类
	 * @return 获得tlv封装好的数据包
	 */
	public static byte[] getConfigByte(){
		ConfigRequest cifRequest = new ConfigRequest();
		head.PNumber = partNum ++;    //获得局部的包计数
		head.GNumber = ServoControl.getGolbalNum();
		xmlFileIO.resetPath(url);
		cifRequest.setCfgRequest(head, xmlFileIO.xmlFile2byte());
		return cifRequest.getByteArray();

	}





	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//        以下是命令
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 *
	 * @return 返回 开始命令的字节流
	 */

	//开始任务，下位机开始BOOT
	public static byte[] StartComm(){
		return getCommByte("Start");
	}

	//开始任务，下位机开始BOOT
	public static byte[] RebootComm(){
		return getCommByte("Reboot");
	}

	//关闭任务，发送shutdown
	public static byte[] ShutDownComm(){
		return getCommByte("ShutDown");
	}

	//暂停任务
	public static byte[] PasueComm(){
		return getCommByte("Pasue");
	}

	//恢复任务
	public static byte[] ResumeComm(){
		return getCommByte("Resume");
	}

	//查询平台状态，开还是关
	public static byte[] SysStateComm(){
		return getCommByte("SysState");
	}

	//查询操作系统状态，包括cpu，物理内存，硬盘，以逗号分隔
	public static byte[] OsStateComm(){
		return getCommByte("OsState");
	}


	/**
	 *
	 * @param command  自己定义的字节流，不推荐使用，可能无法识别
	 * @return
	 */

	@Deprecated
	public static byte[] getCommByte(String command){
		comm = command;
		return CommByte();
	}
	/**
	 *
	 * @return 获得tlv封装号的命令包
	 */

	private static byte[] CommByte(){
		CommRequest cmRequest =new CommRequest();
		head.PNumber = partNum ++;
		head.GNumber = ServoControl.getGolbalNum();
		cmRequest.setCommRequest(head, comm.getBytes());
		return cmRequest.getByteArray();
	}

}

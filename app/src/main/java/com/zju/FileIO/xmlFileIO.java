package com.zju.FileIO;

import com.zju.servocontrol.ServoControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.logging.Logger;

/**
 *
 * 对xml文档的读写类
 * @author Ake
 *
 */
public class xmlFileIO {

	private static String path = "./INSTR_INFO/data485.xml";   //默认的路径
	public static void resetPath(String p){path = p;}          //重新设置路径
	public static  String getPath(){return path;}              //获得路径
	private static String LINE_BREAK = "\r\n";

	private static final Logger logger = Logger.getLogger("com.zju.FileIO.xmlFileIO");

	/**
	 * 这个函数是将磁盘中的文档读取出来以后，并把它转换为byte的数组，
	 * 可以应用在上位机发送配置文件的时候。
	 * 上位机把配置文件放在指定的目录下，然后将它转换成byte的数组，通过tlv把数组发送到下位机
	 * @return
	 */
	public static byte[] xmlFile2byte(){

		File file = ServoControl.getInstance().getContext().getExternalFilesDir(path);
		String str = "";
		if(!file.exists()){
			logger.info("尝试读取文件<"+path+">时，发现文件不存在.");
			return null;                                       //如果不存在文件，那么返回null

		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			String strTemp = "";
			try {
				while((strTemp = reader.readLine())!=null){              //逐次的读每一行
					str += strTemp;
					str += LINE_BREAK;
				}
				reader.close();   //关闭
				logger.info("在读取了文件<"+path+">.");
				return str.getBytes();
			} catch (IOException e) {
				logger.info("在读取文件<"+path+">时，发生了错误，可能文件已经被移除.");
				return null;
			}

		} catch (FileNotFoundException e) {
			logger.info("尝试读取文件<"+path+">时，发生了错误，可能文件已经被移除.");
			return null;
		}
	}

	/**
	 * 这个函数的作用是接受byte数组，然后根据byte数组，把它转化成一个字盘的文件，保存起来；
	 * 下位机可以利用这个文件来配置自己的运行的功能。
	 * @return
	 */

	public static int saveByte2xmlFile(byte[] bstream){

		if(null == bstream)return -1;
		File file = new File(path);  //创建文件对象
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.info("尝试创建文件<"+path+">时，发生了错误.");
				return -1;
			}

		}
		try {
			PrintWriter out = new PrintWriter(file);
			out.print(new String(bstream));
			out.close();
			return 1;
		} catch (FileNotFoundException e) {
				logger.info("尝试文件<"+path+">时，发生了错误.");
			return -1;
		}

	}



}

package com.zju.datatrans;

import com.zju.ReqAndRsp.TLVTool;
import com.zju.servocontrol.ServoControl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by jjbb_1 on 2016/4/23.
 */
public class SelectConnector {
    private String tarIP = "192.168.1.100";   //目标服务器的IP
    private int tarPort = 5050; 	//目标服务器的端口
    protected SocketChannel cfgSokcet;
    protected Selector selector = null;
    private boolean initFlag = true;
    //	private ConfigureHandler cfgHandler;

    private DataManager dataManager;
    private Set seleSet;
    private int tryTime = 0;
    private int tryMaxTime = -1; //试验最多的次数，超过这个次数，报超时，-1为无线次数
    private static final Logger logger = Logger.getLogger("com.zju.datatrans.SelectConnector");
    //+++++++++++++++++++++++++++++++++++++++++++++++++
    private ByteBuffer cache = ByteBuffer.allocate(5);
    //	private InstrResponse tool = new InstrResponse();
    TLVTool tlvtool = new TLVTool();
    //构造函数
    public SelectConnector(DataManager dataM)
    {
        this.dataManager = dataM;
    }

    public void setPort(int p) {
        this.tarPort = p;

    }

    public void init() throws IOException {
        tarIP = ServoControl.getInstance().getServoControlInfo("IP");
        selector = Selector.open();
        logger.info("Start init the connector of ConfigTool. "); //日志记录
        logger.info("IP:"+tarIP+" "+"DataPort:"+tarPort); //日志记录
        //localIP = java.net.InetAddress.getLocalHost().getHostAddress(); //本地地址获取

//        cfgSokcet.connect(new InetSocketAddress(tarIP, tarPort));//请求
        //cfgHandler.sendHello();
    }

    //开始运作配置工具，在外面的大while循环中一直运行的是这个函数
    public void doConfiguring() throws IOException{

        int readyNum = selector.selectNow(); //selector
        if(readyNum > 0){
            seleSet = selector.selectedKeys();
//            logger.info("Now the size of selector Keys is: " + selector.selectedKeys().size());

            Iterator it =  seleSet.iterator();   //得到迭代器
            while(it.hasNext()){
                SelectionKey key = (SelectionKey)it.next();     //获得下一个关键字
                it.remove();                               //把已经处理过的取消掉
//                logger.info("Now the size of selector Keys is: " + selector.selectedKeys().size());
                handleSelected(key);                     //处理关键字
            }
        }else if(initFlag){
            initFlag = false;
            cfgSokcet = SocketChannel.open();    //打开套接字
            cfgSokcet.configureBlocking(false);  //使用非阻塞的模式进行工作
            cfgSokcet.register(selector, SelectionKey.OP_CONNECT);  //注册连接事件
            cfgSokcet.connect(new InetSocketAddress(tarIP, tarPort));//请求
            int readyNum1 = selector.selectNow();
//            logger.info("ReadyNum is:" + readyNum1);
            /*if(cfgSokcet.finishConnect())//完成连接
            {
                cfgSokcet.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE); //注册读写事件
//                    Platform.getPlatform().welUI.setTextAndValue("Finished...", 100);
                logger.info("connector of ConfigTool Succeed！");
                initFlag = false;
            }*/
        }
    }


    //不同的事件调用不同的方法
    private void handleSelected(SelectionKey key) throws IOException
    {
        //首先判断下key是否还是在有效期之内

        if(key.isValid()){ if(key.isConnectable()){handleConnecting();}}

        if(key.isValid()){if(key.isWritable()){handleWriting();}}

        if(key.isValid()){if(key.isReadable()){handleReading();}}
    }

    //请求事件的发生
    private void handleConnecting() throws IOException  {
//        logger.info("Now handle connecting...");

        if(cfgSokcet.isConnectionPending()){
//            logger.info("Socket is connecting pengding...");
            try {
                if(cfgSokcet.finishConnect())//完成连接
                {
                    cfgSokcet.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE); //注册读写事件
                    logger.info("connector of ConfigTool Succeed！");
//                    Platform.getPlatform().welUI.setTextAndValue("Finished...", 100);
                }
            }

            catch (IOException e) {

                if(tryMaxTime != -1 && tryTime >= tryMaxTime ){   //判断是否超时
                    logger.info("Out of Time when tring to connect with Instrumrnt. Check it!");
                    System.exit(0);
                }

                tryTime ++;
                logger.info("Tring the "+tryTime+" time to connect with instrument.");
                cfgSokcet = SocketChannel.open();    //打开套接字
                cfgSokcet.configureBlocking(false);  //使用非阻塞的模式进行工作
                cfgSokcet.register(selector, SelectionKey.OP_CONNECT);  //注册连接事件
                cfgSokcet.connect(new InetSocketAddress(tarIP, tarPort));//请求
                logger.info("IP:"+tarIP+" "+"TarPort:"+tarPort);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //写事件
    private void handleWriting() throws IOException{

        byte[] resp = dataManager.getWriteData();
        if(null !=resp){
            // byte[] resp  = cfgHandler.getNextWriteBytes();       //得到要写的数据
            ByteBuffer buf =ByteBuffer.allocate(resp.length+5);  //给链路加上头
            byte[] num ={0,0,0,0};
            // int lenTemp = resp.length;
            for(int i = 0; i <4; i++)
            {
                num[i] = (byte) (0xFF&(resp.length>>i*8));
            }
            buf.clear();
            buf.put((byte) 0x7E);   //数据格式是7E(len:int) datadatadata
            buf.put(num);
            buf.put(resp);
            buf.flip();
            while(buf.hasRemaining())
            {
                cfgSokcet.write(buf);         //不断的写，写完为止
            }
        }

    }
    //读事件
    private void handleReading() {

        byte[] datahead = new byte[5];
        while(cache.hasRemaining())
            try {
                cfgSokcet.read(cache);
            } catch (IOException e) {
                logger.info("Error caused when writing the Socket, it maybe caused by unconcetion!");
                System.exit(0);
            }//一个数据包肯定是大于4个byte，因为7E
        cache.flip();
        cache.get(datahead);
        cache.clear();
        if (datahead[0]==0x7E)
        {
            int datalen = (0xFF&datahead[1])+((0xFF&datahead[2])<<8)+((0xFF&datahead[3])<<16)+((0xFF&datahead[4])<<24);
            ByteBuffer datacoming =ByteBuffer.allocate(datalen);
            datacoming.clear();
            while(datacoming.hasRemaining())
            {
                try {
                    cfgSokcet.read(datacoming);
                } catch (IOException e) {
                    logger.info("Error caused when writing the Socket, it maybe caused by unconcetion!");
                    System.exit(0);
                }
            }

            dataManager.putReadData(tlvtool.getDataTarget(datacoming.array(), 0), datacoming.array());
            //将数据放入数据管理区域
            //cfgHandler.putReadBuffer(datacoming.array());   //将数据保存起来
            //System.out.println(new String(datacoming.array())); //测试的时候使用
        }

    }
}

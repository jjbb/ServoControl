package com.zju.Modules;

import android.app.Application;

import com.zju.ConfigTool.ConfigTool;
import com.zju.PipeDataType.pString;
import com.zju.PipeDataType.pipeData;
import com.zju.servocontrol.ServoControl;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by jjbb_1 on 2016/4/25.
 */
public class ConfigCoreModul extends BaseModule{
    private String config = null;                      //配置命令
    private static String path =  "INSTR_INFO/sModule";  //配置文件的默认值
    private String temp = null;     		//在端口读取的时暂存
    private static final Logger logger = Logger.getLogger("com.zju.ConfigCoreModul");
    private pString configStringMap;
    @Override
    public void job(pipeData data){

        configStringMap = (pString) data;

        temp = configStringMap.getValue("DataOut1");
        if(null == temp){return;}    //首先判断是否有些新的命令
        config = temp;
        logger.info("received the: " + config );
        temp = configStringMap.getValue("DataOut2");
        if(null != temp){path = temp;}
        //++++++++++++++++++++++++++++++++++++++++++++++++

        // 在config脚有数据的时候才会执行这部分的代码
        Config  cfg  = Enum.valueOf(Config.class, config);
        cfg.config();



        //++++++++++++++++++++++++++++++++++++++++++++++++
    }
    /**
     * <class>内部枚举类</class>
     * @author Ake
     *
     *
     */
    public enum Config{
        Start,Reboot,Stop,Pause,Resume,Config,SysState,OsState;
        ServoControl pf = ServoControl.getInstance();    //平台的引用
        ConfigTool tool = pf.getCfgTool();
        public void config()
        {
            switch(this)
            {
                case Start:
                    tool.start();  //开始
                    break;
                case Reboot://重启
                    tool.reboot();
                    break;
                case Stop:
                    tool.shutdown();
                    break;      //结束
                case Pause:     //暂停
                    tool.pause();
                    break;
                case Resume:   //恢复
                    tool.resume();
                    break;
                case Config:   //配置
//                    File file = ServoControl.getInstance().getContext().getExternalFilesDir(path);
                    File file = new File(ServoControl.getInstance().getContext().getExternalFilesDir(null),path);
                    if(!file.exists())
                    {
                        logger.info("the path: "+path+" is not existed!, it use the default path" );
                        path = "INSTR_INFO/sModule";
                        return;
                    }
                    tool.config(path);
                    break;
                case SysState://平台状态
                    tool.sysState();
                    break;
                case OsState://系统状态
                    tool.osState();
                    break;
                default:
                    break;
            }
        }



    }
}

package com.zju.Modules;

import com.zju.PipeDataType.pString;
import com.zju.PipeDataType.pipeData;

/**
 * Created by jjbb_1 on 2016/4/25.
 */
public class PlatformConfigTool extends BaseModule {
    private pipeData pD;
    private int step = 3;  //每次重新配置需要3次
    private String path;
    private boolean ready = false;
    private ConfigCoreModul configCoreModul = new ConfigCoreModul();

    @Override
    public void job(pipeData data){

        pD = data;

        if(null != pD){
            path = (((pString)pD).getValue("DataIn"));
            if(null == path)return;
            configCoreModul.job(new pString("DataOut1","Pause"));
            step--;                   //第一步，先让下位机停止
            new Thread(new platformConfigThread()).start();
            return;
        }
    }

    class platformConfigThread implements Runnable{
        @Override
        public void run() {
            if(2 == step)
            {
                pString data = new pString("DataOut1","Config");
                data.addPipeData("DataOut2",path);
                configCoreModul.job(data);
                step--;//第二步，给下位机发配置文件
                try {
                    Thread.sleep(50);                  //时间可以改动,每过500毫秒进行一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(1 == step)
            {
                configCoreModul.job(new pString("DataOut1","Start"));
                step =3;
                try {
                    Thread.sleep(50);                  //时间可以改动,每过500毫秒进行一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

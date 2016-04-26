package com.zju.servocontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zju.Modules.PlatformConfigTool;
import com.zju.Modules.ServoAxisModule;
import com.zju.PipeDataType.pString;

public class MainActivity extends AppCompatActivity {


    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private static final String[] content = {"抓东西","放东西","其他"};
    private Button  buttonAddLeft;
    private Button  buttonDecLeft;
    private Button  buttonAddRight;
    private Button  buttonDecRight;
    private Button  buttonAddRot;
    private Button  buttonDecRot;
    private Button  buttonAddHand;
    private Button  buttonDecHand;
    private RelativeLayout servoAxis;

    private Button buttonCfgSend;
    private PlatformConfigTool configTool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,content);
        spinner.setAdapter(adapter);
        SurfaceView axisView = (SurfaceView) findViewById(R.id.axisView);
        SurfaceView servoView = (SurfaceView) findViewById(R.id.servoView);
        TextView servoPos = (TextView) findViewById(R.id.servoPos);
        ServoAxisModule servoAxisModule = new ServoAxisModule(this,axisView,servoView,servoPos);

        buttonCfgSend = (Button) findViewById(R.id.button_cfg);
        configTool = new PlatformConfigTool();
        buttonCfgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configTool.job(new pString("DataIn","INSTR_INFO/servo.xml"));
            }
        });
    }



}

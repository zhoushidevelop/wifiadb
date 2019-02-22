package com.zhou.wifiadb;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_open_adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_open_adb = findViewById(R.id.btn_open_adb);
        btn_open_adb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_adb:
                openAdb();
                break;
            default:
                break;
        }
    }

    /**
     * 开启adbwifi连接功能（需要root）
     */
    private void openAdb() {
        DataOutputStream os = null;
        try {
            Process localProcess = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(localProcess.getOutputStream());
            os.writeBytes("setprop service.adb.tcp.port 5555\n");
            os.writeBytes("stop adbd\n");
            os.writeBytes("start adbd\n");
            os.flush();


            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = Formatter.formatIpAddress(ipAddress);

            Toast.makeText(MainActivity.this, ip, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {

            }

        }
    }
}

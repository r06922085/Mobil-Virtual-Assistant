package app.tomlai.com.ui;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import tw.nicky.WifiManagerExample.R;

public class MainActivity extends Activity {
    private WifiManager wiFiManager;
    private Button turnOnWifiButn;
    private Button turnOffWifiButn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 取得WifiManager
        wiFiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        turnOnWifiButn = (Button) findViewById(R.id.turnOnWifiButn);
        turnOffWifiButn = (Button) findViewById(R.id.turnOffWifiButn);

        // 開啟wifi
        turnOnWifiButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //若wifi狀態為關閉則將它開啟
                if (!wiFiManager.isWifiEnabled()) {
                    wiFiManager.setWifiEnabled(true);
                }
            }
        });

        // 關閉wifi
        turnOffWifiButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //若wifi狀態為開啟則將它關閉
                if (wiFiManager.isWifiEnabled()) {
                    wiFiManager.setWifiEnabled(false);
                }
            }
        });
    }
}
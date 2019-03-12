package app.tomlai.com.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button turnOnBFButn;
    private Button turnOffBFButn;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }*/
        mBluetoothAdapter.disable();
        turnOnBFButn = (Button) findViewById(R.id.turnOnBFButn);
        turnOffBFButn = (Button) findViewById(R.id.turnOffBFButn);

        // 開啟Bluetooth
        turnOnBFButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //若Bluetooth狀態為關閉則將它開啟
                if (!mBluetoothAdapter.isEnabled()) {
                    //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    //startActivityForResult(enableBtIntent, 2);
                    mBluetoothAdapter.enable();
                }
            }
        });

        // 關閉Bluetooth
        turnOffBFButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }
            }
        });
    }
}

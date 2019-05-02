package app.tomlai.com.screen;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //改變當前螢幕亮度,如果畫面跳轉就會恢復
        int brightness = 100;
        int darkness = 10;
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.screenBrightness = brightness * 1.0f / 255;
        getWindow().setAttributes(layoutParams);

        //改變系統亮度,是永久的改變
        //以下4行是跳轉到手動同意權限的頁面,執行一次就夠了
        int REQUEST_CODE_WRITE_SETTINGS = 1;
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS );


        ContentResolver contentResolver = getContentResolver();
        //get brightness
        try {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set brightness
        brightness = 2; // 设置亮度值为255
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, brightness);


    }
}

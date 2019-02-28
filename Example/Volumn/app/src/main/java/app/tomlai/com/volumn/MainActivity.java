package app.tomlai.com.volumn;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //reference: https://codertw.com/android-%E9%96%8B%E7%99%BC/347200/
    private Button VolumnUp;
    private Button VolumnDown;
    private AudioManager am;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        VolumnUp = (Button) findViewById(R.id.VolumnUp);
        VolumnDown = (Button) findViewById(R.id.VolumnDown);

        // 音量大
        VolumnUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

        // 音量小
        VolumnDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER , AudioManager.FLAG_SHOW_UI);
            }
        });
    }
}

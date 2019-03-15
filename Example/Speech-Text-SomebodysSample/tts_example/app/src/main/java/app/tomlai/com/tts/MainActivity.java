package app.tomlai.com.tts;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends Activity {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 建立 TTS
        createLanguageTTS();

        Button speakButton = (Button) findViewById(R.id.btn_speak);
        speakButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View arg0)
            {
                //【英文】發音
                tts.speak( "你好", TextToSpeech.QUEUE_FLUSH, null );
            }
        });


    }


    protected void onDestroy()
    {
        // 釋放 TTS
        if( tts != null ) tts.shutdown();

        super.onDestroy();
    }

    private void createLanguageTTS()
    {
        if( tts == null )
        {
            tts = new TextToSpeech(this, new OnInitListener(){

                public void onInit(int arg0)
                {
                    // TTS 初始化成功
                    if( arg0 == TextToSpeech.SUCCESS )
                    {
                        // 指定的語系: 英文(美國)
                        Locale l = Locale.TRADITIONAL_CHINESE;  // 不要用 Locale.ENGLISH, 會預設用英文(印度)

                        // 目前指定的【語系+國家】TTS, 已下載離線語音檔, 可以離線發音
                        if( tts.isLanguageAvailable( l ) == TextToSpeech.LANG_COUNTRY_AVAILABLE )
                        {
                            tts.setLanguage( l );
                        }
                    }
                }}
            );
        }
    }
}

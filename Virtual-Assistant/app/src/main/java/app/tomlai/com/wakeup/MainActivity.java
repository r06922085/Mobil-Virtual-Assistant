package app.tomlai.com.wakeup;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextToSpeech tts;
    private SpeechRecognizer sr;
    private static final String TAG = "MyStt3Activity";
    private TextView mText;//displaying the recognized words
    private String Command;
    private String ToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 建立 TTS and STT
        createLanguageTTS();
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());

        //Find By ID
        mText = (TextView) findViewById(R.id.textView1);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        sr.startListening(intent);
    }

    class listener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d(TAG,  "error " +  error);
            mText.setText("error " + error);
            startActivity(new Intent(MainActivity.this, ListeningActivity.class));
        }
        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(TAG, "result " + data.get(i));
                str += data.get(i);
            }
            //mText.setText(""+data.get(0));
            //tts.speak( ""+data.get(0), TextToSpeech.QUEUE_FLUSH, null );

            Command = ""+data.get(0);
            ToDo();

            startActivity(new Intent(MainActivity.this, ListeningActivity.class));

        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
    protected void ToDo(){
        new Task().execute();
    }
    protected  void do_command(){
        if(ToDo.equals("打開wifi")) Wifi(1);
        else if(ToDo.equals("關閉wifi")) Wifi(0);
        else if(ToDo.equals("打開藍芽")) BT(1);
        else if(ToDo.equals("關閉藍芽"))BT(0);
        else if(ToDo.equals("調大銀幕")) Screen(1);
        else if(ToDo.equals("調小銀幕")) Screen(0);
        else if(ToDo.equals("調大音量")) Volumn(1);
        else if(ToDo.equals("調小音量")) Volumn(0);
    }
    protected  void Wifi(int type){
        Log.d(TAG, "hi " + type);
        WifiManager wiFiManager;
        wiFiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(type==1){
            if (!wiFiManager.isWifiEnabled()) {
                wiFiManager.setWifiEnabled(true);
            }
        }
        else{
            if (wiFiManager.isWifiEnabled()) {
                wiFiManager.setWifiEnabled(false);
            }
        }
    }
    protected  void BT(int type){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(type==1){
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
        else{
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }
    }
    protected  void Screen(int type){
        ContentResolver contentResolver = getContentResolver();
        int System_Brightness=0;
        try {
            System_Brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(type==1)System_Brightness = System_Brightness+40;

        else System_Brightness = System_Brightness-40;


        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, System_Brightness);
    }
    protected  void Volumn(int type){
        AudioManager am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(type==1){
            am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }
        else{
            am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER , AudioManager.FLAG_SHOW_UI);
            am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER , AudioManager.FLAG_SHOW_UI);
            am.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER , AudioManager.FLAG_SHOW_UI);
        }
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

    class Task extends AsyncTask<Void,Void,String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public String doInBackground(Void... arg0) {

            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;

            try
            {
                // create the HttpURLConnection
                url = new URL("http://140.112.31.89/line/main.php?Str="+Command);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // 使用甚麼方法做連線
                connection.setRequestMethod("GET");

                // 是否添加參數(ex : json...等)
                //connection.setDoOutput(true);

                // 設定TimeOut時間
                connection.setReadTimeout(15*1000);
                connection.connect();

                // 伺服器回來的參數
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                // close the reader; this can throw an exception too, so
                // wrap it in another try/catch block.
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!"".equals(result) || null != result){
                ToDo = result.replaceAll("\r|\n", "");;
                do_command();
                mText.setText("已經"+ToDo);
                tts.speak( "已經"+ToDo, TextToSpeech.QUEUE_FLUSH, null );
            }
        }
    }
}


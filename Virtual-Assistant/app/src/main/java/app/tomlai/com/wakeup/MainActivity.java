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
    private String ToAnswer;

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
            //mText.setText("error " + error);

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
            sr.startListening(intent);
            //startActivity(new Intent(MainActivity.this, ListeningActivity.class));
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

            if(Command.contains("沒事")){
                tts.speak( "那就好", TextToSpeech.QUEUE_FLUSH, null );
            }
            else{
                ToDo();
            }

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
        String[] command = {"打開WiFi", "關閉WiFi", "打開藍芽",
                "關閉藍芽","調大螢幕", "調小螢幕", "調大音量", "調小音量", "關機", "打開相機", "none"};

        for(int i=0;i<command.length;i++)
            ToDo = ToDo.replaceAll(command[i], " "+i);
        String[] ToDo_arr = ToDo.split(" ");


        for(int i=1;i<ToDo_arr.length;i++){
            Log.d(TAG, "ToDo_here:"+ToDo_arr[i]+";");
            if(Integer.valueOf(ToDo_arr[i]) == 0) Wifi(1);
            if(Integer.valueOf(ToDo_arr[i]) == 1) Wifi(0);
            if(Integer.valueOf(ToDo_arr[i]) == 2) BT(1);
            if(Integer.valueOf(ToDo_arr[i]) == 3) BT(0);
            if(Integer.valueOf(ToDo_arr[i]) == 4) Screen(1);
            if(Integer.valueOf(ToDo_arr[i]) == 5) Screen(0);
            if(Integer.valueOf(ToDo_arr[i]) == 6) Volumn(1);
            if(Integer.valueOf(ToDo_arr[i]) == 7) Volumn(0);

        }

    }
    protected  void Wifi(int type){
        Log.d(TAG, "hi " + type);
        WifiManager wiFiManager;
        wiFiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(type==1){
            if (!wiFiManager.isWifiEnabled()) {
                wiFiManager.setWifiEnabled(true);
            }
            else{
                ToAnswer = ToAnswer.replaceAll("打開wifi", ",wifi已經是打開的了,");
            }
        }
        else{
            if (wiFiManager.isWifiEnabled()) {
                wiFiManager.setWifiEnabled(false);
            }
            else{
                ToAnswer = ToAnswer.replaceAll("關閉wifi", ",wifi已經是關著的了,");
            }
        }
    }
    protected  void BT(int type){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(type==1){
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            else{
                ToAnswer = ToAnswer.replaceAll("打開藍芽", ",藍芽已經是打開的了,");
            }
        }
        else{
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
            else{
                ToAnswer = ToAnswer.replaceAll("關閉藍芽", ",藍芽已經是關上的了,");
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

        if(type==1)System_Brightness = System_Brightness + 40;

        else System_Brightness = System_Brightness - 40;

        //以下4行只需執行一次
        //int REQUEST_CODE_WRITE_SETTINGS = 1;
        //Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        //intent.setData(Uri.parse("package:" + getPackageName()));
        //startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS );
        if(System_Brightness<0){
            System_Brightness = 0;
            ToAnswer = ToAnswer.replaceAll("調暗銀幕", ",亮度已經是最暗的了,");
        }
        if(System_Brightness>255){
            System_Brightness = 255;
            ToAnswer = ToAnswer.replaceAll("調亮銀幕", ",亮度已經是最亮的了,");
        }

        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, System_Brightness);
    }
    protected  void Volumn(int type){
        AudioManager am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(type==1){

            if(am.getStreamVolume(AudioManager.STREAM_MUSIC)== am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)){
                ToAnswer = ToAnswer.replaceAll("調大音量", ",音量已經是最大的了,");
            }
            if(am.getStreamVolume(AudioManager.STREAM_MUSIC)== 0){
                ToAnswer = ToAnswer.replaceAll("調小音量", ",音量已經是最小的了,");
            }
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

                        tts.setPitch(0.8f);
                        tts.setSpeechRate(0.9f);

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
                url = new URL("http://140.112.31.89/virtual_assistant/main.php?Str="+Command);
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
                String response = result.replaceAll("\r|\n", "");

                String[] response_arr= response.split("-");
                ToDo = response_arr[0];
                ToAnswer = response_arr[1];

                do_command();

                tts.speak( ToAnswer, TextToSpeech.QUEUE_FLUSH, null );

                mText.setText(ToAnswer);

            }
        }
    }
}


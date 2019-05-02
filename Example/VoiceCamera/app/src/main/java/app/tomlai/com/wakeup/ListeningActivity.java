package app.tomlai.com.wakeup;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * The ListeningActivity implements the Pocket-Sphinx's RecognitionListener and moves on the
 * MainActivity, only after the wake word has been recognized.
 * <p>
 * While the Wake Word get read from a resource file, to change it, a new wake word would also need
 * to be added the ./assets/sync/models/lm/words.dic
 * Don't forget to generate a new MD5 hash for dictionary after you modified it, and store it in
 * ./assets/sync/models/lm/words.dic.md5 (E.g. use http://passwordsgenerator.net/md5-hash-generator/
 *
 * @author <a mailto="wolf@wolfpaulus.com">Wolf Paulus</a>
 */
public class ListeningActivity extends AppCompatActivity implements RecognitionListener , SurfaceHolder.Callback{
    //WakeWord
    private static final String LOG_TAG = ListeningActivity.class.getName();
    private static final String WAKEWORD_SEARCH = "WAKEWORD_SEARCH";
    private static final int PERMISSIONS_REQUEST_CODE = 5;
    private static int sensibility = 10;
    private SpeechRecognizer mRecognizer;
    private Vibrator mVibrator;

    //Camera
    Camera myCamera;
    SurfaceView previewSurfaceView;
    SurfaceHolder previewSurfaceHolder;
    boolean previewing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        //WakeWord
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final TextView threshold = (TextView) findViewById(R.id.threshold);
        threshold.setText(String.valueOf(sensibility));
        final SeekBar seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar.setProgress(sensibility);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold.setText(String.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // intentionally empty
            }

            public void onStopTrackingTouch(final SeekBar seekBar) {
                sensibility = seekBar.getProgress();
                Log.i(LOG_TAG, "Changing Recognition Threshold to " + sensibility);
                threshold.setText(String.valueOf(sensibility));
                mRecognizer.removeListener(ListeningActivity.this);
                mRecognizer.stop();
                mRecognizer.shutdown();
                setup();
            }
        });

        ActivityCompat.requestPermissions(ListeningActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, VIBRATE}, PERMISSIONS_REQUEST_CODE);

        //Camera
        getWindow().setFormat(PixelFormat.UNKNOWN);
        previewSurfaceView = (SurfaceView)findViewById(R.id.preview);
        previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(this);
        previewSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    //CameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCamera
    //CameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCamera
    //CameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCamera
    //CameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCamera
    //CameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCamera
    //CameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCameraCamera
    ShutterCallback shutterCallback = new ShutterCallback(){

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }};

    PictureCallback rawPictureCallback = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }};

    PictureCallback jpegPictureCallback = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

        }};
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

        if(previewing){
            myCamera.stopPreview();
            previewing = false;
        }


        try {
            myCamera.setPreviewDisplay(arg0);
            myCamera.startPreview();
            previewing = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        myCamera = Camera.open();
        myCamera.setDisplayOrientation(90);
        getSupportActionBar().setSubtitle("Say: Take");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;
        previewing = false;
    }
    public void TakePicture(){
        myCamera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);

        myCamera.stopPreview();
        SystemClock.sleep(500);
        myCamera.startPreview();
    }


    //WakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWord
    //WakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWord
    //WakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWord
    //WakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWord
    //WakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWordWakeWord
    public void onRequestPermissionsResult(
            final int requestCode,
            @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (0 < grantResults.length && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Audio recording permissions denied.", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();
    }

    /**
     * Stop the recognizer.
     * Since cancel() does trigger an onResult() call,
     * we cancel the recognizer rather then stopping it.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mRecognizer != null) {
            mRecognizer.removeListener(this);
            mRecognizer.cancel();
            mRecognizer.shutdown();
            Log.d(LOG_TAG, "PocketSphinx Recognizer was shutdown");
        }
    }

    /**
     * Setup the Recognizer with a sensitivity value in the range [1..100]
     * Where 1 means no false alarms but many true matches might be missed.
     * and 100 most of the words will be correctly detected, but you will have many false alarms.
     */
    private void setup() {
        try {
            final Assets assets = new Assets(ListeningActivity.this);
            final File assetDir = assets.syncAssets();
            mRecognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetDir, "models/en-us-ptm"))
                    .setDictionary(new File(assetDir, "models/lm/words.dic"))
                    .setKeywordThreshold(Float.valueOf("1.e-" + 2 * sensibility))
                    .getRecognizer();
            mRecognizer.addKeyphraseSearch(WAKEWORD_SEARCH, getString(R.string.wake_word));
            mRecognizer.addListener(this);
            mRecognizer.startListening(WAKEWORD_SEARCH);
            Log.d(LOG_TAG, "... listening");
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    //
    // RecognitionListener Implementation
    //

    @Override
    public void onBeginningOfSpeech() {
        Log.d(LOG_TAG, "Beginning Of Speech");
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setSubtitle("~ ~ ~");
        }
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(LOG_TAG, "End Of Speech");
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setSubtitle("Say: Take");
        }
    }

    @Override
    public void onPartialResult(final Hypothesis hypothesis) {
        if (hypothesis != null) {
            final String text = hypothesis.getHypstr();
            //getSupportActionBar().setSubtitle(text);
            Log.d(LOG_TAG, "on partial: " + text);
            if (text.equals(getString(R.string.wake_word))) {
                TakePicture();
                mVibrator.vibrate(100);

            }
            mRecognizer.removeListener(ListeningActivity.this);
            mRecognizer.stop();
            mRecognizer.shutdown();
            setup();
        }
    }

    @Override
    public void onResult(final Hypothesis hypothesis) {
        if (hypothesis != null) {
            Log.d(LOG_TAG, "on Result: " + hypothesis.getHypstr() + " : " + hypothesis.getBestScore());
            if (getSupportActionBar() != null) {
                //getSupportActionBar().setSubtitle("Say: Take");
            }
        }
    }

    @Override
    public void onError(final Exception e) {
        Log.e(LOG_TAG, "on Error: " + e);
    }

    @Override
    public void onTimeout() {
        Log.d(LOG_TAG, "on Timeout");
    }
}

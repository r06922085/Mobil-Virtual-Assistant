package app.tomlai.com.voice_camera;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback{

    Camera myCamera;
    SurfaceView previewSurfaceView;
    SurfaceHolder previewSurfaceHolder;
    boolean previewing = false;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        previewSurfaceView = (SurfaceView)findViewById(R.id.preview);
        previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(this);
        previewSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Button buttonTakePicture = (Button)findViewById(R.id.button);
        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //myCamera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
                //myCamera.startPreview();

                myCamera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);

                myCamera.stopPreview();
                SystemClock.sleep(2000);
                myCamera.startPreview();
            }});
    }

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

    @Override
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;
        previewing = false;
    }
}

/*import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,Camera.AutoFocusCallback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera camera;
    private Button TakePicture;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSurfaceView = (SurfaceView) this.findViewById(R.id.preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        TakePicture = this.findViewById(R.id.button);


        TakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
            }
        });

    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        //camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
        camera.setDisplayOrientation(90);//調成直的
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            camera.release();
            camera = null;
        }
        Log.d("MYLOG","SurfaceView is Creating!");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("MYLOG","SurfaceView is Change!");
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("MYLOG","SurfaceView is Destroyed!");
        camera.release();
        camera = null;
    }
    private void takeScreenshot() {
        //Date now = new Date();
        //android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "now" + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}*/

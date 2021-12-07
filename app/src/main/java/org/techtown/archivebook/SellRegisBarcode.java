package org.techtown.archivebook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SellRegisBarcode extends AppCompatActivity {

//    private ListenableFuture cameraProvideFuture;
//    private ExecutorService cameraExecuter;
//    private PreviewView pv_preview;
//    FrameLayout fl_container_barcode;
//    ImageView iv_barcode_background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_regis_barcode);

//        pv_preview = findViewById(R.id.pv_preview);
//        this.getWindow().setFlags(1024, 1024);
//
//        cameraExecuter = Executors.newSingleThreadExecutor();
//        cameraProvideFuture = ProcessCameraProvider.getInstance(this);
//
//        cameraProvideFuture.addListener(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void run() {
//                try {
//                    ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProvideFuture.get();
//                    bindpreview(processCameraProvider);
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, ContextCompat.getMainExecutor(this));
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void bindpreview(ProcessCameraProvider processCameraProvider) {
//        Preview preview = new Preview.Builder().build();
//        CameraSelector cameraSelector = new CameraSelector().Builder.requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
//        processCameraProvider.unbindAll();
//        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, )
//    }
}
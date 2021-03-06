package org.techtown.archivebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SellRegisDescribe extends AppCompatActivity {

    EditText et_sell_regis_describe;
    Button btn_sell_regis_next;
    ImageButton ibtn_camera;
    ImageView iv_photo;

    Uri photoUri;

    String accountId;
    private String imgStr;
    String fileName;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference imgRef;
    FirebaseStorage firebaseStorage;
    StorageReference imageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_regis_describe);

        et_sell_regis_describe = (EditText) findViewById(R.id.et_sell_regis_describe);
        btn_sell_regis_next = (Button) findViewById(R.id.btn_sell_regis_next);
        ibtn_camera = (ImageButton) findViewById(R.id.ibtn_camera);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);

        ibtn_camera.setVisibility(View.VISIBLE);
        iv_photo.setVisibility(View.GONE);

        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("account_id");

        Log.d("accountId-sell-describe", accountId);

        //Firebase DB ?????? ????????? image?????? ???????????? ????????????
        firebaseDatabase = FirebaseDatabase.getInstance("https://archibook-8eca7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        imgRef = firebaseDatabase.getReference();

        // Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        imageRef = firebaseStorage.getReference();

        // ?????? ?????? ????????? ?????? ?????? ????????? ?????? ?????? ???????????? ??????
        btn_sell_regis_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeNow = System.currentTimeMillis();

                // ????????? ????????? ????????????????????? ????????????
//                imgStr = intentGet.getStringExtra("image_path");
//                ImageData imageData = new ImageData();
//                imageData.setImage(imgStr);
//                imgRef.child("image").child(accountId + timeNow).push().setValue(imageData);

                // ????????? ?????????????????? ??????????????? ?????????
                fileName = accountId + timeNow + ".jpg";
                StorageReference riversRef = imageRef.child(fileName);
                UploadTask uploadTask = riversRef.putFile(photoUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("firebase storage", "upload success");
                    }
                });


                Intent intentPut = new Intent(getApplicationContext(), SellRegisConfirm.class);
                intentPut.putExtra("account_id", accountId);
                intentPut.putExtra("title", intentGet.getStringExtra("title"));
                intentPut.putExtra("isbn", intentGet.getStringExtra("isbn"));
                intentPut.putExtra("author", intentGet.getStringExtra("author"));
                intentPut.putExtra("publisher", intentGet.getStringExtra("publisher"));
                intentPut.putExtra("date_published", intentGet.getStringExtra("date_published"));
                intentPut.putExtra("price_origin", intentGet.getIntExtra("price_origin", 0));
                intentPut.putExtra("image_url", intentGet.getStringExtra("image_url"));
                intentPut.putExtra("link", intentGet.getStringExtra("link"));
                intentPut.putExtra("describe_detail", et_sell_regis_describe.getText().toString());
                intentPut.putExtra("image_path", fileName);
                startActivity(intentPut);
            }
        });

        ibtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????? ?????? ??????
                TedPermission.with(SellRegisDescribe.this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage("????????? ????????? ???????????????.")
                        .setDeniedMessage("????????? ????????? ??????????????????.")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .check();

                // ????????? ????????? Intent
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // ???????????? ?????? ?????? ??? ????????????
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // ????????? ???????????? ??????????????? ??????
                        if(photoFile != null) {
                            photoUri = FileProvider.getUriForFile(SellRegisDescribe.this, getPackageName() + ".fileprovider", photoFile);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                            startActivityForResult(i, 0);
                        }
                    }
                }
                else {
                    if(photoFile != null) {
                        photoUri = FileProvider.getUriForFile(SellRegisDescribe.this, getPackageName() + ".fileprovider", photoFile);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                        startActivityForResult(i, 0);
                    }
                }

                // ????????? ???????????? ??????????????? ??????
//                if(photoFile != null) {
//                    photoUri = FileProvider.getUriForFile(SellRegisDescribe.this, getPackageName() + ".fileprovider", photoFile);
//                    i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//
//                    startActivityForResult(i, 0);
//                }
            }
        });
    }

    // ???????????? ?????????
    @Override
    public void onBackPressed() {
        // ?????? ?????? ?????? ?????? dialog ?????????
        AlertDialog.Builder builder = new AlertDialog.Builder(SellRegisDescribe.this)
                .setMessage("?????? ????????? ?????????????????????????")
                .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ????????? ????????? ?????? ?????? ??????
                    }
                })
                .setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ??? ????????? accountId ??? ??????????????? ????????????
                        Intent intentBack = new Intent(SellRegisDescribe.this, MainActivity.class);
                        intentBack.putExtra("account_id", accountId);
                        Log.d("intentBack - accountId", accountId);
                        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentBack);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(SellRegisDescribe.this, "?????? ??????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(SellRegisDescribe.this, "?????? ??????\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ????????? ????????? ?????? ??????????????? ?????? ??????
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //?????????????????? bitmap ????????? ?????????
            //String imageFilePath = null;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try{
                exif = new ExifInterface(imageFilePath);
            }
            catch (IOException e){
                e.printStackTrace();
            }

            //????????? ??????????????? ?????????
            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            //???????????? ??????
            iv_photo.setImageBitmap(rotate(bitmap, exifDegree));
            ibtn_camera.setVisibility(View.GONE);
            iv_photo.setVisibility(View.VISIBLE);

//            // ????????? byte[] -> string ????????? ??????
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.JPEG, 30, stream);
//            byte[] photoImage = stream.toByteArray();
//
//            imgStr =  byteArrayToBinaryString(photoImage);

            // Bitmap -> String
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] bytes = baos.toByteArray();
            imgStr = Base64.encodeToString(bytes, Base64.NO_WRAP);

            Log.d("imgstr", imgStr);
        }
    }

    // byteArray -> string
    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    // byte -> string
    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    // ImageFile??? ????????? ????????? ????????? ??????
    String imageFilePath;
    private File createImageFile() throws IOException {
        // ??????????????? ?????? ??? ???????????? ??????
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        imageFilePath = image.getAbsolutePath();
        Log.d("imageFilePath", imageFilePath);

        return image;
    }

    //????????? ????????? ????????? ???????????? ????????? ??????
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    //???????????? ??????????????? ????????? ??????
    public Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
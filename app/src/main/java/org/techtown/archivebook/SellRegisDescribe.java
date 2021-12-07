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

        //Firebase DB 관리 객체와 image노드 참조객체 얻어오기
        firebaseDatabase = FirebaseDatabase.getInstance("https://archibook-8eca7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        imgRef = firebaseDatabase.getReference();

        // Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        imageRef = firebaseStorage.getReference();

        // 다음 버튼 클릭시 도서 정보 포함해 가격 입력 화면으로 전환
        btn_sell_regis_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeNow = System.currentTimeMillis();

                // 이미지 문자열 파이어베이스에 저장하기
//                imgStr = intentGet.getStringExtra("image_path");
//                ImageData imageData = new ImageData();
//                imageData.setImage(imgStr);
//                imgRef.child("image").child(accountId + timeNow).push().setValue(imageData);

                // 이미지 파이어베이스 스토리지에 업로드
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
                //카메라 권한 체크
                TedPermission.with(SellRegisDescribe.this)
                        .setPermissionListener(permissionlistener)
                        .setRationaleMessage("카메라 권한이 필요합니다.")
                        .setDeniedMessage("카메라 권한을 거부했습니다.")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .check();

                // 카메라 기능을 Intent
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 사진파일 변수 선언 및 경로세팅
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // 사진을 저장하고 이미지뷰에 출력
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

                // 사진을 저장하고 이미지뷰에 출력
//                if(photoFile != null) {
//                    photoUri = FileProvider.getUriForFile(SellRegisDescribe.this, getPackageName() + ".fileprovider", photoFile);
//                    i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//
//                    startActivityForResult(i, 0);
//                }
            }
        });
    }

    // 뒤로가기 클릭시
    @Override
    public void onBackPressed() {
        // 판매 취소 여부 묻는 dialog 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(SellRegisDescribe.this)
                .setMessage("판매 등록을 취소하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 아니오 선택시 화면 전환 없음
                    }
                })
                .setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 예 선택시 accountId 값 전달하면서 뒤로가기
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
            Toast.makeText(SellRegisDescribe.this, "권한 허가", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(SellRegisDescribe.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //이미지파일을 bitmap 변수에 초기화
            //String imageFilePath = null;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try{
                exif = new ExifInterface(imageFilePath);
            }
            catch (IOException e){
                e.printStackTrace();
            }

            //이미지 회전각도를 구한다
            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            //이미지를 출력
            iv_photo.setImageBitmap(rotate(bitmap, exifDegree));
            ibtn_camera.setVisibility(View.GONE);
            iv_photo.setVisibility(View.VISIBLE);

//            // 이미지 byte[] -> string 형으로 변환
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

    // ImageFile의 경로를 가져올 메서드 선언
    String imageFilePath;
    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
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

    //사진의 돌아간 각도를 계산하는 메서드 선언
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

    //이미지를 회전시키는 메서드 선언
    public Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
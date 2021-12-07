package org.techtown.archivebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SellingDetail extends AppCompatActivity {

    ImageView iv_book_selling_image;
    Button btn_book_selling_describe;
    ImageView iv_book_selling_profile;
    TextView tv_book_selling_nick_name;
    TextView tv_book_selling_title;
    TextView tv_book_selling_date_registerd;
    TextView tv_book_selling_author;
    TextView tv_book_selling_publisher;
    TextView tv_book_selling_date_published;
    TextView tv_book_selling_price_registerd;
    Button btn_selling_to_chat;
    Button btn_change_sold;
    ImageButton ibtn_delete;
    TextView tv_book_is_sold;

    Retrofit retrofit;
    SellingRetrofitInterface sellingRetrofitInterface;
    ChatRoomRetrofitInterface chatRoomRetrofitInterface;
    UserRetrofitInterface userRetrofitInterface;
    SoldRetrofitInterface soldRetrofitInterface;

    int registerdId;
    String accountId;

    ProfileColor profile_color = new ProfileColor();

    String backPage = "";
    String nickNameBack = "";

    String sellerId;
    String sellerNick;
    String sellerProfile;
    String sellingBook;
    String buyerNick;
    String buyerProfile;

    String select;
    String isbn;
    String buyerId;

    String imagePath;
    String imgStr;

    RequestOptions options;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference imgRef;
    FirebaseStorage firebaseStorage;
    StorageReference imageRef;

    //private ActivityResultLauncher<Intent> resultLauncher;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_detail);

        iv_book_selling_image = (ImageView) findViewById(R.id.iv_book_selling_image);
        btn_book_selling_describe = (Button) findViewById(R.id.btn_book_selling_describe);
        iv_book_selling_profile = (ImageView) findViewById(R.id.iv_book_selling_profile);
        tv_book_selling_nick_name = (TextView) findViewById(R.id.tv_book_selling_nick_name);
        tv_book_selling_title = (TextView) findViewById(R.id.tv_book_selling_title);
        tv_book_selling_date_registerd = (TextView) findViewById(R.id.tv_book_selling_date_registerd);
        tv_book_selling_author = (TextView) findViewById(R.id.tv_book_selling_author);
        tv_book_selling_publisher = (TextView) findViewById(R.id.tv_book_selling_publisher);
        tv_book_selling_date_published = (TextView) findViewById(R.id.tv_book_selling_date_published);
        tv_book_selling_price_registerd = (TextView) findViewById(R.id.tv_book_selling_price_registerd);
        btn_selling_to_chat = (Button) findViewById(R.id.btn_selling_to_chat);
        btn_change_sold = (Button) findViewById(R.id.btn_change_sold);
        ibtn_delete = (ImageButton) findViewById(R.id.ibtn_delete);
        tv_book_is_sold = (TextView) findViewById(R.id.tv_book_is_sold);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SellingRetrofitInterface sellingRetrofitInterface = retrofit.create(SellingRetrofitInterface.class);
        chatRoomRetrofitInterface = retrofit.create(ChatRoomRetrofitInterface.class);
        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);
        soldRetrofitInterface = retrofit.create(SoldRetrofitInterface.class);

        // activity 콜백 함수 - 뒤로 가기로 화면 이동시 accountId 값 받기
//        resultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if(result.getResultCode() == RESULT_OK) {
//                            Intent intentToBack = result.getData();
//                            String CallType = intentToBack.getStringExtra("accountId");
//                            accountId = CallType;
//                        }
//                    }
//                }
//        );

        //Firebase DB 관리 객체와 chat노드 참조객체 얻어오기
        firebaseDatabase = FirebaseDatabase.getInstance("https://archibook-8eca7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        imgRef = firebaseDatabase.getReference();

        // Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        imageRef = firebaseStorage.getReference();

        // 판매 도서 목록에서 화면 전환시 registerd_id 받아오기
        Intent intent = getIntent();
        registerdId = intent.getIntExtra("registerdId", 1);
        accountId = intent.getStringExtra("account_id");
        backPage = intent.getStringExtra("back_page");
        nickNameBack = intent.getStringExtra("nick_name");
        select = intent.getStringExtra("select");
        isbn = intent.getStringExtra("isbn");
        buyerId = intent.getStringExtra("buyer_id");

        // select 값이 yes인 경우 구매자 데이터 DB에 저장
        if (select != null) {
            if (select.equals("yes")) {
                SoldData soldData = new SoldData();
                soldData.setRegisterd_id(registerdId);
                soldData.setBuyer_id(buyerId);

                soldRetrofitInterface.postRegister(soldData).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == 200) {
                            Log.d("server sold register", "success" + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("server sold register", "fail" + t);
                    }
                });
            }
        }

        //test
        Log.d("accountId-sell-detail", accountId);

        // 서버로부터 해당 registerd_id의 정보 불러오기
        sellingRetrofitInterface.getDetail(registerdId).enqueue(new Callback<SellingData>() {
            @Override
            public void onResponse(Call<SellingData> call, Response<SellingData> response) {
                Log.d("server selling detail", "success");
                SellingData sellingResult = response.body();
                imagePath = sellingResult.getImage_path();
                iv_book_selling_profile.setBackground(profile_color.colorDrawable(getApplicationContext(), sellingResult.getProfile_color()));
                tv_book_selling_nick_name.setText(sellingResult.getNick_name());
                tv_book_selling_title.setText(sellingResult.getTitle());
                tv_book_selling_date_registerd.setText(sellingResult.getDate_registerd());
                tv_book_selling_author.setText(sellingResult.getAuthor());
                tv_book_selling_publisher.setText(sellingResult.getPublisher());
                tv_book_selling_date_published.setText(sellingResult.getDate_published());
                tv_book_selling_price_registerd.setText(sellingResult.getPrice_registerd() + " 원");

                // 파이어베이스에서 해당 imagePath의 이미지 문자열 받아오기
//                imgRef.child("image").child(imagePath).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        // 새로 추가된 데이터 가져오기
//                        ImageData imageData = snapshot.getValue(ImageData.class);
//                        imgStr = imageData.getImage();
//
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                // byte[] -> Drawable
//                if (imgStr != null) {
//                    byte[] b = binaryStringToByteArray(imgStr);
//                    ByteArrayInputStream is = new ByteArrayInputStream(b);
//                    Drawable photoImg = Drawable.createFromStream(is, "photoImg");
//                    iv_book_selling_image.setImageDrawable(photoImg);
//                }

                // firebase storage로부터 이미지 받아오기
                imageRef.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri uri) {
                        // 이미지 띄우기
                        options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

                        if (uri != null) {
                            Glide.with(getApplicationContext()).load(uri).apply(options).into(iv_book_selling_image);
                        }
                        else {
                            iv_book_selling_image.setImageResource(R.drawable.img_no_book_image);
                        }
                    }
                });

                sellerId = sellingResult.getAccount_id();
                sellerNick = sellingResult.getNick_name();
                sellerProfile = sellingResult.getProfile_color();
                sellingBook = sellingResult.getTitle();

                // 판매여부가 참일 경우 판매 완료 띄우기, 채팅으로 거래하기 버튼 비활성화
                if (sellingResult.getIs_sold()) {
                    tv_book_is_sold.setText("판매완료");
                    tv_book_is_sold.setTextColor(Color.parseColor("#C8C8C8"));
                    btn_selling_to_chat.setEnabled(false);
                }
                else {
                    // 판매여부가 거짓이고 판매 도서 등록자와 로그인 된 등록자의 id가 같을 경우 판매완료 버튼, 삭제 버튼 보이기, 채팅으로 거래하기 버튼 비활성화
                    if (sellingResult.getAccount_id().equals(accountId)) {
                        btn_change_sold.setVisibility(View.VISIBLE);
                        ibtn_delete.setVisibility(View.VISIBLE);
                        btn_selling_to_chat.setEnabled(false);
                    }
                    else {
                        btn_change_sold.setVisibility(View.INVISIBLE);
                        ibtn_delete.setVisibility(View.INVISIBLE);
                    }
                }

                Log.d("detail-color", profile_color.colorDrawable(getApplicationContext(), sellingResult.getProfile_color()).toString());

                // 상세설명 보기 버튼 클릭시 판매 등록된 도서 상태 설명 dialog 띄우기
                btn_book_selling_describe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SellingDescribeDialog.class);
                        intent.putExtra("bookDescribe", sellingResult.getDescribe_detail());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<SellingData> call, Throwable t) {
                Log.d("server selling detail", "fail");
            }
        });

        // 판매 완료 버튼 클릭시
        btn_change_sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 판매 완료 전환 확인 dialog 띄우기
                AlertDialog.Builder builder = new AlertDialog.Builder(SellingDetail.this)
                        .setMessage("판매를 완료하시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 아니오 선택시 화면 전환 없음
                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 예 선택시 판매 DB is_sold true로 변경하고 판매완료 DB에 저장하기
                                SellingData sellingData = new SellingData();
                                sellingData.setAccount_id(accountId);
                                sellingData.setRegisterd_id(registerdId);
                                sellingRetrofitInterface.putSold(sellingData).enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if (response.code() == 200) {
                                            Log.d("server selling sold", "success" + " " + response);

                                            tv_book_is_sold.setText("판매완료");
                                            tv_book_is_sold.setTextColor(Color.parseColor("#C8C8C8"));
                                            btn_change_sold.setVisibility(View.INVISIBLE);
                                        }
                                        else {
                                            Log.d("server selling sold", "fail" + " " + response);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Log.d("server selling sold", "fail" + " " + t);
                                    }
                                });

                                // 판매 대상 목록 화면 띄우기
                                Intent intentD = new Intent(SellingDetail.this, SellRegisSold.class);
                                intentD.putExtra("account_id", accountId);
                                intentD.putExtra("registerd_id", registerdId);
                                startActivity(intentD);
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 판매 등록 삭제 확인 dialog 띄우기
                AlertDialog.Builder builder = new AlertDialog.Builder(SellingDetail.this)
                        .setMessage("판매 등록을 삭제하시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 아니오 선택시 화면 전환 없음
                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 예 선택시 판매 DB에서 해당 데이터 삭제 후 메인으로 화면 전환
                                sellingRetrofitInterface.deleteDelete(registerdId, accountId).enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if (response.code() == 200) {
                                            Log.d("server selling delete", "success" + " " + response);

                                            Intent intentList = new Intent(getApplicationContext(), MainActivity.class);
                                            intentList.putExtra("account_id", accountId);
                                            startActivity(intentList);
                                            finish();
                                        }
                                        else {
                                            Log.d("server selling delete", "fail" + " " + response);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Log.d("server selling delete", "fail" + " " + t);
                                    }
                                });
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 채팅으로 거래하기 버튼 클릭
        btn_selling_to_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자 닉네임, 프로필 컬러 가져오기
                userRetrofitInterface.getDetail(accountId).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        Log.d("server user detail", "success" + " " + response);

                        buyerNick = response.body().getNick_name();
                        buyerProfile = response.body().getProfile_color();
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Log.d("server user detail", "fail" + " " + t);
                    }
                });

                // 생성할 채팅방 데이터
                ChatRoomData chatRoomData = new ChatRoomData();
                long timeNow = System.currentTimeMillis();

                chatRoomData.setBuyer_id(accountId);
                chatRoomData.setBuyer_nick(buyerNick);
                chatRoomData.setBuyer_profile(buyerProfile);
                chatRoomData.setSeller_id(sellerId);
                chatRoomData.setSeller_nick(sellerNick);
                chatRoomData.setSeller_profile(sellerProfile);
                chatRoomData.setRegisterd_id(registerdId);
                chatRoomData.setRoom_name(sellingBook);
                chatRoomData.setChat_id(accountId + timeNow);

                // 채팅방 생성
                chatRoomRetrofitInterface.postRegister(chatRoomData).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.code() == 200) {
                            Log.d("server chat register", "success" + " " + response);

                            // 채팅 화면으로 전환
                            Intent intent2 = new Intent(SellingDetail.this, ChatMessageList.class);
                            //Intent intent2 = new Intent(SellingDetail.this, MainActivity.class);
                            intent2.putExtra("account_id", accountId);
                            intent2.putExtra("room_name", sellingBook);
                            intent2.putExtra("other_nick", sellerNick);
                            intent2.putExtra("my_nick", buyerNick);
                            intent2.putExtra("my_profile", buyerProfile);
                            intent2.putExtra("chat_id", accountId + timeNow);
                            intent2.putExtra("frag", "chat");
                            startActivity(intent2);
                        }
                        else {
                            Log.d("server chat register", "fail" + " " + response);
                            Log.d("chat datas", buyerNick + " " + buyerProfile + " " + sellerId + " " + sellerNick + " " + sellerProfile + " " + registerdId + " " + sellingBook);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("server chat register", "fail" + " " + t);
                    }
                });
            }
        });
    }

    // 뒤로가기 이동시 accountId 값 전달
    @Override
    public void onBackPressed() {
        //Log.d("back_page", backPage);
        // 뒤로갈 화면 지정
//        switch (backPage) {
//            case "selling":
//                Intent intentBackSelling = new Intent(getApplicationContext(), MySellingList.class);
//                intentBackSelling.putExtra("account_id", accountId);
//                intentBackSelling.putExtra("nick_name", nickNameBack);
//                Log.d("intentBack - accountId", accountId);
//                intentBackSelling.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intentBackSelling);
//                finish();
//                break;
//            case "sold":
//                Intent intentBackSold = new Intent(getApplicationContext(), MySoldList.class);
//                intentBackSold.putExtra("account_id", accountId);
//                intentBackSold.putExtra("nick_name", nickNameBack);
//                intentBackSold.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                Log.d("intentBack - accountId", accountId);
//                startActivity(intentBackSold);
//                finish();
//                break;
//            default:
//                Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
//                intentBack.putExtra("account_id", accountId);
//                Log.d("intentBack - accountId", accountId);
//                intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intentBack);
//                finish();
//        }

        finish();
    }

    public static byte[] binaryStringToByteArray(String s) {
        int count = s.length()/8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i-1)*8, i*8);
            b[i-1] = binaryStringToByte(t);
        }
        return b;
    }

    public static byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7-i) == '1') ? (byte) (1 << i):0;
            total = (byte) (ret|total);
        }
        return total;
    }
}
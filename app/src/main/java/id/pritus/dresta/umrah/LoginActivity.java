package id.pritus.dresta.umrah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class LoginActivity extends AppCompatActivity {
    EditText username_input,password_input;
    TextView link_daftar;
    Button btnLogin;
    Vibrator v;
    private PrefManager prefManager;


    interface MyAPIService {
        @FormUrlEncoded
        @POST("/android/login/cek")
        Call<List<Datapengguna>> getDatapengguna(@Field("username") String username, @Field("password") String password);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username_input = findViewById(R.id.userName);
        password_input = findViewById(R.id.loginPassword);
        link_daftar = findViewById(R.id.link_daftar);
        btnLogin = findViewById(R.id.btnLogin);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prefManager = new PrefManager(this);
        String loggedInIdAgen= prefManager.LoggedInIdAgen();
        String loggedInIdJamaah= prefManager.LoggedInIdJamaah();
        if (loggedInIdAgen!=null||loggedInIdJamaah!=null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        FloatingActionButton FabBack = findViewById(R.id.FabBack);
        FabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView TvLupass = findViewById(R.id.TvLupaPass);
        TvLupass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ilupass = new Intent(LoginActivity.this,LupassActivity.class);
                startActivity(Ilupass);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });


        //when someone clicks on login
        link_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ke halaman regist
                Intent intent = new Intent(getApplicationContext(),PilihActivity.class);
                startActivity(intent);
            }
        });

    }
    private void validateUserData() {
        //fflagirst getting the values
        final String username= username_input.getText().toString();
        final String password = password_input.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(username)) {
            username_input.setError("Username masih kosong");
            username_input.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            btnLogin.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(password)) {
            password_input.setError("Password masih kosong");
            password_input.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            btnLogin.setEnabled(true);
            return;
        }



        //Login User if everything is fine
        loginUser(username,password);


    }
    public void set_sess_agen(String u,String ip){
        prefManager.storeIdAgen(ip);
        prefManager.storeUserName(u);
    }
    public void set_sess_jamaah(String u,String ip){
        prefManager.storeIdJamaah(ip);
        prefManager.storeUserName(u);
    }
    private void loginUser(String username, String password) {
        //making api call
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setTitle("Login");
        progressDoalog.setMessage("Login....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDoalog.show();

        Call<List<Datapengguna>> call = myAPIService.getDatapengguna(username,password);

        call.enqueue(new Callback<List<Datapengguna>>() {

            @Override
            public void onResponse(Call<List<Datapengguna>> call, Response<List<Datapengguna>> response) {

                if (response.body() != null) {
                    progressDoalog.dismiss();
                    if(!response.body().get(0).getStatus().equals("gagal")){
                        //get username
                        String user = response.body().get(0).getUsername();
                        String id_user;
                        if (response.body().get(0).getId_jamaah()!=null){
                            id_user = response.body().get(0).getId_jamaah();
                            set_sess_jamaah(user,id_user);

                        }else{
                            id_user = response.body().get(0).getId_agen();
                            set_sess_agen(user,id_user);
                        }
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                        Toast.makeText(LoginActivity.this, response.body().get(0).getStatus(), Toast.LENGTH_SHORT).show();

                    }else{
                        progressDoalog.dismiss();
                        Toast.makeText(LoginActivity.this,response.body().get(0).getPesan(),Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Datapengguna>> call, Throwable throwable) {
                progressDoalog.dismiss();
                Toast.makeText(LoginActivity.this, "Gagal Login, Coba Lagi", Toast.LENGTH_LONG).show();
            }
        });

    }
    public class Datapengguna {

        @SerializedName("id_agen")
        private String id_agen;
        @SerializedName("id_jamaah")
        private String id_jamaah;
        @SerializedName("status")
        private  String status;
        @SerializedName("username")
        private String username;
        @SerializedName("pesan")
        private String pesan;
        public Datapengguna(String id_agen,String id_jamaah,String status,String username, String pesan) {
            this.id_agen=id_agen;
            this.id_jamaah=id_jamaah;
            this.status= status;
            this.username= username;
            this.pesan= pesan;
        }
        public String getId_jamaah() {
            return id_jamaah;
        }
        public void setId_jamaah(String id_jamaah) {
            this.id_jamaah = id_jamaah;
        }
        public String getId_agen() {
            return id_agen;
        }

        public void setId_agen(String id_agen) {
            this.id_agen = id_agen;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public void setPesan(String pesan) {
            this.pesan = pesan;
        }
        public String getPesan() {
            return pesan;
        }
    }

}

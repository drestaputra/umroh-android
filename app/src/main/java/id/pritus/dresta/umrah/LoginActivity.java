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
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.pritus.dresta.umrah.model.GeneralResponse;
import id.pritus.dresta.umrah.model.GeneralSingleResponse;
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
        @POST("login/user")
        Call<GeneralSingleResponse> getDatapengguna(@Field("username") String username, @Field("password") String password);

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

        Call<GeneralSingleResponse> call = myAPIService.getDatapengguna(username,password);

        call.enqueue(new Callback<GeneralSingleResponse>() {

            @Override
            public void onResponse(Call<GeneralSingleResponse> call, Response<GeneralSingleResponse> response) {

                if (response.body() != null) {
                    progressDoalog.dismiss();

                    Datapengguna datapengguna = response.body().getData(Datapengguna.class);
                    if(response.body().getStatus() == 200){
                        //get username
                        String user = datapengguna.getUsername();
                        String id_user;
                        if (datapengguna.getId_jamaah()!=null){
                            id_user = datapengguna.getId_jamaah();
                            set_sess_jamaah(user,id_user);

                        }else{
                            id_user = datapengguna.getId_agen();
                            set_sess_agen(user,id_user);
                        }
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                        Toast.makeText(LoginActivity.this, datapengguna.getStatus(), Toast.LENGTH_SHORT).show();

                    }else{
                        progressDoalog.dismiss();
                        Toast.makeText(LoginActivity.this,response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<GeneralSingleResponse> call, Throwable throwable) {
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
        @SerializedName("username")
        private String username;

        public Datapengguna(String id_agen, String id_jamaah, String username) {
            this.id_agen = id_agen;
            this.id_jamaah = id_jamaah;
            this.username = username;
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
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
    }

}


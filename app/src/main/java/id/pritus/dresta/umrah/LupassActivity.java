package id.pritus.dresta.umrah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.pritus.dresta.umrah.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LupassActivity extends AppCompatActivity {

    TextView TxvKembali;
    EditText EtEmail;
    Button BtKirim;

    interface APIService{
        @FormUrlEncoded
        @POST("login/forget_password")
        Call<GeneralResponse> forgetPassword(@Field("email") String email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupass);
        TxvKembali = findViewById(R.id.TxvKembali);
        EtEmail = findViewById(R.id.EtEmail);
        TxvKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        BtKirim = findViewById(R.id.BtnKirim);
        BtKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EtEmail.getText().length() == 0){
                    return;
                }
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(LupassActivity.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Mengirim....");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                progressDoalog.show();


                String EtEmails = EtEmail.getText().toString();
                APIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
                Call<GeneralResponse> call = myAPIService.forgetPassword(EtEmails);
                call.enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.body().getStatus()==200){
                            progressDoalog.dismiss();
                            Intent Ilogin = new Intent(LupassActivity.this,LoginActivity.class);
                            Ilogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Ilogin);
//                            Toast.makeText(LupassActivity.this, response.body().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        }else{
                            progressDoalog.dismiss();
                            Toast.makeText(LupassActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            Intent Ilogin = new Intent(LupassActivity.this,LoginActivity.class);
                            Ilogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Ilogin);
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        progressDoalog.dismiss();
                        Toast.makeText(LupassActivity.this, "Kesalah koneksi, silahkan periksa jaringan", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}

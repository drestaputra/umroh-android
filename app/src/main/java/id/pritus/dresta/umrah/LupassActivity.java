package id.pritus.dresta.umrah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
        @POST("/android/login/lupass")
        Call<List<LupassResponse>> getDatapengguna(@Field("username") String username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupass);
        TxvKembali = findViewById(R.id.TxvKembali);
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
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(LupassActivity.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Mengirim....");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                progressDoalog.show();

                EtEmail = findViewById(R.id.EtEmail);
                String EtEmails = EtEmail.getText().toString();
                APIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
                Call<List<LupassResponse>> call = myAPIService.getDatapengguna(EtEmails);
                call.enqueue(new Callback<List<LupassResponse>>() {
                    @Override
                    public void onResponse(Call<List<LupassResponse>> call, Response<List<LupassResponse>> response) {
                        if (response.body().get(0).getStatus()==200){
                            progressDoalog.dismiss();
                            Intent Ilogin = new Intent(LupassActivity.this,LoginActivity.class);
                            Ilogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Ilogin);
//                            Toast.makeText(LupassActivity.this, response.body().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        }else{
                            progressDoalog.dismiss();
                            Toast.makeText(LupassActivity.this, response.body().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            Intent Ilogin = new Intent(LupassActivity.this,LoginActivity.class);
                            Ilogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Ilogin);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LupassResponse>> call, Throwable t) {
                        progressDoalog.dismiss();
                        Toast.makeText(LupassActivity.this, "Kesalah koneksi, silahkan periksa jaringan", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    class LupassResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;

        public LupassResponse(Integer status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

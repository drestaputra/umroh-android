package id.pritus.dresta.umrah;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AkunActivity extends AppCompatActivity {
    TextView TxvNama, TxvUsername, TxvEmail, TxvNoHp, TxvAlamat, TxvTglLahir, TxvTempatLahir, TxvPekerjaan,TxvJenisUser,TxvIduser;
    ImageView ImvFoto, ImvFotoKtp;
    Button BtBack;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    private String id_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        prefManager = new PrefManager(this);
        TxvNama = findViewById(R.id.TxvNama);
        TxvUsername = findViewById(R.id.TxvUsername);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvAlamat = findViewById(R.id.TxvAlamat);
        TxvTglLahir = findViewById(R.id.TxvTglLahir);
        TxvTempatLahir = findViewById(R.id.TxvTempatLahir);
        TxvPekerjaan = findViewById(R.id.TxvPekerjaan);
        TxvJenisUser = findViewById(R.id.TxvJenisUser);
//        TxvIduser = findViewById(R.id.TxvIdUser);
        ImvFoto = findViewById(R.id.ImvFoto);
        ImvFotoKtp = findViewById(R.id.ImvFotoKtp);
        mShimmerViewContainer = findViewById(R.id.shimmer_akun_container);
        mShimmerViewContainer.startShimmerAnimation();

        BtBack = findViewById(R.id.BtBack);
        BtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AkunActivity.this,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

//        TxvNama.setText("tes");
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        if (prefManager.isJamaahLoggedIn()==true){
            id_user=prefManager.LoggedInIdJamaah();
        }else if(prefManager.isAgenLoggedIn()==true){
            id_user=prefManager.LoggedInIdAgen();
        }
//        id_user = getIntent().getStringExtra("id_user");
        Call<List<Akun>> call = myAPIService.getAkun(id_user);
        call.enqueue(new Callback<List<Akun>>() {
            @Override
            public void onResponse(Call<List<Akun>> call, Response<List<Akun>> response) {

                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//                    TextView TxvNama,TxvUsername,TxvEmail,TxvNoHp,TxvAlamat,TxvTglLahir,TxvTempatLahir,TxvPekerjaan;
                    TxvNama.setText(response.body().get(0).getNama_lengkap());
                    TxvUsername.setText(response.body().get(0).getUsername());
                    TxvEmail.setText(response.body().get(0).getEmail());
                    TxvNoHp.setText(response.body().get(0).getNo_hp());
                    TxvAlamat.setText(response.body().get(0).getAlamat());
                    TxvTglLahir.setText(response.body().get(0).getTgl_lahir());
                    TxvTglLahir.setText(response.body().get(0).getTgl_lahir());
                    TxvTempatLahir.setText(response.body().get(0).getTempat_lahir());
                    TxvPekerjaan.setText(response.body().get(0).getPekerjaan());
                    TxvJenisUser.setText(response.body().get(0).getJenis_user().toUpperCase());
//                    TxvIduser.setText(response.body().get(0).getId_user());
//                    Log.d("tesjenisuser", response.body().get(0).getJenis_user());
                    if(prefManager.isAgenLoggedIn()==true){
                        TxvJenisUser.setText(response.body().get(0).getJenis_user().toUpperCase()+" \n ID : "+id_user);
                    }

                    Log.d("tesfoto", response.body().get(0).getFoto_ktpJamaah());
                    if (response.body().get(0).getJenis_user()=="agen"){
                        if(response.body().get(0).getFoto_ktpAgen() != null && response.body().get(0).getFoto_ktpAgen().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(response.body().get(0).getFoto_ktpAgen()).placeholder(R.color.greycustom2).into(ImvFotoKtp);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFotoKtp);
                        }
                        if(response.body().get(0).getFotoAgen() != null && response.body().get(0).getFotoAgen().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(response.body().get(0).getFoto_ktpAgen()).placeholder(R.color.greycustom2).into(ImvFoto);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFoto);
                        }
                    }else{
                        if(response.body().get(0).getFoto_ktpJamaah() != null && response.body().get(0).getFoto_ktpJamaah().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(response.body().get(0).getFoto_ktpJamaah()).placeholder(R.color.greycustom2).into(ImvFotoKtp);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFotoKtp);
                        }
                        if(response.body().get(0).getFotoJamaah() != null && response.body().get(0).getFotoJamaah().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(response.body().get(0).getFoto_ktpJamaah()).placeholder(R.color.greycustom2).into(ImvFoto);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFoto);
                        }

                    }


                } else {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//                    shimmer stop, failed get data
                }
            }

            @Override
            public void onFailure(Call<List<Akun>> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);

            }
        });
    }

    interface MyAPIService {
        @FormUrlEncoded
        @POST("akun/detail")
        Call<List<Akun>> getAkun(@Field("id_user") String id_user);
    }

    class Akun {
        @SerializedName("id_user")
        private String id_user;
        @SerializedName("nama_lengkap")
        private String nama_lengkap;
        @SerializedName("jenis_user")
        private String jenis_user;
        @SerializedName("username")
        private String username;
        @SerializedName("email")
        private String email;
        @SerializedName("no_hp")
        private String no_hp;
        @SerializedName("alamat")
        private String alamat;
        @SerializedName("tgl_lahir")
        private String tgl_lahir;
        @SerializedName("tempat_lahir")
        private String tempat_lahir;
        @SerializedName("pekerjaan")
        private String pekerjaan;
        @SerializedName("foto_ktp")
        private String foto_ktp;
        @SerializedName("foto")
        private String foto;

        public Akun(String id_user, String nama_lengkap, String jenis_user, String username, String email, String no_hp, String alamat, String tgl_lahir, String tempat_lahir, String pekerjaan, String foto_ktp, String foto) {
            this.id_user = id_user;
            this.nama_lengkap = nama_lengkap;
            this.jenis_user = jenis_user;
            this.username = username;
            this.email = email;
            this.no_hp = no_hp;
            this.alamat = alamat;
            this.tgl_lahir = tgl_lahir;
            this.tempat_lahir = tempat_lahir;
            this.pekerjaan = pekerjaan;
            this.foto_ktp = foto_ktp;
            this.foto = foto;
        }

        public String getId_user() {
            return id_user;
        }

        public String getNama_lengkap() {
            return nama_lengkap;
        }

        public String getJenis_user() {
            return jenis_user;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getNo_hp() {
            return no_hp;
        }

        public String getAlamat() {
            return alamat;
        }

        public String getTgl_lahir() {
            return tgl_lahir;
        }

        public String getTempat_lahir() {
            return tempat_lahir;
        }

        public String getPekerjaan() {
            return pekerjaan;
        }

        public String getFoto_ktpAgen() {
            return "https://admin.biroumrohcilacap.com/assets/img/agen/" + foto_ktp;
        }

        public String getFoto_ktpJamaah() {
            return "https://admin.biroumrohcilacap.com/assets/img/jamaah/" + foto_ktp;
        }

        public String getFotoAgen() {
            return "https://admin.biroumrohcilacap.com/assets/img/agen/" + foto;
        }

        public String getFotoJamaah() {
            return "https://admin.biroumrohcilacap.com/assets/img/jamaah/" + foto;
        }
    }
}

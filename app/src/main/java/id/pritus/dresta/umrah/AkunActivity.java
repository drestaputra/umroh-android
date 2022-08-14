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

import id.pritus.dresta.umrah.model.GeneralSingleResponse;
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
    private String jenisUser;


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
            jenisUser = "jamaah";
        }else if(prefManager.isAgenLoggedIn()==true){
            id_user=prefManager.LoggedInIdAgen();
            jenisUser = "agen";
        }
//        id_user = getIntent().getStringExtra("id_user");
        Call<GeneralSingleResponse> call = myAPIService.getAkun(id_user, jenisUser);
        call.enqueue(new Callback<GeneralSingleResponse>() {
            @Override
            public void onResponse(Call<GeneralSingleResponse> call, Response<GeneralSingleResponse> response) {

                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
//                    TextView TxvNama,TxvUsername,TxvEmail,TxvNoHp,TxvAlamat,TxvTglLahir,TxvTempatLahir,TxvPekerjaan;
                    Akun akun = response.body().getData(Akun.class);
                    TxvNama.setText(akun.getNama_lengkap());
                    TxvUsername.setText(akun.getUsername());
                    TxvEmail.setText(akun.getEmail());
                    TxvNoHp.setText(akun.getNo_hp());
                    TxvAlamat.setText(akun.getAlamat());
                    TxvTglLahir.setText(akun.getTgl_lahir());
                    TxvTglLahir.setText(akun.getTgl_lahir());
                    TxvTempatLahir.setText(akun.getTempat_lahir());
                    TxvPekerjaan.setText(akun.getPekerjaan());
                    TxvJenisUser.setText(akun.getJenis_user().toUpperCase());

                    if(prefManager.isAgenLoggedIn()){
                        TxvJenisUser.setText(akun.getJenis_user().toUpperCase()+" \n ID : "+id_user);
                    }

                    if (akun.getJenis_user().equalsIgnoreCase("agen")){
                        if(akun.getFoto_ktpAgen() != null && akun.getFoto_ktpAgen().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(akun.getFoto_ktpAgen()).placeholder(R.color.greycustom2).into(ImvFotoKtp);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFotoKtp);
                        }
                        if(akun.getFotoAgen() != null && akun.getFotoAgen().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(akun.getFoto_ktpAgen()).placeholder(R.color.greycustom2).into(ImvFoto);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFoto);
                        }
                    }else{
                        if(akun.getFoto_ktpJamaah() != null && akun.getFoto_ktpJamaah().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(akun.getFoto_ktpJamaah()).placeholder(R.color.greycustom2).into(ImvFotoKtp);
                        }else {
                            Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(ImvFotoKtp);
                        }
                        if(akun.getFotoJamaah() != null && akun.getFotoJamaah().length()>0)
                        {
                            Picasso.with(getApplicationContext()).load(akun.getFoto_ktpJamaah()).placeholder(R.color.greycustom2).into(ImvFoto);
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
            public void onFailure(Call<GeneralSingleResponse> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);

            }
        });
    }

    interface MyAPIService {
        @FormUrlEncoded
        @POST("akun/profil")
        Call<GeneralSingleResponse> getAkun(@Field("id_user") String id_user, @Field("jenis_user") String jenisUser);
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
            return foto_ktp;
        }

        public String getFoto_ktpJamaah() {
            return foto_ktp;
        }

        public String getFotoAgen() {
            return foto;
        }

        public String getFotoJamaah() {
            return foto;
        }
    }
}

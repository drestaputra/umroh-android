package id.pritus.dresta.umrah;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.pritus.dresta.umrah.model.GeneralSingleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class DetailBeritaActivity extends AppCompatActivity {

    ImageView ivGambarBerita;
    TextView TxvTglArtikel,TxvPenulisArtikel,TxvJudulArtikel;
    WebView WvIsiArtikel;
    Toolbar toolbar;

    interface MyAPIService{
        @FormUrlEncoded
        @POST("artikel/detail_artikel")
        Call<GeneralSingleResponse> getDetailArtikel(@Field("id_artikel") String id_artikel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);


        toolbar = findViewById(R.id.toolbar);
        ivGambarBerita = (ImageView) findViewById(R.id.IvGambarArtikel);
        TxvTglArtikel= (TextView) findViewById(R.id.TxvTglArtikel);
        TxvJudulArtikel = (TextView) findViewById(R.id.TxvJudulArtikel);
        TxvPenulisArtikel= (TextView) findViewById(R.id.TxvPenulisArtikel);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WvIsiArtikel = (WebView) findViewById(R.id.wvKontenBerita);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;
        String id_artikeli = getIntent().getStringExtra("id_artikel");
        Call<GeneralSingleResponse> call = myAPIService.getDetailArtikel(id_artikeli);
        call.enqueue(new Callback<GeneralSingleResponse>() {

            @Override
            public void onResponse(Call<GeneralSingleResponse> call, Response<GeneralSingleResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200){
                        Detail detail = response.body().getData(Detail.class);
                        showDetailBerita(detail);
                    }
                }else{

                }
            }
            @Override
            public void onFailure(Call<GeneralSingleResponse> call, Throwable throwable) {
                Toast.makeText(DetailBeritaActivity.this, "Gagal membuka artikel", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }



        // Jalankan method tampil detail berita




    private void showDetailBerita(Detail detail) {
        // Tangkap data dari intent
        String judul_artikel= detail.getJudul_artikel();
        String tgl_berita = detail.gettgl_artikel();
        String penulis_berita = detail.getPenulis_artikel();
        String isi_berita = detail.getIsi_artikel();
        String foto_berita = detail.getGambar_artikel();

        // Set judul actionbar / toolbar
        TxvJudulArtikel.setText(judul_artikel);


        // Set ke widget
        TxvPenulisArtikel.setText("Oleh : " + penulis_berita);
        TxvTglArtikel.setText(tgl_berita);
        // Untuk gambar berita
        Picasso.with(this).load(foto_berita).into(ivGambarBerita);
        // Set isi berita sebagai html ke WebView
        WvIsiArtikel.getSettings().setJavaScriptEnabled(true);
        WvIsiArtikel.loadData(isi_berita, "text/html; charset=utf-8", "UTF-8");
    }

    class Detail{
        @SerializedName("id_artikel")
        String id_artikel;
        @SerializedName("tgl_artikel")
        String tgl_artikel;
        @SerializedName("judul_artikel")
        String judul_artikel;
        @SerializedName("isi_artikel")
        String isi_artikel;
        @SerializedName("penulis_artikel")
        String penulis_artikel;
        @SerializedName("gambar_artikel")
        String gambar_artikel;


        public Detail(String id_artikel, String tgl_artikel, String judul_artikel, String isi_artikel, String penulis_artikel, String gambar_artikel) {
            this.id_artikel = id_artikel;
            this.tgl_artikel = tgl_artikel;
            this.judul_artikel = judul_artikel;
            this.isi_artikel = isi_artikel;
            this.penulis_artikel = penulis_artikel;
            this.gambar_artikel = gambar_artikel;
        }

        public String getId_artikel() {
            return id_artikel;
        }

        public void setId_artikel(String id_artikel) {
            this.id_artikel = id_artikel;
        }

        public String gettgl_artikel() {
            return tgl_artikel;
        }

        public void settgl_artikel(String tgl_artikel) {
            this.tgl_artikel = tgl_artikel;
        }

        public String getJudul_artikel() {
            return judul_artikel;
        }

        public void setJudul_artikel(String judul_artikel) {
            this.judul_artikel = judul_artikel;
        }

        public String getIsi_artikel() {
            return isi_artikel;
        }

        public void setIsi_artikel(String isi_artikel) {
            this.isi_artikel = isi_artikel;
        }

        public String getPenulis_artikel() {
            return penulis_artikel;
        }

        public void setPenulis_artikel(String penulis_artikel) {
            this.penulis_artikel = penulis_artikel;
        }

        public String getGambar_artikel() {
            return  gambar_artikel;
        }

        public void setGambar_artikel(String gambar_artikel) {
            this.gambar_artikel = gambar_artikel;
        }
    }


}

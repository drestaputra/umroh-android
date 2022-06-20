package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 4/15/2017.
 */

public class RiwayatActivity extends AppCompatActivity {


    interface MyAPIService {
        @FormUrlEncoded
        @POST("android/riwayat")
        Call<List<Riwayat>> getRiwayat(@Field("id_pendaftar") String id_user);
    }
    class Riwayat{
        @SerializedName("id_pendaftaran")
        String id_pendaftaran;
        @SerializedName("tgl_daftar")
        String tgl_daftar;
        @SerializedName("nama_lengkap")
        String nama_lengkap;
        @SerializedName("email")
        String email;
        @SerializedName("produk")
        String produk;
        @SerializedName("data1")
        String data1;
        @SerializedName("data2")
        String data2;

        public Riwayat(String id_pendaftaran, String tgl_daftar, String nama_lengkap, String email, String produk, String data1, String data2) {
            this.id_pendaftaran = id_pendaftaran;
            this.tgl_daftar = tgl_daftar;
            this.nama_lengkap = nama_lengkap;
            this.email = email;
            this.produk = produk;
            this.data1 = data1;
            this.data2 = data2;
        }

        public String getId_pendaftaran() {
            return id_pendaftaran;
        }

        public void setId_pendaftaran(String id_pendaftaran) {
            this.id_pendaftaran = id_pendaftaran;
        }

        public String getTgl_daftar() {
            return tgl_daftar;
        }

        public void setTgl_daftar(String tgl_daftar) {
            this.tgl_daftar = tgl_daftar;
        }

        public String getNama_lengkap() {
            return nama_lengkap;
        }

        public void setNama_lengkap(String nama_lengkap) {
            this.nama_lengkap = nama_lengkap;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProduk() {
            return produk;
        }

        public void setProduk(String produk) {
            this.produk = produk;
        }

        public String getData1() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1 = data1;
        }

        public String getData2() {
            return data2;
        }

        public void setData2(String data2) {
            this.data2 = data2;
        }
    }


    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    String id_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_riwayat);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(RiwayatActivity.this, MainActivity.class);
                startActivity(iback);
            }
        });
        prefManager = new PrefManager(this);
        if (prefManager.isJamaahLoggedIn()==true){
            id_user=prefManager.LoggedInIdJamaah();
        }else if(prefManager.isAgenLoggedIn()==true){
            id_user=prefManager.LoggedInIdAgen();
        }
//        mShimmerViewContainer = findViewById(R.id.shimmer_produk_container);
//        mShimmerViewContainer.startShimmerAnimation();
        final TextView textView = (TextView) findViewById(R.id.textPF);
        final LinearLayout noconnlayout = (LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);


        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<List<Riwayat>> call = myAPIService.getRiwayat(id_user);
        call.enqueue(new Callback<List<Riwayat>>() {

            @Override
            public void onResponse(Call<List<Riwayat>> call, Response<List<Riwayat>> response) {
                if (response.body() != null) {
//                    mShimmerViewContainer.stopShimmerAnimation();
//                    mShimmerViewContainer.setVisibility(View.GONE);
                    populateGridView(response.body());
                } else {
                    textView.setText("Produk belum ada");
                }
            }

            @Override
            public void onFailure(Call<List<Riwayat>> call, Throwable throwable) {
//                mShimmerViewContainer.stopShimmerAnimation();
//                mShimmerViewContainer.setVisibility(View.GONE);
                noconnlayout.setVisibility(View.VISIBLE);
                Button btRefresh = (Button) findViewById(R.id.btRefresh);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentcart = new Intent(RiwayatActivity.this, RiwayatActivity.class);
                        intentcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentcart);
                    }
                });
            }
        });

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mShimmerViewContainer.startShimmerAnimation();
//    }
//
//    @Override
//    protected void onPause() {
//        mShimmerViewContainer.stopShimmerAnimation();
//        super.onPause();
//    }


    private GridViewAdapter adapter;
    private GridView mGridView;


    private void populateGridView(List<Riwayat> riwayatList) {
        mGridView = findViewById(R.id.GvRiwayat);
        adapter = new GridViewAdapter(this, riwayatList);
        mGridView.setAdapter(adapter);
    }

    class GridViewAdapter extends BaseAdapter {

        private List<Riwayat> riwayats;
        private Context context;

        public GridViewAdapter(Context context, List<Riwayat> riwayats) {
            this.context = context;
            this.riwayats = riwayats;
        }

        @Override
        public int getCount() {
            return riwayats.size();
        }

        @Override
        public Object getItem(int pos) {
            return riwayats.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_adapter_riwayat, viewGroup, false);
            }
            TextView TxvTglDaftar= view.findViewById(R.id.TxvTglDaftar);
            TextView TxvIdPendaftaran= view.findViewById(R.id.TxvIdPendaftaran);
            TextView TxvProduk= view.findViewById(R.id.TxvProduk);
            TextView TxvNamaLengkap= view.findViewById(R.id.TxvNamaLengkap);
            TextView TxvEmail= view.findViewById(R.id.TxvEmail);
            TextView TxvData1= view.findViewById(R.id.TxvData1);
            TextView TxvData2= view.findViewById(R.id.TxvData2);


            final Riwayat thisRiwayat = riwayats.get(position);

            TxvTglDaftar.setText(thisRiwayat.getTgl_daftar());
            TxvIdPendaftaran.setText("ID Pendaftaran : "+thisRiwayat.getId_pendaftaran());
            TxvProduk.setText(thisRiwayat.getProduk());
            TxvNamaLengkap.setText("Nama : "+thisRiwayat.getNama_lengkap());
            TxvEmail.setText("Email : "+thisRiwayat.getEmail());
            TxvData1.setText(thisRiwayat.getData1());
            TxvData2.setText(thisRiwayat.getData2());


//            btPesan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intentp = new Intent(ProdukActivity.this, DetailActivity.class);
////                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intentp.putExtra("id_produk", thisProduk.getId());
//                    startActivity(intentp);
//                }
//            });

            return view;
        }
    }



}

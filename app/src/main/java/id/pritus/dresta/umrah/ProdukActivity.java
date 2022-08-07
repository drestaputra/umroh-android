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

import id.pritus.dresta.umrah.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 4/15/2017.
 */

public class ProdukActivity extends AppCompatActivity {


    interface MyAPIService {
        @GET("produk")
        Call<GeneralResponse> getProduk();
    }


    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_produk);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(ProdukActivity.this, MainActivity.class);
                startActivity(iback);
            }
        });
        mShimmerViewContainer = findViewById(R.id.shimmer_produk_container);
        mShimmerViewContainer.startShimmerAnimation();
        final TextView textView = (TextView) findViewById(R.id.textPF);
        final LinearLayout noconnlayout = (LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);


        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<GeneralResponse> call = myAPIService.getProduk();
        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<Produk> produks = response.body().getData(Produk.class);
                    populateGridView(produks);
                } else {
                    textView.setText("Produk belum ada");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable throwable) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                noconnlayout.setVisibility(View.VISIBLE);
                Button btRefresh = (Button) findViewById(R.id.btRefresh);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentcart = new Intent(ProdukActivity.this, ProdukActivity.class);
                        intentcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentcart);
                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


    private GridViewAdapter adapter;
    private GridView mGridView;


    private void populateGridView(List<Produk> produkList) {
        mGridView = findViewById(R.id.fav_products);
        adapter = new GridViewAdapter(this, produkList);
        mGridView.setAdapter(adapter);
    }

    class GridViewAdapter extends BaseAdapter {

        private List<Produk> produks;
        private Context context;

        public GridViewAdapter(Context context, List<Produk> produks) {
            this.context = context;
            this.produks = produks;
        }

        @Override
        public int getCount() {
            return produks.size();
        }

        @Override
        public Object getItem(int pos) {
            return produks.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_adapter_produk, viewGroup, false);
            }
            Button btPesan = view.findViewById(R.id.btPesan);
            TextView Txvnama_produk= view.findViewById(R.id.Txvnama_produk);
            TextView Txvharga_produk = view.findViewById(R.id.Txvharga_produk);
            TextView Txvharga_coret = view.findViewById(R.id.Txvharga_coret);
            TextView TxvSisaSeat = view.findViewById(R.id.TxvSisaSeat);
            TextView TxvFasilitas= view.findViewById(R.id.TxvFasilitas);
            // CheckBox chkTechExists = view.findViewById(R.id.myCheckBox);
            ImageView spacecraftImageView = view.findViewById(R.id.Imvfoto_produk);

            final Produk thisProduk = produks.get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TxvFasilitas.setText(Html.fromHtml(thisProduk.getFasilitas(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                TxvFasilitas.setText(Html.fromHtml(thisProduk.getFasilitas()));
            }
            Txvnama_produk.setText(thisProduk.getName());
            String hargarpp = NumberFormat.getInstance().format(Integer.parseInt(thisProduk.getHarga_produk()));
            String harga_coret_rpp = "0";
            if (thisProduk.getHarga_coret() != null){
                harga_coret_rpp = NumberFormat.getInstance().format(Integer.parseInt(thisProduk.getHarga_coret()));
            }
            String sisa_seat_nf = "0";
            if (thisProduk.getSisa_seat() != null){
                sisa_seat_nf = NumberFormat.getInstance().format(Integer.parseInt(thisProduk.getSisa_seat()));
            }

            Txvharga_produk.setText("Rp. " + hargarpp);
            if (harga_coret_rpp.equals("0")){
                Txvharga_coret.setVisibility(View.GONE);
            }else{
                Txvharga_coret.setText("Rp. " + harga_coret_rpp);
            }
            if (sisa_seat_nf.equals("0")){
                TxvSisaSeat.setVisibility(View.GONE);
            }else{
                TxvSisaSeat.setText("Sisa seat: " + sisa_seat_nf);
            }

            if (thisProduk.getImageURL() != null && thisProduk.getImageURL().length() > 0) {

                Picasso.with(context).load(thisProduk.getImageURL()).placeholder(R.color.greycustom2).into(spacecraftImageView);
                Log.d("foto_produk2", thisProduk.getImageURL());
            } else {
                Picasso.with(context).load(R.color.greycustom2).into(spacecraftImageView);
            }

            btPesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentp = new Intent(ProdukActivity.this, DetailActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentp.putExtra("id_produk", thisProduk.getId());
                    startActivity(intentp);
                }
            });

            return view;
        }
    }

    class Produk {
        /*
        INSTANCE FIELDS
         */
//        DecimalFormat formatter = new DecimalFormat("#.###.###");
        @SerializedName("id_produk")
        private String id_produk;
        @SerializedName("nama_produk")
        private String nama_produk;
        @SerializedName("fasilitas")
        private String fasilitas;
        @SerializedName("hak_calon_jamaah")
        private String hak_calon_jamaah;
        @SerializedName("syarat_ketentuan")
        private String syarat_ketentuan;
        @SerializedName("deskripsi_produk")
        private String deskripsi_produk;
        @SerializedName("harga_produk")
        private String harga_produk;
        @SerializedName("harga_coret")
        private String harga_coret;
        @SerializedName("foto_produk")
        private String foto_produk;
        @SerializedName("sisa_seat")
        private String sisa_seat;

        public Produk(String id_produk, String nama_produk, String fasilitas, String hak_calon_jamaah, String syarat_ketentuan, String deskripsi_produk, String harga_produk, String harga_coret, String foto_produk, String sisa_seat) {
            this.id_produk = id_produk;
            this.nama_produk = nama_produk;
            this.fasilitas = fasilitas;
            this.hak_calon_jamaah = hak_calon_jamaah;
            this.syarat_ketentuan = syarat_ketentuan;
            this.deskripsi_produk = deskripsi_produk;
            this.harga_produk = harga_produk;
            this.harga_coret = harga_coret;
            this.foto_produk = foto_produk;
            this.sisa_seat = sisa_seat;
        }

        /*
         *GETTERS AND SETTERS
         */

        public String getSisa_seat() {
            return sisa_seat;
        }

        public void setSisa_seat(String sisa_seat) {
            this.sisa_seat = sisa_seat;
        }

        public String getId() {
            return id_produk;
        }

        public void setId(String id) {
            this.id_produk = id;
        }

        public String getName() {
            return nama_produk;
        }

        public void setName(String nama_produk) {
            this.nama_produk = nama_produk;
        }

        public String getHarga_produk() {
            return harga_produk;
        }

        public void setHarga_produk(String harga_produk) {
            this.harga_produk = harga_produk;
        }

        public String getHarga_coret() {
            return harga_coret;
        }

        public void setHarga_coret(String harga_coret) {
            this.harga_coret = harga_coret;
        }

        public String getFasilitas() {
            return fasilitas;
        }

        public void setFasilitas(String fasilitas) {
            this.fasilitas = fasilitas;
        }

        public String getHak_calon_jamaah() {
            return hak_calon_jamaah;
        }

        public void setHak_calon_jamaah(String hak_calon_jamaah) {
            this.hak_calon_jamaah = hak_calon_jamaah;
        }

        public String getSyarat_ketentuan() {
            return syarat_ketentuan;
        }

        public void setSyarat_ketentuan(String syarat_ketentuan) {
            this.syarat_ketentuan = syarat_ketentuan;
        }

        public String getImageURL() {
            return foto_produk;
        }

        public String getDeskripsi_produk() {
            return deskripsi_produk;
        }

        public void setDeskripsi_produk(String deskripsi_produk) {
            this.deskripsi_produk = deskripsi_produk;
        }
    }

}

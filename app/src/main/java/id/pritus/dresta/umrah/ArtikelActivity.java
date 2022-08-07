package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.pritus.dresta.umrah.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ArtikelActivity extends AppCompatActivity {
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView recyclerView;

    interface MyAPIService{
        @GET("artikel")
        Call<GeneralResponse>getArtikel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);
        mShimmerViewContainer = findViewById(R.id.shimmer_produk_container);
        mShimmerViewContainer.startShimmerAnimation();
        final TextView textView = (TextView) findViewById(R.id.textPF);
        final LinearLayout noconnlayout = (LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<GeneralResponse> call = myAPIService.getArtikel();
        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    List<Artikel> artikels = response.body().getData(Artikel.class);
                    populateGridView(artikels);
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
                        Intent intentcart = new Intent(ArtikelActivity.this, ArtikelActivity.class);
                        intentcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentcart);
                    }
                });
            }
        });

    }

    private GridViewAdapter adapter;
    private GridView mGridView;

    private void populateGridView(List<Artikel> artikels) {
        mGridView = findViewById(R.id.GvArtikel);
        adapter = new GridViewAdapter(this, artikels);
        mGridView.setAdapter(adapter);
    }

    class GridViewAdapter extends BaseAdapter {

        private List<Artikel> artikels;
        private Context context;

        public GridViewAdapter(Context context, List<Artikel> artikels) {
            this.context = context;
            this.artikels = artikels;
        }

        @Override
        public int getCount() {
            return artikels.size();
        }

        @Override
        public Object getItem(int pos) {
            return artikels.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.recycle_adapter_artikel, viewGroup, false);
            }
            TextView TxHead = view.findViewById(R.id.TxHead);
            if  (position==0){
                TxHead.setVisibility(View.VISIBLE);
            }
            ImageView IvGambarArtikel = view.findViewById(R.id.IvGambarArtikel);
            TextView TxvJudulArtikel= view.findViewById(R.id.TxvJudulArtikel);
            TextView TxvIsiArtikel = view.findViewById(R.id.TxvIsiArtikel);
            TextView TxvTanggalArtikel = view.findViewById(R.id.TxvTanggalArtikel);
            TextView TxvPenulisArtikel = view.findViewById(R.id.TxvPenulisArtikel);

            final Artikel thisArtikel = artikels.get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TxvIsiArtikel.setText(Html.fromHtml(thisArtikel.getIsi_artikel(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                TxvIsiArtikel.setText(Html.fromHtml(thisArtikel.getIsi_artikel()));
            }
            TxvJudulArtikel.setText(thisArtikel.getJudul_artikel());
            TxvPenulisArtikel.setText("Oleh "+thisArtikel.getpenulis_artikel());
            TxvTanggalArtikel.setText(thisArtikel.gettgl_artikel());

            if (thisArtikel.getGambar_artikel() != null && thisArtikel.getGambar_artikel().length() > 0) {

                Picasso.with(context).load(thisArtikel.getGambar_artikel()).placeholder(R.color.greycustom2).into(IvGambarArtikel);
            } else {
                Picasso.with(context).load(R.color.greycustom2).into(IvGambarArtikel);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intenta = new Intent(getApplication().getApplicationContext(), DetailBeritaActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intenta.putExtra("id_artikel", thisArtikel.getId_artikel());
                    startActivity(intenta);
                }
            });

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
    class Artikel{
        @SerializedName("id_artikel")
        String id_artikel;
        @SerializedName("tgl_artikel")
        String tgl_artikel;
        @SerializedName("judul_artikel")
        String judul_artikel;
        @SerializedName("gambar_artikel")
        String gambar_artikel;
        @SerializedName("isi_artikel")
        String isi_artikel;
        @SerializedName("penulis_artikel")
        String penulis_artikel;

        public Artikel(String id_artikel, String tgl_artikel, String judul_artikel, String gambar_artikel, String isi_artikel, String penulis_artikel) {
            this.id_artikel = id_artikel;
            this.tgl_artikel = tgl_artikel;
            this.judul_artikel = judul_artikel;
            this.gambar_artikel = gambar_artikel;
            this.isi_artikel = isi_artikel;
            this.penulis_artikel = penulis_artikel;
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

        public String getGambar_artikel() {

            String gambar_artikel2= gambar_artikel;
            return  gambar_artikel2;
        }

        public void setGambar_artikel(String gambar_artikel) {
            this.gambar_artikel = gambar_artikel;
        }

        public String getIsi_artikel() {
            return isi_artikel;
        }

        public void setIsi_artikel(String isi_artikel) {
            this.isi_artikel = isi_artikel;
        }

        public String getpenulis_artikel() {
            return penulis_artikel;
        }

        public void setpenulis_artikel(String penulis_artikel) {
            this.penulis_artikel = penulis_artikel;
        }
    }

}

package id.pritus.dresta.umrah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import id.pritus.dresta.umrah.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class ManasikActivityNew extends AppCompatActivity {
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manasik_new);
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

        Call<GeneralResponse> call = myAPIService.getManasik();
        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getStatus() == 200){
                        if (response.body().getData() != null){
                            populateGridView(response.body().getData(ProdukActivity.Produk.class));
                        }else{
                            textView.setText("Belum ada panduan");
                        }
                    }
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
                        Intent intentcart = new Intent(ManasikActivityNew.this, ManasikActivityNew.class);
                        intentcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentcart);
                    }
                });
            }
        });
    }

    class GridViewAdapter extends BaseAdapter {

        private List<ProdukActivity.Produk> produks;
        private Context context;

        public GridViewAdapter(Context context,List<ProdukActivity.Produk> produks){
            this.context = context;
            this.produks= produks;
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
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(context).inflate(R.layout.grid_adapter_manasik_new,viewGroup,false);
            }

            ProdukActivity.Produk prod = produks.get(position);
            final String idProduk = prod.getId();
            TextView TxvJudul = view.findViewById(R.id.TxvJudul);
            Button btBaca= view.findViewById(R.id.btBaca);
            btBaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentdoa = new Intent(ManasikActivityNew.this, ManasikActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentdoa.putExtra("posisi",String.valueOf(idProduk));
                    startActivity(intentdoa);
                }
            });

            final ProdukActivity.Produk thisManasik= produks.get(position);

            TxvJudul.setText(thisManasik.getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentdoa = new Intent(ManasikActivityNew.this, ManasikActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentdoa.putExtra("posisi",String.valueOf(idProduk));
                    startActivity(intentdoa);
                }
            });
            return view;
        }
    }


    private GridViewAdapter adapter;
    private GridView mGridView;
    private void populateGridView(List<ProdukActivity.Produk> doaList) {
        mGridView = findViewById(R.id.GvManasik);
        adapter = new GridViewAdapter(this,doaList);
        mGridView.setAdapter(adapter);
    }
    interface MyAPIService {
        @GET("produk/manasik")
        Call<GeneralResponse> getManasik();
    }
}

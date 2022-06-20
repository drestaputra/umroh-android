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

        Call<List<ManasikActivity.Manasik>> call = myAPIService.getManasik();
        call.enqueue(new Callback<List<ManasikActivity.Manasik>>() {

            @Override
            public void onResponse(Call<List<ManasikActivity.Manasik>> call, Response<List<ManasikActivity.Manasik>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    populateGridView(response.body());
                } else {
                    textView.setText("Belum ada panduan");
                }
            }

            @Override
            public void onFailure(Call<List<ManasikActivity.Manasik>> call, Throwable throwable) {
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

        private List<ManasikActivity.Manasik> manasiks;
        private Context context;

        public GridViewAdapter(Context context,List<ManasikActivity.Manasik> manasiks){
            this.context = context;
            this.manasiks= manasiks;
        }

        @Override
        public int getCount() {
            return manasiks.size();
        }

        @Override
        public Object getItem(int pos) {
            return manasiks.get(pos);
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

            TextView TxvJudul = view.findViewById(R.id.TxvJudul);
            Button btBaca= view.findViewById(R.id.btBaca);
            btBaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentdoa = new Intent(ManasikActivityNew.this, ManasikActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentdoa.putExtra("posisi",String.valueOf(position));
                    startActivity(intentdoa);
                }
            });

            final ManasikActivity.Manasik thisManasik= manasiks.get(position);

            TxvJudul.setText(thisManasik.getJudul());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentdoa = new Intent(ManasikActivityNew.this, ManasikActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentdoa.putExtra("posisi",String.valueOf(position));
                    startActivity(intentdoa);
                }
            });
            return view;
        }
    }


    private GridViewAdapter adapter;
    private GridView mGridView;
    private void populateGridView(List<ManasikActivity.Manasik> doaList) {
        mGridView = findViewById(R.id.GvManasik);
        adapter = new GridViewAdapter(this,doaList);
        mGridView.setAdapter(adapter);
    }
    interface MyAPIService {
        @GET("android/manasik/tampil")
        Call<List<ManasikActivity.Manasik>> getManasik();
    }
}

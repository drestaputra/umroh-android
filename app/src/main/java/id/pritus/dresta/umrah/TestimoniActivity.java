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
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 4/15/2017.
 */

public class TestimoniActivity extends AppCompatActivity {

    interface MyAPIService {
        @GET("android/testimoni/tampil")
        Call<List<Testimoni>> getTestimoni();
    }

    private ShimmerFrameLayout mShimmerViewContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_testimoni);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback= new Intent(TestimoniActivity.this,MainActivity.class);
                startActivity(iback);
            }
        });
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        final TextView textView= (TextView) findViewById(R.id.textPF);
        final LinearLayout noconnlayout=(LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);





        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<List<Testimoni>> call = myAPIService.getTestimoni();
        call.enqueue(new Callback<List<Testimoni>>() {

            @Override
            public void onResponse(Call<List<Testimoni>> call, Response<List<Testimoni>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    populateGridView(response.body());
                }else{
                    textView.setText("Testimoni belum ada");
                }
            }
            @Override
            public void onFailure(Call<List<Testimoni>> call, Throwable throwable) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                noconnlayout.setVisibility(View.VISIBLE);
                Button btRefresh=(Button) findViewById(R.id.btRefresh);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentcart = new Intent(TestimoniActivity.this, TestimoniActivity.class);
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


    private void populateGridView(List<Testimoni> testimoniList) {
        mGridView = findViewById(R.id.fav_products);
        adapter = new GridViewAdapter(this,testimoniList);
        mGridView.setAdapter(adapter);
    }

    class GridViewAdapter extends BaseAdapter {

        private List<Testimoni> testimonis;
        private Context context;

        public GridViewAdapter(Context context,List<Testimoni> testimonis){
            this.context = context;
            this.testimonis= testimonis;
        }

        @Override
        public int getCount() {
            return testimonis.size();
        }

        @Override
        public Object getItem(int pos) {
            return testimonis.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=LayoutInflater.from(context).inflate(R.layout.grid_adapter_testimoni,viewGroup,false);
            }

            TextView Txvnamatester = view.findViewById(R.id.TxvNamaTester);
            TextView Txvisitestimoni = view.findViewById(R.id.TxvIsiTestimoni);
            ImageView Ivfoto_tester = view.findViewById(R.id.Ivfoto_tester);

            // CheckBox chkTechExists = view.findViewById(R.id.myCheckBox);
            ImageView spacecraftImageView = view.findViewById(R.id.Imvfoto_produk);

            final Testimoni thisTestimoni= testimonis.get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Txvisitestimoni.setText(Html.fromHtml(thisTestimoni.getIsi_testimoni(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                Txvisitestimoni.setText(Html.fromHtml(thisTestimoni.getIsi_testimoni()));
            }
            Txvnamatester.setText(thisTestimoni.getName());
            if(thisTestimoni.getFoto_tester() != null && thisTestimoni.getFoto_tester().length()>0)
            {

                Picasso.with(context).load(thisTestimoni.getFoto_tester()).placeholder(R.color.greycustom2).into(Ivfoto_tester);
                Log.d("foto_produk2", thisTestimoni.getFoto_tester());
            }else {
                Picasso.with(context).load(R.color.greycustom2).into(Ivfoto_tester);
            }



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentp = new Intent(TestimoniActivity.this, WelcomeActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentp.putExtra("id_testimoni",thisTestimoni.getId());
                    startActivity(intentp);
                }
            });

            return view;
        }
    }

    class Testimoni {
        /*
        INSTANCE FIELDS
         */
//        DecimalFormat formatter = new DecimalFormat("#.###.###");
        @SerializedName("id_testimoni")
        private String id_testimoni;
        @SerializedName("nama_tester")
        private String nama_tester;
        @SerializedName("isi_testimoni")
        private String isi_testimoni;
        @SerializedName("foto_tester")
        private String foto_tester;

        public Testimoni(String id_testimoni,
                      String nama_tester,
                      String isi_testimoni,String foto_tester
                      ) {
            this.id_testimoni= id_testimoni;
            this.nama_tester= nama_tester;
            this.isi_testimoni = isi_testimoni;
            this.foto_tester= foto_tester;
        }


        /*
         *GETTERS AND SETTERS
         */
        public String getId() {
            return id_testimoni;
        }
        public void setId(String id) {
            this.id_testimoni= id;
        }
        public String getName() {
            return nama_tester;
        }
        public void setName(String nama_tester) {
            this.nama_tester= nama_tester;
        }
        public String getIsi_testimoni() {
            return isi_testimoni;
        }
        public void setIsi_testimoni(String isi_testimoni) {
            this.isi_testimoni= isi_testimoni;
        }
        public String getFoto_tester() {
            String foto_tester2="https://admin.biroumrohcilacap.com/assets/img/testimoni/"+foto_tester;
            return foto_tester2;
        }
    }

}


package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class ItineraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(ItineraryActivity.this, MainActivity.class);
                startActivity(iback);
            }
        });
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<List<Itinerary>> call = myAPIService.getItinerary();
        call.enqueue(new Callback<List<Itinerary>>() {

            @Override
            public void onResponse(Call<List<Itinerary>> call, Response<List<Itinerary>> response) {
                if (response.body() != null) {
                    populateGridView(response.body());
                }else{

                }
            }
            @Override
            public void onFailure(Call<List<Itinerary>> call, Throwable throwable) {

            }
        });
    }
    class GridViewAdapter extends BaseAdapter {

        private List<Itinerary> itineraries;
        private Context context;

        public GridViewAdapter(Context context,List<Itinerary> itineraries){
            this.context = context;
            this.itineraries= itineraries;
        }

        @Override
        public int getCount() {
            return itineraries.size();
        }

        @Override
        public Object getItem(int pos) {
            return itineraries.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(context).inflate(R.layout.grid_adapter_produk_itinerary,viewGroup,false);
            }

            TextView Txnama_produk = view.findViewById(R.id.Txvnama_produk);
            Button btBaca= view.findViewById(R.id.btBaca);


            final Itinerary thisItinerary= itineraries.get(position);

            Txnama_produk.setText(thisItinerary.getNama_produk());
            btBaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentproduk = new Intent(ItineraryActivity.this, DetailItineraryActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentproduk.putExtra("id_produk",String.valueOf(thisItinerary.getId_produk()));
                    intentproduk.putExtra("nama_produk",String.valueOf(thisItinerary.getNama_produk()));
                    startActivity(intentproduk);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentproduk = new Intent(ItineraryActivity.this, DetailItineraryActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentproduk.putExtra("id_produk",String.valueOf(thisItinerary.getId_produk()));
                    intentproduk.putExtra("nama_produk",String.valueOf(thisItinerary.getNama_produk()));
                    startActivity(intentproduk);
                }
            });
            return view;
        }
    }
    private GridViewAdapter adapter;
    private GridView mGridView;


    private void populateGridView(List<Itinerary> itineraryList) {
        mGridView = findViewById(R.id.GvItin);
        adapter = new GridViewAdapter(this, itineraryList);
        mGridView.setAdapter(adapter);
    }
    interface MyAPIService {
        @GET("android/itinerary/tampil_produk")
        Call<List<Itinerary>> getItinerary();
    }
    class Itinerary{
        @SerializedName("id_produk")
        private String id_produk;
        @SerializedName("nama_produk")
        private String nama_produk;
        public Itinerary(String id_produk,String nama_produk){
            this.id_produk=id_produk;
            this.nama_produk=nama_produk;
        }
        public String getNama_produk() {
            return nama_produk;
        }
        public void setNama_produk(String nama_produk) {
            this.nama_produk = nama_produk;
        }
        public String getId_produk() {
            return id_produk;
        }
        public void setId_produk(String id_produk) {
            this.id_produk = id_produk;
        }
    }
}

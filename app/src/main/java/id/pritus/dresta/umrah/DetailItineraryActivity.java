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

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class DetailItineraryActivity extends AppCompatActivity {
    String id_produk;
    String nama_program;
    TextView TxvItinerary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_itinerary);
        TxvItinerary = findViewById(R.id.TxvItinerary);
        TxvItinerary.setText("Itinerary "+getIntent().getStringExtra("nama_produk"));
        id_produk= getIntent().getStringExtra("id_produk");
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(DetailItineraryActivity.this, MainActivity.class);
                startActivity(iback);
            }
        });
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<List<Itinerary>> call = myAPIService.getItinerary(id_produk);
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
                view= LayoutInflater.from(context).inflate(R.layout.grid_adapter_detail_itinerary,viewGroup,false);
            }

            TextView TxJudulItinerary= view.findViewById(R.id.TxvJudulItinerary);
            TextView TxIsiItinerary= view.findViewById(R.id.TxvIsiItinerary);
            final Itinerary thisItinerary= itineraries.get(position);
            TxIsiItinerary.setText(thisItinerary.getIsi_itinerary());
            TxJudulItinerary.setText(thisItinerary.getJudul_itinerary());
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
        @FormUrlEncoded
        @POST("android/itinerary/tampil_detail_itin")
        Call<List<Itinerary>> getItinerary(@Field("id_produk") String id_produk);
    }
    class Itinerary{
        @SerializedName("id_itinerary")
        private String id_itinerary;
        @SerializedName("id_produk")
        private String id_produk;
        @SerializedName("judul_itinerary")
        private String judul_itinerary;
        @SerializedName("isi_itinerary")
        private String isi_itinerary;

        public Itinerary(String id_itinerary,String id_produk,String judul_itinerary,String isi_itinerary ) {
            this.id_itinerary = id_itinerary;
            this.id_produk= id_produk;
            this.judul_itinerary= judul_itinerary;
            this.isi_itinerary= isi_itinerary; }

        public String getIsi_itinerary() {
            return isi_itinerary;
        }

        public void setIsi_itinerary(String isi_itinerary) {
            this.isi_itinerary = isi_itinerary;
        }

        public String getId_produk() {
            return id_produk;
        }

        public void setId_produk(String id_produk) {
            this.id_produk = id_produk;
        }

        public String getJudul_itinerary() {
            return judul_itinerary;
        }

        public void setJudul_itinerary(String judul_itinerary) { this.judul_itinerary = judul_itinerary; }

        public String getId_itinerary() {
            return id_itinerary;
        }
        public void setId_itinerary(String id_itinerary) { this.id_itinerary = id_itinerary; }
    }
}
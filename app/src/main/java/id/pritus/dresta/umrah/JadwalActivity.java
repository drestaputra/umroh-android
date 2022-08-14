package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.pritus.dresta.umrah.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class JadwalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(JadwalActivity.this, MainActivity.class);
                startActivity(iback);
            }
        });
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<GeneralResponse> call = myAPIService.getJadwal();
        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body() != null) {
                    populateGridView(response.body().getData(Jadwal.class));
                }else{

                }
            }
            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable throwable) {
                Toast.makeText(JadwalActivity.this, "Belum ada data untuk saaat ini", Toast.LENGTH_LONG).show();
            }
        });
    }
    class GridViewAdapter extends BaseAdapter {

        private List<Jadwal> jadwals;
        private Context context;

        public GridViewAdapter(Context context,List<Jadwal> jadwals){
            this.context = context;
            this.jadwals= jadwals;
        }

        @Override
        public int getCount() {
            return jadwals.size();
        }

        @Override
        public Object getItem(int pos) {
            return jadwals.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(context).inflate(R.layout.grid_adapter_jadwal,viewGroup,false);
            }

            TextView TxvProgram= view.findViewById(R.id.TxvProgram);
            TextView TxvMaskapai= view.findViewById(R.id.TxvMaskapai);
            TextView TxvTglJadwal= view.findViewById(R.id.TxvTglJadwal);


            final Jadwal thisJadwal= jadwals.get(position);

            TxvProgram.setText(thisJadwal.getProgram());
            TxvMaskapai.setText("Maskapai : "+thisJadwal.getMaskapai());
            TxvTglJadwal.setText(thisJadwal.getTgl_jadwal());
            return view;
        }
    }
    private GridViewAdapter adapter;
    private GridView mGridView;


    private void populateGridView(List<Jadwal> jadwalList) {
        mGridView = findViewById(R.id.GvJadwal);
        adapter = new GridViewAdapter(this, jadwalList);
        mGridView.setAdapter(adapter);
    }
    interface MyAPIService {
        @GET("jadwal")
        Call<GeneralResponse> getJadwal();
    }
    class Jadwal{
        @SerializedName("id_jadwal")
        private String id_jadwal;
        @SerializedName("tgl_jadwal")
        private String tgl_jadwal;
        @SerializedName("program")
        private String program;
        @SerializedName("maskapai")
        private String maskapai;

        public Jadwal(String id_jadwal, String tgl_jadwal, String program, String maskapai) {
            this.id_jadwal = id_jadwal;
            this.tgl_jadwal = tgl_jadwal;
            this.program = program;
            this.maskapai = maskapai;
        }

        public String getId_jadwal() {
            return id_jadwal;
        }

        public void setId_jadwal(String id_jadwal) {
            this.id_jadwal = id_jadwal;
        }

        public String getTgl_jadwal() {
            return tgl_jadwal;
        }

        public void setTgl_jadwal(String tgl_jadwal) {
            this.tgl_jadwal = tgl_jadwal;
        }

        public String getProgram() {
            return program;
        }

        public void setProgram(String program) {
            this.program = program;
        }

        public String getMaskapai() {
            return maskapai;
        }

        public void setMaskapai(String maskapai) {
            this.maskapai = maskapai;
        }
    }
}

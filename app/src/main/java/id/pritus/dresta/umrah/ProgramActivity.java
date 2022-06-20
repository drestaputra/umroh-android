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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by User on 4/15/2017.
 */

public class ProgramActivity extends AppCompatActivity {


    interface MyAPIService {
        @GET("android/program/tampil")
        Call<List<Program>> getProgram();
    }


    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_program);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(ProgramActivity.this, MainActivity.class);
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

        Call<List<Program>> call = myAPIService.getProgram();
        call.enqueue(new Callback<List<Program>>() {

            @Override
            public void onResponse(Call<List<Program>> call, Response<List<Program>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    populateGridView(response.body());
                } else {
                    textView.setText("Produk belum ada");
                }
            }

            @Override
            public void onFailure(Call<List<Program>> call, Throwable throwable) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                noconnlayout.setVisibility(View.VISIBLE);
                Button btRefresh = (Button) findViewById(R.id.btRefresh);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentcart = new Intent(ProgramActivity.this, ProgramActivity.class);
                        intentcart.addFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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


    private void populateGridView(List<Program> programList) {
        mGridView = findViewById(R.id.GvProgram);
        adapter = new GridViewAdapter(this, programList);
        mGridView.setAdapter(adapter);
    }

    class GridViewAdapter extends BaseAdapter {

        private List<Program> programs;
        private Context context;

        public GridViewAdapter(Context context, List<Program> programs) {
            this.context = context;
            this.programs= programs;
        }

        @Override
        public int getCount() {
            return programs.size();
        }

        @Override
        public Object getItem(int pos) {
            return programs.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_adapter_program, viewGroup, false);
            }
            Button btBaca = view.findViewById(R.id.btBaca);
            TextView Txvnama_program= view.findViewById(R.id.Txvnama_program);
            TextView TxvDeskripsiProgram= view.findViewById(R.id.TxvDeskripsiProgram);

            final Program thisProgram= programs.get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TxvDeskripsiProgram.setText(Html.fromHtml(thisProgram.getDeskripsi_program(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                TxvDeskripsiProgram.setText(Html.fromHtml(thisProgram.getDeskripsi_program()));
            }
            Txvnama_program.setText(thisProgram.getNama_program());
            ;

            btBaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentp = new Intent(ProgramActivity.this, DetailProgramActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentp.putExtra("id_program", thisProgram.getId_program());
                    startActivity(intentp);
                }
            });

            return view;
        }
    }

    class Program {
        /*
        INSTANCE FIELDS
         */
//        DecimalFormat formatter = new DecimalFormat("#.###.###");
        @SerializedName("id_program")
        private String id_program;
        @SerializedName("nama_program")
        private String nama_program;
        @SerializedName("deskripsi_program")
        private String deskripsi_program;
        @SerializedName("cara_pendaftaran")
        private String cara_pendaftaran;
        @SerializedName("ketentuan")
        private String ketentuan;

        public Program(String id_program, String nama_program, String deskripsi_program, String cara_pendaftaran, String ketentuan) {
            this.id_program = id_program;
            this.nama_program = nama_program;
            this.deskripsi_program = deskripsi_program;
            this.cara_pendaftaran = cara_pendaftaran;
            this.ketentuan = ketentuan;
        }

        public String getId_program() {
            return id_program;
        }

        public void setId_program(String id_program) {
            this.id_program = id_program;
        }

        public String getNama_program() {
            return nama_program;
        }

        public void setNama_program(String nama_program) {
            this.nama_program = nama_program;
        }

        public String getDeskripsi_program() {
            return deskripsi_program;
        }

        public void setDeskripsi_program(String deskripsi_program) {
            this.deskripsi_program = deskripsi_program;
        }

        public String getCara_pendaftaran() {
            return cara_pendaftaran;
        }

        public void setCara_pendaftaran(String cara_pendaftaran) {
            this.cara_pendaftaran = cara_pendaftaran;
        }

        public String getKetentuan() {
            return ketentuan;
        }

        public void setKetentuan(String ketentuan) {
            this.ketentuan = ketentuan;
        }
    }

}

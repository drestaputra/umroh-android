package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.NumberFormat;
import java.util.List;
import com.facebook.shimmer.ShimmerFrameLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DetailProgramActivity extends AppCompatActivity {
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5;
    TextView TxvNamaProgram,TxvDeskrisiProgram,TxvCaraPendaftaran,TxvKetentuan;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    String id_pendaftar;
    interface MyAPIService {
        @FormUrlEncoded
        @POST("android/program/detail")
        Call<List<Program>> getProgram(@Field("id_program") String id_program);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_program);
        final ConstraintLayout constraintcontent= (ConstraintLayout) findViewById(R.id.constraintcontent);
        constraintcontent.setVisibility(View.GONE);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback= new Intent(DetailProgramActivity.this,ProgramActivity.class);
                startActivity(iback);
            }
        });
        mShimmerViewContainer = findViewById(R.id.shimmer_produk_container);
        mShimmerViewContainer.startShimmerAnimation();

        TxvNamaProgram=(TextView) findViewById(R.id.TxvNamaProgram);
        TxvDeskrisiProgram=(TextView) findViewById(R.id.TxvDeskripsiProgram);
        TxvCaraPendaftaran=(TextView) findViewById(R.id.TxvCaraPendaftaran);
        TxvKetentuan=(TextView) findViewById(R.id.TxvKetentuan);

        final LinearLayout noconnlayout=(LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);
        prefManager = new PrefManager(this);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        final String id_program= getIntent().getStringExtra("id_program");
        Call<List<Program>> call = myAPIService.getProgram(id_program);
        call.enqueue(new Callback<List<Program>>() {

            @Override
            public void onResponse(Call<List<Program>> call, final Response<List<Program>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    constraintcontent.setVisibility(View.VISIBLE);
                    TxvNamaProgram.setText(response.body().get(0).getNama_program());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        TxvDeskrisiProgram.setText(Html.fromHtml(response.body().get(0).getDeskripsi_program(), Html.FROM_HTML_MODE_COMPACT));
                        TxvCaraPendaftaran.setText(Html.fromHtml(response.body().get(0).getCara_pendaftaran(), Html.FROM_HTML_MODE_COMPACT));
                        TxvKetentuan.setText(Html.fromHtml(response.body().get(0).getKetentuan(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        TxvDeskrisiProgram.setText(Html.fromHtml(response.body().get(0).getDeskripsi_program()));
                        TxvCaraPendaftaran.setText(Html.fromHtml(response.body().get(0).getCara_pendaftaran()));
                        TxvKetentuan.setText(Html.fromHtml(response.body().get(0).getKetentuan()));
                    }

                }else{
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Program>> call, Throwable throwable) {

                constraintcontent.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                noconnlayout.setVisibility(View.VISIBLE);
                Button btRefresh=(Button) findViewById(R.id.btRefresh);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentcart = new Intent(DetailProgramActivity.this, DetailProgramActivity.class);
                        intentcart.putExtra("id_program",id_program);
                        intentcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentcart);
                    }
                });
            }
        });

    }
    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
        Integer v = expandableLayout1.getVisibility();
//        0,4,8 (visible,invisible,gone)
        if (v==0){
            expandableLayout1.setVisibility(View.GONE);
        }else{
            expandableLayout1.setVisibility(View.VISIBLE);
        }
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
        Integer v = expandableLayout2.getVisibility();
//        0,4,8 (visible,invisible,gone)
        if (v==0){
            expandableLayout2.setVisibility(View.GONE);
        }else{
            expandableLayout2.setVisibility(View.VISIBLE);
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

package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.Html;
import android.util.AttributeSet;
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
import android.widget.Toast;


import java.text.NumberFormat;
import java.util.List;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.w3c.dom.Text;

import id.pritus.dresta.umrah.model.GeneralSingleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class HajiPlusActivity extends AppCompatActivity {
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5;
    TextView Txvharga_produk,Txvnama_produk,Txvdeskripsi_produk,Txvfasilitas,Txvhak_calon_jamaah,Txvsyarat_ketentuan,Txvharga_coret,TxvSisaSeat;
    ImageView Imvfoto_produk;
    Button buttondaftar,expandablebuttonItinerary;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    String id_pendaftar,id_produk,nama_produk;
    private ConstraintLayout constraintcontent;
    private LinearLayout noconnlayout;
    private MyAPIService myAPIService;
    interface MyAPIService {
        @GET("haji_plus")
        Call<GeneralSingleResponse> getProduk();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        constraintcontent= (ConstraintLayout) findViewById(R.id.constraintcontent);
        constraintcontent.setVisibility(View.GONE);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback= new Intent(HajiPlusActivity.this,ProdukActivity.class);
                startActivity(iback);
            }
        });
        expandablebuttonItinerary= findViewById(R.id.expandablebuttonItinerary);
        buttondaftar = findViewById(R.id.buttondaftar);
        mShimmerViewContainer = findViewById(R.id.shimmer_produk_container);
        mShimmerViewContainer.startShimmerAnimation();
        Txvharga_produk=(TextView) findViewById(R.id.Txvharga_produk);
        Txvharga_coret=(TextView) findViewById(R.id.Txvharga_coret);
        TxvSisaSeat=(TextView) findViewById(R.id.TxvSisaSeat);
        Txvnama_produk=(TextView) findViewById(R.id.Txvnama_produk);
        Txvdeskripsi_produk=(TextView) findViewById(R.id.Txvdeskripsi_produk);
        Imvfoto_produk=(ImageView) findViewById(R.id.Imvfoto_produk);
        Txvfasilitas=(TextView) findViewById(R.id.Txvfasilitas);
        Txvhak_calon_jamaah=(TextView) findViewById(R.id.Txvhak_calon_jamaah);
        Txvsyarat_ketentuan=(TextView) findViewById(R.id.Txvsyarat_ketentuan);
        noconnlayout=(LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);
        prefManager = new PrefManager(this);
        myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        hit();

    }
    private void hit(){
        Call<GeneralSingleResponse> call = myAPIService.getProduk();
        call.enqueue(new Callback<GeneralSingleResponse>() {
            @Override
            public void onResponse(Call<GeneralSingleResponse> call, final Response<GeneralSingleResponse> response) {
                if (response.body() != null) {
                    final HajiPlusActivity.Produk produk = response.body().getData(HajiPlusActivity.Produk.class);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    constraintcontent.setVisibility(View.VISIBLE);

                    String hargarpp= NumberFormat.getInstance().format(Integer.parseInt(produk.getHarga_produk()));;
                    Txvharga_produk.setText("Rp. "+hargarpp);
                    String harga_coret_rpp = NumberFormat.getInstance().format(Integer.parseInt(produk.getHarga_coret()));
                    String sisa_seat_nf = NumberFormat.getInstance().format(Integer.parseInt(produk.getSisa_seat()));
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

                    Txvnama_produk.setText(produk.getName());
                    nama_produk=produk.getName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Txvdeskripsi_produk.setText(Html.fromHtml(produk.getDeskripsi_produk(), Html.FROM_HTML_MODE_COMPACT));
                        Txvfasilitas.setText(Html.fromHtml(produk.getFasilitas(), Html.FROM_HTML_MODE_COMPACT));
                        Txvhak_calon_jamaah.setText(Html.fromHtml(produk.getHak_calon_jamaah(), Html.FROM_HTML_MODE_COMPACT));
                        Txvsyarat_ketentuan.setText(Html.fromHtml(produk.getSyarat_ketentuan(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        Txvdeskripsi_produk.setText(Html.fromHtml(produk.getDeskripsi_produk()));
                        Txvfasilitas.setText(Html.fromHtml(produk.getFasilitas()));
                        Txvhak_calon_jamaah.setText(Html.fromHtml(produk.getHak_calon_jamaah()));
                        Txvsyarat_ketentuan.setText(Html.fromHtml(produk.getSyarat_ketentuan()));
                    }
                    buttondaftar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (prefManager.LoggedInIdJamaah()==null||prefManager.LoggedInIdAgen()==null) {
                                AlertDialog alertDialog = new AlertDialog.Builder(HajiPlusActivity.this)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setMessage("Anda harus login untuk mendaftar..")
                                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent ilogin= new Intent(HajiPlusActivity.this,LoginActivity.class);
                                                ilogin.addFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(ilogin);
                                            }
                                        })//set negative button
                                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        })
                                        .show();
                            }else if (prefManager.LoggedInIdJamaah()!=null){
                                id_pendaftar = prefManager.LoggedInIdJamaah();
                                Intent idaftar = new Intent(HajiPlusActivity.this,WebActivity.class);
                                String urldaftar = produk.getUrl_daftar()+"/"+id_pendaftar;
                                Log.d("urldaftar", urldaftar);
                                idaftar.putExtra("url",urldaftar);
                                startActivity(idaftar);
                            }else if(prefManager.LoggedInIdAgen()!=null){
                                id_pendaftar = prefManager.LoggedInIdAgen();
                                Intent idaftar = new Intent(HajiPlusActivity.this,WebActivity.class);
                                String urldaftar=produk.getUrl_daftar()+"/"+id_pendaftar;
                                idaftar.putExtra("url",urldaftar);
                                Log.d("urldaftar", urldaftar);
                                startActivity(idaftar);
                            }
                        }
                    });

                    if(produk.getImageURL() != null && produk.getImageURL().length()>0)
                    {
                        Picasso.with(getApplicationContext()).load(produk.getImageURL()).placeholder(R.color.greycustom2).into(Imvfoto_produk);
                    }else {
                        Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(Imvfoto_produk);
                    }

                }else{
                    setRefresh();
                }
            }
            @Override
            public void onFailure(Call<GeneralSingleResponse> call, Throwable throwable) {
                setRefresh();
            }
        });
    }
    private void setRefresh(){
        constraintcontent.setVisibility(View.GONE);
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        noconnlayout.setVisibility(View.VISIBLE);
        Button btRefresh=(Button) findViewById(R.id.btRefresh);
        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hit();
            }
        });
    }
    private ExpandableHeightGridView mGridView;





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

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
        Integer v = expandableLayout3.getVisibility();
//        0,4,8 (visible,invisible,gone)
        if (v==0){
            expandableLayout3.setVisibility(View.GONE);
        }else{
            expandableLayout3.setVisibility(View.VISIBLE);
        }
    }
    public void expandableButtonItin(View view) {
        Intent intentproduk = new Intent(HajiPlusActivity.this, DetailItineraryActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentproduk.putExtra("id_produk",String.valueOf(id_produk));
        intentproduk.putExtra("nama_produk",String.valueOf(nama_produk));
        startActivity(intentproduk);
    }

    class Produk {
        /*
        INSTANCE FIELDS
         */
//        DecimalFormat formatter = new DecimalFormat("#.###.###");
        @SerializedName("id_produk")
        private String id_produk;
        @SerializedName("nama_haji_plus")
        private String nama_produk;
        @SerializedName("biaya_sudah_termasuk")
        private String fasilitas;
        @SerializedName("biaya_tidak_termasuk")
        private String hak_calon_jamaah;
        @SerializedName("syarat_ketentuan")
        private String syarat_ketentuan;
        @SerializedName("deskripsi_produk")
        private String deskripsi_produk;
        @SerializedName("harga_haji_plus")
        private String harga_produk;
        @SerializedName("harga_coret")
        private String harga_coret;
        @SerializedName("sisa_seat")
        private String sisa_seat;
        @SerializedName("foto_haji_plus")
        private String foto_produk;
        @SerializedName("url_daftar")
        private String url_daftar;

        public Produk(String id_produk, String nama_produk, String fasilitas, String hak_calon_jamaah, String syarat_ketentuan, String deskripsi_produk, String harga_produk, String harga_coret, String sisa_seat, String foto_produk, String url_daftar) {
            this.id_produk = id_produk;
            this.nama_produk = nama_produk;
            this.fasilitas = fasilitas;
            this.hak_calon_jamaah = hak_calon_jamaah;
            this.syarat_ketentuan = syarat_ketentuan;
            this.deskripsi_produk = deskripsi_produk;
            this.harga_produk = harga_produk;
            this.harga_coret = harga_coret;
            this.sisa_seat = sisa_seat;
            this.foto_produk = foto_produk;
            this.url_daftar = url_daftar;
        }
        /*
         *GETTERS AND SETTERS
         */

        public String getHarga_coret() {
            return harga_coret;
        }

        public void setHarga_coret(String harga_coret) {
            this.harga_coret = harga_coret;
        }

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
            this.harga_produk= harga_produk;
        }
        public String getFasilitas(){
            return fasilitas;
        }
        public void setFasilitas(String fasilitas){
            this.fasilitas=fasilitas;
        }
        public String getHak_calon_jamaah()
        {
            return hak_calon_jamaah;
        }
        public void setHak_calon_jamaah(String hak_calon_jamaah){
            this.hak_calon_jamaah=hak_calon_jamaah;
        }
        public String getSyarat_ketentuan()
        {
            return syarat_ketentuan;
        }
        public void setSyarat_ketentuan(String syarat_ketentuan)
        {
            this.syarat_ketentuan=syarat_ketentuan;
        }
        public String getImageURL() {
            String foto_produk2="https://admin.biroumrohcilacap.com/assets/img/produk/"+foto_produk;
            return foto_produk2;
        }
        public String getDeskripsi_produk() {
            return deskripsi_produk;
        }
        public void setDeskripsi_produk(String deskripsi_produk){
            this.deskripsi_produk=deskripsi_produk;
        }

        public String getUrl_daftar() {
            return url_daftar;
        }

        public void setUrl_daftar(String url_daftar) {
            this.url_daftar = url_daftar;
        }
    }

}

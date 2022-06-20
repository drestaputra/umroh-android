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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DetailActivity extends AppCompatActivity {
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5;
    TextView Txvharga_produk,Txvnama_produk,Txvdeskripsi_produk,Txvfasilitas,Txvhak_calon_jamaah,Txvsyarat_ketentuan,Txvharga_coret,TxvSisaSeat;
    ImageView Imvfoto_produk;
    Button buttondaftar,expandablebuttonItinerary;
    private ShimmerFrameLayout mShimmerViewContainer;
    private PrefManager prefManager;
    String id_pendaftar,id_produk,nama_produk;
    interface MyAPIService {
        @FormUrlEncoded
        @POST("android/produk/detail")
        Call<List<Produk>> getProduk(@Field("id_produk") String id_produk);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final ConstraintLayout constraintcontent= (ConstraintLayout) findViewById(R.id.constraintcontent);
        constraintcontent.setVisibility(View.GONE);
        Button btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback= new Intent(DetailActivity.this,ProdukActivity.class);
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
        final LinearLayout noconnlayout=(LinearLayout) findViewById(R.id.included_nocon);
        noconnlayout.setVisibility(View.GONE);
        prefManager = new PrefManager(this);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        id_produk = getIntent().getStringExtra("id_produk");
        Call<List<Produk>> call = myAPIService.getProduk(id_produk);
        call.enqueue(new Callback<List<Produk>>() {

            @Override
            public void onResponse(Call<List<Produk>> call, final Response<List<Produk>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    constraintcontent.setVisibility(View.VISIBLE);
                    if (response.body().get(0).getItin() !=null){
                        expandablebuttonItinerary.setVisibility(View.VISIBLE);
                    }
                    String hargarpp= NumberFormat.getInstance().format(Integer.parseInt(response.body().get(0).getHarga_produk()));;
                    Txvharga_produk.setText("Rp. "+hargarpp);
                    String harga_coret_rpp = NumberFormat.getInstance().format(Integer.parseInt(response.body().get(0).getHarga_coret()));
                    String sisa_seat_nf = NumberFormat.getInstance().format(Integer.parseInt(response.body().get(0).getSisa_seat()));
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

                    Txvnama_produk.setText(response.body().get(0).getName());
                    nama_produk=response.body().get(0).getName();
                    Txvfasilitas.setText("tes");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Txvdeskripsi_produk.setText(Html.fromHtml(response.body().get(0).getDeskripsi_produk(), Html.FROM_HTML_MODE_COMPACT));
                        Txvfasilitas.setText(Html.fromHtml(response.body().get(0).getFasilitas(), Html.FROM_HTML_MODE_COMPACT));
                        Txvhak_calon_jamaah.setText(Html.fromHtml(response.body().get(0).getHak_calon_jamaah(), Html.FROM_HTML_MODE_COMPACT));
                        Txvsyarat_ketentuan.setText(Html.fromHtml(response.body().get(0).getSyarat_ketentuan(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        Txvdeskripsi_produk.setText(Html.fromHtml(response.body().get(0).getDeskripsi_produk()));
                        Txvfasilitas.setText(Html.fromHtml(response.body().get(0).getFasilitas()));
                        Txvhak_calon_jamaah.setText(Html.fromHtml(response.body().get(0).getHak_calon_jamaah()));
                        Txvsyarat_ketentuan.setText(Html.fromHtml(response.body().get(0).getSyarat_ketentuan()));
                    }
                    buttondaftar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (prefManager.LoggedInIdJamaah()==null||prefManager.LoggedInIdAgen()==null) {
                                AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setMessage("Anda harus login untuk mendaftar..")
                                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent ilogin= new Intent(DetailActivity.this,LoginActivity.class);
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
                                Intent idaftar = new Intent(DetailActivity.this,WebActivity.class);
                                String urldaftar=response.body().get(0).getUrl_daftar()+"/"+id_pendaftar;
                                Log.d("urldaftar", urldaftar);
                                idaftar.putExtra("url",urldaftar);
                                startActivity(idaftar);
                            }else if(prefManager.LoggedInIdAgen()!=null){
                                id_pendaftar = prefManager.LoggedInIdAgen();
                                Intent idaftar = new Intent(DetailActivity.this,WebActivity.class);
                                String urldaftar=response.body().get(0).getUrl_daftar()+"/"+id_pendaftar;
                                idaftar.putExtra("url",urldaftar);
                                Log.d("urldaftar", urldaftar);
                                startActivity(idaftar);
                            }
                        }
                    });

                    if(response.body().get(0).getImageURL() != null && response.body().get(0).getImageURL().length()>0)
                    {
                        Picasso.with(getApplicationContext()).load(response.body().get(0).getImageURL()).placeholder(R.color.greycustom2).into(Imvfoto_produk);
                    }else {
                        Picasso.with(getApplicationContext()).load(R.color.greycustom2).into(Imvfoto_produk);
                    }

                }else{
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Produk>> call, Throwable throwable) {

                constraintcontent.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                noconnlayout.setVisibility(View.VISIBLE);
                Button btRefresh=(Button) findViewById(R.id.btRefresh);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentcart = new Intent(DetailActivity.this, ProdukActivity.class);
//                        intentcart.putExtra("id_produk",thisProduk.getId());
                        intentcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentcart);
                    }
                });
            }
        });

    }
    private GridViewAdapter adapter;
    private ExpandableHeightGridView mGridView;


    private void populateGridView(List<Itinerary> itineraryList) {
//        mGridView = findViewById(R.id.GvItin);
        mGridView.setExpanded(true);
        adapter = new GridViewAdapter(this, itineraryList);
        mGridView.setAdapter(adapter);
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
        Intent intentproduk = new Intent(DetailActivity.this, DetailItineraryActivity.class);
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
        @SerializedName("nama_produk")
        private String nama_produk;
        @SerializedName("fasilitas")
        private String fasilitas;
        @SerializedName("hak_calon_jamaah")
        private String hak_calon_jamaah;
        @SerializedName("syarat_ketentuan")
        private String syarat_ketentuan;
        @SerializedName("deskripsi_produk")
        private String deskripsi_produk;
        @SerializedName("harga_produk")
        private String harga_produk;
        @SerializedName("harga_coret")
        private String harga_coret;
        @SerializedName("sisa_seat")
        private String sisa_seat;
        @SerializedName("foto_produk")
        private String foto_produk;
        @SerializedName("url_daftar")
        private String url_daftar;
        @SerializedName("itin")
        private List<Itinerary> itin;

        public Produk(String id_produk, String nama_produk, String fasilitas, String hak_calon_jamaah, String syarat_ketentuan, String deskripsi_produk, String harga_produk, String harga_coret, String sisa_seat, String foto_produk, String url_daftar, List<Itinerary> itin) {
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
            this.itin = itin;
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

        public List<Itinerary> getItin() { return itin; }
        public void setItin(List<Itinerary> itin) { this.itin = itin; }
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

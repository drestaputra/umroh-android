package id.pritus.dresta.umrah.salat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.List;

import id.pritus.dresta.umrah.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class Tab1Harian extends Fragment {
    String zuhur, ashar, magrib, isya, subuh, tanggal;
    List<Jadwal> jadwal;
    private Context context;
    private GridView gridView;
    private JSONArray SampleProducts;
    private String lokasi;


    public static Tab1Harian newInstance() {
        return new Tab1Harian();
    }
    class GridViewAdapter extends BaseAdapter {

        private List<Jadwal> jadwal;
        private Context context;

        public GridViewAdapter(Context context,List<Jadwal> jadwal){
            this.context = context;
            this.jadwal = jadwal;
        }

        @Override
        public int getCount() {
            return jadwal.size();
        }

        @Override
        public Object getItem(int pos) {
            return jadwal.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=LayoutInflater.from(context).inflate(R.layout.grid_adapter_salat,viewGroup,false);
            }

            TextView TxShubuh = view.findViewById(R.id.TxvSubuh);
            TextView TxDhuhur= view.findViewById(R.id.TxvDhuhur);
            TextView TxAshar= view.findViewById(R.id.TxvAshar);
            TextView TxMaghrib= view.findViewById(R.id.TxvMaghrib);
            TextView TxIsha= view.findViewById(R.id.TxvIsha);
            TextView Txvtanggal= view.findViewById(R.id.TxvTanggal);


            subuh = jadwal.get(0).getFajar();
            zuhur = jadwal.get(0).getZuhur();
            ashar = jadwal.get(0).getAshar();
            magrib = jadwal.get(0).getMaghrib();
            isya = jadwal.get(0).getIsya();
            tanggal = jadwal.get(0).getTanggal();
            TxShubuh.setText(subuh);
            TxDhuhur.setText(zuhur);
            TxAshar.setText(ashar);
            TxMaghrib.setText(magrib);
            TxIsha.setText(isya);
            Txvtanggal.setText(tanggal);

//

            final Jadwal thisJadwal= jadwal.get(position);

//            nameTxt.setText(thisSpacecraft.getName());
//            String hargarpp=NumberFormat.getInstance().format(Integer.parseInt(thisSpacecraft.getPropellant()));;
//            txtPropellant.setText("Rp. "+hargarpp);
            // chkTechExists.setChecked( thisSpacecraft.getTechnologyExists()==1);
            // chkTechExists.setEnabled(false);


            return view;
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.content_fragment_salat, container, false);
        initViews(view);
        lokasi=getArguments().getString("lokasi");
//        lokasi="sewon";
//        Log.d("lokasi", lokasi);
        gridView = (GridView) view.findViewById(R.id.jadwalsk);
//        final String lokasi="sewon";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Items> call = apiInterface.getJadwalSholat(lokasi);
        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                Log.d("Data ", " respon" + response.body().getItems());
                jadwal = response.body().getItems();
                createGridView(jadwal);

                Log.d("respon data ", "" + new Gson().toJson(jadwal));

//                if (jadwal != null) {
//                    zuhur = jadwal.get(0).getZuhur();
//                    ashar = jadwal.get(0).getAshar();
//                    magrib = jadwal.get(0).getMaghrib();
//                    isya = jadwal.get(0).getIsya();
//                    subuh = jadwal.get(0).getFajar();
//                    tanggal = jadwal.get(0).getTanggal();
//                    Log.d("respon :", "" + zuhur);
////                    txtDzuhur.setText(zuhur);
////                    txtAshar.setText(ashar);
////                    txtMaghrib.setText(magrib);
////                    txtIsya.setText(isya);
//                    TxvSubuh.setText(subuh);
//
//                    Log.d("", "lokasi" + lokasi);
//                    tvNamaDaerah.setText(lokasi);
//                    tvTanngal.setText(tanggal);
//
//                } else {
//                    Toast.makeText(getActivity().getApplicationContext(), "Error Network", Toast.LENGTH_SHORT).show();
//                }
                //  loadData(jadwal);
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                Log.d("Data ", "" + t.getMessage());
            }
        });



        return view;
    }

    private void initViews(View view) {


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private GridViewAdapter adapter;
    private GridView mGridView;

    private void createGridView(List<Jadwal> jadwalList){
        // Create grid adopter
        GridViewAdapter productAdapter = new GridViewAdapter( getActivity(), jadwalList );
        // Set grid adapter into GridView
        gridView.setAdapter( productAdapter );
    }
}

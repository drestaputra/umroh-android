package id.pritus.dresta.umrah;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoaFragment extends Fragment {


    public DoaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_doa, container, false);
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<List<Doa>> call = myAPIService.getDoa();
        call.enqueue(new Callback<List<Doa>>() {

            @Override
            public void onResponse(Call<List<Doa>> call, Response<List<Doa>> response) {
                if (response.body() != null) {
                    populateGridView(response.body(),view);
                    Log.d("doalog", response.body().get(0).getNama_doa());
                }else{

                }
            }
            @Override
            public void onFailure(Call<List<Doa>> call, Throwable throwable) {

            }
        });
        return view;
    }
    class Doa{
        @SerializedName("id_doa")
        private String id_doa;
        @SerializedName("nama_doa")
        private String nama_doa;
        @SerializedName("bacaan_doa")
        private String bacaan_doa;
        @SerializedName("arti_doa")
        private String arti_doa;

        public Doa(String id_doa,String nama_doa,String bacaan_doa,String arti_doa){
            this.id_doa=id_doa;
            this.nama_doa=nama_doa;
            this.bacaan_doa=bacaan_doa;
            this.arti_doa=arti_doa;
        }
        public void setId_doa(String id_doa) {
            this.id_doa = id_doa;
        }
        public String getId_doa() {
            return id_doa;
        }
        public void setNama_doa(String nama_doa) {
            this.nama_doa = nama_doa;
        }
        public String getNama_doa() {
            return nama_doa;
        }
        public void setBacaan_doa(String bacaan_doa) {
            this.bacaan_doa = bacaan_doa;
        }
        public String getBacaan_doa() {
            return bacaan_doa;
        }
        public void setArti_doa(String arti_doa) {
            this.arti_doa = arti_doa;
        }
        public String getArti_doa() {
            return arti_doa;
        }
    }
    class GridViewAdapter extends BaseAdapter {

        private List<Doa> doas;
        private Context context;

        public GridViewAdapter(Context context,List<Doa> doas){
            this.context = context;
            this.doas= doas;
        }

        @Override
        public int getCount() {
            return doas.size();
        }

        @Override
        public Object getItem(int pos) {
            return doas.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=LayoutInflater.from(context).inflate(R.layout.grid_adapter_doa,viewGroup,false);
            }

            TextView Txnama_doa = view.findViewById(R.id.Txvnama_doa);
            Button btBaca= view.findViewById(R.id.btBaca);
            btBaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentdoa = new Intent(getActivity().getApplicationContext(), DoaActivity.class);
//                    intentp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentdoa.putExtra("posisi",String.valueOf(position));
                    startActivity(intentdoa);
                }
            });

            final Doa thisDoa= doas.get(position);

            Txnama_doa.setText(thisDoa.getNama_doa());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentdoa = new Intent(getActivity().getApplicationContext(), DoaActivity.class);
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


    private void populateGridView(List<Doa> doaList,View v) {
        mGridView = v.findViewById(R.id.Gvdoa);
        adapter = new GridViewAdapter(getActivity().getApplicationContext(),doaList);
        mGridView.setAdapter(adapter);
    }
    interface MyAPIService {
        @GET("android/doa/tampil")
        Call<List<Doa>> getDoa();
    }



}


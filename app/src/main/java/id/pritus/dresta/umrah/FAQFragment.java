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

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.util.List;

import id.pritus.dresta.umrah.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

import static java.lang.Math.ceil;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {


    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_faq, container, false);
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
//        Integer id_pengguna = getArguments().getInt("position") + 1;

        Call<GeneralResponse> call = myAPIService.getFaq();
        call.enqueue(new Callback<GeneralResponse>() {

            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200){
                        if (response.body().getData() != null){
                            List<Faq> faqList = response.body().getData(Faq.class);
                            populateGridView(faqList,view);
                        }
                    }

                }else{

                }
            }
            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable throwable) {

            }
        });
        return view;
    }
   class Faq{

        @SerializedName("id_faq")
        String id_faq;
        @SerializedName("pertanyaan")
        String pertanyaan;
        @SerializedName("jawaban")
        String jawaban;
        @SerializedName("status")
        String status;

       public Faq(String id_faq, String pertanyaan, String jawaban, String status) {
           this.id_faq = id_faq;
           this.pertanyaan = pertanyaan;
           this.jawaban = jawaban;
           this.status = status;
       }

       public String getId_faq() {
           return id_faq;
       }

       public void setId_faq(String id_faq) {
           this.id_faq = id_faq;
       }

       public String getPertanyaan() {
           return pertanyaan;
       }

       public void setPertanyaan(String pertanyaan) {
           this.pertanyaan = pertanyaan;
       }

       public String getJawaban() {
           return jawaban;
       }

       public void setJawaban(String jawaban) {
           this.jawaban = jawaban;
       }

       public String getStatus() {
           return status;
       }

       public void setStatus(String status) {
           this.status = status;
       }
   }
    ExpandableRelativeLayout expandableLayout;
    class GridViewAdapter extends BaseAdapter {

        private List<Faq> faqs;
        private Context context;

        public GridViewAdapter(Context context,List<Faq> faqs){
            this.context = context;
            this.faqs= faqs;
        }

        @Override
        public int getCount() {
            return faqs.size();
        }

        @Override
        public Object getItem(int pos) {
            return faqs.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=LayoutInflater.from(context).inflate(R.layout.adapter_faq,viewGroup,false);
            }

            final Button expandButton = view.findViewById(R.id.expandButton);
            final ExpandableRelativeLayout expandableLayout= view.findViewById(R.id.expandableLayout);
            TextView TxvJawaban = view.findViewById(R.id.TxvJawaban);

            expandButton.setText(faqs.get(position).getPertanyaan());
            TxvJawaban.setText(faqs.get(position).getJawaban());
            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expandableLayout.toggle(); // toggle expand and collapse
                }
            });


            return view;
        }
    }


    private GridViewAdapter adapter;
    private GridView mGridView;


    private void populateGridView(List<Faq> faqList,View v) {
        mGridView = v.findViewById(R.id.GvFaq);
        adapter = new GridViewAdapter(getActivity().getApplicationContext(),faqList);
        mGridView.setAdapter(adapter);
    }
    interface MyAPIService {
        @GET("faq")
        Call<GeneralResponse> getFaq();
    }



}


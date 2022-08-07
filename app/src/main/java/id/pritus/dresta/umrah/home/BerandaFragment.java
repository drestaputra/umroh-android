package id.pritus.dresta.umrah.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.pritus.dresta.umrah.AkunActivity;
import id.pritus.dresta.umrah.ArtikelActivity;
import id.pritus.dresta.umrah.JadwalActivity;
import id.pritus.dresta.umrah.LegalitasActivity;
import id.pritus.dresta.umrah.LoginActivity;
import id.pritus.dresta.umrah.ManasikActivityNew;
import id.pritus.dresta.umrah.PrefManager;
import id.pritus.dresta.umrah.ProdukActivity;
import id.pritus.dresta.umrah.ProfilFragment;
import id.pritus.dresta.umrah.ProgramActivity;
import id.pritus.dresta.umrah.R;
import id.pritus.dresta.umrah.RetrofitClientInstance;
import id.pritus.dresta.umrah.TestimoniActivity;
import id.pritus.dresta.umrah.model.GeneralPaginationResponse;
import id.pritus.dresta.umrah.salat.SalatActivity;
import id.pritus.dresta.umrah.slider.data.Customization;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;


public class BerandaFragment extends Fragment {


    private List<View> pageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PageIndicatorView pageIndicatorView;
    private Customization customization;
    private LinearLayout mLinearLayout;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private PrefManager prefManager;
    private LinearLayout LbtProduk, LbtJadwal, LbtPanduan, LbtItin, LbtProgram, LbtLogin, LbtTestimoni, LbtSholat, LbtLegalitas, LbtAkun,LbtArtikel;
    final Fragment fragmentd = new ProfilFragment();
//    final Fragment fragmenta = new BerandaFragment();
//    final FragmentManager fm2 = getFragmentManager();
//    final FragmentManager fm2 = getActivity().getSupportFragmentManager();
    public BerandaFragment() {
        // Required empty public constructor
    }

    interface MyAPIService {
        @GET("slider")
        Call<GeneralPaginationResponse> getSlider();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
//        customization = new Customization();
        prefManager = new PrefManager(getActivity().getApplicationContext());
        LbtProduk = (LinearLayout) view.findViewById(R.id.LbtProduk);
        LbtProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iproduk = new Intent(getActivity().getApplicationContext(), ProdukActivity.class);
                startActivity(Iproduk);
            }
        });
        LbtJadwal = (LinearLayout) view.findViewById(R.id.LbtJadwal);
        LbtJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ijadwal=new Intent(getActivity().getApplicationContext(), JadwalActivity.class);
                startActivity(Ijadwal);
            }
        });
        LbtPanduan = (LinearLayout) view.findViewById(R.id.LbtPanduan);
        LbtPanduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Imanasik=new Intent(getActivity().getApplicationContext(), ManasikActivityNew.class);
                startActivity(Imanasik);
            }
        });

//        LbtItin = (LinearLayout) view.findViewById(R.id.LbtItin);
//        LbtItin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent Iitin=new Intent(getActivity().getApplicationContext(), ItineraryActivity.class);
//                startActivity(Iitin);
//            }
//        });
        LbtProgram = (LinearLayout) view.findViewById(R.id.LbtProgram);
        LbtProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iprogram=new Intent(getActivity().getApplicationContext(), ProgramActivity.class);
                startActivity(Iprogram);
            }
        });
        LbtArtikel= (LinearLayout) view.findViewById(R.id.LbtArtikel);
        LbtArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iartikel=new Intent(getActivity().getApplicationContext(), ArtikelActivity.class);
                startActivity(Iartikel);
            }
        });
        LbtLogin = (LinearLayout) view.findViewById(R.id.LbtLogin);

        LbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ilogin = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(Ilogin);
            }
        });
        LbtAkun = (LinearLayout) view.findViewById(R.id.LbtAkun);
        LbtAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ITestimoni = new Intent(getActivity().getApplicationContext(), AkunActivity.class);
                startActivity(ITestimoni);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.main_container, fragmentd, "4")
//                        .addToBackStack("1")
//                        .commit();
//
            }
        });
        if (prefManager.isJamaahLoggedIn()==true||prefManager.isAgenLoggedIn()==true){
            LbtLogin.setVisibility(View.GONE);
        }else{
            LbtAkun.setVisibility(View.GONE);
        }
        LbtTestimoni =  view.findViewById(R.id.LbtTestimoni);
        LbtTestimoni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ITestimoni = new Intent(getActivity().getApplicationContext(), TestimoniActivity.class);
                startActivity(ITestimoni);
            }
        });
        LbtSholat = view.findViewById(R.id.LbtSholat);
        LbtSholat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ishalat = new Intent(getActivity().getApplicationContext(), SalatActivity.class);
                startActivity(Ishalat);
            }
        });
        LbtLegalitas =  view.findViewById(R.id.LbtLegalitas);
        LbtLegalitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Ilegalitas= new Intent(getActivity().getApplicationContext(), LegalitasActivity.class);
                startActivity(Ilegalitas);
            }
        });
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);

        Call<GeneralPaginationResponse> call = myAPIService.getSlider();
        call.enqueue(new Callback<GeneralPaginationResponse>() {
            @Override
            public void onResponse(Call<GeneralPaginationResponse> call, Response<GeneralPaginationResponse> response) {
                if (response.body() != null) {
                    List<Slider> sliders = response.body().getData(Slider.class);
                    init(sliders);
                }else{
//                    textView.setText("Testimoni belum ada");
                }
            }
            @Override
            public void onFailure(Call<GeneralPaginationResponse> call, Throwable throwable) {

            }
        });
        return view;
    }

    private void init(List<Slider> sliderList) {

        mPager = getView().findViewById(R.id.viewPager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity().getApplicationContext(), sliderList));

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius

        NUM_PAGES = sliderList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 7000, 7000);

        // Pager listener over indicator


    }


    private void updateIndicator() {
        pageIndicatorView.setAnimationType(AnimationType.WORM);
        pageIndicatorView.setOrientation(customization.getOrientation());
        pageIndicatorView.setOrientation(customization.getOrientation());
        pageIndicatorView.setRtlMode(customization.getRtlMode());
        pageIndicatorView.setInteractiveAnimation(customization.isInteractiveAnimation());
        pageIndicatorView.setAutoVisibility(customization.isAutoVisibility());
        pageIndicatorView.setFadeOnIdle(customization.isFadeOnIdle());
        if (customization == null) {
            return;
        }
    }

    class Slider {
        //        DecimalFormat formatter = new DecimalFormat("#.###.###");
        @SerializedName("id_slider")
        private String id_slider;

        @SerializedName("url_slider")
        private String url_slider;

        @SerializedName("foto_slider")
        private String foto_slider;

        public Slider(String id_slider, String url_slider, String foto_slider) {
            this.id_slider = id_slider;
            this.url_slider = url_slider;
            this.foto_slider = foto_slider;
        }

        public void setId_slider(String id_slider) {
            this.id_slider = id_slider;
        }

        public String getId_slider() {
            return id_slider;
        }

        public void setUrl_slider(String url_slider) {
            this.url_slider = url_slider;
        }

        public String getUrl_slider() {
            return url_slider;
        }

        public String getFoto_slider() {
            return foto_slider;
        }
    }

    class SlidingImage_Adapter extends PagerAdapter {


        private List<Slider> sliders;
        private LayoutInflater inflater;
        private Context context;


        public SlidingImage_Adapter(Context context, List<Slider> sliders) {
            this.context = context;
            this.sliders = sliders;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return sliders.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.adapter_slider, view, false);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);

            Glide.with(context)
                    .load(sliders.get(position).getFoto_slider())
                    .into(imageView);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Toast.makeText(getContext(), sliders.get(position).getFoto_slider(), Toast.LENGTH_SHORT).show();
//                    Intent browse= new Intent(getActivity().getApplicationContext(), WebActivity.class);
//                    browse.putExtra("url",sliders.get(position).getUrl_slider());
//                    startActivity(browse);
//                }
//            });
            view.addView(imageLayout, 0);

            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }
}

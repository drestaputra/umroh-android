package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import id.pritus.dresta.umrah.slider.customize.CustomizeActivity;
import id.pritus.dresta.umrah.slider.data.Customization;

import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.annotations.SerializedName;
import com.rd.PageIndicatorView;




import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class DoaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DoaAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private PageIndicatorView pageIndicatorView;
    private TextView[] dots;
    private int[] layouts;
    private int jumlahdoa;
    private Button btnBack, btnNext, btBack;
    private PrefManager prefManager;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Customization customization;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doa);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mShimmerViewContainer = findViewById(R.id.shimmer_manasik_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnBack = (Button) findViewById(R.id.btn_back);
        btBack =(Button) findViewById(R.id.btBack);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack.setVisibility(View.INVISIBLE);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iback=new Intent(DoaActivity.this,MainActivity.class);
                startActivity(Iback);
            }
        });
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        Call<List<Doa>> call = myAPIService.getDoa();
        call.enqueue(new Callback<List<Doa>>() {
            @Override
            public void onResponse(Call<List<Doa>> call, Response<List<Doa>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    init(response.body());
                }else{
//                    textView.setText("Testimoni belum ada");
                }
            }
            @Override
            public void onFailure(Call<List<Doa>> call, Throwable throwable) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init(List<Doa> doaListn) {

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(new DoaAdapter(this, doaListn));
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);
        Integer posisi=Integer.valueOf(getIntent().getStringExtra("posisi"));
        mPager.setCurrentItem(posisi);
        jumlahdoa=doaListn.size();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mengecek page terakhir (slide 4)
                // jika activity home sudah tampil
                int current = getItem(+1);
                if (current < jumlahdoa) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {
                    DoaScreen();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(-1);
                if (current < jumlahdoa) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {
                    DoaScreen();
                }
            }
        });
        final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
        NUM_PAGES = doaListn.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(2, true);
            }
        };
    }
//    private void addBottomDots(int currentPage) {
//        dots = new TextView[jumlahdoa];
//        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
//        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < jumlahdoa; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(colorsInactive[currentPage]);
//            dotsLayout.addView(dots[i]);
//        }
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
//    }



    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
    }

    private void DoaScreen() {
//        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        startActivity(new Intent(DoaActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
//            addBottomDots(position);


            // mengubah button lanjut 'NEXT' / 'GOT IT'
            if (position == jumlahdoa - 1) {
                // last page. make button text to GOT IT
                btnNext.setVisibility(View.INVISIBLE);
            } else if(position==0){
                btnBack.setVisibility(View.INVISIBLE);
                btnNext.setVisibility(View.VISIBLE);
            }else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnNext.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    interface MyAPIService {
        @GET("android/doa/tampil")
        Call<List<Doa>> getDoa();
    }

    /**
     * View pager adapter
     */
    class DoaAdapter extends PagerAdapter {


        private List<Doa> doas;
        private LayoutInflater inflater;
        private Context context;


        public DoaAdapter(Context context, List<Doa> doas) {
            this.context = context;
            this.doas= doas;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return doas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.adapter_doa, view, false);

            assert imageLayout != null;
            final TextView Txvjuduldoa = (TextView) imageLayout.findViewById(R.id.Txvjuduldoa);
            final TextView Txvbacaandoa= (TextView) imageLayout.findViewById(R.id.Txvbacaandoa);
            Txvjuduldoa.setText(doas.get(position).getNama_doa());
            final TextView Txvartidoa = (TextView) imageLayout.findViewById(R.id.Txvartidoa);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Txvbacaandoa.setText(Html.fromHtml(doas.get(position).getBacaan_doa(), Html.FROM_HTML_MODE_COMPACT));
                Txvjuduldoa.setText(Html.fromHtml(doas.get(position).getNama_doa(), Html.FROM_HTML_MODE_COMPACT));
                Txvartidoa.setText(Html.fromHtml(doas.get(position).getArti_doa(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                Txvbacaandoa.setText(Html.fromHtml(doas.get(position).getBacaan_doa()));
                Txvjuduldoa.setText(Html.fromHtml(doas.get(position).getNama_doa()));
                Txvartidoa.setText(Html.fromHtml(doas.get(position).getArti_doa()));
            }



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

    class Doa{
        @SerializedName("id_doa")
        private String id_doa;
        @SerializedName("nama_doa")
        private String nama_doa;
        @SerializedName("bacaan_doa")
        private String bacaan_doa;
        @SerializedName("arti_doa")
        private  String arti_doa;

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
}

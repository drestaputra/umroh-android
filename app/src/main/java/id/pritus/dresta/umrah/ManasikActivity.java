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

public class ManasikActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ManasikAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private PageIndicatorView pageIndicatorView;
    private TextView[] dots;
    private int[] layouts;
    private int jumlahmanasik;
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
        setContentView(R.layout.activity_manasik);
        mShimmerViewContainer = findViewById(R.id.shimmer_manasik_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mShimmerViewContainer.startShimmerAnimation();
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnBack = (Button) findViewById(R.id.btn_back);
        btBack =(Button) findViewById(R.id.btBack);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack.setVisibility(View.INVISIBLE);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iback=new Intent(ManasikActivity.this,MainActivity.class);
                startActivity(Iback);
            }
        });
        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);

        Call<List<Manasik>> call = myAPIService.getManasik();
        call.enqueue(new Callback<List<Manasik>>() {
            @Override
            public void onResponse(Call<List<Manasik>> call, Response<List<Manasik>> response) {
                if (response.body() != null) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    init(response.body());
                }else{
//                    textView.setText("Testimoni belum ada");
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Manasik>> call, Throwable throwable) {
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }
    private void init(List<Manasik> manasikListn) {

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(new ManasikAdapter(this, manasikListn));
        Integer posisi=Integer.valueOf(getIntent().getStringExtra("posisi"));
        mPager.setCurrentItem(posisi);
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);
        jumlahmanasik=manasikListn.size();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mengecek page terakhir (slide 4)
                // jika activity home sudah tampil
                int current = getItem(+1);
                if (current < jumlahmanasik) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {
                    ManasikScreen();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(-1);
                if (current < jumlahmanasik) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {
                    ManasikScreen();
                }
            }
        });

        final float density = getResources().getDisplayMetrics().density;


//Set circle indicator radius

        NUM_PAGES = manasikListn.size();

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
//        dots = new TextView[jumlahmanasik];
//
//        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
//        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
//
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < jumlahmanasik; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(colorsInactive[currentPage]);
//            dotsLayout.addView(dots[i]);
//        }
//
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
//    }



    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
    }

    private void ManasikScreen() {
//        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        startActivity(new Intent(ManasikActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
//            addBottomDots(position);


            // mengubah button lanjut 'NEXT' / 'GOT IT'
            if (position == jumlahmanasik - 1) {
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
        @GET("android/manasik/tampil")
        Call<List<Manasik>> getManasik();
    }

    /**
     * View pager adapter
     */
    class ManasikAdapter extends PagerAdapter {


        private List<Manasik> manasiks;
        private LayoutInflater inflater;
        private Context context;


        public ManasikAdapter(Context context, List<Manasik> manasiks) {
            this.context = context;
            this.manasiks= manasiks;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return manasiks.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.adapter_manasik, view, false);

            assert imageLayout != null;
            final TextView Txvjudulm = (TextView) imageLayout.findViewById(R.id.Txvjudulm);
            Txvjudulm.setText(manasiks.get(position).getJudul());
            final TextView Txvisim = (TextView) imageLayout.findViewById(R.id.Txvisim);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Txvisim.setText(Html.fromHtml(manasiks.get(position).getIsi(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                Txvisim.setText(Html.fromHtml(manasiks.get(position).getIsi()));
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

    class Manasik{
        @SerializedName("id_manasik")
        private String id_manasik;
        @SerializedName("judul")
        private String judul;
        @SerializedName("isi")
        private String isi;

        public Manasik(String id_manasik,String judul,String isi){
            this.id_manasik=id_manasik;
            this.judul=judul;
            this.isi=isi;
        }
        public void setId_manasik(String id_manasik){
            this.id_manasik=id_manasik;
        }
        public String getId_manasik(){
            return id_manasik;
        }
        public void setJudul(String judul){
            this.judul=judul;
        }
        public String getJudul(){
            return judul;
        }
        public void setIsi(String isi){
            this.isi=isi;
        }
        public String getIsi(){
            return isi;
        }

    }
}

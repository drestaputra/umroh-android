package id.pritus.dresta.umrah.salat;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import id.pritus.dresta.umrah.R;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String locationDF;
    public PagerAdapter(FragmentManager fm, int NumOfTabs, String lokasi) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.locationDF=lokasi;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putString("lokasi",locationDF);
        switch (position) {
            case 0:
                Tab1Harian tab1 = new Tab1Harian();
                tab1.setArguments(b);
                return tab1;
            case 1:
                Tab2Mingguan tab2 = new Tab2Mingguan();
                tab2.setArguments(b);
                return tab2;
            case 2:
                Tab3Bulanan tab3 = new Tab3Bulanan();
                tab3.setArguments(b);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

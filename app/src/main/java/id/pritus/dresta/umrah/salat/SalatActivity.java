package id.pritus.dresta.umrah.salat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import id.pritus.dresta.umrah.MainActivity;
import pub.devrel.easypermissions.EasyPermissions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
//import   location
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import id.pritus.dresta.umrah.R;


public class SalatActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private String provider,lokasi;
    private TextView txvlokasi;
    private FloatingActionButton fab;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    Context context = this;
    private FusedLocationProviderClient mFusedLocationClient;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(13,2,47));
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        //getSupportActionBar().hide();


    }
    private void tampil(String lokasir,String loc){
        setContentView(R.layout.activity_salat);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(SalatActivity.this,SalatActivity.class);
                startActivity(refresh);

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_salat);
        tabLayout.addTab(tabLayout.newTab().setText("Harian"));
        tabLayout.addTab(tabLayout.newTab().setText("Mingguan"));
        tabLayout.addTab(tabLayout.newTab().setText("Bulanan"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        txvlokasi=(TextView) findViewById(R.id.txvLokasi);
        txvlokasi.setText(loc);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),lokasir);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void fetchLocation() {


        if (ContextCompat.checkSelfPermission(SalatActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SalatActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Membutuhkan Akses Lokasi")
                        .setMessage("Aktifkan lokasi Anda untuk mendapatkan jadwal Shalat yang sesuai lokasi..")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SalatActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
//                                intent ke home
                                Intent Ihome = new Intent(SalatActivity.this,MainActivity.class);
                                startActivity(Ihome);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SalatActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Geocoder gcd3 = new Geocoder(getBaseContext(), Locale.getDefault());
                                List<Address> addresses;

                                try {
                                    addresses = gcd3.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses.size() > 0)

                                    {
                                        //while(locTextView.getText().toString()=="Location") {
//                                        lokasi = addresses.get(0).getLocality().toString();
                                        lokasi = addresses.get(0).getSubAdminArea();
                                        String l=addresses.get(0).getAddressLine(0);
//                                        String lokasir = lokasi.replaceAll("Kecamatan ","");
                                        Log.d("Cek lokasi", "1 :" + lokasi);
                                        tampil(lokasi,l);
//                                        lokasi = addresses.get(0).getLocality().toString();

                                    }

                                } catch (NullPointerException e) {
                                    e.printStackTrace();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // Logic to handle location object
                                Double latittude = location.getLatitude();
                                Double longitude = location.getLongitude();



                            }
//                            masuk di on failure
                            else{
                                tampil("Jakarta","Lokasi Anda Belum AKTIF, Jadwal Berikut Adalah JADWAL SHALAT JAKARTA");
//                                Intent intenth = new Intent(SalatActivity.this, MainActivity.class);
//                                startActivity(intenth);
                            }
                        }
                    });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent1 = new Intent(SalatActivity.this, SalatActivity.class);
                startActivity(intent1);
            }else{

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
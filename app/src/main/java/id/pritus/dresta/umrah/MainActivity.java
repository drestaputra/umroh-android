package id.pritus.dresta.umrah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import id.pritus.dresta.umrah.home.BerandaFragment;

public class MainActivity extends AppCompatActivity {

    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5,expandableLayout6,
            expandableLayout7,expandableLayout8;
    Button expandButton1,expandButton2;
    final Fragment fragment1 = new BerandaFragment();
    final Fragment fragment2 = new KontakFragment();
    final Fragment fragment3 = new FAQFragment();
    final Fragment fragment4 = new ProfilFragment();
    final Fragment fragment5 = new DoaFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new FirebaseNotificationOpenedHandler(this))
                .init();
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.main_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();
    }
    public class FirebaseNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        Context ctx;
        FirebaseNotificationOpenedHandler(Context context) {
            ctx = context;
        }
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            Toast.makeText(ctx, "Pendaftaran sedang diproses...", Toast.LENGTH_SHORT).show();
            if (data != null) {
                String customKey = data.optString("customkey", null);
                String lagikey = data.optString("lagikey", null);
//                if (customKey != null)
////                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
//                if (lagikey != null)
//                    Log.i("OneSignalExample", "lagikey set with value: " + lagikey);
            }
//            if (actionType == OSNotificationAction.ActionType.ActionTaken)
//                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_beranda:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_kontak:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.navigation_faq:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_agen:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
                case R.id.navigation_doa:
                    fm.beginTransaction().hide(active).show(fragment5).commit();
                    active = fragment5;
                    return true;
            }
            return false;
        }
    };
    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }
//    public void expandableButton4(View view) {
//        expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
//        expandableLayout4.toggle(); // toggle expand and collapse
//    }
//    public void expandableButton5(View view) {
//        expandableLayout5 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout5);
//        expandableLayout5.toggle(); // toggle expand and collapse
//    }
//    public void expandableButton6(View view) {
//        expandableLayout6 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout6);
//        expandableLayout6.toggle(); // toggle expand and collapse
//    }
//    public void expandableButton7(View view) {
//        expandableLayout7 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout7);
//        expandableLayout7.toggle(); // toggle expand and collapse
//    }
//    public void expandableButton8(View view) {
//        expandableLayout8 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout8);
//        expandableLayout8.toggle(); // toggle expand and collapse
//    }
}
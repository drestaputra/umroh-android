package id.pritus.dresta.umrah;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


/**
 * A simple {@link Fragment} subclass.
 */
public class KontakFragment extends Fragment {
    Button AddReportBt;
    LinearLayout LbtEmail,LbtInstagram,LbtFacebook,LbtTelepon,LbtTwitter,LbtWhatsapp;




    public KontakFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kontak, container, false);
        LbtEmail = view.findViewById(R.id.LbtEmail);
        LbtInstagram = view.findViewById(R.id.LbtInstagram);
        LbtFacebook = view.findViewById(R.id.LbtFacebook);
        LbtTelepon = view.findViewById(R.id.LbtTelepon);
        LbtTwitter = view.findViewById(R.id.LbtTwitter);
        LbtWhatsapp = view.findViewById(R.id.LbtWhatsapp);
        LbtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","almakwacilacap@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tanya Aplikasi Alamawa");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Haloo, mau nanya nih \n");
                startActivity(Intent.createChooser(emailIntent, "Tanya Aplikasi Alamawa"));
            }
        });
        LbtInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/almakwanu_tour");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/almakwanu_tour")));
                }
            }
        });
        LbtFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a;
                try {
                    getActivity().getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    a = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/AF4DDLX5nXnzUt5QhlwVvZQ"));
                } catch (Exception e) {
                    a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/almakwanu"));
                }
                startActivity(a);
            }
        });
//        LbtTelepon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "5263536"));
//                startActivity(intent);
//            }
//        });
        LbtTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getActivity().getApplicationContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=almakwacilacap"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/almakwacilacap"));
                }
                startActivity(intent);
            }
        });
        LbtWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=6285777738909";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return view;
    }











}

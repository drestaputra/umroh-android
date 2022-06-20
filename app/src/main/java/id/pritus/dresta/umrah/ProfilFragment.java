package id.pritus.dresta.umrah;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.pritus.dresta.umrah.PrefManager;
/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    Button btLogin,btDaftar,btProfil,btAbout,btRiwayat;
    TextView Txlogin,Txusername;
    public ProfilFragment() {
        // Required empty public constructor
    }
    private PrefManager prefManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ConstraintLayout ClMenu= (ConstraintLayout) view.findViewById(R.id.ClMenu);
        LinearLayout Llauth = (LinearLayout) view.findViewById(R.id.Llauth);
        Txusername = view.findViewById(R.id.Txusername);
        Txlogin = view.findViewById(R.id.Txvlogin);
        btLogin= view.findViewById(R.id.btLogin);
        btDaftar= view.findViewById(R.id.btDaftar);
        btProfil = view.findViewById(R.id.btProfil);
        btAbout = view.findViewById(R.id.btAbout);
        btRiwayat = view.findViewById(R.id.btRiwayat);

        prefManager = new PrefManager(getActivity().getApplicationContext());
        Button btnLogout =(Button) view.findViewById(R.id.btLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.logout();
            }
        });
        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IAkun=new Intent(getActivity().getApplicationContext(),AkunActivity.class);
                startActivity(IAkun);
            }
        });
        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IAkun=new Intent(getActivity().getApplicationContext(),PilihActivity.class);
                startActivity(IAkun);
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ilogin=new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                startActivity(Ilogin);
            }
        });
        btRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IRiwayat= new Intent(getActivity().getApplicationContext(),RiwayatActivity.class);
                startActivity(IRiwayat);
            }
        });
        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IAkun=new Intent(getActivity().getApplicationContext(),AkunActivity.class);
                startActivity(IAkun);
            }
        });
        btAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iabout= new Intent(getActivity().getApplicationContext(),AboutActivity.class);
                startActivity(Iabout);
            }
        });

        if (prefManager.LoggedInIdJamaah()==null||prefManager.LoggedInIdAgen()==null) {
//            jika belum login
//            menghilangkan Menu
            ClMenu.setVisibility(View.INVISIBLE);
        }else {
            if (prefManager.LoggedInIdJamaah()!=null){
                Txusername.setText(prefManager.LoggedInJamaahUsername());
            }else{
                Txusername.setText(prefManager.LoggedInAgenUsername());
            }
            ClMenu.setVisibility(View.VISIBLE);
            Llauth.setVisibility(View.GONE);
            Txlogin.setVisibility(View.GONE);
        }
        return view;
    }

}


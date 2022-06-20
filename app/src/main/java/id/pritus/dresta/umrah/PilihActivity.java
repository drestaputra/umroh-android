package id.pritus.dresta.umrah;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class PilihActivity extends AppCompatActivity {

    LinearLayout LbtDaftarAgen, LbtDaftarCalonJamaah;
    Button btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih);
        LbtDaftarAgen = findViewById(R.id.LbtDaftarAgen);
        LbtDaftarCalonJamaah = findViewById(R.id.LbtDaftarCalonJamaah);
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iback = new Intent(PilihActivity.this,MainActivity.class);
                startActivity(Iback);
            }
        });
        LbtDaftarAgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Iagen = new Intent(PilihActivity.this,RegisterActivity.class);
                startActivity(Iagen);
            }
        });
        LbtDaftarCalonJamaah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ijamaah= new Intent(PilihActivity.this,RegisterJamaahActivity.class);
                startActivity(Ijamaah);
            }
        });


    }

}

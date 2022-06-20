package id.pritus.dresta.umrah;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    Button BtBack;
    TextView TxvLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        BtBack = findViewById(R.id.BtBack);
        BtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentl= new Intent(AboutActivity.this,MainActivity.class);
                intentl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentl);
            }
        });
        TxvLink = findViewById(R.id.TxvLink);
        TxvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentl= new Intent(AboutActivity.this,WebActivity.class);
                intentl.putExtra("url","https://www.biroumrohcilacap.com/");
                startActivity(intentl);
            }
        });
    }

}

package id.pritus.dresta.umrah;

/**
 * Created by dresta on 22/11/2017.
 */


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import id.pritus.dresta.umrah.model.GeneralSingleResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;




public class RegisterJamaahActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "RegisterJamaahActivity";

    private static final String[] EXTERNAL_STORAGE_P ={Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_GET_SINGLE_FILE_KTP = 1;
    private static final int REQUEST_GET_SINGLE_FILE_FOTO = 2;
    private static final int RC_EXTERNAL_STORAGE= 10;

    EditText EtNamaLengkap,EtTempatLahir,EtTanggalLahir,EtAlamat,EtNomorHp,EtPekerjaan,EtEmail,EtNomorRekening,EtBank,EtUsername,EtPassword,EtCPassword;
    TextView link_login;
    ImageView IvFotoKtpJamaah,imv,imv2;
    Button btnDaftar,btFotoKtpJamaah,btFotoJamaah,BtTanggalLahir;
    Uri UFotoKtpJamaah,UFotoJamaah;
    File FFotoKTPjamaah,FFotoJamaah;
    String PFFotoKTPJamaah,notif_a_id;
    private RadioGroup RgJenisKelamin;
    private RadioButton RbJenisKelamin;
    private int mYear, mMonth, mDay, mHour, mMinute;


    public class DaftarJamaah{
        private String isSuccess;
        private String error;

        public DaftarJamaah(String isSuccess,String error) {
            this.isSuccess = isSuccess;
            this.error= error;
        }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getIsSuccess() { return isSuccess; }
        public void setIsSuccess(String isSuccess) { this.isSuccess = isSuccess; }
    }

    interface MyAPIService {
        @Multipart
        @POST("daftar/jamaah")
        Call<GeneralSingleResponse> getDaftarjamaah
                (@Part("nama_jamaah") RequestBody description,
                 @Part("notif_app_id") RequestBody notif_app_id,
                @Part("tempat_lahir") RequestBody tempat_lahir,
                @Part("tgl_lahir") RequestBody tgl_lahir,
                @Part("jenis_kelamin") RequestBody jenis_kelamin,
                @Part("alamat") RequestBody alamat,
                @Part("no_hp") RequestBody no_hp,
                @Part("pekerjaan") RequestBody pekerjaan,
                @Part("email") RequestBody email,
                @Part("no_rekening") RequestBody no_rekening,
                @Part("bank_jamaah") RequestBody bank_jamaah,
                @Part("username") RequestBody username,
                @Part("password") RequestBody password,
                @Part MultipartBody.Part file,
                @Part MultipartBody.Part file2
                );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(RegisterJamaahActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},10);

        setContentView(R.layout.activity_register_jamaah);
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.ic_background_login));
        imv = (ImageView) findViewById(R.id.IvFotoKtpJamaah);
        imv2 = (ImageView) findViewById(R.id.IvFotoJamaah);
        imv.setVisibility(View.GONE);
        imv2.setVisibility(View.GONE);

        btFotoKtpJamaah = findViewById(R.id.btFotoKtpJamaah);
        btFotoKtpJamaah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE_KTP);
            }
        });
        btFotoJamaah = findViewById(R.id.btFotoJamaah);
        btFotoJamaah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE_FOTO);
            }
        });
        link_login= findViewById(R.id.link_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ilogin = new Intent(RegisterJamaahActivity.this,LoginActivity.class);
                startActivity(Ilogin);
            }
        });

        btnDaftar = findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        EtNamaLengkap= findViewById(R.id.EtNamaLengkap);
        EtTempatLahir= findViewById(R.id.EtTempatLahir);
        EtTanggalLahir= findViewById(R.id.EtTanggalLahir);
        EtAlamat= findViewById(R.id.EtAlamat);
        EtNomorHp= findViewById(R.id.EtNomorHp);
        EtPekerjaan= findViewById(R.id.Etpekerjaan);
        EtEmail= findViewById(R.id.EtEmail);
        EtNomorRekening= findViewById(R.id.EtNomorRekening);
        EtBank= findViewById(R.id.EtBank);
        EtUsername= findViewById(R.id.EtUsername);
        EtPassword= findViewById(R.id.EtPassword);
        EtCPassword= findViewById(R.id.EtCPassword);
        BtTanggalLahir = findViewById(R.id.BtTanggalLahir);
        BtTanggalLahir.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {



            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            EtTanggalLahir.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();


}
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                Intent Iback = new Intent(RegisterJamaahActivity.this,PilihActivity.class);
                startActivity(Iback);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE_KTP) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    UFotoKtpJamaah = selectedImageUri;
                    Picasso.with(getApplicationContext()).load(selectedImageUri).placeholder(R.color.greycustom2).into(imv);
                    imv.setVisibility(View.VISIBLE);
                }else if(requestCode == REQUEST_GET_SINGLE_FILE_FOTO){
                    Uri selectedImageUri2 = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri2);
                    if (path != null) {
                        File f2 = new File(path);
                        selectedImageUri2 = Uri.fromFile(f2);
                    }
                    UFotoJamaah = selectedImageUri2;
                    Picasso.with(getApplicationContext()).load(selectedImageUri2).placeholder(R.color.greycustom2).into(imv2);
                    imv2.setVisibility(View.VISIBLE);
                }
//                Toast.makeText(this, String.valueOf(UFotoJamaah), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, String.valueOf(UFotoKtpJamaah), Toast.LENGTH_SHORT).show();


//                    imv.setImageURI(selectedImageUri);

            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }



    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();

        return res;
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnDaftar.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterJamaahActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Daftar");
        progressDialog.setMessage("Proses...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        String EtNamaLengkaps = EtNamaLengkap.getText().toString();
        String EtTempatLahirs = EtTempatLahir.getText().toString();
        String EtTanggalLahirs = EtTanggalLahir.getText().toString();
        String EtAlamats = EtAlamat.getText().toString();
        String EtNomorHps= EtNomorHp.getText().toString();
        String EtPekerjaans= EtPekerjaan.getText().toString();
        String EtEmails= EtEmail.getText().toString();
        String EtNomorrekenings= EtNomorRekening.getText().toString();
        String EtBanks= EtBank.getText().toString();
        String EtUsernames= EtUsername.getText().toString();
        String EtPasswords= EtPassword.getText().toString();
        RgJenisKelamin = (RadioGroup) findViewById(R.id.RgJenisKelamin);
        int selectedId = RgJenisKelamin.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RbJenisKelamin = (RadioButton) findViewById(selectedId);
        final String EtJenisKelamin0 = RbJenisKelamin.getText().toString();



        FFotoKTPjamaah = FileUtils.getFile(this, UFotoKtpJamaah);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(UFotoKtpJamaah)),
                        FFotoKTPjamaah
                );
        FFotoJamaah= FileUtils.getFile(this, UFotoJamaah);
        RequestBody requestFile2 =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(UFotoJamaah)),
                        FFotoJamaah
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("foto_ktp_jamaah", FFotoKTPjamaah.getName(), requestFile);
        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("foto_jamaah", FFotoJamaah.getName(), requestFile2);

//        menambah isi part dari multipart request
        notif_a_id = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        RequestBody notif_ap_id = RequestBody.create(okhttp3.MultipartBody.FORM, notif_a_id);
        RequestBody nama_jamaah = RequestBody.create(okhttp3.MultipartBody.FORM, EtNamaLengkaps);
        RequestBody tempat_lahir = RequestBody.create(okhttp3.MultipartBody.FORM, EtTempatLahirs);
        RequestBody tanggal_lahir= RequestBody.create(okhttp3.MultipartBody.FORM, EtTanggalLahirs);
        RequestBody jenis_kelamin= RequestBody.create(okhttp3.MultipartBody.FORM, EtJenisKelamin0);
        RequestBody alamat= RequestBody.create(okhttp3.MultipartBody.FORM, EtAlamats);
        RequestBody no_hp= RequestBody.create(okhttp3.MultipartBody.FORM, EtNomorHps);
        RequestBody pekerjaan= RequestBody.create(okhttp3.MultipartBody.FORM, EtPekerjaans);
        RequestBody email= RequestBody.create(okhttp3.MultipartBody.FORM, EtEmails);
        RequestBody no_rekening= RequestBody.create(okhttp3.MultipartBody.FORM, EtNomorrekenings);
        RequestBody bank= RequestBody.create(okhttp3.MultipartBody.FORM, EtBanks);
        RequestBody username= RequestBody.create(okhttp3.MultipartBody.FORM, EtUsernames);
        RequestBody password= RequestBody.create(okhttp3.MultipartBody.FORM, EtPasswords);

        MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);
        Call<GeneralSingleResponse> call = myAPIService.getDaftarjamaah(nama_jamaah,notif_ap_id,tempat_lahir,tanggal_lahir,jenis_kelamin
                ,alamat,no_hp,pekerjaan,email,no_rekening,bank,username,password,body,body2);
        call.enqueue(new Callback<GeneralSingleResponse>() {

            @Override
            public void onResponse(Call<GeneralSingleResponse> call, Response<GeneralSingleResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus() == 200){
                        onSignupSuccess();
                        progressDialog.dismiss();
                        Intent intent1 = new Intent(RegisterJamaahActivity.this, LoginActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(RegisterJamaahActivity.this, "Berhasil Mendaftar Silahkan Login", Toast.LENGTH_LONG).show();
                        startActivity(intent1);
                    }
                    else{
                        progressDialog.dismiss();
                        onSignupFailed();
                        Toast.makeText(RegisterJamaahActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralSingleResponse> call, Throwable throwable) {
                onSignupFailed();
                progressDialog.dismiss();
                Toast.makeText(RegisterJamaahActivity.this, "Periksa Jaringan", Toast.LENGTH_LONG).show();
            }
        });

//         find the radiobutton by returned id


        // TODO: Implement your own signup logic here.

    }


    public void onSignupSuccess() {

        btnDaftar.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
//        Toast.makeText(getBaseContext(), "Gagal Mendaftar", Toast.LENGTH_LONG).show();
        btnDaftar.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String EtNamaLengkaps = EtNamaLengkap.getText().toString();
        String EtTempatLahirs = EtTempatLahir.getText().toString();
        String EtTanggalLahirs = EtTanggalLahir.getText().toString();
        String EtAlamats = EtAlamat.getText().toString();
        String EtNomorHps= EtNomorHp.getText().toString();
        String EtPekerjaans= EtPekerjaan.getText().toString();
        String EtEmails= EtEmail.getText().toString();
        String EtNomorrekenings= EtNomorRekening.getText().toString();
        String EtBanks= EtBank.getText().toString();
        String EtUsernames= EtUsername.getText().toString();
        String EtPasswords= EtPassword.getText().toString();
        String EtCPasswords= EtCPassword.getText().toString();


        if (EtNamaLengkaps.isEmpty()) {
            EtNamaLengkap.setError("Nama lengkap kosong");
            valid = false;
        } else {
            EtNamaLengkap.setError(null);
        }
        if (EtTempatLahirs.isEmpty()) {
            EtTempatLahir.setError("Tempat lahir kosong");
            valid = false;
        } else {
            EtTempatLahir.setError(null);
        }
        if (EtTanggalLahirs.isEmpty()) {
            EtTanggalLahir.setError("Tanggal lahir kosong");
            valid = false;
        } else {
            EtTanggalLahir.setError(null);
        }
        if (EtAlamats.isEmpty()) {
            EtAlamat.setError("Alamat kosong");
            valid = false;
        } else {
            EtAlamat.setError(null);
        }
        if (imv.getVisibility()==View.GONE){
//            btFotoJamaah.setError("Nomor HP kosong");
            Toast.makeText(this, "Anda Harus Mengupload Foto KTP", Toast.LENGTH_SHORT).show();
            valid = false;
        }else{
            btFotoJamaah.setError(null);
        }

        if (imv2.getVisibility()==View.GONE){
            Toast.makeText(this, "Anda Harus Mengupload Pas Foto", Toast.LENGTH_SHORT).show();
//            btFotoKtpJamaah.setError("Nomor HP kosong");
            valid = false;
        }else{
            btFotoKtpJamaah.setError(null);
        }

        if (EtNomorHps.isEmpty()) {
            EtNomorHp.setError("Nomor HP kosong");
            valid = false;
        } else {
            EtNomorHp.setError(null);
        }

        if (EtPekerjaans.isEmpty()) {
            EtPekerjaan.setError("Pekerjaan kosong");
            valid = false;
        } else {
            EtPekerjaan.setError(null);
        }
        if (EtEmails.isEmpty()) {
            EtEmail.setError("Email kosong");
            valid = false;
        } else {
            EtEmail.setError(null);
        }
//        if (EtNomorrekenings.isEmpty()) {
//            EtNomorRekening.setError("Nomor Rekening kosong");
//            valid = false;
//        } else {
//            EtNomorRekening.setError(null);
//        }
        if (EtBanks.isEmpty()) {
            EtBank.setError("Bank kosong");
            valid = false;
        } else {
            EtBank.setError(null);
        }
        if (EtBanks.isEmpty()||EtUsernames.length() < 4 ) {
            EtUsername.setError("Username harus lebih dari 4 karakter");
            valid = false;
        } else {
            EtBank.setError(null);
        }
        //Username,password,c;
        if (EtPasswords.isEmpty() || EtPasswords.length() < 4 || EtPasswords.length() > 25) {
            EtPassword.setError("Password harus lebih dari 5 karakter");
            valid = false;
        } else {
            EtPassword.setError(null);
        }
        if (!EtPasswords.equals(EtCPasswords)){
            EtCPassword.setError("Konfirmasi Password Salah");
            valid = false;
        }else {
            EtCPassword.setError(null);
        }

        return valid;
    }
}
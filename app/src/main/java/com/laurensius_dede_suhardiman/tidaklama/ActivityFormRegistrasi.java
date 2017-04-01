package com.laurensius_dede_suhardiman.tidaklama;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityFormRegistrasi extends Activity {

    private EditText etUsername, etPassword, etKonfirmasiPassword, etNamaLengkap,  etAlamat, etNoHp;
    private DatePicker dpTtl;
    private String v_username, v_password, v_konfirmasipassword, v_namalengkap, v_tangallahir, v_alamat, v_nohp;
    private Button btnMasuk, btnDaftar;
    private TextView tvDebug;
    protected Dialog dialBox;

    ProgressDialog pDialog;

    private static String VAL_OK = "OK";
    private static String VAL_FRM_ERR = "Pastikan semua komponen pendaftaran telah terisi.";
    private static String VAL_EMAIL_ERR = "Pastikan format penulisan alamat email Anda benar.";
    private static String VAL_PASWD_ERR = "Pastikan password dan konfirmasi password sesuai. \n Panjang password minimal 6 karakter";
    String response_pendaftaran;
    private SambunganServer myServer = new SambunganServer();
    private DibalikLayar dibalikLayar = new DibalikLayar();

    List<NameValuePair> data_pendaftaran = new ArrayList<NameValuePair>(7);

    private static String CODE = "";
    private static String MESSAGE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registrasi);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etKonfirmasiPassword = (EditText) findViewById(R.id.etKonfirmasiPassword);
        etNamaLengkap = (EditText) findViewById(R.id.etNamaLengkap);
        etAlamat = (EditText) findViewById(R.id.etAlamat);
        etNoHp = (EditText) findViewById(R.id.etNoHp);
        tvDebug = (TextView) findViewById(R.id.tvDebug);

        dpTtl = (DatePicker) findViewById(R.id.dpTtl);
        dpTtl.setCalendarViewShown(false);

       btnMasuk = (Button) findViewById(R.id.btnMasuk);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityFormRegistrasi.this,ActivityFormLogin.class);
                startActivity(i);
                finish();
            }
        });

        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validasiform, validasiusername, validasipassword;
                v_username = etUsername.getText().toString();
                v_password = etPassword.getText().toString();
                v_konfirmasipassword = etKonfirmasiPassword.getText().toString();
                v_namalengkap = etNamaLengkap.getText().toString();
                v_alamat = etAlamat.getText().toString();
                v_nohp = etNoHp.getText().toString();

                validasiform = validasiForm();
                if (validasiform.equals(VAL_OK) == false) {
                    Toast.makeText(getApplicationContext(), "Perhatian : \n" + validasiform, Toast.LENGTH_SHORT).show();
                }

                validasipassword = validasiPassword();
                if (validasipassword.equals(VAL_OK) == false) {
                    Toast.makeText(getApplicationContext(), "Perhatian : \n" + validasipassword, Toast.LENGTH_SHORT).show();
                }

                validasiusername = validasiUsername();
                if (validasiusername.equals(VAL_OK) == false) {
                    Toast.makeText(getApplicationContext(), "Perhatian : \n" + validasiusername, Toast.LENGTH_SHORT).show();
                }

                if (validasiform.equals(VAL_OK) && validasiusername.equals(VAL_OK) && validasipassword.equals(VAL_OK)) {
                    v_tangallahir = String.valueOf(dpTtl.getYear()).concat("-").concat(String.valueOf(dpTtl.getMonth())).concat("-").concat(String.valueOf(dpTtl.getDayOfMonth()));
                    data_pendaftaran.add(new BasicNameValuePair("username", v_username));
                    data_pendaftaran.add(new BasicNameValuePair("password", v_password));
                    data_pendaftaran.add(new BasicNameValuePair("nama_lengkap", v_namalengkap));
                    data_pendaftaran.add(new BasicNameValuePair("tanggal_lahir", v_tangallahir));
                    data_pendaftaran.add(new BasicNameValuePair("alamat", v_alamat));
                    data_pendaftaran.add(new BasicNameValuePair("no_hp", v_nohp));
                    new DibalikLayar().execute();
                }
            }
        });
        formInit();
    }

    private void formInit(){
        etUsername.setText("");
        etPassword.setText("");
        etKonfirmasiPassword.setText("");
        etNamaLengkap.setText("");
        etAlamat.setText("");
        etNoHp.setText("");
    }

    private String validasiForm() {
        if(v_username.equals("") || v_password.equals("") ||
            v_konfirmasipassword.equals("") || v_namalengkap.equals("") ||
            v_alamat.equals("")){
            return VAL_FRM_ERR;
        }else{
            return VAL_OK;
        }
    }

    private String validasiUsername(){
        if(v_username.contains("@") && v_username.contains(".") && (v_username.length() > 4) && (v_username.contains(" ")==false))
            return this.VAL_OK;
        else
            return this.VAL_EMAIL_ERR;
    }

    private String validasiPassword(){
        if(v_password.equals(v_konfirmasipassword) && v_password.length() >= 6)
            return this.VAL_OK;
        else
            return this.VAL_PASWD_ERR;
    }

    public class DibalikLayar extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ActivityFormRegistrasi.this);
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String host_server = getString(R.string.host_server);
            String app_class = getString(R.string.app_class_user);
            String app_method = getString(R.string.app_method_registrasi);
            String uri = host_server.concat(app_class).concat(app_method);
            response_pendaftaran = myServer.ambilResponseServer(uri,SambunganServer.POST,data_pendaftaran);
            if (response_pendaftaran != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response_pendaftaran);
                    ActivityFormRegistrasi.CODE = jsonResponse.getString("code");
                    ActivityFormRegistrasi.MESSAGE = jsonResponse.getString("message");
                } catch (final JSONException e) {
                    Log.e("Pendaftaran : ", "Error pada saat parsing JSON detail sebagai berikut : " + e.getMessage());
                    ActivityFormRegistrasi.MESSAGE = "Pendaftaran gagal. Silahkan coba lagi!";
                }
            } else {
                ActivityFormRegistrasi.MESSAGE = "Pendaftaran gagal. Periksa layanan internet dan silahkan coba lagi!";
                Log.d("Pendaftaran : ", "Tidak dapat mengakses JSON web service.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvDebug.setText("");
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            formInit();
            Toast.makeText(getApplicationContext(),ActivityFormRegistrasi.MESSAGE,Toast.LENGTH_LONG).show();
//            tvDebug.setText(response_pendaftaran);
            Intent i = new Intent(ActivityFormRegistrasi.this, ActivityFormLogin.class);
            startActivity(i);
            finish();
        }
    }


}

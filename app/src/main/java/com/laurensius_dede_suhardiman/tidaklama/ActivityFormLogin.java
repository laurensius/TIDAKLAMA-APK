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
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityFormLogin extends Activity {

    private EditText etUsername, etPassword;
    private Button btnMasuk,btnDaftar;
    protected Dialog dialBox;
    private TextView tvNotifikasi;

    String response_login;
    private SambunganServer myServer = new SambunganServer();
    private DibalikLayar dibalikLayar = new DibalikLayar();

    private ProgressDialog pDialog;
    List<NameValuePair> data_login = new ArrayList<NameValuePair>(7);

    private static String CODE;
    private static String MESSAGE;
    private static String DATA_USER;

    private String v_username, v_password;
    private String id_user;
    private String username;
    private String password;
    private String nama_lengkap;
    private String tanggal_lahir;
    private String alamat;
    private String no_hp;
    private String tanggal_registrasi;
    private String mode_registrasi;
    private String status_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvNotifikasi = (TextView) findViewById(R.id.tvNotifikasi);

        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityFormLogin.this, ActivityFormRegistrasi.class);
                startActivity(i);
                finish();
            }
        });

        btnMasuk = (Button) findViewById(R.id.btnMasuk);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFormLogin.CODE = "";
                ActivityFormLogin.MESSAGE = "";
                ActivityFormLogin.DATA_USER = "";
                v_username = etUsername.getText().toString();
                v_password = etPassword.getText().toString();
                data_login.add(new BasicNameValuePair("username", v_username));
                data_login.add(new BasicNameValuePair("password", v_password));
                new DibalikLayar().execute();
            }
        });
        formInit();
    }

    private void formInit(){
        etUsername.setText("");
        etPassword.setText("");
        etUsername.findFocus();
    }

    public class DibalikLayar extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ActivityFormLogin.this);
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String host_server = getString(R.string.host_server);
            String app_class = getString(R.string.app_class_user);
            String app_method = getString(R.string.app_method_login);
            String uri = host_server.concat(app_class).concat(app_method);
            response_login = myServer.ambilResponseServer(uri,SambunganServer.POST,data_login);
            if (response_login != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response_login);
                    ActivityFormLogin.CODE = jsonResponse.getString("code");
                    ActivityFormLogin.MESSAGE = jsonResponse.getString("message");

                    JSONArray datasetDataUser = jsonResponse.getJSONArray("data_user");
                    for(int x=0;x<datasetDataUser.length();x++){
                        JSONObject data_user = datasetDataUser.getJSONObject(x);
                        id_user = data_user.getString("id_user");
                        username = data_user.getString("username");
                        password = data_user.getString("password");
                        nama_lengkap = data_user.getString("nama_lengkap");
                        tanggal_lahir = data_user.getString("tanggal_lahir");
                        alamat = data_user.getString("alamat");
                        no_hp = data_user.getString("no_hp");
                        tanggal_registrasi = data_user.getString("tanggal_registrasi");
                        mode_registrasi = data_user.getString("mode_registrasi");
                        status_user = data_user.getString("status_user");
                    }
                } catch (final JSONException e) {
                    Log.e("Login : ", "Error pada saat parsing JSON detail sebagai berikut : " + e.getMessage());
                    ActivityFormLogin.MESSAGE = "Login gagal. Silahkan coba lagi!";
                }
            } else {
                ActivityFormLogin.MESSAGE = "Login gagal. Periksa layanan internet dan silahkan coba lagi!";
                Log.d("Login : ", "Tidak dapat mengakses JSON web service.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            formInit();
            tvNotifikasi.setText(ActivityFormLogin.MESSAGE);
            if(ActivityFormLogin.CODE.equals("1")){
                if(status_user.equals("1")){
                    tvNotifikasi.setText(ActivityFormLogin.MESSAGE + "Verified");
                }else
                if(status_user.equals("2")){
                    tvNotifikasi.setText(ActivityFormLogin.MESSAGE + "Unverified");
                }else
                if(status_user.equals("3")){
                    tvNotifikasi.setText(ActivityFormLogin.MESSAGE + "Banned");
                }
            }
        }
    }
}

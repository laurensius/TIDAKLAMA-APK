package com.laurensius_dede_suhardiman.tidaklama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityFormLogin extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnMasuk,btnDaftar;
    private TextView tvNotifikasiLogin;
    String response_login;
    private SambunganServer myServer = new SambunganServer();
    private ProgressDialog pDialog;
    List<NameValuePair> data_login = new ArrayList<NameValuePair>(7);
    private static String CODE = "";
    private static String MESSAGE = "";
    private static String DATA_USER = "";
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
    private String TAG;
    private KelolaDatabase kelolaDatabase = new KelolaDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        TAG = getString(R.string.app_name);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvNotifikasiLogin = (TextView) findViewById(R.id.tvNotifikasiLogin);
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
                if(v_username.equals("") || v_password.equals("")){
                    formInit();
                    tvNotifikasiLogin.setText(SystemMessage.VAL_LOGIN_ERR);
                    toastMaker(SystemMessage.VAL_LOGIN_ERR);
                }else{
                    data_login.add(new BasicNameValuePair("username", v_username));
                    data_login.add(new BasicNameValuePair("password", v_password));
                    formInit();
                    new DibalikLayar().execute();
                }
            }
        });
        formInit();
    }

    private void formInit(){
        tvNotifikasiLogin.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etUsername.findFocus();
    }

    private void toastMaker(String text){
        Toast toast= Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.show();
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
                    Log.e(TAG, e.getMessage());
                    ActivityFormLogin.CODE = SystemMessage.LOGIN_FAILED;
                    ActivityFormLogin.MESSAGE = SystemMessage.LOGIN_GAGAL_JSON;
                }
            } else {
                ActivityFormLogin.CODE = SystemMessage.LOGIN_FAILED;
                ActivityFormLogin.MESSAGE = SystemMessage.LOGIN_GAGAL_NETWORK;
                Log.d(TAG, SystemMessage.LOGIN_GAGAL_NETWORK);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            toastMaker(ActivityFormLogin.MESSAGE);
            if(ActivityFormLogin.CODE.equals(SystemMessage.LOGIN_SUCCESS)){
                String return_buat_database = kelolaDatabase.buatDatabase(getString(R.string.sqlite_db_name));
                if(return_buat_database.equals(SystemMessage.BUAT_DATABASE_SUCCESS)){
                    Log.d(TAG,return_buat_database);
                    String return_cek_tabel_config = kelolaDatabase.cekTabel(getString(R.string.sqlite_t_config));
                    if(return_cek_tabel_config.equals(SystemMessage.CEK_TABEL_UNAVAILABLE)){
                        String return_buat_table = kelolaDatabase.buatTabelConfig();
                        Log.d(TAG,return_buat_table);
                        if(return_buat_table.equals(SystemMessage.BUAT_TABEL_CONFIG_SUCCESS)){
                            String return_insert_config = kelolaDatabase.inputTabelConfig(id_user,username,password,status_user,SystemMessage.LOGIN_STATE_IN);
                            Log.d(TAG,return_insert_config);
                            if(return_insert_config.equals(SystemMessage.INSERT_DATA_CONFIG_SUCCESS)){
                                kelolaDatabase.tutupKoneksi();
                                Intent i = new Intent(ActivityFormLogin.this,ActivityUtama.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }else
                    if(return_cek_tabel_config.equals(SystemMessage.CEK_TABEL_AVAILABLE)){
                        String return_update_config = kelolaDatabase.inputTabelConfig(id_user,username,password,status_user,SystemMessage.LOGIN_STATE_IN);
                        Log.d(TAG,return_update_config);
                        if(return_update_config.equals(SystemMessage.UPDATE_DATA_CONFIG_SUCCESS)){
                            kelolaDatabase.tutupKoneksi();
                            Intent i = new Intent(ActivityFormLogin.this,ActivityUtama.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }else
            if(ActivityFormLogin.CODE.equals(SystemMessage.LOGIN_FAILED)) {
                tvNotifikasiLogin.setText(ActivityFormLogin.MESSAGE);
            }
        }
    }
}

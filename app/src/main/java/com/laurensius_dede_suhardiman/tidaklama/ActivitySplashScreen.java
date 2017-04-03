package com.laurensius_dede_suhardiman.tidaklama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class ActivitySplashScreen extends AppCompatActivity {

    TextView tvLogoSingle, tvLogoString, tvLogoLoading;
    Intent i;
    String return_buat_database, return_cek_tabel,return_cek_isi_tabel;
    String TAG = getString(R.string.tag);

    private KelolaDatabase kelolaDatabase = new KelolaDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvLogoSingle = (TextView)findViewById(R.id.tvLogoSingle);
        tvLogoString = (TextView)findViewById(R.id.tvLogoString);
        tvLogoLoading = (TextView)findViewById(R.id.tvLogoLoading);

        return_buat_database = kelolaDatabase.buatDatabase(getString(R.string.sqlite_db_name));
        Log.d(TAG,return_buat_database);
        if(return_buat_database.equals(SystemMessage.BUAT_DATABASE_SUCCESS)){
            return_cek_tabel = kelolaDatabase.cekTabel(getString(R.string.sqlite_t_config));
            Log.d(TAG,return_cek_tabel);
            if(return_cek_tabel.equals(SystemMessage.CEK_TABEL_AVAILABLE)){ //tabel sudah ada -> artinya aplikasi sudah pernah diinstal
                return_cek_isi_tabel = kelolaDatabase.cekIsiTabel(getString(R.string.sqlite_t_config));
                Log.d(TAG,return_cek_isi_tabel);
                if(return_cek_isi_tabel.equals(SystemMessage.CEK_ISI_TABEL_CONTAIN)){ //tabel terisi, login sesuai data di tabel.
                    i = new Intent(ActivitySplashScreen.this,ActivityUtama.class);
                }else
                if(return_cek_isi_tabel.equals(SystemMessage.CEK_ISI_TABEL_EMPTY)){ //tabel kosong, jalankan Activity Login
                    i = new Intent(ActivitySplashScreen.this,ActivityFormLogin.class);
                }
            }else
            if(return_cek_tabel.equals(SystemMessage.CEK_TABEL_UNAVAILABLE)){ //tabel belum ada -> artinya aplikasi belum pernah diinstal
                i = new Intent(ActivitySplashScreen.this,ActivityFormRegistrasi.class);
            }
        }
        startActivity(i);
        finish();
    }

}

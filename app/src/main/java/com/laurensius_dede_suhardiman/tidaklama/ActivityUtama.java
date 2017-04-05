package com.laurensius_dede_suhardiman.tidaklama;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ActivityUtama extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment = null;
    KelolaDatabase kelolaDatabase = new KelolaDatabase();

    public static String v_id = "";
    public static String v_username = "";
    public static String v_password = "";
    public static String v_user_status = "";
    public static String v_login_status = "";

    private TextView tvUsername,tvStatusAkun;
    View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
        this.inisialisasiKomponenAplikasi();
        this.ambilLocalData();
        tvUsername = (TextView)headerView.findViewById(R.id.tvUsername);
        tvStatusAkun = (TextView)headerView.findViewById(R.id.tvStatusAkun);
        tvUsername.setText(v_username);
        tvStatusAkun.setText("Status Akun : " + v_user_status + "\nStatus Login : " +v_login_status);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        bukaFragment(item.getItemId());
        return true;
    }

    public void bukaFragment(int id){
        fragment = null;
        if (id == R.id.nav_panic_button) {
            fragment = new FragmentPanicButton();
        } else if (id == R.id.nav_kirim_laporan) {
            fragment = new FragmentKirimLaporan();
        } else if (id == R.id.nav_petunjuk_penggunaan) {
            fragment = new FragmentPetunjukPenggunaan();
        } else if (id == R.id.nav_tentag_aplikasi) {
            fragment = new FragmentPetunjukPenggunaan();
        } else if (id == R.id.nav_keluar) {
            KelolaDatabase kelolaDatabase = new KelolaDatabase();
            kelolaDatabase.buatDatabase(getString(R.string.sqlite_db_name));
            kelolaDatabase.deleteTableConfig(ActivityUtama.v_id);
            kelolaDatabase.tutupKoneksi();
            Intent i = new Intent(ActivityUtama.this,ActivityFormLogin.class);
            startActivity(i);
            super.finish();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void inisialisasiKomponenAplikasi(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        bukaFragment(R.id.nav_panic_button);
    }

    void ambilLocalData(){
        kelolaDatabase.buatDatabase(getString(R.string.sqlite_db_name));
        String[] data_user = new String[kelolaDatabase.loadTabelConfig().length];
        data_user = kelolaDatabase.loadTabelConfig();
        Log.d("DATABASE : ", data_user[0]);
        String[] spl = new String[data_user[0].split(SystemMessage.SEPARATOR.toString()).length];
        spl = data_user[0].split(SystemMessage.SEPARATOR.toString());
        this.v_id = spl[0];
        this.v_username = spl[1];
        this.v_password = spl[2];
        this.v_user_status = spl[3];
        this.v_login_status = spl[4];
        kelolaDatabase.tutupKoneksi();
    }
}

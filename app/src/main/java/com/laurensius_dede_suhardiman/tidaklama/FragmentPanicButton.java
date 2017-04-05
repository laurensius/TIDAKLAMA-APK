package com.laurensius_dede_suhardiman.tidaklama;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentPanicButton extends Fragment {

    public FragmentPanicButton() {}
    List<NameValuePair> data_panic = new ArrayList<NameValuePair>(5);
    TextView tvPanic;
    Button btnPanicButton;
    ProgressDialog pDialog;
    private KelolaDatabase kelolaDatabase = new KelolaDatabase();
    private SambunganServer myServer = new SambunganServer();

    String response_panic;
    private static String CODE = "";
    private static String MESSAGE = "";
    private String TAG;
    long waktu_mulai,durasi;
    double detik;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflaterPanicButton = inflater.inflate(R.layout.fragment_panic_button, container, false);
        btnPanicButton = (Button)inflaterPanicButton.findViewById(R.id.btnPanicButton);
        TAG = getString(R.string.app_name);
        return inflaterPanicButton;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kelolaDatabase.buatDatabase(getString(R.string.sqlite_db_name));
        String str = "";
        int len = kelolaDatabase.loadTabelConfig().length;
        if(len>0){
            String isi[] = new String[len];
            isi = kelolaDatabase.loadTabelConfig();
            str += isi[0];
        }
        kelolaDatabase.tutupKoneksi();

        btnPanicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    btnPanicButton.setBackgroundColor(Color.parseColor("#8F1D21"));
                    waktu_mulai = System.currentTimeMillis();
                }else
                if(event.getAction()==MotionEvent.ACTION_UP){
                    btnPanicButton.setBackgroundColor(Color.parseColor("#CF000F"));
                    durasi = System.currentTimeMillis() - waktu_mulai;
                    detik = durasi/1000;
                    Log.d("Waktu Mulai : ",String.valueOf(waktu_mulai));
                    Log.d("Durasi : ",String.valueOf(durasi));
                    if(detik>5){
                        String indikasi_lat = "0.0";
                        String indikasi_lon = "0.0";
                        data_panic.add(new BasicNameValuePair("indikasi_lat", indikasi_lat));
                        data_panic.add(new BasicNameValuePair("indikasi_lon", indikasi_lon));
                        data_panic.add(new BasicNameValuePair("pelapor", ActivityUtama.v_id));
                        new DibalikLayar().execute();
                    }
                }
                return false;
            }
        });
    }

    public void toastMaker(String text){
        Toast toast= Toast.makeText(getContext(),text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.show();
    }

    public class DibalikLayar extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String host_server = getString(R.string.host_server);
            String app_class = getString(R.string.app_class_panic);
            String app_method = getString(R.string.app_method_kirim_panic);
            String uri = host_server.concat(app_class).concat(app_method);
            response_panic = myServer.ambilResponseServer(uri,SambunganServer.POST,data_panic);
            if (response_panic != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response_panic);
                    FragmentPanicButton.CODE = jsonResponse.getString("code");
                    FragmentPanicButton.MESSAGE = jsonResponse.getString("message");
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                    FragmentPanicButton.CODE = SystemMessage.PANIC_FAILED;
                    FragmentPanicButton.MESSAGE = SystemMessage.PANIC_GAGAL_JSON;
                }
            } else {
                FragmentPanicButton.CODE = SystemMessage.PANIC_FAILED;
                FragmentPanicButton.MESSAGE = SystemMessage.PANIC_GAGAL_NETWORK;
                Log.d(TAG, SystemMessage.PANIC_GAGAL_NETWORK);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            toastMaker(FragmentPanicButton.MESSAGE);
        }
    }

}

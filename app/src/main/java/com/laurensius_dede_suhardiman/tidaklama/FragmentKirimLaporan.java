package com.laurensius_dede_suhardiman.tidaklama;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentKirimLaporan extends Fragment  {

    public FragmentKirimLaporan() {}

    List<NameValuePair> data_laporan = new ArrayList<NameValuePair>(5);
    DatePicker dpTanggalKejadian;
    EditText etLokasiKejadian, etDeskripsiKejadian;
    Spinner spJenisKejadian;
    SpinnerAdapter spJenisKejadianAdapter;
    Button btnKirimLaporan;
    TextView tvNotifikasiLaporan;
    protected Dialog dialBox;
    private ProgressDialog pDialog;
    private LinearLayout layoutReady, layoutNotReady;


    String v_id;
    String v_username;
    String v_tanggal_kejadian;
    String v_lokasi_kejadian;
    String v_jenis_kejadian;
    String v_deskripsi_kejadian;
    private String TAG = "TIDAKLAMA";
    private static String CODE = "";
    private static String MESSAGE = "";
    String response_laporan;
    String referensi_kejadian;

    List<String> data = new ArrayList<String>();

    private SambunganServer myServer = new SambunganServer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflaterKirimLaporan = inflater.inflate(R.layout.fragment_kirim_laporan, container, false);
        layoutReady = (LinearLayout)inflaterKirimLaporan.findViewById(R.id.layoutReady);
        layoutNotReady = (LinearLayout)inflaterKirimLaporan.findViewById(R.id.layoutNotReady);
        dpTanggalKejadian = (DatePicker)inflaterKirimLaporan.findViewById(R.id.dpTanggalKejadian);
        etLokasiKejadian = (EditText)inflaterKirimLaporan.findViewById(R.id.etLokasiKejadian);
        spJenisKejadian = (Spinner)inflaterKirimLaporan.findViewById(R.id.spJenisKejadian);
        etDeskripsiKejadian = (EditText)inflaterKirimLaporan.findViewById(R.id.etDeskripsiKejadian);
        btnKirimLaporan = (Button)inflaterKirimLaporan.findViewById(R.id.btnKirimLaporan);
        tvNotifikasiLaporan = (TextView)inflaterKirimLaporan.findViewById(R.id.tvNotifikasiLaporan);
        dpTanggalKejadian.setCalendarViewShown(false);
        formInit();
        return inflaterKirimLaporan;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ReferensiKejadian().execute();
        spJenisKejadian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] item = parent.getItemAtPosition(position).toString().split("Kode:");
                v_jenis_kejadian = String.valueOf(item[1]);
                Log.d("TAG", item[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String[] item = parent.getItemAtPosition(0).toString().split("Kode:");
                v_jenis_kejadian = String.valueOf(item[1]);
                Log.d("TAG", item[1]);
            }
        });
        btnKirimLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_id = ActivityUtama.v_id;
                v_tanggal_kejadian = String.valueOf(dpTanggalKejadian.getYear()).concat("-").concat(String.valueOf(dpTanggalKejadian.getMonth())).concat("-").concat(String.valueOf(dpTanggalKejadian.getDayOfMonth()));
                v_lokasi_kejadian = etLokasiKejadian.getText().toString();
                v_deskripsi_kejadian = etDeskripsiKejadian.getText().toString();
                if (validasiFormLaporan().equals(SystemMessage.VAL_OK)) {
                    data_laporan.add(new BasicNameValuePair("tanggal", v_tanggal_kejadian));
                    data_laporan.add(new BasicNameValuePair("lokasi", v_lokasi_kejadian));
                    data_laporan.add(new BasicNameValuePair("jenis", v_jenis_kejadian));
                    data_laporan.add(new BasicNameValuePair("deskripsi", v_deskripsi_kejadian));
                    data_laporan.add(new BasicNameValuePair("pelapor", v_id));
                    formInit();
                    new DibalikLayar().execute();
                } else {
                    tvNotifikasiLaporan.setText(SystemMessage.VAL_FORM_LAPORAN_FAILED);
                    toastMaker(SystemMessage.VAL_FORM_LAPORAN_FAILED);
                }
            }
        });
    }

    public String validasiFormLaporan(){
        if(v_lokasi_kejadian.equals("") || v_deskripsi_kejadian.equals("")){
            return SystemMessage.VAL_FORM_LAPORAN_FAILED;
        }else{
            return SystemMessage.VAL_OK;    
        }
    }

    public void formInit(){
        tvNotifikasiLaporan.setText("");
        etLokasiKejadian.setText("");
        etDeskripsiKejadian.setText("");
    }

    private void toastMaker(String text){
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
            String app_class = getString(R.string.app_class_kejadian);
            String app_method = getString(R.string.app_method_kirim_kejadian);
            String uri = host_server.concat(app_class).concat(app_method);
            response_laporan = myServer.ambilResponseServer(uri,SambunganServer.POST,data_laporan);
            Log.d("Response Laporan : ",response_laporan);
            if (response_laporan != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response_laporan);
                    FragmentKirimLaporan.CODE = jsonResponse.getString("code");
                    FragmentKirimLaporan.MESSAGE = jsonResponse.getString("message");
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                    FragmentKirimLaporan.CODE = SystemMessage.LAPORAN_FAILED;
                    FragmentKirimLaporan.MESSAGE = SystemMessage.LAPORAN_GAGAL_JSON;
                }
            } else {
                FragmentKirimLaporan.CODE = SystemMessage.LAPORAN_FAILED;
                FragmentKirimLaporan.MESSAGE = SystemMessage.LAPORAN_GAGAL_NETWORK;
                Log.d(TAG, SystemMessage.LAPORAN_GAGAL_NETWORK);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            toastMaker(FragmentKirimLaporan.MESSAGE);
            if(FragmentKirimLaporan.CODE.equals(SystemMessage.LAPORAN_SUCCESS)){
                formInit();
            }else{
                tvNotifikasiLaporan.setText(FragmentKirimLaporan.MESSAGE);
            }
        }
    }

    public class ReferensiKejadian extends AsyncTask<Void,Void,Void> {
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
            String app_class = getString(R.string.app_class_kejadian);
            String app_method = getString(R.string.app_method_referensi_kejadian);
            String uri = host_server.concat(app_class).concat(app_method);
            referensi_kejadian = myServer.ambilResponseServer(uri,SambunganServer.POST);
            Log.d("Response Laporan : ", referensi_kejadian);
            if (referensi_kejadian != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(referensi_kejadian);
                    JSONArray referensi_kejadian = jsonResponse.getJSONArray("referensi_kejadian");
                    for(int x = 0;x<referensi_kejadian.length();x++){
                        JSONObject kejadian = referensi_kejadian.getJSONObject(x);
                        data.add(kejadian.getString("jenis").concat(" - Kode:").concat(kejadian.getString("id_kejadian")));
                    }
                    FragmentKirimLaporan.CODE = SystemMessage.LAPORAN_SUCCESS;
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                    FragmentKirimLaporan.CODE = SystemMessage.LAPORAN_FAILED;
                    FragmentKirimLaporan.MESSAGE = SystemMessage.LAPORAN_GAGAL_JSON;
                }
            } else {
                FragmentKirimLaporan.CODE = SystemMessage.LAPORAN_FAILED;
                FragmentKirimLaporan.MESSAGE = SystemMessage.LAPORAN_GAGAL_NETWORK;
                Log.d(TAG, SystemMessage.LAPORAN_GAGAL_NETWORK);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(FragmentKirimLaporan.CODE.equals(SystemMessage.LAPORAN_SUCCESS)){
                layoutReady.setVisibility(View.VISIBLE);
                layoutNotReady.setVisibility(View.INVISIBLE);
                ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, data);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spJenisKejadian.setAdapter(aa);
            }else
            if(FragmentKirimLaporan.CODE.equals(SystemMessage.LAPORAN_FAILED)){
                layoutReady.setVisibility(View.INVISIBLE);
                layoutNotReady.setVisibility(View.VISIBLE);
            }
            tvNotifikasiLaporan.setText("");
        }
    }

}

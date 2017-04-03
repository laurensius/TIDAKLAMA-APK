package com.laurensius_dede_suhardiman.tidaklama;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class KelolaDatabase {

    private SQLiteDatabase db;

    public String buatDatabase(String nama_database){
        String nama_direktori = "/data/data/com.laurensius_dede_suhardiman.tidaklama/";
        try{
            db = SQLiteDatabase.openDatabase(nama_direktori.concat(nama_database),null,SQLiteDatabase.CREATE_IF_NECESSARY);
            return SystemMessage.BUAT_DATABASE_SUCCESS;
        }catch (SQLiteException ex){
            return SystemMessage.BUAT_DATABASE_FAILED;
        }
    }

    public String cekTabel(String nama_table){
        String query = "Select name from sqlite_master where type ='table' and name='"+nama_table+"'";
        int numRow = db.rawQuery(query, null).getCount();
        if(numRow == 0){
            return SystemMessage.CEK_TABEL_UNAVAILABLE;
        }else{
            return SystemMessage.CEK_TABEL_AVAILABLE;
        }
    }

    public String cekIsiTabel(String nama_tabel){
        String sql = "select * from " + nama_tabel;
        int numRow = db.rawQuery(sql, null).getCount();
        if(numRow==0){
            return SystemMessage.CEK_ISI_TABEL_EMPTY;
        }else{
            return SystemMessage.CEK_ISI_TABEL_CONTAIN;
        }
    }

    public String buatTabelConfig(){
        String retVal;
        String sql = "create table config" +
                "(id_user text," +
                "username text," +
                "password text," +
                "user_status text," +
                "login_status text);";
        try{
            db.execSQL(sql);
            retVal = SystemMessage.BUAT_TABEL_CONFIG_SUCCESS;
        }catch(SQLiteException e){
            retVal = SystemMessage.BUAT_TABEL_CONFIG_FAILED;
        }
        return retVal;
    }
}

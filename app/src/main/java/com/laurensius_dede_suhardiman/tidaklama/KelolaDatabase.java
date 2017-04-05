package com.laurensius_dede_suhardiman.tidaklama;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class KelolaDatabase {

    private SQLiteDatabase db;

    public String buatDatabase(String nama_database){
        String nama_direktori = "/data/data/com.laurensius_dede_suhardiman.tidaklama/";
        try{
            db = SQLiteDatabase.openDatabase(nama_direktori.concat(nama_database), null, SQLiteDatabase.CREATE_IF_NECESSARY);
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

    public String inputTabelConfig(String id_user,String username,String password,String user_status,String login_status){
        String retVal;
        String sql = "insert into config " +
                "(id_user,username,password,user_status,login_status) " +
                " values('"+ id_user +"','"+ username +"','"+ password+"','"+ user_status+"','"+ login_status+"');";
        try{
            db.execSQL(sql);
            retVal = SystemMessage.INSERT_DATA_CONFIG_SUCCESS;
        }catch(SQLiteException ex){
            retVal = SystemMessage.INSERT_DATA_CONFIG_FAILED;
        }
        return retVal;
    }

    public String updateTabelConfig(String id_user,String username,String password,String user_status,String login_status){
        String retVal;
        String sql = "update config set " +
                "id_user = '"+id_user+"', " +
                "username = '"+username+"', " +
                "password = '"+password+"', " +
                "user_status = '"+user_status+"', " +
                "login_status = '"+login_status+"' " +
                "where id_user='"+id_user+"'";
        try{
            db.execSQL(sql);
            retVal = SystemMessage.INSERT_DATA_CONFIG_SUCCESS;
        }catch(SQLiteException ex){
            retVal = SystemMessage.INSERT_DATA_CONFIG_FAILED;
        }
        return retVal;
    }

    public String[] loadTabelConfig(){
        String sql = "select * from config";
        Cursor c = db.rawQuery(sql, null);
        int jml_data = c.getCount();
        if(jml_data == 0){
            return null;
        }else{
            String[] data = new String[jml_data];
            try{
                int ctr = 0;
                int id_user = c.getColumnIndex("id_user");
                int username  = c.getColumnIndex("username");
                int password = c.getColumnIndex("password");
                int user_status = c.getColumnIndex("user_status");
                int login_status = c.getColumnIndex("login_status");
                while(c.moveToNext()){
                    Log.d("Debug id_user", c.getString(id_user));
                    Log.d("Debug username", c.getString(username));
                    Log.d("Debug password", c.getString(password));
                    Log.d("Debug user_status", c.getString(user_status));
                    Log.d("Debug login_status", c.getString(login_status));
                    data[ctr] = c.getString(id_user)
                            .concat(SystemMessage.SEPARATOR)
                            .concat(c.getString(username))
                            .concat(SystemMessage.SEPARATOR)
                            .concat(c.getString(password))
                            .concat(SystemMessage.SEPARATOR)
                            .concat(c.getString(user_status))
                            .concat(SystemMessage.SEPARATOR)
                            .concat(c.getString(login_status));
                    ctr++;
                }
                return data;
            }catch(SQLiteException e){
                Log.e("Error","Errornya : " + e.getMessage());
                return data;
            }
        }
    }


    public String deleteTableConfig(String id_user){
        String retVal="";
        String sql = "delete from config where id_user='"+ id_user +"'";
        try{
            db.execSQL(sql);
            retVal =  SystemMessage.DELETE_DATA_CONFIG_SUCCESS;
        }catch(SQLiteException e){
            retVal =  SystemMessage.DELETE_DATA_CONFIG_FALIED;
        }
        return retVal;
    }


    public void tutupKoneksi() {
        if (db.isOpen()) {
            db.close();
        }
    }
}

package com.laurensius_dede_suhardiman.tidaklama;

/**
 * Created by Laurensius D.S on 4/2/2017.
 */
public class SystemMessage {
    //-----------------------DATABASE-----------------------------------
    public static String BUAT_DATABASE_SUCCESS = "Buat database sukses";
    public static String BUAT_DATABASE_FAILED = "Buat database gagal";

    //-----------------------CEK TABEL-----------------------------------
    public static String CEK_TABEL_AVAILABLE = "Tabel tersedia";
    public static String CEK_TABEL_UNAVAILABLE = "Tabel belum tersedia";

    //-----------------------CEK ISI TABEL-----------------------------------
    public static String CEK_ISI_TABEL_CONTAIN = "Tabel terisi";
    public static String CEK_ISI_TABEL_EMPTY = "Tabel kosong";

    //-----------------------BUAT TABEL-----------------------------------
    public static String BUAT_TABEL_CONFIG_SUCCESS = "Tabel config berhasil dibuat";
    public static String BUAT_TABEL_CONFIG_FAILED = "Tabel config gagal dibuat";

    //-----------------------VALIDASI FORM REGISTRASI-----------------------------------
    public static String VAL_OK = "OK";
    public static String VAL_FRM_ERR = "Pastikan semua komponen pendaftaran telah terisi.";
    public static String VAL_EMAIL_ERR = "Pastikan format penulisan alamat email Anda benar.";
    public static String VAL_PASWD_ERR = "Pastikan password dan konfirmasi password sesuai. \n Panjang password minimal 6 karakter";
    public static String REGISTRASI_GAGAL_JSON = "Pendaftaran gagal, silahkan coba kembali.";
    public static String REGISTRASI_GAGAL_NETWORK = "Pastikan perangkat Anda terhubung dengan layanan Intenet.";

    //-----------------------VALIDASI FORM REGISTRASI-----------------------------------
    public static String REGISTRATION_SUCCESS = "1";
    public static String REGISTRATION_FAILED = "2";

    //-----------------------VALIDASI FORM LOGIN-----------------------------------
    public static String LOGIN_SUCCESS = "1";
    public static String LOGIN_FAILED = "2";
    public static String VAL_LOGIN_ERR = "Pastikan kolom username dan password sudah terisi.";
    public static String LOGIN_GAGAL_JSON = "Login gagal, silahkan coba kembali.";
    public static String LOGIN_GAGAL_NETWORK = "Pastikan perangkat Anda terhubung dengan layanan Intenet.";
    public static String LOGIN_STATE_IN = "1";
    public static String LOGIN_STATE_OUT = "2";


    public static String INSERT_DATA_CONFIG_SUCCESS = "1";
    public static String INSERT_DATA_CONFIG_FAILED = "2";

    public static String UPDATE_DATA_CONFIG_SUCCESS = "1";
    public static String UPDATE_DATA_CONFIG_FAILED = "2";

    public static String DELETE_DATA_CONFIG_SUCCESS = "1";
    public static String DELETE_DATA_CONFIG_FALIED = "2";


    public static String SEPARATOR = "#separator#";


    public static String VAL_FORM_LAPORAN_FAILED = "Pastikan formulir pelaporan telah terisi semua.";
    public static String LAPORAN_SUCCESS = "1";
    public static String LAPORAN_FAILED = "2";
    public static String LAPORAN_GAGAL_JSON = "Kirim laporan gagal, silahkan coba kembali.";
    public static String LAPORAN_GAGAL_NETWORK = "Pastikan perangkat Anda terhubung dengan layanan Intenet.";

    public static String PANIC_SUCCESS = "1";
    public static String PANIC_FAILED = "2";
    public static String PANIC_GAGAL_JSON = "Kirim laporan via panic button gagal, silahkan coba kembali.";
    public static String PANIC_GAGAL_NETWORK = "Kirim laporan via panic button gagal.\nPastikan perangkat Anda terhubung dengan layanan Intenet.";



}

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
    public static String REGISTRASI_GAGAL_JSON = "Pendaftaran gagal, silahkan coba kemabali.";
    public static String REGISTRASI_GAGAL_NETWORK = "Pastikan perangkat Anda terhubung dengan layanan Intenet.";

}

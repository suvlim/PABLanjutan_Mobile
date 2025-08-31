package edu.uph.m23si2.pertamaapp.model;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
public class KelasMataKuliah extends RealmObject {
    @PrimaryKey
    private int kelasID;
    private String namaKelas;
    private Matakuliah matakuliah;

    public KelasMataKuliah() {
    }

    public KelasMataKuliah(int kelasID, String namaKelas, Matakuliah matakuliah) {
        this.kelasID = kelasID;
        this.namaKelas = namaKelas;
        this.matakuliah = matakuliah;
    }

    public int getKelasID() {
        return kelasID;
    }

    public void setKelasID(int kelasID) {
        this.kelasID = kelasID;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public Matakuliah getMatakuliah() {
        return matakuliah;
    }

    public void setMatakuliah(Matakuliah matakuliah) {
        this.matakuliah = matakuliah;
    }
}


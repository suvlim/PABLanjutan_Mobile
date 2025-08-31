package edu.uph.m23si2.pertamaapp.model;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
public class Krs_Detail extends RealmObject {
    @PrimaryKey
    private int detailID;
    private Krs krs;
    private KelasMataKuliah kelasMatakuliah;
    private String nilai;
    public Krs_Detail() {
    }


    public Krs_Detail(int detailID, Krs krs, KelasMataKuliah kelasMatakuliah, String nilai) {
        this.detailID = detailID;
        this.krs = krs;
        this.kelasMatakuliah = kelasMatakuliah;
        this.nilai = nilai;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public Krs getKrs() {
        return krs;
    }

    public void setKrs(Krs krs) {
        this.krs = krs;
    }

    public KelasMataKuliah getKelasMatakuliah() {
        return kelasMatakuliah;
    }

    public void setKelasMatakuliah(KelasMataKuliah kelasMatakuliah) {
        this.kelasMatakuliah = kelasMatakuliah;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }
}


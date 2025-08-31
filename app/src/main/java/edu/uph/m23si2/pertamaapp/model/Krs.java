package edu.uph.m23si2.pertamaapp.model;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
public class Krs extends RealmObject {
    @PrimaryKey
    private int krsID;
    private int semester;
    private String tahunAjaran;
    private Mahasiswa mahasiswa;
    private RealmList<Krs_Detail> detailList;

    public Krs() {
    }

    public Krs(int krsID, int semester, String tahunAjaran, Mahasiswa mahasiswa) {
        this.krsID = krsID;
        this.semester = semester;
        this.tahunAjaran = tahunAjaran;
        this.mahasiswa = mahasiswa;
    }



    public int getKrsID() {
        return krsID;
    }

    public void setKrsID(int krsID) {
        this.krsID = krsID;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public RealmList<Krs_Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(RealmList<Krs_Detail> detailList) {
        this.detailList = detailList;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }
}

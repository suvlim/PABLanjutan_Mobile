package edu.uph.m23si2.pertamaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.uph.m23si2.pertamaapp.model.KelasMataKuliah;
import edu.uph.m23si2.pertamaapp.model.Krs;
import edu.uph.m23si2.pertamaapp.model.Krs_Detail;
import edu.uph.m23si2.pertamaapp.model.Mahasiswa;
import edu.uph.m23si2.pertamaapp.model.Matakuliah;
import edu.uph.m23si2.pertamaapp.model.Prodi;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtNama, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true) // sementara aktifkan untuk demo
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        initData();
        btnLogin = findViewById(R.id.btnLogin);
        edtNama = findViewById(R.id.edtNama);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDashboard();
            }
        });
    }

    public void initData(){ // menambahkan data prodi dan matakuliah
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            Number maxId = r.where(Mahasiswa.class).max("studentID");
            Prodi prodiSI = r.createObject(Prodi.class,0);
            prodiSI.setFakultas("Fakultas Teknologi Informasi");
            prodiSI.setNama("Sistem Informasi");

            Matakuliah matMobile = r.createObject(Matakuliah.class,0);
            matMobile.setNama("Pemrograman Mobile Lanjut");
            matMobile.setSks(3);
            matMobile.setProdi(prodiSI);
            matMobile.setKelasList(new RealmList<>());

            Matakuliah matPBO = r.createObject(Matakuliah.class,1);
            matPBO.setNama("Pemrograman Berorientasi Objek");
            matPBO.setSks(3);
            matPBO.setProdi(prodiSI);
            matPBO.setKelasList(new RealmList<>());

            KelasMataKuliah kelasJava = r.createObject(KelasMataKuliah.class, 0);
            kelasJava.setNamaKelas("Java 001");
            kelasJava.setMatakuliah(matMobile);

            KelasMataKuliah kelasBasisData = r.createObject(KelasMataKuliah.class, 2);
            kelasBasisData.setNamaKelas("Basis Data 002");
            kelasBasisData.setMatakuliah(matPBO);

            matMobile.getKelasList().add(kelasJava);
            matPBO.getKelasList().add(kelasBasisData);

            Krs krs001 = r.createObject(Krs.class, 0);
            krs001.setSemester(4);
            krs001.setTahunAjaran("2024/2025");
            krs001.setDetailList(new RealmList<>());

            Krs krs002 = r.createObject(Krs.class, 1);
            krs002.setSemester(2);
            krs002.setTahunAjaran("2024/2025");
            krs002.setDetailList(new RealmList<>());


            Krs_Detail detail001 = r.createObject(Krs_Detail.class, 0);
            detail001.setKrs(krs001);
            detail001.setKelasMatakuliah(kelasJava);
            detail001.setNilai("A");

            Krs_Detail detail002 = r.createObject(Krs_Detail.class, 1);
            detail002.setKrs(krs002);
            detail002.setKelasMatakuliah(kelasBasisData);
            detail002.setNilai("B+");

            krs001.getDetailList().add(detail001);
            krs002.getDetailList().add(detail002);

            Mahasiswa mhs1 = r.createObject(Mahasiswa.class, 101);
            mhs1.setNama("Budi Santoso");
            mhs1.setJenisKelamin("Laki-laki");
            mhs1.setHobi("Coding");
            mhs1.setEmail("budi@kampus.ac.id");
            mhs1.setProdi("Teknik Informatika");
            mhs1.setListKRS(new RealmList<>());

            mhs1.getListKRS().add(krs001);
        });
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();

    }
    public void toProfil(){
        Intent intent = new Intent(this,ProfilActivity.class);
        intent.putExtra("nama",edtNama.getText().toString());
        startActivity(intent);
    }
    public void toDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}
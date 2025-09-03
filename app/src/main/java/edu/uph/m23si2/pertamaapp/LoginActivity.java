package edu.uph.m23si2.pertamaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.uph.m23si2.pertamaapp.api.ApiKotaResponse;
import edu.uph.m23si2.pertamaapp.api.ApiResponse;
import edu.uph.m23si2.pertamaapp.api.ApiService;
import edu.uph.m23si2.pertamaapp.model.KelasMataKuliah;
import edu.uph.m23si2.pertamaapp.model.Kota;
import edu.uph.m23si2.pertamaapp.model.Krs;
import edu.uph.m23si2.pertamaapp.model.Krs_Detail;
import edu.uph.m23si2.pertamaapp.model.Mahasiswa;
import edu.uph.m23si2.pertamaapp.model.Matakuliah;
import edu.uph.m23si2.pertamaapp.model.Prodi;
import edu.uph.m23si2.pertamaapp.model.Provinsi;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtNama, edtPassword;
    Spinner sprProvinsi, sprKota;
    List<Provinsi> provinsiList =  new ArrayList<>();
    List<String> namaProvinsi = new ArrayList<>();
    List<Kota> kotaList =  new ArrayList<>();
    List<String> namaKota = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterKota;

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
        //initData();
        sprProvinsi = findViewById(R.id.sprProvinsi);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,namaProvinsi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sprProvinsi.setAdapter(adapter);

        sprKota = findViewById(R.id.sprKota);
        adapterKota = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,namaKota);
        adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sprKota.setAdapter(adapterKota);

        //init retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wilayah.id")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        //panggil API

        Log.d("API_CALL", "Calling getProvinsi()");
        apiService.getProvinsi().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Log.d("API_SUCCESS", "Response received: " + new Gson().toJson(response.body()));
                    provinsiList = response.body().getData();
                    namaProvinsi.clear();
                    for(Provinsi p: provinsiList){
                        if(p.getName()!=null){
                            Log.d("Provinsi", p.getName());
                            namaProvinsi.add(p.getName());
                        }
                    }

                    adapter.notifyDataSetChanged();

                    sprProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Provinsi selected = provinsiList.get(position);
                            String provinceCode = selected.getCode();
                            Log.d("Provinsi", selected.getCode() + " - " + selected.getName());
                            apiService.getKota(provinceCode).enqueue(new Callback<ApiKotaResponse>() {
                                @Override
                                public void onResponse(Call<ApiKotaResponse> call, Response<ApiKotaResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        kotaList = response.body().getData();
                                        namaKota.clear();
                                        for (Kota k : kotaList) {
                                            namaKota.add(k.getName());
                                        }
                                        adapterKota.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Gagal memuat kabupaten", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiKotaResponse> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("API_KABUPATEN", "onFailure: ", t);
                                }
                            });
                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
                else {
                    Log.e("API_RESPONSE", "Response not successful or body null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Gagal :"+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "onFailure: ", t);
            }
        });

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
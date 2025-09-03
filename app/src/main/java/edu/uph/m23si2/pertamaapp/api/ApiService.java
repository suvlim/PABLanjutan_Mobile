package edu.uph.m23si2.pertamaapp.api;

import edu.uph.m23si2.pertamaapp.model.Kota;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/provinces.json")
    Call<ApiResponse> getProvinsi();

    @GET("/api/regencies/{provinceCode}.json")
    Call<ApiKotaResponse> getKota(@Path("provinceCode") String provinceCode);
}
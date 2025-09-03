package edu.uph.m23si2.pertamaapp.api;

import java.util.List;

import edu.uph.m23si2.pertamaapp.model.Provinsi;

public class ApiResponse {
    private List<Provinsi> data;

    public List<Provinsi> getData(){
        return data;
    }

    public void setData(List<Provinsi> data) {
        this.data = data;
    }
}
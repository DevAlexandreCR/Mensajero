package com.mensajero.equipo.mensajero.Constantes;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by equipo on 03/10/2017.
 */

public class Mensajeros implements Serializable{

    private String nombre;
    private float calificacion;
    private String placa;
    private String codigo;
    private String urlFoto;
    private String telefono;
    private String token;
    private Double lat_dir_ini;
    private Double lgn_dir_ini;
    private Float distancia;
    private int posicion_en_la_lista;






    public  Mensajeros(){

    }

    public Mensajeros(String nombre, String urlFoto, float calificacion,
                      String codigo, String placa, Double lgn_dir_ini, String telefono, Double lat_dir_ini, String token){


        this.nombre = nombre;
        this.calificacion = calificacion;
        this.urlFoto = urlFoto;
        this.codigo = codigo;
        this.placa = placa;
        this.telefono = telefono;
        this.lat_dir_ini = lat_dir_ini;
        this.lgn_dir_ini = lgn_dir_ini;
        this.token = token;

    }

    public void setPosicion_en_la_lista(int posicion_en_la_lista) {
        this.posicion_en_la_lista = posicion_en_la_lista;
    }

    public int getPosicion_en_la_lista() {
        return posicion_en_la_lista;
    }

    public void setLat_dir_ini(Double lat_dir_ini) {
        this.lat_dir_ini = lat_dir_ini;
    }

    public void setLgn_dir_ini(Double lgn_dir_ini) {
        this.lgn_dir_ini = lgn_dir_ini;
    }

    public Float getDistancia() {
        return distancia;
    }

    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }


    public float getCalificacion() {
        return calificacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }
    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }



    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getToken() {
        return token;
    }

    public Double getLat_dir_ini() {
        return lat_dir_ini;
    }

    public Double getLgn_dir_ini() {
        return lgn_dir_ini;
    }
}

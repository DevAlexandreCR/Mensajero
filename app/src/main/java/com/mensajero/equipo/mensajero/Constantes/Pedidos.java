package com.mensajero.equipo.mensajero.Constantes;

import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by equipo on 10/09/2017.
 */

public class Pedidos implements Serializable{

    private String id_pedido;
    private String Token;
    private String tipo_pedido;
    private String forma_de_pago;
    private String nombre;
    private String telefono;
    private String id_usuario;
    private String fecha_pedido;
    private String dir_inicial;
    private String dir_final;
    private double valor_pedido;
    private String estado_pedido;
    private String comentario;
    private String codigo_mensajero;
    private String tipo_servicio;
    private double lat_dir_inicial;
    private double lat_dir_final;
    private double long_dir_inicial;
    private double long_dir_final;
    private float calificacion;
    private String token_conductor;
    private String pqr;
    private boolean servicio_empresa;
    private int cuantos_mensajeros;
    private boolean copia;
    private String ciudad;
    private Long date;

    public Pedidos() {

    }

    public Pedidos(String id_pedido, String id_usuario, String fecha_pedido,Long date, String dir_inicial
            , String dir_final, double valor_pedido, String estado_pedido, @Nullable String comentario, String codigo_mensajero,
                   String tipo_servicio, String Token, @Nullable String tipo_pedido, String forma_de_pago, String nombre,
                   String telefono, double lat_dir_inicial, double long_dir_inicial, double lat_dir_final,
                   double long_dir_final, String ciudad) {

        this.fecha_pedido = fecha_pedido;
        this.dir_inicial = dir_inicial;
        this.dir_final = dir_final;
        this.valor_pedido = valor_pedido;
        this.estado_pedido = estado_pedido;
        this.comentario = comentario;
        this.id_pedido = id_pedido;
        this.id_usuario = id_usuario;
        this.codigo_mensajero = codigo_mensajero;
        this.tipo_servicio = tipo_servicio;
        this.forma_de_pago = forma_de_pago;
        this.nombre = nombre;
        this.Token = Token;
        this.lat_dir_final = lat_dir_final;
        this.lat_dir_inicial = lat_dir_inicial;
        this.long_dir_final = long_dir_final;
        this.long_dir_inicial = long_dir_inicial;
        this.telefono = telefono;
        this.tipo_pedido = tipo_pedido;
        this.ciudad = ciudad;
        this.date = date;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public boolean isCopia() {
        return copia;
    }

    public void setCopia(boolean copia) {
        this.copia = copia;
    }

    public int getCuantos_mensajeros() {

        return cuantos_mensajeros;
    }

    public void setCuantos_mensajeros(int cuantos_mensajeros) {
        this.cuantos_mensajeros = cuantos_mensajeros;
    }

    public boolean isServicio_empresa() {
        return servicio_empresa;
    }

    public void setServicio_empresa(boolean servicio_empresa) {
        this.servicio_empresa = servicio_empresa;
    }

    public String getPqr() {
        return pqr;
    }

    public void setPqr(String pqr) {
        this.pqr = pqr;
    }

    public String getToken_conductor() {
        return token_conductor;
    }

    public void setToken_conductor(String token_conductor) {
        this.token_conductor = token_conductor;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getForma_de_pago() {
        return forma_de_pago;
    }

    public void setForma_de_pago(String forma_de_pago) {
        this.forma_de_pago = forma_de_pago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getLat_dir_inicial() {
        return lat_dir_inicial;
    }

    public void setLat_dir_inicial(double lat_dir_inicial) {
        this.lat_dir_inicial = lat_dir_inicial;
    }

    public double getLat_dir_final() {
        return lat_dir_final;
    }

    public void setLat_dir_final(double lat_dir_final) {
        this.lat_dir_final = lat_dir_final;
    }

    public double getLong_dir_inicial() {
        return long_dir_inicial;
    }

    public void setLong_dir_inicial(double long_dir_inicial) {
        this.long_dir_inicial = long_dir_inicial;
    }

    public double getLong_dir_final() {
        return long_dir_final;
    }

    public void setLong_dir_final(double long_dir_final) {
        this.long_dir_final = long_dir_final;
    }

    public String getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(String tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }


    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getTipo_pedido() {
        return tipo_pedido;
    }

    public void setTipo_pedido(String tipo_pedido) {
        this.tipo_pedido = tipo_pedido;
    }

    public String getDir_inicial() {
        return dir_inicial;
    }

    public void setDir_inicial(String dir_inicial) {
        this.dir_inicial = dir_inicial;
    }

    public String getDir_final() {
        return dir_final;
    }

    public void setDir_final(String dir_final) {
        this.dir_final = dir_final;
    }

    public double getValor_pedido() {
        return valor_pedido;
    }

    public String getEstado_pedido() {
        return estado_pedido;

    }

    public void setValor_pedido(double valor_pedido) {
        this.valor_pedido = valor_pedido;
    }

    public void setEstado_pedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getCodigo_mensajero() {
        return codigo_mensajero;
    }

    public void setCodigo_mensajero(String codigo_mensajero) {
        this.codigo_mensajero = codigo_mensajero;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}


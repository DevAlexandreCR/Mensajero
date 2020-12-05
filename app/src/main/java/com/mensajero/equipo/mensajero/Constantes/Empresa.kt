package com.mensajero.equipo.mensajero.Constantes


class Empresa{
    var nombre: String? = null
    var es_empresa: Boolean = true
    var email: String? = null
    var telefono: String? = null
    var saldo: Int = 0
    var token: String? = null
    var direccion_empresa: String? = null
    var id_usuario: String? = null
    var categoria: String? = null
    var url_foto_perfil: String? = null
    var url_foto_carta: String? = null
    var url_foto_portada: String? = null
    var latitud: Double = 0.0
    var longitud: Double = 0.0
    var horarios: Horario? = null
    var descripcion: String? = null
    var ciudad: String? = null
    var placeID: String? = null


    constructor()

    constructor(id_usuario: String, direccion_empresa: String, token: String, nombre: String, telefono: String, email: String) {

        this.id_usuario = id_usuario
        this.nombre = nombre
        this.telefono = telefono
        this.email = email
        this.direccion_empresa = direccion_empresa
        this.token = token
    }


}
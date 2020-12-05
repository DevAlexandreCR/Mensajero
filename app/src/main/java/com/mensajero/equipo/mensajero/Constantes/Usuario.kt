package com.mensajero.equipo.mensajero.Constantes

/**
 * Created by equipo on 02/10/2017.
 */

class Usuario {

    var id_usuario: String? = null
    var nombre: String? = null
    var telefono: String? = null
    var email: String? = null
    var id_auth: String? = null
    var token: String? = null
    var saldo: Double = 0.toDouble()
    var refiere: String? = null
    var documento: String? = null
    var es_empresa: Boolean = false
    var direccion_empresa: String? = null
    var codigo_referido: String? = null

    constructor() {

    }

    constructor(id_usuario: String, id_auth: String, token: String, nombre: String, telefono: String, email: String) {

        this.id_usuario = id_usuario
        this.nombre = nombre
        this.telefono = telefono
        this.email = email
        this.id_auth = id_auth
        this.token = token
    }
}

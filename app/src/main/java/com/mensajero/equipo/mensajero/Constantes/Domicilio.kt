package com.mensajero.equipo.mensajero.Constantes

class Domicilio {
    var date: Long? = null
    var comentario: String? = null
    var dir_entrega: String? = null
    var dir_compra: String? = null
    var estado: String? = null // va a tener estados de pendiente , aceptado, despachado
    var fecha_domicilio: String? = null
    var forma_de_pago: String? = null //  por ahora efectivo luego ya se aplcará pago con tarjeta
    var id_domicilio: String? = null
    var id_usuario: String? = null
    var lat_dir_entrega: Double? = 0.0
    var lat_dir_compra: Double? = 0.0
    var long_dir_entrega: Double? = 0.0
    var long_dir_compra: Double? = 0.0
    var nombre: String? = null
    var telefono: String?  = null
    var token: String? = null
    var token_empresa: String? = null
    var valor_pedido:  Double? = 0.0 // el valor de la compra
    var valor_domicilio:  Double? = 0.0 // el valor de el viaje de dentrega
    var empresa: Empresa? = null
    var descripcion: String? = null
    var ciudad: String? = null
    var codigo_mensajero: String? = null
    // var chat:

    constructor () {
    }

}
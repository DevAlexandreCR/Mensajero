package com.mensajero.equipo.mensajero.Constantes;

import java.util.ArrayList;

/**
 * Created by equipo on 07/08/2017.
 */

public class Constantes {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String EFECTIVO = "efectivo";
    private static final String PACKAGE_NAME = "com.mensajero.equipo.mensajero";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String ACTION_PROGRESO=PACKAGE_NAME+".PROGRESO";
    public static final String ACTION_FIN =PACKAGE_NAME+".FIN";
    public static final String ACTION_FIN_ERROR=PACKAGE_NAME+".ERROR";

    // request code direccion
    public static final int REQUEST_DIRECCION_INICIAL = 1;
    public static final int REQUEST_DIRECCION_FINAL = 2;

    public static final String BUSCANDO_DIRECCION = "buscando dirección...";

    public static final String GOOGLE_API_KEY = "AIzaSyApH44A7kXhY_UCePee_sx37f2G9y5KOWg";

    // Ciudades habilitadas
    public static final String POPAYAN = "Popayán";

    //Nodos de la base de datos
    public static final String BD_NOMBRE_USUARIO = "nombre";
    public static final String BD_TELEFONO_USUARIO = "telefono";
    public static final String BD_ID_USUARIO = "id_usuario";
    public static final String BD_ID_PEDIDO = "id_pedido";
    public static final String BD_USUARIO="usuario";
    public static final String BD_GERENTE="gerente";
    public static final String BD_ADMIN="admin";
    public static final String BD_PEDIDO="pedido";
    public static final String BD_DOMICILIO="domicilio";
    public static final String BD_DIR_INICIAL="dir_inicial";
    public static final String BD_ESTADO_PEDIDO="estado_pedido";
    public static final String BD_HORA_PEDIDO_PROGRAMADO  ="hora_pedido";
    public static final String BD_FECHA_PEDIDO="fecha_pedido";
    public static final String BD_VALOR_PEDIDO="valor_pedido";
    public static final String BD_PQR="pqr";
    public static final String BD_USUARIO_EMPRESA = "usuario_empresa";
    public static final String BD_COMENTARIO ="comentario";
    public static final String BD_CODIGO_MENSAJERO ="codigo_mensajero";
    public static final String BD_CODIGO ="codigo";
    public static final String BD_ASIGNAR_MOVIL ="asignar movil";
    public static final String INICIAR_DE_NOTIFICACION = "notificacion";

    //tipos de servicio

    public static final String BD_TIPO_PEDIDO="tipo_pedido";
    public static final String COMPRAS_PEDIR_DOMICILIOS="compras_domicilios";
    public static final String FACTURAS_TRAMITES="facturas_tramites";
    public static final String ENCOMIENDAS="encomiendas";
    public static final String SOLICITUD_RAPIDA="solicitud_rapida";

    //constantes para direcciones
    public static final String ELIJE_UN_DESTINO = "Elije un destino";
    public static final String ENTREGAR_EN = "Entregar en: ";
    public static final String RECOGER_EN = "Recoger en: ";
    public static final String RECOGEME_AQUI = "Recógeme aquí: ";
    public static final String QUE_HACE_EL_MENSAJERO = "¿Qué debe hacer el Mensajero? ";
    public static final String COMENTARIO_VIAJE = "Agrega la dirección patoja o sugerencias ";

    //estados de los pedidos
    public static final String BD_CANCELADO="cancelado";//puede ser por el conductor o el usuario
    public static final String BD_ESTADO_PENDIENTE ="sin_movil_asignado";//estado inicial del pedido
    public static final String BD_EN_CURSO ="en_curso";//cuando ya tiene un mensajero asignado
    public static final String BD_ESTADO_OK="terminado";//usuario en su destino final ok
    public static final String BD_ESTADO_VIAJE_INICIADO="viaje_iniciado";//cuando el conductor inicia el viaje
    public static final String CONFIRMAR_SOLICITUD = "OK";
    public static final String CANCELAR_SOLICITUD = "CANCELAR";
    public static final String VIAJE_EN_CURSO = "VIAJE EN CURSO";

    //tipo pagos
    public static final String BD_FORMA_DE_PAGO="forma_de_pago";

    //botones de buscar direcciones

    public static final String REQUESTCODE = "requestCode";

    //para permiso de ubicaciones
    public static final int PETICION_PERMISO_LOCALIZACION = 1;

    //TOKEN
    public static final String TOKEN = "Token";


    public static final String MENSAJE_SERVICIO_DIRECCION=PACKAGE_NAME+".MENSAJE";

    //para las peticiones de busqueda de direccion

    public static final String BD_ESTADO_PROGRAMADO = "programado";
    public static final String BD_MENSAJERO = "mensajero";
    public static final String BD_PEDIDO_ESPECIAL ="pedido_especial" ;
    public static final String TIPO_SERVICIO = "tipo_servicio";
    public static final String SERVICIO_MENSAJERO = "servicio_mensajero";
    public static final String SERVICIO_MENSAJERO_ESPECIAL = "servicio_mensajero_especial";
    public static final String SERVICIO_DOMICILIO = "servicio_domicilio";

    //para enviar las coordenadas de la ubicacion inicial del cliente;
    public static final String BD_LAT_DIR_INICIAL = "lat_dir_inicial";
    public static final String BD_LONG_DIR_INICIAL = "long_dir_inicial";
    public static final String MENSAJERO_ESPECIAL_CONECTADO = "mensajero_especial_conectado";
    public static final String ACTION_ID_LISTA = "id_lista";
    public static final String BD_MENSAJEROS_ESPECIAL_ENV_MENSAJE = "mensajeros_especial_env_mensaje" ;
    public static final String MENSAJERO_CONECTADO = "mensajero_conectado";
    public static final String BD_MENSAJEROS_ENV_MENSAJE = "mensajeros_env_mensaje" ;

    //Acciones del receptor de mensajes
    public static final String ACTION_SIN_MENSAJERO_CERCA = "SIN_MENSAJERO_CERCA";
    public static final String ACTION_MENSAJE_CHAT = "action.mensaje_chat";
    public static final String ACTION_CONFIRMAR_LLEGADA = "com.mensajerogo.confirmar_llegada";
    public static final String ACTION_CONFIRMAR_SALDO = "action.confirmar_saldo";



    public static final String BD_MENSAJERO_ESPECIAL = "mensajero_especial";
    public static final String URL_FOTO_PERFIL_CONDUCTOR = "mensajeros/mensajero_carro/movil_";
    public static final String URL_FOTO_PERFIL_CONDUCTOR_MOTO = "mensajeros/mensajero_moto/movil_";
    public static final String CONFIRMAR_LLEGADA = "confirmar_llegada";
    public static final String MENSAJE_CHAT = "mensaje_chat";
    public static final String MENSAJE_CHAT_DOMICILIO = "mensaje_chat_domicilio";
    public static final String REVISAR_HISTORIAL = "revisar_historial";
    public static final String ID_CANAL_LISTADO = "id_canal_01";
    public static final String NOMBRE_CANAL_LISTADO = "canal_listado";

    //constante para los bonos de firth_open
    public static final String BONO_BIENVENIDA = "MIPMENSAJERO";
    public static final String REFERIDOS = "referidos";
    public static final String BONOS = "bonos_usuarios";
    public static final String PROMOCIONES = "promociones";

    //estados de los bonos
    public static final String ESTADO_BONO_PENDIENTE = "pendiente";
    public static final String ESTADO_BONO_ACTIVO = "activo";
    public static final String CHAT = "chat";
    public static final String ACTION_ACTUALIZAR = "actualizacion_disponible";

    public static final String ID_CANAL_CHAT = "com.mensajero.canal_01";
    public static final String NOMBRE_CANAL_CHAT= "com.mensajero.chat";
    public static final String ID_CANAL_DEFAULT = "com.mensajero.canal_02";
    public static final String NOMBRE_CANAL_DEFAULT= "com.mensajero.default";

    //texto tipo servicio

    public static final String TIPO_SERVICIO_SOLICITUD_RAPIDA = "Domicilios";

    //estados del serviio

    public static final String ESTADO_SERVICIO_INICIADO ="servicio_iniciado";
    //para encomiendas
    public static final String ESTADO_PAQUETE_RECOGIDO="paquete_recogido"; //cuando el mensajero recoge el paquete
    public static final String ESTADO_PAQUETE_ENTREGADO="paquete_entregado"; //cuando el mensajero entrega la encimienda, luego tendrá la opción de terminarlo si ya recibio el dinero
    //para compras
    public static final String ESTADO_MENSAJERO_EN_TIENDA="mensajero_en_tienda";//mensajero en el lugar de la compra, inicia a facturar
    public static final String ESTADO_COMPRA_REALIZADA="compra_realizada";// ya el mensajero se dirige a la entrega
    //para domicilios
    public static final String COMPRAR_EN = "Comprar en";

    //constantes empresas
    public static final String BD_EMPRESA = "usuario_empresa";
    public static final String ID_EMPRESA = "id_usuario";
    public static final String BD_CATEGORIA_EMPRESA = "categoria";
    public static final String RESTAURANTE = "restaurante";
    public static final String FARMACIA = "farmacia";
    public static final String FLORISTERIA = "floristeria";
    public static final String LICORES = "licores";
    public static final String SUPERMERCADO = "supermercado";
    public static final String OFERTAS = "ofertas";

    // estados de un domicilio
    public static final String ESTADO_DOMICILIO_PENDIENTE = "pendiente"; // el servicio está pendiente a que la empresa lo acepte o lo confirme con el cliente
    public static final String ESTADO_DOMICILIO_CONFIRMADO = "confirmado"; // un pedido confirmado es aquel que ya tiene la aprovacion del usuario y el negocio
    public static final String ESTADO_DOMICILIO_DESPACHADO = "despachado"; // es el domicilio que ya salió del negocio
    public static final String ESTADO_DOMICILIO_ENTREGAGO = "entregado";// el usuario ya recibió su domicilio
    public static final String ESTADO_DOMICILIO_CANCELADO = "cancelado";
    public static final String ESTADO_DOMICILIO_NO_APLICA = "N/A";

    public static final String BD_ESTADO_DOMICILIO = "estado";

    // para referidos
    public static final String INI_REF_GO = "GO_";
    public static final String CODIGO_REFERIDO = "codigo_referido";


    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}

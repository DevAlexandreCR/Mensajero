package com.mensajero.equipo.mensajero.Servicios;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.MensajeChat;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by equipo on 11/09/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.i("onMensajeReceiver"," mensaje recibido de "+ from);

        if(remoteMessage.getNotification()!=null){
            Log.d("notificacion",remoteMessage.getNotification().getBody());
            String body = remoteMessage.getNotification().getBody();
            if (body != null && body.contains("bono")) {
                try {
                    ActivityManager activityManager = (ActivityManager) getApplication().getSystemService( Context.ACTIVITY_SERVICE );
                    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            Log.i("Foreground App", appProcess.processName);
                            if (appProcess.processName.equals(getApplication().getPackageName())) {
                                Log.i(TAG, " APP EN PRIMER PLANO");
                                Intent confirmar_llegada = new Intent();
                                confirmar_llegada.setAction(Constantes.ACTION_CONFIRMAR_SALDO);
                                sendBroadcast(confirmar_llegada);
                                break;
                            }
                        } else {
                            Log.i(TAG, appProcess.processName);
                            Log.i(TAG, "APP EN SEGUNDO PLANO");
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                mostratNotificacion(remoteMessage.getNotification().getTitle(),
                                        remoteMessage.getNotification().getBody(), Constantes.ID_CANAL_DEFAULT, null, null);
                            }else{
                                mostratNotificacion(remoteMessage.getNotification().getTitle(),
                                        remoteMessage.getNotification().getBody(),null, null, null);
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        mostratNotificacion(remoteMessage.getNotification().getTitle(),
                                remoteMessage.getNotification().getBody(), Constantes.ID_CANAL_DEFAULT, null, null);
                    }else{
                        mostratNotificacion(remoteMessage.getNotification().getTitle(),
                                remoteMessage.getNotification().getBody(),null, null, null);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        }
        if(remoteMessage.getData().size()>0){
            String datos = remoteMessage.getData().toString();
            Log.d("MENSAJE RECIBIDO",datos+"");
            if(datos.contains(Constantes.ACTION_SIN_MENSAJERO_CERCA)){
                Intent broadCastIntent = new Intent();
                broadCastIntent.setAction(Constantes.ACTION_SIN_MENSAJERO_CERCA);
                sendBroadcast(broadCastIntent);
                Log.d("sendbroadc","MENSAJE ENVIADO AL RECEPTOR");
            }else if(datos.contains(Constantes.ACTION_ID_LISTA)){
                Log.d("sendbroadc","ID_LISTA ENVIADO AL RECEPTOR");
                JSONObject DatosMensaje = null;
                try {
                    DatosMensaje = new JSONObject(datos);
                    String id_lista = DatosMensaje.get(Constantes.ACTION_ID_LISTA).toString();
                    Log.d("sendbroadc","ID_LISTA "+ id_lista);
                    Intent broadCastIntent = new Intent();
                    broadCastIntent.setAction(Constantes.ACTION_ID_LISTA);
                    broadCastIntent.putExtra(Constantes.ACTION_ID_LISTA,id_lista);
                    sendBroadcast(broadCastIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(datos.contains(Constantes.ACTION_CONFIRMAR_LLEGADA)){
                try {
                    ActivityManager activityManager = (ActivityManager) getApplication().getSystemService( Context.ACTIVITY_SERVICE );
                    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                Log.i("Foreground App", appProcess.processName);
                                if (appProcess.processName.equals(getApplication().getPackageName())) {
                                    Log.i(TAG, " APP EN PRIMER PLANO");
                                    Intent confirmar_llegada = new Intent();
                                    confirmar_llegada.setAction(Constantes.ACTION_CONFIRMAR_LLEGADA);
                                    sendBroadcast(confirmar_llegada);
                                    break;
                                }
                            } else {
                                Log.i(TAG, appProcess.processName);
                                Log.i(TAG, "APP EN SEGUNDO PLANO");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    mostratNotificacion("Bib...Bib!","Tu mensajero ha llegado", Constantes.ID_CANAL_DEFAULT, null, null);
                                }else{
                                    mostratNotificacion("Bib...Bib!","Tu mensajero ha llegado", null, null, null);
                                }
                            }
                        }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else if(datos.contains(Constantes.MENSAJE_CHAT)){
                JSONObject DatosMensaje = null;

                try {
                    try {
                        datos = datos.replace(" ","_");
                        DatosMensaje = new JSONObject(datos);
                        Log.i("Datos Mensaje", DatosMensaje.toString());
                        String mensaje = DatosMensaje.get("_mensaje").toString();
                        String tipo_servicio = DatosMensaje.get("tipo_servicio").toString();
                        mensaje = mensaje.replace("_"," ");
                        String id_pedido = DatosMensaje.get("_"+Constantes.BD_ID_PEDIDO).toString();
                        Log.i("datos mensaj tipo",DatosMensaje.get("tipo_servicio").toString());
                        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService( Context.ACTIVITY_SERVICE );
                        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                Log.i("Foreground App", appProcess.processName);
                                if (appProcess.processName.equals(getApplication().getPackageName())) {
                                    Log.i(TAG, " APP EN PRIMER PLANO");
                                    Intent chat = new Intent();
                                    chat.putExtra(Constantes.MENSAJE_CHAT,mensaje);
                                    chat.putExtra(Constantes.TIPO_SERVICIO, tipo_servicio);
                                    chat.putExtra(Constantes.BD_ID_PEDIDO, id_pedido);
                                    chat.setAction(Constantes.ACTION_MENSAJE_CHAT);
                                    sendBroadcast(chat);
                                    break;
                                }
                            } else {
                                Log.i(TAG, appProcess.processName);
                                Log.i(TAG, "APP EN SEGUNDO PLANO");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    mostratNotificacion("Tienes un nuevo mensaje!", mensaje, Constantes.ID_CANAL_CHAT, tipo_servicio, id_pedido);
                                }else{
                                    mostratNotificacion("Tienes un nuevo mensaje!", mensaje, null, tipo_servicio, id_pedido);
                                }
                            }
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }


        }
    }
    private void mostratNotificacion(String title, String body, @Nullable String canal, @Nullable String tipo_servicio, @Nullable String id_pedido){

        try {
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(title.contains("mensaje")){
                intent.putExtra(Constantes.TIPO_SERVICIO, tipo_servicio);
                intent.putExtra(Constantes.BD_ID_PEDIDO, id_pedido);
                if(tipo_servicio.equals(Constantes.BD_PEDIDO_ESPECIAL)){
                    intent.setAction(Constantes.MENSAJE_CHAT);
                }else if (tipo_servicio.equals(Constantes.BD_DOMICILIO)){
                    intent.setAction(Constantes.MENSAJE_CHAT_DOMICILIO);
                }

            }
            if (title.contains("Bib")) {
                intent.setAction(Constantes.CONFIRMAR_LLEGADA);
            }else if (body.contains("exito")){
                intent.setAction(Constantes.REVISAR_HISTORIAL);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            Uri sonidoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tin);

            if(canal!= null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Notification.Builder notificacion = new Notification.Builder(this, canal)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, notificacion.build());
                }
            }else{
                NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(sonidoUri)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificacion.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String TOKEN = s;


        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        try {
            String id_user = mAuth.getCurrentUser().getUid();
            //aqui se registran los datos del ususario en la base de datos
            DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO)
                    .child(id_user);

            database.child(Constantes.TOKEN).setValue(TOKEN);
            Log.i(Constantes.TOKEN,TOKEN);
        }catch (Exception e){
            Log.i("Error Auth.getid",e.toString());
        }

    }

}

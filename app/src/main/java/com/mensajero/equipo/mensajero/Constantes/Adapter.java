package com.mensajero.equipo.mensajero.Constantes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by equipo on 10/09/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.PedidosViewHolder> {


    List<Pedidos> pedidos;
    List<Mensajeros> mensajeros;
    int paranada;
    Mensajeros mensajero;
    Context context;
    String estado;
    String tipo_servicio;


    public Adapter(List<Mensajeros> mensajeros, int paranada) {
        this.mensajeros = mensajeros;
        this.paranada = paranada;
    }

    public Adapter(List<Pedidos> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;
    }

    @Override
    public PedidosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_pedidos, parent, false);
        PedidosViewHolder holder = new PedidosViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(final PedidosViewHolder holder, int position) {


        try {
            final Pedidos pedido = pedidos.get(position);
            final String codigo = pedido.getCodigo_mensajero();
            final String id_pedido = pedido.getId_pedido();
            try {
                tipo_servicio = pedido.getTipo_servicio();
            } catch (Exception e) {
                tipo_servicio = Constantes.SERVICIO_MENSAJERO;
                e.printStackTrace();
            }

            Log.i("codigo Mensajero", "" + codigo);

            //aqui vamos a traer los datos del mensajero asignado para llenar el recycler
            String tipo_mensajero = "mensajero";

            if (tipo_servicio.equals(Constantes.SERVICIO_MENSAJERO)) {
                tipo_mensajero = Constantes.BD_MENSAJERO;
                holder.textView_nombre.setText(Constantes.BD_MENSAJERO);
                holder.foto_mensajero.setImageResource(R.mipmap.boton_moto);
            } else if (tipo_servicio.equals(Constantes.SERVICIO_MENSAJERO_ESPECIAL)) {
                tipo_mensajero = Constantes.BD_MENSAJERO_ESPECIAL;
                holder.textView_nombre.setText(Constantes.BD_MENSAJERO);
                holder.foto_mensajero.setImageResource(R.mipmap.boton_carro);
            }
            if (!codigo.equals(Constantes.BD_ASIGNAR_MOVIL)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference pedidosBD = database.getReference();

                pedidosBD.child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(tipo_mensajero)
                        .orderByKey().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if (dataSnapshot.getKey().equals(codigo)) {
                            mensajero = dataSnapshot.getValue(Mensajeros.class);
                            holder.textView_nombre.setText(mensajero.getNombre());
                            holder.ratingBar.setRating(pedido.getCalificacion());
                            String pathFoto = Constantes.URL_FOTO_PERFIL_CONDUCTOR+mensajero.getCodigo();

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageReference = storage.getReference().child(pathFoto+"/foto_perfil");


                            GlideApp.with(context)
                                    .load(storageReference)
                                    .into(holder.foto_mensajero);

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            Log.i("mensajero", "" + mensajero);


            if (mensajero != null) {

            }
            DecimalFormat decimalFormat = new DecimalFormat("#");
            holder.textViewFecha.setText(pedido.getFecha_pedido());
            holder.textView_valor.setText("$" + decimalFormat.format(pedido.getValor_pedido()));
            holder.textView_dir_ini.setText(pedido.getDir_inicial());
            holder.textView_comentario.setText(pedido.getComentario());

            estado = pedido.getEstado_pedido();

            final String solicitar_aayuda_con_viaje = "Solicitar ayuda con éste viaje";
            //aqui evaluamos si el tipo de pedido es de motos o de carros para agregarle el click de llamada
            if (pedido.getTipo_pedido() == null) {
                holder.textViewTipoPedido.setTextColor(Color.BLUE);
                holder.textViewTipoPedido.setLinkTextColor(Color.BLUE);

                holder.textViewTipoPedido.setPaintFlags(holder.textViewTipoPedido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                holder.textViewTipoPedido.setText(solicitar_aayuda_con_viaje);
                holder.textViewTipoPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + "3202973621"));

                        int permissionCheck = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.CALL_PHONE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                            ActivityCompat.requestPermissions((MainActivity)context
                                    , new String[]{Manifest.permission.CALL_PHONE}, 225);
                        } else {
                            Log.i("Mensaje", "Se tiene permiso!");
                            context.startActivity(intent);
                        }
                    }
                });

            }else{
                holder.textViewTipoPedido.setText(pedido.getTipo_pedido());

            }


            //verificar si el cliente insgresó una direccion final

            holder.textView_dir_final.setText(pedido.getDir_final());

        }catch (NullPointerException e){
            Log.e("error firebase",e.toString());
            Toast.makeText(context,"Error de conexion a internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class PedidosViewHolder extends RecyclerView.ViewHolder{

        TextView textViewFecha,textViewTipoPedido, textView_dir_ini,textView_dir_final,textView_valor,textView_comentario;
        TextView  textView_nombre,text_estado;
        CircleImageView foto_mensajero;
        RatingBar ratingBar;


        public PedidosViewHolder(View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.text_fecha);
            textViewTipoPedido = itemView.findViewById(R.id.text_tipo_pedido);
            textView_dir_ini = itemView.findViewById(R.id.text_dir_inicial);
            textView_dir_final = itemView.findViewById(R.id.text_dir_final);
            textView_valor = itemView.findViewById(R.id.text_valor_pedido);
            textView_comentario = itemView.findViewById(R.id.text_comentario);
            ratingBar = itemView.findViewById(R.id.calif_mensajero);
            textView_nombre = itemView.findViewById(R.id.text_nombre_mensajero);
            foto_mensajero =  itemView.findViewById(R.id.foto_mensajero);
            text_estado = itemView.findViewById(R.id.text_estado);

        }


    }

}

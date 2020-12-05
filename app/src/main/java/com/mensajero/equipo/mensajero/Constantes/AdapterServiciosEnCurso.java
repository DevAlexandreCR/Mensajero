package com.mensajero.equipo.mensajero.Constantes;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterServiciosEnCurso extends RecyclerView.Adapter<AdapterServiciosEnCurso.PedidosViewHolder> {

    List<Pedidos> pedidos;
    Context context;


    public AdapterServiciosEnCurso() {
    }

    public AdapterServiciosEnCurso(List<Pedidos> pedidos, Context context){
        this.pedidos = pedidos;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recicler_pedidos_en_curso, viewGroup, false);
        PedidosViewHolder holder = new PedidosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PedidosViewHolder pedidosViewHolder, int i) {

        final Pedidos pedido = pedidos.get(i);

        pedidosViewHolder.boton_estado_servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pedidosViewHolder.boton_estado_servicio.getText().toString().equals(Constantes.CANCELAR_SOLICITUD)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true)
                            .setTitle("Cancelar Servicio")
                            .setMessage("¿ Desea cancelar el servicio ?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                            .getReference().child(Constantes.BD_GERENTE)
                                            .child(Constantes.BD_ADMIN).child(Constantes.BD_PEDIDO)
                                            .child(pedido.getId_pedido());
                                    databaseReference.child(Constantes.BD_ESTADO_PEDIDO)
                                            .setValue(Constantes.BD_CANCELADO).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(context,"Servicio cancelado",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(context,"Intente nuevamente",Toast.LENGTH_SHORT).show();
                                                    }
                                                    dialog.dismiss();
                                                }
                                            }
                                    );
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });

        if (pedido != null) {

            pedidosViewHolder.imagenmensajero.setImageResource(R.mipmap.boton_moto);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_MENSAJERO)
                    .child(pedido.getCodigo_mensajero());
            Query query = database.orderByKey();


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
                    if (mensajero!=null) {
                        pedidosViewHolder.texto_fecha.setText(pedido.getFecha_pedido());
                        pedidosViewHolder.texto_nombre_mensajero.setText(mensajero.getNombre());
                        pedidosViewHolder.texto_valor_servicio.setText(pedido.getValor_pedido()+"");
                        pedidosViewHolder.texto_placa_mensajero.setText(mensajero.getPlaca());
                        pedidosViewHolder.texto_telefono_mensajero.setText(mensajero.getTelefono());
                        pedidosViewHolder.texto_telefono_mensajero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + mensajero.getTelefono()));

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
                        if (pedido.getEstado_pedido().equals(Constantes.BD_EN_CURSO)) {
                            pedidosViewHolder.boton_estado_servicio.setText(Constantes.CANCELAR_SOLICITUD);
                        } else {
                            pedidosViewHolder.boton_estado_servicio.setText(pedido.getEstado_pedido().replace("_"," "));
                        }
                        String pathFoto = Constantes.URL_FOTO_PERFIL_CONDUCTOR_MOTO+mensajero.getCodigo();

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference().child(pathFoto+"/foto_perfil");


                        GlideApp.with(context)
                                .load(storageReference)
                                .into(pedidosViewHolder.imagenmensajero);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class PedidosViewHolder extends RecyclerView.ViewHolder{

        TextView texto_fecha,texto_nombre_mensajero,texto_telefono_mensajero,texto_valor_servicio
                ,texto_placa_mensajero;
        Button boton_estado_servicio;
        CircleImageView imagenmensajero;


        public PedidosViewHolder(View itemView) {
            super(itemView);

            texto_fecha = itemView.findViewById(R.id.text_fechaencurso);
            texto_nombre_mensajero = itemView.findViewById(R.id.text_nombreencurso);
            texto_placa_mensajero = itemView.findViewById(R.id.text_placaencurso);
            texto_telefono_mensajero = itemView.findViewById(R.id.texttelefono_encurso);
            texto_valor_servicio = itemView.findViewById(R.id.text_valorencurso);
            boton_estado_servicio = itemView.findViewById(R.id.boton_estadoencurso);
            imagenmensajero = itemView.findViewById(R.id.imageView4);


        }


    }
}

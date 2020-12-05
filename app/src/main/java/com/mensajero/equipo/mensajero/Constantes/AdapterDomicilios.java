package com.mensajero.equipo.mensajero.Constantes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mensajero.equipo.mensajero.Fragmentos.FragmentChat;
import com.mensajero.equipo.mensajero.Fragmentos.FragmentVerEmpresa;
import com.mensajero.equipo.mensajero.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDomicilios extends RecyclerView.Adapter<AdapterDomicilios.DomiciliosViewholder> {

    public List<Domicilio> domicilios;
    public Context context;
    public String id_domicilio;
    public String id_usuario;

    public AdapterDomicilios(List<Domicilio> domicilios, Context context) {
        this.domicilios = domicilios;
        this.context = context;
    }

    @NonNull
    @Override
    public DomiciliosViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_domi_en_curso,parent, false);
        DomiciliosViewholder viewholder = new DomiciliosViewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull DomiciliosViewholder holder, int position) {

        final Domicilio domi = domicilios.get(position);
        holder.domi_nombre_empresa.setText(domi.getEmpresa().getNombre());
        holder.domi_estado.setText(domi.getEstado());
        holder.domi_descripcion.setText(domi.getDescripcion());
        holder.domi_valor.setText("$ "+ domi.getValor_pedido());
        GlideApp.with(context)
                .load(domi.getEmpresa().getUrl_foto_perfil())
                .into(holder.foto_empresa);

        if (!domi.getEstado().equals(Constantes.ESTADO_DOMICILIO_PENDIENTE)){
            holder.cancelar.setVisibility(View.INVISIBLE);
        }
        holder.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                        .setCancelable(true)
                        .setTitle("¿Seguro desdea cancelar?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancelar_domi(domi.getId_domicilio());
                            }
                        }).setNegativeButton("NO", null).show();
            }
        });
        holder.chatear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChat fragmentChat = new FragmentChat();
                Bundle b =new Bundle();
                b.putString(Constantes.TIPO_SERVICIO, Constantes.BD_DOMICILIO);
                b.putString(Constantes.BD_ID_PEDIDO, domi.getId_domicilio());
                b.putString(Constantes.BD_NOMBRE_USUARIO, domi.getNombre());
                fragmentChat.setArguments(b);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.fragment_domi_en_curso,fragmentChat, FragmentChat.TAG);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return domicilios.size();
    }

    public static class DomiciliosViewholder extends RecyclerView.ViewHolder{

        private TextView domi_nombre_empresa, domi_descripcion, domi_valor, domi_estado;
        private CircleImageView foto_empresa;
        private Button cancelar, chatear;


        public DomiciliosViewholder(@NonNull View itemView) {
            super(itemView);

            domi_descripcion = itemView.findViewById(R.id.desc_domi_en_curso);
            domi_estado = itemView.findViewById(R.id.estado_domi_en_curso);
            domi_nombre_empresa = itemView.findViewById(R.id.nombre_domi_en_curso);
            domi_valor = itemView.findViewById(R.id.valor_domi_en_curso);
            foto_empresa = itemView.findViewById(R.id.img_domicilio_en_curso);
            cancelar = itemView.findViewById(R.id.btn_cancelar_domi_en_curso);
            chatear = itemView.findViewById(R.id.btn_chat_domi_en_curso);
        }
    }

    public void cancelar_domi(String id_domicilio){
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_DOMICILIO);
        database.child(id_domicilio).child(Constantes.BD_ESTADO_DOMICILIO).setValue(Constantes.ESTADO_DOMICILIO_CANCELADO)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,"Domicilio cancelado!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,"Ocurrio un error, intente nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

package com.mensajero.equipo.mensajero.Constantes;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mensajero.equipo.mensajero.Fragmentos.FragmentVerEmpresa;
import com.mensajero.equipo.mensajero.R;

import java.util.List;

public class AdapterEmpresas extends RecyclerView.Adapter<AdapterEmpresas.EmpresasHolder> implements View.OnClickListener {

    List<Empresa> empresas;
    Context context;
    String id_empresa;
    private static final String TAG = "AdapterEmpresas";

    public AdapterEmpresas(List<Empresa> empresas, Context context){
        this.context = context;
        this.empresas = empresas;
    }


    @NonNull
    @Override
    public EmpresasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_empresas, parent, false);
        EmpresasHolder holder = new EmpresasHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresasHolder holder, int position) {

        final Empresa e = empresas.get(position);
        holder.nombre.setText(e.getNombre());
        holder.descripcion.setText(e.getDescripcion());
        id_empresa = e.getId_usuario();
        holder.id.setText(id_empresa);
        GlideApp.with(context)
                .load(e.getUrl_foto_perfil())
                .into(holder.perfil_empresa);
        holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        TextView t = view.findViewById(R.id.text_id_empresa);
        id_empresa = t.getText().toString();
        bundle.putString(FragmentVerEmpresa.ID_EMPRESA, id_empresa);
        FragmentVerEmpresa fragmentVerEmpresa = new FragmentVerEmpresa();
        fragmentVerEmpresa.setArguments(bundle);
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.fragment_empresas,fragmentVerEmpresa, FragmentVerEmpresa.TAG);
        ft.commit();
    }

    public static class EmpresasHolder extends RecyclerView.ViewHolder{

        ImageView perfil_empresa;
        TextView nombre, descripcion, id;

        public EmpresasHolder(@NonNull View itemView) {
            super(itemView);
            perfil_empresa = itemView.findViewById(R.id.img_empresa);
            nombre = itemView.findViewById(R.id.nombre_empresa);
            descripcion = itemView.findViewById(R.id.descripcion_empresa);
            id = itemView.findViewById(R.id.text_id_empresa);
        }

    }
}

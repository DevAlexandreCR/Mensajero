package com.mensajero.equipo.mensajero.Fragmentos;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mensajero.equipo.mensajero.Constantes.AdapterDomicilios;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Domicilio;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Domicilios_en_curso extends Fragment {

    public static final String TAG = "Domicilios en curso";
    private AdapterDomicilios adapterDomicilios;
    private List<Domicilio> domicilios;
    private RecyclerView recyclerView;
    private Query query;
    private String id_usuario;
    TextView text_vacio;
    private ValueEventListener listenerDomi = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            domicilios.clear();
            if(dataSnapshot.hasChildren()){
                for (DataSnapshot s:
                        dataSnapshot.getChildren()) {
                    Domicilio domi = s.getValue(Domicilio.class);
                    if (domi != null && !domi.getEstado().equals(Constantes.ESTADO_DOMICILIO_CANCELADO)
                            && !domi.getEstado().equals(Constantes.ESTADO_DOMICILIO_ENTREGAGO)){
                        domicilios.add(domi);
                    }
                }
                if (domicilios.size() == 0)text_vacio.setText(R.string.texto_historial_vacio);
                adapterDomicilios.notifyDataSetChanged();
            } else {
                text_vacio.setText(R.string.texto_historial_vacio);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public Domicilios_en_curso() {
        // Required empty public constructor
    }

    public Domicilios_en_curso(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_domicilios_en_curso, container, false);

        domicilios = new ArrayList<>();
        adapterDomicilios = new AdapterDomicilios(domicilios, getContext());
        recyclerView = v.findViewById(R.id.recycler_domi_en_curso);
        text_vacio = v.findViewById(R.id.text_vacio_domi_en_curso);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_DOMICILIO);

        query = database.orderByChild(Constantes.BD_ID_USUARIO).equalTo(id_usuario);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(adapterDomicilios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //query.addValueEventListener(listenerDomi);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(query != null && listenerDomi != null) query.removeEventListener(listenerDomi);
    }

    @Override
    public void onResume() {
        super.onResume();
       if(query != null && listenerDomi != null) query.addValueEventListener(listenerDomi);
    }
}

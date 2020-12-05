package com.mensajero.equipo.mensajero.Fragmentos;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mensajero.equipo.mensajero.Constantes.AdapterServiciosEnCurso;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Pedidos;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mandados_en_curso extends Fragment {

    public static final String TAG = "Domicilios En Curso";
    private RecyclerView recyclerView;
    private List<Pedidos> pedidos;
    private AdapterServiciosEnCurso adapter;
    private ProgressBar progressBar;
    private TextView textview;
    private String id_user;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private ValueEventListener listenerPedidos = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            pedidos.clear();
            if (dataSnapshot.hasChildren()) {
                for(DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    Pedidos pedido = snapshot.getValue(Pedidos.class);
                    if (pedido.getId_usuario() != null && id_user!= null) {
                        if (pedido.getId_usuario().equals(id_user)) {
                            if(!pedido.getEstado_pedido().equals(Constantes.BD_ESTADO_OK) &&
                                    !pedido.getEstado_pedido().equals(Constantes.BD_CANCELADO)) {
                                pedidos.add(pedido);
                                Log.i(TAG, pedido.getNombre());
                            }else{
                                for(int i = 0; i<pedidos.size(); i++){
                                    if(pedidos.get(i).getId_pedido().equals(pedido.getId_pedido())){
                                        pedidos.remove(i);
                                    }
                                }
                            }
                        }
                    } else {
                        Log.i(TAG, "pedido nulo");
                    }
                }
                if (pedidos.size() > 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    textview.setText("");
                    textview.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    textview.setVisibility(View.VISIBLE);
                    textview.setText(R.string.texto_historial_vacio);
                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                textview.setVisibility(View.VISIBLE);
                textview.setText(R.string.texto_historial_vacio);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            textview.setText("Algo salió mal :( , Intente más tarde");
        }
    };
    Query pedidosBD;

    public Mandados_en_curso() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mandados_en_curso, container, false);
        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null) {
            id_user = mAuth.getCurrentUser().getUid();
        }else{
            Log.i(TAG, "id_user nulo");
        }


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        pedidos = new ArrayList<>();
        adapter = new AdapterServiciosEnCurso(pedidos, getActivity());
        textview = view.findViewById(R.id.trextencurso);
        progressBar = view.findViewById(R.id.progressBarencurso);
        recyclerView = view.findViewById(R.id.recycler_en_curso);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        pedidosBD = database.getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_PEDIDO).limitToLast(100);

        pedidosBD.addValueEventListener(listenerPedidos);

    }

    @Override
    public void onPause() {
        super.onPause();
        pedidosBD.removeEventListener(listenerPedidos);
    }
}

package com.mensajero.equipo.mensajero.Fragmentos;


import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mensajero.equipo.mensajero.Constantes.Adapter;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Pedidos;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mis_Solicitudes extends Fragment {

    public static final String TAG = "Historial";

    RecyclerView recyclerView;
    List<Pedidos> pedidos;
    Adapter adapter;
    ProgressBar progressBar;
    TextView textview;
    String id_user;
    String id_auth;
    FirebaseAuth mAuth;
    SharedPreferences preferences;

    public Mis_Solicitudes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mis__solicitudes, container, false);
        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = rootView.findViewById(R.id.recicler);
        View v = rootView.findViewById(R.id.recicler);
        v.setPadding(0, MainActivity.tamanoAppBar, 0, 0);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        textview =  rootView.findViewById(R.id.textView);

        pedidos = new ArrayList<>();
        adapter = new Adapter(pedidos, getActivity());

        progressBar = rootView.findViewById(R.id.progressBar2);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());




        mAuth = FirebaseAuth.getInstance();
        try {
            id_auth = mAuth.getCurrentUser().getUid();
            Log.i("auth",id_auth);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(MainActivity.Tipo_servicio.equals(Constantes.SERVICIO_MENSAJERO)){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference pedidosBD = database.getReference()
                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_PEDIDO)
                    .child(id_auth).child(Constantes.BD_PEDIDO);

            pedidosBD.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pedidos.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            Pedidos pedido = snapshot.getValue(Pedidos.class);

                            pedidos.add(0, pedido);
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
                    adapter.notifyDataSetChanged();
        }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    textview.setText("Algo salió mal :( , Intente más tarde");
                }
            });
        }else{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference pedidosEspecialBD = database.getReference()
                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO)
                    .child(id_auth).child(Constantes.BD_PEDIDO_ESPECIAL);

            pedidosEspecialBD.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pedidos.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            Pedidos pedido = snapshot.getValue(Pedidos.class);

                            pedidos.add(0, pedido);
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

                    adapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    textview.setText("Algo salió mal :( , Intente más tarde");
                }
            });
        }

    }

}

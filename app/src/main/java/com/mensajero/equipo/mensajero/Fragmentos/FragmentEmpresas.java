package com.mensajero.equipo.mensajero.Fragmentos;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mensajero.equipo.mensajero.Constantes.AdapterEmpresas;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Empresa;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEmpresas extends Fragment {

    private RecyclerView recyclerView;
    private List<Empresa> empresas;
    private AdapterEmpresas adapterEmpresas;
    private String categoria = Constantes.RESTAURANTE;
    public static final String TAG = "fragmentEmpresas";
    private String titulo = "Restaurantes";

    public FragmentEmpresas() {
    }

    public FragmentEmpresas(String categoria) {
        // Required empty public constructor
        this.categoria = categoria;
        Log.i(TAG, categoria);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_empresas, container, false);

        recyclerView = v.findViewById(R.id.recycler_list_empresas);
        empresas = new ArrayList<>();
        adapterEmpresas = new AdapterEmpresas(empresas, getActivity());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(tituloToolbar(categoria));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "view creado");

        recyclerView.setAdapter(adapterEmpresas);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pedidosBD = database.getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_EMPRESA);
        Query query = pedidosBD.orderByChild(Constantes.BD_CATEGORIA_EMPRESA).equalTo(categoria);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    empresas.clear();
                    for (DataSnapshot s:
                         dataSnapshot.getChildren()) {
                        Empresa e = s.getValue(Empresa.class);
                        empresas.add(e);
                    }
                    ProgressBar p = view.findViewById(R.id.progre);
                    p.setVisibility(View.INVISIBLE);

                    if (empresas.size() == 0) {
                        TextView t = view.findViewById(R.id.text_sin_empresas);
                        t.setVisibility(View.VISIBLE);
                        t.setText("Pronto aquí encontrarás tus tiendas favoritas... ");
                    }
                    adapterEmpresas.notifyDataSetChanged();
                } else {
                    ProgressBar p = view.findViewById(R.id.progre);
                    p.setVisibility(View.INVISIBLE);
                    TextView t = view.findViewById(R.id.text_sin_empresas);
                    t.setVisibility(View.VISIBLE);
                    t.setText("Pronto aquí encontrarás tus tiendas favoritas... ");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, databaseError.getMessage());
                ProgressBar p = view.findViewById(R.id.progre);
                p.setVisibility(View.INVISIBLE);
                TextView t = view.findViewById(R.id.text_sin_empresas);
                t.setVisibility(View.VISIBLE);
                t.setText("Algo salió mal, intenta más tarde...");
            }
        });
    }


    private String tituloToolbar(String categoria){

        String titulo = "Comidas";

        switch (categoria) {
            case Constantes.RESTAURANTE:
                titulo = "Comidas";
                break;
            case Constantes.LICORES:
                titulo = "Licores";
                break;
            case Constantes.FARMACIA:
                titulo = "Droguerías";
                break;
            case Constantes.FLORISTERIA:
                titulo = "Sorpresas";
                break;
            case Constantes.SUPERMERCADO:
                titulo = "Super y tiendas";
                break;
        }

        return titulo;
    }
}

package com.mensajero.equipo.mensajero.Fragmentos;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.mensajero.equipo.mensajero.Constantes.AdapterOferta;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Domicilio;
import com.mensajero.equipo.mensajero.Constantes.DownloadTaskDistancia;
import com.mensajero.equipo.mensajero.Constantes.Empresa;
import com.mensajero.equipo.mensajero.Constantes.GlideApp;
import com.mensajero.equipo.mensajero.Constantes.Oferta;
import com.mensajero.equipo.mensajero.Constantes.OnCompleteTaskListener;
import com.mensajero.equipo.mensajero.Constantes.Pedidos;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import ru.nikartm.support.ImageBadgeView;

public class FragmentVerEmpresa extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ID_EMPRESA = "id_empresa";
    public static final String TAG = "FragmentVerEmpresa";
    private String id_empresa;
    private ImageView foto_portada, foto_perfil;
    private ImageBadgeView canasta;
    private TextView text_titulo, text_descripcion, text_horario,text_valor_domicilio;
    private Empresa empresa;
    private Oferta oferta;
    private ArrayList<Oferta> ofertas;
    private RecyclerView recyclerView;
    private AdapterOferta adapterOferta;
    private MainActivity main;
    private double valor_domicilio;
    private DialogConfirmarDomi dialog_confirmar;


    public FragmentVerEmpresa() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_empresa = getArguments().getString(ID_EMPRESA);
        }

        ofertas = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_ver_empresa, container, false);

         text_titulo = v.findViewById(R.id.text_nombre_empresa);
         text_descripcion = v.findViewById(R.id.text_descripcion_empresa);
         text_horario = v.findViewById(R.id.text_horario_empresa);
         foto_perfil = v.findViewById(R.id.perfil_empresa);
         foto_portada = v.findViewById(R.id.portada_empresa);
         recyclerView = v.findViewById(R.id.recycler_ofertas);
         canasta = v.findViewById(R.id.img_canasta);
         text_valor_domicilio = v.findViewById(R.id.text_valor_domicilio);


         adapterOferta = new AdapterOferta(ofertas, getActivity(), canasta);
         recyclerView.setAdapter(adapterOferta);
         recyclerView.setHasFixedSize(true);
         recyclerView.setItemAnimator(new DefaultItemAnimator());
         recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        main = (MainActivity)getActivity();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pedidosBD = database.getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_EMPRESA)
                .child(id_empresa);
        pedidosBD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())return;
                empresa= dataSnapshot.getValue(Empresa.class);
                text_titulo.setText(empresa.getNombre());
                text_descripcion.setText(empresa.getDescripcion());
                text_horario.setText(empresa.getHorarios().getLunes().getAbre() +" : "+ empresa.getHorarios().getLunes().getCierra());
                GlideApp.with(getActivity())
                        .load(empresa.getUrl_foto_perfil())
                        .into(foto_perfil);
                GlideApp.with(getActivity())
                        .load(empresa.getUrl_foto_portada())
                        .into(foto_portada);
                try {
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(empresa.getNombre());
                    ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String urldistancia = obtenerdistanciaURL(new LatLng(empresa.getLatitud(),empresa.getLongitud()),
                        main.pocisionInicial);
                DownloadTaskDistancia downloadTaskDistancia = new DownloadTaskDistancia(null, new OnCompleteTaskListener() {
                    @Override
                    public void onCompleteTask(String distanciaString, int distanciaNum, String tiempoString, int tiempoNum) {
                        valor_domicilio = calcularPrecio(distanciaNum);
                        text_valor_domicilio.setText("domicilio $ "+ valor_domicilio);
                    }
                });
                downloadTaskDistancia.execute(urldistancia);
                canasta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(empresa.getCategoria());
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pedidosBD.child(Constantes.OFERTAS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot:
                         dataSnapshot.getChildren()) {
                        oferta = snapshot.getValue(Oferta.class);
                        ofertas.add(oferta);
                    }
                    adapterOferta.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // crear URl para la solicitud de distancia y tiempo
    private String obtenerdistanciaURL(LatLng origin, LatLng dest) {

        String str_origin = origin.latitude + "," + origin.longitude;

        String str_dest = dest.latitude + "," + dest.longitude;

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + str_origin + "&destinations="
                + str_dest+ "&key="+ getResources().getString(R.string.API_KEY_PROD);

        return url;
    }

    public double calcularPrecio(int distancia) {
        double precio = 0;
        final double porcentaje = 0.1;

        if (distancia > 0) {
            if (distancia < 2500) {
                precio = 2000 + (2000 * porcentaje);
            } else if (distancia >= 2500 && distancia < 4000) {
                precio = 3000 + (3000 * porcentaje);
            } else if (distancia >= 4000 && distancia < 6500) {
                precio = 4000 + (4000 * porcentaje);
            } else if (distancia >= 6500 && distancia < 8000) {
                precio = 5000 + (5000 * porcentaje);
            } else if (distancia >= 8000 && distancia < 10000) {
                precio = 6000 + (6000 * porcentaje);
            } else if (distancia >= 10000 && distancia < 11000) {
                precio = 7000 + (7000 * porcentaje);
            } else if (distancia >= 11000 && distancia < 13000) {
                precio = 8000 + (8000 * porcentaje);
            } else if (distancia >= 13000 && distancia < 14000) {
                precio = 10000 + (10000 * porcentaje);
            } else if (distancia >= 14000 && distancia < 15000) {
                precio = 12000 + (12000 * porcentaje);
            } else if (distancia >= 15000 && distancia < 16000) {
                precio = 14000 + (14000 * porcentaje);
            }
        }

        return precio;

    }

    private void showDialog(String categoria_empresa){
        dialog_confirmar = new DialogConfirmarDomi(categoria_empresa,adapterOferta,main, empresa, valor_domicilio);
        FragmentManager manager = getFragmentManager();
        dialog_confirmar.show(manager, DialogConfirmarDomi.TAG);
    }
}

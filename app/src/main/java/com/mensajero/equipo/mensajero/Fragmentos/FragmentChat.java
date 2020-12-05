package com.mensajero.equipo.mensajero.Fragmentos;


import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.mensajero.equipo.mensajero.Constantes.AdapterChat;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Domicilio;
import com.mensajero.equipo.mensajero.Constantes.MensajeChat;
import com.mensajero.equipo.mensajero.Constantes.Pedidos;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChat extends Fragment implements View.OnClickListener {

    public static final String TAG = "Chat";

    private RecyclerView recyclerView;
    private AdapterChat adapterChat;
    private List<MensajeChat> mensajes;
    private String id_pedido;
    private ValueEventListener listenerChat;
    private TextView texto_chat_vacio;
    private Button enviar;
    private String nombre;
    private EditText textoMensaje;
    private ProgressBar progressBar;
    private FirebaseFunctions funtions;
    private Pedidos pedido_go;
    private String tipo_setvicio; //para domicilios, mensajero bike o mensajero go
    private Domicilio domicilio;
    private String token_destinatario;


    public FragmentChat() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_chat, container, false);
        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        funtions = FirebaseFunctions.getInstance();
        mensajes = new ArrayList<>();
        recyclerView = v.findViewById(R.id.reyclerview_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterChat = new AdapterChat(getActivity(),mensajes);
        textoMensaje = v.findViewById(R.id.edittext_chatbox);
        texto_chat_vacio = v.findViewById(R.id.texto_chat_vacio);
        enviar = v.findViewById(R.id.button_chatbox_send);
        progressBar = v.findViewById(R.id.progressBar_enviar_mensaje);
        if (getArguments() != null) {
            id_pedido = getArguments().getString(Constantes.BD_ID_PEDIDO);
            nombre = getArguments().getString(Constantes.BD_NOMBRE_USUARIO);
            tipo_setvicio = getArguments().getString(Constantes.TIPO_SERVICIO);

            if (id_pedido !=null && tipo_setvicio != null) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference pedidosEspecialBD = database.getReference()
                        .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(tipo_setvicio)
                        .child(id_pedido);
                Query query = pedidosEspecialBD;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        switch (tipo_setvicio){
                            case Constantes.BD_DOMICILIO:
                                domicilio = dataSnapshot.getValue(Domicilio.class);
                                token_destinatario = domicilio.getToken_empresa();
                                break;
                            case Constantes.BD_PEDIDO_ESPECIAL:
                                pedido_go = dataSnapshot.getValue(Pedidos.class);
                                token_destinatario = pedido_go.getToken_conductor();
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        listenerChat = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mensajes.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {
                        MensajeChat mensaje = snapshot.getValue(MensajeChat.class);
                        mensajes.add(mensaje);
                    }
                    if (!mensajes.isEmpty()) {
                        texto_chat_vacio.setVisibility(View.INVISIBLE);
                    }

                    adapterChat.notifyDataSetChanged();

                }
                recyclerView.scrollToPosition(mensajes.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setAdapter(adapterChat);
        enviar.setOnClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (id_pedido != null && tipo_setvicio != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference pedidosEspecialBD = database.getReference()
                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(tipo_setvicio)
                    .child(id_pedido).child(Constantes.CHAT);

            Query query = pedidosEspecialBD.orderByKey();
            query.addValueEventListener(listenerChat);


        }else{
            Toast.makeText(getActivity(),"Ha ocurrido un error, intenta nuevamente",Toast.LENGTH_LONG).show();
            Log.i("datos", "" + tipo_setvicio+id_pedido);
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.button_chatbox_send:

                try {
                    if (textoMensaje.getText() != null && textoMensaje.getText().toString().length() >= 2) {
                        progressBar.setVisibility(View.VISIBLE);
                        final String mensaje = textoMensaje.getText().toString();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference currentServiceDB = database.getReference()
                                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(tipo_setvicio)
                                .child(id_pedido).child(Constantes.CHAT);

                        currentServiceDB = currentServiceDB.push();
                        String id_usuario = FirebaseAuth.getInstance().getUid();

                        currentServiceDB.setValue(new MensajeChat(mensaje,id_usuario,nombre))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        if (token_destinatario !=null) {
                                            ChatServicio(mensaje, token_destinatario, tipo_setvicio);
                                        }else {
                                            Toast.makeText(getActivity(),"intente más tarde",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(),"intente más tarde",Toast.LENGTH_SHORT).show();
                            }
                        });
                        try {
                            textoMensaje.setText("");
                            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow( textoMensaje.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            recyclerView.scrollToPosition(mensajes.size()-1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(getActivity(),"Mensaje muy corto",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Ups! algo salió mal, intenta más tarde",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private Task<String> ChatServicio(String mensaje, String token_usuario, String tipo_setvicio) {
        Map<String, Object> data = new HashMap<>();
        data.put(MensajeChat.CLAVE_CHAT, Constantes.MENSAJE_CHAT);
        data.put(MensajeChat.TEXTO_MENSAJE, mensaje);
        data.put(MensajeChat.TOKEN_DESTINATARIO, token_usuario);
        data.put(Constantes.TIPO_SERVICIO, tipo_setvicio);
        data.put(Constantes.BD_ID_PEDIDO,id_pedido);

        return funtions
                .getHttpsCallable("ChatServicio")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        Log.i("ChatServicio", result);
                        return result;
                    }
                });
    }
}

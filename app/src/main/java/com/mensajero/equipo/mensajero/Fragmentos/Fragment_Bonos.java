package com.mensajero.equipo.mensajero.Fragmentos;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;
import com.mensajero.equipo.mensajero.Constantes.Usuario;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Bonos extends Fragment implements View.OnClickListener{

    public static final String TAG = "Mis Bonos";
    public String id_user;
    private TextView texto_saldo,texto_codigo,copiar;
    private EditText ET_cod_prom;
    private Button boton_verificar_cod_prom,boton_compartir;
    private Usuario usuario;
    private ProgressDialog progressDatosusuario;
    private ListView listaReferidos;
    private ArrayAdapter<String> lista;


    public Fragment_Bonos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__bonos, container, false);
        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getArguments()!=null) {
            id_user = getArguments().getString(Constantes.BD_ID_USUARIO,"nulo");
        }
        if(id_user.equals("nulo")){
            id_user = FirebaseAuth.getInstance().getUid();
        }

        //inicializar campos de texto y botones de la UI
        texto_codigo = v.findViewById(R.id.text_codigo_invitacion);
        texto_saldo = v.findViewById(R.id.text_saldo);
        copiar = v.findViewById(R.id.text_copiar);
        ET_cod_prom = v.findViewById(R.id.ETcod_prom);
        listaReferidos = v.findViewById(R.id.lista);
        boton_compartir = v.findViewById(R.id.boton_invitar);
        boton_verificar_cod_prom = v.findViewById(R.id.boton_verificar_cod_prom);
        progressDatosusuario = new ProgressDialog(getActivity());
        lista = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boton_verificar_cod_prom.setOnClickListener(this);
        boton_compartir.setOnClickListener(this);
        copiar.setOnClickListener(this);
        progressDatosusuario.setMessage("Cargando datos...");
        progressDatosusuario.setCancelable(true);
        progressDatosusuario.setCanceledOnTouchOutside(false);
        datosUsuario(id_user);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.boton_invitar:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                final String URL_DESCARGA = "http://cort.as/-7d6j";
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola éste es mi código de referido " +
                        "de Mensajero:    "+ id_user + "              descargala aqui...    "+ URL_DESCARGA);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.boton_verificar_cod_prom:
                String codigo = ET_cod_prom.getText().toString();
                ET_cod_prom.setText("");
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(ET_cod_prom.getWindowToken(), 0);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                if (!codigo.isEmpty()) {
                    verificarCodigo(codigo);
                } else {
                    ET_cod_prom.setError("debe escribir un código");
                }
                break;
            case R.id.text_copiar:
                try {
                    ClipData clip = ClipData.newPlainText("text", id_user);
                    ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(MainActivity.CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(),"copiado",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }


    public void datosUsuario(String id_user){

        progressDatosusuario.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference currenUser = database.getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO)
                .child(id_user);
        final Query query = currenUser.orderByKey();
        lista.clear();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null) {
                     usuario = dataSnapshot.getValue(Usuario.class);
                     if(usuario != null){
                         texto_codigo.setText(usuario.getCodigo_referido()+"");
                         progressDatosusuario.dismiss();
                        texto_saldo.setText(usuario.getSaldo()+"");
                     }
                     }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                usuario = null;
                progressDatosusuario.dismiss();
                texto_saldo.setText("0");
                Toast.makeText(getActivity(),"Error al cargar, intente más tarde",Toast.LENGTH_LONG).show();
                Log.i("ERROR_DATOSDU",databaseError.getDetails());
            }
        });

        Query query1 = database.getReference().child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BONOS)
                .child(id_user).child(Constantes.REFERIDOS);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        if (snapshot.getKey()!=null) {
                            final String estado = snapshot.getValue().toString();
                            Query query2 = database.getReference()
                                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO)
                                    .child(snapshot.getKey());
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        Usuario usuariorreferido = dataSnapshot.getValue(Usuario.class);
                                        String referido = usuariorreferido.getNombre()+" - "+estado;
                                        lista.add(referido);
                                        listaReferidos.setAdapter(lista);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }else{
                    String lista_vacia = "0 redimidos / 0 pendientes";
                    lista.add(lista_vacia);
                    listaReferidos.setAdapter(lista);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void verificarCodigo(final String codigo){

        progressDatosusuario.setMessage("Validando...");
        progressDatosusuario.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference currenUser = database.getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.PROMOCIONES)
                .child(codigo);

        Query query = currenUser.orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String codigo_activo = dataSnapshot.getValue().toString();
                    if (codigo_activo.equals(Constantes.ESTADO_BONO_ACTIVO)) {
                        final DatabaseReference data_bono = FirebaseDatabase.getInstance().getReference()
                                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                .child(Constantes.BONOS);
                        Query query1 = data_bono.child(id_user).child(codigo);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null){
                                    Toast.makeText(getActivity(),"Ya activaste éste codigo anteriormente"
                                            ,Toast.LENGTH_LONG).show();
                                    progressDatosusuario.dismiss();
                                }else{
                                    data_bono.child(id_user).child(codigo).setValue(Constantes.ESTADO_BONO_PENDIENTE).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        try {
                                                                Toast.makeText(getActivity(),"En un momento tu " +
                                                                        "bono será cargado a tu saldo",Toast.LENGTH_LONG).show();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity(),"Ha ocurrido un error, intenta más tarde"
                                                                ,Toast.LENGTH_LONG).show();
                                                    }
                                                    progressDatosusuario.dismiss();
                                                }
                                            }
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else{
                        Toast.makeText(getActivity(),"Codigo promocional VENCIDO"
                                ,Toast.LENGTH_LONG).show();
                        progressDatosusuario.dismiss();
                    }
                }else{
                    Toast.makeText(getActivity(),"Codigo promocional NO válido"
                            ,Toast.LENGTH_LONG).show();
                    progressDatosusuario.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Ha ocurrido un error, intenta más tarde"
                        ,Toast.LENGTH_LONG).show();

                                    progressDatosusuario.dismiss();

            }
        });
    }

}























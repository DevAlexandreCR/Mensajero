package com.mensajero.equipo.mensajero.Fragmentos;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.LoginActivity;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;
import com.mensajero.equipo.mensajero.Constantes.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_perfil extends Fragment implements View.OnClickListener{
    public static final String TAG = "Mi Perfil";
    public SharedPreferences preferences;
    private TextInputEditText ET_nombre,ET_numero,ET_email;
    private Button cerrar_sesion,cambiar_pass;
    private String nombre, email, telefono;


    public Fragment_perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        try {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        //iniciar vistas
        ET_email = v.findViewById(R.id.perfil_email);
        ET_email.setFocusable(false);
        ET_nombre = v.findViewById(R.id.perfil_nombre);
        ET_nombre.setFocusable(false);
        ET_numero = v.findViewById(R.id.perfil_telefono);
        ET_numero.setFocusable(false);
        cambiar_pass = v.findViewById(R.id.boton_cambiar_pass);
        cerrar_sesion = v.findViewById(R.id.boton_cerrar_sesion);
        cambiar_pass.setOnClickListener(this);
        cerrar_sesion.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Gson gson = new Gson(); //Instancia Gson.
        String json = preferences.getString(Constantes.BD_USUARIO, null);
        Usuario usuario = gson.fromJson(json, Usuario.class);

        if (usuario!=null) {
            ET_email.setText(usuario.getEmail());
            ET_numero.setText(usuario.getTelefono());
            ET_nombre.setText(usuario.getNombre());
        }else{
                if(MainActivity.isOnline(getActivity())){
                    String id_user = FirebaseAuth.getInstance().getUid();

                    if (id_user!=null) {
                        datos(id_user);
                    }else{
                        Log.i("iduser perfil", "este es el id");
                    }
                }else{
                    Toast.makeText(getActivity(),"Verifique su conexión a internet",Toast.LENGTH_LONG).show();
                    ET_numero.setText("000 000 0000");
                    ET_nombre.setText("Nombre");
                    ET_email.setText("minombre@ejemplo.com");
                }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.boton_cambiar_pass:
                if (email!=null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("¿Enviar correo de recuperación a "+email + "?")
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(getActivity(),
                                                        "Se ha enviado un email de recuperación, sigue los pasos para continuar ",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Error emailrecup",e.getLocalizedMessage() );
                                    String email_no_regsitrado = "There is no user record corresponding to this identifier. The user may have been deleted.";

                                    try {
                                        if (e.getLocalizedMessage().equals(email_no_regsitrado)) {
                                            Toast.makeText(getActivity(),
                                                    "No se encontró ninguna cuanta con ese email," +
                                                            " puedes registrarte o corregir el email",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "Ha ocurrido un error, intente más tarde ",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        }

                    }).create().show();
                }else{
                    Toast.makeText(getActivity(),"Email novlaido ",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.boton_cerrar_sesion:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Cerrar Sesión?")
                        .setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //cerrar la sesion del usuario actual.
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        })
                        .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                break;
        }
    }

    public void datos(String id){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference currenUser = database.getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO)
                .child(id);
        final Query query = currenUser.orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    nombre = usuario.getNombre();
                    telefono = usuario.getTelefono();
                    email = usuario.getEmail();
                    ET_numero.setText(telefono);
                    ET_nombre.setText(nombre);
                    ET_email.setText(email);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


































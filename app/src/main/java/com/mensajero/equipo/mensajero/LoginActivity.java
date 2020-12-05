package com.mensajero.equipo.mensajero;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Mensajeros;
import com.mensajero.equipo.mensajero.Constantes.Usuario;

import java.util.Arrays;
import java.util.List;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener{

    // UI referencias.
    private TextInputEditText mPasswordView,mNombreView,mNumeroView,mEmailView,mReferidoView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private Query query;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity" ;
    private TextView textView_correo, texto_terminos, textinfo;
    private ProgressDialog progressDialog, progres_referido;
    private Button mRegsitrarButton, recup_contrasena;
    private TextInputLayout tnombrelayout,tnumerolyout,treferidolayout,tpasslayout;
    private String email;
    private String nombre;
    private String numero;
    private final String NUEVA_CUENTA = "¿No tienes cuenta? Regístrate";
    public Animation vibrar;
    public List<Usuario> listausuarios;
    public String id_user;
    public String id_auth;
    public  String token;
    public boolean es_empresa = false;
    public String direccion_empresa;
    public int cuantos_referidos_tiene;
    boolean usuario_existe = false;
    boolean numero_existe = false;
    private  DatabaseReference database;
    private Usuario referido_por;
    private Mensajeros ref_por_go;
    private String id_de_quien_refiere;
    private RadioGroup radioGroup;
    LinearLayout layout;
    private RadioGroup.OnCheckedChangeListener ra = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getCheckedRadioButtonId()) {
                case R.id.radioButtonU:
                    es_empresa =false;
                    tnombrelayout.setHint(getResources().getString(R.string.prompt_nombre));
                    tnumerolyout.setHint(getResources().getString(R.string.prompt_num_tel));
                    treferidolayout.setHint(getResources().getString(R.string.prompt_referido));
                    break;
                case R.id.radioButtonE:
                    es_empresa = true;
                    tnombrelayout.setHint("Nombre de la empresa");
                    tnumerolyout.setHint("Teléfono de la empresa");
                    treferidolayout.setHint("Direccion de la empresa");
                    break;
            }
        }
    };
    //aqui guardaremos los datos para restablecer un pedido si está en curso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // Set up the login form

        //definimos la animacion
        vibrar = AnimationUtils.loadAnimation(this,R.anim.vibrar);
        vibrar.setRepeatCount(3);
        vibrar.reset();

        //inicializamos el arreglo de ususarios
        //listausuarios = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO);


        mLoginFormView = findViewById(R.id.login_form);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null) {
            mAuth.getCurrentUser().reload();
        }
        mAuth.setLanguageCode("es");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {

                    Intent sesionIniciada = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(sesionIniciada);
                    dialog.dismiss();
                    finish();
                } else {

                    dialog.dismiss();
                }
            }
        };
        layout =  findViewById(R.id.layoutregistrar);
        mEmailView = findViewById(R.id.email);
        mPasswordView =  findViewById(R.id.password);
        mNombreView =  findViewById(R.id.nombre);
        mNumeroView =  findViewById(R.id.numero_tel);
        tnombrelayout =  findViewById(R.id.tnombre);
        tnumerolyout =  findViewById(R.id.tnumero);
        tpasslayout = findViewById(R.id.textInputLayout2);
        treferidolayout = findViewById(R.id.treferido);
        mReferidoView = findViewById(R.id.referido);
        textView_correo =  findViewById(R.id.textView_correo_usuario);
        progressDialog = new ProgressDialog(this);
        progres_referido = new ProgressDialog(this);
       // mNumeroView.setVisibility(View.INVISIBLE);
       // mNombreView.setVisibility(View.INVISIBLE);
       // tnombrelayout.setVisibility(View.INVISIBLE);
       // tnumerolyout.setVisibility(View.INVISIBLE);
       // treferidolayout.setVisibility(View.INVISIBLE);
       // mReferidoView.setVisibility(View.INVISIBLE);
        recup_contrasena =findViewById(R.id.recuperar_contrasena);
        recup_contrasena.setOnClickListener(this);
        texto_terminos = findViewById(R.id.text_terminos);
        texto_terminos.setOnClickListener(this);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setVisibility(View.INVISIBLE);
        radioGroup.setOnCheckedChangeListener(ra);
        textinfo = findViewById(R.id.textinfo);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT
                , 0);
        params.topToBottom = R.id.textInputLayout2;
        params.bottomToTop = R.id.iniciarSesion;
        layout.setLayoutParams(params);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        final Button mEmailSignInButton =  findViewById(R.id.iniciarSesion);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailSignInButton.getText().equals("Registrarse")) {

                    if(es_empresa){
                        registrar();
                    }else{
                        String referido = mReferidoView.getText().toString();
                        ocultarTeclado(mReferidoView);
                        if(!referido.isEmpty()) {
                            isReferidoValid(referido);
                        }else{
                            registrar();
                        }
                    }


                } else {
                    iniciarSesion();
                }

            }
        });

        mRegsitrarButton =  findViewById(R.id.registrar);
        mRegsitrarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRegsitrarButton.getText().toString().equals(NUEVA_CUENTA)){
                    mEmailSignInButton.startAnimation(vibrar);
                    mEmailSignInButton.setText(R.string.action_registrarse);
                    mRegsitrarButton.setText(R.string.si_cuenta);
                   // mNombreView.setVisibility(View.VISIBLE);
                    tpasslayout.setHint("Crea una contraseña");
                   // mNumeroView.setVisibility(View.VISIBLE);
                   // tnumerolyout.setVisibility(View.VISIBLE);
                   // tnombrelayout.setVisibility(View.VISIBLE);
                   // treferidolayout.setVisibility(View.VISIBLE);
                   // mReferidoView.setVisibility(View.VISIBLE);
                    //radioGroup.setVisibility(View.VISIBLE);
                    //textinfo.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT
                            , ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    params.topToBottom = R.id.textInputLayout2;
                    params.bottomToTop = R.id.iniciarSesion;
                    layout.setLayoutParams(params);

                } else {
                    mEmailSignInButton.startAnimation(vibrar);
                    mEmailSignInButton.setText(R.string.action_sign_in);
                    mRegsitrarButton.setText(R.string.nueva_cuenta);
                    mNombreView.setVisibility(View.INVISIBLE);
                   // tpasslayout.setHint("Contraseña");
                   // mNumeroView.setVisibility(View.INVISIBLE);
                   // tnumerolyout.setVisibility(View.INVISIBLE);
                   // tnombrelayout.setVisibility(View.INVISIBLE);
                   // treferidolayout.setVisibility(View.INVISIBLE);
                   // mReferidoView.setVisibility(View.INVISIBLE);
                    //radioGroup.setVisibility(View.INVISIBLE);
                    //textinfo.setVisibility(View.INVISIBLE);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT
                            , 0);
                    params.topToBottom = R.id.textInputLayout2;
                    params.bottomToTop = R.id.iniciarSesion;
                    layout.setLayoutParams(params);

                }

            }

        });
    }

    private void registrar() {

        progressDialog.setMessage("Registrando...");
        progressDialog.show();
        email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString();
        nombre = mNombreView.getText().toString();
        numero = mNumeroView.getText().toString();

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mNumeroView.setError(null);
        mNombreView.setError(null);
        mReferidoView.setError(null);

        View foco = null;
        Boolean cancel=false;

        if(!isEmailValid(email)){
            mEmailView.setError(getString(R.string.error_invalid_email));
            foco = mEmailView;
            cancel = true;
            progressDialog.dismiss();
        }else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            foco = mPasswordView;
            cancel = true;
            progressDialog.dismiss();
        }else if (!isNombreValid(nombre)){
            mNombreView.setError(getString(R.string.error_invalid_nombre));
            foco = mNombreView;
            cancel = true;
            progressDialog.dismiss();
        }else if (!isNumeroValid(numero)){
            mNumeroView.setError(getString(R.string.error_invalid_numero));
            foco = mNumeroView;
            cancel = true;
            progressDialog.dismiss();
        }else{
            EditText editText;
            if(mReferidoView.hasFocus()){
                editText = mReferidoView;
            }else if (mNombreView.hasFocus()){
                editText = mNombreView;
            }else if(mNumeroView.hasFocus()){
                editText = mNumeroView;
            }else if(mPasswordView.hasFocus()){
                editText = mPasswordView;
            }else if(mEmailView.hasFocus()){
                editText = mEmailView;
            }else{
                editText = null;
            }

            if (editText!=null) {
                ocultarTeclado(editText);
            }
            if(es_empresa){
                if (isDireccionValid(mReferidoView.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Usuario registrado... Iniciando sesion...",
                                                Toast.LENGTH_SHORT).show();
                                        mNombreView.setVisibility(View.INVISIBLE);
                                        mNumeroView.setVisibility(View.INVISIBLE);
                                        tnombrelayout.setVisibility(View.INVISIBLE);
                                        tnumerolyout.setVisibility(View.INVISIBLE);
                                        id_auth = mAuth.getCurrentUser().getUid();
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                            @Override
                                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                                token = instanceIdResult.getToken();
                                                id_user = mAuth.getCurrentUser().getUid();
                                                mAuth.setLanguageCode("es");
                                                Usuario nuevousuario;
                                                nuevousuario = new Usuario(id_user,id_auth,token , nombre, numero, email);
                                                nuevousuario.setDireccion_empresa(mReferidoView.getText().toString());
                                                nuevousuario.setEs_empresa(true);

                                                //aqui guardamos el usuario en la base de datos
                                                database.child(id_user).setValue(nuevousuario);
                                                final DatabaseReference data_empresa = FirebaseDatabase.getInstance().getReference()
                                                            .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                                            .child(Constantes.BD_USUARIO_EMPRESA);
                                                data_empresa.child(id_user).setValue(nuevousuario);

                                                progressDialog.dismiss();
                                            }
                                        });



                                    } else {
                                        String mensaje_error;
                                        String mensaje_usuario_existe = "The email address is already in use by another account.";
                                        try {
                                            mensaje_error = task.getException().getLocalizedMessage();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            mensaje_error = "";
                                        }

                                        if (mensaje_error.equals(mensaje_usuario_existe)) {
                                            Toast.makeText(LoginActivity.this, "Ya existe una cuenta con ese email",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "error al registrar, intente nuevamente",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                } else {
                    mReferidoView.setError(getString(R.string.error_incorrect_direccion));
                    foco = mReferidoView;
                    cancel = true;
                    progressDialog.dismiss();
                }
            }else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Usuario registrado... Iniciando sesion...",
                                            Toast.LENGTH_SHORT).show();
                                    mNombreView.setVisibility(View.INVISIBLE);
                                    mNumeroView.setVisibility(View.INVISIBLE);
                                    tnombrelayout.setVisibility(View.INVISIBLE);
                                    tnumerolyout.setVisibility(View.INVISIBLE);
                                    id_auth = mAuth.getCurrentUser().getUid();
                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {
                                            token = instanceIdResult.getToken();
                                            id_user = mAuth.getCurrentUser().getUid();
                                            mAuth.setLanguageCode("es");
                                            Usuario nuevousuario;
                                            nuevousuario = new Usuario(id_user,id_auth,token , nombre, numero, email);

                                            if (id_de_quien_refiere != null) {
                                                nuevousuario.setRefiere(id_de_quien_refiere);
                                            }

                                            //aqui guardamos el usuario en la base de datos
                                            database.child(id_user).setValue(nuevousuario);

                                            if (id_de_quien_refiere!=null) {
                                                //aqui agregamos el usuario a la lista de ususarios con bonos y le damos el bono de REFERIDO SI LO TIENE
                                                final DatabaseReference data_bono = FirebaseDatabase.getInstance().getReference()
                                                        .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                                        .child(Constantes.BONOS);
                                                // aqui vamos a ver cuantos bonos por refereir tiene el usuario
                                                // que refirió a éste y le sumamos otro bono si es el caso
                                                data_bono.child(id_de_quien_refiere)
                                                        .child(Constantes.REFERIDOS).child(id_user).setValue(Constantes.ESTADO_BONO_PENDIENTE);
                                            }
                                            progressDialog.dismiss();
                                        }
                                    });



                                } else {
                                    String mensaje_error;
                                    String mensaje_usuario_existe = "The email address is already in use by another account.";
                                    try {
                                        mensaje_error = task.getException().getLocalizedMessage();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        mensaje_error = "";
                                    }

                                    if (mensaje_error.equals(mensaje_usuario_existe)) {
                                        Toast.makeText(LoginActivity.this, "Ya existe una cuenta con ese email",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "error al registrar, intente nuevamente",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }



        }

        if(cancel) {

            foco.requestFocus();

        }

    }

    private void iniciarSesion() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        progressDialog.setMessage("Iniciando Sesion, por favor espere...");
        progressDialog.show();

        View foco = null;
        Boolean cancel=false;

        if(!isEmailValid(email)){
            Toast.makeText(LoginActivity.this, "ingrese el email correctamente",
                    Toast.LENGTH_SHORT).show();
            mEmailView.setError(getString(R.string.error_invalid_email));
            foco = mEmailView;
            cancel = true;
            progressDialog.dismiss();
        }else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            foco = mPasswordView;
            cancel = true;
            progressDialog.dismiss();
        }
        if(cancel) {

            foco.requestFocus();

        }else{

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail", task.getException());
                            if (MainActivity.isOnline(LoginActivity.this)) {
                                Toast.makeText(LoginActivity.this, "el usuario no esta registrado o" +
                                                " la constraseña es incorrecta",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "Verifique su conexión a internet",
                                        Toast.LENGTH_SHORT).show();
                            }
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    private boolean isNombreValid(String nombre) {
        //TODO: Replace this with your own logic
        return nombre.length() > 5;
    }

    private boolean isNumeroValid(String numero) {
        //TODO: Replace this with your own logic
        return numero.length() == 10;
    }
    private boolean isDireccionValid(String direccion) {
        //TODO: Replace this with your own logic
        return direccion.length() > 7;
    }

    private void isReferidoValid(final String referido){

        if(referido!=null){
            progres_referido.setMessage("Validando id...");
            progres_referido.setCancelable(false);
            progres_referido.setCanceledOnTouchOutside(false);
            progres_referido.show();
            try {
                if(referido.substring(0,3).equals(Constantes.INI_REF_GO)){
                    query = FirebaseDatabase.getInstance().getReference()
                            .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_MENSAJERO_ESPECIAL).orderByChild(Constantes.CODIGO_REFERIDO).equalTo(referido);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                Log.i("referido", dataSnapshot.getValue().toString());
                                for (DataSnapshot s:
                                        dataSnapshot.getChildren()) {
                                    ref_por_go = s.getValue(Mensajeros.class);
                                    id_de_quien_refiere = ref_por_go.getCodigo();
                                    Toast.makeText(getApplicationContext(),"Referido por: "+ref_por_go.getNombre()
                                            ,Toast.LENGTH_LONG).show();
                                }
                            }else{
                                id_de_quien_refiere =null;
                            }
                            progres_referido.dismiss();
                            if (id_de_quien_refiere!=null) {
                                registrar();
                            } else {
                                Toast.makeText(LoginActivity.this
                                        , "el id no pertenece a ningún usuario"
                                        , Toast.LENGTH_LONG).show();
                                mReferidoView.setError("verifique que el id esté bien escrito");
                                mReferidoView.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progres_referido.dismiss();
                            Toast.makeText(LoginActivity.this,"Ha ocurrido un error intente más tarde"
                                    ,Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    query = database.orderByChild(Constantes.CODIGO_REFERIDO).equalTo(referido);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                Log.i("referido", dataSnapshot.getValue().toString());
                                for (DataSnapshot s:
                                        dataSnapshot.getChildren()) {
                                    referido_por = s.getValue(Usuario.class);
                                    Toast.makeText(getApplicationContext(),"Referido por: "+referido_por.getNombre()
                                            ,Toast.LENGTH_LONG).show();
                                    id_de_quien_refiere = referido_por.getId_usuario();
                                }
                            }else{
                                id_de_quien_refiere =null;
                            }
                            progres_referido.dismiss();
                            if (id_de_quien_refiere!=null) {
                                registrar();
                            } else {
                                Toast.makeText(LoginActivity.this
                                        , "el id no pertenece a ningún usuario"
                                        , Toast.LENGTH_LONG).show();
                                mReferidoView.setError("verifique que el id esté bien escrito");
                                mReferidoView.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progres_referido.dismiss();
                            Toast.makeText(LoginActivity.this,"Ha ocurrido un error intente más tarde"
                                    ,Toast.LENGTH_LONG).show();

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this
                        , "Ha ocurrido un error, intenta más tarde"
                        , Toast.LENGTH_LONG).show();
                mReferidoView.setError("verifique que el id esté bien escrito");
                mReferidoView.requestFocus();
            }

        }else{
            referido_por = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonU:
                es_empresa =false;
                break;
            case R.id.radioButtonE:
                es_empresa = true;
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.recuperar_contrasena:
                final String correo = mEmailView.getText().toString();
                if (correo!=null && isEmailValid(correo)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("¿Enviar correo de recuperación a "+correo + "?")
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.sendPasswordResetEmail(correo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(LoginActivity.this,
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
                                            Toast.makeText(LoginActivity.this,
                                                    "No se encontró ninguna cuanta con ese email," +
                                                            " puedes registrarte o corregir el email",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this,
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
                    Toast.makeText(this,"Escriba una dirección de email ",Toast.LENGTH_LONG).show();
                    mEmailView.setError(getString(R.string.error_invalid_email));
                }
                break;
            case R.id.text_terminos:
                try {
                    Uri uri = Uri.parse("https://www.mensajeroapp.co/home/privacidad");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
    public void ocultarTeclado(EditText e) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        try {
            inputMethodManager.hideSoftInputFromWindow(e.getWindowToken(), 0);
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public String generateCodigoreferido(String name){
        String[] s = name.split(" ");
        String nombre = s[0].toUpperCase();
        String ape = "";
        if(s[1] != null)ape = s[1].substring(0,3).toUpperCase();
        int num = (int)Math.floor(Math.random() * 9999);

        return nombre + ape + num;
    }
}


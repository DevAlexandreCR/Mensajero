package com.mensajero.equipo.mensajero;

import android.Manifest;
import android.app.Dialog;

import androidx.core.math.MathUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.DirectionJSONParser;
import com.mensajero.equipo.mensajero.Constantes.DistanciaEvent;
import com.mensajero.equipo.mensajero.Constantes.Domicilio;
import com.mensajero.equipo.mensajero.Constantes.DownloadTaskDistancia;
import com.mensajero.equipo.mensajero.Constantes.Mensajeros;
import com.mensajero.equipo.mensajero.Constantes.OnCompleteTaskListener;
import com.mensajero.equipo.mensajero.Constantes.Pedidos;
import com.mensajero.equipo.mensajero.Constantes.Usuario;
import com.mensajero.equipo.mensajero.Fragmentos.Ayuda;
import com.mensajero.equipo.mensajero.Fragmentos.Domicilios_en_curso;
import com.mensajero.equipo.mensajero.Fragmentos.Mandados_en_curso;
import com.mensajero.equipo.mensajero.Fragmentos.FragmentChat;
import com.mensajero.equipo.mensajero.Fragmentos.FragmentEmpresas;
import com.mensajero.equipo.mensajero.Fragmentos.FragmentVerEmpresa;
import com.mensajero.equipo.mensajero.Fragmentos.Fragment_Bonos;
import com.mensajero.equipo.mensajero.Fragmentos.Fragment_perfil;
import com.mensajero.equipo.mensajero.Fragmentos.Mis_Solicitudes;
import com.mensajero.equipo.mensajero.Servicios.AddressResultReceiver;
import com.mensajero.equipo.mensajero.Servicios.FetchAddressIntentService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,View.OnFocusChangeListener, OnCompleteTaskListener {

    private static final String CAMBIAR_ORIGEN = "cambiar origen";
    // Crear service's de localizacion
    public DrawerLayout drawer;
    public PolylineOptions lineOptions;
    public EditText direccionInicial, direccionFinal, Ecomentario;
    public TextView Eemailusuario, EnombreUsuario, texto_valor, texto_confirmacion_carrera_activa,
            textoVersion, texto_placa_carrera_activa,cuantos_mensajeros, titulo_cat;
    public FusedLocationProviderClient Proveedor_de_localizacion;
    public AddressResultReceiver addressResultReceiver;
    public Location mLastLocation;
    public GoogleMap map;
    public String DireccionInicial,Direccion_Final, Comentario, nombre, telefono;
    private ReceiverServicioDireccion rcv;
    private FirebaseAuth firebaseAuth;
    ArrayList<LatLng> points;
    private LinearLayout layout_solicitudes, layout_dir_ini,layout_confirmar_carrera,
            layout_spiner, layout_dir_fin, layout_comentario, layout_moto, layout_carro, layout_botones;
    public int idboton;
    private RatingBar ratingMensajero,ratingCuantosMensajeros;
    public LatLng pocisionFinal, pocisionInicial;
    private ProgressDialog progressDialog;
    private Mis_Solicitudes mis_solicitudes;
    private Fragment_Bonos fragment_bonos;
    private Mandados_en_curso mandadosencurso;
    private Domicilios_en_curso domicilios_en_curso;
    private FragmentChat fragmentChat;
    public Spinner spinner;
    public ArrayAdapter spinneradapter;
    public Button solicitar_moto, solcitar_carro,confirmar_carro;
    public AlertDialog alert = null;
    private LocationManager locationManager;
    public Dialog dialog;
    public SimpleDateFormat fecha_pedido;
    public static ProgressDialog progressConfirmar;
    public Calendar calendar;
    public final String TIPO_SOLICITUD = "Elija un tipo de solicitud ";
    public final String SOLICITAR_MOTO = "Mensajero  Moto";
    public final String SOLICITAR_CARRO = "Mensajero  Carro";
    public final String DESTINO = "¿A dónde quieres ir?";
    public String email;
    public LottieAnimationView animationView, buscarDireccionInicial, buscarDireccionFinal;
    private Toolbar toolbar;
    public static int tamanoAppBar;
    private Ayuda ayuda;
    private Fragment_perfil perfil;
    private ImageView img_buscar_location;
    private ConstraintLayout moto, carro, view_inicio ;
    private CircleImageView img_carrera_activa;
    private TextView texto_direcciones,texto_tiempo;
    private FloatingActionButton fabOk, fabCancel, fabChat;
    private final int FOCO_INICIAL = 2;
    private final int FOCO_FINAL = 1;
    private int tienefoco;
    private LottieAnimationView no_internet;
    public double precio;
    private LinearLayout.LayoutParams layoutParams, params_ver, params_seleccionado;
    private TextInputLayout textInputInicial, textInputFinal, textInputComent;
    public ImageView appbarboton;

    //con ésta variable sabremos si el pedido es de carros o de motos
    //variable para saber si el historial es para carros o motos
    public static String Tipo_servicio="";
    public static boolean MENSAJERO_ESPECIAL = false;

    //esta variable es para saber si se requiere cambiar la direccion de pártida en mensajero especial
    public static boolean CAMBIAR_DIRECCION_DE_ORIGEN =false;
    //para la animacion del boton
    public Animation vibrar, aparecer_der_izq, aparecer, aparecer_izq_der, aparecerCompras, aparecerEncomiendas
            , aparecer_abajo_arriba;

    //para animar el layout de direccion final
    public LayoutAnimationController animationController;
    //para autocompletar
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private Dialog dialog_Actualizar;

    //establecer área de popayan en el mapa
    public RectangularBounds Popayan;
    LatLngBounds bounds_ruta;

    // para empezar a habilitar ciudades
    public String ciudad;

    // booleana para ver si se clickó el boton de buscar location
    private boolean buscar_location = false;
    private boolean calcular_tiempo_mensajero = false;

    AppBarLayout appBarLayout;

    public Display display;
    public int ancho, alto, contador;

    //variable para sacar el id del usuario y buscar en la base de datos
    public  String id_user,token;
    public String id_auth;
    public boolean es_empresa = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressBar BuscarMensajeroBar;
    private String IdPedidoEnCurso="";
    private String Tipo = "";
    private ReceptorMensajesServidor receptorMensajesServidor;

    //ESTA VARIABLE ES PARA SABER CUANTAS VECES SE HA ENVIADO EL SERVICIO
    public int VECES_QUE_SE_HA_ENVIADO = 1;

    //instancia database para los conductores conectados
    private DatabaseReference DatabaseConectados,databaseServicio,databaseServicioMoto;
    private Query Query_conectados,queryEstadoPedido,querymotos;
    private ChildEventListener ListenerLocacionConectados = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if (dataSnapshot.getValue()!=null) {

                Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);

                if (mensajero!=null && mensajero.getLat_dir_ini()!=null && mensajero.getLgn_dir_ini()!=null) {
                    //aqui agregamos el marcador de cada mensajero
                    LatLng poscision_mensajero = new LatLng(mensajero.getLat_dir_ini(), mensajero.getLgn_dir_ini());

                    Marker marker = null;
                    if (map != null) {
                        marker = map.addMarker(new MarkerOptions()
                                .position(poscision_mensajero)
                                .title("Mensajero")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.boton_carro)));
                        marker.setTag(mensajero);

                    } else {
                        Log.i("ListenerLocationMensajeros", "map null");
                    }


                    ListaMarcadores.add(marker);
                    Log.i("On Child Aded data ", " " + mensajero.getLat_dir_ini() + mensajero.getLgn_dir_ini());
                    Log.i("On Child Aded data ", " " + dataSnapshot.getValue().toString());
                }
            }

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
            // map.clear();
                if (mensajero!=null && ListaMarcadores.size()>0) {
                    for(int i = 0; i<= ListaMarcadores.size()-1;i++){

                        if (ListaMarcadores.get(i) != null) {
                            Mensajeros mensajeromarcador = (Mensajeros)ListaMarcadores.get(i).getTag();
                            if(mensajeromarcador != null && mensajero.getCodigo().equals(mensajeromarcador.getCodigo())){
                                ListaMarcadores.get(i).setPosition(new LatLng(mensajero.getLat_dir_ini(),mensajero.getLgn_dir_ini()));
                            }
                        }
                    }
                }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
            Mensajeros mensajeroborrar = new Mensajeros();
                if (mensajero!=null && ListaMarcadores.size()>0) {

                    for (int i = 0; i <= ListaMarcadores.size() - 1; i++) {

                        if (ListaMarcadores.get(i) != null) {
                            Mensajeros mensajeromarcador = (Mensajeros) ListaMarcadores.get(i).getTag();
                            if (mensajeromarcador != null && mensajero.getCodigo().equals(mensajeromarcador.getCodigo())) {
                                ListaMarcadores.get(i).remove();
                            }
                        }

                    }
                }

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private ChildEventListener ListenerMotosConectados = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if (dataSnapshot.getValue()!=null) {

                Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);

                if (mensajero!=null && mensajero.getLat_dir_ini()!=null && mensajero.getLgn_dir_ini()!=null) {
                    //aqui agregamos el marcador de cada mensajero
                    LatLng poscision_mensajero = new LatLng(mensajero.getLat_dir_ini(), mensajero.getLgn_dir_ini());
                    Marker marker = null;
                    if (map != null) {
                        marker = map.addMarker(new MarkerOptions()
                                .position(poscision_mensajero)
                                .title("Mensajero")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.boton_moto)));
                        marker.setTag(mensajero);
                    }
                    ListaMarcadores.add(marker);
                    Log.i("On Child Aded data ", " " + mensajero.getLat_dir_ini() + mensajero.getLgn_dir_ini());
                    Log.i("On Child Aded data ", " " + dataSnapshot.getValue().toString());
                }
            }

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
            // map.clear();
            if (mensajero!=null && ListaMarcadores.size()>0) {
                for(int i = 0; i<= ListaMarcadores.size()-1;i++){

                    if (ListaMarcadores.get(i) != null) {
                        Mensajeros mensajeromarcador = (Mensajeros)ListaMarcadores.get(i).getTag();
                        if(mensajeromarcador != null && mensajero.getCodigo().equals(mensajeromarcador.getCodigo())){
                            ListaMarcadores.get(i).setPosition(new LatLng(mensajero.getLat_dir_ini(),mensajero.getLgn_dir_ini()));
                        }
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
            if (mensajero!=null && ListaMarcadores.size()>0) {
                for(int i = 0; i<= ListaMarcadores.size()-1;i++){
                    if (ListaMarcadores.get(i) != null) {
                        Mensajeros mensajeromarcador = (Mensajeros)ListaMarcadores.get(i).getTag();
                        if(mensajeromarcador != null && mensajero.getCodigo().equals(mensajeromarcador.getCodigo())){
                            ListaMarcadores.get(i).remove();
                            ListaMarcadores.remove(i);
                        }
                    }

                }
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //Para crear canal de notificaciones
    private NotificationManager notificationManager;

    //listener para el servicio pedido
    private ValueEventListener listenerestadoServicio = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            final String TAG = "listener pedido";

            try {
                Log.i(TAG,dataSnapshot.getValue().toString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            final Pedidos pedido =  dataSnapshot.getValue(Pedidos.class);
            String estado = Constantes.BD_CANCELADO;
            try{
                estado = pedido.getEstado_pedido();

                if(estado.equals(Constantes.BD_CANCELADO)){
                    BuscarMensajeroBar.setVisibility(View.INVISIBLE);
                    actualizarUI(texto_direcciones, direccionInicial, direccionFinal, Ecomentario, null);
                    layout_solicitudes.setVisibility(View.INVISIBLE);
                    layout_botones.setVisibility(View.INVISIBLE);
                    layout_confirmar_carrera.setVisibility(View.INVISIBLE);
                    ratingMensajero.setVisibility(View.INVISIBLE);
                    texto_placa_carrera_activa.setText(Constantes.BD_MENSAJERO);
                    texto_valor.setTextColor(getResources().getColor(R.color.colorBlanco));
                    texto_tiempo.setText(null);
                    if(fragmentChat.isAdded()){
                        appbarboton.setVisibility(View.VISIBLE);
                        appBarLayout.setVisibility(View.INVISIBLE);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        ft.remove(fragmentChat);
                        ft.commit();
                    }
                    View v = findViewById(R.id.mapa);
                    v.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.MATCH_PARENT));
                    view_inicio.setVisibility(View.VISIBLE);
                    appbarboton.setVisibility(View.VISIBLE);
                    confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
                    confirmar_carro.setEnabled(true);
                    IdPedidoEnCurso = null;
                    fabChat.hide();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                    }
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("El servicio ha sido cancelado");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(true);
                        builder.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map != null) {
                        map.clear();
                        agregarMapa();
                    }
                    queryEstadoPedido.removeEventListener(this);
                }else if(estado.equals(Constantes.BD_EN_CURSO)||estado.equals(Constantes.BD_ESTADO_VIAJE_INICIADO)){
                    layout_confirmar_carrera.setVisibility(View.VISIBLE);
                    View vmapa = findViewById(R.id.mapa);
                    vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                    view_inicio.setVisibility(View.INVISIBLE);
                    BuscarMensajeroBar.setVisibility(View.INVISIBLE);
                    if (estado.equals(Constantes.BD_EN_CURSO)) {
                        confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
                        confirmar_carro.setEnabled(true);
                        fabChat.show();
                        confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
                        String codigo_mensajero = pedido.getCodigo_mensajero();

                        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference()
                                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                .child(Constantes.MENSAJERO_ESPECIAL_CONECTADO);

                        Query query = firebaseDatabase.child(codigo_mensajero);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
                                    LatLng pocision_Mensajero = new LatLng(mensajero.getLat_dir_ini(),
                                            mensajero.getLgn_dir_ini());
                                    String urldistancia = obtenerdistanciaURL(pocision_Mensajero, pocisionInicial);
                                    DownloadTaskDistancia downloadTaskDistancia = new DownloadTaskDistancia();
                                    downloadTaskDistancia.execute(urldistancia);
                                    calcular_tiempo_mensajero = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    } else if(estado.equals(Constantes.BD_ESTADO_VIAJE_INICIADO)){

                        confirmar_carro.setText(Constantes.VIAJE_EN_CURSO);
                        confirmar_carro.setTextColor(Color.WHITE);
                        confirmar_carro.setEnabled(true);
                        fabChat.hide();
                    }
                    String codigo_mensajero = pedido.getCodigo_mensajero();
                    Log.i(TAG,codigo_mensajero+"codigo mensajero");
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                            .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                            .child(Constantes.BD_MENSAJERO_ESPECIAL).child(codigo_mensajero);

                    final Query query1 = data.orderByChild(Constantes.BD_CODIGO);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                Log.i(TAG,dataSnapshot.getValue().toString());
                                Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
                                String nombre = mensajero.getNombre();
                                String placa = mensajero.getPlaca();
                                final String numero = mensajero.getTelefono();
                                String codigo_mensajero = mensajero.getCodigo();

                                String pathFoto ="movil_"+codigo_mensajero;


                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                final StorageReference storageReference = storage.getReference().child("mensajeros")
                                        .child("mensajero_carro").child(pathFoto).child("foto_perfil");

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(MainActivity.this)
                                                .load(storageReference)
                                                .into(img_carrera_activa);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("storage", "nulo");
                                        img_carrera_activa.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                                    }
                                });
                                texto_confirmacion_carrera_activa.setText(nombre);
                                ratingMensajero.setVisibility(View.VISIBLE);
                                ratingMensajero.setRating(mensajero.getCalificacion());
                                texto_placa_carrera_activa.setText(placa);
                                texto_valor.setText(numero);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_llamada, 0);
                                }
                                texto_valor.setTextColor(getResources().getColor(R.color.colorPrimary));
                                texto_valor.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + numero));

                                        int permissionCheck = ContextCompat.checkSelfPermission(
                                                MainActivity.this, Manifest.permission.CALL_PHONE);
                                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                            Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 225);
                                        } else {
                                            Log.i("Mensaje", "Se tiene permiso!");
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                query1.removeEventListener(this);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else if(estado.equals(Constantes.BD_ESTADO_OK)){

                    try {
                        final AlertDialog.Builder alertRating = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.layout_calificar, null);
                        final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
                        alertRating.setView(dialogLayout);
                        final AlertDialog dialogRating = alertRating.create();
                        final ProgressBar progressBarCalificacion = dialogLayout.findViewById(R.id.progressBarCalificacion);
                        final TextView tipocalificacion = dialogLayout.findViewById(R.id.calificacion);
                        final EditText comentarioEdit = dialogLayout.findViewById(R.id.editComentario);
                        final Button enviar = dialogLayout.findViewById(R.id.enviarcalificacion);
                        final TextView textoinformacion = dialogLayout.findViewById(R.id.info);
                        textoinformacion.setText("El valor de tu viaje fué de: $"+pedido.getValor_pedido());
                        enviar.setEnabled(false);
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                pedido.setCalificacion(rating);
                                enviar.setEnabled(true);
                                if(rating<=1){
                                    tipocalificacion.setText("Malo");
                                }else if(rating<=2){
                                    tipocalificacion.setText("Regular");
                                }else if(rating<=3){
                                    tipocalificacion.setText("Bueno");
                                }else if(rating<=4){
                                    tipocalificacion.setText("Muy Bueno");
                                }else if(rating<=5){
                                    tipocalificacion.setText("Excelente");
                                }
                            }
                        });
                        enviar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String comentario = null;
                                progressBarCalificacion.setVisibility(View.VISIBLE);
                                if (comentarioEdit.getText() != null) {
                                     comentario = comentarioEdit.getText().toString();
                                    if(comentario.length()<10){
                                        comentario = null;
                                    }else{
                                        pedido.setComentario(comentario);
                                    }
                                }

                                DatabaseReference dataPedido = FirebaseDatabase.getInstance().getReference()
                                        .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                        .child(Constantes.BD_PEDIDO_ESPECIAL).child(pedido.getId_pedido());
                                dataPedido.setValue(pedido).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBarCalificacion.setVisibility(View.INVISIBLE);
                                        dialogRating.dismiss();

                                    }
                                });

                            }
                        });
                        dialogRating.show();
                        if(dialogRating.getWindow() != null)dialogRating.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                    } catch (Exception e) {
                        e.printStackTrace();
                        reiniciarActivity(MainActivity.this);
                    }
                    fabChat.hide();
                    BuscarMensajeroBar.setVisibility(View.INVISIBLE);
                    actualizarUI(texto_direcciones, direccionInicial, direccionFinal, Ecomentario, null);
                    layout_solicitudes.setVisibility(View.INVISIBLE);
                    layout_botones.setVisibility(View.INVISIBLE);
                    ratingMensajero.setVisibility(View.INVISIBLE);
                    layout_confirmar_carrera.setVisibility(View.INVISIBLE);
                    View v = findViewById(R.id.mapa);
                    v.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.MATCH_PARENT));
                    view_inicio.setVisibility(View.VISIBLE);
                    appbarboton.setVisibility(View.VISIBLE);
                    confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
                    confirmar_carro.setEnabled(true);
                    IdPedidoEnCurso = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                    }
                    try {
                        toast("El servicio ha sido terminado");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map != null) {
                        map.clear();
                        agregarMapa();
                    }

                    queryEstadoPedido.removeEventListener(this);

                }else{
                    fabChat.hide();
                    texto_placa_carrera_activa.setText("Esto puede tardar unos minutos");
                    texto_confirmacion_carrera_activa.setText("Buscando un mensajero...");
                    BuscarMensajeroBar.setVisibility(View.VISIBLE);
                    confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
                    confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }

            }catch (NullPointerException e){
                e.printStackTrace();
                queryEstadoPedido.removeEventListener(this);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener listenerestadoServicioMoto = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            final String TAG = "listener pedido Moto";
            if(dataSnapshot.getValue(Pedidos.class) != null){
                final Pedidos pedido =  dataSnapshot.getValue(Pedidos.class);
                Log.i(TAG,pedido.toString());

                if (pedido.getTipo_pedido() != null) {
                    switch (pedido.getTipo_pedido()){
                        case Constantes.COMPRAS_PEDIR_DOMICILIOS:
                            ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                            cuantos_mensajeros.setVisibility(View.INVISIBLE);
                            switch (pedido.getEstado_pedido()){
                                case Constantes.BD_EN_CURSO:
                                    servicioEnCurso(pedido);
                                    break;
                                case Constantes.ESTADO_MENSAJERO_EN_TIENDA:
                                    servicioIniciado(pedido,Constantes.ESTADO_MENSAJERO_EN_TIENDA);
                                    break;
                                case Constantes.ESTADO_COMPRA_REALIZADA:
                                    servicioIniciado(pedido,Constantes.ESTADO_COMPRA_REALIZADA);
                                    break;
                                case Constantes.BD_ESTADO_OK:
                                    servicioTerminado(pedido);
                                    break;
                                case Constantes.BD_CANCELADO:
                                    servicioCancelado();
                                    break;

                                default:
                                    fabChat.hide();
                                    texto_placa_carrera_activa.setText("Esto puede tardar unos minutos");
                                    texto_confirmacion_carrera_activa.setText("Buscando un mensajero...");
                                    BuscarMensajeroBar.setVisibility(View.VISIBLE);
                                    confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
                                    confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                    }
                                    break;
                            }

                            break;
                        case Constantes.ENCOMIENDAS:
                            ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                            cuantos_mensajeros.setVisibility(View.INVISIBLE);
                            switch (pedido.getEstado_pedido()){
                                case Constantes.BD_EN_CURSO:
                                    servicioEnCurso(pedido);
                                    break;
                                case Constantes.ESTADO_PAQUETE_RECOGIDO:
                                    servicioIniciado(pedido,Constantes.ESTADO_PAQUETE_RECOGIDO);
                                    break;
                                case Constantes.ESTADO_PAQUETE_ENTREGADO:
                                    servicioIniciado(pedido,Constantes.ESTADO_PAQUETE_ENTREGADO);
                                    break;
                                case Constantes.BD_ESTADO_OK:
                                    servicioTerminado(pedido);
                                    break;
                                case Constantes.BD_CANCELADO:
                                    servicioCancelado();
                                    break;
                                default:
                                    fabChat.hide();
                                    texto_placa_carrera_activa.setText("Esto puede tardar unos minutos");
                                    texto_confirmacion_carrera_activa.setText("Buscando un mensajero...");
                                    BuscarMensajeroBar.setVisibility(View.VISIBLE);
                                    confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
                                    confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                    }
                                    break;
                            }
                            break;
                        case Constantes.FACTURAS_TRAMITES:
                            ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                            cuantos_mensajeros.setVisibility(View.INVISIBLE);
                            switch (pedido.getEstado_pedido()){
                                case Constantes.BD_EN_CURSO:
                                    servicioEnCurso(pedido);
                                    break;
                                case Constantes.ESTADO_SERVICIO_INICIADO:
                                    servicioIniciado(pedido, Constantes.ESTADO_SERVICIO_INICIADO);
                                    break;
                                case Constantes.BD_ESTADO_OK:
                                    servicioTerminado(pedido);
                                    break;
                                case Constantes.BD_CANCELADO:
                                    servicioCancelado();
                                    break;
                                default:
                                    fabChat.hide();
                                    texto_placa_carrera_activa.setText("Esto puede tardar unos minutos");
                                    texto_confirmacion_carrera_activa.setText("Buscando un mensajero...");
                                    BuscarMensajeroBar.setVisibility(View.VISIBLE);
                                    confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
                                    confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                    }
                                    break;
                            }
                            break;
                        case Constantes.SOLICITUD_RAPIDA:
                            switch (pedido.getEstado_pedido()){
                                case Constantes.BD_EN_CURSO:
                                    ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                                    cuantos_mensajeros.setVisibility(View.INVISIBLE);
                                    servicioEnCurso(pedido);
                                    break;
                                case Constantes.ESTADO_SERVICIO_INICIADO:
                                    ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                                    cuantos_mensajeros.setVisibility(View.INVISIBLE);
                                    servicioIniciado(pedido,Constantes.ESTADO_SERVICIO_INICIADO);
                                    break;
                                case Constantes.BD_ESTADO_OK:
                                    ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                                    cuantos_mensajeros.setVisibility(View.INVISIBLE);
                                    servicioTerminado(pedido);
                                    break;
                                case Constantes.BD_CANCELADO:
                                    ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                                    cuantos_mensajeros.setVisibility(View.INVISIBLE);
                                    servicioCancelado();
                                    break;
                                default:
                                    ratingCuantosMensajeros.setVisibility(View.VISIBLE);
                                    cuantos_mensajeros.setVisibility(View.VISIBLE);
                                    fabChat.hide();
                                    texto_placa_carrera_activa.setText("Esto puede tardar unos minutos");
                                    texto_confirmacion_carrera_activa.setText("Buscando un mensajero...");
                                    BuscarMensajeroBar.setVisibility(View.VISIBLE);
                                    confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
                                    confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                                    }
                                    break;
                            }
                            break;

                    }
                }

            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    //lista de mensajeros conectados
    private ArrayList<Mensajeros> MensajerosConectados;

    //lista de marcadores del mapa de cada mensajero
    private ArrayList<Marker> ListaMarcadores;

    //aqui guardaremos los datos para restablecer un pedido si está en curso
    public   SharedPreferences preferences;
    private String DireccionEmpresa ;
    NavigationView navigationView;

    //click de las categorias de las empresas
    private View.OnClickListener categoriaSeleccionada = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String categoria = "";
            switch (view.getId()){
                case R.id.chip_comidas:
                    categoria = Constantes.RESTAURANTE;
                    break;
                case R.id.chip_farmacia:
                    categoria = Constantes.FARMACIA;
                    break;
                case R.id.chip_floristeria:
                    categoria = Constantes.FLORISTERIA;
                    break;
                case R.id.chip_licores:
                    categoria = Constantes.LICORES;
                    break;
                case R.id.chip_mercado:
                    categoria = Constantes.SUPERMERCADO;
                    break;
            }
            appbarboton.setVisibility(View.INVISIBLE);
            appBarLayout.setVisibility(View.VISIBLE);
            direccionInicial.setError(null);
            Ecomentario.setError(null);
            direccionFinal.setError(null);
            solicitar_moto.setText(SOLICITAR_MOTO);
            texto_direcciones.setText("");
            texto_direcciones.setVisibility(View.INVISIBLE);
            layout_solicitudes.setVisibility(View.INVISIBLE);
            fabOk.hide();
            fabCancel.hide();
            fragmentEmpresas = new FragmentEmpresas(categoria);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.replace(R.id.main_content, fragmentEmpresas, FragmentEmpresas.TAG);
                    ft.commit();
        }
    };

    private Chip farmacia, restaurante, licores, mercado, floristeria;
    private FragmentEmpresas fragmentEmpresas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // seleccionando unica ciudad disponible
        ciudad = Constantes.POPAYAN;

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            firebaseAuth.getCurrentUser().reload();
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    token = instanceIdResult.getToken();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //inicializar NOtification maganger
        notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CrearCanalesDeNOtificaciones(notificationManager);
        }

        //proegress bar para esperar mientras se busca al mensajero
        BuscarMensajeroBar = findViewById(R.id.buscarmensajero);
        BuscarMensajeroBar.getIndeterminateDrawable()
                .setColorFilter(Color.rgb(43,150,211), PorterDuff.Mode.SRC_IN);
        //texto donde se agrega el valor
        texto_valor = findViewById(R.id.texto_valor);
        texto_tiempo = findViewById(R.id.textTiempo);
        ratingMensajero = findViewById(R.id.ratinMensajero);
        cuantos_mensajeros = findViewById(R.id.cuantosmensajeros);
        ratingCuantosMensajeros = findViewById(R.id.ratingBarcuantos);

        //appbar para ocultar y mostrar
        appBarLayout =  findViewById(R.id.appbar);

        //---------- esto es para poner transparente la barra de estado dependiendo del nivel de android-----------
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 21) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int uiOptions1 = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(uiOptions1);
        }

        //---------------fin-----------------------------------//


        //establecemos el area de popayan para el ltdlngbounds
        Popayan = RectangularBounds.newInstance(
                new LatLng(2.419813, -76.659250),
                new LatLng(2.494589, -76.556597)
        );

        // Initialize Places.
        Places.initialize(getApplicationContext(), Constantes.GOOGLE_API_KEY);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        //definimos la animacion

        vibrar = AnimationUtils.loadAnimation(this, R.anim.vibrar);
        vibrar.setRepeatCount(3);
        vibrar.reset();

        aparecer = AnimationUtils.loadAnimation(this, R.anim.aparecer);
        aparecer.reset();

        aparecer_der_izq = AnimationUtils.loadAnimation(this, R.anim.aparecer_der_izq);
        aparecer_der_izq.reset();


        aparecer_izq_der = AnimationUtils.loadAnimation(this, R.anim.aparecer_izq_der);
        aparecer_izq_der.reset();

        aparecerCompras = AnimationUtils.loadAnimation(this, R.anim.aparecer_compras);
        aparecerCompras.reset();

        aparecerEncomiendas = AnimationUtils.loadAnimation(this, R.anim.aparecer_encomiendas);
        aparecerEncomiendas.reset();
        animationController = new LayoutAnimationController(aparecer);

        aparecer_abajo_arriba = AnimationUtils.loadAnimation(this, R.anim.aparecer_abajo_arriba);
        aparecer_abajo_arriba.reset();

        //inicializamos la animacion lottie
        animationView =  findViewById(R.id.animation_view);
        animationView.loop(true);
        no_internet =  findViewById(R.id.anim_no_internet);
        no_internet.loop(true);

        Eemailusuario =  findViewById(R.id.textView_correo_usuario);
        EnombreUsuario =  findViewById(R.id.text_nombre);
        texto_confirmacion_carrera_activa = findViewById(R.id.texto_confirmacion);
        texto_placa_carrera_activa = findViewById(R.id.placa_carrera_activa);
        //MEDIMOS LA PANTALLA
        display = getWindowManager().getDefaultDisplay();
        ancho = display.getWidth();
        alto = display.getHeight();

        //inicializando los fragmentos
        mis_solicitudes = new Mis_Solicitudes();
        ayuda = new Ayuda();
        fragment_bonos = new Fragment_Bonos();
        perfil = new Fragment_perfil();
        fragmentChat = new FragmentChat();
        mandadosencurso = new Mandados_en_curso();
        fragmentEmpresas = new FragmentEmpresas();
        domicilios_en_curso = new Domicilios_en_curso();

        //inicializamos la imagen y la ocultamos de la vista principal
        img_buscar_location = findViewById(R.id.img_buscar_location);
        moto =  findViewById(R.id.layout_bike);
        carro =  findViewById(R.id.layout_go);
        view_inicio = findViewById(R.id.view_inicio);
        img_carrera_activa = findViewById(R.id.imageView3);
        img_buscar_location.setVisibility(View.INVISIBLE);
        moto.setOnClickListener(this);
        carro.setOnClickListener(this);

        //se definen los layouts y se les da la aninmacion de aparecer
        layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        params_ver = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params_seleccionado = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        layout_spiner =  findViewById(R.id.layout_spiner);
        layout_solicitudes =  findViewById(R.id.layout_solicitudes);
        layout_dir_ini =  findViewById(R.id.layoutdirinicial);
        layout_dir_fin =  findViewById(R.id.layoutdirfinal);
        layout_comentario =  findViewById(R.id.layout_comentario);
        layout_botones =  findViewById(R.id.layout_botones);
        layout_confirmar_carrera = findViewById(R.id.layout_confirmar_carrera);
        layout_comentario.setLayoutParams(layoutParams);
        layout_dir_ini.setLayoutParams(layoutParams);
        layout_dir_fin.setLayoutParams(layoutParams);
        layout_confirmar_carrera.setVisibility(View.INVISIBLE);
        layout_solicitudes.setVisibility(View.INVISIBLE);
        layout_dir_ini.setVisibility(View.INVISIBLE);
        layout_comentario.setVisibility(View.INVISIBLE);
        layout_dir_fin.setVisibility(View.INVISIBLE);
        layout_botones.setVisibility(View.INVISIBLE);


        //layout de los botones
        layout_moto =  findViewById(R.id.layout_moto);
        layout_carro =  findViewById(R.id.layout_carro);


        // boton que parece la appbar
        appbarboton =  findViewById(R.id.appbarboton);
        appbarboton.setOnClickListener(this);
        appbarboton.startAnimation(aparecer_izq_der);


        //comprobar el acceso a internet
        if (!isOnline(this)) {

            AlertaNoInternet();

        }
        //instacia de servicios de localizacion
        Proveedor_de_localizacion = LocationServices.getFusedLocationProviderClient(this);
        //iniciamos procesos de localizacion
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //para tomar el momento extacto donde se solicita el pedido



        //boton de enviar
        solicitar_moto =  findViewById(R.id.solicitar_moto);
        Typeface font = Typeface.createFromAsset(getAssets(), "bariol_bold.otf");
        solicitar_moto.setTypeface(font);
        solicitar_moto.setVisibility(View.VISIBLE);
        solicitar_moto.setText(SOLICITAR_MOTO);

        //boton confirmar pedido especial
        confirmar_carro = (findViewById(R.id.boton_confirmar_especial));
        confirmar_carro.setTypeface(font);
        confirmar_carro.setOnClickListener(this);

        solcitar_carro =  findViewById(R.id.solicitar_carro);
        solcitar_carro.setTypeface(font);
        solcitar_carro.setText(SOLICITAR_CARRO);
        //se crea el spinner para los tipos de pedidos y se
        //le asigna la lista
        spinner = findViewById(R.id.spinner2);
        spinneradapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.tipos_de_pedidos,
                R.layout.spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(spinneradapter);

        // agregando los campos de texto

        textInputInicial =  findViewById(R.id.inputtextinicial);
        textInputFinal = findViewById(R.id.inputtextfinal);
        textInputComent = findViewById(R.id.inputtextcoment);
        direccionInicial =  findViewById(R.id.direccion_inicial);
        direccionInicial.setError(null);
        direccionInicial.setOnFocusChangeListener(this);
        direccionInicial.setOnClickListener(this);
        direccionInicial.requestFocus();
        direccionInicial.setInputType(InputType.TYPE_NULL);
        direccionFinal =  findViewById(R.id.direccion_final);
        direccionFinal.setOnFocusChangeListener(this);
        direccionFinal.setOnClickListener(this);
        direccionFinal.setInputType(InputType.TYPE_NULL);
        Ecomentario =  findViewById(R.id.Ecomentariosolicitar);
        Ecomentario.setError(null);
        Ecomentario.setOnFocusChangeListener(this);
        texto_direcciones =  findViewById(R.id.texto_direccion);
        texto_direcciones.setVisibility(View.INVISIBLE);
        textoVersion = findViewById(R.id.textViewversionApp);

        //los botones de busqueda
        buscarDireccionInicial =  findViewById(R.id.imageButtonBuscarInicial);
        buscarDireccionFinal =  findViewById(R.id.imageButtonBuscarFinal);

        //botones de buscar locacion
        fabOk =  findViewById(R.id.fabOk);
        fabOk.hide();
        fabOk.setOnClickListener(this);
        fabCancel =  findViewById(R.id.fabCancelar);
        fabCancel.hide();
        fabCancel.setOnClickListener(this);
        fabChat = findViewById(R.id.fab_chat);
        fabChat.setOnClickListener(this);
        fabChat.hide();


        //botones a la escucha del evento on click para buscar las direcciones
        buscarDireccionInicial.setOnClickListener(this);
        buscarDireccionFinal.setOnClickListener(this);
        solicitar_moto.setOnClickListener(this);
        solcitar_carro.setOnClickListener(this);

        //iniciando el dialogo de progreso
        progressDialog = new ProgressDialog(this);
        progressConfirmar = new ProgressDialog(this);

        //instancia de autenticacion de firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //inicializar la referencia a la base de datos
        DatabaseConectados = FirebaseDatabase.getInstance().getReference().child(Constantes.BD_GERENTE)
                .child(Constantes.BD_ADMIN).child(Constantes.MENSAJERO_ESPECIAL_CONECTADO);
        Query_conectados = DatabaseConectados.orderByKey();

        querymotos = FirebaseDatabase.getInstance().getReference().child(Constantes.BD_GERENTE)
                .child(Constantes.BD_ADMIN).child(Constantes.MENSAJERO_CONECTADO).orderByKey();


        // este es para la escucha del estado del servicio actualmente solicitado
        //aqui se registran los datos del pedido en la base de datos
        databaseServicio = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                .child(Constantes.BD_PEDIDO_ESPECIAL);

        databaseServicioMoto = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                .child(Constantes.BD_PEDIDO);


        //instancia de clase para buscar las direcciones
        addressResultReceiver = new AddressResultReceiver(new Handler());

        //el navegador lateral
        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // empresas
        farmacia = findViewById(R.id.chip_farmacia);
        restaurante = findViewById(R.id.chip_comidas);
        floristeria = findViewById(R.id.chip_floristeria);
        mercado = findViewById(R.id.chip_mercado);
        licores = findViewById(R.id.chip_licores);
        titulo_cat = findViewById(R.id.titulo_cat);

        farmacia.setOnClickListener(categoriaSeleccionada);
        restaurante.setOnClickListener(categoriaSeleccionada);
        floristeria.setOnClickListener(categoriaSeleccionada);
        mercado.setOnClickListener(categoriaSeleccionada);
        licores.setOnClickListener(categoriaSeleccionada);

        //para el perfil de usuario
        navigationView =  findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        Eemailusuario =  hView.findViewById(R.id.textView_correo_usuario);
        EnombreUsuario =  hView.findViewById(R.id.text_nombre);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_inicio);
        //creando el dialogo confirmacion
        dialog = new Dialog(this, R.style.Theme_AppCompat_Dialog_Alert);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //asignar valores a las variables de nombre y telefono
        datosUsuario();
        // obtenemos la locacion iniial y el mapa
        permisoLocalizacion();

        //para saber si se inicia desde una notificacion
        Bundle bundle = getIntent().getExtras();
        String notificacion = getIntent().getAction();
        if (bundle != null) {
            switch (notificacion){
                case Constantes.MENSAJE_CHAT:
                    String tipo = bundle.getString(Constantes.TIPO_SERVICIO, Constantes.BD_PEDIDO_ESPECIAL);
                    appbarboton.setVisibility(View.INVISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    fragmentChat = new FragmentChat();
                    Bundle bundle1 = new Bundle();
                    IdPedidoEnCurso = preferences.getString(Constantes.BD_ID_PEDIDO,null);
                    bundle1.putString(Constantes.BD_NOMBRE_USUARIO,nombre);
                    bundle1.putString(Constantes.BD_ID_PEDIDO,bundle.getString(Constantes.BD_ID_PEDIDO));
                    bundle1.putString(Constantes.TIPO_SERVICIO,tipo);
                    fragmentChat.setArguments(bundle1);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.replace(R.id.main_content, fragmentChat, FragmentChat.TAG);
                    ft.commit();
                    break;
                case Constantes.REVISAR_HISTORIAL:
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    ft1.replace(R.id.main_content, mis_solicitudes, Mis_Solicitudes.TAG);
                    ft1.commit();
                    break;
                case Constantes.CONFIRMAR_LLEGADA:
                    new AlertDialog.Builder(this).setCancelable(true)
                            .setMessage("tu mensajero ha llegado!")
                            .setTitle("Bip! Bip!")
                            .setNeutralButton("OK", null).create().show();
                    break;
                case Constantes.MENSAJE_CHAT_DOMICILIO:
                    Log.i(Constantes.MENSAJE_CHAT_DOMICILIO, "actionnotifi");
                    String tipop = bundle.getString(Constantes.TIPO_SERVICIO, Constantes.BD_DOMICILIO);
                    appbarboton.setVisibility(View.INVISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    fragmentChat = new FragmentChat();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(Constantes.BD_NOMBRE_USUARIO,nombre);
                    bundle2.putString(Constantes.BD_ID_PEDIDO,bundle.getString(Constantes.BD_ID_PEDIDO));
                    bundle2.putString(Constantes.TIPO_SERVICIO,tipop);
                    fragmentChat.setArguments(bundle2);
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    ft2.replace(R.id.main_content, fragmentChat, FragmentChat.TAG);
                    ft2.commit();
                    break;
            }
        }
        //acciones disponibles para el tipo de pedido
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    spinner.setBackgroundColor(Color.TRANSPARENT);
                }

                DecimalFormat format = new DecimalFormat("#");
                switch (position){
                    case 0:
                        if (!solicitar_moto.getText().toString().equals(SOLICITAR_MOTO)) {
                            solicitar_moto.setText(TIPO_SOLICITUD);
                        }
                        ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                        cuantos_mensajeros.setVisibility(View.INVISIBLE);
                        layout_dir_ini.setLayoutParams(layoutParams);
                        layout_dir_fin.setLayoutParams(layoutParams);
                        layout_comentario.setLayoutParams(layoutParams);
                        layout_dir_ini.setVisibility(View.INVISIBLE);
                        layout_comentario.setVisibility(View.INVISIBLE);
                        layout_dir_fin.setVisibility(View.INVISIBLE);
                        texto_confirmacion_carrera_activa.setText("viaja tranquilo");
                        break;
                    case 1:
                        ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                        cuantos_mensajeros.setVisibility(View.INVISIBLE);
                        aparecerEncomiendas.setInterpolator(new BounceInterpolator());
                        aparecerEncomiendas.setDuration(500);
                        String compras_domicilios = Constantes.COMPRAS_PEDIR_DOMICILIOS;
                        texto_confirmacion_carrera_activa.setText(compras_domicilios.replace("_"," "));
                        if (layout_dir_ini.getVisibility() == View.VISIBLE) {
                            solicitar_moto.startAnimation(vibrar);
                        } else {
                            layout_dir_ini.setVisibility(View.VISIBLE);
                            layout_solicitudes.startAnimation(aparecerEncomiendas);
                            aparecerCompras.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            aparecerEncomiendas.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    solicitar_moto.startAnimation(vibrar);

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });


                        }

                        layout_comentario.setVisibility(View.VISIBLE);


                        //hacer aparecer los layouts
                        layout_dir_ini.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout_dir_ini.setVisibility(View.VISIBLE);
                        layout_comentario.setVisibility(View.VISIBLE);
                        layout_dir_fin.setVisibility(View.VISIBLE);
                        layout_comentario.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout_dir_fin.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        precio = calcularPrecio(0);
                        solicitar_moto.setText("Aproximadamente: $" + format.format(precio) + " Solicitar");

                        textInputFinal.setHint(Constantes.ENTREGAR_EN);
                        direccionInicial.setText("");
                        textInputInicial.setHint(Constantes.COMPRAR_EN);
                        textInputComent.setHint(Constantes.QUE_HACE_EL_MENSAJERO);
                        startIntentService(Constantes.REQUEST_DIRECCION_FINAL);
                        break;
                    case 2:
                        aparecerCompras.setInterpolator(new BounceInterpolator());
                        aparecerCompras.setDuration(500);
                        ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                        cuantos_mensajeros.setVisibility(View.INVISIBLE);
                        texto_direcciones.setVisibility(View.INVISIBLE);
                        String pagos = Constantes.FACTURAS_TRAMITES;
                        texto_confirmacion_carrera_activa.setText(pagos.replace("_"," "));
                        if (layout_dir_ini.getVisibility() == View.VISIBLE) {
                            solicitar_moto.startAnimation(vibrar);
                        } else {
                            layout_dir_ini.setVisibility(View.VISIBLE);
                            layout_solicitudes.startAnimation(aparecerCompras);

                        }

                        aparecerCompras.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                startIntentService(Constantes.REQUEST_DIRECCION_INICIAL);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                solicitar_moto.startAnimation(vibrar);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        layout_comentario.setVisibility(View.VISIBLE);
                        layout_comentario.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout_dir_ini.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout_dir_fin.setLayoutParams(layoutParams);
                        precio = calcularPrecio(0);
                        solicitar_moto.setText("Aproximadamente: $" + format.format(precio) + " Solicitar");
                        textInputInicial.setHint(Constantes.RECOGER_EN);
                        textInputComent.setHint(Constantes.QUE_HACE_EL_MENSAJERO);

                        break;
                    case 3:
                        ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                        cuantos_mensajeros.setVisibility(View.INVISIBLE);
                        aparecerEncomiendas.setInterpolator(new BounceInterpolator());
                        aparecerEncomiendas.setDuration(500);
                        texto_confirmacion_carrera_activa.setText(Constantes.ENCOMIENDAS);
                        aparecerEncomiendas.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                solicitar_moto.startAnimation(vibrar);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        aparecerCompras.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                startIntentService(Constantes.REQUEST_DIRECCION_INICIAL);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        if (layout_dir_ini.getVisibility() == View.VISIBLE) {
                            layout_solicitudes.startAnimation(aparecerEncomiendas);

                        } else {
                            layout_solicitudes.startAnimation(aparecerCompras);
                        }

                        layout_dir_ini.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout_dir_ini.setVisibility(View.VISIBLE);
                        layout_comentario.setVisibility(View.VISIBLE);
                        layout_dir_fin.setVisibility(View.VISIBLE);
                        layout_comentario.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout_dir_fin.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        precio = calcularPrecio(1);
                        solicitar_moto.setText("Aproximadamente: $" + format.format(precio) + " Solicitar");
                        textInputInicial.setHint(Constantes.RECOGER_EN);
                        textInputFinal.setHint(Constantes.ENTREGAR_EN);
                        textInputComent.setHint(Constantes.QUE_HACE_EL_MENSAJERO);
                        direccionFinal.setText("");
                        startIntentService(Constantes.REQUEST_DIRECCION_INICIAL);
                        buscarDireccionFinal.playAnimation();

                        break;
                    case 4:

                        ratingCuantosMensajeros.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                                if(rating<2){
                                    cuantos_mensajeros.setText("Solicitar un mensajero");
                                }else if(rating<3){
                                    cuantos_mensajeros.setText("Solicitar dos mensajeros");
                                }else if(rating<4){
                                    cuantos_mensajeros.setText("Solicitar tres mensajeros");
                                }else if(rating<5){
                                    cuantos_mensajeros.setText("Solicitar cuatro mensajeros");
                                }else if(rating==5){
                                    cuantos_mensajeros.setText("Solicitar cinco mensajeros");
                                }
                            }
                        });
                        texto_confirmacion_carrera_activa.setText(Constantes.TIPO_SERVICIO_SOLICITUD_RAPIDA);
                        DireccionInicial = DireccionEmpresa;
                        direccionInicial.setText(DireccionEmpresa);
                        cuantos_mensajeros.setVisibility(View.VISIBLE);
                        ratingCuantosMensajeros.setVisibility(View.VISIBLE);
                        ratingCuantosMensajeros.setRating(1.0f);
                        map.clear();
                        map.setPadding(10,alto/3,10,alto/4);
                        bounds_ruta = null;

                        //ocultamos el layout de solicitudes
                        aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                        aparecer_der_izq.setDuration(250);
                        aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                layout_solicitudes.setVisibility(View.INVISIBLE);
                                texto_direcciones.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        layout_solicitudes.startAnimation(aparecer_der_izq);

                        //para ocultar el appbar boton
                        aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                        aparecer_izq_der.setDuration(300);
                        aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                appbarboton.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        appbarboton.startAnimation(aparecer_izq_der);

                        //agregamos el precio base
                        DecimalFormat format1 = new DecimalFormat("#");
                        precio = 2000;
                        texto_valor.setText("$" +format1.format(precio)+" +");

                        layout_botones.setVisibility(View.INVISIBLE);
                        layout_confirmar_carrera.setVisibility(View.VISIBLE);
                        confirmar_carro.setTextColor(Color.WHITE);
                        img_carrera_activa.setImageResource(R.drawable.moto);


                        View vmapa = findViewById(R.id.mapa);
                        vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));

                        //direccionInicial.setText(DireccionInicial);
                        // bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();



                        if (pocisionInicial==null) {
                            obtenerlocation();
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLng(pocisionInicial));
                        Marker inicio = map.addMarker(new MarkerOptions()
                                .position(pocisionInicial)
                                .title(nombre)
                                .snippet(DireccionInicial)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_moto)));
                        inicio.showInfoWindow();

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                try{
                                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                            .setCountry("Co")
                                            .setLocationBias(Popayan)
                                            .build(MainActivity.this);
                                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                                } catch (Exception e) {
                                    // TODO: Handle the error.
                                }
                            }
                        });


                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ponemos en pantalla el texto de la version de la app
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);
            String version = "v "+ info.versionName;
            textoVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void animarTitulo(TextView t, Context c){
        Animation animation = AnimationUtils.loadAnimation(c, R.anim.animar_canasta);
        t.startAnimation(animation);
    }

    private void AlertaNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El acceso a internet está desactivado y es necesario para el uso de la aplication. ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });

        alert = builder.create();
        alert.show();

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private void AlertaNoGps() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Es necesario activar el GPS y/o configurarlo en modo de alta precisión " +
                "con Wi-fi y Redes móviles. ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        alert = builder.create();
        alert.show();
    }


    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentByTag(FragmentVerEmpresa.TAG);
        Fragment chat = getSupportFragmentManager().findFragmentByTag(FragmentChat.TAG);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
           drawer.closeDrawer(GravityCompat.START);
        }else if(f != null && f.isAdded()){
            Log.i("onbackpressed", "fragmentverempresa añadido");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(f);
            ft.commit();
        }else if(chat != null && chat.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(chat);
            ft.commit();
            appbarboton.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
            toolbar.setTitle(R.string.app_name);
            if(getSupportFragmentManager().findFragmentByTag(Domicilios_en_curso.TAG) != null
                    && getSupportFragmentManager().findFragmentByTag(Domicilios_en_curso.TAG).isAdded()){
                appbarboton.setVisibility(View.INVISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
                toolbar.setTitle(Domicilios_en_curso.TAG);
            }
            Log.i("onBackpresed","remove chat");
        }else if (domicilios_en_curso.isAdded()) {
            appbarboton.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
            layout_botones.setVisibility(View.INVISIBLE);
            view_inicio.setVisibility(View.VISIBLE);
            Log.i("onBackpresed", "frag domi_en_curso...");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(domicilios_en_curso);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
            //si esta el servicio activo se esconden los botones de moto y carro
            if(layout_confirmar_carrera.getVisibility()==View.VISIBLE){
                Log.i("OnResume()","esconder botones");
                view_inicio.setVisibility(View.INVISIBLE);
            }


        }else if (fragmentEmpresas.isAdded()) {

            appbarboton.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
            layout_botones.setVisibility(View.INVISIBLE);
            view_inicio.setVisibility(View.VISIBLE);
            Log.i("onBackpresed", "frag empresas...");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(fragmentEmpresas);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
            //si esta el servicio activo se esconden los botones de moto y carro
            if(layout_confirmar_carrera.getVisibility()==View.VISIBLE){
                Log.i("OnResume()","esconder botones");
                view_inicio.setVisibility(View.INVISIBLE);
            }


        } else if (mis_solicitudes.isAdded()) {

            appbarboton.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
            layout_botones.setVisibility(View.INVISIBLE);
            view_inicio.setVisibility(View.VISIBLE);
            Log.i("onBackpresed", "mis solicitudes.is...");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(mis_solicitudes);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
            //si esta el servicio activo se esconden los botones de moto y carro
            if(layout_confirmar_carrera.getVisibility()==View.VISIBLE){
                Log.i("OnResume()","esconder botones");
                view_inicio.setVisibility(View.INVISIBLE);
            }

        } else if (mandadosencurso.isAdded()) {

            appbarboton.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
            layout_botones.setVisibility(View.INVISIBLE);
            view_inicio.setVisibility(View.VISIBLE);
            Log.i("onBackpresed", "mis solicitudes.is...");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(mandadosencurso);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
            //si esta el servicio activo se esconden los botones de moto y carro
            if(layout_confirmar_carrera.getVisibility()==View.VISIBLE){
                Log.i("OnResume()","esconder botones");
                view_inicio.setVisibility(View.INVISIBLE);
            }

        }else if (ayuda.isAdded()) {
            appBarLayout.setVisibility(View.INVISIBLE);
            appbarboton.setVisibility(View.VISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(ayuda);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
        }else if (fragment_bonos.isAdded()) {
            appBarLayout.setVisibility(View.INVISIBLE);
            appbarboton.setVisibility(View.VISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(fragment_bonos);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
        } else if (perfil.isAdded()) {
            appBarLayout.setVisibility(View.INVISIBLE);
            appbarboton.setVisibility(View.VISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(perfil);
            ft.commit();
            toolbar.setTitle(R.string.app_name);
        }  else if (dialog.isShowing()) {
            dialog.dismiss();
        } else if (buscar_location) {
            buscar_location = false;
            fabOk.hide();
            fabCancel.hide();
            texto_direcciones.setVisibility(View.INVISIBLE);
            layout_solicitudes.setVisibility(View.VISIBLE);
            layout_botones.setVisibility(View.VISIBLE);
            solicitar_moto.setVisibility(View.VISIBLE);
            solcitar_carro.setVisibility(View.VISIBLE);
            img_buscar_location.setVisibility(View.INVISIBLE);
            appbarboton.setVisibility(View.VISIBLE);
            pocisionFinal = null;
            Direccion_Final = null;

        } else if (layout_solicitudes.getVisibility() == View.VISIBLE) {

                            // para volver a porner los botones de moto y carro
                            solcitar_carro.setText(SOLICITAR_CARRO);
                            solicitar_moto.setText(SOLICITAR_MOTO);
                            layout_botones.setVisibility(View.INVISIBLE);
                            view_inicio.setVisibility(View.VISIBLE);
                            ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                            cuantos_mensajeros.setVisibility(View.INVISIBLE);

                            params_ver.gravity = Gravity.END;
                            params_ver.weight = 1;
                            layout_carro.setLayoutParams(params_ver);
                            //params_ver.leftMargin = 20;
                            params_ver.gravity = Gravity.START;
                            layout_moto.setLayoutParams(params_ver);


                            aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                            aparecer_der_izq.setDuration(250);
                            aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    layout_solicitudes.setVisibility(View.INVISIBLE);
                                    texto_direcciones.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            layout_solicitudes.startAnimation(aparecer_der_izq);

                            fabCancel.hide();
                            fabOk.hide();
                            direccionInicial.setText("");
                            direccionFinal.setText("");
                            layout_dir_fin.setLayoutParams(layoutParams);
                            spinner.setAdapter(spinneradapter);
                            textInputInicial.setHint(Constantes.ENTREGAR_EN);
                            textInputComent.setHint(Constantes.QUE_HACE_EL_MENSAJERO);
                            toolbar.setTitle(R.string.app_name);
                            pocisionInicial=null;
                            pocisionFinal = null;
                            obtenerlocation();


        }else if(layout_confirmar_carrera.getVisibility()==View.VISIBLE) {

            String tex = confirmar_carro.getText().toString();

            if(tex.contains(Constantes.CANCELAR_SOLICITUD)||tex.contains("VIAJE")){
                if(es_empresa){
                    // para volver a porner los botones de moto y carro
                    BuscarMensajeroBar.setVisibility(View.INVISIBLE);
                    layout_botones.setVisibility(View.INVISIBLE);
                    view_inicio.setVisibility(View.VISIBLE);
                    ratingCuantosMensajeros.setVisibility(View.INVISIBLE);
                    cuantos_mensajeros.setVisibility(View.INVISIBLE);

                    params_ver.gravity = Gravity.END;
                    params_ver.weight = 1;
                    layout_carro.setLayoutParams(params_ver);
                    //params_ver.leftMargin = 20;
                    params_ver.gravity = Gravity.START;
                    layout_moto.setLayoutParams(params_ver);


                    aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                    aparecer_der_izq.setDuration(250);
                    aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout_solicitudes.setVisibility(View.INVISIBLE);
                            texto_direcciones.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layout_solicitudes.startAnimation(aparecer_der_izq);

                    fabCancel.hide();
                    fabOk.hide();
                    direccionFinal.setText("");
                    layout_dir_fin.setLayoutParams(layoutParams);
                    spinner.setAdapter(spinneradapter);
                    textInputInicial.setHint(Constantes.ENTREGAR_EN);
                    textInputComent.setHint(Constantes.QUE_HACE_EL_MENSAJERO);
                    toolbar.setTitle(R.string.app_name);
                    pocisionInicial=null;
                    pocisionFinal = null;
                    obtenerlocation();
                    texto_direcciones.setVisibility(View.INVISIBLE);
                    layout_confirmar_carrera.setVisibility(View.INVISIBLE);
                    aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            appbarboton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //hacer el boton carro coupar todo el ancho
                    // layout_botones.setVisibility(View.VISIBLE);
                    layout_dir_ini.setVisibility(View.VISIBLE);
                    direccionInicial.setText(Constantes.ELIJE_UN_DESTINO);
                    solcitar_carro.setText(Constantes.SOLICITUD_RAPIDA);
                    appbarboton.startAnimation(aparecer_izq_der);
                    direccionFinal.setText(null);
                    //volvemos a hacer pantalla completa el mapa
                    View v = findViewById(R.id.mapa);
                    v.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.MATCH_PARENT));
                    map.setPadding(0,0,0,0);

                }else{
                    super.onBackPressed();
                }
            }else{
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_confirmar_carrera.setVisibility(View.INVISIBLE);
                aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        appbarboton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //hacer el boton carro coupar todo el ancho
                // layout_botones.setVisibility(View.VISIBLE);
                layout_dir_ini.setVisibility(View.VISIBLE);
                direccionInicial.setText(Constantes.ELIJE_UN_DESTINO);
                solcitar_carro.setText(Constantes.SOLICITUD_RAPIDA);

                if(spinner.getSelectedItemPosition()==4){
                    solicitar_moto.setText(Constantes.ELIJE_UN_DESTINO);
                }
                layout_solicitudes.setVisibility(View.VISIBLE);
                aparecer_der_izq.setInterpolator(new BounceInterpolator());
                aparecer_der_izq.setDuration(500);
                layout_solicitudes.startAnimation(aparecer_der_izq);
                //para ocultar el appbar boton
                aparecer_izq_der.setInterpolator(new LinearInterpolator());
                aparecer_izq_der.setDuration(500);
                appbarboton.startAnimation(aparecer_izq_der);
                layout_botones.setVisibility(View.VISIBLE);
                obtenerlocation();
                pocisionFinal = null;
                Direccion_Final = null;
                direccionFinal.setText(null);
                //volvemos a hacer pantalla completa el mapa
                View v = findViewById(R.id.mapa);
                v.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT));
                map.setPadding(0,0,0,0);
            }

        }else {

            super.onBackPressed();
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i("onsupperot", "clocik");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_inicio) {
            if (layout_confirmar_carrera.getVisibility() ==View.INVISIBLE) {
                appBarLayout.setVisibility(View.INVISIBLE);
                appbarboton.setVisibility(View.VISIBLE);
                direccionInicial.setError(null);
                Ecomentario.setError(null);
                direccionFinal.setError(null);
                solicitar_moto.setText(SOLICITAR_MOTO);
                texto_direcciones.setText("");
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                toolbar.setTitle(R.string.app_name);
                fabOk.hide();
                fabCancel.hide();
                layout_dir_fin.setLayoutParams(layoutParams);
                spinner.setAdapter(spinneradapter);
                layout_botones.setVisibility(View.INVISIBLE);
                view_inicio.setVisibility(View.VISIBLE);
                agregarMapa();
            }else {
                appBarLayout.setVisibility(View.INVISIBLE);
                onBackPressed();
            }
            if(fragmentChat.isAdded()) {
                appbarboton.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.INVISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(fragmentChat);
                ft.commit();
                toolbar.setTitle(R.string.app_name);
            }else if(fragmentEmpresas.isAdded()) {
                appbarboton.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.INVISIBLE);
                layout_botones.setVisibility(View.INVISIBLE);
                view_inicio.setVisibility(View.VISIBLE);
                Log.i("onBackpresed", "frag empresas...");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(fragmentEmpresas);
                ft.commit();
                toolbar.setTitle(R.string.app_name);
                //si esta el servicio activo se esconden los botones de moto y carro
                if(layout_confirmar_carrera.getVisibility()==View.VISIBLE){
                    Log.i("OnResume()","esconder botones");
                    view_inicio.setVisibility(View.INVISIBLE);
                }
            }else if (mis_solicitudes.isAdded()) {
                Log.i("onBackpresed", "mis solicitudes.is...");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(mis_solicitudes);
                ft.commit();
                if (map != null) {
                    map.clear();
                }
                toolbar.setTitle(R.string.app_name);

            }else if (mandadosencurso.isAdded()) {
                Log.i("onBackpresed", "mis solicitudes.is...");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(mandadosencurso);
                ft.commit();
                if (map != null) {
                    map.clear();
                }
                toolbar.setTitle(R.string.app_name);

            } else if (ayuda.isAdded()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(ayuda);
                ft.commit();
                toolbar.setTitle(R.string.app_name);
            }else if (fragment_bonos.isAdded()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    ft.remove(fragment_bonos);
                    ft.commit();
                    toolbar.setTitle(R.string.app_name);

            }else if (perfil.isAdded()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(perfil);
                ft.commit();
                toolbar.setTitle(R.string.app_name);
            }else if (domicilios_en_curso.isAdded()) {
                Log.i("onBackpresed", "mis solicitudes.is...");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.remove(domicilios_en_curso);
                ft.commit();
                if (map != null) {
                    map.clear();
                }
                toolbar.setTitle(R.string.app_name);

            }

        } else if (id == R.id.nav_mis_domicilios_en_curso) {

            if (isOnline(this)) {
                appBarLayout.setVisibility(View.VISIBLE);
                appbarboton.setVisibility(View.INVISIBLE);
                direccionInicial.setError(null);
                Ecomentario.setError(null);
                direccionFinal.setError(null);
                solicitar_moto.setText(SOLICITAR_MOTO);
                texto_direcciones.setText("");
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                fabOk.hide();
                fabCancel.hide();
                domicilios_en_curso = new Domicilios_en_curso(id_user);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.main_content, domicilios_en_curso, Domicilios_en_curso.TAG);
                ft.commit();
                Log.i(Domicilios_en_curso.TAG, "click");

            } else {
                toast("No hay conexión a internet, intente nuevamente");
            }

        }else if (id == R.id.nav_mis_mandados_en_curso) {

            if (isOnline(this)) {
                appBarLayout.setVisibility(View.VISIBLE);
                appbarboton.setVisibility(View.INVISIBLE);
                direccionInicial.setError(null);
                Ecomentario.setError(null);
                direccionFinal.setError(null);
                solicitar_moto.setText(SOLICITAR_MOTO);
                texto_direcciones.setText("");
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                fabOk.hide();
                fabCancel.hide();
                mandadosencurso = new Mandados_en_curso();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.main_content, mandadosencurso, Mandados_en_curso.TAG);
                ft.commit();
                Log.i(Mandados_en_curso.TAG, "click");

            } else {
                toast("No hay conexión a internet, intente nuevamente");
            }

        }else if (id == R.id.nav_mis_solicitudes) {

            if (isOnline(this)) {
                Tipo_servicio = Constantes.SERVICIO_MENSAJERO;
                appBarLayout.setVisibility(View.VISIBLE);
                appbarboton.setVisibility(View.INVISIBLE);
                direccionInicial.setError(null);
                Ecomentario.setError(null);
                direccionFinal.setError(null);
                solicitar_moto.setText(SOLICITAR_MOTO);
                texto_direcciones.setText("");
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                fabOk.hide();
                fabCancel.hide();
                mis_solicitudes = new Mis_Solicitudes();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.main_content,mis_solicitudes,Mis_Solicitudes.TAG);
                ft.commit();

            } else {
                toast("No hay conexión a internet, intente nuevamente");
            }

        }else if(id ==R.id.nav_mis_solicitudes_carros){
            if (isOnline(this)) {
                Tipo_servicio = Constantes.SERVICIO_MENSAJERO_ESPECIAL;
                appBarLayout.setVisibility(View.VISIBLE);
                appbarboton.setVisibility(View.INVISIBLE);
                direccionInicial.setError(null);
                Ecomentario.setError(null);
                direccionFinal.setError(null);
                solicitar_moto.setText(SOLICITAR_MOTO);
                texto_direcciones.setText("");
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                fabOk.hide();
                fabCancel.hide();
                mis_solicitudes = new Mis_Solicitudes();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.main_content,mis_solicitudes,Mis_Solicitudes.TAG);
                ft.commit();

            } else {
                toast("No hay conexión a internet, intente nuevamente");
            }
        }else if (id == R.id.nav_perfil) {
            appBarLayout.setVisibility(View.VISIBLE);
            appbarboton.setVisibility(View.INVISIBLE);
            direccionInicial.setError(null);
            Ecomentario.setError(null);
            direccionFinal.setError(null);
            solicitar_moto.setText(SOLICITAR_MOTO);
            texto_direcciones.setText("");
            texto_direcciones.setVisibility(View.INVISIBLE);
            layout_solicitudes.setVisibility(View.INVISIBLE);
            fabOk.hide();
            fabCancel.hide();
            Log.i(Fragment_perfil.TAG, "boton perfil");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.main_content, perfil, Fragment_perfil.TAG);
            ft.commit();


        }else if (id == R.id.nav_ayuda) {
            appBarLayout.setVisibility(View.VISIBLE);
            appbarboton.setVisibility(View.INVISIBLE);
            direccionInicial.setError(null);
            Ecomentario.setError(null);
            direccionFinal.setError(null);
            solicitar_moto.setText(SOLICITAR_MOTO);
            texto_direcciones.setText("");
            texto_direcciones.setVisibility(View.INVISIBLE);
            layout_solicitudes.setVisibility(View.INVISIBLE);
            fabOk.hide();
            fabCancel.hide();
            Log.i(Ayuda.TAG, "boton ayuda");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.main_content, ayuda, Ayuda.TAG);
            ft.commit();


        } else if (id == R.id.nav_share) {

            direccionInicial.setError(null);
            Ecomentario.setError(null);
            direccionFinal.setError(null);
            solicitar_moto.setText(SOLICITAR_MOTO);
            texto_direcciones.setText("");
            texto_direcciones.setVisibility(View.INVISIBLE);
            layout_solicitudes.setVisibility(View.INVISIBLE);
            fabOk.hide();
            fabCancel.hide();
            if (map != null) {
                map.clear();
            }
            String id_user = firebaseAuth.getCurrentUser().getUid();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            final String URL_DESCARGA = "http://cort.as/-7d6j";
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola éste es mi código de referido " +
                    "de Mensajero:    "+ id_user + "              descargala aqui...    "+ URL_DESCARGA);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }else if(id ==R.id.nav_mis_bonos){
            if (isOnline(this)) {
                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                    appBarLayout.setVisibility(View.VISIBLE);
                    appbarboton.setVisibility(View.INVISIBLE);
                    direccionInicial.setError(null);
                    Ecomentario.setError(null);
                    direccionFinal.setError(null);
                    solicitar_moto.setText(SOLICITAR_MOTO);
                    texto_direcciones.setText("");
                    texto_direcciones.setVisibility(View.INVISIBLE);
                    layout_solicitudes.setVisibility(View.INVISIBLE);
                    fabOk.hide();
                    fabCancel.hide();
                    fragment_bonos = new Fragment_Bonos();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constantes.BD_ID_USUARIO,id_user);
                    fragment_bonos.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.replace(R.id.main_content,fragment_bonos,Fragment_Bonos.TAG);
                    ft.commit();
                }else{
                    try {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Verifica tu correo y luego vuelve a 'Mis Bonos' ")
                                .setPositiveButton("verificar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialogInterface.dismiss();
                                                        builder.setMessage("Revisa tu correo y haz click en el enlace");
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        toast("intenta más tarde");
                    }
                }
            } else {
                toast("No hay conexión a internet, intente nuevamente");
            }
        } else if (id == R.id.nav_salir) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Cerrar Sesión?")
                    .setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //cerrar la sesion del usuario actual.
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();


        }


        drawer.closeDrawer(GravityCompat.START);


        return true;

    }

    protected void startIntentService(int requestCode) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constantes.RECEIVER, addressResultReceiver);
        intent.putExtra(Constantes.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(Constantes.REQUESTCODE, requestCode);
        startService(intent);
        texto_direcciones.setText(Constantes.BUSCANDO_DIRECCION);
    }

    @Override
    public void onClick(View v) {
        idboton = v.getId();

        switch (idboton) {
            case R.id.imageButtonBuscarInicial:
                ocultarTeclado(direccionInicial);
                if (map != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(pocisionInicial, 17));
                    map.setPadding(0, 0, 0, 0);
                }
                img_buscar_location.setVisibility(View.VISIBLE);
                texto_direcciones.setVisibility(View.VISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                solicitar_moto.setVisibility(View.INVISIBLE);
                solcitar_carro.setVisibility(View.INVISIBLE);
                appbarboton.setVisibility(View.INVISIBLE);
                fabOk.show();
                fabCancel.show();
                tienefoco = FOCO_INICIAL;
                buscar_location = true;

                break;
            case R.id.imageButtonBuscarFinal:
                if (map != null && pocisionInicial != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(pocisionInicial, 17));
                    map.setPadding(0, 0, 0, 0);
                }
                img_buscar_location.setVisibility(View.VISIBLE);
                texto_direcciones.setVisibility(View.VISIBLE);
                layout_solicitudes.setVisibility(View.INVISIBLE);
                solicitar_moto.setVisibility(View.INVISIBLE);
                solcitar_carro.setVisibility(View.INVISIBLE);
                fabOk.show();
                fabCancel.show();
                tienefoco = FOCO_FINAL;
                buscar_location = true;
                appbarboton.setVisibility(View.INVISIBLE);
                break;
            case R.id.fab_chat:
                appBarLayout.setVisibility(View.VISIBLE);
                appbarboton.setVisibility(View.INVISIBLE);
                fragmentChat = new FragmentChat();
                Bundle bundle = new Bundle();
                bundle.putString(Constantes.BD_NOMBRE_USUARIO,nombre);
                bundle.putString(Constantes.BD_ID_PEDIDO,IdPedidoEnCurso);
                bundle.putString(Constantes.TIPO_SERVICIO, Constantes.BD_PEDIDO_ESPECIAL);
                fragmentChat.setArguments(bundle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.main_content, fragmentChat, FragmentChat.TAG);
                ft.commit();
            break;
            case R.id.fabOk:
                fabCancel.hide();
                fabOk.hide();
                appbarboton.setVisibility(View.VISIBLE);
                buscar_location = false;
                if (map != null) {
                    map.clear();
                }
                layout_botones.setVisibility(View.VISIBLE);
                img_buscar_location.setVisibility(View.INVISIBLE);
                //para ocultar el appbar boton

                texto_direcciones.setVisibility(View.INVISIBLE);
                solicitar_moto.setVisibility(View.VISIBLE);
                solcitar_carro.setVisibility(View.VISIBLE);
                if (tienefoco == FOCO_INICIAL) {
                            //limpiamos el mapa para agregar los nuevos marcadores y la ruta
                            map.clear();
                            map.setPadding(10,alto/3,10,alto/4);
                    layout_solicitudes.setVisibility(View.VISIBLE);
                            bounds_ruta = null;
                            if(Tipo.equals("carro")){
                                img_carrera_activa.setImageResource(R.drawable.carro);
                                map.addMarker(new MarkerOptions()
                                        .position(pocisionInicial)
                                        .title(CAMBIAR_ORIGEN)
                                        .snippet(DireccionInicial)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));
                            }else{
                                img_carrera_activa.setImageResource(R.drawable.moto);
                                map.addMarker(new MarkerOptions()
                                        .position(pocisionInicial)
                                        .title(CAMBIAR_ORIGEN)
                                        .snippet(DireccionInicial)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_moto)));
                            }


                            if (pocisionFinal != null) {
                                if(MENSAJERO_ESPECIAL){
                                    View vmapa = findViewById(R.id.mapa);
                                    vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                                    bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();
                                    layout_solicitudes.setVisibility(View.INVISIBLE);
                                    appbarboton.setVisibility(View.INVISIBLE);
                                    img_carrera_activa.setImageResource(R.drawable.carro);
                                    texto_direcciones.setVisibility(View.INVISIBLE);
                                    map.addMarker(new MarkerOptions()
                                            .position(pocisionInicial)
                                            .title(CAMBIAR_ORIGEN)
                                            .snippet(DireccionInicial)
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));

                                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 200));
                                    Marker fin = map.addMarker(new MarkerOptions()
                                            .position(pocisionFinal)
                                            .title("Déjame Aquí")
                                            .snippet(Direccion_Final)
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));
                                    fin.showInfoWindow();
                                    ruta();
                                    texto_direcciones.setVisibility(View.INVISIBLE);
                                    layout_botones.setVisibility(View.INVISIBLE);
                                    layout_confirmar_carrera.setVisibility(View.VISIBLE);
                                    confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
                                    confirmar_carro.setEnabled(true);

                                }else{
                                    bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();
                                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 100));
                                    Marker fin = map.addMarker(new MarkerOptions()
                                            .position(pocisionFinal)
                                            .snippet(Direccion_Final)
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));
                                    fin.showInfoWindow();
                                    ruta();
                                }

                            }

                            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    CAMBIAR_DIRECCION_DE_ORIGEN = true;
                                    try{
                                        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                                .setCountry("Co")
                                                .setLocationBias(Popayan)
                                                .build(MainActivity.this);
                                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                                    } catch (Exception e) {
                                        // TODO: Handle the error.
                                    }

                                }
                            });



                } else if (tienefoco == FOCO_FINAL) {
                    if (pocisionFinal != null) {
                    if(MENSAJERO_ESPECIAL){
                        View vmapa = findViewById(R.id.mapa);
                        vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                        bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();
                        img_carrera_activa.setImageResource(R.drawable.carro);
                        texto_direcciones.setVisibility(View.INVISIBLE);
                        Marker fin = map.addMarker(new MarkerOptions()
                                .position(pocisionFinal)
                                .title("Déjame Aquí")
                                .snippet(Direccion_Final)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));
                        fin.showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 200));
                        ruta();
                        layout_botones.setVisibility(View.INVISIBLE);
                        layout_confirmar_carrera.setVisibility(View.VISIBLE);
                        layout_solicitudes.setVisibility(View.INVISIBLE);
                        appbarboton.setVisibility(View.INVISIBLE);
                    }else{
                        direccionFinal.setText(Direccion_Final);
                        //map.setPadding(20, alto/2+100, 20, 100);
                        //  map.setMinZoomPreference(13.0f);

                            map.addMarker(new MarkerOptions()
                                    .position(pocisionFinal)
                                    .title("Entregar Aquí")
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));

                            bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();
                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 100));


                            //agregamos el poliline al mapa
                            ruta();





                        Ecomentario.requestFocus();

                        map.addMarker(new MarkerOptions()
                                .position(pocisionInicial)
                                .title("Recoger Aquí")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_moto)));
                        layout_solicitudes.setVisibility(View.VISIBLE);
                        img_buscar_location.setVisibility(View.INVISIBLE);
                        solicitar_moto.setVisibility(View.VISIBLE);
                        solcitar_carro.setVisibility(View.VISIBLE);
                    }
                    } else {
                        toast("Debe seleccionar un ubicación direfente a la inicial");
                    }

                }



                break;
            case R.id.fabCancelar:
                appbarboton.setVisibility(View.VISIBLE);
                buscar_location = false;
                pocisionFinal = null;
                Direccion_Final = null;
                fabOk.hide();
                fabCancel.hide();
                texto_direcciones.setVisibility(View.INVISIBLE);
                layout_solicitudes.setVisibility(View.VISIBLE);
                solicitar_moto.setVisibility(View.VISIBLE);
                solcitar_carro.setVisibility(View.VISIBLE);
                img_buscar_location.setVisibility(View.INVISIBLE);
                break;
            case R.id.solicitar_moto:
                View foco = null;
                    switch (spinner.getSelectedItemPosition()){

                        case 0:
                            toast("Debes seleccionar un tipo de solicitud");
                            spinner.setBackgroundColor(Color.rgb(252, 212, 84));

                            break;
                        case 1:
                            //verificamos si ha escrito un comentario y si la direccion de entrega esta escrita
                            if(Ecomentario.getText().toString().length() < 30){
                                Ecomentario.setError("¿Qué debe hacer el mensajero?");
                                foco = Ecomentario;
                                foco.requestFocus();
                            }else if(direccionFinal.getText().toString().length() < 3) {
                                direccionFinal.setError("Debe agregar una dirección de Entrega");
                                direccionFinal.requestFocus();
                            }else{
                                Comentario = Ecomentario.getText().toString();
                                map.setPadding(10,alto/3,10,alto/4);

                                bounds_ruta = null;

                                //ocultamos el layout de solicitudes
                                aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                                aparecer_der_izq.setDuration(250);
                                aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        layout_solicitudes.setVisibility(View.INVISIBLE);
                                        texto_direcciones.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                layout_solicitudes.startAnimation(aparecer_der_izq);

                                //para ocultar el appbar boton
                                aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                                aparecer_izq_der.setDuration(300);
                                aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        appbarboton.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                appbarboton.startAnimation(aparecer_izq_der);

                                //agregamos el precio base
                                DecimalFormat format = new DecimalFormat("#");
                                texto_valor.setText("$" +format.format(precio));

                                layout_botones.setVisibility(View.INVISIBLE);
                                layout_confirmar_carrera.setVisibility(View.VISIBLE);
                                confirmar_carro.setTextColor(Color.WHITE);
                                img_carrera_activa.setImageResource(R.drawable.moto);

                                View vmapa = findViewById(R.id.mapa);
                                vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                            }
                            break;
                        case 2:
                            //verificamos si ha escrito un comentario y si la direccion de recogida esta escrita
                            if(Ecomentario.getText().toString().length() < 30){
                                Ecomentario.setError("¿Qué debe hacer el mensajero?");
                                foco = Ecomentario;
                                foco.requestFocus();
                            }else if(direccionInicial.getText().toString().length() < 3) {
                                direccionFinal.setError("Debe agregar una dirección de Recepcion");
                                direccionFinal.requestFocus();
                            }else {
                                Comentario = Ecomentario.getText().toString();
                                map.setPadding(10, alto / 3, 10, alto / 4);

                                bounds_ruta = null;

                                //ocultamos el layout de solicitudes
                                aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                                aparecer_der_izq.setDuration(250);
                                aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        layout_solicitudes.setVisibility(View.INVISIBLE);
                                        texto_direcciones.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                layout_solicitudes.startAnimation(aparecer_der_izq);

                                //para ocultar el appbar boton
                                aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                                aparecer_izq_der.setDuration(300);
                                aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        appbarboton.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                appbarboton.startAnimation(aparecer_izq_der);

                                //agregamos el precio base
                                DecimalFormat format = new DecimalFormat("#");
                                texto_valor.setText("$" + format.format(precio));

                                layout_botones.setVisibility(View.INVISIBLE);
                                layout_confirmar_carrera.setVisibility(View.VISIBLE);
                                confirmar_carro.setTextColor(Color.WHITE);
                                img_carrera_activa.setImageResource(R.drawable.moto);

                                View vmapa = findViewById(R.id.mapa);
                                vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                            }
                            break;
                        case 3:
                            if (direccionFinal.getText().length() < 1) {

                                direccionFinal.setError("Debe agregar una dirección de Entrega");
                                direccionFinal.requestFocus();

                            } else if (Ecomentario.getText().length() < 30) {
                                Ecomentario.setError("¿Qué debe hacer el mensajero?");
                                foco = Ecomentario;
                                foco.requestFocus();
                            } else if (direccionInicial.getText().length() < 1) {
                                direccionInicial.setError("agregue una direccion");
                                foco = direccionInicial;
                                foco.requestFocus();

                            } else {

                                Comentario = Ecomentario.getText().toString();
                                map.setPadding(10,alto/3,10,alto/4);

                                bounds_ruta = null;

                                //ocultamos el layout de solicitudes
                                aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                                aparecer_der_izq.setDuration(250);
                                aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        layout_solicitudes.setVisibility(View.INVISIBLE);
                                        texto_direcciones.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                layout_solicitudes.startAnimation(aparecer_der_izq);

                                //para ocultar el appbar boton
                                aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                                aparecer_izq_der.setDuration(300);
                                aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        appbarboton.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                appbarboton.startAnimation(aparecer_izq_der);

                                //agregamos el precio base
                                DecimalFormat format = new DecimalFormat("#");
                                texto_valor.setText("$" +format.format(precio));

                                layout_botones.setVisibility(View.INVISIBLE);
                                layout_confirmar_carrera.setVisibility(View.VISIBLE);
                                confirmar_carro.setTextColor(Color.WHITE);
                                img_carrera_activa.setImageResource(R.drawable.moto);

                                View vmapa = findViewById(R.id.mapa);
                                vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                            }

                            break;
                        case 4:
                            map.clear();
                            map.setPadding(10,alto/3,10,alto/4);
                            bounds_ruta = null;

                            //ocultamos el layout de solicitudes
                            aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                            aparecer_der_izq.setDuration(250);
                            aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    layout_solicitudes.setVisibility(View.INVISIBLE);
                                    texto_direcciones.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            layout_solicitudes.startAnimation(aparecer_der_izq);

                            //para ocultar el appbar boton
                            aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                            aparecer_izq_der.setDuration(300);
                            aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    appbarboton.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            appbarboton.startAnimation(aparecer_izq_der);

                            //agregamos el precio base
                            DecimalFormat format = new DecimalFormat("#");
                            precio = 2000;
                            texto_valor.setText("$" +format.format(precio)+" +");

                            layout_botones.setVisibility(View.INVISIBLE);
                            layout_confirmar_carrera.setVisibility(View.VISIBLE);
                            confirmar_carro.setTextColor(Color.WHITE);
                            img_carrera_activa.setImageResource(R.drawable.moto);


                            View vmapa = findViewById(R.id.mapa);
                            vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));

                            //direccionInicial.setText(DireccionInicial);
                            // bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();



                            if (pocisionInicial==null) {
                                obtenerlocation();
                            }
                            map.animateCamera(CameraUpdateFactory.newLatLng(pocisionInicial));
                            String direccion_inicial = "click para cambiar";
                            Marker inicio = map.addMarker(new MarkerOptions()
                                    .position(pocisionInicial)
                                    .title("Agregar Destino")
                                    .snippet(DireccionInicial)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));
                            inicio.showInfoWindow();

                            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    try{
                                        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                                .setCountry("Co")
                                                .setLocationBias(Popayan)
                                                .build(MainActivity.this);
                                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                                    } catch (Exception e) {
                                        // TODO: Handle the error.
                                    }
                                }
                            });

                            break;
                    }

                break;
            case R.id.layout_bike:


                Tipo = "moto";

                MENSAJERO_ESPECIAL=false;
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                if (hourOfDay >= 7 && hourOfDay < 23) {
                    // layout_carro.setLayoutParams(params_ocultar);
                    view_inicio.setVisibility(View.INVISIBLE);
                    layout_botones.setVisibility(View.VISIBLE);
                    layout_moto.setLayoutParams(params_seleccionado);
                    layout_spiner.setLayoutParams(params_seleccionado);
                    confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
                    texto_placa_carrera_activa.setText(Constantes.BD_MENSAJERO);
                    confirmar_carro.setTextColor(Color.WHITE);

                    foco = direccionInicial;
                    foco.requestFocus();
                    layout_solicitudes.setVisibility(View.VISIBLE);

                    aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    spinner.setSelection(0,true);
                    aparecer_der_izq.setInterpolator(new BounceInterpolator());
                    aparecer_der_izq.setDuration(500);
                    layout_solicitudes.startAnimation(aparecer_der_izq);
                    solicitar_moto.setText(TIPO_SOLICITUD);
                    tienefoco = FOCO_INICIAL;
                    buscarDireccionInicial.playAnimation();
                } else {
                    String mensaje = "El horario  " +
                            "de atención en moto es de 7:00 am hasta las 10:30pm... No importa puedes " +
                            "solicitar tu mensajero en carro";
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                }
                break;

               // toast("Muy Pronto...");
               // break;
            case R.id.layout_go:
                Tipo = "carro";
                contador=0;
                pocisionInicial=null;
                MENSAJERO_ESPECIAL=true;
                obtenerlocation();
                texto_confirmacion_carrera_activa.setText("viaja tranquilo");
                layout_spiner.setLayoutParams(layoutParams);
                aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                aparecerEncomiendas.setInterpolator(new BounceInterpolator());
                aparecerEncomiendas.setDuration(500);
                aparecerEncomiendas.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                aparecerCompras.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        startIntentService(Constantes.REQUEST_DIRECCION_INICIAL);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if (layout_dir_ini.getVisibility() == View.VISIBLE) {
                    layout_solicitudes.startAnimation(aparecerEncomiendas);

                } else {
                    layout_solicitudes.startAnimation(aparecerCompras);
                }

                layout_dir_ini.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout_dir_ini.setVisibility(View.VISIBLE);
                layout_comentario.setVisibility(View.VISIBLE);
                layout_dir_fin.setVisibility(View.VISIBLE);
                layout_comentario.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout_dir_fin.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                //hacer el boton carro coupar todo el ancho
                layout_carro.setLayoutParams(params_seleccionado);
                //desaparecemos los botones de imagen moto y carro
                view_inicio.setVisibility(View.INVISIBLE);

                textInputInicial.setHint(Constantes.RECOGEME_AQUI);
                textInputFinal.setHint(Constantes.ELIJE_UN_DESTINO);
                textInputComent.setHint(Constantes.COMENTARIO_VIAJE);
                layout_botones.setVisibility(View.VISIBLE);
                solcitar_carro.setText(Constantes.SOLICITUD_RAPIDA);
                layout_dir_ini.setVisibility(View.VISIBLE);
                layout_dir_ini.setLayoutParams(params_seleccionado);
                layout_solicitudes.setVisibility(View.VISIBLE);
                aparecer_der_izq.setInterpolator(new BounceInterpolator());
                aparecer_der_izq.setDuration(500);
                layout_solicitudes.startAnimation(aparecer_der_izq);
                confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
                texto_placa_carrera_activa.setText(Constantes.BD_MENSAJERO);
                confirmar_carro.setTextColor(Color.WHITE);
                break;
            case R.id.solicitar_carro:

                //cambiamos el texto del boton a la espera del ingreso de la direccion de destino

                if (solcitar_carro.getText().toString().equals(Constantes.SOLICITUD_RAPIDA)) {
                    try {
                        map.clear();
                        map.setPadding(10,alto/3,10,alto/4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bounds_ruta = null;

                        //ocultamos el layout de solicitudes
                        aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                        aparecer_der_izq.setDuration(250);
                        aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                layout_solicitudes.setVisibility(View.INVISIBLE);
                                texto_direcciones.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        layout_solicitudes.startAnimation(aparecer_der_izq);

                        //para ocultar el appbar boton
                        aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                        aparecer_izq_der.setDuration(300);
                        aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                appbarboton.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        appbarboton.startAnimation(aparecer_izq_der);

                        //agregamos el precio base
                        DecimalFormat format = new DecimalFormat("#");
                        precio = 3500;
                        texto_valor.setText("$ " +format.format(precio)+" +");

                        layout_botones.setVisibility(View.INVISIBLE);
                        layout_confirmar_carrera.setVisibility(View.VISIBLE);
                        confirmar_carro.setTextColor(Color.WHITE);
                        img_carrera_activa.setImageResource(R.drawable.carro);

                        View vmapa = findViewById(R.id.mapa);
                        vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));

                        //direccionInicial.setText(DireccionInicial);
                       // bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();

                        if (pocisionInicial == null) {
                            obtenerlocation();
                        }
                        if (pocisionInicial!=null) {
                            map.animateCamera(CameraUpdateFactory.newLatLng(pocisionInicial));
                        }
                    try {
                        if (pocisionInicial!=null) {
                            Marker inicio = map.addMarker(new MarkerOptions()
                                    .position(pocisionInicial)
                                    .title("Agregar Destino")
                                    .snippet(DireccionInicial)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));
                            inicio.showInfoWindow();
                        } else {
                            obtenerlocation();
                        }
                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                try{
                                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                            .setCountry("Co")
                                            .setLocationBias(Popayan)
                                            .build(MainActivity.this);
                                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                                } catch (Exception e) {
                                    // TODO: Handle the error.
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }


                }

                break;
            case R.id.direccion_inicial:
                tienefoco = FOCO_INICIAL;
                try{
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                            .setCountry("Co")
                            .setLocationBias(Popayan)
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (Exception e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.direccion_final:
                tienefoco = FOCO_FINAL;
                try{
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                            .setCountry("Co")
                            .setLocationBias(Popayan)
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (Exception e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.boton_confirmar_especial:

                if(Tipo.equals("carro")){
                    if(confirmar_carro.getText().equals(Constantes.CONFIRMAR_SOLICITUD)){
                        if (isOnline(this)) {
                            enviarpedido_especial();
                        }else{
                            toast("Verifique su conexión a Internet");
                        }
                    }else if(confirmar_carro.getText().equals(Constantes.VIAJE_EN_CURSO)) {
                        toast("viaje en curso");
                    }else {
                        try {
                            AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("¿Desea cancelar el servicio?")
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                                            .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_PEDIDO_ESPECIAL);
                                    if (IdPedidoEnCurso!=null) {
                                        data.child(IdPedidoEnCurso).child(Constantes.BD_ESTADO_PEDIDO).setValue(Constantes.BD_CANCELADO)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                                        IdPedidoEnCurso =null;
                                    }else{
                                        reiniciarActivity(MainActivity.this);
                                    }
                                }
                            }).setCancelable(false).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("click","cancelar");
                        }
                    }
                }else if(Tipo.equals("moto")){
                    if(confirmar_carro.getText().equals(Constantes.CONFIRMAR_SOLICITUD)){
                        enviarpedido();
                    }else if(confirmar_carro.getText().toString().equals(Constantes.CANCELAR_SOLICITUD)){
                            try {
                                AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("¿Desea cancelar el servicio?")
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                                                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_PEDIDO);
                                        if (IdPedidoEnCurso!=null) {
                                            data.child(IdPedidoEnCurso).child(Constantes.BD_ESTADO_PEDIDO).setValue(Constantes.BD_CANCELADO)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                            IdPedidoEnCurso =null;
                                        }else{
                                            reiniciarActivity(MainActivity.this);
                                        }
                                    }
                                }).setCancelable(false).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("click","cancelar");
                            }
                        }else{
                        toast("El servicio ya está en curso");
                    }
                }
                break;
            case R.id.appbarboton:
                drawer.openDrawer(GravityCompat.START, true);
                Log.d("boton appbar", "click");
                break;
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {

        int id = view.getId();
        switch (id) {
            case R.id.direccion_inicial:
                if (view.hasFocus()) {
                    tienefoco = FOCO_INICIAL;
                }
                break;
            case R.id.direccion_final:
                if (view.hasFocus()) {
                    tienefoco = FOCO_FINAL;
                }
                break;
            case R.id.Ecomentariosolicitar:
                tienefoco = 0;
                break;
            default:
                tienefoco = 0;
                break;
        }

    }

    @Override
    public void onCompleteTask(String distanciaString, int distanciaNum, String tiempoString, int tiempoNum) {
        if(calcular_tiempo_mensajero){
            texto_tiempo.setText("Llegará en "+ tiempoNum/60 + " minutos aproximadamente");
        } else if (solicitar_moto.getText().toString().equals(Constantes.SOLICITUD_RAPIDA) ||solcitar_carro.getText().toString().equals(Constantes.SOLICITUD_RAPIDA) || solcitar_carro.getText().toString().contains("Aceptar")) {

            precio = calcular_precio_carrera(tiempoNum, distanciaNum);
            DecimalFormat format = new DecimalFormat("#");
            String titulodatos = distanciaString + " " + tiempoString;
            texto_direcciones.setVisibility(View.VISIBLE);
            texto_direcciones.setText(titulodatos);

            solcitar_carro.setText("$" + format.format(precio - 1000) + " - $" + format.format(precio) + " Click para Aceptar");
            texto_valor.setText("$" + format.format(precio - 1000) + " - $" + format.format(precio));
            solcitar_carro.setAnimation(vibrar);
            solicitar_moto.setAnimation(vibrar);

        } else {
            precio = calcularPrecio(distanciaNum);
            DecimalFormat format = new DecimalFormat("#");
            String titulodatos = distanciaString + " " + tiempoString;
            texto_direcciones.setText(titulodatos);
            texto_direcciones.setVisibility(View.VISIBLE);
            solicitar_moto.setText("Aproximadamente: $" + format.format(precio) + " Solicitar");
            solicitar_moto.setAnimation(vibrar);

        }
    }


    private class ReceiverServicioDireccion extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            switch (action){

                case Constantes.ACTION_FIN:
                    progressDialog.dismiss();

                    int requestCode = intent.getIntExtra(Constantes.REQUESTCODE,0);
                    switch (requestCode){

                        case Constantes.REQUEST_DIRECCION_INICIAL:

                            DireccionInicial = AddressResultReceiver.mAddressOutput.replace(
                                    ", Popayán, Cauca, Colombia", ""
                            );

                            direccionInicial.setText(DireccionInicial);

                            break;
                        case Constantes.REQUEST_DIRECCION_FINAL:
                            Direccion_Final =AddressResultReceiver.mAddressOutput.replace(
                                    ", Popayán, Cauca, Colombia", ""
                            );
                            direccionFinal.setText(Direccion_Final);
                            pocisionFinal = new LatLng(intent.getDoubleExtra(Constantes.BD_LAT_DIR_INICIAL,0),intent.getDoubleExtra(Constantes.BD_LONG_DIR_INICIAL,0));
                            break;
                        default:

                            break;
                    }
                    texto_direcciones.setText(AddressResultReceiver.mAddressOutput.replace(
                            ", Popayán, Cauca, Colombia", ""
                    ));

                    Log.i("receiver inicial", "direccion inicial" + DireccionInicial);
                    break;
                case Constantes.ACTION_FIN_ERROR:

                    progressDialog.dismiss();
                    direccionInicial.setText("");
                    texto_direcciones.setText("Sin conexión a Internet...");
                    fabOk.hide();
                    img_buscar_location.setVisibility(View.INVISIBLE);

                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
        agregarMapa();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constantes.ACTION_PROGRESO);
        filter.addAction(Constantes.ACTION_FIN);
        filter.addAction(Constantes.ACTION_FIN_ERROR);
        rcv = new ReceiverServicioDireccion();

        //verificar si hay algún pedido en curso para restablecer la pantalla con datos del pedido

        IdPedidoEnCurso = preferences.getString(Constantes.BD_ID_PEDIDO,null);
        Tipo = preferences.getString(Constantes.TIPO_SERVICIO,"");

        if(IdPedidoEnCurso!=null){
            Log.i("OnStart()","IdPedido no null");
            queryEstadoPedido = databaseServicio.child(IdPedidoEnCurso).orderByChild(Constantes.BD_ESTADO_PEDIDO);
            queryEstadoPedido.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        queryEstadoPedido.addValueEventListener(listenerestadoServicio);
                        Log.i("OnStart()","IdPedido carro");
                    }else{
                        if (IdPedidoEnCurso != null) {
                            queryEstadoPedido = databaseServicioMoto.child(IdPedidoEnCurso).orderByChild(Constantes.BD_ESTADO_PEDIDO);
                            queryEstadoPedido.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        queryEstadoPedido.addValueEventListener(listenerestadoServicioMoto);
                                        Log.i("OnStart()","IdPedido moto");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Log.i("OnStart()","IdPedido null");
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.BD_ID_PEDIDO,IdPedidoEnCurso);
        editor.putString(Constantes.TIPO_SERVICIO,Tipo);
        editor.apply();

        if (queryEstadoPedido!=null) {
            queryEstadoPedido.removeEventListener(listenerestadoServicio);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rcv != null) {
            unregisterReceiver(rcv);
        }

        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
        }
        try {
            if(receptorMensajesServidor!=null)unregisterReceiver(receptorMensajesServidor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("ONDESTROY","RECEPTOR NO REGISTRADO");
        }

        try {
            Query_conectados.removeEventListener(ListenerLocacionConectados);
           querymotos.removeEventListener(ListenerMotosConectados);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("OnPause()","Query_conectados null");
        }
        if(id_auth != null){
            verificarDomiciliosEnCurso(id_auth);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();


        //------------- implementamos el receptor para los mensajes del servidor------------//
        receptorMensajesServidor = new ReceptorMensajesServidor();
        IntentFilter intentFilter = new IntentFilter(Constantes.ACTION_SIN_MENSAJERO_CERCA);
        intentFilter.addAction(Constantes.ACTION_SIN_MENSAJERO_CERCA);
        intentFilter.addAction(Constantes.ACTION_ID_LISTA);
        intentFilter.addAction(Constantes.ACTION_CONFIRMAR_LLEGADA);
        intentFilter.addAction(Constantes.ACTION_MENSAJE_CHAT);
        intentFilter.addAction(Constantes.ACTION_CONFIRMAR_SALDO);
        registerReceiver(receptorMensajesServidor,intentFilter);
        //--------------------------------------------------------------------


        IntentFilter filter = new IntentFilter();
        filter.addAction(Constantes.ACTION_PROGRESO);
        filter.addAction(Constantes.ACTION_FIN);
        filter.addAction(Constantes.ACTION_FIN_ERROR);
        rcv = new ReceiverServicioDireccion();
        registerReceiver(rcv, filter);

        ListaMarcadores = new ArrayList<>();


        //si esta el servicio activo se esconden los botones de moto y carro
        if(layout_confirmar_carrera.getVisibility()==View.VISIBLE){
            Log.i("OnResume()","esconder botones");
            view_inicio.setVisibility(View.INVISIBLE);
        }

        registerReceiver(networkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        //comprobar el acceso a internet
        if (!isOnline(this)) {

            Toast.makeText(this,"Verifique su conexión a internet",Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alert != null) {
            alert.dismiss();
        }

    }
int contador_de_servicios_enviados = 1;
    public void enviarpedido() {

        try{
            progressConfirmar.setMessage("Enviando Solicitud....");
            progressConfirmar.setCanceledOnTouchOutside(false);
            progressConfirmar.show();

            fecha_pedido = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            calendar = Calendar.getInstance();

            //SACAMOS LAS COORDENADAS DE LA UBICACION DEL CLIENTE
            Double latitud = pocisionInicial.latitude;
            Double longitud = pocisionInicial.longitude;
            Double latitud_final = latitud;
            Double longitud_final = longitud;

            if(pocisionFinal!=null){
                latitud_final = pocisionFinal.latitude;
                longitud_final = pocisionFinal.longitude;
            }


            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    token = instanceIdResult.getToken();
                }
            });
            String tipo_servicio;
            int tip;
            tip = spinner.getSelectedItemPosition();

            switch (tip){
                case 1:
                    tipo_servicio = Constantes.COMPRAS_PEDIR_DOMICILIOS;
                    break;
                case 2:
                    tipo_servicio = Constantes.FACTURAS_TRAMITES;
                    break;
                case 3:
                    tipo_servicio = Constantes.ENCOMIENDAS;
                    break;
                case 4:
                    tipo_servicio = Constantes.SOLICITUD_RAPIDA;
                    break;
                default:
                    tipo_servicio = Constantes.COMPRAS_PEDIR_DOMICILIOS;
                    break;
            }

            String tipo_pago = "";
            tipo_pago = "efectivo";
            Log.i("tipo pedido", "" + tip);

            Log.i("fecha", " " + fecha_pedido);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String id_user = "";
            try{
                 id_user = mAuth.getCurrentUser().getUid();
            }catch (NullPointerException e){
                toast("Ha ocurrido un error intente mas tarde");
                return;
            }

            if (nombre ==null) {
                datosUsuario();
            }

            final  Pedidos pedido = new Pedidos(IdPedidoEnCurso,id_user,fecha_pedido.format(calendar.getTime()), calendar.getTimeInMillis(),
                    DireccionInicial,Direccion_Final,precio,Constantes.BD_ESTADO_PENDIENTE,Comentario,Constantes.BD_ASIGNAR_MOVIL,
                    Constantes.SERVICIO_MENSAJERO,token,tipo_servicio,tipo_pago,nombre,telefono,latitud,longitud,latitud_final,longitud_final, ciudad);

            if(es_empresa){
                int mensajeros = 1;
                pedido.setServicio_empresa(true);
                if(ratingCuantosMensajeros.getRating()<2){
                    mensajeros = 1;
                }else if(ratingCuantosMensajeros.getRating()<3){
                    mensajeros = 2;
                }else if(ratingCuantosMensajeros.getRating()<4){
                    mensajeros = 3;
                }else if(ratingCuantosMensajeros.getRating()<5){
                    mensajeros = 4;
                }else if(ratingCuantosMensajeros.getRating()==5){
                    mensajeros = 5;
                }
                pedido.setCuantos_mensajeros(mensajeros);
            }else{
                pedido.setCuantos_mensajeros(1);
            }




                new Timer().scheduleAtFixedRate(new TimerTask(){
                    @Override public void run(){
                        final int vez = contador_de_servicios_enviados;
                        if(vez <= pedido.getCuantos_mensajeros()) {
                            final DatabaseReference currentUserDB = databaseServicioMoto.push();
                            IdPedidoEnCurso = currentUserDB.getKey();
                            pedido.setId_pedido(IdPedidoEnCurso);
                            calendar = Calendar.getInstance();
                            queryEstadoPedido = databaseServicioMoto.child(IdPedidoEnCurso).orderByChild(Constantes.BD_ESTADO_PEDIDO);
                            pedido.setFecha_pedido(fecha_pedido.format(calendar.getTime()));
                            currentUserDB.setValue(pedido, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    Log.i("Enviando Servicio", vez + " vez");
                                    progressConfirmar.setMessage("Enviando Servicio "+vez+ "...");
                                    contador_de_servicios_enviados++;
                                }
                            });
                        }else{
                            this.cancel();
                            if(progressConfirmar.isShowing())progressConfirmar.dismiss();
                            queryEstadoPedido.addValueEventListener(listenerestadoServicioMoto);
                            contador_de_servicios_enviados = 1;
                        }
                    }
                    },0,5000);
        }catch (NullPointerException e){
            e.printStackTrace();
            toast("Ha ocurrido un error intente nuevamente");
        }
    }


    public void enviarpedido_especial() {

       try{

           //si es la primera vez que se envía se muestra el progres bar
           FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
               @Override
               public void onSuccess(InstanceIdResult instanceIdResult) {
                   token = instanceIdResult.getToken();
                   progressConfirmar.setMessage("Enviando Solicitud....");
                   progressConfirmar.setCanceledOnTouchOutside(false);
                   progressConfirmar.show();


                   fecha_pedido = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                   calendar = Calendar.getInstance();
                   String id_user = "";
                   try{
                       id_user = mAuth.getCurrentUser().getUid();
                   }catch (NullPointerException e){
                       toast("Ha ocurrido un error intente mas tarde");
                       return;
                   }

                   //SACAMOS LAS COORDENADAS DE LA UBICACION DEL CLIENTE
                   Double latitud = pocisionInicial.latitude;
                   Double longitud = pocisionInicial.longitude;
                   Double latitud_final = latitud;
                   Double longitud_final = longitud;

                   if(pocisionFinal!=null){
                       latitud_final = pocisionFinal.latitude;
                       longitud_final = pocisionFinal.longitude;
                   }

                   Log.i("fecha", " " + fecha_pedido);

                   if (nombre ==null) {
                       datosUsuario();
                   }


                   DatabaseReference currentUserDB = databaseServicio.push();
                   if(currentUserDB.getKey()!=null) {
                       IdPedidoEnCurso = currentUserDB.getKey();
                   }else{
                       toast("ocurrio un error intente nuevamente");
                       return;
                   }
                   queryEstadoPedido = databaseServicio.child(IdPedidoEnCurso).orderByChild(Constantes.BD_ESTADO_PEDIDO);
                   String tipo_pago = "efectivo";

                   final Pedidos pedido = new Pedidos(IdPedidoEnCurso,id_user,fecha_pedido.format(calendar.getTime()), calendar.getTimeInMillis(),
                           DireccionInicial,Direccion_Final,precio,Constantes.BD_ESTADO_PENDIENTE,null,Constantes.BD_ASIGNAR_MOVIL,Constantes.SERVICIO_MENSAJERO_ESPECIAL,token,null,
                           tipo_pago,nombre,telefono,latitud,longitud,latitud_final,longitud_final, ciudad);


                   currentUserDB.setValue(pedido, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                           if(progressConfirmar.isShowing())progressConfirmar.dismiss();
                           SharedPreferences.Editor editor = preferences.edit();
                           editor.putString(Constantes.BD_ID_PEDIDO,IdPedidoEnCurso);
                           editor.apply();
                           queryEstadoPedido.addValueEventListener(listenerestadoServicio);
                       }
                   });
               }
           });







       /* */
       }catch (Exception e){
           e.printStackTrace();
           toast("Ha ocurrido un error intente nuevamente");
       }
    }

    private void agregarMapa() {


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (pocisionInicial==null) {
                    obtenerlocation();
                }
                map = googleMap;
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.style_json));

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                Constantes.PETICION_PERMISO_LOCALIZACION);
                        return;
                    } else {


                        map.setMyLocationEnabled(true);
                        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                obtenerlocation();
                                return false;
                            }
                        });
                    }
                }


                //map.setMinZoomPreference(13.0f);

                map.setPadding(0, 60, 0, 0);

                if(pocisionInicial != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(pocisionInicial, 15), 1000, null);
                }

                map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {

                        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {

                            if (buscar_location) {
                                fabOk.hide();
                                fabCancel.hide();
                            }

                        }

                    }
                });
                map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                        if (buscar_location) {
                            if (tienefoco == FOCO_INICIAL) {

                                fabOk.show();
                                fabCancel.show();
                                map.clear();
                                pocisionInicial = map.getCameraPosition().target;
                                try {
                                    mLastLocation.setLatitude(pocisionInicial.latitude);
                                    mLastLocation.setLongitude(pocisionInicial.longitude);
                                    startIntentService(Constantes.REQUEST_DIRECCION_INICIAL);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    toast("intenta nuevamente");
                                }
                            } else if (tienefoco == FOCO_FINAL) {
                                fabOk.show();
                                fabCancel.show();
                                pocisionFinal = map.getCameraPosition().target;
                                try {
                                    mLastLocation.setLatitude(pocisionFinal.latitude);
                                    mLastLocation.setLongitude(pocisionFinal.longitude);
                                    startIntentService(Constantes.REQUEST_DIRECCION_FINAL);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    toast("intenta nuevamente");
                                }
                            }
                        }
                    }
                });

                if (Query_conectados != null && ListenerLocacionConectados!=null) {
                    Query_conectados.addChildEventListener(ListenerLocacionConectados);
                }
                if(querymotos != null && ListenerMotosConectados != null){
                    querymotos.addChildEventListener(ListenerMotosConectados);
                }



            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (requestCode == Constantes.PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido
                onResume();


            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización
                Toast.makeText(this, "Se necesita permiso de localizacion para usar la aplicacion", Toast.LENGTH_LONG)
                        .show();
                onRestart();
                Log.e("permisos", "Permiso denegado");
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void permisoLocalizacion() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        Constantes.PETICION_PERMISO_LOCALIZACION);
                return;
            } else {

                obtenerlocation();

            }
        } else {
            obtenerlocation();
        }
    }

    public String getCiudad(){
        return ciudad;
    }
    public void obtenerlocation() {
        Proveedor_de_localizacion.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            pocisionInicial = (new LatLng(location.getLatitude(), location.getLongitude()));
                            mLastLocation = location;
                            startIntentService(Constantes.REQUEST_DIRECCION_INICIAL);
                                agregarMapa();
                        }
                    }
                });
    }

    public void toast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    //para el mensaje de recibido
    public void toastrecibido(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }


    private String obtenerDireccionesURL(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key="+ getResources().getString(R.string.API_KEY_PROD);

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.i("URL api", url);

        return url;
    }

    // crear URl para la solicitud de distancia y tiempo
    private String obtenerdistanciaURL(LatLng origin, LatLng dest) {

        String str_origin = origin.latitude + "," + origin.longitude;

        String str_dest = dest.latitude + "," + dest.longitude;

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + str_origin + "&destinations="
                + str_dest+ "&key="+ getResources().getString(R.string.API_KEY_PROD);

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creamos una conexion http
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conectamos
            urlConnection.connect();

            // Leemos desde URL
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
            Log.i("error de Conexion", "sin internet");

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("cargando ruta...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("ERRORALOBTENERINFODELWS", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

            progressDialog.dismiss();

        }
    }

    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            points = null;

            lineOptions = new PolylineOptions();

            if (result!=null) {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);

                    }

                    lineOptions.addAll(points);
                    lineOptions.width(15);
                    lineOptions.jointType(JointType.ROUND);
                    lineOptions.startCap(new RoundCap());
                    lineOptions.endCap(new RoundCap());
                    lineOptions.color(Color.rgb(41, 135, 189));
                }
            }else{
                toast("No se pudo cargar la ruta, intente nuevamente");
            }
            if (lineOptions != null) {
                map.addPolyline(lineOptions);
                progressDialog.dismiss();
            }
            progressDialog.dismiss();

        }
    }


    private void ruta() {
        try {
            String url = obtenerDireccionesURL(pocisionInicial, pocisionFinal);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);

            String urldistancia = obtenerdistanciaURL(pocisionInicial, pocisionFinal);
            DownloadTaskDistancia downloadTaskDistancia = new DownloadTaskDistancia(progressDialog, this);
            downloadTaskDistancia.execute(urldistancia);
        } catch (Exception e) {
            Log.i("RUtas", "error al buscar la ruta");
        }
    }

    public void confirmar(String Direccion1, @Nullable String Direccion2,
                          String tipo, Double precio,
                          String comentario) {


        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_confirmacion, null);
        dialog.setContentView(v);


        final Button confirmar =  v.findViewById(R.id.confirmarButConf);
        Button cancelar =  v.findViewById(R.id.cancelarButConf);
        TextView direccionInicial =  v.findViewById(R.id.direccionInicialConf);
        TextView direccionfinal =  v.findViewById(R.id.direccionFinalConf);
        TextView tipopedido =  v.findViewById(R.id.tipoPedidoConf);
        TextView formapago =  v.findViewById(R.id.formaPagoConf);
        TextView fechapedido =  v.findViewById(R.id.fechaPedidoConf);
        TextView horapedido =  v.findViewById(R.id.horaPedidoConf);
        TextView comentarioText =  v.findViewById(R.id.comentarioConf);

        fechapedido.setHeight(0);
        horapedido.setHeight(0);
        fechapedido.setVisibility(View.INVISIBLE);
        horapedido.setVisibility(View.INVISIBLE);

        DecimalFormat format = new DecimalFormat("#");
        direccionInicial.setText("Direccion Inicial: " + Direccion1);
        direccionfinal.setText("Direccion Final: " + Direccion2);
        tipopedido.setText("Tipo de solcitud: " + tipo);
        formapago.setText("Valor Aproximadamente: $" + format.format(precio));
        comentarioText.setText("Comentario: " + comentario);


        confirmar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // enviar pedido
                        if (isOnline(MainActivity.this)) {
                            enviarpedido();
                            solicitar_moto.setText(SOLICITAR_MOTO);
                        } else {
                            toast("No hay conexión a internet, intentalo nuevamente");
                        }

                        dialog.dismiss();


                    }
                }
        );

        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Loguear...
                        dialog.dismiss();

                    }
                }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    public void datosUsuario() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
            }
        });
        Gson gson = new Gson(); //Instancia Gson.
        String json = preferences.getString(Constantes.BD_USUARIO, null);
        Usuario usuario = gson.fromJson(json, Usuario.class);
        if (usuario==null) {
            Log.i("Datos Usuario","usuario null");
            try {
                id_auth = mAuth.getCurrentUser().getUid();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (id_auth != null) {
                Log.i("Datos Usuario","id auth " + id_auth);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference currenUser = database.getReference()
                        .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_USUARIO)
                        .child(id_auth);
                currenUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue()!=null) {
                            Log.i("Datos Usuario",dataSnapshot.toString());
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            id_user = usuario.getId_usuario();
                            nombre = usuario.getNombre();
                            telefono = usuario.getTelefono();
                            email = usuario.getEmail();
                            es_empresa = usuario.getEs_empresa();
                            navigationView.getMenu().clear();
                            navigationView.inflateMenu(R.menu.activity_main_drawer);
                            spinneradapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.tipos_de_pedidos,
                                    R.layout.spinner_item);
                            spinneradapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                            spinner.setAdapter(spinneradapter);
                            verificarDomiciliosEnCurso(id_auth);

                            if (nombre != null && email != null) {
                                EnombreUsuario.setText(nombre);
                                Eemailusuario.setText(email);
                            }
                            //aqui se registran el token del ususario en la base de datos
                            currenUser.child("token").setValue(token);
                            usuario.setToken(token);
                        } else {
                            Log.i("Datos Usuario","data  null " + dataSnapshot );
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("Datos Usuario","database error" + databaseError.getDetails());
                    }
                });
            }else {
                Log.i("Datos Usuario","id_auth null");
            }

        } else {
            id_user = usuario.getId_usuario();
            nombre = usuario.getNombre();
            telefono = usuario.getTelefono();
            email = usuario.getEmail();
            es_empresa = usuario.getEs_empresa();
            if (es_empresa) {
                spinneradapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_pedidos,
                        R.layout.spinner_item);
                spinner.setAdapter(spinneradapter);
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer_empresa);
            } else {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                spinneradapter = ArrayAdapter.createFromResource(this, R.array.tipos_de_pedidos_empresa,
                        R.layout.spinner_item);
                spinner.setAdapter(spinneradapter);
            }

            if (nombre != null && email != null) {
                EnombreUsuario.setText(nombre);
                Eemailusuario.setText(email);
            }
        }
        //guardamos los datos del usuario en las preferencias
        if (usuario!=null) {
            usuario.setToken(token);
        }


        json = gson.toJson(usuario);//convertimos el objeto a json
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.BD_USUARIO, json);
        editor.apply();


    }

    public void ocultarTeclado(EditText e) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }

    public static void actualizarUI(@Nullable TextView e0, EditText e1, EditText e2, EditText e3, @Nullable EditText e4) {

        if (e0 != null) {
            e0.setVisibility(View.INVISIBLE);
            e0.setText("");
        }
        e1.setText("");
        e2.setText("");
        e3.setText("");
        if (e4 != null) {
            e4.setText("");
        }
    }

    //reinicia una Activity
    public static void reiniciarActivity(AppCompatActivity actividad) {
        Intent intentreiniciar = new Intent();
        intentreiniciar.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intentreiniciar);
        //finalizamos la actividad actual
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            actividad.finishAffinity();
        }else{
            actividad.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String hint;
                try {
                    hint = textInputInicial.getHint().toString();
                } catch (Exception e) {
                    hint = "";
                    e.printStackTrace();
                }
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("activity-result", "Place: " + place.getName());
                if (tienefoco == FOCO_INICIAL) {

                    if(MENSAJERO_ESPECIAL){

                        if (pocisionFinal != null) {
                            //limpiamos el mapa para agregar los nuevos marcadores y la ruta
                            if (map != null) {
                                map.clear();
                                map.setPadding(10,alto/3,10,alto/4);
                            }
                            bounds_ruta = null;

                            //ocultamos el layout de solicitudes
                            aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                            aparecer_der_izq.setDuration(250);
                            aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    layout_solicitudes.setVisibility(View.INVISIBLE);
                                    texto_direcciones.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            layout_solicitudes.startAnimation(aparecer_der_izq);

                            //para ocultar el appbar boton
                            aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                            aparecer_izq_der.setDuration(300);
                            aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    appbarboton.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            appbarboton.startAnimation(aparecer_izq_der);

                            layout_botones.setVisibility(View.INVISIBLE);
                            layout_confirmar_carrera.setVisibility(View.VISIBLE);
                            img_carrera_activa.setImageResource(R.drawable.carro);

                            View vmapa = findViewById(R.id.mapa);
                            vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
                            pocisionInicial = place.getLatLng();
                            DireccionInicial = place.getName();
                            if(CAMBIAR_DIRECCION_DE_ORIGEN){
                                CAMBIAR_DIRECCION_DE_ORIGEN = false;
                                }
                            try {

                                direccionInicial.setText(place.getName());
                                bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();

                                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 100));

                                Marker inicio = map.addMarker(new MarkerOptions()
                                        .position(pocisionInicial)
                                        .title(CAMBIAR_ORIGEN)
                                        .snippet(DireccionInicial)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));
                                inicio.showInfoWindow();


                                map.addMarker(new MarkerOptions()
                                        .position(pocisionFinal)
                                        .title("Dejarme Aqui")
                                        .snippet(Direccion_Final)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));

                                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {

                                        CAMBIAR_DIRECCION_DE_ORIGEN=true;
                                        try{
                                            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                                            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                                    .setCountry("Co")
                                                    .setLocationBias(Popayan)
                                                    .build(MainActivity.this);
                                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                                        } catch (Exception e) {
                                            // TODO: Handle the error.
                                        }
                                    }
                                });
                                ruta();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                toast("Ha ocurrido un error, intente nuevamente");
                            }


                        } else {
                            direccionInicial.setText(place.getName().replace(", Popayán, Cauca, Colombia", ""));
                            direccionFinal.requestFocus();
                            pocisionInicial = place.getLatLng();
                            map.clear();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pocisionInicial, 17), 1000, null);

                            map.addMarker(new MarkerOptions()
                                    .position(pocisionInicial)
                                    .title("Recoger Aquí")
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));

                        }


                    }else{
                        direccionInicial.setText(place.getName().replace(", Popayán, Cauca, Colombia", ""));
                        direccionFinal.requestFocus();
                        pocisionInicial = place.getLatLng();

                        if (pocisionFinal != null) {
                            ruta();
                        }

                        if (map != null ) {
                            map.clear();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pocisionInicial, 17), 1000, null);

                            map.addMarker(new MarkerOptions()
                                    .position(pocisionInicial)
                                    .title("Recoger Aquí")
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_moto)));
                        }
                    }

                } else if (tienefoco == FOCO_FINAL) {

                    direccionFinal.setText(place.getName().replace(", Popayán, Cauca, Colombia", ""));
                    Ecomentario.requestFocus();
                    pocisionFinal = place.getLatLng();

                    //limpiamos el mapa para agregar los nuevos marcadores y la ruta
                    if (map != null) {
                        map.clear();
                        bounds_ruta = LatLngBounds.builder().include(pocisionInicial).include(pocisionFinal).build();
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 200));
                    }


                    if(MENSAJERO_ESPECIAL){
                        //ocultamos el layout de solicitudes
                        aparecer_der_izq.setInterpolator(new ReverseInterpolator());
                        aparecer_der_izq.setDuration(250);
                        aparecer_der_izq.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                layout_solicitudes.setVisibility(View.INVISIBLE);
                                texto_direcciones.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        layout_solicitudes.startAnimation(aparecer_der_izq);

                        //para ocultar el appbar boton
                        aparecer_izq_der.setInterpolator(new ReverseInterpolator());
                        aparecer_izq_der.setDuration(300);
                        aparecer_izq_der.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                appbarboton.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        appbarboton.startAnimation(aparecer_izq_der);

                        layout_botones.setVisibility(View.INVISIBLE);
                        layout_confirmar_carrera.setVisibility(View.VISIBLE);
                        img_carrera_activa.setImageResource(R.drawable.carro);

                        View vmapa = findViewById(R.id.mapa);
                        vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));

                        map.addMarker(new MarkerOptions()
                                .position(pocisionInicial)
                                .title("Recógeme Aqui")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_carro)));
                        map.addMarker(new MarkerOptions()
                                .position(pocisionFinal)
                                .title("Déjame Aqui")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));

                    }else{
                        map.setPadding(100, alto/3+100, 100, 100);
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds_ruta, 10));
                        map.addMarker(new MarkerOptions()
                                .position(pocisionInicial)
                                .title("Recoger Aqui")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_moto)));
                        map.addMarker(new MarkerOptions()
                                .position(pocisionFinal)
                                .title("Entregar Aqui")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_final)));
                    }
                    ruta();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                // TODO: Handle the error.
                Log.i("activity-result", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public double calcularPrecio(int distancia) {
        double precio = 0;
        final double porcentaje = 0.1;

        int item = spinner.getSelectedItemPosition();
        switch (item) {
            case 1:
                precio = 3000 + (3000 * porcentaje);
                break;
            case 2:
                precio = 5000 + (5000 * porcentaje);
                break;
            case 3:
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
                break;
        }


        return precio;

    }


    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat - 1f);
        }
    }

    public double calcular_precio_carrera(int tiempo, int distancia) {
        double precio_carrera = 0;
        double valor_minuto = 0;
        double valor_km = 0;
        double precio_base = 0;
        if(Tipo.equals("carro")){
             valor_minuto = 120;
             valor_km = 530;
             precio_base = 3000;

            //tomar la hora actual
            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int diadelaSemana = calendar.get(Calendar.DAY_OF_WEEK);

            // se calculan los precios de acuerdo a la hora y la distancia
            if (distancia < 12000) {
                Log.i("hora del dia", "" + hourOfDay);
                if (hourOfDay >= 5 && hourOfDay < 21) {

                    valor_minuto = valor_minuto * 1.05;
                    valor_km = valor_km * 1.05;
                    precio_base = precio_base * 1.05;
                    Log.i("calcularpreciocarrera", "" + precio_base);
                    Log.i("dia de la semana", "" + calendar.get(Calendar.DAY_OF_WEEK));

                } else if ((hourOfDay >= 21 && hourOfDay <= 23) || (hourOfDay >= 0 && hourOfDay < 3)) {

                    valor_minuto = valor_minuto * 1.1;
                    valor_km = valor_km * 1.1;
                    precio_base = precio_base * 1.1;
                    Log.i("calcularpreciocarrera", "" + precio_base);
                    Log.i("dia de la semana", "" + calendar.get(Calendar.DAY_OF_WEEK));

                } else if (hourOfDay >= 3 && hourOfDay < 5) {

                    valor_minuto = valor_minuto * 1.2;
                    valor_km = valor_km * 1.2;
                    precio_base = precio_base * 1.2;
                    Log.i("calcularpreciocarrera", "" + precio_base);

                }

            } else {
                Log.i("hora del dia", "" + hourOfDay);
                if (hourOfDay >= 5 && hourOfDay < 21) {

                    valor_minuto = valor_minuto * 1.3;
                    valor_km = valor_km * 1.3;
                    precio_base = precio_base * 1.3;
                    Log.i("calcularpreciocarrera", "" + precio_base);

                } else if (hourOfDay >= 21 && hourOfDay < 3 || (hourOfDay >= 0 && hourOfDay < 3)) {

                    valor_minuto = valor_minuto * 1.35;
                    valor_km = valor_km * 1.35;
                    precio_base = precio_base * 1.35;
                    Log.i("calcularpreciocarrera", "" + precio_base);

                } else if (hourOfDay >= 3 && hourOfDay < 5) {

                    valor_minuto = valor_minuto * 1.5;
                    valor_km = valor_km * 1.5;
                    precio_base = precio_base * 1.5;
                    Log.i("calcularpreciocarrera", "" + precio_base);

                }

            }
        }else{
            valor_minuto = 80;
            valor_km = 400;
            precio_base = 1700;
        }


        // ----------------------------------- fin del calculo de hora y distancia -----------------------------------------


        precio_carrera = (valor_minuto * (tiempo / 60)) + (valor_km * (distancia / 1000)) + precio_base;

        return precio_carrera;
    }

    public class ReceptorMensajesServidor extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("RECEPTOR","RECIBIDO");
            String accion = "accion";
            if (intent!=null) {
                accion = intent.getAction();
            }

            if (accion !=null) {
                switch (accion){
                    case Constantes.ACTION_SIN_MENSAJERO_CERCA:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Nuestros Mensajeros están ocupados, ¿desea intentar nuevamente?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, int i) {
                                        if (IdPedidoEnCurso!=null) {
                                            DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                                                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                                    .child(Constantes.BD_PEDIDO_ESPECIAL).child(IdPedidoEnCurso);

                                            database.removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            enviarpedido_especial();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    toast("Ups algo falló intenta nuevamente");
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                        }else{
                                            toast("Ups algo falló intenta nuevamente");
                                            dialogInterface.dismiss();
                                        }

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (IdPedidoEnCurso!=null) {
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                                            .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                            .child(Constantes.BD_PEDIDO_ESPECIAL).child(IdPedidoEnCurso);

                                    database.child(Constantes.BD_ESTADO_PEDIDO).setValue(Constantes.BD_CANCELADO)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            toast("Servicio Cancelado");
                                        }
                                    });
                                }else{
                                    dialogInterface.dismiss();
                                }
                            }
                        }).show();
                        break;
                    case Constantes.ACTION_CONFIRMAR_LLEGADA:
                        AlertDialog.Builder builder1 = new  AlertDialog.Builder(context);
                        builder1.setMessage("Tu mensajero ha llegado")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setCancelable(false)
                                .show();
                        break;
                    case Constantes.ACTION_MENSAJE_CHAT:
                        if (!fragmentChat.isAdded()) {
                            String id_p = intent.getStringExtra(Constantes.BD_ID_PEDIDO);
                            appbarboton.setVisibility(View.INVISIBLE);
                            appBarLayout.setVisibility(View.VISIBLE);
                            fragmentChat = new FragmentChat();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constantes.BD_NOMBRE_USUARIO,nombre);
                            bundle.putString(Constantes.BD_ID_PEDIDO,id_p);
                            bundle.putString(Constantes.TIPO_SERVICIO, intent.getStringExtra(Constantes.TIPO_SERVICIO));
                            fragmentChat.setArguments(bundle);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            ft.replace(R.id.main_content, fragmentChat, FragmentChat.TAG);
                            ft.commit();
                        }
                        break;
                    case Constantes.ACTION_CONFIRMAR_SALDO:
                        try {
                            if (fragment_bonos.isAdded()) {
                                AlertDialog.Builder builder2 = new  AlertDialog.Builder(context);
                                builder2.setMessage("Bono redimido exitósamente")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).setCancelable(true)
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                fragment_bonos.datosUsuario(id_user);
                                            }
                                        })
                                        .show();

                            } else {
                                toast("Bono redimido exitósamente");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constantes.ACTION_ACTUALIZAR:
                        Log.i("ACTION_ACTUALIZAR", "HAY UNA ACTUALIZACION DISPONIBLE");
                        MostrarActualizar();
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DistanciaEvent event) {
        if(event.distancia<1){
            texto_confirmacion_carrera_activa.setText("Ampliando busqueda... Radio 1Km");
        }else if(event.distancia<2){
            texto_confirmacion_carrera_activa.setText("Ampliando busqueda... Radio 2Km");
        }else{
            texto_confirmacion_carrera_activa.setText("Ampliando busqueda... Radio 3Km");
        }
    }

    private void MostrarActualizar() {
        try {
            //creando el dialogo de actualizacion
            dialog_Actualizar = new Dialog(this, R.style.Theme_AppCompat_DialogWhenLarge);
            dialog_Actualizar.setCancelable(true);
            LayoutInflater inflater = this.getLayoutInflater();
            View v = inflater.inflate(R.layout.layout_actualizar, null);
            Button cerrar = v.findViewById(R.id.cerrar_actualizar);
            Button actualizar = v.findViewById(R.id.actualizar);
            cerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_Actualizar.dismiss();
                }
            });
            actualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPlayStore();
                    dialog_Actualizar.dismiss();
                }
            });
            dialog_Actualizar.setContentView(v);
            dialog_Actualizar.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPlayStore(){
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    //metodo para crear canales de notificaciones
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CrearCanalesDeNOtificaciones(NotificationManager notificationManager) {


        try {
            Uri sonidoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tin);
            //-------- canal para el servicio conectado --------------------
            NotificationChannel canalDefault =
                    new NotificationChannel(Constantes.ID_CANAL_DEFAULT, Constantes.NOMBRE_CANAL_DEFAULT
                            , NotificationManager.IMPORTANCE_DEFAULT);
            canalDefault.setLightColor(Color.MAGENTA);
            canalDefault.setSound(null, null);
            canalDefault.setBypassDnd(false);
            canalDefault.enableLights(true);
            canalDefault.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(canalDefault);
            //------- canal creado ---------------

            //-------- canal para el servicio desconectar --------------------
            NotificationChannel canalChat =
                    new NotificationChannel(Constantes.ID_CANAL_CHAT, Constantes.NOMBRE_CANAL_CHAT
                            , NotificationManager.IMPORTANCE_HIGH);
            canalChat.setLightColor(Color.MAGENTA);
            canalChat.setSound(sonidoUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            canalChat.setBypassDnd(true);
            canalChat.setImportance(NotificationManager.IMPORTANCE_HIGH);
            canalChat.enableVibration(true);
            canalChat.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(canalChat);
            //------- canal creado ---------------

            //-------- canal para el servicio conectado --------------------
            NotificationChannel canalListado =
                    new NotificationChannel(Constantes.ID_CANAL_LISTADO, Constantes.NOMBRE_CANAL_LISTADO
                            , NotificationManager.IMPORTANCE_DEFAULT);
            canalListado.setLightColor(Color.MAGENTA);
            canalListado.setSound(null,null);
            canalListado.setBypassDnd(false);
            canalListado.enableLights(true);
            canalListado.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(canalListado);
            //------- canal creado ---------------
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void servicioTerminado( @NonNull final Pedidos pedido){

        if(!pedido.getTipo_pedido().equals(Constantes.SOLICITUD_RAPIDA)){
            try {
                final AlertDialog.Builder alertRating = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.layout_calificar, null);
                final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
                alertRating.setView(dialogLayout);
                final AlertDialog dialogRating = alertRating.create();
                final ProgressBar progressBarCalificacion = dialogLayout.findViewById(R.id.progressBarCalificacion);
                final TextView tipocalificacion = dialogLayout.findViewById(R.id.calificacion);
                final EditText comentarioEdit = dialogLayout.findViewById(R.id.editComentario);
                final Button enviar = dialogLayout.findViewById(R.id.enviarcalificacion);
                final TextView textoinformacion = dialogLayout.findViewById(R.id.info);
                textoinformacion.setText("El valor de tu viaje fué de: $"+pedido.getValor_pedido());
                enviar.setEnabled(false);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        pedido.setCalificacion(rating);
                        enviar.setEnabled(true);
                        if(rating<=1){
                            tipocalificacion.setText("Malo");
                        }else if(rating<=2){
                            tipocalificacion.setText("Regular");
                        }else if(rating<=3){
                            tipocalificacion.setText("Bueno");
                        }else if(rating<=4){
                            tipocalificacion.setText("Muy Bueno");
                        }else if(rating<=5){
                            tipocalificacion.setText("Excelente");
                        }
                    }
                });
                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comentario = null;
                        progressBarCalificacion.setVisibility(View.VISIBLE);
                        if (comentarioEdit.getText() != null) {
                            comentario = comentarioEdit.getText().toString();
                            if(comentario.length()<10){
                                comentario = null;
                            }else{
                                pedido.setPqr(comentario);
                            }
                        }

                        DatabaseReference dataPedido = FirebaseDatabase.getInstance().getReference()
                                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                                .child(Constantes.BD_PEDIDO).child(pedido.getId_pedido()).child(Constantes.BD_PQR);
                        dataPedido.setValue(pedido.getPqr()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBarCalificacion.setVisibility(View.INVISIBLE);

                                dialogRating.dismiss();

                            }
                        });

                    }
                });
                dialogRating.show();

            } catch (Exception e) {
                e.printStackTrace();
                reiniciarActivity(MainActivity.this);
            }
        }

        fabChat.hide();
        BuscarMensajeroBar.setVisibility(View.INVISIBLE);
        actualizarUI(texto_direcciones, direccionInicial, direccionFinal, Ecomentario, null);
        layout_solicitudes.setVisibility(View.INVISIBLE);
        layout_botones.setVisibility(View.INVISIBLE);
        ratingMensajero.setVisibility(View.INVISIBLE);
        layout_confirmar_carrera.setVisibility(View.INVISIBLE);
        View v = findViewById(R.id.mapa);
        v.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));
        view_inicio.setVisibility(View.VISIBLE);
        appbarboton.setVisibility(View.VISIBLE);
        confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
        confirmar_carro.setEnabled(true);
        texto_tiempo.setText(null);
        IdPedidoEnCurso = null;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constantes.BD_ID_PEDIDO,IdPedidoEnCurso);
        editor.apply();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        }

        if (map != null) {
            map.clear();
            agregarMapa();
        }

        queryEstadoPedido.removeEventListener(listenerestadoServicioMoto);
    }

    private void servicioEnCurso(@NonNull final Pedidos pedido){
        layout_confirmar_carrera.setVisibility(View.VISIBLE);
        View vmapa = findViewById(R.id.mapa);
        vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
        confirmar_carro.setOnClickListener(this);
        view_inicio.setVisibility(View.INVISIBLE);
        texto_tiempo.setText(pedido.getTipo_pedido());

        BuscarMensajeroBar.setVisibility(View.INVISIBLE);
            confirmar_carro.setText(Constantes.CANCELAR_SOLICITUD);
            confirmar_carro.setEnabled(true);
            fabChat.show();
            confirmar_carro.setTextColor(getResources().getColor(R.color.colornaranja));
            String codigo_mensajero = pedido.getCodigo_mensajero();

            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference()
                    .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                    .child(Constantes.MENSAJERO_CONECTADO);

            Query query = firebaseDatabase.child(codigo_mensajero);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                .child(Constantes.BD_MENSAJERO).child(codigo_mensajero);

        final Query query1 = data.orderByChild(Constantes.BD_CODIGO);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
                    String nombre = mensajero.getNombre();
                    String placa = mensajero.getPlaca();
                    final String numero = mensajero.getTelefono();
                    String codigo_mensajero = mensajero.getCodigo();

                    String pathFoto ="movil_"+codigo_mensajero;


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageReference = storage.getReference().child("mensajeros")
                            .child("mensajero_carro").child(pathFoto).child("foto_perfil");

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(MainActivity.this)
                                    .load(storageReference)
                                    .into(img_carrera_activa);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("storage", "nulo");
                            img_carrera_activa.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                        }
                    });
                    texto_confirmacion_carrera_activa.setText(nombre);
                    ratingMensajero.setVisibility(View.VISIBLE);
                    ratingMensajero.setRating(mensajero.getCalificacion());
                    texto_placa_carrera_activa.setText(placa);
                    texto_valor.setText(numero);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_llamada, 0);
                    }
                    texto_valor.setTextColor(getResources().getColor(R.color.colorPrimary));
                    texto_valor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + numero));

                            int permissionCheck = ContextCompat.checkSelfPermission(
                                    MainActivity.this, Manifest.permission.CALL_PHONE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 225);
                            } else {
                                Log.i("Mensaje", "Se tiene permiso!");
                                startActivity(intent);
                            }
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    query1.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void servicioIniciado(@NonNull final Pedidos pedido,@NonNull  String estado){


        confirmar_carro.setText(estado.replace("_"," "));
        confirmar_carro.setTextColor(Color.WHITE);
        fabChat.show();
        layout_confirmar_carrera.setVisibility(View.VISIBLE);
        View vmapa = findViewById(R.id.mapa);
        vmapa.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, alto / 2));
        view_inicio.setVisibility(View.INVISIBLE);
        texto_tiempo.setText(pedido.getTipo_pedido());
        BuscarMensajeroBar.setVisibility(View.INVISIBLE);
        String codigo_mensajero = pedido.getCodigo_mensajero();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN)
                .child(Constantes.BD_MENSAJERO).child(codigo_mensajero);

        final Query query1 = data.orderByChild(Constantes.BD_CODIGO);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    Mensajeros mensajero = dataSnapshot.getValue(Mensajeros.class);
                    String nombre = mensajero.getNombre();
                    String placa = mensajero.getPlaca();
                    final String numero = mensajero.getTelefono();
                    String codigo_mensajero = mensajero.getCodigo();

                    String pathFoto ="movil_"+codigo_mensajero;


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageReference = storage.getReference().child("mensajeros")
                            .child("mensajero_carro").child(pathFoto).child("foto_perfil");

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(MainActivity.this)
                                    .load(storageReference)
                                    .into(img_carrera_activa);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("storage", "nulo");
                            img_carrera_activa.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                        }
                    });
                    texto_confirmacion_carrera_activa.setText(nombre);
                    ratingMensajero.setVisibility(View.VISIBLE);
                    ratingMensajero.setRating(mensajero.getCalificacion());
                    texto_placa_carrera_activa.setText(placa);
                    texto_valor.setText(numero);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_llamada, 0);
                    }
                    texto_valor.setTextColor(getResources().getColor(R.color.colorPrimary));
                    texto_valor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + numero));

                            int permissionCheck = ContextCompat.checkSelfPermission(
                                    MainActivity.this, Manifest.permission.CALL_PHONE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 225);
                            } else {
                                Log.i("Mensaje", "Se tiene permiso!");
                                startActivity(intent);
                            }
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    query1.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void servicioCancelado(){
        BuscarMensajeroBar.setVisibility(View.INVISIBLE);
        actualizarUI(texto_direcciones, direccionInicial, direccionFinal, Ecomentario, null);
        layout_solicitudes.setVisibility(View.INVISIBLE);
        layout_botones.setVisibility(View.INVISIBLE);
        layout_confirmar_carrera.setVisibility(View.INVISIBLE);
        ratingMensajero.setVisibility(View.INVISIBLE);
        texto_placa_carrera_activa.setText(Constantes.BD_MENSAJERO);
        texto_valor.setTextColor(getResources().getColor(R.color.colorBlanco));
        texto_tiempo.setText(null);
        if(fragmentChat.isAdded()){
            appbarboton.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.remove(fragmentChat);
            ft.commit();
        }
        View v = findViewById(R.id.mapa);
        v.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));
        view_inicio.setVisibility(View.VISIBLE);
        appbarboton.setVisibility(View.VISIBLE);
        confirmar_carro.setText(Constantes.CONFIRMAR_SOLICITUD);
        confirmar_carro.setEnabled(true);
        IdPedidoEnCurso = null;
        fabChat.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            texto_valor.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("El servicio ha sido cancelado");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        queryEstadoPedido.removeEventListener(listenerestadoServicioMoto);
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };

    private void onNetworkChange(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                Log.d("MenuActivity", "CONNECTED");
                if(isOnline(this)){
                    no_internet.setVisibility(View.INVISIBLE);
                }

            } else {
                Log.d("MenuActivity", "DISCONNECTED");
                no_internet.setVisibility(View.VISIBLE);
            }
        }
    }

    private void verificarDomiciliosEnCurso(String id_usuario){
        final List<Domicilio> domis = new ArrayList<>();
        final TextView domi_en_curso = (TextView)navigationView.getMenu().findItem(R.id.nav_mis_domicilios_en_curso).getActionView();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child(Constantes.BD_GERENTE).child(Constantes.BD_ADMIN).child(Constantes.BD_DOMICILIO);
        Log.i("menuitemview", "start listener");

        Query query = database.orderByChild(Constantes.BD_ID_USUARIO).equalTo(id_usuario).limitToLast(100);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                domis.clear();
                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot s:
                            dataSnapshot.getChildren()) {
                        Domicilio domi = s.getValue(Domicilio.class);
                        if (domi != null && !domi.getEstado().equals(Constantes.ESTADO_DOMICILIO_CANCELADO)
                                && !domi.getEstado().equals(Constantes.ESTADO_DOMICILIO_ENTREGAGO)){
                            domis.add(domi);
                        }
                    }
                    if(domis.size() > 0){
                        domi_en_curso.setGravity(Gravity.CENTER_VERTICAL);
                        domi_en_curso.setTypeface(null, Typeface.BOLD);
                        domi_en_curso.setText(" "+ domis.size());
                    } else {
                        domi_en_curso.setGravity(Gravity.CENTER_VERTICAL);
                        domi_en_curso.setTypeface(null, Typeface.BOLD);
                        domi_en_curso.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        domi_en_curso.setText("");
                    }
                } else {
                    domi_en_curso.setGravity(Gravity.CENTER_VERTICAL);
                    domi_en_curso.setTypeface(null, Typeface.BOLD);
                    domi_en_curso.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    domi_en_curso.setText("");
                    Log.i("menuitemview", "error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("menuitemview", "error");
            }
        });
    }
}





















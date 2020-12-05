package com.mensajero.equipo.mensajero.Fragmentos;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.mensajero.equipo.mensajero.Constantes.AdapterOferta;
import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.Constantes.Domicilio;
import com.mensajero.equipo.mensajero.Constantes.Empresa;
import com.mensajero.equipo.mensajero.Constantes.Oferta;
import com.mensajero.equipo.mensajero.Constantes.Pedidos;
import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DialogConfirmarDomi extends DialogFragment {

    public static final String TAG = "DialogCondirmardomi";
    private Button conf;
    private TextView valor_domi, valor_pedido, valor_total, def_pedido;
    private TextInputEditText desc;
    private View.OnClickListener click_conf;
    private double valor_domicilio;
    private String categoria_empresa;
    private AdapterOferta adapterOferta;
    private MainActivity main;
    private Empresa empresa;

    public DialogConfirmarDomi(String categoria_empresa, AdapterOferta adapterOferta, MainActivity main, Empresa empresa, double valor_domicilio) {
        this.categoria_empresa = categoria_empresa;
        this.adapterOferta = adapterOferta;
        this.main = main;
        this.empresa = empresa;
        this.valor_domicilio = valor_domicilio;
    }

    public DialogConfirmarDomi() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_confirmar_domi, container);
        conf = v.findViewById(R.id.conf_domi_comfirmar_btn);
        valor_domi = v.findViewById(R.id.conf_domi_valordomicilio);
        valor_pedido = v.findViewById(R.id.comf_domi_valorpedido);
        valor_total = v.findViewById(R.id.conf_domi_total);
        desc = v.findViewById(R.id.conf_domi_descripcion);
        def_pedido = v.findViewById(R.id.text_desc_domi_conf);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (categoria_empresa.equals(Constantes.RESTAURANTE)){
            final Domicilio domi = crearDomicilio(main, adapterOferta, empresa);
            valor_domi.setText("$ " + domi.getValor_domicilio());
            valor_pedido.setText("$ " + domi.getValor_pedido());
            double total = domi.getValor_pedido() + domi.getValor_domicilio();
            valor_total.setText("$ " + total);
            def_pedido.setText(domi.getDescripcion());
            click_conf = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(desc.getText() != null)domi.setDescripcion(domi.getDescripcion() + desc.getText().toString());
                    if(domi.getDescripcion() != null){
                        pedirDomicilio(domi);
                    } else {
                        Toast.makeText(getContext(),"pide algo!",Toast.LENGTH_LONG).show();
                    }
                    Log.i(TAG,"click conf");
                }
            };
        }else {
            final Pedidos domi = crearMandadoCompras(main, adapterOferta, empresa);
            double precio = 0.0;
            for (int i = 0; i < adapterOferta.ofertas_agregadas.size(); i++) {
                precio += adapterOferta.ofertas_agregadas.get(i).getPrecio();
            }
            valor_domi.setText("$ " + domi.getValor_pedido());
            valor_pedido.setText("$ " + precio);
            double total = domi.getValor_pedido() + precio;
            valor_total.setText("$ " + total);
            def_pedido.setText(Html.fromHtml(domi.getComentario()));
            click_conf = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(desc.getText() != null)domi.setComentario(domi.getComentario() + desc.getText().toString());
                    if(!domi.getComentario().isEmpty()){
                        pedirMensajeroCompras(domi);
                    } else {
                        Toast.makeText(getContext(),"pide algo!",Toast.LENGTH_LONG).show();
                    }

                }
            };
        }

        conf.setOnClickListener(click_conf);

    }
    public Domicilio crearDomicilio(MainActivity main, AdapterOferta adapterOferta, Empresa empresa){
        if(main == null) {
            return new Domicilio();
        }
        String descripcion = "";
        double precio = 0.0;
        for (int i = 0; i < adapterOferta.ofertas_agregadas.size(); i++) {
            descripcion += " * " + adapterOferta.ofertas_agregadas.get(i).getCantidad() + " " +
                    adapterOferta.ofertas_agregadas.get(i).getTitulo() + " \n";
            precio += adapterOferta.ofertas_agregadas.get(i).getPrecio();
        }

        Domicilio domicilio = new Domicilio();
        domicilio.setCiudad(empresa.getCiudad());
        domicilio.setDir_entrega(main.DireccionInicial);
        domicilio.setDir_compra(empresa.getDireccion_empresa());
        domicilio.setEstado(Constantes.ESTADO_DOMICILIO_PENDIENTE);
        SimpleDateFormat fechaDomi = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        domicilio.setDate(calendar.getTimeInMillis());
        domicilio.setFecha_domicilio(fechaDomi.format(calendar.getTime()));
        domicilio.setForma_de_pago(Constantes.EFECTIVO);
        domicilio.setId_usuario(main.id_user);
        domicilio.setLat_dir_entrega(main.pocisionInicial.latitude);
        domicilio.setLong_dir_entrega(main.pocisionInicial.longitude);
        domicilio.setLat_dir_compra(empresa.getLatitud());
        domicilio.setLong_dir_compra(empresa.getLongitud());
        domicilio.setNombre(main.nombre);
        domicilio.setTelefono(main.telefono);
        domicilio.setToken(main.token);
        domicilio.setToken_empresa(empresa.getToken());
        domicilio.setValor_pedido(precio);
        domicilio.setValor_domicilio(valor_domicilio);
        domicilio.setEmpresa(empresa);
        domicilio.setDescripcion(descripcion);


        Log.i("ver empresa", descripcion);
        return domicilio;
    }

    private Pedidos crearMandadoCompras(MainActivity main, AdapterOferta adapterOferta, Empresa empresa) {
        main = (MainActivity)getActivity();
        if(main == null) {
            return new Pedidos();
        }
        String descripcion = "";
        double precio = 0.0;
        for (int i = 0; i < adapterOferta.ofertas_agregadas.size(); i++) {
            if(i == 0)descripcion = "Comprar en " + empresa.getNombre()+ ": <br> ";
            descripcion += "<b> * " + adapterOferta.ofertas_agregadas.get(i).getCantidad() + " " +
                    adapterOferta.ofertas_agregadas.get(i).getTitulo() + "</b><br>";
            precio += adapterOferta.ofertas_agregadas.get(i).getPrecio();
            if(i == adapterOferta.ofertas_agregadas.size()-1)descripcion += "valor de la compra $" + precio;
        }
        Pedidos pedido = new Pedidos();
        pedido.setCiudad(empresa.getCiudad());
        pedido.setTipo_pedido(Constantes.COMPRAS_PEDIR_DOMICILIOS);
        pedido.setDir_inicial(empresa.getDireccion_empresa());
        pedido.setDir_final(main.DireccionInicial);
        SimpleDateFormat fechaDomi = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        pedido.setEstado_pedido(Constantes.BD_ESTADO_PENDIENTE);
        pedido.setDate(calendar.getTimeInMillis());
        pedido.setFecha_pedido(fechaDomi.format(calendar.getTime()));
        pedido.setForma_de_pago(Constantes.EFECTIVO);
        pedido.setId_usuario(main.id_user);
        pedido.setLat_dir_final(main.pocisionInicial.latitude);
        pedido.setLong_dir_final(main.pocisionInicial.longitude);
        pedido.setLat_dir_inicial(empresa.getLatitud());
        pedido.setLong_dir_final(empresa.getLongitud());
        pedido.setNombre(main.nombre);
        pedido.setTelefono(main.telefono);
        pedido.setToken(main.token);
        pedido.setValor_pedido(valor_domicilio); //en este caso es el valor del flete o del pago al mensajero
        pedido.setCuantos_mensajeros(1);
        pedido.setComentario(descripcion);
        Log.i("crear mandadocompras", descripcion);

        return pedido;
    }

    public void pedirDomicilio(@NotNull final Domicilio domicilio){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Constantes.BD_GERENTE)
                .child(Constantes.BD_ADMIN).child(Constantes.BD_DOMICILIO);
        String id_domi = database.push().getKey();
        if (id_domi != null){
            domicilio.setId_domicilio(id_domi);
            database.child(id_domi).setValue(domicilio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.i(TAG, "domicilio agregado correctamente");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Domicilios_en_curso frag= new Domicilios_en_curso(domicilio.getId_usuario());
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.fragment_ver_empresa,frag,Domicilios_en_curso.TAG);
                        ft.commit();
                        getDialog().dismiss();
                    } else {
                        Log.i(TAG, "domicilio no agregado");
                    }
                }
            });
        } else {
            Log.i(TAG, "id_domi null");
        }
    }

    public void pedirMensajeroCompras(@NotNull final Pedidos pedido){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Constantes.BD_GERENTE)
                .child(Constantes.BD_ADMIN).child(Constantes.BD_PEDIDO);
        String id_domi = database.push().getKey();
        if (id_domi != null){
            pedido.setId_pedido(id_domi);
            database.child(id_domi).setValue(pedido).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.i(TAG, "pedido agregado correctamente");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Domicilios_en_curso frag= new Domicilios_en_curso(pedido.getId_usuario());
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.fragment_ver_empresa,frag,Domicilios_en_curso.TAG);
                        ft.commit();
                        getDialog().dismiss();
                    } else {
                        Log.i(TAG, "pedido no agregado");
                    }
                }
            });
        } else {
            Log.i(TAG, "id_domi null");
        }
    }

}

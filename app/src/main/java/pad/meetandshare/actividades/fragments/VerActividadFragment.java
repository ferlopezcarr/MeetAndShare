package pad.meetandshare.actividades.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Category;
import pad.meetandshare.actividades.utils.FechaUtil;
import pad.meetandshare.negocio.modelo.User;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.MyCallBack;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;

public class VerActividadFragment extends Fragment implements View.OnClickListener {

    public static final String ACTIVIDAD = "Actividad";
    public static final String NOMBRE_USUARIO = "nombreUsuario";

    private TextView tvNombre;
    private TextView tvFechaIni;
    private TextView tvHoraIni;
    private TextView tvFechaFin;
    private TextView tvHoraFin;
    private TextView tvMaxParticipantes;
    private TextView tvPlazasLibres;
    private TextView tvNombreAdmin;
    private TextView tvEstado;
    private TextView tvDescripcion;
    private Button ubicacionBoton;
    private Button inscribirseBoton;
    private Button verUsuariosInscritosBoton;
    private FloatingActionButton modificarActividadBoton;
    private Button borrarActividad;

    private List<User> usuarios;


    private Actividad actividad;
    private String nombreUsuario;

    private View rootView;

    private LayoutInflater miInflater;


    public VerActividadFragment() {
        // Required empty public constructor
    }

    public static VerActividadFragment newInstance(Actividad actividad, String nombreUsuario) {
        VerActividadFragment fragment = new VerActividadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACTIVIDAD, actividad);
        bundle.putString(NOMBRE_USUARIO, nombreUsuario);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            actividad = (Actividad) bundle.getSerializable(ACTIVIDAD);
            nombreUsuario = bundle.getString(NOMBRE_USUARIO);
        }
    }

    // ON CREATE VIEW ---------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ver_actividad, container, false);
        miInflater = inflater;

        initViewElems();


        ( rootView.findViewById(R.id.scrollVerActividad)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                (rootView.findViewById(R.id.descripcionVerActividad)).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        (rootView.findViewById(R.id.descripcionVerActividad)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ( rootView.findViewById(R.id.descripcionVerActividad)).getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return rootView;
    }

    private void initViewElems() {
        tvNombre = ((TextView) rootView.findViewById(R.id.nombreVerActividad));

        tvFechaIni = ((TextView) rootView.findViewById(R.id.fechaIniVerActividad));
        tvHoraIni = ((TextView) rootView.findViewById(R.id.horaIniVerActividad));
        tvFechaFin = ((TextView) rootView.findViewById(R.id.fechaFinVerActividad));
        tvHoraFin = ((TextView) rootView.findViewById(R.id.horaFinVerActividad));
        tvMaxParticipantes = ((TextView) rootView.findViewById(R.id.maxParticipantesVerActividad));
        tvPlazasLibres = ((TextView) rootView.findViewById(R.id.plazasLibresVerActividad));
        tvNombreAdmin = ((TextView) rootView.findViewById(R.id.administradorVerActividad));
        tvEstado = ((TextView) rootView.findViewById(R.id.finalizadaVerActividad));
        tvDescripcion = ((TextView) rootView.findViewById(R.id.descripcionVerActividad));


        ubicacionBoton = ((Button) rootView.findViewById(R.id.ver_ubicacion));
        ubicacionBoton.setOnClickListener(this);

        tvDescripcion.setMovementMethod(new ScrollingMovementMethod());

        inscribirseBoton = ((Button) rootView.findViewById(R.id.inscribirse));
        verUsuariosInscritosBoton = ((Button) rootView.findViewById(R.id.ver_usuarios_inscritos));
        verUsuariosInscritosBoton.setOnClickListener(this);

        boolean inscribirseOK = false;

        //si el usuario NO esta inscrito en la actividad y esta activa
        inscribirseOK = (
                !actividad.getRegisteredUserIds().contains(AutorizacionFirebase.getCurrentUser().getUid())
                        && actividad.getActive());

        if(inscribirseOK)
            inscribirseBoton.setOnClickListener(this);
        else
            inscribirseBoton.setVisibility(View.GONE);

        modificarActividadBoton =  rootView.findViewById(R.id.editaActividad);
        borrarActividad =  rootView.findViewById(R.id.eliminar_actividad);

        boolean modificarOK = false;
        boolean borrarOK = false;

        //si el admin eres tu
        if(actividad.getAdminUid().equalsIgnoreCase(AutorizacionFirebase.getUser().getUid())) {
            if(actividad.getActive()) {//y la actividad esta activa
                //si ya ha empezado (o terminado)
                modificarOK = (actividad.getStartDate().after(new Date()));
                borrarOK = true;
            }
        }

        if(modificarOK)
            modificarActividadBoton.setOnClickListener(this);
        else
            modificarActividadBoton.setVisibility(View.GONE);

        if(borrarOK)
            borrarActividad.setOnClickListener(this);
        else
            borrarActividad.setVisibility(View.GONE);
    }
    //-------------------------

    @Override
    public void onStart() {
        super.onStart();

        usuarios= new ArrayList<User>();
        traeUsuariosActividad();

        String fechaIni = FechaUtil.getDateWithHourFormat().format(actividad.getStartDate());
        String arrayFechaIni[] = fechaIni.split(" ");

        String fechaFin = FechaUtil.getDateWithHourFormat().format(actividad.getEndDate());
        String arrayFechaFin[] = fechaFin.split(" ");

        Integer maxParticipantes = actividad.getMaxRegistered();

        //Nombre
        tvNombre.setText(actividad.getName());

        //FechaIni
        tvFechaIni.setText(arrayFechaIni[0]);
        //HoraIni
        tvHoraIni.setText(arrayFechaIni[1]);

        //FechaFin
        tvFechaFin.setText(arrayFechaFin[0]);
        //HoraFin
        tvHoraFin.setText(arrayFechaFin[1]);

        //Max Participantes
        tvMaxParticipantes.setText(maxParticipantes.toString());

        //Plazas libres
        Integer plazasLibres = maxParticipantes - actividad.getRegisteredUserIds().size();
        if(plazasLibres <= 0) {
            tvPlazasLibres.setText("-");
        }
        else {//si hay plazas libres
            tvPlazasLibres.setText(plazasLibres.toString());
        }

        //NombreAdmin
        String nombreAdmin = nombreUsuario;
        //si el adminstrador es el usuario de la sesion
        if(actividad.getAdminUid().equalsIgnoreCase(AutorizacionFirebase.getCurrentUser().getUid())) {
            nombreAdmin = "Tú";
        }
        tvNombreAdmin.setText(nombreAdmin);

        //Estado
        String estado = "";
        if (actividad.activityFinished()) {
            estado = "Finalizada";
        } else {//no finalizada
            if(actividad.getActive()) {
                estado = "Activa";
            }
            else {
                estado = "Cancelada";
            }
        }
        tvEstado.setText(estado);

        //Categorias
        if(!actividad.getActive()){
            TextView cancelado = (TextView) miInflater.inflate(R.layout.layout_interes, null);
            cancelado.setText("Cancelado");
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cancelado.setLayoutParams(llp);
            ((LinearLayout) rootView.findViewById(R.id.containercancelado)).addView(cancelado);
        }
        if(actividad.getCategories() != null) {
            for (Category interes : actividad.getCategories()) {
                TextView interesVista = (TextView) miInflater.inflate(R.layout.layout_interes, null);
                interesVista.setText(interes.getDisplayName());
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                llp.setMargins(0, 20, 0, 0); // llp.setMargins(left, top, right, bottom);
                interesVista.setLayoutParams(llp);
                ((LinearLayout) rootView.findViewById(R.id.containerInteresesActividad)).addView(interesVista);
            }
        }

        //Descripción
        if(actividad.getDescription() != null) {
            if(actividad.getDescription().length() != 0) {
                tvDescripcion.setText(actividad.getDescription());
            }
            else {
                rootView.findViewById(R.id.labelDescripcion).setVisibility(View.GONE);
                rootView.findViewById(R.id.descripcionVerActividad).setVisibility(View.GONE);
            }
        }
        else {
            rootView.findViewById(R.id.labelDescripcion).setVisibility(View.GONE);
            rootView.findViewById(R.id.descripcionVerActividad).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ver_ubicacion:
                verUbicacion();
                break;

            case R.id.inscribirse:
                inscribirse();
                recargarVista();
                break;

            case R.id.ver_usuarios_inscritos:
                verUsuariosInscritos();
                break;

            case R.id.editaActividad:
                changeToModificarActividad();
                break;

            case R.id.eliminar_actividad:
                eliminarActividad();
                break;

        }
    }

    private void verUbicacion() {

        Fragment fr = InicioFragment.newInstance(actividad.getUbication());
        pad.meetandshare.actividades.FragmentTransaction fc=(pad.meetandshare.actividades.FragmentTransaction) this.getActivity();
        fc.replaceFragment(fr);

    }

    private void inscribirse() {
        String uidUsuario = AutorizacionFirebase.getCurrentUser().getUid();

        if(actividad.addRegisteredUser(uidUsuario)) {
            SAActividad saActividad = new SAActividadImp();
            saActividad.save(actividad);

            final SAUsuario saUsuario = new SAUsuarioImp();
            saUsuario.get(uidUsuario, new MyCallBack() {
                @Override
                public void onCallbackUsuario(User value) {
                    value.addRegisteredActivityId(actividad.getUid());
                    saUsuario.save(value);
                }

                @Override
                public void onCallbackActividad(Actividad actividad) { }

                @Override
                public void onCallbackActividadAll(ArrayList<Actividad> actividad) { }
            });

            Toast.makeText(getActivity(), "¡Te has inscrito a la actividad '"+actividad.getName()+"'!", Toast.LENGTH_LONG).show();
            inscribirseBoton.setVisibility(View.GONE);
        }
        else {//si ya contiene al usuario
            Toast.makeText(getActivity(), "¡Ya estás inscrito!", Toast.LENGTH_LONG).show();
        }
    }

    private void verUsuariosInscritos() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Usuarios inscritos");

        String usersNames= "";
        for(User user: usuarios){
            usersNames += user.getName()+"\n";
        }
        usersNames += "\n";
        usersNames += "Total: " + String.valueOf(usuarios.size());

        alertDialog.setMessage(usersNames);

        // Alert dialog button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Alert dialog action goes here
                    // onClick button code here
                    dialog.dismiss();// use dismiss to cancel alert dialog
                }
            });
        alertDialog.show();
    }

    private void changeToModificarActividad() {

        Fragment fragmento = ModificaActividadFragment.newInstance(actividad, actividad.getRegisteredUserIds().size());

        FragmentManager fm = this.getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ContenedorMenuLateral, fragmento);
        ft.addToBackStack(null);

        ft.commit();
    }


    private void eliminarActividad(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.titulo);
        builder.setMessage(R.string.cuerpo);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),R.string.borradoConfirm , Toast.LENGTH_SHORT).show();
                SAActividad saActividad = new SAActividadImp();
                saActividad.delete(actividad);
                recargarVista();
            }
        });

        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void traeUsuariosActividad(){

        SAUsuario saUsuario = new SAUsuarioImp();

        for(String id : actividad.getRegisteredUserIds()){

            saUsuario.get(id, new MyCallBack() {
                @Override
                public void onCallbackUsuario(User user) {
                    usuarios.add(user);
                }

                @Override
                public void onCallbackActividad(Actividad actividad) { }

                @Override
                public void onCallbackActividadAll(ArrayList<Actividad> actividad) { }
            });
        }
    }

    private void recargarVista() {
        
        Fragment fr = VerActividadFragment.newInstance(actividad, nombreUsuario);
        pad.meetandshare.actividades.FragmentTransaction fc=(pad.meetandshare.actividades.FragmentTransaction) this.getActivity();
        fc.replaceFragment(fr);
    }
}
package pad.meetandshare.actividades;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.MyCallBack;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;

public class VerActividadFragment extends Fragment implements View.OnClickListener {

    public static final String ACTIVIDAD = "Actividad";
    public static final String NOMBRE_USUARIO = "nombreUsuario";

    private String uidActividad;
    private Actividad actividad;
    private String nombreUsuario;
    private View rootView;
    private LayoutInflater miInflater;
    private Button ubicacionBoton;
    private Button inscribirseBoton;


    public VerActividadFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ver_actividad, container, false);
        miInflater = inflater;

        ubicacionBoton = ((Button) rootView.findViewById(R.id.ver_ubicacion));
        ubicacionBoton.setOnClickListener(this);

        inscribirseBoton = ((Button) rootView.findViewById(R.id.inscribirse));
        inscribirseBoton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        String fechaIni = FechaUtil.getDateWithHourFormat().format(actividad.getFechaInicio());
        String arrayFechaIni[] = fechaIni.split(" ");

        String fechaFin = FechaUtil.getDateWithHourFormat().format(actividad.getFechaFin());
        String arrayFechaFin[] = fechaFin.split(" ");

        Integer maxParticipantes = actividad.getMaxParticipantes();

        //Nombre actividad
        ((TextView) rootView.findViewById(R.id.nombreVerActividad)).setText(actividad.getNombre());

        //FechaIni
        ((TextView) rootView.findViewById(R.id.fechaIniVerActividad)).setText(arrayFechaIni[0]);
        //HoraIni
        ((TextView) rootView.findViewById(R.id.horaIniVerActividad)).setText(arrayFechaIni[2]);

        //FechaFin
        ((TextView) rootView.findViewById(R.id.fechaFinVerActividad)).setText(arrayFechaFin[0]);
        //HoraFin
        ((TextView) rootView.findViewById(R.id.horaFinVerActividad)).setText(arrayFechaFin[2]);

        //Max Participantes
        ((TextView) rootView.findViewById(R.id.maxParticipantesVerActividad)).setText(maxParticipantes.toString());

        //Plazas libres
        Integer plazasLibres = maxParticipantes - actividad.getIdUsuariosInscritos().size();
        if(plazasLibres <= 0) {
            ((TextView) rootView.findViewById(R.id.plazasLibresVerActividad)).setText("-");
        }
        else {//si hay plazas libres
            ((TextView) rootView.findViewById(R.id.plazasLibresVerActividad)).setText(plazasLibres.toString());
        }

        //NombreAdmin
        ((TextView) rootView.findViewById(R.id.administradorVerActividad)).setText(nombreUsuario);

        //Estado
        String estado = "";
        if (actividad.getFinalizada()) {
            estado = "Finalizada";
        } else {//no finalizada
            estado = "Activa";
        }
        ((TextView) rootView.findViewById(R.id.finalizadaVerActividad)).setText(estado);

        //Categorias
        if(actividad.getCategorias() != null) {
            for (Categoria interes : actividad.getCategorias()) {
                TextView interesVista = (TextView) miInflater.inflate(R.layout.layout_interes, null);
                interesVista.setText(interes.getDisplayName());
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                llp.setMargins(0, 20, 0, 0); // llp.setMargins(left, top, right, bottom);
                interesVista.setLayoutParams(llp);
                ((LinearLayout) rootView.findViewById(R.id.containerInteresesActividad)).addView(interesVista);
            }
        }

        //Descripción
        if(actividad.getDescripcion() != null)
            ((TextView) rootView.findViewById(R.id.descripcionVerActividad)).setText(actividad.getDescripcion());

        //Boton de inscribirse
        //si el usuario esta inscrito en la actividad
        if(actividad.getIdUsuariosInscritos().contains(AutorizacionFirebase.getCurrentUser().getUid())) {
            inscribirseBoton.setVisibility(View.GONE);
        }
        else {//si no esta inscrito
            inscribirseBoton.setVisibility(View.VISIBLE);
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
                break;
        }
    }

    private void verUbicacion() {

    }

    private void inscribirse() {
        if(actividad.addUsuario(AutorizacionFirebase.getCurrentUser().getUid())) {
            SAActividad saActividad = new SAActividadImp();
            saActividad.save(actividad, actividad.getUid());
        }
        else {//si ya contiene al usuario
            Toast.makeText(getActivity(), "¡Ya estás inscrito!", Toast.LENGTH_LONG).show();

        }
    }


}
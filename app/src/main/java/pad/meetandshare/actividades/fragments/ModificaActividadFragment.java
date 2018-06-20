package pad.meetandshare.actividades.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.FechaUtil;
import pad.meetandshare.negocio.modelo.Ubicacion;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;

import static android.app.Activity.RESULT_OK;
import static pad.meetandshare.negocio.modelo.Ubicacion.PLACE_PICKER_REQUEST;
import static pad.meetandshare.negocio.modelo.Ubicacion.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class ModificaActividadFragment extends Fragment implements View.OnClickListener {

    //NOMBRE
    private EditText etNombre;
    //FECHA INI
    private EditText etFechaIni;
    private ImageButton ibObtenerFechaIni;
    private EditText etHoraIni;
    private ImageButton ibObtenerHoraIni;
    //FECHA FIN
    private EditText etFechaFin;
    private ImageButton ibObtenerFechaFin;
    private EditText etHoraFin;
    private ImageButton ibObtenerHoraFin;
    //MAX PARTICIPANTES
    private EditText etMaxParticipantes;
    //INTERESES BUTTON
    private Button interesesBoton;
    //UBICACION BOTON
    private Button ubicacionBoton;
    //DESCRIPCION
    private EditText etDescripcion;
    //MODIFICAR ACTIVIDAD
    private Button modificarActividadBoton;

    private Ubicacion ubicacionSeleccionada;

    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();

    private static final String ACTIVIDAD = "actividad";
    private Actividad actividad;
    private SAActividad saActividad;

    private ValueEventListener eventListener;
    private View rootView;
    private LayoutInflater miInflater;

    private boolean btnModificarActividadPressed = false;
    private boolean actividadModificada = false;


    public ModificaActividadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param act Actividad.
     * @return A new instance of fragment ModificaActividadFragment.
     */
    public static ModificaActividadFragment newInstance(Actividad act) {
        ModificaActividadFragment fragment = new ModificaActividadFragment();
        Bundle args = new Bundle();
        args.putSerializable(ACTIVIDAD, act);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actividad = (Actividad) getArguments().getSerializable(ACTIVIDAD);
        }

        listItems = Categoria.getArray();
        checkedItems = new boolean[listItems.length];

        List<Categoria> lista = actividad.getCategorias();

        int k=0;
        for(int i=0; i< Categoria.getArray().length && k < lista.size();++i){
            if(lista.get(k).getDisplayName()==Categoria.getArray()[i]){
                checkedItems[i]=true;
                k++;
            }
        }

        saActividad = new SAActividadImp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_modifica_actividad, container, false);
        miInflater = inflater;

        initViewElems();

        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return rootView;
    }

    private void initViewElems() {

        //NOMBRE
        etNombre = ((EditText) rootView.findViewById(R.id.nombreModificarActividad));
        etNombre.setText(actividad.getNombre());

        //FECHA INI
        //Widget EditText donde se mostrara la fecha obtenida
        etFechaIni = (EditText) rootView.findViewById(R.id.fechaIniModificarActividad);
        etFechaIni.setText(FechaUtil.getDateFormat().format(actividad.getFechaInicio()));
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFechaIni = (ImageButton) rootView.findViewById(R.id.ib_obtener_fechaIniModificar);
        ibObtenerFechaIni.setOnClickListener(this);

        //HORA INI
        //Widget EditText donde se mostrara la hora obtenida
        etHoraIni = (EditText) rootView.findViewById(R.id.horaIniModificarActividad);
        etHoraIni.setText(FechaUtil.horaMostrarString(actividad.getFechaInicio()));
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHoraIni = (ImageButton) rootView.findViewById(R.id.ib_obtener_horaIniModificar);
        //Evento setOnClickListener - clic
        ibObtenerHoraIni.setOnClickListener(this);

        if(actividad.getFechaInicio().before(new Date())) {//si ya ha empezado
            etFechaIni.setEnabled(false);
            etHoraIni.setEnabled(false);
        }

        //FECHA FIN
        //Widget EditText donde se mostrara la fecha obtenida
        etFechaFin = (EditText) rootView.findViewById(R.id.fechaFinModificarActividad);
        etFechaFin.setText(FechaUtil.getDateFormat().format(actividad.getFechaFin()));
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFechaFin = (ImageButton) rootView.findViewById(R.id.ib_obtener_fechaFinModificar);
        ibObtenerFechaFin.setOnClickListener(this);

        //HORA FIN
        //Widget EditText donde se mostrara la hora obtenida
        etHoraFin = (EditText) rootView.findViewById(R.id.horaFinModificarActividad);
        etHoraFin.setText(FechaUtil.horaMostrarString(actividad.getFechaFin()));
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHoraFin = (ImageButton) rootView.findViewById(R.id.ib_obtener_horaFinModificar);
        //Evento setOnClickListener - clic
        ibObtenerHoraFin.setOnClickListener(this);

        //MAX PARTICIPANTES
        etMaxParticipantes = (EditText) rootView.findViewById(R.id.maxParticipantesModificarActividad);
        etMaxParticipantes.setText(String.valueOf(actividad.getMaxParticipantes()));

        //INTERESES
        interesesBoton = (Button) rootView.findViewById(R.id.botonInteresModificarActividad);
        listenerButtonIntereses(interesesBoton);

        //UBICACION
        ubicacionBoton = (Button) rootView.findViewById(R.id.botonSeleccionarUbicacionModificarActividad);
        ubicacionBoton.setOnClickListener(this);

        //DESCRIPCION
        etDescripcion = (EditText) rootView.findViewById(R.id.descripcionModificarActividad);
        etDescripcion.setText(actividad.getDescripcion());

        //MODIFICAR ACTIVIDAD
        modificarActividadBoton = (Button) rootView.findViewById(R.id.modificarActividadPost);
        modificarActividadBoton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FechaUtil fechaUtil = new FechaUtil();

        switch (v.getId()) {
            case R.id.ib_obtener_fechaIniModificar:
                if(actividad.getFechaInicio().before(new Date())) {
                    String toastMsg = String.format("¡La actividad ya ha empezado!");
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                }
                else if(etFechaIni.isEnabled()){
                    fechaUtil.obtenerFecha(getActivity(), R.id.fechaIniModificarActividad);
                }
                break;

            case R.id.ib_obtener_horaIniModificar:
                if(actividad.getFechaInicio().before(new Date())) {
                    String toastMsg = String.format("¡La actividad ya ha empezado!");
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                }
                else if(etHoraIni.isEnabled()){
                    fechaUtil.obtenerHora(getActivity(), R.id.horaIniModificarActividad);
                }
                break;

            case R.id.ib_obtener_fechaFinModificar:
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaFinModificarActividad);
                break;

            case R.id.ib_obtener_horaFinModificar:
                fechaUtil.obtenerHora(getActivity(), R.id.horaFinModificarActividad);
                break;

            case R.id.botonSeleccionarUbicacionModificarActividad:
                obtenerUbicacion();
                break;

            case R.id.modificarActividadPost:
                modificarActividad();
                this.btnModificarActividadPressed = true;
                break;
        }
    }

    private void listenerButtonIntereses(Button intereses) {

        interesesBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle(R.string.intereses);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                    }
                });

                mBuilder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    // PERMISOS UBICACION --------
    public void obtenerUbicacion() {

        String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(
                this.getActivity(), accessFineLocation)
                != PackageManager.PERMISSION_GRANTED) {

            //si no tiene permisos y debe pedirlos
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), accessFineLocation)) {

                ActivityCompat.requestPermissions(
                        this.getActivity(),
                        new String[]{accessFineLocation},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {//si no debe pedirlos
                renderPlacePicker(this.getActivity());
            }
        } else {//si tiene permisos
            renderPlacePicker(this.getActivity());
        }
    }

    // PERMISOS UBICACION --------
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //si se conceden los permisos
                    renderPlacePicker(this.getActivity());
                } else {
                    String toastMsg = String.format("Para seleccionar la ubicación debes aceptar los permisos");
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void renderPlacePicker(Activity activity) {
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this.getActivity()), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            String msg = "Google Play Services no esta disponible en este momento";
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this.getActivity());

                ubicacionSeleccionada = new Ubicacion(place);

                String toastMsg = String.format("Ubicación seleccionada satisfactoriamente");
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
// ---------------------------

    @Override
    public void onStart() {
        super.onStart();

        /*
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(saActividad.checkActividad(actividad, dataSnapshot)) {
                    if (btnModificarActividadPressed && actividadModificada) {
                        modificarActividad();
                        changeToVerActividad();
                    }
                }

            }//onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        saActividad.getDatabaseReference().addValueEventListener(eventListener);
        */
    }

    @Override
    public void onStop() {
        super.onStop();
        if (eventListener != null)
            saActividad.getDatabaseReference().removeEventListener(eventListener);
    }

    private void modificarActividad() {

        if(ubicacionSeleccionada == null)
            ubicacionSeleccionada = actividad.getUbicacion();
        /*
        actividad = Actividad.checkInputActividadModificar(
                this.getActivity(),
                actividad,
                listItems,
                checkedItems,
                actividad.getFechaInicio(),
                actividad.getIdUsuariosInscritos().size(),
                ubicacionSeleccionada,
                AutorizacionFirebase.getUser().getUid(),
                etNombre, etFechaIni, etHoraIni, etFechaFin, etHoraFin, etMaxParticipantes, etDescripcion);
        */
        if (actividad != null) {

            if (AutorizacionFirebase.getUser() != null) {
                saActividad.save(actividad, AutorizacionFirebase.getUser().getUid());
                actividadModificada = true;
                changeToVerActividad();
            } else {
                String toastMsg = String.format("Error usuario logeado no encontrado");
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void changeToVerActividad() {

        Fragment fragmento = VerActividadFragment.newInstance(actividad, AutorizacionFirebase.getUser().getNombre());

        FragmentManager fm = this.getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ContenedorMenuLateral, fragmento);
        ft.addToBackStack(null);

        ft.commit();
    }

}

package pad.meetandshare.actividades.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import pad.meetandshare.R;
import pad.meetandshare.actividades.ParserObjects.ParserActividad;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Category;
import pad.meetandshare.actividades.utils.FechaUtil;
import pad.meetandshare.negocio.modelo.Ubication;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;

import static android.app.Activity.RESULT_OK;
import static pad.meetandshare.negocio.modelo.Ubication.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static pad.meetandshare.negocio.modelo.Ubication.PLACE_PICKER_REQUEST;


public class CrearActividadFragment extends Fragment implements View.OnClickListener {

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
    //CREAR ACTIVIDAD
    private Button crearActividadBoton;

    private Ubication ubicacionSeleccionada;

    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();

    private Actividad actividad;
    private SAActividad saActividad;

    private ValueEventListener eventListener;
    private View rootView;

    private boolean btnCrearActividadPressed = false;
    private boolean actividadCreada = false;

    public CrearActividadFragment() {
        // Required empty public constructor
    }

    public static CrearActividadFragment newInstance() {
        CrearActividadFragment fragment = new CrearActividadFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems = Category.getArray();
        checkedItems = new boolean[listItems.length];

        saActividad = new SAActividadImp();
    }

    // ON CREATE VIEW ------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_crear_actividad,
                container, false);

        if (AutorizacionFirebase.amIAuthentificated()) {//Si el usuario esta logeado
            initViewElems();
        }
        return rootView;
    }

    private void initViewElems() {
        //VISTAS----------------------
        //NOMBRE
        etNombre = ((EditText) rootView.findViewById(R.id.nombreCrearActividad));

        //FECHA INI
        //Widget EditText donde se mostrara la fecha obtenida
        etFechaIni = (EditText) rootView.findViewById(R.id.fechaIniCrearActividad);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFechaIni = (ImageButton) rootView.findViewById(R.id.ib_obtener_fechaIni);
        ibObtenerFechaIni.setOnClickListener(this);

        //HORA INI
        //Widget EditText donde se mostrara la hora obtenida
        etHoraIni = (EditText) rootView.findViewById(R.id.horaIniCrearActividad);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHoraIni = (ImageButton) rootView.findViewById(R.id.ib_obtener_horaIni);
        //Evento setOnClickListener - clic
        ibObtenerHoraIni.setOnClickListener(this);

        //FECHA FIN
        //Widget EditText donde se mostrara la fecha obtenida
        etFechaFin = (EditText) rootView.findViewById(R.id.fechaFinCrearActividad);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFechaFin = (ImageButton) rootView.findViewById(R.id.ib_obtener_fechaFin);
        ibObtenerFechaFin.setOnClickListener(this);

        //HORA FIN
        //Widget EditText donde se mostrara la hora obtenida
        etHoraFin = (EditText) rootView.findViewById(R.id.horaFinCrearActividad);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHoraFin = (ImageButton) rootView.findViewById(R.id.ib_obtener_horaFin);
        //Evento setOnClickListener - clic
        ibObtenerHoraFin.setOnClickListener(this);

        //MAX PARTICIPANTES
        etMaxParticipantes = rootView.findViewById(R.id.maxParticipantesCrearActividad);

        //INTERESES
        interesesBoton = (Button) rootView.findViewById(R.id.botonInteresRegistro);
        listenerButtonIntereses(interesesBoton);

        //UBICACION
        ubicacionBoton = (Button) rootView.findViewById(R.id.botonSeleccionarUbicacion);
        ubicacionBoton.setOnClickListener(this);

        //DESCRIPCION
        etDescripcion = rootView.findViewById(R.id.descripcionCrearActividad);

        //CREAR ACTIVIDAD
        crearActividadBoton = (Button) rootView.findViewById(R.id.crearActividadPost);
        crearActividadBoton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FechaUtil fechaUtil = new FechaUtil();

        switch (v.getId()) {
            case R.id.ib_obtener_fechaIni:
                etFechaIni = fechaUtil.obtenerFecha(getActivity(), etFechaIni);
                break;

            case R.id.ib_obtener_horaIni:
                etHoraIni = fechaUtil.obtenerHora(getActivity(), etHoraIni);
                break;

            case R.id.ib_obtener_fechaFin:
                etFechaFin = fechaUtil.obtenerFecha(getActivity(), etFechaFin);
                break;

            case R.id.ib_obtener_horaFin:
                etHoraFin = fechaUtil.obtenerHora(getActivity(), etHoraFin);
                break;

            case R.id.botonSeleccionarUbicacion:
                obtenerUbicacion();
                break;

            case R.id.crearActividadPost:
                crearActividad();
                this.btnCrearActividadPressed = true;
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


    // ON START ------------------
    @Override
    public void onStart() {
        super.onStart();

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(saActividad.checkActividad(actividad, dataSnapshot)) {
                    String toastMsg = String.format("Error ya has creado una actividad con ese nombre");
                    Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                    btnCrearActividadPressed = false;
                    actividadCreada = false;
                }
                else {
                    if (btnCrearActividadPressed && actividadCreada) {
                        btnCrearActividadPressed = false;
                        actividadCreada = false;
                        changeToVerActividad();
                    }
                }

            }//onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        saActividad.getDatabaseReference().addValueEventListener(eventListener);
    }

    private void changeToVerActividad() {

        Fragment fragmento = VerActividadFragment.newInstance(actividad, AutorizacionFirebase.getUser().getName());

        FragmentManager fm = this.getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ContenedorMenuLateral, fragmento);

        ft.commit();
    }
    // ---------------------------

    @Override
    public void onStop() {
        super.onStop();
        if (eventListener != null)
            saActividad.getDatabaseReference().removeEventListener(eventListener);
    }

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

                ubicacionSeleccionada = new Ubication(place);

                String toastMsg = String.format("Ubicación seleccionada satisfactoriamente");
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    // ---------------------------

    private void crearActividad() {

        actividad = checkInputActividad();

        if (actividad != null) {

            if (AutorizacionFirebase.getUser() != null) {
                saActividad.create(actividad);
                actividadCreada = true;
            } else {
                String toastMsg = String.format("Error usuario logeado no encontrado");
                Toast.makeText(this.getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    public Actividad checkInputActividad() {
        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = etNombre.getText().toString();
        String fechaIniString = etFechaIni.getText().toString();
        String horaIniString = etHoraIni.getText().toString();
        String fechaFinString = etFechaFin.getText().toString();
        String horaFinString = etHoraFin.getText().toString();
        String maxParticipantesString = etMaxParticipantes.getText().toString();
        String descripcion = etDescripcion.getText().toString();

        //PROCESAMIENTO PREVIO
        //QUITAR ESPACIOS AL PRINCIPIO Y FINAL DE CADA INPUT
        nombre = nombre.trim();
        fechaIniString = fechaIniString.trim();
        horaIniString = horaIniString.trim();
        fechaFinString = fechaFinString.trim();
        horaFinString = horaFinString.trim();
        descripcion = descripcion.trim();


        //INICIALIZAR
        boolean unlessOneInteres = false;
        boolean ubicacionOK = false;

        Integer maxParticipantes = 0;
        Date fechaIni = null;
        Date fechaFin = null;

        View focusView = null;
        Actividad act = null;

        // --- CHECKS --- //
        ParserActividad pa = new ParserActividad();

        //NOMBRE
        nombre = pa.procesarNombre(nombre, etNombre, focusView);

        //FECHA INI
        fechaIniString = pa.procesarFechaIniSinHora(fechaIniString, etFechaIni, focusView);

        //HORA INI
        horaIniString = pa.procesarHoraIni(horaIniString, etHoraIni, focusView);

        //FECHA INI Y HORA INI
        fechaIni = pa.procesarFechaIniCompleta(fechaIniString, horaIniString, etHoraIni, focusView);

        //FECHA FIN
        fechaFinString = pa.procesarFechaFinSinHora(fechaFinString, fechaIniString, etFechaFin, focusView);

        //HORA FIN
        horaFinString = pa.procesarHoraFin(horaFinString, etHoraFin, focusView);

        //FECHA FIN Y HORA FIN
        fechaFin = pa.procesarFechaFinCompleta(fechaIni, fechaFinString, horaFinString, etHoraFin, focusView);

        //MAX PARTICIPANTES
        maxParticipantes = pa.procesarMaxParticipantes(maxParticipantesString, etMaxParticipantes, focusView);

        //INTERESES
        Pair<Boolean, ArrayList<Category>> resIntereses = pa.procesarIntereses(listItems, checkedItems, this.getActivity());
        unlessOneInteres = resIntereses.first;

        //UBICACION
        ubicacionOK = pa.procesarUbicacion(ubicacionSeleccionada, this.getActivity());

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        // -------------- //

        if(focusView != null)
            focusView.setFocusable(true);

        if(nombre != null && fechaIni != null && fechaFin != null && maxParticipantes != null && unlessOneInteres && ubicacionOK) {
            act = new Actividad(nombre, fechaIni, fechaFin, maxParticipantes, descripcion, ubicacionSeleccionada, resIntereses.second, AutorizacionFirebase.getUser().getUid());
        }

        return act;
    }
}

package pad.meetandshare.actividades;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.FechaUtil;
import pad.meetandshare.negocio.modelo.Ubicacion;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;

import static android.app.Activity.RESULT_OK;


public class CrearActividadFragment extends Fragment implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //Widgets
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
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private Date fechaIni;
    private long horaIni;
    private Date fechaFin;
    private long horaFin;
    private int maxParticipantes;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();

    private Ubicacion ubicacionSeleccionada;

    private Actividad actividad;
    private Usuario usuarioLogeado;

    private SAActividad saActividad;

    private ValueEventListener eventListener;

    private View rootView;

    private boolean btnCrearActividadPressed = false;
    private boolean actividadCreada = false;

    public CrearActividadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearActividadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearActividadFragment newInstance(String param1, String param2) {
        CrearActividadFragment fragment = new CrearActividadFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems = Categoria.getArray();
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
        listenerUbicacion();

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
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaIniCrearActividad);
                break;

            case R.id.ib_obtener_horaIni:
                fechaUtil.obtenerHora(getActivity(), R.id.horaIniCrearActividad);
                break;

            case R.id.ib_obtener_fechaFin:
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaFinCrearActividad);
                break;

            case R.id.ib_obtener_horaFin:
                fechaUtil.obtenerHora(getActivity(), R.id.horaFinCrearActividad);
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

    private void listenerUbicacion() {
        ubicacionBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(
                        getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    //si no tiene permisos
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)) {//si debe pedirlos
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    } else {//si no debe pedirlos
                        renderPlacePicker(getActivity());
                    }
                } else {//si tiene permisos
                    renderPlacePicker(getActivity());
                }
            }
        });
    }
    // ---------------------------

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
                }

                if (btnCrearActividadPressed && actividadCreada) {
                    changeToVerActividad();
                }

            }//onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        saActividad.getDatabaseReference().addValueEventListener(eventListener);
    }

    private void changeToVerActividad() {

        Fragment fragmento = VerActividadFragment.newInstance(actividad, usuarioLogeado.getNombre());

        FragmentManager fm = this.getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ContenedorMenuLateral, fragmento);
        ft.addToBackStack(null);

        ft.commit();
    }
    // ---------------------------

    @Override
    public void onStop() {
        super.onStop();
        if (eventListener != null)
            saActividad.getDatabaseReference().removeEventListener(eventListener);
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

    private boolean checkInputActividad(String nombre, String fechaIniString, String horaIniString, String fechaFinString, String horaFinString, String maxParticipantesString, String descripcion) {

        boolean nombreOk = false;
        boolean fechaIniOK = false;
        boolean horaIniOK = false;
        boolean fechaFinOK = false;
        boolean horaFinOK = false;
        boolean maxParticipantesOK = false;
        View focusView = null;

        //QUITAR ESPACIOS AL PRINCIPIO Y FINAL DE CADA INPUT
        nombre = nombre.trim();
        fechaIniString = fechaIniString.trim();
        horaIniString = horaIniString.trim();
        fechaFinString = fechaFinString.trim();
        horaFinString = horaFinString.trim();
        descripcion = descripcion.trim();

        final String campoObligatorio = "Por favor, rellene todos los campos";

        //NOMBRE
        if (nombre == null || nombre.isEmpty()) {
            etNombre.setError(campoObligatorio);
            focusView = etNombre;
        } else if (!actividad.isValidNombre(nombre)) {
            etNombre.setError("El nombre introducido no es válido, sólo puede contener letras");
            focusView = etNombre;
        } else {
            nombreOk = true;
        }

        //FECHA INI
        if (fechaIniString == null || fechaIniString.isEmpty()) {
            etFechaIni.setError(campoObligatorio);
            focusView = etFechaIni;
        } else {
            try {
                fechaIni = FechaUtil.getDateFormat().parse(fechaIniString);

                if(!actividad.isOnlyFechaIniLaterThanToday(fechaIni)) {
                    etFechaIni.setError("La fecha de inicio debe ser hoy o posterior");
                    focusView = etFechaIni;
                }
                else {
                    fechaIniOK = true;
                }
            } catch (ParseException e) {
                etFechaIni.setError("Formato de fecha incorrecto");
                focusView = etFechaIni;
            }
        }

        //HORA INI
        if (horaIniString == null || horaIniString.isEmpty()) {
            etHoraIni.setError(campoObligatorio);
            focusView = etHoraIni;
        } else {
            horaIniString = FechaUtil.horaCorrectFormat(horaIniString);

            if (!Actividad.isValidHora(horaIniString)) {
                etHoraIni.setError("Formato de hora incorrecto");
                focusView = etHoraIni;
            } else {
                if (fechaIniOK) {
                    fechaIni = FechaUtil.dateCorrectFormat(fechaIniString, horaIniString);

                    horaIniOK = Actividad.isValidFechaIni(fechaIni);
                    if (!horaIniOK) {
                        etHoraIni.setError("La hora de inicio debe ser ahora o posterior");
                        focusView = etHoraIni;
                    }
                } else {
                    etHoraIni.setError("Introduce una fecha de inicio correcta");
                    focusView = etHoraIni;
                }
            }
        }

        if (fechaIniOK && horaIniOK) {
            //FECHA FIN
            if (fechaFinString == null || fechaFinString.isEmpty()) {
                etFechaFin.setError(campoObligatorio);
                focusView = etFechaFin;
            } else {
                try {
                    fechaFin = FechaUtil.getDateFormat().parse(fechaFinString);
                } catch (ParseException e) {
                    fechaFinOK = true;
                    etFechaFin.setError("Formato de fecha incorrecto");
                    focusView = etFechaFin;
                }
            }

            //HORA FIN
            if (horaFinString == null || horaFinString.isEmpty()) {
                etHoraFin.setError(campoObligatorio);
                focusView = etHoraFin;
            } else {
                horaFinString = FechaUtil.horaCorrectFormat(horaFinString);

                try {
                    if (!actividad.isOnlyFechaFinLaterThanFechaIni(fechaIniString, fechaFinString)) {
                        etFechaFin.setError("La fecha de inicio debe ser igual o posterior a la fecha de fin");
                        focusView = etFechaFin;
                    }
                    else {
                        fechaFinOK = true;

                        if (!Actividad.isValidHora(horaFinString)) {
                            etHoraFin.setError("Formato de hora incorrecto");
                            focusView = etHoraFin;
                        }
                        else {
                            fechaFin = FechaUtil.dateCorrectFormat(fechaFinString, horaFinString);

                            if (!Actividad.isValidFechaFin(fechaIni, fechaFin)) {
                                etHoraFin.setError("La hora de fin debe ser posterior a la hora de inicio");
                                focusView = etHoraFin;
                            } else {
                                horaFinOK = true;
                            }
                        }
                    }
                } catch(ParseException e) {
                    fechaFinOK = false;
                    etFechaFin.setError("Formato de fecha incorrecto");
                    focusView = etFechaFin;
                }
            }
        } else {
            etFechaFin.setError("Introduce una fecha de inicio correcta");
            focusView = etFechaFin;
        }

        //MAX PARTICIPANTES
        if (maxParticipantesString == null || maxParticipantesString.isEmpty()) {
            etMaxParticipantes.setError(campoObligatorio);
            focusView = etMaxParticipantes;
        } else if (!Actividad.isValidMaxParticipantes(maxParticipantesString)) {
            etMaxParticipantes.setError("La actividad debe permitir almenos 2 participantes");
            focusView = etMaxParticipantes;
        } else {
            maxParticipantes = Integer.parseInt(maxParticipantesString);
            maxParticipantesOK = true;
        }

        //INTERESES
        int i = 0;
        boolean unlessOneInteres = false;
        while (i < this.checkedItems.length && !unlessOneInteres) {
            unlessOneInteres = checkedItems[i];
            i++;
        }

        if (!unlessOneInteres) {
            Toast toast2 = Toast.makeText(getActivity(), "Debes seleccionar al menos un interés", Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }

        boolean ubicacionOk = false;
        //UBICACION
        if (ubicacionSeleccionada == null) {
            Toast toast3 = Toast.makeText(getActivity(), "Selecciona una ubicación", Toast.LENGTH_SHORT);
            toast3.setGravity(Gravity.CENTER, 0, 0);
            toast3.show();
        } else {
            ubicacionOk = true;
        }

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        return (nombreOk && fechaIniOK && horaIniOK && fechaFinOK && horaFinOK && maxParticipantesOK && ubicacionOk && unlessOneInteres);
    }

    private void crearActividad() {
        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = etNombre.getText().toString();
        String fechaIniString = etFechaIni.getText().toString();
        String horaIniString = etHoraIni.getText().toString();
        String fechaFinString = etFechaFin.getText().toString();
        String horaFinString = etHoraFin.getText().toString();
        String maxParticipantesString = etMaxParticipantes.getText().toString();
        String descripcion = etDescripcion.getText().toString();

        ArrayList<Categoria> intereses = new ArrayList<>();

        for (int i = 0; i < checkedItems.length; ++i) {
            if (checkedItems[i]) {
                Categoria cat = Categoria.getCategoria(listItems[i]);
                intereses.add(cat);
            }
        }

        if (checkInputActividad(nombre, fechaIniString, horaIniString, fechaFinString, horaFinString, maxParticipantesString, descripcion)) {
            //buscar el usuario logeado
            usuarioLogeado = AutorizacionFirebase.getUser();

            if (usuarioLogeado != null) {
                //crear la actividad
                actividad = new Actividad(nombre, fechaIni, fechaFin, maxParticipantes, descripcion, ubicacionSeleccionada, intereses, usuarioLogeado.getUid());

                saActividad.create(actividad);

                actividadCreada = true;
            } else {
                String toastMsg = String.format("Error usuario logeado no encontrado");
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

}

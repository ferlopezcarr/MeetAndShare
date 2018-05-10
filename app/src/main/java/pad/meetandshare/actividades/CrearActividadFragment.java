package pad.meetandshare.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;

import static android.app.Activity.RESULT_OK;
import static java.lang.Double.parseDouble;


public class CrearActividadFragment extends Fragment implements View.OnClickListener {

    //Widgets
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
    //INTERESES BUTTON
    private Button interesesBoton;
    //UBICACION BOTON
    private Button ubicacionBoton;
    //CREAR ACTIVIDAD
    private Button crearActividadBoton;

    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_PICKER_REQUEST = 1;

    private Date fechaIni;
    private long horaIni;
    private Date fechaFin;
    private long horaFin;
    private int maxParticipantes;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();


    private View rootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_crear_actividad,
                container, false);

        FirebaseUser currentUser = AutorizacionFirebase.getFirebaseAuth().getCurrentUser();



        if(currentUser != null) {//Si el usuario esta logeado

            //VISTAS----------------------
            //FECHA INI
            //Widget EditText donde se mostrara la fecha obtenida
            etFechaIni = (EditText) rootView.findViewById(R.id.fechaIniCrearActividad);
            //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
            ibObtenerFechaIni = (ImageButton) rootView.findViewById(R.id.ib_obtener_fechaIni);
            ibObtenerFechaIni.setOnClickListener(this);

            //HORA INI

            //FECHA FIN
            //Widget EditText donde se mostrara la fecha obtenida
            etFechaFin = (EditText) rootView.findViewById(R.id.fechaFinCrearActividad);
            //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
            ibObtenerFechaFin = (ImageButton) rootView.findViewById(R.id.ib_obtener_fechaFin);
            ibObtenerFechaFin.setOnClickListener(this);

            //HORA FIN

            //UBICACION
            ubicacionBoton = (Button) rootView.findViewById(R.id.botonSeleccionarUbicacion);
            ubicacionBoton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            //INTERESES
            interesesBoton = (Button) rootView.findViewById(R.id.botonInteresRegistro);
            listenerButtonIntereses(interesesBoton);

            //APIS
            /*
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            */

            place(getActivity());
        }
        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems = Categoria.getArray();
        checkedItems = new boolean[listItems.length];

    }

    /**
     * Método que se ejecuta al hacer click en alguno de los botones capturados
     * @param v
     */
    @Override
    public void onClick(View v) {
        FechaUtil fechaUtil = new FechaUtil();

        switch (v.getId()){
            case R.id.ib_obtener_fechaIni:
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaIniCrearActividad);
                break;

            case R.id.ib_obtener_fechaFin:
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaFinCrearActividad);
                break;

            case R.id.crearActividadPost:
                crearActividad();
                break;
        }
    }


    /* ------------ MÉTODOS PRIVADOS ------------ */

    private void crearActividad() {
        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = ((EditText) rootView.findViewById(R.id.nombreCrearActividad)).getText().toString();
        String fechaIniString =((EditText) rootView.findViewById(R.id.fechaIniCrearActividad)).getText().toString();
        String horaIniString =((EditText) rootView.findViewById(R.id.horaIniCrearActividad)).getText().toString();
        String fechaFinString =((EditText) rootView.findViewById(R.id.fechaFinCrearActividad)).getText().toString();
        String horaFinString =((EditText) rootView.findViewById(R.id.horaFinCrearActividad)).getText().toString();
        String maxPuntuacionesString = ((EditText) rootView.findViewById(R.id.maxParticipantesCrearActividad)).getText().toString();
        String descripcion = ((EditText) rootView.findViewById(R.id.descripcionCrearActividad)).getText().toString();

        ArrayList<Categoria> intereses = new ArrayList<>();

        for(int i = 0; i < checkedItems.length; ++i){
            if(checkedItems[i]){
                Categoria cat = Categoria.getCategoria(listItems[i]);
                intereses.add(cat);
            }
        }

        if(checkInputActividad(nombre, fechaIniString, horaIniString, fechaFinString, horaFinString, maxPuntuacionesString, descripcion)) {
            //buscar el usuario logeado

            //conseguir la ubicacion

            //crear la actividad
            //Actividad actividad = new Actividad(nombre, Date fechaInicio, Date fechaFin, int maxParticipantes, String descripcion, Place ubicacion, Usuario administrador);

            //mirar que la actividad no existe en la bd

            //guardar la actividad
        }
    }

    private void place(Activity activity) {
        try {
            int PLACE_PICKER_REQUEST = 1;

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

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
        EditText etNombre = (EditText) rootView.findViewById(R.id.nombreCrearActividad);
        if(nombre == null || nombre.isEmpty()){
            etNombre.setError(campoObligatorio);
            focusView = etNombre;
        }
        else if(!Usuario.isValidNombre(nombre)) {
            etNombre.setError("El nombre introducido no es válido, sólo puede contener letras");
            focusView = etNombre;
        }
        else {
            nombreOk = true;
        }

        //FECHA INI
        EditText etFechaIni = (EditText) rootView.findViewById(R.id.fechaIniCrearActividad);
        if (fechaIniString == null || fechaIniString.isEmpty()) {
            etFechaIni.setError(campoObligatorio);
            focusView = etFechaIni;
        } else {
            try {
                fechaIni = FechaUtil.getDateFormat().parse(fechaIniString);

                if(!Actividad.isValidFechaIni(fechaIni)) {
                    etFechaIni.setError("Debes ser mayor de edad");
                    focusView = etFechaIni;
                }
                else{
                    fechaIniOK = true;
                }
            } catch (ParseException e) {
                etFechaIni.setError("Formato de fecha incorrecto");
                focusView = etFechaIni;
            }
        }

        //HORA INI
        EditText etHoraIni = (EditText) rootView.findViewById(R.id.horaIniCrearActividad);
        if (horaIniString == null || horaIniString.isEmpty()) {
            etHoraIni.setError(campoObligatorio);
            focusView = etHoraIni;
        } else {
            String hora[] = horaIniString.split(":");
            horaIni = Long.parseLong(hora[0]);
            horaIni += Long.parseLong(hora[1]);

            if (!Actividad.isValidHora(hora[0], hora[1])) {
                etHoraIni.setError("Formato de hora incorrecto");
                focusView = etHoraIni;
            } else {
                fechaIni.setTime(horaIni);
                horaIniOK = true;
            }
        }

        if(fechaIniOK) {
            //FECHA FIN
            EditText etFechaFin = (EditText) rootView.findViewById(R.id.fechaFinCrearActividad);
            if (fechaFinString == null || fechaFinString.isEmpty()) {
                etFechaFin.setError(campoObligatorio);
                focusView = etFechaFin;
            } else {
                try {
                    fechaFin = FechaUtil.getDateFormat().parse(fechaFinString);

                    if (!Actividad.isValidFechaFin(fechaIni, fechaFin)) {
                        etFechaFin.setError("Debes ser mayor de edad");
                        focusView = etFechaFin;
                    } else {
                        fechaFinOK = true;
                    }
                } catch (ParseException e) {
                    etFechaFin.setError("Formato de fecha incorrecto");
                    focusView = etFechaFin;
                }
            }

            //HORA FIN
            EditText etHoraFin = (EditText) rootView.findViewById(R.id.horaFinCrearActividad);
            if (horaFinString == null || horaFinString.isEmpty()) {
                etHoraFin.setError(campoObligatorio);
                focusView = etHoraFin;
            } else {
                String hora[] = horaIniString.split(":");
                horaFin = Long.parseLong(hora[0]);
                horaFin += Long.parseLong(hora[1]);

                if (!Actividad.isValidHora(hora[0], hora[1])) {
                    etHoraFin.setError("Formato de hora incorrecto");
                    focusView = etHoraFin;
                } else {
                    fechaFin.setTime(horaFin);
                    horaFinOK = true;
                }
            }
        }

        //MAX PARTICIPANTES
        EditText etMaxParticipantes = (EditText) rootView.findViewById(R.id.maxParticipantesCrearActividad);
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
        while(i < this.checkedItems.length && !unlessOneInteres) {
            unlessOneInteres = checkedItems[i];
            i++;
        }

        if(!unlessOneInteres) {
            Toast toast2 = Toast.makeText(getActivity(), "Debes seleccionar al menos un interés", Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        return (nombreOk && fechaIniOK && horaIniOK && fechaFinOK && horaFinOK && maxParticipantesOK && unlessOneInteres);
    }

    /**
     * Método que configura el botón de seleccionar intereses
     * @param intereses
     */
    private void listenerButtonIntereses(Button intereses) {

        interesesBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle(R.string.intereses);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
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

}

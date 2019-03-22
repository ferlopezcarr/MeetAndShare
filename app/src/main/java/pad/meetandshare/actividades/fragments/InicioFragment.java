package pad.meetandshare.actividades.fragments;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;

import pad.meetandshare.R;
import pad.meetandshare.actividades.LoginActivity;
import pad.meetandshare.integracion.ColorFile;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Category;
import pad.meetandshare.actividades.utils.FechaUtil;
import pad.meetandshare.negocio.modelo.Ubication;
import pad.meetandshare.negocio.modelo.User;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.MyCallBack;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InicioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class InicioFragment
        extends Fragment
        implements
            GoogleMap.OnInfoWindowClickListener,
            OnMapReadyCallback,
            ActivityCompat.OnRequestPermissionsResultCallback
{

    private Float color = ColorFile.DEFAULT_COLOR;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FragmentManager fragmentManager;
    private View rootView;
    private FloatingActionButton leyendaBoton;

    private final int MY_LOCATION_REQUEST_CODE = 123;

    public static final String UBICACION = "ubication";

    private Ubication ubicacionVerAct = null;

    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance(Ubication ubication) {
        InicioFragment fragment = new InicioFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UBICACION, ubication);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());

        if(!AutorizacionFirebase.amIAuthentificated()) {
            AutorizacionFirebase.singOut();
            Intent myIntent = new Intent(this.getActivity(), LoginActivity.class);

            this.startActivity(myIntent);
            this.onResume();
        }

        MapsInitializer.initialize(this.getActivity());

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            ubicacionVerAct = (Ubication) bundle.getSerializable(UBICACION);
        }

        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        fragmentManager = this.getFragmentManager();

        mapView =  rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        leyendaBoton = (FloatingActionButton) rootView.findViewById(R.id.botonLeyenda);

        leyendaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToLeyenda();
            }
        });

        return rootView;
    }

    private void changeToLeyenda() {

        Fragment frag = new LeyendaMapaFragment();

        this.getFragmentManager().beginTransaction().
                replace(R.id.ContenedorMenuLateral, frag).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InicioFragment.OnFragmentInteractionListener) {
            mListener = (InicioFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        readColorFromFile();
    }

    void readColorFromFile() {
        if(ColorFile.read(color, this.getActivity())) {
            if(color == null) {
                color = ColorFile.DEFAULT_COLOR;
            }
        }
        else {
            ColorFile.write(ColorFile.DEFAULT_COLOR, this.getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mapView != null)
            mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //MAPA

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Updates the location and zoom of the MapView
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        try {
            //si tienes permisos
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {


                mMap.setMyLocationEnabled(true);

                if(this.ubicacionVerAct != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ubicacionVerAct.getLatitude(), ubicacionVerAct.getLongitude()), 17));
                }
                else {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                            else {
                                Toast.makeText(getActivity(), "¡Activa la ubicación!", Toast.LENGTH_LONG).show();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.4167, -3.70325), 17));
                            }
                        }
                    });
                }

            } else {//si no tienes permisos
                //pides los permisos
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }
        } catch (SecurityException e){
            Toast.makeText(getActivity(), "¡Security exception!", Toast.LENGTH_LONG).show();
            //deslogear
        }

        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {

                final Actividad actividad = (Actividad) marker.getTag();

                SAUsuario saUsuario = new SAUsuarioImp();

                saUsuario.get(actividad.getAdminUid(), new MyCallBack() {
                    @Override
                    public void onCallbackUsuario(User usuario) {
                        if(usuario != null) {
                            //hacer la transicion

                            Fragment fr = VerActividadFragment.newInstance(actividad, AutorizacionFirebase.getUser().getName());
                            pad.meetandshare.actividades.FragmentTransaction fc=(pad.meetandshare.actividades.FragmentTransaction) getActivity();
                            fc.replaceFragment(fr);

                        }
                    }

                    @Override
                    public void onCallbackActividad(Actividad actividad) {

                    }

                    @Override
                    public void onCallbackActividadAll(ArrayList<Actividad> actividad) {

                    }
                });
            }
        });

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this.getActivity().getLayoutInflater()));


        final SAActividad saActividad = new SAActividadImp();
        // Add a marker in Sydney and move the camera

        saActividad.getAll(new MyCallBack() {
            @Override
            public void onCallbackUsuario(User usuario) {
            }

            @Override
            public void onCallbackActividad(Actividad actividad) {
            }

            @Override
            public void onCallbackActividadAll(ArrayList<Actividad> actividades) {

                for(Actividad actual : actividades) {
                    //si la fecha de fin es mas tarde de ahora
                    if(!actual.activityFinished()) {
                        mMap.addMarker(construirMarcador(actual)).setTag(actual);
                    }
                }
            }
        });

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Actividad actividad = (Actividad) marker.getTag();

        if(actividad != null) {
            String snippet = "";
            if(!actividad.getActive())
                 snippet += "CANCELADA \n";

            if(actividad.getCategories() != null) {
                snippet = snippet + "Categorías:";
                for (Category cat : actividad.getCategories()) {
                    snippet = snippet + '\n' + cat.getDisplayName();
                }
            }
            marker.setSnippet(snippet);
        }

        marker.hideInfoWindow();
        marker.showInfoWindow();
    }

    private MarkerOptions construirMarcador(Actividad act) {
        MarkerOptions marcador = new MarkerOptions().position(new LatLng(act.getUbication().getLatitude(), act.getUbication().getLongitude()));
        marcador.title(act.getName());
        Float x = (float) 0.7;
        Float y = (float) 0.7;
        marcador.infoWindowAnchor(x,y);
        Float opacity = (float) 0.8;
        marcador.alpha(opacity);


        //si estas inscrito en la actividad
        if (act.getRegisteredUserIds().contains(AutorizacionFirebase.getCurrentUser().getUid())) {
            //si eres el admin de la actividad
            if (act.getAdminUid().equalsIgnoreCase(AutorizacionFirebase.getCurrentUser().getUid())) {
                marcador.icon(BitmapDescriptorFactory.defaultMarker(ColorFile.ADMIN_COLOR));//morado
            } else {
                marcador.icon(BitmapDescriptorFactory.defaultMarker(ColorFile.PARTICIPANT_COLOR));
            }
        } else {//resto de actividades
            marcador.icon(BitmapDescriptorFactory.defaultMarker(color));
        }

        Date tommorrow = new Date();
        tommorrow = FechaUtil.sumarRestarDiasFecha(tommorrow, ColorFile.TIME_DIFFERENCE);
        //si la activadad empieza mañana
        if (act.getStartDate().before(tommorrow)) {
            marcador.icon(BitmapDescriptorFactory.defaultMarker(ColorFile.ACT_STARTS_TOMORROW_COLOR));
        }


        return marcador;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //si tienes permisos
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        } else {//si no tienes permisos

            if (requestCode == MY_LOCATION_REQUEST_CODE) {
                if (permissions.length == 2 &&
                        permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&  permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mMap.setMyLocationEnabled(true);

                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                            else
                                Toast.makeText(getActivity(), "¡Activa la ubicación!", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    mMap.setMyLocationEnabled(false);
                }
            }
        }
    }
}
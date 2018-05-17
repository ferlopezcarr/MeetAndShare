package pad.meetandshare.actividades;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InicioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment
        extends Fragment
        implements
            GoogleMap.OnInfoWindowClickListener,
            OnMapReadyCallback,
            ActivityCompat.OnRequestPermissionsResultCallback
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FragmentManager fragmentManager;

    private View rootView;

    private final int MY_LOCATION_REQUEST_CODE = 123;

    public InicioFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance() {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!AutorizacionFirebase.amIAuthentificated()) {
            AutorizacionFirebase.setSingOut(true);
            AutorizacionFirebase.getFirebaseAuth().signOut();
            Intent myIntent = new Intent(this.getActivity(), LoginActivity.class);

            this.startActivity(myIntent);
            this.onResume();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_inicio, container, false);
        fragmentManager = this.getFragmentManager();


        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());

        mapView =  rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);

        return rootView;
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
        // TODO: Update argument type and name
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

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                        else
                            Toast.makeText(getActivity(), "¡Activa la ubicación!", Toast.LENGTH_LONG).show();


                    }
                });

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

                saUsuario.get(actividad.getIdAdministrador(), new MyCallBack() {
                    @Override
                    public void onCallbackUsuario(Usuario usuario) {
                        if(usuario != null) {
                            //hacer la transicion
                            Fragment fragmento = VerActividadFragment.newInstance(actividad, usuario.getNombre());

                            FragmentTransaction ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.ContenedorMenuLateral, fragmento);
                            ft.addToBackStack(null);

                            ft.commit();
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


        SAActividad saActividad = new SAActividadImp();
        // Add a marker in Sydney and move the camera

        saActividad.getAll(new MyCallBack() {
            @Override
            public void onCallbackUsuario(Usuario usuario) {
            }

            @Override
            public void onCallbackActividad(Actividad actividad) {
            }

            @Override
            public void onCallbackActividadAll(ArrayList<Actividad> actividad) {

                for(Actividad actual : actividad) {

                    mMap.addMarker(construirMarcador(actual)).setTag(actual);
                }
            }
        });

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Actividad actividad = (Actividad) marker.getTag();

        if(actividad != null) {
            String snippet = "";

            if(actividad.getCategorias() != null) {
                snippet = snippet + "Categorías:";
                for (Categoria cat : actividad.getCategorias()) {
                    snippet = snippet + '\n' + cat.getDisplayName();
                }
            }
            marker.setSnippet(snippet);
        }

        marker.hideInfoWindow();
        marker.showInfoWindow();
    }

    private MarkerOptions construirMarcador(Actividad act) {
        MarkerOptions marcador = new MarkerOptions().position(new LatLng(act.getUbicacion().getLatitude(), act.getUbicacion().getLongitude()));
        marcador.title(act.getNombre());
        Float x = (float) 0.7;
        Float y = (float) 0.7;
        marcador.infoWindowAnchor(x,y);
        Float opacity = (float) 0.8;
        marcador.alpha(opacity);

        //si la actividad es tuya
        if(act.getIdAdministrador().equalsIgnoreCase(AutorizacionFirebase.getCurrentUser().getUid())) {
            marcador.icon(BitmapDescriptorFactory.defaultMarker(215));
        }
        else {//si no eres el administrador
            //http://paletton.com/#uid=15z0u0kkQm7agxYf-rppWh2vlbA
            marcador.icon(BitmapDescriptorFactory.defaultMarker(354));
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
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
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
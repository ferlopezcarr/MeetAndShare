package pad.meetandshare.actividades;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.MyCallBack;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;

public class VerActividadFragment extends Fragment {

    public static final String UID_ACTIVIDAD = "uidActividad";

    private String uidActividad;
    private Actividad actividad;
    private Usuario admin;
    private VerActividadFragment.OnFragmentInteractionListener mListener;
    private View rootView;
    private LayoutInflater miInflater;


    public VerActividadFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PerfilUsuarioFragment newInstance(String uidAdministrador) {
        PerfilUsuarioFragment fragment = new PerfilUsuarioFragment();
        Bundle args = new Bundle();
        args.putString(UID_ACTIVIDAD, uidAdministrador);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();

        if (b != null)
            uidActividad = b.getString("UID_ACTIVIDAD");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ver_actividad, container, false);
        miInflater = inflater;

        SAActividad saActividad = new SAActividadImp();

        saActividad.get(this.uidActividad, new MyCallBack() {
            @Override
            public void onCallbackUsuario(Usuario value) {
            }

            @Override
            public void onCallbackActividad(Actividad value) {

                actividad = value;

                SAUsuario saUsuario = new SAUsuarioImp();
                saUsuario.get(actividad.getIdAdministrador(), new MyCallBack() {
                    @Override
                    public void onCallbackUsuario(Usuario value) {
                        admin = value;
                        ((TextView) rootView.findViewById(R.id.administradorVerActividad)).setText(admin.getNombre());
                    }

                    @Override
                    public void onCallbackActividad(Actividad actividad) {
                    }
                });

                String fechaIni = FechaUtil.getDateWithHourFormat().format(actividad.getFechaInicio());
                String arrayFechaIni[] = fechaIni.split(" ");

                String fechaFin = FechaUtil.getDateWithHourFormat().format(actividad.getFechaFin());
                String arrayFechaFin[] = fechaFin.split(" ");

                ((TextView) rootView.findViewById(R.id.nombreVerActividad)).setText(actividad.getNombre());

                ((TextView) rootView.findViewById(R.id.fechaIniVerActividad)).setText(arrayFechaIni[0]);
                ((TextView) rootView.findViewById(R.id.horaIniVerActividad)).setText(arrayFechaIni[1]);
                ((TextView) rootView.findViewById(R.id.fechaFinVerActividad)).setText(arrayFechaIni[0]);
                ((TextView) rootView.findViewById(R.id.horaFinVerActividad)).setText(arrayFechaIni[1]);

                ((TextView) rootView.findViewById(R.id.maxParticipantesVerActividad)).setText(actividad.getMaxParticipantes());
                ((TextView) rootView.findViewById(R.id.descripcionVerActividad)).setText(actividad.getDescripcion());
                //ubicacion

                for (Categoria interes : actividad.getCategorias()) {
                    TextView interesVista = (TextView) miInflater.inflate(R.layout.layout_interes, null);
                    interesVista.setText(interes.getDisplayName());
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    llp.setMargins(0, 20, 0, 0); // llp.setMargins(left, top, right, bottom);
                    interesVista.setLayoutParams(llp);
                    ((LinearLayout) rootView.findViewById(R.id.containerInteresesPerfil)).addView(interesVista);
                }

                //lista de usuarios se muestra cuando pulsas el boton de ver usuarios

                String estado = "";
                if (actividad.getFinalizada()) {
                    estado = "Finalizada";
                } else {//no finalizada
                    estado = "Activa";
                }
                ((TextView) rootView.findViewById(R.id.finalizadaVerActividad)).setText(estado);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VerActividadFragment.OnFragmentInteractionListener) {
            mListener = (VerActividadFragment.OnFragmentInteractionListener) context;
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

}
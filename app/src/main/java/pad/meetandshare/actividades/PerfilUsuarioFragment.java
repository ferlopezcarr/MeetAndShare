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

import java.text.SimpleDateFormat;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilUsuarioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerfilUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilUsuarioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Usuario user;
    private OnFragmentInteractionListener mListener;
    private View rootView;



    public PerfilUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilUsuarioFragment newInstance(String param1, String param2) {
        PerfilUsuarioFragment fragment = new PerfilUsuarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_perfil_usuario,
                container, false);

        Usuario miUser = AutorizacionFirebase.getUser();


      ((TextView) rootView.findViewById(R.id.nombrePerfil)).setText(miUser.getNombre());
      ((TextView) rootView.findViewById(R.id.emailPerfil)).setText(miUser.getEmail());
      ((TextView) rootView.findViewById(R.id.descripcionPerfil)).setText(miUser.getDescripcion());

      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


      for(Categoria interes: miUser.getCategorias()){


          TextView interesVista =  (TextView) inflater.inflate(R.layout.layout_interes, null);
          interesVista.setText(interes.getDisplayName());
          LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

          llp.setMargins(0, 20, 0, 0); // llp.setMargins(left, top, right, bottom);
          interesVista.setLayoutParams(llp);
          ((LinearLayout) rootView.findViewById(R.id.containerInteresesPerfil)).addView(interesVista);

      }

      ((TextView) rootView.findViewById(R.id.fechaNacimientoPerfil)).setText(format.format(miUser.getFechaNacimiento()));



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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

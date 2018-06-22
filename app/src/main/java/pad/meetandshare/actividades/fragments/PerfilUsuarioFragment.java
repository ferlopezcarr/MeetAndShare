package pad.meetandshare.actividades.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import pad.meetandshare.R;
import pad.meetandshare.actividades.FragmentTransaction;
import pad.meetandshare.actividades.LoginActivity;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.actividades.utils.FechaUtil;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;

public class PerfilUsuarioFragment extends Fragment {

    private Usuario user;
    private View rootView;


    public PerfilUsuarioFragment() {
        // Required empty public constructor
    }

    public static PerfilUsuarioFragment newInstance() {
        PerfilUsuarioFragment fragment = new PerfilUsuarioFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!AutorizacionFirebase.amIAuthentificated()) {
            AutorizacionFirebase.singOut();
            Intent myIntent = new Intent(this.getActivity(), LoginActivity.class);

            this.startActivity(myIntent);
            this.onResume();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        Usuario miUser = AutorizacionFirebase.getUser();

        ((TextView) rootView.findViewById(R.id.nombrePerfil)).setText(miUser.getNombre());
        ((TextView) rootView.findViewById(R.id.emailPerfil)).setText(miUser.getEmail());
        ((TextView) rootView.findViewById(R.id.descripcionPerfil)).setText(miUser.getDescripcion());
        ((TextView) rootView.findViewById(R.id.descripcionPerfil)).setMovementMethod(new ScrollingMovementMethod());

        for (Categoria interes : miUser.getCategorias()) {
            TextView interesVista = (TextView) inflater.inflate(R.layout.layout_interes, null);
            interesVista.setText(interes.getDisplayName());
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            llp.setMargins(0, 20, 0, 0); // llp.setMargins(left, top, right, bottom);
            interesVista.setLayoutParams(llp);
            ((LinearLayout) rootView.findViewById(R.id.containerInteresesPerfil)).addView(interesVista);
        }

        ((TextView) rootView.findViewById(R.id.fechaNacimientoPerfil)).setText(FechaUtil.getDateFormat().format(miUser.getFechaNacimiento()));

        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(miUser.getDescripcion() == null) {
            rootView.findViewById(R.id.labelDescripcion).setVisibility(View.GONE);
            rootView.findViewById(R.id.descripcionPerfil).setVisibility(View.GONE);
        }
        else {
            if(miUser.getDescripcion().length() == 0) {
                rootView.findViewById(R.id.labelDescripcion).setVisibility(View.GONE);
                rootView.findViewById(R.id.descripcionPerfil).setVisibility(View.GONE);
            }
        }

        ((ScrollView) rootView.findViewById(R.id.scrollPerfil)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) rootView.findViewById(R.id.descripcionPerfil)).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        ((TextView) rootView.findViewById(R.id.descripcionPerfil)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) rootView.findViewById(R.id.descripcionPerfil)).getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        rootView.findViewById(R.id.editaPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fr= ModificaUsuarioFragment.newInstance();
                FragmentTransaction fc= (FragmentTransaction) getActivity();
                fc.replaceFragment(fr);


            }
        });

        return rootView;
    }

}

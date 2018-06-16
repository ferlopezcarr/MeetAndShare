package pad.meetandshare.actividades;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.FechaUtil;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.SAActividad;
import pad.meetandshare.negocio.servicioAplicacion.SAActividadImp;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;

public class ModificaUsuarioFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText tvNombreUsuario;
    private EditText tvFechaNac;
    private ImageButton ibObtenerFechaNac;
    private EditText tvDescripcion;
    private Button botonIntereses;
    private Button botonGuardar;

    private Usuario miUser;
    private SAUsuarioImp saUsuario;

    private ValueEventListener eventListener;

    private boolean btnModificarPressed = false;
    private boolean usuarioModificado = false;

    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();


    public ModificaUsuarioFragment() {
        // Required empty public constructor
    }

    public static ModificaUsuarioFragment newInstance() {
        ModificaUsuarioFragment fragment = new ModificaUsuarioFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listItems = Categoria.getArray();
        checkedItems = new boolean[listItems.length];

        saUsuario = new SAUsuarioImp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        miUser = AutorizacionFirebase.getUser();

        List<Categoria> lista = miUser.getCategorias();

        int k=0;
        for(int i=0; i< Categoria.getArray().length && k < lista.size();++i){
            if(lista.get(k).getDisplayName()==Categoria.getArray()[i]){
                checkedItems[i]=true;
                k++;
            }
        }

        rootView = inflater.inflate(R.layout.fragment_modifica_usuario, container, false);

        //Nombre
        tvNombreUsuario = ((EditText) rootView.findViewById(R.id.nombreModifica));
        tvNombreUsuario.setText(miUser.getNombre());

        //FechaNac
        tvFechaNac = ((EditText) rootView.findViewById(R.id.fechaNacimientoModificar));
        String fechaNac = FechaUtil.getDateFormat().format(miUser.getFechaNacimiento());
        tvFechaNac.setText(fechaNac);
        ibObtenerFechaNac = (ImageButton) rootView.findViewById(R.id.ib_obtener_fecha);
        ibObtenerFechaNac.setOnClickListener(this);

        //Descripción
        tvDescripcion = ((EditText) rootView.findViewById(R.id.descripcionPerfilModificar));
        if(miUser.getDescripcion().length() != 0)
            tvDescripcion.setText(miUser.getDescripcion());
        /*
        tvDescripcion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                tvDescripcion.setLines(8);
                tvDescripcion.setScroller(new Scroller(getContext()));
                tvDescripcion.setVerticalScrollBarEnabled(true);
                tvDescripcion.setMovementMethod(new ScrollingMovementMethod());
                tvDescripcion.setVerticalScrollBarEnabled(true);
            }
        });
        */
        tvDescripcion.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                tvDescripcion.setLines(6);
                if (view.getId() == R.id.descripcionPerfilModificar) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            tvDescripcion.setLines(tvDescripcion.getMinLines());
                            break;
                    }
                }
                return false;
            }
        });

        botonIntereses = (Button)rootView.findViewById(R.id.botonInteresModificar);
        listenerButtonIntereses(botonIntereses);

        botonGuardar = (Button)rootView.findViewById(R.id.botonModificarUsuarioPost);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        FechaUtil fechaUtil = new FechaUtil();

        switch (v.getId()) {
            case R.id.ib_obtener_fecha:
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaNacimientoModificar);
                break;

            case R.id.botonModificarUsuarioPost:
                modificarUsuario();
                this.btnModificarPressed = true;
                break;
        }
    }

    /**
     * Método que configura el botón de seleccionar intereses
     *
     * @param intereses
     */
    private void listenerButtonIntereses(Button intereses) {

        intereses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
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

    private void modificarUsuario() {

    }

    @Override
    public void onStart() {
        super.onStart();

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object obj = dataSnapshot.getValue(Usuario.class);

                if(obj.getClass() == Usuario.class) {
                    Usuario usr = (Usuario)obj;

                    if(usr.getUid() != null && usr.getEmail() != null) {
                        if(saUsuario.checkUsuario(miUser, dataSnapshot)) {
                            if (btnModificarPressed && usuarioModificado) {
                                changeToVerPerfil();
                            }
                        }
                    }
                }

            }//onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        saUsuario.getDatabaseReference().addValueEventListener(eventListener);
    }

    private void changeToVerPerfil() {
        Fragment fragmento = PerfilUsuarioFragment.newInstance();

        FragmentManager fm = this.getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ContenedorMenuLateral, fragmento);
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (eventListener != null)
            saUsuario.getDatabaseReference().removeEventListener(eventListener);
    }

}

package pad.meetandshare.actividades.fragments;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pad.meetandshare.R;
import pad.meetandshare.actividades.FragmentTransaction;
import pad.meetandshare.actividades.RegistroActivity;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.presentacion.FechaUtil;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;
import pad.meetandshare.presentacion.Parser;
import pad.meetandshare.presentacion.ParserUsuario;

public class ModificaUsuarioFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private EditText etNombreUsuario;
    private EditText etFechaNac;
    private ImageButton ibObtenerFechaNac;
    private EditText etDescripcion;
    private Button botonIntereses;
    private Button botonGuardar;

    private Usuario miUser;
    private SAUsuarioImp saUsuario;

    private boolean btnModificarPressed = false;

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
        etNombreUsuario = ((EditText) rootView.findViewById(R.id.nombreModifica));
        etNombreUsuario.setText(miUser.getNombre());

        //FechaNac
        etFechaNac = ((EditText) rootView.findViewById(R.id.fechaNacimientoModificar));
        String fechaNac = FechaUtil.getDateFormat().format(miUser.getFechaNacimiento());
        etFechaNac.setText(fechaNac);
        ibObtenerFechaNac = (ImageButton) rootView.findViewById(R.id.ib_obtener_fecha);
        ibObtenerFechaNac.setOnClickListener(this);

        //Descripción
        etDescripcion = ((EditText) rootView.findViewById(R.id.descripcionPerfilModificar));
        if(miUser.getDescripcion().length() != 0)
            etDescripcion.setText(miUser.getDescripcion());
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
        etDescripcion.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                etDescripcion.setLines(6);
                if (view.getId() == R.id.descripcionPerfilModificar) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            etDescripcion.setLines(etDescripcion.getMinLines());
                            break;
                    }
                }
                return false;
            }
        });

        botonIntereses = (Button)rootView.findViewById(R.id.botonInteresModificar);
        botonIntereses.setOnClickListener(this);

        botonGuardar = (Button)rootView.findViewById(R.id.botonModificarUsuarioPost);
        botonGuardar.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        FechaUtil fechaUtil = new FechaUtil();

        switch (v.getId()) {
            case R.id.ib_obtener_fecha:
                fechaUtil.obtenerFecha(getActivity(), R.id.fechaNacimientoModificar);
                break;

            case R.id.botonInteresModificar:
                listenerButtonIntereses(v);
                break;

            case R.id.botonModificarUsuarioPost:
                this.btnModificarPressed = true;
                modificarUsuario();
                break;
        }
    }

    /**
     * Método que configura el botón de seleccionar intereses
     * @param view
     */
    private void listenerButtonIntereses(View view) {

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

    private Usuario checkUsuario() {
        View focusView = null;

        Usuario usr = miUser;

        String nombre = etNombreUsuario.getText().toString().trim();
        String fechaStr = etFechaNac.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        ParserUsuario pu = new ParserUsuario();

        //NOMBRE
        nombre = pu.procesarNombre(nombre, etNombreUsuario, focusView);
        if(nombre != null && !nombre.equalsIgnoreCase(usr.getNombre())) {
            usr.setNombre(nombre);
        }

        //FECHA DE NACIMIENTO
        Date fechaNueva = pu.procesarFechaNacimiento(fechaStr,etFechaNac,focusView);
        //si es valida y distinta
        if(fechaNueva != null && fechaNueva.compareTo(usr.getFechaNacimiento()) != 0) {
            usr.setFechaNacimiento(fechaNueva);
        }
        else {
            etFechaNac.setText(FechaUtil.getDateFormat().format(usr.getFechaNacimiento()));
        }

        //INTERESES
        Pair<Boolean, ArrayList<Categoria>> resIntereses = pu.procesarIntereses(listItems, checkedItems, this.getActivity());
        if(resIntereses.first) {
            usr.setCategorias(resIntereses.second);
        }

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }
        usr.setDescripcion(descripcion);

        if(focusView != null)
            focusView.setFocusable(true);

        if(nombre == null || fechaNueva == null || !resIntereses.first)
            usr = null;

        return usr;
    }

    private void modificarUsuario() {

        Usuario usrModificado = checkUsuario();

        if(usrModificado != null){

            ArrayList<Categoria> intereses = new ArrayList<>();

            saUsuario.save(usrModificado);

            AutorizacionFirebase.setUsuario(usrModificado);

            this.changeToVerPerfil();
        }
    }

    private void changeToVerPerfil() {

        Fragment fr = PerfilUsuarioFragment.newInstance();
        FragmentTransaction fc=(FragmentTransaction) this.getActivity();
        fc.replaceFragment(fr);

    }

}

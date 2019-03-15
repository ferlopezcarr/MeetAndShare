package pad.meetandshare.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import pad.meetandshare.R;
import pad.meetandshare.actividades.ParserObjects.ParserUsuario;
import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Category;
import pad.meetandshare.actividades.utils.FechaUtil;
import pad.meetandshare.negocio.modelo.User;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;
import pad.meetandshare.negocio.servicioAplicacion.MyCallBack;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;

import static android.support.constraint.Constraints.TAG;


public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    private EditText etEmail;
    private EditText etNombre;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private EditText etFecha;
    private EditText etDescripcion;
    private ImageButton ibObtenerFecha;
    private Button interesesBoton;
    private Button registro;

    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();

    private String userEmail;
    private String userPassword;


    private User miUsuario;

    /* ------------ MÉTODOS PÚBLICOS ------------ */

    /* --------- ACTIVIDAD --------- */

    /**
     * Método que se ejecuta al crear la actividad
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listItems = Category.getArray();
        checkedItems = new boolean[listItems.length];
        setContentView(R.layout.activity_registro);

        //EMAIL
        etEmail = (EditText) findViewById(R.id.emailRegistro);

        //NOMBRE
        etNombre = (EditText) findViewById(R.id.nombreRegistro);

        //PASSWORD
        etPassword = (EditText) findViewById(R.id.passwordRegistro);

        //PASSWORD CONFIRM
        etPasswordConfirm = (EditText) findViewById(R.id.passwordConfirmationRegistro);

        //FECHA
        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) findViewById(R.id.fechaNacimientoRegistro);

        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        ibObtenerFecha.setOnClickListener(this);

        //DESCRIPCION
        etDescripcion = (EditText) findViewById(R.id.descripcionRegistro);

        //INTERESES
        interesesBoton = (Button) findViewById(R.id.botonInteresRegistro);
        interesesBoton.setOnClickListener(this);

        //REGISTRO
        registro = (Button) findViewById(R.id.registroPost);
        registro.setOnClickListener(this);

        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Método que se ejecuta al hacer click en alguno de los botones capturados
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_obtener_fecha:
                FechaUtil fechaUtil = new FechaUtil();
                etFecha = fechaUtil.obtenerFecha(this, etFecha);
                break;

            case R.id.botonInteresRegistro:
                listenerButtonIntereses(v);
                break;

            case R.id.registroPost:
                registro();
                break;
        }
    }

    public void updateUI(FirebaseUser user) {

        SAUsuario saUsuario = new SAUsuarioImp();
        saUsuario.get(user.getUid(), new MyCallBack() {
            @Override
            public void onCallbackUsuario(User value) {
                AutorizacionFirebase.setUsuario(value);
                Intent myIntent = new Intent(RegistroActivity.this, MenuLateralActivity.class);
                RegistroActivity.this.startActivity(myIntent);
                //RegistroActivity.this.onResume();
                finish();
            }

            @Override
            public void onCallbackActividad(Actividad actividad) { }

            @Override
            public void onCallbackActividadAll(ArrayList<Actividad> actividad) { }
        });
    }


    /* ------------ MÉTODOS PRIVADOS ------------ */

    /**
     * Método que configura el botón de seleccionar intereses
     * @param view
     */
    private void listenerButtonIntereses(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegistroActivity.this);
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

    /**
     * Método que coge los datos del usuario de la vista, comprueba si son validos,
     * y crea un nuevo usuario en la base de datos
     */
    private void registro() {

        miUsuario = checkInputUsuario(this, listItems, checkedItems);

        if(miUsuario != null) {
            AutorizacionFirebase.getFirebaseAuth().createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = AutorizacionFirebase.getCurrentUser();

                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("server/saving-data/fireblog");
                                SAUsuario miSaUsuario = new SAUsuarioImp();

                                String uid = user.getUid();
                                miUsuario.setUid(uid);
                                miSaUsuario.save(miUsuario);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                Toast.makeText(RegistroActivity.this, "Email ya registrado, por favor utilice otro",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });//addOnCompleteListener
        }
    }

    /**
     * Método que comprueba si son validos los datos del Usuario
     * @param activity
     * @param listItems
     * @param checkedItems
     * @return si son correctos o no todos los datos
     */
    private User checkInputUsuario(
            Activity activity,
            String[] listItems,
            boolean[] checkedItems
    ) {
        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = etNombre.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();
        String fechaNacString = etFecha.getText().toString();
        String descripcion = etDescripcion.getText().toString();

        //QUITAR ESPACIOS AL PRINCIPIO Y FINAL DE CADA INPUT
        nombre = nombre.trim();
        email = email.trim();
        password = password.trim();
        passwordConfirm = passwordConfirm.trim();
        fechaNacString = fechaNacString.trim();
        descripcion = descripcion.trim();

        boolean nombreOk = false;
        boolean emailOk = false;
        boolean passwordOk = false;
        boolean fechaNacOK = false;

        //INICIALIZAR
        boolean unlessOneInteres = false;
        Date fechaNacimiento = null;

        View focusView = null;
        User user = null;

        // --- CHECKS --- //
        ParserUsuario pu = new ParserUsuario();

        //NOMBRE
        nombre = pu.procesarNombre(nombre, etNombre, focusView);

        //EMAIL
        email = pu.procesarEmail(email, etEmail, focusView);

        //PASSWORD
        password = pu.procesarPassword(password, etPassword, focusView);

        //PASSWORD CONFIRM
        passwordConfirm = pu.procesarPasswordAndSamePass(password, passwordConfirm, etPasswordConfirm, focusView);

        //FECHA NACIMIENTO
        fechaNacimiento = pu.procesarFechaNacimiento(fechaNacString, etFecha, focusView);

        //INTERESES
        Pair<Boolean, ArrayList<Category>> resIntereses = pu.procesarIntereses(listItems, checkedItems, activity);
        unlessOneInteres = resIntereses.first;

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        // -------------- //

        if(focusView != null)
            focusView.setFocusable(true);

        if(nombre != null && email != null && password != null && passwordConfirm != null && fechaNacimiento != null && unlessOneInteres) {
            userEmail = email;
            userPassword = password;
            user = new User(email, nombre, fechaNacimiento, descripcion, resIntereses.second);
        }

        return user;
    }

}

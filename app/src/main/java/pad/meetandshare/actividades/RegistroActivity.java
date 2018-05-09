package pad.meetandshare.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import static android.support.constraint.Constraints.TAG;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Categoria;
import pad.meetandshare.negocio.modelo.Usuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuario;
import pad.meetandshare.negocio.servicioAplicacion.SAUsuarioImp;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.util.Log;


public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    private EditText etFecha;
    private ImageButton ibObtenerFecha;
    private Button interesesBoton;
    private Button registro;

    private Date fecha;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();

    private FirebaseAuth mAuth;
    private Usuario miUsuario;

    /* ------------ MÉTODOS PÚBLICOS ------------ */

    /* --------- ACTIVIDAD --------- */
    /**
     * Método que se ejecuta al crear la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listItems = Categoria.getArray();
        checkedItems = new boolean[listItems.length];
        setContentView(R.layout.activity_registro);

        //FECHA
        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) findViewById(R.id.fechaNacimientoRegistro);

        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        ibObtenerFecha.setOnClickListener(this);

        //INTERESES
        interesesBoton = (Button) findViewById(R.id.botonInteresRegistro);
        listenerButtonIntereses(interesesBoton);

        //REGISTRO
        registro = (Button) findViewById(R.id.registroPost);
        registro.setOnClickListener(this);

        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Método que se ejecuta al hacer click en alguno de los botones capturados
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                FechaUtil fechaUtil = new FechaUtil();
                fechaUtil.obtenerFecha(this, R.id.fechaNacimientoRegistro);
                break;

            case R.id.registroPost:
                registro();
                break;
        }
    }

    public void updateUI(FirebaseUser user){

    }


    /* ------------ MÉTODOS PRIVADOS ------------ */

    private void registro(){

        //OBTENER ELEMENTOS DE LA VISTA
        String nombre = ((EditText) findViewById(R.id.nombreRegistro)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailRegistro)).getText().toString();
        String contrasenia = ((EditText) findViewById(R.id.passwordRegistro)).getText().toString();
        String contraseniaConfirm = ((EditText) findViewById(R.id.passwordConfirmationRegistro)).getText().toString();
        String fechaString =((EditText) findViewById(R.id.fechaNacimientoRegistro)).getText().toString();
        String descripcion = ((EditText) findViewById(R.id.descripcionRegistro)).getText().toString();

        ArrayList<Categoria> intereses = new ArrayList<>();

        for(int i = 0; i < checkedItems.length; ++i){
            if(checkedItems[i]){
                Categoria cat = Categoria.getCategoria(listItems[i]);
                intereses.add(cat);
            }
        }

        if(checkInputRegistro(nombre, email, contrasenia, contraseniaConfirm, fechaString, descripcion)) {

            miUsuario = new Usuario(email, nombre, fecha, descripcion, intereses);

            mAuth=FirebaseAuth.getInstance();

            mAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("server/saving-data/fireblog");
                        SAUsuario miSaUsuario = new SAUsuarioImp();

                        miSaUsuario.save(miUsuario, user.getUid());
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // ...
                }
            });
        }
    }

    private void listenerButtonIntereses(Button intereses) {

        interesesBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegistroActivity.this);
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

    private boolean checkInputRegistro(String nombre, String email, String password, String passwordConfirm, String fechaNacString, String descripcion) {

        boolean nombreOk = false;
        boolean emailOk = false;
        boolean passwordOk = false;
        boolean fechaNacOK = false;
        View focusView = null;

        //QUITAR ESPACIOS AL PRINCIPIO Y FINAL DE CADA INPUT
        nombre = nombre.trim();
        email = email.trim();
        password = password.trim();
        passwordConfirm = passwordConfirm.trim();
        fechaNacString = fechaNacString.trim();
        descripcion = descripcion.trim();

        final String campoObligatorio = "Por favor, rellene todos los campos";

        //NOMBRE
        EditText etNombre = (EditText) findViewById(R.id.nombreRegistro);
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

        //EMAIL
        EditText etEmail = (EditText) findViewById(R.id.emailRegistro);
        if (email == null || email.isEmpty()) {
            etEmail.setError(campoObligatorio);
            focusView = etEmail;
        } else if (!Usuario.isValidEmail(email)) {
            etEmail.setError("El email introducido no es válido, debe ser de la forma 'usuario@dominio.xxx'");
            focusView = etEmail;
        } else {
            emailOk = true;
        }

        //PASSWORD
        EditText etPassword = (EditText) findViewById(R.id.passwordRegistro);
        if (password == null || password.isEmpty()) {
            etPassword.setError(campoObligatorio);
            focusView = etPassword;
        } else if (!Usuario.isValidPassword(password)) {
            etPassword.setError("La contraseña introducida no es válida, debe contener al menos 6 caracteres");
            focusView = etPassword;
        } else {
            //PASSWORD CONFIRMACION
            EditText etPasswordConfirm = (EditText) findViewById(R.id.passwordConfirmationRegistro);
            if (passwordConfirm == null || passwordConfirm.isEmpty()) {
                etPasswordConfirm.setError(campoObligatorio);
                focusView = etPassword;
            } else if (password.compareTo(passwordConfirm) != 0) {
                etPasswordConfirm.setError("Las contraseñas deben coincidir");
                focusView = etPasswordConfirm;
            } else {
                passwordOk = true;
            }
        }

        //FECHA NACIMIENTO
        EditText etFechaNacim = (EditText) findViewById(R.id.fechaNacimientoRegistro);
        if (fechaNacString == null || fechaNacString.isEmpty()) {
            etFechaNacim.setError(campoObligatorio);
            focusView = etFechaNacim;
        } else {
            try {
                fecha = FechaUtil.getDateFormat().parse(fechaNacString);

                if(!Usuario.isValidFechaNacimiento(fecha)) {
                    etFechaNacim.setError("Debes ser mayor de edad");
                    focusView = etFechaNacim;
                }
                else{
                    fechaNacOK = true;
                }
            } catch (ParseException e) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Formato de fecha incorrecto", Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER, 0, 0);
                toast1.show();
            }
        }

        //INTERESES
        int i = 0;
        boolean unlessOneInteres = false;
        while(i < this.checkedItems.length && !unlessOneInteres) {
            unlessOneInteres = checkedItems[i];
            i++;
        }

        if(!unlessOneInteres) {
            Toast toast2 = Toast.makeText(getApplicationContext(), "Debes seleccionar al menos un interés", Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }

        //DESCRIPCION
        if (descripcion == null) {
            descripcion = "";
        }

        return (nombreOk && emailOk && passwordOk && fechaNacOK && unlessOneInteres);
    }

}

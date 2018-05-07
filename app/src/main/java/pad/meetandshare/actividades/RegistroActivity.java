package pad.meetandshare.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;

import android.view.Gravity;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import static android.support.constraint.Constraints.TAG;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Widgets
    private EditText etFecha;
    private ImageButton ibObtenerFecha;
    private Button interesesBoton;

    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();


    private FirebaseAuth mAuth;
    private Usuario miUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listItems = Categoria.getArray();
        checkedItems = new boolean[listItems.length];
        setContentView(R.layout.activity_registro);

        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        //Evento setOnClickListener - clic
        ibObtenerFecha.setOnClickListener(this);


        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //INTERESES
        interesesBoton = (Button) findViewById(R.id.botonInteresRegistro);

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


        Button registro = (Button) findViewById(R.id.registroPost);

        registro.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;

            case R.id.registroPost:

                registro();
                break;

        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }

            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget

        recogerFecha.show();

    }


    private void registro(){


        //OBTENER ELEMENTOS DE LA VISTA

        String nombre = ((EditText) findViewById(R.id.nombreRegistro)).getText().toString();
        String fechaString =((EditText) findViewById(R.id.et_mostrar_fecha_picker)).getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date fecha = dateFormat.parse(fechaString);


            String email = ((EditText) findViewById(R.id.emailRegistro)).getText().toString();
            String contrasenia = ((EditText) findViewById(R.id.passwordRegistro)).getText().toString();
            String contraseniaConfirm = ((EditText) findViewById(R.id.passwordConfirmationRegistro)).getText().toString();
            miUsuario = new Usuario(email,nombre,fecha,contrasenia);


            boolean cancel=false;
            View focusView = null;

            if(email.isEmpty()){
                cancel=true;
                ((EditText) findViewById(R.id.emailRegistro)).setError("El email no puede ser valido");
                focusView=((EditText) findViewById(R.id.emailRegistro));
            }else{
                if(!email.contains("@")){
                    cancel=true;
                    ((EditText) findViewById(R.id.emailRegistro)).setError("El email debe de ser un email valido");
                    focusView=((EditText) findViewById(R.id.emailRegistro));
                }else{
                    if(contrasenia.compareTo(contraseniaConfirm)!=0){
                        ((EditText) findViewById(R.id.passwordRegistro)).setError("Las contraseñas deben coincidir");
                        focusView = findViewById(R.id.passwordRegistro);
                        cancel=true;
                    }

                }
            }



            if(!cancel) {
                mAuth=FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email, contrasenia )
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
        catch (ParseException e){

            Toast toast1 = Toast.makeText(getApplicationContext(), "Formato de fecha incorrecto", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();                }
    }




    public void updateUI(FirebaseUser user){



    }



}

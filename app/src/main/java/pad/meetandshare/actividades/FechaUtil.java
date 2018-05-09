package pad.meetandshare.actividades;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pad.meetandshare.R;

public class FechaUtil {

    EditText etFecha;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public static final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    public static final int mes = c.get(Calendar.MONTH);
    public static final int dia = c.get(Calendar.DAY_OF_MONTH);
    public static final int anio = c.get(Calendar.YEAR);

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public FechaUtil () {}

    public void obtenerFecha(AppCompatActivity activity, int id){

        etFecha = (EditText) activity.findViewById(id);

        DatePickerDialog recogerFecha = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
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
        },anio, mes, dia);
        //Muestro el widget

        recogerFecha.show();
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

}

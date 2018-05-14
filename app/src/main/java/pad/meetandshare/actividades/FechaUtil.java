package pad.meetandshare.actividades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pad.meetandshare.R;

public class FechaUtil {

    EditText etFecha;
    EditText etHora;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public static final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    public static final int mes = c.get(Calendar.MONTH);
    public static final int dia = c.get(Calendar.DAY_OF_MONTH);
    public static final int anio = c.get(Calendar.YEAR);

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);


    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat dateWithHourFormat = new SimpleDateFormat("dd/MM/yyyy  hh:mm");

    public FechaUtil () {}

    public void obtenerFecha(Activity activity, int id){

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

    public void obtenerHora(Activity activity, int id){

        etHora = (EditText) activity.findViewById(id);

        TimePickerDialog recogerHora = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static SimpleDateFormat getDateWithHourFormat() { return dateWithHourFormat; }

    public static String horaCorrectFormat(String horaIniString) {
        horaIniString = horaIniString.substring(0,5);//eliminar am / pm
        horaIniString += ":00";//añadir segs
        return horaIniString;
    }

    public static Date dateCorrectFormat(String fechaIniString, String horaIniString) {
        String days = fechaIniString.substring(0, 2);
        String months = fechaIniString.substring(3, 5);
        String years = fechaIniString.substring(6, 10);

        return new Date(years + "/" + months + "/" + days + " " + horaIniString);
    }

}

package pad.meetandshare.actividades.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FechaUtil {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat dateWithHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final static SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

    private EditText etFecha;
    private EditText etHora;

    public FechaUtil() {
        etFecha = null;
        etHora = null;
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static SimpleDateFormat getDateWithHourFormat() {
        return dateWithHourFormat;
    }

    public static String horaCorrectFormat(String horaIniString) {
        horaIniString = horaIniString.substring(0, 5);//eliminar am / pm
        horaIniString += ":00";//añadir segs
        return horaIniString;
    }

    public static Date dateCorrectFormat(String fechaIniString, String horaIniString) {
        String days = fechaIniString.substring(0, 2);
        String months = fechaIniString.substring(3, 5);
        String years = fechaIniString.substring(6, 10);

        return new Date(years + "/" + months + "/" + days + " " + horaIniString);
    }

    public EditText obtenerFecha(Activity activity, EditText etFechaAux) {

        etFecha = etFechaAux;

        Calendar c = Calendar.getInstance();
        DatePickerDialog recogerFecha = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }

            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        //Muestro el widget
        recogerFecha.show();

        return etFecha;
    }

    public EditText obtenerHora(Activity activity, EditText etHoraAux) {

        etHora = etHoraAux;

        Calendar c = Calendar.getInstance();
        TimePickerDialog recogerHora = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf("0" + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, c.get(Calendar.HOUR_OF_DAY),  c.get(Calendar.MINUTE), false);

        recogerHora.show();

        return etHora;
    }

    public static String horaMostrarString(Date fecha) {
        String am_pm = "a.m.";
        String hora = hourFormat.format(fecha);
        String res = "";
        String auxHora = "";

        int horas = Integer.parseInt(hora.substring(0,2));

        if(horas > 12) {
            am_pm = "p.m.";
        }
        else if(horas == 12) {
            am_pm = "p.m.";
        }

        if(horas < 10)
            auxHora = "0";

        res = auxHora + horas + hora.substring(2) + " " + am_pm;

        return res;
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha); // Configuramos la fecha que se recibe

        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

}

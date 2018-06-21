package pad.meetandshare.actividades.ParserObjects;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pad.meetandshare.negocio.modelo.FechaUtil;
import pad.meetandshare.negocio.modelo.Ubicacion;

public class ParserActividad extends Parser {

    private static final String NOMBRE_PATTERN = "^([0-9a-zA-ZáéíóúñÁÉÍÓÚÑçÇ¡!¿? ])*$";

    private static final int PARSE_COD_FORMATO_INCORRECTO = 201;
    private static final String PE_MS_FORMATO_INCORRECTO = "Formato incorrecto";

    //Nombre
    private static final int PARSE_COD_NOMBRE_CARACTERES_INVALIDOS = 202;
    private static final String PE_MS_NOMBRE_CARACTERES_INVALIDOS = "Este campo contiene caracteres no válidos";

    //FechaIni
    private static final int PARSE_COD_FECHA_INI_BEFORE_TODAY = 203;
    private static final String PE_MS_FECHA_INI_BEFORE_TODAY = "La fecha de inicio debe ser posterior a la actual";

    //FechaFin
    private static final int PARSE_COD_FECHA_FIN_BEFORE_FECHA_INI = 204;
    private static final String PE_MS_FECHA_FIN_BEFORE_FECHA_INI = "La fecha de fin debe ser posterior a la de inicio";

    //MaxParticipantes
    private static final int PARSE_COD_MAX_PARTICIPANTES_LESS_THAN_MIN = 205;
    private static final String PE_MS_MAX_PARTICIPANTES_LESS_THAN_MIN = "¡No puedes estar tú solo!";

    private static final int PARSE_COD_MAX_PARTICIPANTES_LESS_THAN_INSCRITOS = 206;
    private static final String PE_MS_MAX_PARTICIPANTES_LESS_THAN_INSCRITOS = "¡No puedes echar a nadie!";


    public ParserActividad() { }


    // ---- CHECKS ---- //

    //Nombre
    public static boolean isValidNombre(String nombre) throws ParseException {
        Pattern pattern = Pattern.compile(NOMBRE_PATTERN);
        Matcher matcher = pattern.matcher(nombre);

        if (!matcher.matches())
            throw new ParseException(PE_MS_NOMBRE_CARACTERES_INVALIDOS, PARSE_COD_NOMBRE_CARACTERES_INVALIDOS);

        return true;
    }

    //Fechas y horas
    public static Date parseFecha(String fechaStr) throws ParseException {
        Date fecha = null;
        try {
            fecha = FechaUtil.getDateFormat().parse(fechaStr);
        } catch(ParseException pe) {
            throw new ParseException(PE_MS_FORMATO_INCORRECTO, PARSE_COD_FORMATO_INCORRECTO);
        }
        return fecha;
    }


    public static boolean isOnlyFechaIniLaterThanToday(Date fechaIni) throws ParseException {

        Date todayWithHour = new Date();
        String todayStr = FechaUtil.getDateFormat().format(todayWithHour);

        Date today = FechaUtil.dateCorrectFormat(todayStr, "00:00");

        if(fechaIni.before(today))
            throw new ParseException(PE_MS_FECHA_INI_BEFORE_TODAY, PARSE_COD_FECHA_INI_BEFORE_TODAY);

        return true;//hoy o despues
    }


    public static boolean isValidFechaIni(Date fechaIni) throws ParseException {
        //fecha Ini despues de ahora
        if (fechaIni.before(new Date()))
            throw new ParseException(PE_MS_FECHA_INI_BEFORE_TODAY, PARSE_COD_FECHA_INI_BEFORE_TODAY);

        return true;
    }

    public static boolean isValidHora(String horaString) throws ParseException {
        String hora[] = horaString.split(":");
        String horas = hora[0];
        String minutos = hora[1];

        if((Integer.parseInt(horas) >= 24 || Integer.parseInt(horas) < 0 )
                || (Integer.parseInt(minutos) >= 60 && Integer.parseInt(minutos) < 0)) {
            throw new ParseException(PE_MS_FORMATO_INCORRECTO, PARSE_COD_FORMATO_INCORRECTO);
        }

        return true;
    }

    public static boolean isOnlyFechaFinLaterThanFechaIni(String fechaIniString, String fechaFinString) throws ParseException {

        Date fechaIniWithOutHour = FechaUtil.getDateFormat().parse(fechaIniString);
        Date fechaFinWithOutHour = FechaUtil.getDateFormat().parse(fechaFinString);

        if (fechaFinWithOutHour.before(fechaIniWithOutHour))
            throw new ParseException(PE_MS_FECHA_FIN_BEFORE_FECHA_INI, PARSE_COD_FECHA_FIN_BEFORE_FECHA_INI);

        return true;
    }

    public static boolean isValidFechaFin(Date fechaIni, Date fechaFin) throws ParseException {

        if(fechaFin.before(fechaIni))
            throw new ParseException(PE_MS_FECHA_FIN_BEFORE_FECHA_INI, PARSE_COD_FECHA_FIN_BEFORE_FECHA_INI);

        return true;
    }

    //Max participantes
    public static boolean isValidMaxParticipantes(int maxParticipantes) throws ParseException {

        if(maxParticipantes <= 1)
            throw new ParseException(PE_MS_MAX_PARTICIPANTES_LESS_THAN_MIN, PARSE_COD_MAX_PARTICIPANTES_LESS_THAN_MIN);

        return true;
    }

    public static boolean isValidMaxParticipantesModificar(int maxParticipantes, int numInscritos) throws ParseException {

        if(maxParticipantes < numInscritos)
            throw new ParseException(PE_MS_MAX_PARTICIPANTES_LESS_THAN_INSCRITOS, PARSE_COD_MAX_PARTICIPANTES_LESS_THAN_INSCRITOS);

        return true;
    }

    // ---- PROCESAR ---- //

    private void setError(EditText et, String err, View focusView) {
        et.setError(err);
        if(focusView != null)
            focusView = et;
    }


    public String procesarNombre(String nombre, EditText etNombre, View focusView) {
        boolean nombreOK = false;

        try {
            nombreOK = (isNotNull(nombre)
                    && isNotEmptyString(nombre)
                    && ParserActividad.isValidNombre(nombre));
        } catch(ParseException pe) {
            setError(etNombre, pe.getMessage(), focusView);
        }

        if(!nombreOK)
            nombre = null;

        return nombre;
    }

    public String procesarFechaIniSinHora(String fechaIniString, EditText etFechaIni, View focusView) {
        boolean fechaIniOK = false;

        try {
            fechaIniOK = (isNotNull(fechaIniString)
                    && isNotEmptyString(fechaIniString));

            if(fechaIniOK) {
                Date fechaIni = ParserActividad.parseFecha(fechaIniString);
                fechaIniOK = (ParserActividad.isOnlyFechaIniLaterThanToday(fechaIni));
            }
        } catch(ParseException pe) {
            fechaIniOK = false;
            setError(etFechaIni, pe.getMessage(), focusView);
        }

        if(!fechaIniOK)
            fechaIniString = null;

        return fechaIniString;
    }

    public String procesarHoraIni(String horaIniString, EditText etHoraIni, View focusView) {
        boolean horaIniOK = false;

        try {
            horaIniOK = (isNotNull(horaIniString)
                    && isNotEmptyString(horaIniString));

            if(horaIniOK) {
                horaIniString = FechaUtil.horaCorrectFormat(horaIniString);
                horaIniOK = (ParserActividad.isValidHora(horaIniString));
            }
        } catch(ParseException pe) {
            horaIniOK = false;
            setError(etHoraIni, pe.getMessage(), focusView);
        }

        if(!horaIniOK)
            horaIniString = null;

        return horaIniString;
    }

    public Date procesarFechaIniCompleta(
            String fechaIniString,
            String horaIniString,
            EditText etHoraIni,
            View focusView
    ) {
        Date fechaIni = null;
        boolean horaIniOK = false;

        try {
            if (fechaIniString != null && horaIniString != null) {
                fechaIni = FechaUtil.dateCorrectFormat(fechaIniString, horaIniString);
                horaIniOK = ParserActividad.isValidFechaIni(fechaIni);
            }
        } catch(ParseException pe) {
            horaIniOK = false;
            setError(etHoraIni, pe.getMessage(), focusView);
        }

        if(!horaIniOK)
            fechaIni = null;

        return fechaIni;
    }

    public String procesarFechaFinSinHora(
            String fechaFinString,
            String fechaIniString,
            EditText etFechaFin,
            View focusView
    ) {
        boolean fechaFinOK = false;

        try {
            fechaFinOK = (isNotNull(fechaFinString)
                    && isNotEmptyString(fechaFinString));

            if(fechaFinOK) {
                Date fechaFin = ParserActividad.parseFecha(fechaFinString);
                if(fechaIniString != null)
                    fechaFinOK = (ParserActividad.isOnlyFechaFinLaterThanFechaIni(fechaIniString, fechaFinString));
            }
        } catch(ParseException pe) {
            fechaFinOK = false;
            setError(etFechaFin, pe.getMessage(), focusView);
        }

        if(!fechaFinOK)
            fechaFinString = null;

        return fechaFinString;
    }

    public String procesarHoraFin(String horaFinString, EditText etHoraFin, View focusView) {
        boolean horaFinOK = false;

        try {
            horaFinOK = (isNotNull(horaFinString)
                    && isNotEmptyString(horaFinString));

            if(horaFinOK) {
                horaFinString = FechaUtil.horaCorrectFormat(horaFinString);
                horaFinOK = (ParserActividad.isValidHora(horaFinString));
            }
        } catch(ParseException pe) {
            horaFinOK = false;
            setError(etHoraFin, pe.getMessage(), focusView);
        }

        if(!horaFinOK)
            horaFinString = null;

        return horaFinString;
    }

    public Date procesarFechaFinCompleta(
        Date fechaIni,
        String fechaFinString,
        String horaFinString,
        EditText etHoraFin,
        View focusView
    ) {
        Date fechaFin = null;
        boolean horaFinOK = false;

        try {
            if (fechaIni != null && fechaFinString != null && horaFinString != null) {
                fechaFin = FechaUtil.dateCorrectFormat(fechaFinString, horaFinString);
                horaFinOK = ParserActividad.isValidFechaFin(fechaIni, fechaFin);
            }
        } catch(ParseException pe) {
            horaFinOK = false;
            setError(etHoraFin, pe.getMessage(), focusView);
        }

        if(!horaFinOK)
            fechaFin = null;

        return fechaFin;
    }

    public Integer procesarMaxParticipantes(String maxParticipantesString, EditText etMaxParticipantes, View focusView) {
        boolean maxParticipantesOK = false;
        Integer maxParticipantes = null;

        try {
            maxParticipantesOK = (isNotNull(maxParticipantesString)
                    && isNotEmptyString(maxParticipantesString));

            if(maxParticipantesOK) {
                maxParticipantes = Integer.parseInt(maxParticipantesString);
                maxParticipantesOK = ParserActividad.isValidMaxParticipantes(maxParticipantes);
            }
        } catch(ParseException pe) {
            maxParticipantesOK = false;
            setError(etMaxParticipantes, pe.getMessage(), focusView);
        }

        if(!maxParticipantesOK)
            maxParticipantes = null;

        return maxParticipantes;
    }

    public boolean procesarUbicacion(Ubicacion ubicacion, Activity activity) {
        if(ubicacion == null) {
            Toast toast3 = Toast.makeText(activity, "Selecciona una ubicación", Toast.LENGTH_SHORT);
            toast3.setGravity(Gravity.CENTER, 0, 0);
            toast3.show();
            return false;
        }
        else
            return true;
    }
}

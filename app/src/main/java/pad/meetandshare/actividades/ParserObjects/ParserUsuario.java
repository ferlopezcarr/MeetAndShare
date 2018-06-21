package pad.meetandshare.actividades.ParserObjects;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Usuario;

public class ParserUsuario extends Parser {

    private static final String EMAIL_PATTERN = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
    private static final String NOMBRE_PATTERN = "^([0-9a-zA-ZáéíóúñÁÉÍÓÚÑ ])*$";

    private static final String EMAIL_EJEMPLO = "ejemplo@ejemplo.es";
    private static final int EMAIL_MIN_LENGTH = 5;

    private static final int PASSWORD_MIN_LENGTH = 6;

    private static final int FECHA_NAC_MIN_EDAD = 18;

    //Email
    private static final int PARSE_COD_EMAIL_PATTERN = 301;
    private static final String PE_MS_EMAIL_PATTERN = "Email inválido, debe ser de la forma: "+EMAIL_EJEMPLO;

    //Nombre
    private static final int PARSE_COD_NOMBRE_PATTERN = 302;
    private static final String PE_MS_NOMBRE_PATTERN = "Email inválido, solo puede contener números o letras";

    //Passwords
    private static final int PARSE_COD_PASSWORD_LENGTH = 303;
    private static final String PE_MS_PASSWORD_LENGTH = "La contraseña debe contener al menos "+PASSWORD_MIN_LENGTH+" caracteres";

    private static final int PARSE_COD_PASSWORDS_DINDT_MATCH = 304;
    private static final String PE_MS_PASSWORDS_DINDT_MATCH = "La contraseñas no coinciden";

    //Fecha Nacim
    private static final int PARSE_COD_FECHA_NAC_MENOR_EDAD = 305;
    private static final String PE_MS_FECHA_NAC_MENOR_EDAD = "Debes ser mayor de "+FECHA_NAC_MIN_EDAD+" años";


    // ---- CHECKS ---- //

    public ParserUsuario() {}

    //Email
    public static boolean isValidEmail(String email) throws ParseException {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches())//"x@x.x"
            throw new ParseException(PE_MS_EMAIL_PATTERN, PARSE_COD_EMAIL_PATTERN);

        return true;
    }

    public static boolean isValidEmailLength(String email) throws ParseException {
        if(email.length() < EMAIL_MIN_LENGTH)
            throw new ParseException(PE_MS_EMAIL_PATTERN, PARSE_COD_EMAIL_PATTERN);

        return true;
    }

    //Nombre
    public static boolean isValidNombre(String nombre) throws ParseException {
        Pattern pattern = Pattern.compile(NOMBRE_PATTERN);
        Matcher matcher = pattern.matcher(nombre);
        if (!matcher.matches())
            throw new ParseException(PE_MS_NOMBRE_PATTERN, PARSE_COD_NOMBRE_PATTERN);

        return true;
    }

    //Passwords
    public static boolean isValidPassword(String password) throws ParseException {
        if (password.length() < PASSWORD_MIN_LENGTH)
            throw new ParseException(PE_MS_PASSWORD_LENGTH, PARSE_COD_PASSWORD_LENGTH);

        return true;
    }

    //Same pass
    public static boolean isValidSamePasswords(String password, String samePass) throws ParseException {
        if (!password.equalsIgnoreCase(samePass))
            throw new ParseException(PE_MS_PASSWORDS_DINDT_MATCH, PARSE_COD_PASSWORDS_DINDT_MATCH);

        return true;
    }

    //Fecha Nacimiento
    public static boolean isValidFechaNacimiento(Date fechaNacimiento) throws ParseException {
        Calendar calendarMayorEdad = Calendar.getInstance();

        int anio = Calendar.getInstance().get(Calendar.YEAR) - FECHA_NAC_MIN_EDAD;
        calendarMayorEdad.set(Calendar.YEAR, anio);

        Date fechaMayorEdad = calendarMayorEdad.getTime();

        int resCompareDates = fechaNacimiento.compareTo(fechaMayorEdad);

        if (resCompareDates > 0)
            throw new ParseException(PE_MS_FECHA_NAC_MENOR_EDAD, PARSE_COD_FECHA_NAC_MENOR_EDAD);

        return true;//<=
    }

    // ---- PROCESAR ---- //

    public String procesarEmail(String email, EditText etEmail, View focusView) {
        boolean emailOK = false;

        try {
            emailOK = (Parser.isNotNull(email)
                    && Parser.isNotEmptyString(email)
                    && ParserUsuario.isValidEmailLength(email)
                    && ParserUsuario.isValidEmail(email));
        } catch(ParseException pe) {
            setError(etEmail, pe.getMessage(), focusView);
        }

        if(!emailOK)
            email = null;

        return email;
    }

    public String procesarNombre(String nombre, EditText etNombre, View focusView) {
        boolean nombreOK = false;

        try {
            nombreOK = (Parser.isNotNull(nombre)
                    && Parser.isNotEmptyString(nombre)
                    && ParserUsuario.isValidNombre(nombre));
        } catch(ParseException pe) {
            setError(etNombre, pe.getMessage(), focusView);
        }

        if(!nombreOK)
            nombre = null;

        return nombre;
    }

    public String procesarPassword(String password, EditText etPassword, View focusView) {
        boolean passwordOK = false;

        try {
            passwordOK = (Parser.isNotNull(password)
                    && Parser.isNotEmptyString(password)
                    && ParserUsuario.isValidPassword(password));
        } catch(ParseException pe) {
            setError(etPassword, pe.getMessage(), focusView);
        }

        if(!passwordOK)
            password = null;

        return password;
    }

    public String procesarPasswordAndSamePass(String password, String samePass, EditText etSamePass, View focusView) {
        boolean samePasswordOK = false;

        try {
            samePasswordOK = (Parser.isNotNull(samePass)
                    && Parser.isNotEmptyString(samePass)
                    && ParserUsuario.isValidPassword(samePass)
                    && ParserUsuario.isValidSamePasswords(password, samePass));
        } catch(ParseException pe) {
            setError(etSamePass, pe.getMessage(), focusView);
        }

        if(!samePasswordOK)
            samePass = null;

        return password;
    }

    public Date procesarFechaNacimiento(String fechaNacString, EditText etFechaNac, View focusView) {
        boolean fechaNacOK = false;
        Date fechaNac = null;

        try {
            fechaNacOK = (Parser.isNotNull(fechaNacString)
                    && Parser.isNotEmptyString(fechaNacString));

            if(fechaNacOK) {
                fechaNac = Parser.parseFecha(fechaNacString);
                fechaNacOK = ParserUsuario.isValidFechaNacimiento(fechaNac);
            }
        } catch(ParseException pe) {
            fechaNacOK = false;
            setError(etFechaNac, pe.getMessage(), focusView);
        }

        if(!fechaNacOK)
            fechaNac = null;
        else {
            etFechaNac.getError();
        }

        return fechaNac;
    }

}

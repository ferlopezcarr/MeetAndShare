package pad.meetandshare.actividades.ParserObjects;

import java.text.ParseException;

public class ParserUsuario extends Parser {

    private static final int PARSE_COD_NULL = 00;
    private static final int PARSE_COD_STRING_EMPTY = 01;

    private static final String PE_MS_CAMPO_OBLIGATORIO = "Por faver, rellena el campo obligatorio";

    public boolean isNull(Object obj) throws ParseException {
        if (obj == null)
            throw new ParseException(PE_MS_CAMPO_OBLIGATORIO, PARSE_COD_NULL);

        return true;
    }

    public boolean isNotEmpty(String str) throws ParseException {
        if (str.isEmpty())
            throw new ParseException(PE_MS_CAMPO_OBLIGATORIO, PARSE_COD_STRING_EMPTY);

        return true;
    }
}

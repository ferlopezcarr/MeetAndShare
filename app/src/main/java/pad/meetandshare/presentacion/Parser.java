package pad.meetandshare.presentacion;

import android.app.Activity;
import android.util.Pair;
import android.view.Gravity;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

import pad.meetandshare.negocio.modelo.Categoria;

public class Parser {

    private static final int PARSE_COD_NULL = 000;
    private static final int PARSE_COD_STRING_EMPTY = 001;

    private static final String PE_MS_CAMPO_OBLIGATORIO = "Por favor, rellena el campo obligatorio";
    private static final String MS_INTERESES = "Debes seleccionar al menos un interés";

    public static boolean isNotNull(Object obj) throws ParseException {
        if (obj == null)
            throw new ParseException(PE_MS_CAMPO_OBLIGATORIO, PARSE_COD_NULL);

        return true;
    }

    public static boolean isNotEmptyString(String str) throws ParseException {
        if (str.isEmpty())
            throw new ParseException(PE_MS_CAMPO_OBLIGATORIO, PARSE_COD_STRING_EMPTY);

        return true;
    }

    public Pair<Boolean, ArrayList<Categoria>> procesarIntereses(String[] listItems, boolean[] checkedItems, Activity activity) {
        Boolean unlessOneInteres = false;

        ArrayList<Categoria> intereses = new ArrayList<>();

        for (int i = 0; i < checkedItems.length; ++i) {
            if (checkedItems[i]) {
                Categoria cat = Categoria.getCategoria(listItems[i]);
                intereses.add(cat);
            }
        }

        int i = 0;
        while (i < checkedItems.length && !unlessOneInteres) {
            unlessOneInteres = checkedItems[i];
            i++;
        }

        if (!unlessOneInteres) {
            Toast toast2 = Toast.makeText(activity, MS_INTERESES, Toast.LENGTH_SHORT);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }

        return new Pair<>(unlessOneInteres, intereses);
    }

}

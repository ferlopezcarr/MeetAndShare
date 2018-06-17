package pad.meetandshare.integracion;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static android.support.constraint.Constraints.TAG;

public class ColorFile {

    //http://paletton.com/#uid=15z0u0kkQm7agxYf-rppWh2vlbA
    public static final Float ADMIN_COLOR = (float) 270;//morado mias
    public static final Float PARTICIPANT_COLOR = (float) 212;//azul inscrito
    public static final Float ACT_STARTS_TOMORROW_COLOR = (float) 60;//amarillo 1 dia para que empiece
    public static final Float DEFAULT_COLOR = (float) 354;//rojas otros

    public static final String ADMIN_COLOR_RGB = "5B2D76";
    public static final String PARCITIPANT_COLOR_RGB = "5B2D76";
    public static final String ACT_STARTS_TOMORROW_COLOR_RGB = "5B2D76";
    public static final String DEFAULT_COLOR_RGB = "5B2D76";

    public static final int TIME_DIFFERENCE = 1;

    private static final String FILE = "color.txt";

    /*
    public static boolean existFile(Activity activity) {
        AssetManager assetManager = activity.getAssets();
        InputStream inputStream = null;

        boolean ok = false;

        try {
            inputStream = assetManager.open(FILE);
            ok = true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            ok = false;
        }
        finally{
            if( inputStream != null ) {
                try{
                    inputStream.close();
                }
                catch(IOException e) {
                    Log.e(TAG, e.getMessage());
                    ok = false;
                }
            }
        }
        return ok;
    }
    */

    /*
    public static Color floatToColor(Float x) {
        int red = Math.min((int)(x * 256), 255);
        int green = Math.min((int)((x * 256 - red) * 256), 255);
        int blue = Math.min((int)(((x * 256 - red) * 256 - green) * 256), 255);

        return Color.valueOf(red, green, blue);
    }
    */

    public static boolean read(Float color, Activity activity) {
        try {
            InputStreamReader archivo = new InputStreamReader(
                    activity.getBaseContext().openFileInput(FILE));
            // Creamos un objeto buffer, en el que iremos almacenando el contenido
            // del archivo.
            BufferedReader br = new BufferedReader(archivo);

            String linea = br.readLine();
            linea.trim();

            color = Float.parseFloat(linea);

            br.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            /*
            Toast.makeText(activity, "Error al leer el archivo de configuracion",
                    Toast.LENGTH_LONG).show();
                    */
            return false;
        }
    }

    public static void write(Float color , Activity activity) {
        try {
            OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                    activity.getBaseContext().openFileOutput(FILE, Context.MODE_PRIVATE));
            // Por cada tiempo escrito en los EditText escribimos una línea
            // en el archivo de alarmas.

            outSWMensaje.write(color.toString());


            outSWMensaje.close();
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage());
            /*
            Toast.makeText(activity, "Error al guardar el token de autenticación",
                    Toast.LENGTH_LONG).show();*/
        }
            /*
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }catch(IOException e){
            Log.e(TAG, e.getMessage());
        }
        */
    }
}

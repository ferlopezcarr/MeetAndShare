package pad.meetandshare.negocio.servicioAplicacion;

import java.util.ArrayList;

import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Usuario;

public interface MyCallBack {

    void onCallbackUsuario(Usuario usuario);

    void onCallbackActividad(Actividad actividad);

    void onCallbackActividadAll(ArrayList<Actividad> actividad);

}

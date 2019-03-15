package pad.meetandshare.negocio.servicioAplicacion;

import java.util.ArrayList;

import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.User;

public interface MyCallBack {

    void onCallbackUsuario(User usuario);

    void onCallbackActividad(Actividad actividad);

    void onCallbackActividadAll(ArrayList<Actividad> actividad);

}

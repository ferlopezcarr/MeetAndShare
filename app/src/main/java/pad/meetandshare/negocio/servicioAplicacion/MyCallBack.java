package pad.meetandshare.negocio.servicioAplicacion;

import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Usuario;

public interface MyCallBack {

    void onCallbackUsuario(Usuario usuario);

    void onCallbackActividad(Actividad actividad);

}

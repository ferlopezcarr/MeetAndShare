package pad.meetandshare.negocio.servicioAplicacion;

import pad.meetandshare.negocio.modelo.Actividad;

public interface SAActividad {

    /**
     * Método que recibe una Actividad y la guarda en la base de datos desactivada
     * @param actividad
     * @return si ha sido borrada o no
     */
    public boolean delete(Actividad actividad);

    /**
     * Método que recibe una Actividad y la persiste en la base de datos,
     * si no existe la crea,
     * si existe la modifica
     * @param actividad
     * @return actividad guardada
     */
    public Actividad save(Actividad actividad);

}

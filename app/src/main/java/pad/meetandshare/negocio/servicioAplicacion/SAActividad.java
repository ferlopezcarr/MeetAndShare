package pad.meetandshare.negocio.servicioAplicacion;

import pad.meetandshare.negocio.modelo.Actividad;

public interface SAActividad {

    /**
     * Método que recibe una Actividad y la guarda en la base de datos desactivada
     * @param actividad
     * @param ui
     * @return si ha sido borrada o no
     */
    boolean delete(Actividad actividad, String ui);

    /**
     * Método que recibe una Actividad y la persiste en la base de datos,
     * si no existe la crea,
     * si existe la modifica
     * @param actividad
     * @param ui
     * @return actividad guardada
     */
    void save(Actividad actividad, String ui);

    /**
     * Método para obtener una actividad de las base de datos,
     * si no existe devuelve null
     * @param ui
     * @return actividad de la base de datos
     */
    void get(String ui, final MyCallBack myCallBack);
}

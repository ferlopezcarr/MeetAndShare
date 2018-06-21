package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import pad.meetandshare.negocio.modelo.Actividad;


public interface SAActividad {

    /**
     * Método que recibe una Actividad, le asigna un id y la persiste en la base de datos
     * @param actividad
     */
    void create(Actividad actividad);

    /**
     * Método que recibe una Actividad y la guarda en la base de datos desactivada
     * @param actividad
     * @return si ha sido borrada o no
     */
    void delete(Actividad actividad);

    /**
     * Método que recibe una Actividad y la persiste en la base de datos,
     * si no existe la crea,
     * si existe la modifica
     * @param actividad
     * @return actividad guardada
     */
    void save(Actividad actividad);

    /**
     * Método para obtener una actividad de las base de datos,
     * si no existe devuelve null
     * @param ui
     * @return actividad de la base de datos
     */
    void get(String ui, final MyCallBack myCallBack);

    /**
     * Método para obtener una actividad de las base de datos,
     * si no existe devuelve null
     * @param myCallBack para hacer el return de todas las actividades de la base de datos
     * @return actividad de la base de datos
     */
    void getAll(final MyCallBack myCallBack);

    DatabaseReference getDatabaseReference();

    boolean checkActividad(Actividad actividadCreada, DataSnapshot dataSnapshot);
}
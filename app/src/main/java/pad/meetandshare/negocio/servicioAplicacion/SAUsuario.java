package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pad.meetandshare.negocio.modelo.Usuario;

public interface SAUsuario {

    void create(Usuario usuario);

    /**
    * Método que recibe un Usuario y lo guarda en la base de datos desactivado
    * @param usuario
    * @return si ha sido borrado o no
    */
    boolean delete(Usuario usuario, String ui);

    /**
    * Método que recibe un Usuario y lo persiste en la base de datos,
    * si no existe lo crea,
    * si existe lo modifica
    * @param usuario
    * @return usuario guardado
    */
    void save(Usuario usuario, String ui);

    void get(String ui, MyCallBack myCallBack);



    DatabaseReference getDatabaseReference();

    boolean checkUsuario(Usuario usuarioModificado, DataSnapshot dataSnapshot);

}

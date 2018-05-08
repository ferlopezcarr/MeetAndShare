package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pad.meetandshare.negocio.modelo.Usuario;

public class SAUsuarioImp implements SAUsuario {



    public boolean delete(Usuario usuario, String ui){



        return false;
    }


    public Usuario save(Usuario usuario, String ui){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(ui).setValue(usuario);
        return null;
    }


    @Override
    public Usuario get(String ui) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        

        return null;
    }
}

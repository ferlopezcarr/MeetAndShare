package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Usuario;

public class SAActividadImp implements SAActividad {

    private final static String databaseName = "activities";

    @Override
    public boolean delete(Actividad actividad, String ui){



        return false;
    }

    @Override
    public Actividad save(Actividad actividad, String ui){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(databaseName);

        myRef.child(ui).setValue(actividad);
        return null;
    }


    @Override
    public Actividad get(String ui) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(databaseName);



        return null;
    }
}

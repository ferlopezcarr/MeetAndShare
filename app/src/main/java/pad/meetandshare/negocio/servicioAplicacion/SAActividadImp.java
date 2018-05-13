package pad.meetandshare.negocio.servicioAplicacion;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.Usuario;

import static android.content.ContentValues.TAG;

public class SAActividadImp implements SAActividad {

    FirebaseDatabase database;
    DatabaseReference myRef;

    public SAActividadImp() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Actividad.ActivitiesDatabaseName);
    }

    @Override
    public boolean delete(Actividad actividad, String ui){



        return false;
    }

    @Override
    public void save(Actividad actividad, String ui){

        myRef = database.getReference(Actividad.ActivitiesDatabaseName);
        myRef.child(ui).setValue(actividad);
    }


    @Override
    public void get(String ui, final MyCallBack myCallBack) {

        myRef = database.getReference(Actividad.ActivitiesDatabaseName);
        myRef = myRef.child(ui);

        ValueEventListener listener= new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Actividad activity = dataSnapshot.getValue(Actividad.class);
                Log.d(TAG, "Value is: " + activity);
                myCallBack.onCallbackActividad(activity);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        myRef.addListenerForSingleValueEvent(listener);
    }

}

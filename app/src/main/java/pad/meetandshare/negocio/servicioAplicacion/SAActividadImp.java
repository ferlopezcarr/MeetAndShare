package pad.meetandshare.negocio.servicioAplicacion;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pad.meetandshare.negocio.modelo.Actividad;

import static android.content.ContentValues.TAG;

public class SAActividadImp implements SAActividad {

    FirebaseDatabase database;
    DatabaseReference myRef;

    public SAActividadImp() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Actividad.ActivitiesDatabaseName);
    }

    @Override
    public void create(Actividad actividad) {

        DatabaseReference pushRef = myRef.push();
        //pushRef.setValue(actividad);

        String uid = pushRef.getKey();

        actividad.setUid(uid);

        save(actividad, actividad.getIdAdministrador());
    }

    @Override
    public void delete(Actividad actividad, String ui){

        //borrado fisico
        //myRef.child(ui).child(actividad.getUid()).removeValue();

        actividad.setActiva(false);
        this.save(actividad, ui);
    }

    @Override
    public void save(Actividad actividad, String uidAdminstrador){

        myRef.child(uidAdminstrador).child(actividad.getUid()).setValue(actividad);

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


    @Override
    public void getAll( final MyCallBack myCallBack) {

        myRef = database.getReference(Actividad.ActivitiesDatabaseName);

        ValueEventListener listener= new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Actividad> lista = new ArrayList<>();


                Iterable<DataSnapshot> dataSnapshotRoot = dataSnapshot.getChildren();

                for(DataSnapshot child : dataSnapshotRoot){

                    Iterable<DataSnapshot> dataSnapshotChild = child.getChildren();

                    for (DataSnapshot ds : dataSnapshotChild) {
                        Actividad act = ds.getValue(Actividad.class);
                        lista.add(act);
                    }
                }

                myCallBack.onCallbackActividadAll(lista);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        };
        myRef.addValueEventListener(listener);
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return myRef;
    }

    @Override
    public boolean checkActividad(Actividad actividadCreada, DataSnapshot dataSnapshot) {
        boolean sameName = false;
        boolean sameAdmin = false;

        Iterable<DataSnapshot> dataSnapshotChid = dataSnapshot.child(AutorizacionFirebase.getCurrentUser().getUid()).getChildren();

        for (DataSnapshot ds : dataSnapshotChid) {
            Actividad act = ds.getValue(Actividad.class);

            //si la actividad ha sido creada y la actividad que se encuentra en la bd no es ella misma
            if (actividadCreada != null && act != null) {

                if (act.getNombre() != null) {
                    if (!act.getUid().equalsIgnoreCase(actividadCreada.getUid())) {

                        sameName = act.getNombre().equalsIgnoreCase(actividadCreada.getNombre());
                        sameAdmin = act.getIdAdministrador().equalsIgnoreCase(actividadCreada.getIdAdministrador());

                        if (sameName && sameAdmin) {
                            ds.getRef().removeValue();
                        }
                    }
                }
            }
        }

        return (sameName && sameAdmin);
    }
}

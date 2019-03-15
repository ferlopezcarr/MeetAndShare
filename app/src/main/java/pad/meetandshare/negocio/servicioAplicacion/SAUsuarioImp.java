package pad.meetandshare.negocio.servicioAplicacion;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pad.meetandshare.negocio.modelo.Actividad;
import pad.meetandshare.negocio.modelo.User;

import static android.content.ContentValues.TAG;

public class SAUsuarioImp implements SAUsuario {

    private static FirebaseDatabase database;
    private static DatabaseReference myRef;

    public SAUsuarioImp(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(User.UsersDataBaseName);
    }

    @Override
    public void create(User usuario) {

        DatabaseReference pushRef = myRef.push();

        pushRef.setValue(usuario);

        String uid = pushRef.getKey();

        usuario.setUid(uid);

        save(usuario);
    }

    public void delete(User usuario){

        usuario.setActive(false);

        this.save(usuario);
    }


    public void save(User usuario){

        myRef = database.getReference(User.UsersDataBaseName);
        myRef.child(usuario.getUid()).setValue(usuario);
    }

    @Override
    public void get(String ui, final MyCallBack myCallBack) {

        myRef = database.getReference(User.UsersDataBaseName);
        myRef = myRef.child(ui);

        ValueEventListener listener= new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Value is: " + user);
                myCallBack.onCallbackUsuario(user);
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
    public DatabaseReference getDatabaseReference() {
        return myRef;
    }

    @Override
    public boolean checkUsuario(User usuarioModificado, DataSnapshot dataSnapshot) {
        boolean sameEmail = false;

        Iterable<DataSnapshot> dataSnapshotChid = dataSnapshot.child(AutorizacionFirebase.getCurrentUser().getUid()).getChildren();

        for (DataSnapshot ds : dataSnapshotChid) {
            User usr = ds.getValue(User.class);

            //si la actividad ha sido creada y la actividad que se encuentra en la bd no es ella misma
            if (usuarioModificado != null && usr != null) {
                if (!usuarioModificado.getUid().equalsIgnoreCase(usr.getUid())) {//mismo id
                    if (usuarioModificado.getEmail() != null && usr.getEmail() != null) {

                        sameEmail = usuarioModificado.getEmail().equalsIgnoreCase(usr.getEmail());
                        ds.getRef().removeValue();
                    }
                }
            }
        }

        return sameEmail;
    }

}

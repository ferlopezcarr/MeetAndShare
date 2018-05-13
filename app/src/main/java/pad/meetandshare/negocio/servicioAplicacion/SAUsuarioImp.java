package pad.meetandshare.negocio.servicioAplicacion;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pad.meetandshare.negocio.modelo.Usuario;

import static android.content.ContentValues.TAG;

public class SAUsuarioImp implements SAUsuario {

    FirebaseDatabase database;
    DatabaseReference myRef;

    public SAUsuarioImp(){
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference(Usuario.UsersDataBaseName);
    }


    public boolean delete(Usuario usuario, String ui){



        return false;
    }


    public void save(Usuario usuario, String ui){

        myRef = database.getReference(Usuario.UsersDataBaseName);
        myRef.child(ui).setValue(usuario);
    }


    @Override
    public void get(String ui, final MyCallBack myCallBack) {

        myRef = database.getReference(Usuario.UsersDataBaseName);
        myRef = myRef.child(ui);

        ValueEventListener listener= new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Usuario user = dataSnapshot.getValue(Usuario.class);
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

}

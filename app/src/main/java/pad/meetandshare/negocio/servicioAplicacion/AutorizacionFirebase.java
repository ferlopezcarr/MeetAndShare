package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutorizacionFirebase {

    private static FirebaseAuth mAuth;

    public static FirebaseAuth getFirebaseAuth() {
        if(mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

}

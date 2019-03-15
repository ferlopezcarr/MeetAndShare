package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pad.meetandshare.negocio.modelo.User;

public class AutorizacionFirebase {

    private static FirebaseAuth mAuth;

    private static User user;

    public static FirebaseAuth getFirebaseAuth() {

        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        return mAuth;
    }

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }


    public static User getUser(){
        return user;
    }

    public static void setUsuario(User usuario) {
        user = usuario;
    }

    public static void singOut() {
        mAuth = null;
        user = null;
        mAuth.signOut();
    }

    public static boolean amIAuthentificated() {
        return (mAuth != null && user != null);
    }

}

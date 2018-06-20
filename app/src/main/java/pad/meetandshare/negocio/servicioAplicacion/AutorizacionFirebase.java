package pad.meetandshare.negocio.servicioAplicacion;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pad.meetandshare.negocio.modelo.Usuario;

public class AutorizacionFirebase {

    private static FirebaseAuth mAuth;

    private static Usuario user;

    public static FirebaseAuth getFirebaseAuth() {

        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        return mAuth;
    }

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }


    public static Usuario getUser(){
        return user;
    }

    public static void setUsuario(Usuario usuario) {
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

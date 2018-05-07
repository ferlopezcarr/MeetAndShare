package pad.meetandshare.negocio.modelo;

import com.google.firebase.auth.FirebaseAuth;

public class  Autenticacion {



    private FirebaseAuth firebaseAuth;


    public void autentica(){


    firebaseAuth = FirebaseAuth.getInstance();

    }

}

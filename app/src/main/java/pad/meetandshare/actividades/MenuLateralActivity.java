package pad.meetandshare.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


import pad.meetandshare.R;
import pad.meetandshare.actividades.fragments.CrearActividadFragment;
import pad.meetandshare.actividades.fragments.InicioFragment;
import pad.meetandshare.actividades.fragments.ModificaUsuarioFragment;
import pad.meetandshare.actividades.fragments.PerfilUsuarioFragment;
import pad.meetandshare.negocio.servicioAplicacion.AutorizacionFirebase;

public class MenuLateralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InicioFragment.OnFragmentInteractionListener,FragmentTransaction {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (getFragmentManager().findFragmentById(R.id.ContenedorMenuLateral) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.ContenedorMenuLateral, new InicioFragment()).commit();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //PARA QUE NO SALGA EL TECLADO SEGUN CARGA LA PANTALLA
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragmento = null;
        boolean cambia = false;
        boolean salir = false;

        if (id == R.id.nav_Inicio) {
            fragmento = new InicioFragment();
        } else if (id == R.id.nav_Perfil) {
            fragmento = PerfilUsuarioFragment.newInstance();
        } else if (id == R.id.nav_CrearActividad) {
            fragmento = CrearActividadFragment.newInstance();
        } else if (id == R.id.nav_CerrarSesion) {
            salir = true;
        }

        if(!salir) {

            FragmentTransaction fc= (FragmentTransaction) this;

            fc.replaceFragment(fragmento);


        }
        else {
            //Log out
            AutorizacionFirebase.getFirebaseAuth().signOut();
            //Volver al login
            Intent myIntent = new Intent(this, LoginActivity.class);

            this.startActivity(myIntent);
            this.onResume();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_inicio);
        if(this.getFragmentManager().getBackStackEntryCount() == 0 && currentFragment != null)
            finish();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void replaceFragment(Fragment fragment) {

        getFragmentManager().beginTransaction().replace(R.id.ContenedorMenuLateral, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);

    }


}

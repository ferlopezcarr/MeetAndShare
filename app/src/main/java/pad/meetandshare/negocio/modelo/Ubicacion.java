package pad.meetandshare.negocio.modelo;

import com.google.android.gms.location.places.Place;

public class Ubicacion {

    private Place place;

    public Ubicacion() {
        place = null;
    }

    public Ubicacion(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return this.place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

}

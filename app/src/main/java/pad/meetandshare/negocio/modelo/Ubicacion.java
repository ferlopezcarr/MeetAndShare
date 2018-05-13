package pad.meetandshare.negocio.modelo;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;

public class  Ubicacion {

    private PlaceEntity place;

    public Ubicacion() {
        place = null;
    }

    public Ubicacion(PlaceEntity place) {
        this.place = place;
    }

    public Place getPlace() {
        return this.place;
    }

    public void setPlace(PlaceEntity place) {
        this.place = place;
    }



}

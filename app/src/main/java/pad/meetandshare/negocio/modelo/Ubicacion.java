package pad.meetandshare.negocio.modelo;


import com.google.android.gms.location.places.Place;

import java.io.Serializable;


public class  Ubicacion implements Serializable {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int PLACE_PICKER_REQUEST = 1;

    private double latitude;
    private double longitude;

    public Ubicacion(){}


    public Ubicacion(Place place) {
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}

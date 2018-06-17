package pad.meetandshare.negocio.modelo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class  Ubicacion implements Serializable {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int PLACE_PICKER_REQUEST = 1;

    private double latitude;
    private double longitude;

    public Ubicacion(){}

    /*
    public Ubicacion(String id, List<Integer> placeTypes, String address, String latLng, String  name, String viewPort, Uri websiteUri, String phoneNumber, float rating, int priceLevel, String attributions, Locale locale) {
        this.id = id;
        this.placeTypes = placeTypes;
        this.address = address;
        this.latLng = latLng;
        this.name = name;
        this.viewPort1 = viewPort;
        this.viewPort2 = viewPort;
        this.locale=locale;
        this.websiteUri = websiteUri;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.priceLevel = priceLevel;
        this.attributions = attributions;
    }
    */

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

    //Para las actividades
    public static void obtenerUbicacion(Activity activity) {

        String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(
                activity, accessFineLocation)
                != PackageManager.PERMISSION_GRANTED) {

            //si no tiene permisos y debe pedirlos
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, accessFineLocation)) {

                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{accessFineLocation},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {//si no debe pedirlos
                Ubicacion.renderPlacePicker(activity,PLACE_PICKER_REQUEST);
            }
        } else {//si tiene permisos
            Ubicacion.renderPlacePicker(activity, PLACE_PICKER_REQUEST);
        }
    }

    public static void renderPlacePicker(Activity activity, int placePickerRequest) {
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            activity.startActivityForResult(builder.build(activity), placePickerRequest);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            String msg = "Google Play Services no esta disponible en este momento";
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /*
    public Ubicacion(String id) {
        this.id = id;
    }

    public String getId(){
       return this.id;
   };


   public List<Integer> getPlaceTypes(){

       return this.placeTypes;
   };

   public String getAddress(){
       return this.address;
   }

    public String getName(){
        return this.name;
    }

    public String getLatLng() {
        return latLng;
    }
    

   public String getViewPort1(){
        return viewPort1;
   }

    public String getViewPort2(){
        return viewPort2;
    }



   public Uri getWebsiteUri(){
        return websiteUri;
   }

   public String getPhoneNumber(){
        return phoneNumber;
   }

    public float getRating(){
        return rating;
    }

    public int getPriceLevel(){
        return priceLevel;
    }

    public String getAttributions(){

       return attributions;
    }

    public Locale getLocale(){
       return locale;
    }
    */

}

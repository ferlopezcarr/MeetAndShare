package pad.meetandshare.negocio.modelo;

import android.net.Uri;


import com.google.android.gms.location.places.Place;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class  Ubicacion implements Serializable {

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

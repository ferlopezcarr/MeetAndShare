package pad.meetandshare.negocio.modelo;

import android.net.Uri;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.zzbej;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

public class  Ubicacion  {


    private  String id;
    private List<Integer> placeTypes;
    private String address;
    private Locale locale;
    private String latLng;
    private String name;
    private String viewPort1;
    private String viewPort2;
    private Uri websiteUri;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private String attributions;

    public Ubicacion(){}

    public Ubicacion(String id, List<Integer> placeTypes, String address, Locale locale, String latLng, String  name, String viewPort, Uri websiteUri, String phoneNumber, float rating, int priceLevel, String attributions) {
        this.id = id;
        this.placeTypes = placeTypes;
        this.address = address;
        this.locale = locale;
        this.latLng = latLng;
        this.name = name;
        this.viewPort1 = viewPort;
        this.viewPort2 = viewPort;

        this.websiteUri = websiteUri;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.priceLevel = priceLevel;
        this.attributions = attributions;
    }


    public Ubicacion(Place place) {
        this.id = place.getId();
        this.placeTypes = getPlaceTypes();
        this.address = place.getAddress().toString();
        this.locale = place.getLocale();
        this.latLng = place.getLatLng().toString();
        this.name = place.getName().toString();
        this.viewPort1 = place.getViewport().northeast.toString();
        this.viewPort2 = place.getViewport().southwest.toString();
        this.websiteUri = place.getWebsiteUri();
        this.phoneNumber = place.getPhoneNumber().toString();
        this.rating = place.getRating();
        this.priceLevel = place.getPriceLevel();
        if(place.getAttributions()!=null)
        this.attributions = place.getAttributions().toString();
    }



    public Ubicacion(String id) {
        this.id = id;
    }

    public String getId(){
       return this.id;
   };


   public List<Integer> getPlaceTypes(){

       return this.placeTypes;
   };

   public CharSequence getAddress(){
       return this.address;
   }

   public Locale getLocale(){
       return this.locale;
   };

    public CharSequence getName(){
        return this.name;
    }

    public String getLatLng() {
        return latLng;
    }

    public LatLng getLatLngLat(){

       String[] latlong = latLng.split(",");
       double latitude = Double.parseDouble(latlong[0]);
       double longitude = Double.parseDouble(latlong[1]);
       LatLng location = new LatLng(latitude, longitude);

        return location;
   }

   public String getViewPort1(){
        return this.viewPort1;
   }

    public String getViewPort2(){
        return this.viewPort2;
    }

   public LatLngBounds getViewportLat(){
       String[] latlong = viewPort1.split(",");

       double latitude = Double.parseDouble(latlong[0]);
       double longitude = Double.parseDouble(latlong[1]);
       LatLng latLng = new LatLng(latitude, longitude);


         latlong = viewPort2.split(",");

        latitude = Double.parseDouble(latlong[0]);
        longitude = Double.parseDouble(latlong[1]);
       LatLng latLng1 = new LatLng(latitude, longitude);

       LatLngBounds latLngBounds = new LatLngBounds(latLng,latLng1);

        return latLngBounds;
   }

   public Uri getWebsiteUri(){
        return this.websiteUri;
   }

   public CharSequence getPhoneNumber(){
        return this.phoneNumber;
   }

    public float getRating(){
        return this.rating;
    }

    public int getPriceLevel(){
        return this.priceLevel;
    }

    public CharSequence getAttributions(){
        return this.attributions;
    }



    public final boolean isDataValid() {
        return true;
    }


}

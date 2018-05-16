package pad.meetandshare.actividades;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import pad.meetandshare.R;
import pad.meetandshare.negocio.modelo.Actividad;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View view;
    private Marker marker;
    private TextView nombreActividadMarker;
    private TextView descripcionActividadMarker;


    public MyInfoWindowAdapter(LayoutInflater layoutInflater) {
        view = layoutInflater.inflate(R.layout.my_info_window_marker,
                null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (marker != null && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        this.marker = marker;

        final String title = marker.getTitle();
        final String snipet = marker.getSnippet();
        nombreActividadMarker = ((TextView) view.findViewById(R.id.nombreActividadMarker));
        descripcionActividadMarker = ((TextView) view.findViewById(R.id.descripcionActividadMarker));

        if (title != null) {
            nombreActividadMarker.setText(title);
        } else {
            nombreActividadMarker.setText("No name");
        }

        if(snipet != null) {
            descripcionActividadMarker.setText(snipet);
        }

        return view;
    }

}
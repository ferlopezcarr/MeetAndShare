package pad.meetandshare.actividades;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pad.meetandshare.R;
import pad.meetandshare.integracion.ColorFile;

public class LeyendaMapaFragment extends Fragment {

    private View rootView;

    private ImageView imgViewAdminColor;

    public LeyendaMapaFragment() {
        // Required empty public constructor
    }

    public static LeyendaMapaFragment newInstance() {
        LeyendaMapaFragment fragment = new LeyendaMapaFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_leyenda_mapa, container, false);

        return rootView;
    }

}

package pad.meetandshare.actividades.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pad.meetandshare.R;


public class LeyendaMapaFragment extends Fragment {

    private View rootView;


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

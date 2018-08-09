package com.livenoproblem.travelsketch.Modules;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

import com.livenoproblem.travelsketch.Modules.Route;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
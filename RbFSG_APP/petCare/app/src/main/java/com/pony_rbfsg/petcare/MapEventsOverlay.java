package com.pony_rbfsg.petcare;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

/**
 * Created by User on 2016/6/12.
 */

public class MapEventsOverlay extends Overlay {

    private MapEventsReceiver mReceiver;

    /**
     * @param ctx the context
     * @param receiver the object that will receive/handle the events.
     * It must implement MapEventsReceiver interface.
     */
    public MapEventsOverlay(Context ctx, MapEventsReceiver receiver) {
        super(ctx);
        mReceiver = receiver;
    }

    @Override protected void draw(Canvas c, MapView osmv, boolean shadow) {
        //Nothing to draw
    }

    @Override public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView){
        Projection proj = mapView.getProjection();
        GeoPoint p = (GeoPoint)proj.fromPixels((int)e.getX(), (int)e.getY());
        return mReceiver.singleTapConfirmedHelper(p);
    }

    @Override public boolean onLongPress(MotionEvent e, MapView mapView) {
        Projection proj = mapView.getProjection();
        GeoPoint p = (GeoPoint)proj.fromPixels((int)e.getX(), (int)e.getY());
        //throw event to the receiver:
        return mReceiver.longPressHelper(p);
    }

}

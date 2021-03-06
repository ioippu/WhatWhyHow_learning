package com.pony_rbfsg.petcare;

/**
 * Created by User on 2016/6/12.
 */

import org.osmdroid.util.GeoPoint;

/**
 * Interface for objects that need to handle map events thrown by a MapEventsOverlay.
 * @see org.osmdroid.bonuspack.overlays.MapEventsOverlay
 * @author M.Kergall
 */
public interface MapEventsReceiver {

    /**
     * @param p the position where the event occurred.
     * @return true if the event has been "consumed" and should not be handled by other objects.
     */
    boolean singleTapConfirmedHelper(GeoPoint p);

    /**
     * @param p the position where the event occurred.
     * @return true if the event has been "consumed" and should not be handled by other objects.
     */
    boolean longPressHelper(GeoPoint p);
}

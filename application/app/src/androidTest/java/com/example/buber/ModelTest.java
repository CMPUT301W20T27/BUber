package com.example.buber;

import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Driver;
import com.example.buber.Model.Trip;
import com.example.buber.Model.UserLocation;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ModelTest {

    @Test
    public void testModel() {
        ApplicationModel m = new ApplicationModel();
        m.setSessionUser(new Driver());
        m.setSessionTrip(new Trip());
        m.setTripListener(() -> { });
        assertNotNull(m.getSessionUser());
        assertNotNull(m.getTripListener());
        assertNotNull(m.getSessionTrip());
        m.clearModelForLogout();
        assertNull(m.getSessionUser());
        assertNull(m.getSessionTrip());
        assertNull(m.getTripListener());
    }

    @Test
    public void testUserLocationDistanceTo() {
        UserLocation l1 = new UserLocation(10, 10);
        UserLocation l2 = new UserLocation(50, 50);
        assertTrue(l1.distanceTo(l2) == 5763.372762468703);
        l1 = new UserLocation(0, 0);
        l2 = new UserLocation(0, 0);
        assertTrue(l1.distanceTo(l2) == 0.0);
        assertTrue(l2.distanceTo(l2) == 0.0);
        l1.setLongitude(10);
        assertTrue(l1.distanceTo(l2) == 1111.8957696000016);
    }

    @Test
    public void testTripNextStatusValid() {
        Trip t = new Trip();
        Trip.STATUS curStatus = Trip.STATUS.PENDING;
        Trip.STATUS nxtStatus = Trip.STATUS.DRIVER_ACCEPT;
        t.setStatus(curStatus);
        assertTrue(t.nextStatusValid(nxtStatus));
        nxtStatus = Trip.STATUS.EN_ROUTE;
        assertFalse(t.nextStatusValid(nxtStatus));
    }
}

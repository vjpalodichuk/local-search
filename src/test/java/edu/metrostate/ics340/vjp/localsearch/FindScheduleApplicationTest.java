package edu.metrostate.ics340.vjp.localsearch;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FindScheduleApplicationTest {

    @Test
    public void testMain() {
        String[] args = null;

        FindScheduleApplication.main(args);
    }

    @Test
    public void testStart() {
        FindScheduleApplication fsa = new FindScheduleApplication();
        String[] args = null;
        assertNotNull(fsa);

        fsa.start(null);
    }
}
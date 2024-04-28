package bookings;

import flights.*;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BusinessClassFlightTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  private final AnySeatManager seatManager = context.mock(AnySeatManager.class);
  private final FlightNumber fn = FlightNumber.of("BA175");
  private final LocalDate date = LocalDate.now().plusMonths(3);
  private final BusinessClassFlight flight = new BusinessClassFlight(fn, date, Airport.LHR, Airport.JFK,
      seatManager);
  private final Seat businessSeat = new Seat("10B",
      ServiceLevel.BUSINESS);
  private final List<Seat> availableSeats = List.of(new Seat("33A", ServiceLevel.ECONOMY), businessSeat,
      new Seat("1A", ServiceLevel.FIRST));

  @Test
  public void eliteStatusPassengersChooseAnySeat() {
    FrequentFlyerStatus status = FrequentFlyerStatus.ELITE;
    context.checking(new Expectations() {{
      exactly(1).of(seatManager).getAvailableSeats(fn, date);
      will(returnValue(availableSeats));
    }});
    List<Seat> seatResults = flight.seatingOptions(status);
    assertThat(seatResults, is(availableSeats));
  }

  @Test
  public void otherBusinessPassengersOnlySeeBusinessClassSeats() {
    FrequentFlyerStatus status = FrequentFlyerStatus.SILVER;
    context.checking(new Expectations() {{
      exactly(1).of(seatManager).getAvailableSeats(fn, date);
      will(returnValue(availableSeats));
    }});
    List<Seat> seatResults = flight.seatingOptions(status);
    assertThat(seatResults, is(List.of(businessSeat)));
  }

  @Test
  public void searchForSeatsInAPastFlightThrowsAnError() {
    FrequentFlyerStatus status = FrequentFlyerStatus.BASIC;
    LocalDate invalidDate = LocalDate.of(2022,10,10);
    BusinessClassFlight invalidFlight = new BusinessClassFlight(fn, invalidDate, Airport.LHR, Airport.JFK,
        seatManager);
    context.checking(new Expectations() {{
      exactly(0).of(seatManager).getAvailableSeats(fn, invalidDate);
    }});
    try {
      invalidFlight.seatingOptions(status);
      fail("Booking Error should have occured");
    } catch (BookingException e) {
      assertTrue(e.toString().contains("past"));
    }
  }
}

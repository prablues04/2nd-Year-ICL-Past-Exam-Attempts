package bookings;

import availability.SeatManager;
import flights.*;

import java.time.LocalDate;
import java.util.List;

abstract class Flight {

  public Flight(
      FlightNumber flightNumber, LocalDate date, Airport origin, Airport destination, AnySeatManager sm) {
    this.flightNumber = flightNumber;
    this.date = date;
    this.origin = origin;
    this.destination = destination;
    this.sm = sm;
  }

  protected final FlightNumber flightNumber;
  protected final LocalDate date;
  protected final Airport origin;
  protected final Airport destination;
  protected final AnySeatManager sm;


  public List<Seat> seatingOptions(FrequentFlyerStatus status) {
    if (date.isBefore(LocalDate.now())) {
      throw new BookingException("Flight is in the past");
    }
    List<Seat> availableSeats = sm.getAvailableSeats(flightNumber, date);
    return filterSeats(status, availableSeats); // Separating into two methods ensures the
    // subclass has to override the filterSeats method whereas this is not the case if I just
    // return available seats in this method and override the same method in the subclass
  }

  protected abstract List<Seat> filterSeats(FrequentFlyerStatus status, List<Seat> availableSeats);

  ;

  public int calculateFare() {
    return origin.distanceTo(destination) * getPencePerMile() + getStandardFeePence();
  }

  protected abstract int getPencePerMile();

  protected abstract int getStandardFeePence();

  @Override
  public String toString() {
    return "Flight "
        + flightNumber
        + " ("
        + date
        + ") from "
        + origin
        + " to "
        + destination;
  }

}

package bookings;

import availability.SeatManager;
import flights.*;
import java.time.LocalDate;
import java.util.List;

public class BusinessClassFlight extends Flight {

  private static final int PENCE_PER_MILE = 35;
  private static final int STANDARD_FEE_PENCE = 8000;

  public BusinessClassFlight(
      FlightNumber flightNumber, LocalDate date, Airport origin, Airport destination,
      AnySeatManager sm) {
    super(flightNumber, date, origin, destination, sm);
  }

  @Override
  protected List<Seat> filterSeats(FrequentFlyerStatus status, List<Seat> availableSeats) {
    if (status == FrequentFlyerStatus.ELITE) {
      // Elite status customers can choose any seat, even in first class.
      return availableSeats;
    }
    return availableSeats.stream().filter(s -> s.cabin() == ServiceLevel.BUSINESS).toList();
  };

  @Override
  protected int getPencePerMile() {
    return PENCE_PER_MILE;
  }

  @Override
  protected int getStandardFeePence() {
    return STANDARD_FEE_PENCE;
  }

  @Override
  public String toString() {
    return super.toString()
        + " ("
        + ServiceLevel.BUSINESS
        + ")";
  }
}

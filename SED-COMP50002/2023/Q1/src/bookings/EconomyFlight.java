package bookings;

import availability.SeatManager;
import flights.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class EconomyFlight extends Flight {

  private static final int PENCE_PER_MILE = 15;
  private static final int STANDARD_FEE_PENCE = 4000;

  public EconomyFlight(
      FlightNumber flightNumber, LocalDate date, Airport origin, Airport destination,
      AnySeatManager sm) {
    super(flightNumber, date, origin, destination, sm);
  }

  @Override
  protected List<Seat> filterSeats(FrequentFlyerStatus status, List<Seat> availableSeats) {
    List<Seat> allAvailableEconomySeats =
        availableSeats.stream().filter(s -> s.cabin() == ServiceLevel.ECONOMY).toList();
    return switch (status) {
      case BASIC -> pickOneAtRandomFrom(allAvailableEconomySeats);
      case SILVER, ELITE -> allAvailableEconomySeats;
    };
  }

  @Override
  protected int getPencePerMile() {
    return PENCE_PER_MILE;
  }

  @Override
  protected int getStandardFeePence() {
    return STANDARD_FEE_PENCE;
  }

  private List<Seat> pickOneAtRandomFrom(List<Seat> allAvailableEconomySeats) {
    int randomPositionInList = (int) (Math.random() * (allAvailableEconomySeats.size() - 1));
    return Collections.singletonList(allAvailableEconomySeats.get(randomPositionInList));
  }

  @Override
  public String toString() {
    return super.toString()
        + " ("
        + ServiceLevel.ECONOMY
        + ")";
  }
}

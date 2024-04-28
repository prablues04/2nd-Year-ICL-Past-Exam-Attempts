package bookings;

import availability.SeatManager;
import flights.FlightNumber;
import flights.Seat;

import java.time.LocalDate;
import java.util.List;

public class SeatManagerAdapter implements AnySeatManager {
  @Override
  public List<Seat> getAvailableSeats(FlightNumber fn, LocalDate date) {
    return SeatManager.getInstance().getAvailableSeats(fn, date);
  }
}

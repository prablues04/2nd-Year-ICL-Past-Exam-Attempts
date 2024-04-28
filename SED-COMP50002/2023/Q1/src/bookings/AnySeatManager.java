package bookings;

import flights.FlightNumber;
import flights.Seat;

import java.time.LocalDate;
import java.util.List;

public interface AnySeatManager {
  List<Seat> getAvailableSeats(FlightNumber fn, LocalDate date);
}

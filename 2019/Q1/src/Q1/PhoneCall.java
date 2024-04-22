package Q1;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalTime;

public class PhoneCall {

  private static final long PEAK_RATE = 25;
  private static final long OFF_PEAK_RATE = 10;

  private final String caller;
  private final String callee;
  private final AnyClock clock;
  private final Bill bill;

  private LocalTime startTime;
  private LocalTime endTime;

  public PhoneCall(String caller, String callee, AnyClock anyClock, Bill bill) {
    this.caller = caller;
    this.callee = callee;
    this.clock = anyClock;
    this.bill = bill;
  }

  public void start() {
    startTime = clock.now();
  }

  public void end() {
    endTime = clock.now();
  }

  public void charge() {
    bill.addBillItem(caller, callee, priceInPence());
  }

  private long priceInPence() {
    if (startTime.isAfter(LocalTime.of(9, 00)) && endTime.isBefore(LocalTime.of(18, 00))) {
      return duration() * PEAK_RATE;
    } else {
      return duration() * OFF_PEAK_RATE;
    }
  }

  private long duration() {
    return MINUTES.between(startTime, endTime) + 1;
  }

  // package private for testing
  long getPeakRate() {return PEAK_RATE;}

  long getOffPeakRate() {
    return OFF_PEAK_RATE;
  }
}

package Q1;

import java.time.LocalTime;

public class SystemClock implements AnyClock {
  @Override
  public LocalTime now() {
    return LocalTime.now();
  }
}

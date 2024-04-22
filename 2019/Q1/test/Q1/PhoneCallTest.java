package Q1;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class PhoneCallTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  Bill bill = context.mock(Bill.class);
  TestClock clock = new TestClock();

  String caller = "+447770123456";
  String callee = "+4479341554433";
  PhoneCall call = new PhoneCall(caller, callee, clock, bill);

  @Test
  public void peakTimeCallsAreChargedCorrectly() {
    clock.setClockStart(LocalTime.of(9, 30));
    call.start();
    clock.advanceTime(4, HOURS);
    call.end();
    Long cost = clock.getDuration() * call.getPeakRate();
    context.checking(new Expectations() {{
      exactly(1).of(bill).addBillItem(caller, callee, cost);
    }});
    call.charge();
  }

  @Test
  public void callStartingBeforePeakChargedOffPeak() {
    clock.setClockStart(LocalTime.of(8, 30));
    call.start();
    clock.advanceTime(3, HOURS);
    call.end();
    Long cost = clock.getDuration() * call.getOffPeakRate();
    context.checking(new Expectations() {{
      exactly(1).of(bill).addBillItem(caller, callee, cost);
    }});
    call.charge();
  }

  @Test
  public void callEndingWhenOffPeakChargedOffPeak() {
    clock.setClockStart(LocalTime.of(9, 30));
    call.start();
    clock.advanceTime(12, HOURS);
    call.end();
    Long cost = clock.getDuration() * call.getOffPeakRate();
    context.checking(new Expectations() {{
      exactly(1).of(bill).addBillItem(caller, callee, cost);
    }});
    call.charge();
  }

  @Test
  public void callOnlyInOffPeakChargedForOffPeak() {
    clock.setClockStart(LocalTime.of(6, 30));
    call.start();
    clock.advanceTime(2, HOURS);
    call.end();
    Long cost = clock.getDuration() * call.getOffPeakRate();
    context.checking(new Expectations() {{
      exactly(1).of(bill).addBillItem(caller, callee, cost);
    }});
    call.charge();
  }

  public void callSurroundingPeakHoursChargedOffPeak() {
    clock.setClockStart(LocalTime.of(8, 30));
    call.start();
    clock.advanceTime(12, HOURS);
    call.end();
    Long cost = clock.getDuration() * call.getOffPeakRate();
    context.checking(new Expectations() {{
      exactly(1).of(bill).addBillItem(caller, callee, cost);
    }});
    call.charge();
  }

  @Test
  public void exampleOfHowToUsePhoneCall() throws Exception {

    PhoneCall call = new PhoneCall("+447770123456", "+4479341554433", new SystemClock(),
        BillingSystem.getInstance());

    call.start();

    waitForSeconds(1);

    call.end();
    call.charge();
  }

  private void waitForSeconds(int n) throws Exception {
    Thread.sleep(n * 1000);
  }

  private class TestClock implements AnyClock {
    private LocalTime time;
    private LocalTime start;

    public void setClockStart(LocalTime start) {
      if (time != null) {
        throw new UnsupportedOperationException("Clock already started");
      }
      this.start = start;
      this.time = start;
    }

    @Override
    public LocalTime now() {
      return time;
    }

    public void advanceTime(long advancement, ChronoUnit unit) {
      if (time != null) {
        time = time.plus(advancement, unit);
      } else {
        throw new UnsupportedOperationException("Cannot advance time when time = null");
      }
    }

    public long getDuration() {
      return MINUTES.between(start, time) + 1;
    }
  }
}
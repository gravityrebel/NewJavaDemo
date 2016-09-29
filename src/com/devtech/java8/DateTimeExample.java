package com.devtech.java8;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Date;

import static java.time.DayOfWeek.*;
import static java.time.temporal.ChronoField.*;
import static java.time.temporal.TemporalAdjusters.*;
import static java.time.Month.*;

/**
 * Java's existing Date and Time library has been replaced.
 * The new library is more verbose and fixes many issues that the old date time API's had.
 *
 * For example, new Date() would create a wrapper around the number of millisecods from epoc, but
 * if you call .toString() on it, the result would include a timezone. This is confusing.
 *
 * The concept of the new API's are that the objects are immutable. As a result, objects are all
 * created via method factories such as .of() or .now()
 *
 * There are 6 classes that clearly describe what they model, which again is an improvement over
 * the existing library. LocalDate, LocalTime, and LocalDateTime are classes that model time from
 * the point of view of the observer. When we see these classes we know that time is not zoned, nor
 * is timezone information collected.
 *
 * There is also ZonedDateTime, OffsetDateTime and OffsetTime.
 *
 * The concept of Period and Duration are also added, making finding ranges possible, or simplifying
 * the ability to add or subtract a set period of time from a date/time instance.
 *
 * This new package is a great example of the shift from using overloaded constructors to using
 * static factories to create new instances. If you are not considering using static factories to
 * create objects, you should begin to. They give NAMES to their actions, they can return pre-constructed
 * instances, and they can return an object of any subtype for their return.
 *
 * In any case, the use of now() is a more clear representation of what is returned than the old use
 * of new Date() where it actually created an instant on the current timeline instead of an empty
 * date object like one may expect.
 */
public class DateTimeExample {


    public static void createNewInstances() {
        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId zoneLA = ZoneId.of("America/Los_Angeles");
        ZoneId southPole = ZoneId.of("Antarctica/South_Pole");

        //LOCAL DATE - Only date information. No time or timezone information is included.
        LocalDate localDate = LocalDate.now();
        LocalDate zachDOB = LocalDate.of(1988, Month.MARCH, 15);
        LocalDate parsedDate = LocalDate.parse("1989-12-14");

        //LOCAL TIME - Only time information is included. No date or timezone information is included.
        LocalTime localTime = LocalTime.now();
        LocalTime noon = LocalTime.NOON;
        LocalTime twoAMChronJob = LocalTime.of(2, 0);
        LocalTime utcTime = LocalTime.now(zoneUTC);

        //Local DATE_TIME - Includes Date and Time. No timezone information is captured.
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime beforeNewMillennium = LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59);  //December 31, 1999 @ 23:59
        LocalDateTime laDateTime = LocalDateTime.now(zoneLA);

        //ZONED DATE_TIME - includes Date, Time, and Timezone information.
        ZonedDateTime zonedDateTime = ZonedDateTime.now(); //uses system default timezone.
        ZonedDateTime firstMilleniumSecond = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, southPole);
        ZonedDateTime laTime = ZonedDateTime.now(zoneLA); //gets the current date and time in LA

        //creates an object that has the localtime here in DC, but zone info is that of LA
        LocalDateTime localDC = LocalDateTime.now(); //gets the current date and time in DC
        ZonedDateTime zoneChange = ZonedDateTime.of(localDC, zoneLA);

        //OTHER
        //only deal with minutes
        Clock minuteOnlyClock = Clock.tickMinutes(ZoneId.systemDefault());
        LocalTime localTimeMinutesOnly = LocalTime.now(minuteOnlyClock);
        ZonedDateTime zonedDateTimeMinutesOnly = ZonedDateTime.now(minuteOnlyClock); //won't display seconds or compare seconds
    }

















    public static void modifyingExistingDates() {
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDate ZACH_DOB = LocalDate.of(1988, Month.MARCH, 15);
        //ADD SUBTRACT time
        final Period ONE_DAY = Period.ofDays(1);
        final Duration THREE_HOURS = Duration.ofHours(3);

        LocalDate tomorrow = NOW.plus(ONE_DAY).toLocalDate();
        LocalDate yesterday = NOW.minusDays(1).toLocalDate();

        LocalTime threeHoursFromNow = NOW.plus(THREE_HOURS).toLocalTime();
        LocalTime twelveHoursAgo = NOW.minusHours(12).toLocalTime();

        //ADJUST DATES
        LocalDate nextWednesday = NOW.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)).toLocalDate();
        LocalDate nextWednesdayStatic = NOW.with(next(WEDNESDAY)).toLocalDate();
        LocalDateTime todayAtNoon = NOW.with(LocalTime.NOON);

        //RANGE CALCULATIONS
        Period lifetime = Period.between(ZACH_DOB, NOW.toLocalDate());
        Period lifetime2 = ZACH_DOB.until(LocalDate.now());
        int age = lifetime.getYears();
    }


















    public static void findSpecificTimes() {
        //STATIC DAYS
        MonthDay zachBirthday = MonthDay.of(Month.MARCH, 15);
        MonthDay julyFourth = MonthDay.of(Month.JULY, 4);
        LocalDate zachBDay2016 = zachBirthday.atYear(2016);

        //DYNAMIC DAYS - Find the date for the next Thanksgiving
        TemporalAdjuster fourthThursdayAdjuster =
                TemporalAdjusters.dayOfWeekInMonth(4, THURSDAY);

        LocalDate thanksgiving = LocalDate.now().with(Month.NOVEMBER).with(fourthThursdayAdjuster);
        if (LocalDate.now().isAfter(thanksgiving)) {
            thanksgiving =
                    thanksgiving.plusYears(1).with(fourthThursdayAdjuster);
        }

        //DYNAMIC DAYS WITH LAMBDA BASED INTERFACES
        LocalDate thanksgivingEasy = LocalDate.now().with(nextThanksgiving());

    }


















    public static TemporalAdjuster nextThanksgiving() {
        return (temporal -> {

            if (temporal.isSupported(YEAR) && temporal.isSupported(MONTH_OF_YEAR) && temporal
                    .isSupported(DAY_OF_MONTH)) {

                TemporalAdjuster fourthThursdayAdjuster = TemporalAdjusters.dayOfWeekInMonth(4, THURSDAY);
                Temporal thanksgiving = temporal.with(NOVEMBER).with(fourthThursdayAdjuster);

                if (LocalDate.from(temporal).isAfter(LocalDate.from(thanksgiving)) || temporal.equals(thanksgiving)) {
                    thanksgiving = thanksgiving.plus(Period.ofYears(1)).with(fourthThursdayAdjuster);
                }

                return thanksgiving;
            }
            throw new UnsupportedTemporalTypeException(
                    "Temporal must support Year, Month, and Day");
        });
    }

    public static TemporalAdjuster nextThanksgiving0() {
        return new TemporalAdjuster() {

            @Override
            public Temporal adjustInto(Temporal foo) {

                if (foo.isSupported(ChronoField.YEAR) &&
                        foo.isSupported(ChronoField.MONTH_OF_YEAR) &&
                        foo.isSupported(DAY_OF_MONTH)) {

                    int month = foo.get(ChronoField.MONTH_OF_YEAR);
                    int day = foo.get(ChronoField.DAY_OF_MONTH);

                    TemporalAdjuster fourthThursday =
                            TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY);

                    Temporal thanksgiving = foo.with(Month.NOVEMBER).with(fourthThursday);

                    if (month >= thanksgiving.get(ChronoField.MONTH_OF_YEAR) &&
                            day >= thanksgiving.get(ChronoField.DAY_OF_MONTH)) {
                        thanksgiving = thanksgiving.plus(Period.ofYears(1)).with(fourthThursday);
                    }
                    return thanksgiving;
                }
                throw new UnsupportedTemporalTypeException(
                        "Temporal must support Year, Month, and Day");
            }
        };
    }


















    public static LocalDateTime convertDateToLocalDate(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date convertDateTimeToDate(ChronoLocalDateTime dateTime) {
       return convertDateTimeToDate(dateTime.atZone(ZoneId.systemDefault()));
    }

    public static Date convertDateTimeToDate(ChronoZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }



















    public static void main(String[] args) {
        createNewInstances();
        convertDateToLocalDate(new Date());
    }
}

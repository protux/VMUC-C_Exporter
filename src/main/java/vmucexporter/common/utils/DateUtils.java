/*
 * Copyright 2016 Nico Schwanebeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vmucexporter.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vmucexporter.common.beans.DateSpan;

/**
 * In this class are some date-related utilities needed in the program.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public final class DateUtils {

    private static final Logger LOGGER = LogManager.getLogger(DateUtils.class);

    private static final DateFormat INPUT_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private static final DateFormat OUTPUT_DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    private DateUtils() {
        // Utility-Class
    }

    /**
     * @param date a date with the pattern "dd.MM.yyyy"
     * @return a calendar with the parsed date
     */
    public static Calendar parseDate(String date) {
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(INPUT_DATE_FORMATTER.parse(date));
            return calendar;
        } catch (ParseException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Eingegebenes Datum konnte nicht geparst werden: " + date, e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * @param calendar the calendar which should be converted to string.
     * @return the string-representation of the date.
     */
    public static String calendarToString(Calendar calendar) {
        return OUTPUT_DATE_FORMATTER.format(calendar.getTime());
    }

    /**
     * Takes a TimeSpan and splits it into smaller timespans which will only
     * span over one calendar week.
     *
     * @param startDate the date to start.
     * @param endDate   the date to end.
     * @return an array with the splitted timespans.
     */
    public static DateSpan[] splitDateInCalendarweeks(Calendar startDate, Calendar endDate) {
        int currentCalendarWeek = startDate.get(Calendar.WEEK_OF_YEAR);
        List<DateSpan> calendarWeeks = new ArrayList<>();
        Calendar fromDate = (Calendar) startDate.clone();
        Calendar toDate;
        Calendar currentDate = (Calendar) fromDate.clone();
        boolean exit = false;

        while (!exit) {
            toDate = (Calendar) currentDate.clone();
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            int tmpWeek = currentDate.get(Calendar.WEEK_OF_YEAR);
            exit = endDate.equals(currentDate) || endDate.before(currentDate);
            if (tmpWeek != currentCalendarWeek || exit) {
                calendarWeeks.add(new DateSpan((Calendar) fromDate.clone(), (Calendar) toDate.clone()));
                currentCalendarWeek = tmpWeek;
                fromDate = (Calendar) currentDate.clone();
            }
        }
        return calendarWeeks.toArray(new DateSpan[calendarWeeks.size()]);
    }
}

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
package vmucexporter.common.beans;

import java.util.Calendar;

/**
 * The <code>DateSpan</code>-Class represents a start date and an end date. It
 * has also some helpful shortcut-methods.
 *
 * @author Nico Schwanebeck
 * @since 1.0.0
 */
public class DateSpan {

    private Calendar startDate;
    private Calendar endDate;

    public DateSpan(Calendar fromDate, Calendar toDate) {
        this.startDate = fromDate;
        this.endDate = toDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * @return the number of the week of the year of the {@link #startDate}.
     */
    public int getStartWeek() {
        return startDate.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * @return the year of the {@link #startDate}.
     */
    public int getStartYear() {
        return startDate.get(Calendar.YEAR);
    }
}

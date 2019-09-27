/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mholzer85.wicket.fullcalendar.helpers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.mholzer85.wicket.fullcalendar.Config;
import io.github.mholzer85.wicket.fullcalendar.FullCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author mholzer
 */
class CalendarHelperTest {

	@SuppressWarnings ("ConstantConditions")
	@Test
	void convertTimezoneNonNullChecks() {
		assertThrows(NullPointerException.class, () -> CalendarHelper.convertTimezone(null, null, null, 0));
		FullCalendar fullCalendar = Mockito.mock(FullCalendar.class);
		assertThrows(NullPointerException.class, () -> CalendarHelper.convertTimezone(fullCalendar, null, null, 0));
		assertThrows(NullPointerException.class, () -> CalendarHelper.convertTimezone(fullCalendar, new DateTime(), null, 0));
	}

	@Test
	void convertTimezoneIgnored() {
		FullCalendar calendarIgnoreTZ = Mockito.mock(FullCalendar.class);
		Config configIgnoreTZ = Mockito.mock(Config.class);
		Mockito.when(configIgnoreTZ.isIgnoreTimezone()).thenReturn(false);
		Mockito.when(calendarIgnoreTZ.getConfig()).thenReturn(configIgnoreTZ);
		DateTime startDate = new DateTime();
		DateTime endDate = new DateTime();

		ImmutablePair<DateTime, DateTime> convertedEventDates = CalendarHelper.convertTimezone(calendarIgnoreTZ, startDate, endDate, 0);
		assertEquals(startDate, convertedEventDates.getLeft());
		assertEquals(endDate, convertedEventDates.getRight());
	}


	@Test
	void convertTimezoneNotIgnored() {
		int defaultOffset = DateTimeZone.getDefault().getOffset(null) / 60000;

		FullCalendar calendar = Mockito.mock(FullCalendar.class);
		Config calendarConfig = Mockito.mock(Config.class);
		Mockito.when(calendarConfig.isIgnoreTimezone()).thenReturn(true);
		Mockito.when(calendar.getConfig()).thenReturn(calendarConfig);
		DateTime startDate = new DateTime();
		DateTime endDate = new DateTime();

		ImmutablePair<DateTime, DateTime> convertedEventDates = CalendarHelper.convertTimezone(calendar, startDate, endDate, 0);

		assertEquals(startDate.minusMinutes(defaultOffset), convertedEventDates.getLeft());
		assertEquals(endDate.minusMinutes(defaultOffset), convertedEventDates.getRight());

		convertedEventDates = CalendarHelper.convertTimezone(calendar, startDate, endDate, defaultOffset);

		assertEquals(startDate, convertedEventDates.getLeft());
		assertEquals(endDate, convertedEventDates.getRight());
	}
}
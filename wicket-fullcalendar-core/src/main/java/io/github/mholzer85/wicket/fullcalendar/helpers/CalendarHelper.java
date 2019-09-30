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

import io.github.mholzer85.wicket.fullcalendar.FullCalendar;
import lombok.NonNull;

/**
 * @author mholzer
 */
public final class CalendarHelper {

	private CalendarHelper() {
	}


	/**
	 * Converts start and end of a calendar event between local (server) and remote (client) time zone.
	 *
	 * @param calendar FullCalendar to get the timezone config from
	 * @param start DateTime start of calendar event
	 * @param end DateTime end of calendar event
	 * @param remoteOffset time zone offset given by client via Ajax
	 * @return Pair of DateTimes (start, end) representing the converted dates/times of the event
	 */
	@NonNull
	public static ImmutablePair<DateTime, DateTime> convertTimezone(@NonNull FullCalendar calendar, @NonNull DateTime start, @NonNull DateTime end,
																	int remoteOffset) {
		if (calendar.getConfig().isIgnoreTimezone()) {
			// Convert to same DateTime in local time zone.
			int localOffset = DateTimeZone.getDefault().getOffset(null) / 60000;
			int minutesAdjustment = remoteOffset - localOffset;
			start = start.plusMinutes(minutesAdjustment);
			end = end.plusMinutes(minutesAdjustment);
		}
		return ImmutablePair.of(start, end);
	}
}

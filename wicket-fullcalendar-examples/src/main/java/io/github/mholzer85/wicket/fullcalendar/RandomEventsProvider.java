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

package io.github.mholzer85.wicket.fullcalendar;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.util.time.Duration;
import org.joda.time.DateTime;

import lombok.NonNull;

public class RandomEventsProvider implements EventProvider {

	private Map<Integer, Event> events = new HashMap<>();

	private final String title;


	public RandomEventsProvider(@NonNull String title) {
		this.title = title;
	}


	@NonNull
	@Override
	public Collection<Event> getEvents(@NonNull DateTime start, @NonNull DateTime end) {
		events.clear();
		SecureRandom random = new SecureRandom();

		Duration duration = Duration.valueOf(end.getMillis()
				- start.getMillis());

		for (int j = 0; j < 1; j++) {
			for (int i = 0; i < duration.days() + 1; i++) {
				DateTime calendar = start;
				calendar = calendar.plusDays(i).withHourOfDay(
						6 + random.nextInt(10));

				Event event = new Event();
				int id = (int)(j * duration.days() + i);
				event.setId("" + id);
				event.setTitle(title + (1 + i));
				event.setStart(calendar);
				calendar = calendar.plusHours(random.nextInt(8));
				event.setEnd(calendar);

				events.put(id, event);
			}
		}
		return events.values();
	}


	@NonNull
	@Override
	public Event getEventForId(@NonNull String id) {
		Integer idd = Integer.valueOf(id);
		Event event = events.get(idd);
		if (event != null) {
			return event;
		}
		throw new EventNotFoundException("Event with id: " + id
				+ " not found");
	}

}

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

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.string.Strings;
import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class CalendarResponse {

	@NonNull
	private final FullCalendar calendar;
	@Getter
	@NonNull
	private final AjaxRequestTarget target;


	@NonNull
	public CalendarResponse refetchEvents() {
		return execute(q("refetchEvents"));
	}


	@NonNull
	public CalendarResponse refetchEvents(@NonNull EventSource source) {
		toggleEventSource(source, false);
		return toggleEventSource(source, true);
	}


	@NonNull
	public CalendarResponse refetchEvents(@Nullable String sourceId) {
		toggleEventSource(sourceId, false);
		return toggleEventSource(sourceId, true);
	}


	@NonNull
	public CalendarResponse refetchEvent(@NonNull EventSource source, @Nullable Event event) {
		// for now we have an unoptimized implementation
		// later we can replace this by searching for the affected event in the
		// clientside buffer
		// and refetching it

		return refetchEvents(source);
	}


	@NonNull
	public CalendarResponse refetchEvent(@Nullable String sourceId, @Nullable String eventId) {
		// for now we have an unoptimized implementation
		// later we can replace this by searching for the affected event in the
		// clientside buffer
		// and refetching it

		return refetchEvents(sourceId);
	}


	@NonNull
	public CalendarResponse toggleEventSource(@Nullable String sourceId, boolean enabled) {
		return execute(q("toggleSource"), q(sourceId), String.valueOf(enabled));
	}


	@NonNull
	public CalendarResponse toggleEventSource(@NonNull EventSource source, boolean enabled) {
		return execute(q("toggleSource"), q(source.getUuid()), String.valueOf(enabled));
	}


	@NonNull
	public CalendarResponse removeEvent(@Nullable String id) {
		return execute(q("removeEvents"), q(id));
	}


	@NonNull
	public CalendarResponse removeEvent(@NonNull Event event) {
		return execute(q("removeEvents"), q(event.getId()));
	}


	@NonNull
	public CalendarResponse gotoDate(@NonNull Date date) {
		return execute(q("gotoDate"), "new Date(" + date.getTime() + ")");
	}


	@NonNull
	private CalendarResponse execute(@NonNull String... args) {
		String js = String.format("$('#%s').fullCalendarExt(" + Strings.join(",", args) + ");", calendar.getMarkupId());
		target.appendJavaScript(js);
		return this;
	}


	@NonNull
	private static String q(@Nullable Object o) {
		if (o == null) {
			return "null";
		}

		return "'" + o.toString() + "'";
	}


	/**
	 * Clears the client-side selection highlight.
	 *
	 * @return this for chaining
	 */
	@NonNull
	public CalendarResponse clearSelection() {
		return execute(q("unselect"));
	}

}

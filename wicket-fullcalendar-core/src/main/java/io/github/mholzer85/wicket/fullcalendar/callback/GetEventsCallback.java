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

package io.github.mholzer85.wicket.fullcalendar.callback;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.util.collections.MicroMap;
import org.joda.time.DateTime;

import io.github.mholzer85.wicket.fullcalendar.EventProvider;
import io.github.mholzer85.wicket.fullcalendar.EventSource;
import io.github.mholzer85.wicket.fullcalendar.helpers.CalendarHelper;
import io.github.mholzer85.wicket.fullcalendar.helpers.ImmutablePair;
import lombok.NonNull;

public class GetEventsCallback extends AbstractCallback {

	private static final String SOURCE_ID = "sid";


	@NonNull
	public String getUrl(@NonNull EventSource source) {
		return getUrl(new MicroMap<>(SOURCE_ID, source.getUuid()));
	}


	@Override
	protected void respond() {
		// get request & parameters
		Request r = getCalendar().getRequest();
		String sid = r.getRequestParameters().getParameterValue(SOURCE_ID).toString();
		DateTime start = new DateTime(r.getRequestParameters().getParameterValue("start").toLong());
		DateTime end = new DateTime(r.getRequestParameters().getParameterValue("end").toLong());

		// convert timezone if necessary
		int remoteOffset = r.getRequestParameters().getParameterValue("timezoneOffset").toInt();
		ImmutablePair<DateTime, DateTime> dateRange = CalendarHelper.convertTimezone(getCalendar(), start, end, remoteOffset);
		start = dateRange.getLeft();
		end = dateRange.getRight();

		// get events / create response
		EventSource source = getCalendar().getEventManager().getEventSource(sid);
		EventProvider provider = source.getEventProvider();
		String response = getCalendar().toJson(provider.getEvents(start, end));

		getCalendar().getRequestCycle().scheduleRequestHandlerAfterCurrent(new TextRequestHandler("application/json", "UTF-8", response));
	}
}

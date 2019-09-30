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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.Request;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import io.github.mholzer85.wicket.fullcalendar.CalendarResponse;
import io.github.mholzer85.wicket.fullcalendar.ViewType;
import lombok.NonNull;

/**
 * A base callback that passes back calendar's starting date
 *
 * @author igor
 */
public abstract class ViewRenderCallback extends AbstractAjaxCallback implements CallbackWithHandler {

	@NonNull
	@Override
	protected String configureCallbackScript(@NonNull String script, @NonNull String urlTail) {
		return script.replace(urlTail, "&view=\"+view.name+\"&start=\"+fullCalendarExtIsoDate(view.start.toDate())+\"&end=\"+fullCalendarExtIsoDate(view.end"
				+ ".toDate())+\"&intervalStart=\"+fullCalendarExtIsoDate(view.intervalStart.toDate())+\"&intervalEnd=\"+fullCalendarExtIsoDate(view"
				+ ".intervalEnd.toDate())+\"");
	}


	@NonNull
	@Override
	public String getHandlerScript() {
		return String.format("function(view, element) {%s;}", getCallbackScript());
	}


	@Override
	protected void respond(@NonNull AjaxRequestTarget target) {
		Request r = target.getPage().getRequest();
		ViewType type = ViewType.forCode(r.getRequestParameters().getParameterValue("view").toString());
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser().withZone(DateTimeZone.UTC);
		DateMidnight start = fmt.parseDateTime(r.getRequestParameters().getParameterValue("start").toString()).toDateMidnight();
		DateMidnight end = fmt.parseDateTime(r.getRequestParameters().getParameterValue("end").toString()).toDateMidnight();
		DateMidnight intervalStart = fmt.parseDateTime(r.getRequestParameters().getParameterValue("intervalStart").toString()).toDateMidnight();
		DateMidnight intervalEnd = fmt.parseDateTime(r.getRequestParameters().getParameterValue("intervalEnd").toString()).toDateMidnight();
		View view = new View(type, start, end, intervalStart, intervalEnd);
		CalendarResponse response = new CalendarResponse(getCalendar(), target);
		onViewRendered(view, response);
	}


	protected abstract void onViewRendered(@NonNull View view, @NonNull CalendarResponse response);
}

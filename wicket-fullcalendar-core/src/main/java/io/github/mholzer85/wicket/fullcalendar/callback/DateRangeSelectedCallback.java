/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.github.mholzer85.wicket.fullcalendar.callback;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.Request;
import org.joda.time.DateTime;

import io.github.mholzer85.wicket.fullcalendar.CalendarResponse;
import io.github.mholzer85.wicket.fullcalendar.helpers.CalendarHelper;
import io.github.mholzer85.wicket.fullcalendar.helpers.ImmutablePair;
import lombok.NonNull;

/**
 * Callback that's executed when a range of dates is selected in the calendar.
 */
public abstract class DateRangeSelectedCallback extends AbstractAjaxCallback implements CallbackWithHandler {

	@NonNull
	@Override
	protected String configureCallbackScript(@NonNull String script, @NonNull String urlTail) {
		/*
		 * According to https://fullcalendar.io/docs/select-callback the prior "allDay" parameter is now obsolete
		 * and can be reproduced by checking start.hasTime() and end.hasTime().
		 * */
		return script.replace(urlTail,
				"&timezoneOffset=\"+startDate.utcOffset()+\"&startDate=\"+startDate.valueOf()+\"&endDate=\"+endDate.valueOf()+\"&allDay=\"+(!(startDate"
						+ ".hasTime()||endDate.hasTime()))+\"");
	}


	/**
	 * @see <a href="https://fullcalendar.io/docs/select-callback">https://fullcalendar.io/docs/select-callback</a>
	 */
	@NonNull
	@Override
	public String getHandlerScript() {
		return "function(startDate, endDate, jsEvent, view) { " + getCallbackScript() + "}";
	}


	@Override
	protected void respond(@NonNull AjaxRequestTarget target) {
		// get request & parameters
		Request r = getCalendar().getRequest();
		DateTime start = new DateTime(r.getRequestParameters().getParameterValue("startDate").toLong());
		DateTime end = new DateTime(r.getRequestParameters().getParameterValue("endDate").toLong());
		boolean allDay = r.getRequestParameters().getParameterValue("allDay").toBoolean();

		// convert timezone if necessary
		int remoteOffset = r.getRequestParameters().getParameterValue("timezoneOffset").toInt();
		ImmutablePair<DateTime, DateTime> dateRange = CalendarHelper.convertTimezone(getCalendar(), start, end, remoteOffset);
		start = dateRange.getLeft();
		end = dateRange.getRight();

		// create response / run callback method
		onSelect(new SelectedRange(start, end, allDay), new CalendarResponse(getCalendar(), target));
	}


	protected abstract void onSelect(@NonNull SelectedRange range, @NonNull CalendarResponse response);

}

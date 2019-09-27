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

import io.github.mholzer85.wicket.fullcalendar.CalendarResponse;
import io.github.mholzer85.wicket.fullcalendar.Event;
import io.github.mholzer85.wicket.fullcalendar.EventSource;
import lombok.NonNull;

public abstract class EventDroppedCallback extends AbstractAjaxCallbackWithClientsideRevert implements CallbackWithHandler {

	@NonNull
	@Override
	protected String configureCallbackScript(@NonNull String script, @NonNull String urlTail) {
		return script.replace(urlTail, "&eventId=\"+event.id+\"&sourceId=\"+event.source.id+\"&minuteDelta=\"+delta.asMinutes()+\"&allDay=\"+event.start"
				+ ".hasTime()+\"");
	}


	@NonNull
	@Override
	public String getHandlerScript() {
		return "function(event, delta, revertFunc, jsEvent, ui, view) {" + getCallbackScript() + "}";
	}


	@Override
	protected boolean onEvent(@NonNull AjaxRequestTarget target) {
		Request r = getCalendar().getRequest();
		String eventId = r.getRequestParameters().getParameterValue("eventId").toString();
		String sourceId = r.getRequestParameters().getParameterValue("sourceId").toString();

		EventSource source = getCalendar().getEventManager().getEventSource(sourceId);
		Event event = source.getEventProvider().getEventForId(eventId);

		// minuteDelta already contains the complete delta in minutes, so we can set daysDelta to 0
		int minuteDelta = r.getRequestParameters().getParameterValue("minuteDelta").toInt();
		boolean allDay = r.getRequestParameters().getParameterValue("allDay").toBoolean();

		return onEventDropped(new DroppedEvent(source, event, 0, minuteDelta, allDay), new CalendarResponse(getCalendar(), target));
	}


	protected abstract boolean onEventDropped(@NonNull DroppedEvent event, @NonNull CalendarResponse response);


	@NonNull
	@Override
	protected String getRevertScript() {
		return "revertFunc();";
	}

}

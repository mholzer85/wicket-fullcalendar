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

public abstract class EventClickedCallback extends AbstractAjaxCallback implements CallbackWithHandler {

	@NonNull
	@Override
	protected String configureCallbackScript(@NonNull String script, @NonNull String urlTail) {
		return script.replace(urlTail, "&eventId=\"+event.id+\"&sourceId=\"+event.source.id+\"");
	}


	@NonNull
	@Override
	public String getHandlerScript() {
		return "function(event, jsEvent, view) {" + getCallbackScript() + " return false; }";
	}


	@Override
	protected void respond(@NonNull AjaxRequestTarget target) {
		Request r = getCalendar().getRequest();
		String eventId = r.getRequestParameters().getParameterValue("eventId").toString();
		String sourceId = r.getRequestParameters().getParameterValue("sourceId").toString();

		EventSource source = getCalendar().getEventManager().getEventSource(sourceId);
		Event event = source.getEventProvider().getEventForId(eventId);

		onClicked(new ClickedEvent(source, event), new CalendarResponse(getCalendar(), target));
	}


	protected abstract void onClicked(@NonNull ClickedEvent event, @NonNull CalendarResponse response);
}

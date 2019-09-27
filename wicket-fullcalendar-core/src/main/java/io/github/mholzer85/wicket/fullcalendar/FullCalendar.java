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

import java.util.UUID;

import org.apache.wicket.IRequestListener;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.collections.MicroMap;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import io.github.mholzer85.wicket.fullcalendar.callback.ClickedEvent;
import io.github.mholzer85.wicket.fullcalendar.callback.DateRangeSelectedCallback;
import io.github.mholzer85.wicket.fullcalendar.callback.DroppedEvent;
import io.github.mholzer85.wicket.fullcalendar.callback.EventClickedCallback;
import io.github.mholzer85.wicket.fullcalendar.callback.EventDroppedCallback;
import io.github.mholzer85.wicket.fullcalendar.callback.EventResizedCallback;
import io.github.mholzer85.wicket.fullcalendar.callback.GetEventsCallback;
import io.github.mholzer85.wicket.fullcalendar.callback.ResizedEvent;
import io.github.mholzer85.wicket.fullcalendar.callback.SelectedRange;
import io.github.mholzer85.wicket.fullcalendar.callback.View;
import io.github.mholzer85.wicket.fullcalendar.callback.ViewRenderCallback;
import lombok.Getter;
import lombok.NonNull;

public class FullCalendar extends AbstractFullCalendar implements IRequestListener {

	private static final TextTemplate EVENTS = new PackageTextTemplate(FullCalendar.class, "FullCalendar.events.tpl");

	@Getter
	private final Config config;

	private EventDroppedCallback eventDropped;
	private EventResizedCallback eventResized;
	private GetEventsCallback getEvents;
	private DateRangeSelectedCallback dateRangeSelected;
	private EventClickedCallback eventClicked;
	private ViewRenderCallback viewRender;


	public FullCalendar(@NonNull String id, @NonNull Config config) {
		super(id);
		this.config = config;
		setVersioned(false);
	}


	@Override
	protected boolean getStatelessHint() {
		return false;
	}


	@NonNull
	public EventManager getEventManager() {
		return new EventManager(this);
	}


	@Override
	protected void onInitialize() {
		super.onInitialize();
		for (EventSource source : config.getEventSources()) {
			if (source.getUuid() == null) {
				String uuid = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "");
				source.setUuid(uuid);
			}
		}
	}


	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		setupCallbacks();
	}


	/**
	 * Configures callback urls to be used by fullcalendar js to talk to this component. If you wish to use custom
	 * callbacks you should override this method and set them here.
	 * <p>
	 * NOTE: This method is called every time this component is rendered to keep the urls current, so if you set them
	 * outside this method they will most likely be overwritten by the default ones.
	 */
	protected void setupCallbacks() {

		if (getEvents == null) {
			getEvents = new GetEventsCallback();
			add(getEvents);
		}
		for (EventSource source : config.getEventSources()) {
			source.setEvents(EVENTS.asString(new MicroMap<>("url", getEvents.getUrl(source))));
		}

		if (eventClicked == null) {
			eventClicked = new EventClickedCallback() {

				@Override
				protected void onClicked(@NonNull ClickedEvent event, @NonNull CalendarResponse response) {
					onEventClicked(event, response);
				}
			};
			add(eventClicked);
		}
		config.setEventClick(eventClicked.getHandlerScript());

		if (dateRangeSelected == null) {
			dateRangeSelected = new DateRangeSelectedCallback() {

				@Override
				protected void onSelect(@NonNull SelectedRange range, @NonNull CalendarResponse response) {
					onDateRangeSelected(range, response);
				}
			};
			add(dateRangeSelected);
		}
		config.setSelect(dateRangeSelected.getHandlerScript());

		if (eventDropped == null) {
			eventDropped = new EventDroppedCallback() {

				@Override
				protected boolean onEventDropped(@NonNull DroppedEvent event, @NonNull CalendarResponse response) {
					return FullCalendar.this.onEventDropped(event, response);
				}
			};
			add(eventDropped);
		}
		config.setEventDrop(eventDropped.getHandlerScript());

		if (eventResized == null) {
			eventResized = new EventResizedCallback() {

				@Override
				protected boolean onEventResized(@NonNull ResizedEvent event, @NonNull CalendarResponse response) {
					return FullCalendar.this.onEventResized(event, response);
				}

			};
			add(eventResized);
		}
		config.setEventResize(eventResized.getHandlerScript());

		if (viewRender == null) {
			viewRender = new ViewRenderCallback() {

				@Override
				protected void onViewRendered(@NonNull View view, @NonNull CalendarResponse response) {
					FullCalendar.this.onViewDisplayed(view, response);
				}
			};
			add(viewRender);
		}
		config.setViewRender(viewRender.getHandlerScript());

		getPage().dirty();
	}


	@Override
	public void renderHead(@NonNull IHeaderResponse response) {
		super.renderHead(response);

		String configuration = "$(\"#" + getMarkupId() + "\").fullCalendarExt(";
		configuration += Json.toJson(config);
		configuration += ");";

		response.render(OnDomReadyHeaderItem.forScript(configuration));
	}


	protected boolean onEventDropped(@NonNull DroppedEvent event, @NonNull CalendarResponse response) {
		// callback method that can be overwritten
		return false;
	}


	protected boolean onEventResized(@NonNull ResizedEvent event, @NonNull CalendarResponse response) {
		// callback method that can be overwritten
		return false;
	}


	protected void onDateRangeSelected(@NonNull SelectedRange range, @NonNull CalendarResponse response) {
		// callback method that can be overwritten
	}


	protected void onEventClicked(@NonNull ClickedEvent event, @NonNull CalendarResponse response) {
		// callback method that can be overwritten
	}


	protected void onViewDisplayed(@NonNull View view, @NonNull CalendarResponse response) {
		// callback method that can be overwritten
	}


	@Override
	public void onRequest() {
		getEvents.onRequest();
	}

}

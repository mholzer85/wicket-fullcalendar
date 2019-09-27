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

import java.util.Locale;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.joda.time.LocalTime;

import io.github.mholzer85.wicket.fullcalendar.callback.ClickedEvent;
import io.github.mholzer85.wicket.fullcalendar.callback.DroppedEvent;
import io.github.mholzer85.wicket.fullcalendar.callback.ResizedEvent;
import io.github.mholzer85.wicket.fullcalendar.callback.SelectedRange;
import io.github.mholzer85.wicket.fullcalendar.callback.View;
import io.github.mholzer85.wicket.fullcalendar.selector.EventSourceSelector;
import lombok.NonNull;

public class HomePage extends WebPage {

	public HomePage() {

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		Config config = new Config();
		config.setSelectable(true);
		config.setSelectHelper(false);

		EventSource reservations = new EventSource();
		reservations.setTitle("Reservations");
		reservations.setEventProvider(new RandomEventsProvider("Reservation "));
		reservations.setEditable(true);
		reservations.setBackgroundColor("#63BA68");
		reservations.setBorderColor("#63BA68");
		config.add(reservations);

		EventSource downtimes = new EventSource();
		downtimes.setTitle("Maintenance");
		downtimes.setBackgroundColor("#B1ADAC");
		downtimes.setBorderColor("#B1ADAC");
		downtimes.setEventProvider(new RandomEventsProvider("Maintenance "));
		config.add(downtimes);

		EventSource other = new EventSource();
		other.setTitle("Other Reservations");
		other.setBackgroundColor("#E6CC7F");
		other.setBorderColor("#E6CC7F");
		other.setEventProvider(new RandomEventsProvider("Other Reservations "));
		config.add(other);

		config.getHeader().setLeft("prev,next today");
		config.getHeader().setCenter("title");
		config.getHeader().setRight("month,agendaWeek,agendaDay listMonth,listYear");

		config.getButtonText().setToday("Week");
		config.getButtonText().setListMonth("list month");
		config.getButtonText().setListYear("list year");

		config.setLocale(Locale.getDefault().toString());

		config.setLoading("function(bool) { if (bool) $(\"#loading\").show(); else $(\"#loading\").hide(); }");

		config.setMinTime(new LocalTime(6, 30));
		config.setMaxTime(new LocalTime(17, 30));
		config.setAllDaySlot(false);
		FullCalendar calendar = new FullCalendar("cal", config) {

			@Override
			protected void onDateRangeSelected(@NonNull SelectedRange range,
											   @NonNull CalendarResponse response) {
				info("Selected region: " + range.getStart() + " - "
						+ range.getEnd() + " / allDay: " + range.isAllDay());

				response.getTarget().add(feedback);
			}


			@Override
			protected boolean onEventDropped(@NonNull DroppedEvent event,
											 @NonNull CalendarResponse response) {
				info("Event drop. eventId: " + event.getEvent().getId()
						+ " sourceId: " + event.getSource().getUuid()
						+ " dayDelta: " + event.getDaysDelta()
						+ " minuteDelta: " + event.getMinutesDelta()
						+ " allDay: " + event.isAllDay());
				info("Original start time: " + event.getEvent().getStart()
						+ ", original end time: " + event.getEvent().getEnd());
				info("New start time: " + event.getNewStartTime()
						+ ", new end time: " + event.getNewEndTime());

				response.getTarget().add(feedback);
				return false;
			}


			@Override
			protected boolean onEventResized(@NonNull ResizedEvent event,
											 @NonNull CalendarResponse response) {
				info("Event resized. eventId: " + event.getEvent().getId()
						+ " sourceId: " + event.getSource().getUuid()
						+ " dayDelta: " + event.getDaysDelta()
						+ " minuteDelta: " + event.getMinutesDelta());
				response.getTarget().add(feedback);
				return false;
			}


			@Override
			protected void onEventClicked(@NonNull ClickedEvent event,
										  @NonNull CalendarResponse response) {
				info("Event clicked. eventId: " + event.getEvent().getId()
						+ ", sourceId: " + event.getSource().getUuid());
				response.refetchEvents();
				response.getTarget().add(feedback);
			}


			@Override
			protected void onViewDisplayed(@NonNull View view, @NonNull CalendarResponse response) {

				info("View displayed. viewType: " + view.getType().name()
						+ ", start: " + view.getStart() + ", end: "
						+ view.getEnd());
				response.getTarget().add(feedback);
			}
		};
		calendar.setMarkupId("calendar");
		add(calendar);
		add(new EventSourceSelector("selector", calendar));
	}

}

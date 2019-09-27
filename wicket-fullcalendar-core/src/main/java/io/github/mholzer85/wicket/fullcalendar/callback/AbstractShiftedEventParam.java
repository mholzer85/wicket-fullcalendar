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

import org.joda.time.DateTime;

import io.github.mholzer85.wicket.fullcalendar.Event;
import io.github.mholzer85.wicket.fullcalendar.EventSource;
import lombok.Getter;
import lombok.NonNull;

@Getter
class AbstractShiftedEventParam extends AbstractEventParam {

	private final int daysDelta;
	private final int minutesDelta;


	AbstractShiftedEventParam(@NonNull EventSource source, @NonNull Event event, int hoursDelta, int minutesDelta) {
		super(source, event);
		this.daysDelta = hoursDelta;
		this.minutesDelta = minutesDelta;
	}


	@NonNull
	public DateTime getNewStartTime() {
		return shift(getEvent().getStart());
	}


	@NonNull
	public DateTime getNewEndTime() {
		return shift(getEvent().getEnd());
	}


	@NonNull
	private DateTime shift(@NonNull DateTime start) {
		return start.plusDays(daysDelta).plusMinutes(minutesDelta);
	}

}

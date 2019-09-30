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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.jetbrains.annotations.Nullable;

import io.github.mholzer85.wicket.fullcalendar.selector.EventSourceSelector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors (chain = true)
@Getter
@Setter
@ToString
public class EventSource implements Serializable {

	private static final String TITLE = "fcxTitle";
	private static final String UUID = "fcxUuid";

	private String id;
	private String color;
	private String backgroundColor;
	private String borderColor;
	private String textColor;
	private String className;
	private Boolean editable;
	private Boolean allDayDefault;
	private Boolean ignoreTimezone;
	private String error;

	private Map<String, String> data = new HashMap<>();

	@JsonRawValue
	private String events;

	@JsonIgnore
	private EventProvider eventProvider;

	/**
	 * If <var>enableInSelector</var> is {@code true} then the check box for this EventSource, if included in a
	 * {@link EventSourceSelector} to begin with, will be enabled. If {@code false} then the check box will not be
	 * enabled. Default is {@code true}.
	 */
	private Boolean enableInSelector = true;

	/**
	 * If <var>includeInSelector</var> is {@code true} then this EventSource will be included in a
	 * {@link EventSourceSelector}, if one exists for the {@link FullCalendar} containing this EventSource. If
	 * {@code false} then this EventSource will not be included. Default is {@code true}.
	 */
	private Boolean includeInSelector = true;


	@NonNull
	public EventSource setTitle(@NonNull String title) {
		data.put(TITLE, title);
		return this;
	}


	@JsonIgnore
	@Nullable
	public String getTitle() {
		return data.get(TITLE);
	}


	@JsonIgnore
	@Nullable
	public String getUuid() {
		return data.get(UUID);
	}


	@NonNull
	public EventSource setUuid(@NonNull String uuid) {
		data.put(UUID, uuid);
		if (getId() == null) {
			setId(uuid);
		}
		return this;
	}

}

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

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.IRequestListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.Nullable;

import io.github.mholzer85.wicket.fullcalendar.FullCalendar;
import lombok.NonNull;

abstract class AbstractCallback extends Behavior implements IRequestListener {

	private FullCalendar calendar;


	@Override
	public void bind(@NonNull Component component) {
		super.bind(component);
		this.calendar = (FullCalendar)component;
	}


	@NonNull
	final String getUrl(@Nullable Map<String, Object> parameters) {
		PageParameters params = new PageParameters();
		StringBuilder url = new StringBuilder(calendar.urlForListener(this, params).toString());

		if (parameters != null) {
			for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
				url.append("&").append(parameter.getKey()).append("=").append(parameter.getValue());
			}
		}
		return url.toString();
	}


	@Override
	public final void onRequest() {
		respond();
	}


	protected abstract void respond();


	@NonNull
	final FullCalendar getCalendar() {
		return calendar;
	}


	@Override
	public boolean getStatelessHint(@Nullable Component component) {
		return false;
	}


	@Override
	public boolean rendersPage() {
		return false;
	}
}

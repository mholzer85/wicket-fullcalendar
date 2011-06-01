/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ftlines.wicket.fullcalendar.callback;

import net.ftlines.wicket.fullcalendar.EventProvider;
import net.ftlines.wicket.fullcalendar.EventSource;

import org.apache.wicket.Request;
import org.apache.wicket.request.target.basic.StringRequestTarget;
import org.apache.wicket.util.collections.MicroMap;
import org.joda.time.DateTime;


public class GetEventsCallback extends AbstractCallback
{
	private static final String SOURCE_ID = "sid";

	public String getUrl(EventSource source)
	{
		return getUrl(new MicroMap<String, Object>(SOURCE_ID, source.getUuid()));
	}

	@Override
	protected void respond()
	{
		Request r = getCalendar().getRequest();
		String sid = r.getParameter(SOURCE_ID);
		DateTime start = new DateTime(Long.valueOf(r.getParameter("start")));
		DateTime end = new DateTime(Long.valueOf(r.getParameter("end")));

		EventSource source = getCalendar().getEventManager().getEventSource(sid);
		EventProvider provider = source.getEventProvider();
		String response = getCalendar().toJson(provider.getEvents(start, end));
		getCalendar().getRequestCycle().setRequestTarget(new StringRequestTarget(response));
	}
}

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

import org.joda.time.DateMidnight;

import io.github.mholzer85.wicket.fullcalendar.ViewType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class View {

	private final ViewType type;
	private final DateMidnight start;
	private final DateMidnight end;
	private final DateMidnight intervalStart;
	private final DateMidnight intervalEnd;

}

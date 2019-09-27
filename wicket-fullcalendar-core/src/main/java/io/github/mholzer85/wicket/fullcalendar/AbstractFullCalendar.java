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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.wicket.ajax.WicketAjaxJQueryResourceReference;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

abstract class AbstractFullCalendar extends WebMarkupContainer {

	/**
	 * This is the original CSS file from the FullCalendar.io project. Can/should be updated when updating FullCalendar version.
	 */
	private static final ResourceReference CSS = new PackageResourceReference(AbstractFullCalendar.class, "res/fullcalendar.min.css");

	/**
	 * <p>JS files for FullCalendar and the integration of it into Wicket. The order of the files is important.</p>
	 * <p>With the exception of 'fullcalendar.ext.js', these are the original JavaScript files from the FullCalendar.io project (and related projects).
	 * These can/should be updated when updating FullCalendar version.</p>
	 */
	private static final Set<ResourceReference> JS_FILES;

	static {
		Set<ResourceReference> sortedSet = new LinkedHashSet<>();
		sortedSet.add(new PackageResourceReference(AbstractFullCalendar.class, "res/moment.min.js"));
		sortedSet.add(new PackageResourceReference(AbstractFullCalendar.class, "res/fullcalendar.min.js"));
		sortedSet.add(new PackageResourceReference(AbstractFullCalendar.class, "res/fullcalendar.ext.js"));
		sortedSet.add(new PackageResourceReference(AbstractFullCalendar.class, "res/locale-all.js"));
		JS_FILES = Collections.unmodifiableSet(sortedSet);
	}


	AbstractFullCalendar(@NonNull String id) {
		super(id);
	}


	/**
	 * Renders the necessary Javascript files for FullCalendar.
	 */
	@Override
	public void renderHead(@NonNull IHeaderResponse response) {
		// first include jQuery:
		response.render(JavaScriptHeaderItem.forReference(WicketAjaxJQueryResourceReference.get()));
		// then include Fullcalendar-specific files:
		response.render(CssReferenceHeaderItem.forReference(CSS));
		for (ResourceReference jsFile : JS_FILES) {
			response.render(JavaScriptReferenceHeaderItem.forReference(jsFile));
		}
	}


	@NonNull
	public final String toJson(@Nullable Object value) {
		return Json.toJson(value);
	}
}

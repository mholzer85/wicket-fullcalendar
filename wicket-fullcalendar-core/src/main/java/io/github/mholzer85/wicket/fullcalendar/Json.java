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

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.module.SimpleModule;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
final class Json {

	@NonNull
	static String toJson(@Nullable Object object) {
		String json;
		try {
			StringWriter sw = new StringWriter();
			ObjectMapper mapper = new ObjectMapper();
			MappingJsonFactory jsonFactory = new MappingJsonFactory();
			try (JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw)) {
				SimpleModule module = new SimpleModule("fullcalendar", new Version(1, 0, 0, null));
				module.addSerializer(new DateTimeSerializer());
				module.addSerializer(new LocalTimeSerializer());
				mapper.registerModule(module);
				mapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);

				mapper.writeValue(jsonGenerator, object);
				sw.close();
			}
			json = sw.toString();
		}
		catch (Exception e) {
			throw new JsonRuntimeException("Error encoding object: " + object + " into JSON string", e);
		}
		return json;
	}


	static class DateTimeSerializer extends JsonSerializer<DateTime> {

		@Override
		public void serialize(@NonNull DateTime value, @NonNull JsonGenerator jgen, @NonNull SerializerProvider provider) throws IOException {
			jgen.writeString(ISODateTimeFormat.dateTime().print(value));
		}


		@NonNull
		@Override
		public Class<DateTime> handledType() {
			return DateTime.class;
		}
	}


	static class LocalTimeSerializer extends JsonSerializer<LocalTime> {

		@Override
		public void serialize(@NonNull LocalTime value, @NonNull JsonGenerator jgen, @NonNull SerializerProvider provider) throws IOException {
			jgen.writeString(value.toString("h:mmaa"));
		}


		@NonNull
		@Override
		public Class<LocalTime> handledType() {
			return LocalTime.class;
		}

	}
}

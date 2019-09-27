package io.github.mholzer85.wicket.fullcalendar;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomEventsProviderTest {

	private static final String TITLE = "Random Foo";
	private static final DateTime testDate = new DateTime();
	private static RandomEventsProvider randomEventsProvider;


	@BeforeAll
	static void setUp() {
		randomEventsProvider = new RandomEventsProvider(TITLE);
	}


	@SuppressWarnings ("ConstantConditions")
	@Test
	void randomEventsProviderNonNullChecks() {
		assertThrows(NullPointerException.class, () -> new RandomEventsProvider(null));
		assertThrows(NullPointerException.class, () -> randomEventsProvider.getEventForId(null));
		assertThrows(NullPointerException.class, () -> randomEventsProvider.getEvents(null, null));
		assertThrows(NullPointerException.class, () -> randomEventsProvider.getEvents(testDate, null));
	}


	@Test
	void getEvents() {
		assertFalse(randomEventsProvider.getEvents(testDate, testDate).isEmpty());
	}


	@Test
	void getEventForId() {
		Event event = randomEventsProvider.getEventForId("0");
		assertTrue(event.getTitle().startsWith(TITLE));
		assertThrows(EventNotFoundException.class, () -> randomEventsProvider.getEventForId("99"));
	}
}
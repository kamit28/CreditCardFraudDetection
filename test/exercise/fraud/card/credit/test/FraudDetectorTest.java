package exercise.fraud.card.credit.test;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exercise.fraud.card.credit.FraudDetector;
import exercise.fraud.card.credit.model.TransactionDto;

public class FraudDetectorTest {

	private FraudDetector fraudDetector;

	@Before
	public void setUp() throws Exception {
		fraudDetector = new FraudDetector();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetFraudulentCardsForAllValidRecords() {
		List<String> transactions = new ArrayList<>();
		transactions.add("abcd,2014-04-29T13:15:54, 80.00");
		transactions.add("bcde,2014-04-29T13:15:54, 80.00");
		transactions.add("cdef,2014-04-29T13:15:54, 10.00");
		transactions.add("abcd,2014-04-29T13:15:54, 30.00");
		transactions.add("bcde,2014-04-30T13:15:54, 10.00");
		transactions.add("bcde,2014-04-29T13:15:54, 10.00");
		transactions.add("defg,2014-04-29T13:15:54, 10.00");

		Instant instant = Instant.parse("2014-04-29T00:00:00Z");
		ZonedDateTime date = ZonedDateTime.ofInstant(instant,
				ZoneId.of("Australia/Sydney"));

		List<String> faudulentCards = fraudDetector.getFraudulentCards(
				transactions, date, 100.00);

		assertNotNull(faudulentCards);
		assertFalse(faudulentCards.isEmpty());
		assertEquals(1, faudulentCards.size());
		assertEquals("abcd", faudulentCards.get(0));
	}

	@Test
	public final void testGetFraudulentCardsWhenListContainsAllInvalidRecords() {
		List<String> transactions = new ArrayList<>();
		transactions.add("abcd,2014-04-, 80.00");
		transactions.add("bcde,2014-04-29T13:15:54, bcd.00");
		transactions.add("");
		transactions.add("abcd,2014-04-29T13:15:54");
		transactions.add("bcde,");

		Instant instant = Instant.parse("2014-04-29T00:00:00Z");
		ZonedDateTime date = ZonedDateTime.ofInstant(instant,
				ZoneId.of("Australia/Sydney"));

		List<String> faudulentCards = fraudDetector.getFraudulentCards(
				transactions, date, 100.00);

		assertNotNull(faudulentCards);
		assertTrue(faudulentCards.isEmpty());
	}

	@Test
	public final void testGetFraudulentCardsWhenListContainsBothValidAndInvalidRecords() {
		List<String> transactions = new ArrayList<>();
		transactions.add("abcd,2014-04-, 80.00");
		transactions.add("bcde,2014-04-29T13:15:54, bcd.00");
		transactions.add("");
		transactions.add("abcd,2014-04-29T13:15:54");
		transactions.add("bcde,");
		transactions.add("abcd,2014-04-29T13:15:54, 80.00");
		transactions.add("bcde,2014-04-29T13:15:54, 80.00");
		transactions.add("cdef,2014-04-29T13:15:54, 10.00");
		transactions.add("abcd,2014-04-29T13:15:54, 30.00");
		transactions.add("defg,2014-04-29T13:15:54, 101.00");
		Instant instant = Instant.parse("2014-04-29T00:00:00Z");
		ZonedDateTime date = ZonedDateTime.ofInstant(instant,
				ZoneId.of("Australia/Sydney"));

		List<String> faudulentCards = fraudDetector.getFraudulentCards(
				transactions, date, 100.00);

		assertNotNull(faudulentCards);
		assertFalse(faudulentCards.isEmpty());
		assertEquals(2, faudulentCards.size());
		assertTrue(faudulentCards.contains("abcd"));
		assertTrue(faudulentCards.contains("defg"));
	}

	@Test
	public final void testGetFraudulentCardsWhenNoCardMathesFraudCriteria() {
		List<String> transactions = new ArrayList<>();
		transactions.add("abcd,2014-04-29T13:15:54, 80.00");
		transactions.add("bcde,2014-04-29T13:15:54, 80.00");
		transactions.add("cdef,2014-04-29T13:15:54, 10.00");
		transactions.add("abcd,2014-04-29T13:15:54, 20.00");
		transactions.add("bcde,2014-04-30T13:15:54, 9.99");
		transactions.add("bcde,2014-04-29T13:15:54, 1.01");
		transactions.add("defg,2014-04-29T13:15:54, 10.00");

		Instant instant = Instant.parse("2014-04-29T00:00:00Z");
		ZonedDateTime date = ZonedDateTime.ofInstant(instant,
				ZoneId.of("Australia/Sydney"));

		List<String> faudulentCards = fraudDetector.getFraudulentCards(
				transactions, date, 100.00);

		assertNotNull(faudulentCards);
		assertTrue(faudulentCards.isEmpty());
		assertEquals(0, faudulentCards.size());
	}

	@Test
	public final void testTranStrToDtoWhenTransactionStringIsEmpty() {
		String transStr = "";
		TransactionDto dto = fraudDetector.tranStrToDto.apply(transStr);
		assertNull(dto);
	}

	@Test
	public final void testTranStrToDtoWhenTransactionStringIsValid() {
		String transStr = "abcd,2014-04-29T13:15:54, 80.00";
		TransactionDto dto = fraudDetector.tranStrToDto.apply(transStr);
		assertNotNull(dto);
		assertEquals("abcd", dto.getTransactionKey().getCardHash());
		Instant instant = Instant.parse("2014-04-29T13:15:54Z");
		LocalDate date = ZonedDateTime.ofInstant(instant,
				ZoneId.of("Australia/Sydney")).toLocalDate();
		assertEquals(date, dto.getTransactionKey().getTransactionTimestamp());
		assertTrue(dto.getPrice() == 80.00d);
	}

	@Test
	public final void testTranStrToDtoWhenTransactionStringHasInvalidCardHash() {
		String transStr = ",2014-04-29T13:15:54, 80.00";
		TransactionDto dto = fraudDetector.tranStrToDto.apply(transStr);
		assertNull(dto);
	}

	@Test
	public final void testTranStrToDtoWhenTransactionStringHasInvalidTimestamp() {
		String transStr = "abcd,2014-sd-29T13:15:54, 80.00";
		TransactionDto dto = fraudDetector.tranStrToDto.apply(transStr);
		assertNull(dto);
	}

	@Test
	public final void testTranStrToDtoWhenTransactionStringHasInvalidPrice() {
		String transStr = "abcd,2014-04-29T13:15:54, qw.00";
		TransactionDto dto = fraudDetector.tranStrToDto.apply(transStr);
		assertNull(dto);
	}
}

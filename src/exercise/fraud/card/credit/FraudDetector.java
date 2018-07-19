package exercise.fraud.card.credit;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import exercise.fraud.card.credit.model.TransactionDto;
import exercise.fraud.card.credit.model.TransactionKey;

public class FraudDetector {

	private static final Logger LOGGER = Logger.getLogger(FraudDetector.class);

	public List<String> getFraudulentCards(List<String> transactions,
			ZonedDateTime date, double dayThreshold) {
		if (null != transactions && transactions.size() > 0) {
			/**
			 * Iterator over input list, parse each record and create
			 * Transaction DTO filter out invalid records group sum of prices of
			 * the transactions by card+timestamp.
			 */
			Map<TransactionKey, Double> transactioDtos = transactions
					.parallelStream()
					.map(tranStrToDto)
					.filter(p -> null != p)
					.collect(
							groupingBy(TransactionDto::getTransactionKey,
									summingDouble(TransactionDto::getPrice)));
			/**
			 * Now filter out cards where day total value for the date is
			 * greater than day threshold
			 */
			List<String> fraudulantCards = transactioDtos
					.entrySet()
					.stream()
					.filter(p -> p.getKey().getTransactionTimestamp()
							.isEqual(date.toLocalDate())
							&& p.getValue() > dayThreshold)
					.map(p -> p.getKey().getCardHash())
					.collect(Collectors.toList());
			return fraudulantCards;
		}
		LOGGER.fatal("Transaction list was empty!! Aborting process....");
		return Collections.emptyList();
	}

	public Function<String, TransactionDto> tranStrToDto = tranStr -> {
		TransactionDto dto = null;
		if (null != tranStr && tranStr.indexOf(',') != -1) {
			String[] tokens = tranStr.split(",");
			if (tokens.length != 3) {
				// / log error and skip this transaction
				LOGGER.error("Invalid transaction string:: " + tranStr);
				LOGGER.error("Transation string does not have all the transarion attributes. Skipping this record!");
			} else {
				// Value checks
				double price = 0.0d;
				boolean isValidRecord = true;
				ZonedDateTime timestamp = null;
				String cardHash = null;
				try {
					cardHash = tokens[0].trim();
					isValidRecord = cardHash.length() > 0;
					if (isValidRecord) {
						try {
							timestamp = ZonedDateTime.parse(tokens[1].trim()
									+ "Z");
						} catch (Exception e) {
							LOGGER.error("Timestamp string was a invalid.... skipping this transaction");
							isValidRecord = false;
						}
						if (isValidRecord) {
							try {
								price = Double.parseDouble(tokens[2].trim());
							} catch (Exception e) {
								LOGGER.error("Price was a non-numeric value.... skipping this transaction");
								isValidRecord = false;
							}
						}
					} else {
						LOGGER.error("Card hash string was a invalid.... skipping this transaction");
					}
					if (isValidRecord) {
						dto = new TransactionDto(new TransactionKey(cardHash,
								timestamp.toLocalDate()), price);
					}
				} catch (Exception ex) {
					LOGGER.error("error in parsing the transaction string....Skipping this record");
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		} else {
			LOGGER.warn("Transaction string was empty or invalid.... Skipping this line...");
		}
		return dto;
	};

	public static void main(String[] args) {
		// Main tester method
		LOGGER.info("Starting process...");
		List<String> transactions = new ArrayList<>();

		transactions.add("abcd,2014-04, 10.00");
		transactions.add("bcde,2014-04-29T13:15:54, 80.00");
		transactions.add("defg,2014-04-29T13:15:54, er.00");
		transactions.add("cdef,2014-04-29T13:15:54, 10.00");
		transactions.add("abcd,2014-04-29T13:15:54, 10.00");
		transactions.add("bcde,2014-04-30T13:15:54, 10.00");
		transactions.add("bcde,2014-04-29T13:15:54, 10.00");
		transactions.add("defg,2014-04-29T13:15:54, 10.00");
		transactions.add("cdef,2014-04-29T13:15:54, 10.00");
		transactions.add("defg,2014-04-29T13:15:54, 40.50");
		transactions.add("uvwx,2014-04-29T13:15:54, 100.50");
		transactions.add("abcd,2014-04-23T13:15:54, 60.00");
		transactions.add("abcd,2014-04-23T13:15:54, 50.00");
		transactions.add("abcd,2014-04-29T13:15:54, 60.20");
		transactions.add("ghij,2014-04-29T13:15:54, 10.00");
		transactions.add("ghij,2014-04-29T13:15:54, 89.00");
		transactions.add("ghij,2014-04-29T13:15:54, 0.12");
		transactions.add("ghij,2014-04-29T13:15:54, 1.00");
		transactions.add("abcd,2014-04-29T13:15:54, 10.00");
		transactions.add("pqrs,2014-04-29T13:15:54, 10.00");
		transactions.add("abcd,2014-04-29T13:15:54, 10.00");
		transactions.add("pqrs,2014-04-29T13:15:54, 10.00");
		transactions.add("pqrs,2014-04-29T13:15:54, 10.00");
		transactions.add("pqrs,2014-04-29T13:15:54, 70.00");
		transactions.add("abcd,2014-04-29T13:15:54, 10.00");
		transactions.add("pqrs,2014-04-29T13:15:54, 10.00");
		transactions.add("rstu,2014-04-29T13:15:54, 10.00");
		transactions.add("bcde,2014-04-29T13:15:54, 10.00");
		transactions.add("abcd,2014-04-27T13:15:54, 50.00");

		FraudDetector fraudDetector = new FraudDetector();
		Instant instant = Instant.parse("2014-04-29T00:00:00Z");
		ZonedDateTime date = ZonedDateTime.ofInstant(instant,
				ZoneId.of("Australia/Sydney"));
		List<String> fraudulantCards = fraudDetector.getFraudulentCards(
				transactions, date, 100.00d);
		fraudulantCards.forEach(LOGGER::info);
	}
}

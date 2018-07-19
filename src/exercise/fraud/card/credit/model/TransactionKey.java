package exercise.fraud.card.credit.model;

import java.io.Serializable;
import java.time.LocalDate;

public class TransactionKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5501678410313163010L;

	private String cardHash;
	private LocalDate transactionTimestamp;

	/**
	 * @param cardHash
	 * @param transactionTimestamp
	 */
	public TransactionKey(String cardHash, LocalDate transactionTimestamp) {
		this.cardHash = cardHash;
		this.transactionTimestamp = transactionTimestamp;
	}

	/**
	 * @return the cardHash
	 */
	public String getCardHash() {
		return cardHash;
	}

	/**
	 * @param cardHash
	 *            the cardHash to set
	 */
	public void setCardHash(String cardHash) {
		this.cardHash = cardHash;
	}

	/**
	 * @return the transactionTimestamp
	 */
	public LocalDate getTransactionTimestamp() {
		return transactionTimestamp;
	}

	/**
	 * @param transactionTimestamp
	 *            the transactionTimestamp to set
	 */
	public void setTransactionTimestamp(LocalDate transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cardHash == null) ? 0 : cardHash.hashCode());
		result = prime
				* result
				+ ((transactionTimestamp == null) ? 0 : transactionTimestamp
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionKey other = (TransactionKey) obj;
		if (cardHash == null) {
			if (other.cardHash != null)
				return false;
		} else if (!cardHash.equals(other.cardHash))
			return false;
		if (transactionTimestamp == null) {
			if (other.transactionTimestamp != null)
				return false;
		} else if (!transactionTimestamp.equals(other.transactionTimestamp))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"TransactionKey [cardHash=%s, transactionTimestamp=%s]",
				cardHash, transactionTimestamp);
	}
}

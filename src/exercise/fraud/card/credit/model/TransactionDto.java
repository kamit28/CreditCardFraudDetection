package exercise.fraud.card.credit.model;

public class TransactionDto {

	TransactionKey transactionKey;

	/**
	 * @param transactionKey
	 * @param price
	 */
	public TransactionDto(TransactionKey transactionKey, double price) {
		this.transactionKey = transactionKey;
		this.price = price;
	}

	private double price;

	/**
	 * @return the transactionKey
	 */
	public TransactionKey getTransactionKey() {
		return transactionKey;
	}

	/**
	 * @param transactionKey
	 *            the transactionKey to set
	 */
	public void setTransactionKey(TransactionKey transactionKey) {
		this.transactionKey = transactionKey;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
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
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((transactionKey == null) ? 0 : transactionKey.hashCode());
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
		TransactionDto other = (TransactionDto) obj;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (transactionKey == null) {
			if (other.transactionKey != null)
				return false;
		} else if (!transactionKey.equals(other.transactionKey))
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
		return String.format("TransactionDto [transactionKey=%s, price=%s]",
				transactionKey, price);
	}
}

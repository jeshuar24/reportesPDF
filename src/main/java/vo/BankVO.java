/**
 * 
 */
package vo;

/**
 * @author jesus
 *
 */
public class BankVO {
	private Long id;
	private Long client_id;
	private String clabe;
	private String bank;
	private String arrangement;
	private String account;
	private String payment_date;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the client_id
	 */
	public Long getClient_id() {
		return client_id;
	}
	/**
	 * @param client_id the client_id to set
	 */
	public void setClient_id(Long client_id) {
		this.client_id = client_id;
	}
	/**
	 * @return the clabe
	 */
	public String getClabe() {
		return clabe;
	}
	/**
	 * @param clabe the clabe to set
	 */
	public void setClabe(String clabe) {
		this.clabe = clabe;
	}
	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}
	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}
	/**
	 * @return the arrangement
	 */
	public String getArrangement() {
		return arrangement;
	}
	/**
	 * @param arrangement the arrangement to set
	 */
	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the payment_date
	 */
	public String getPayment_date() {
		return payment_date;
	}
	/**
	 * @param payment_date the payment_date to set
	 */
	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}

}

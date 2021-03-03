/**
 * 
 */
package vo;

import java.util.Date;

/**
 * @author jesus
 *
 */
public class CreditVO {
	private Long id;
	private Long client_id;
	private Long product_id;
	private String promotor_code;
	private String promotor_name; 
	private String branch_office;
	private Double amount;
	private String disposing;
	private String city;
	private String debt;
	private Date date;
	private String destination;
	private String periodicity;
	private String question;
	private boolean status;
	
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
	 * @return the product_id
	 */
	public Long getProduct_id() {
		return product_id;
	}
	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}
	/**
	 * @return the promotor_code
	 */
	public String getPromotor_code() {
		return promotor_code;
	}
	/**
	 * @param promotor_code the promotor_code to set
	 */
	public void setPromotor_code(String promotor_code) {
		this.promotor_code = promotor_code;
	}
	/**
	 * @return the promotor_name
	 */
	public String getPromotor_name() {
		return promotor_name;
	}
	/**
	 * @param promotor_name the promotor_name to set
	 */
	public void setPromotor_name(String promotor_name) {
		this.promotor_name = promotor_name;
	}
	/**
	 * @return the branch_office
	 */
	public String getBranch_office() {
		return branch_office;
	}
	/**
	 * @param branch_office the branch_office to set
	 */
	public void setBranch_office(String branch_office) {
		this.branch_office = branch_office;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the disposing
	 */
	public String getDisposing() {
		return disposing;
	}
	/**
	 * @param disposing the disposing to set
	 */
	public void setDisposing(String disposing) {
		this.disposing = disposing;
	}
	/**
	 * @return the debt
	 */
	public String getDebt() {
		return debt;
	}
	/**
	 * @param debt the debt to set
	 */
	public void setDebt(String debt) {
		this.debt = debt;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	/**
	 * @return the periodicity
	 */
	public String getPeriodicity() {
		return periodicity;
	}
	/**
	 * @param periodicity the periodicity to set
	 */
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
}

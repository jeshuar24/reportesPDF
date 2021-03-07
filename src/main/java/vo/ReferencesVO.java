/**
 * 
 */
package vo;

/**
 * @author jesus
 *
 */
public class ReferencesVO {
	private Long id;
	private Long client_id;
	private String name;
	private String name2;
	private String first_last_name;
	private String sec_last_name;
	private String nacionality;
	private Long birth;
	private String phone;
	private String relationship;
	private String known;
	private boolean status;
	/**
	 * @return the name2
	 */
	public String getName2() {
		return name2;
	}
	/**
	 * @param name2 the name2 to set
	 */
	public void setName2(String name2) {
		this.name2 = name2;
	}
	/**
	 * @return the first_last_name
	 */
	public String getFirst_last_name() {
		return first_last_name;
	}
	/**
	 * @param first_last_name the first_last_name to set
	 */
	public void setFirst_last_name(String first_last_name) {
		this.first_last_name = first_last_name;
	}
	/**
	 * @return the sec_last_name
	 */
	public String getSec_last_name() {
		return sec_last_name;
	}
	/**
	 * @param sec_last_name the sec_last_name to set
	 */
	public void setSec_last_name(String sec_last_name) {
		this.sec_last_name = sec_last_name;
	}
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the nacionality
	 */
	public String getNacionality() {
		return nacionality;
	}
	/**
	 * @param nacionality the nacionality to set
	 */
	public void setNacionality(String nacionality) {
		this.nacionality = nacionality;
	}
	/**
	 * @return the birth
	 */
	public Long getBirth() {
		return birth;
	}
	/**
	 * @param birth the birth to set
	 */
	public void setBirth(Long birth) {
		this.birth = birth;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the relationship
	 */
	public String getRelationship() {
		return relationship;
	}
	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	/**
	 * @return the known
	 */
	public String getKnown() {
		return known;
	}
	/**
	 * @param known the known to set
	 */
	public void setKnown(String known) {
		this.known = known;
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
	@Override
	public String toString() {
		return name + " "+name2+" "+first_last_name+" "+sec_last_name;
	}
	
}

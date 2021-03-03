/**
 * 
 */
package vo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * @author jesus
 *
 */
public class ClientVO {
	private final static Logger LOGGER = Logger.getLogger(ClientVO.class.getName());
	private Long id;
	private Long user_id;
	private String name;
	private String name2;
	private String first_last_name;
	private String sec_last_name;
	private String nacionality;
	private String country;
	private String state;
	private Date birth;
	private String gender;
	private String type_housing;
	private Long living_there_y;
	private Long living_there_m;
	private String cellphone;
	private String phone;
	private String email;
	private String contact_schedule;
	private String rfc;
	private String curp;
	private String fiel;
	private String civil_status;
	
	
	/**
	 * @return the civil_status
	 */
	public String getCivil_status() {
		return civil_status;
	}
	/**
	 * @param civil_status the civil_status to set
	 */
	public void setCivil_status(String civil_status) {
		this.civil_status = civil_status;
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
	 * @return the user_id
	 */
	public Long getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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
	public Date getBirth() {
		return birth;
	}
	/**
	 * @param birth the birth to set
	 */
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	/**
	 * @return the type_housing
	 */
	public String getType_housing() {
		return type_housing;
	}
	/**
	 * @param type_housing the type_housing to set
	 */
	public void setType_housing(String type_housing) {
		this.type_housing = type_housing;
	}
	/**
	 * @return the living_there_y
	 */
	public Long getLiving_there_y() {
		return living_there_y;
	}
	/**
	 * @param living_there_y the living_there_y to set
	 */
	public void setLiving_there_y(Long living_there_y) {
		this.living_there_y = living_there_y;
	}
	/**
	 * @return the living_there_m
	 */
	public Long getLiving_there_m() {
		return living_there_m;
	}
	/**
	 * @param living_there_m the living_there_m to set
	 */
	public void setLiving_there_m(Long living_there_m) {
		this.living_there_m = living_there_m;
	}
	/**
	 * @return the cellphone
	 */
	public String getCellphone() {
		return cellphone;
	}
	/**
	 * @param cellphone the cellphone to set
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the fiel
	 */
	public String getFiel() {
		return fiel;
	}
	/**
	 * @param fiel the fiel to set
	 */
	public void setFiel(String fiel) {
		this.fiel = fiel;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the contact_schedule
	 */
	public String getContact_schedule() {
		return contact_schedule;
	}
	/**
	 * @param contact_schedule the contact_schedule to set
	 */
	public void setContact_schedule(String contact_schedule) {
		this.contact_schedule = contact_schedule;
	}
	/**
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}
	/**
	 * @param rfc the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	/**
	 * @return the curp
	 */
	public String getCurp() {
		return curp;
	}
	/**
	 * @param curp the curp to set
	 */
	public void setCurp(String curp) {
		this.curp = curp;
	}
	@Override
	public String toString() {
		return this.name+" "+this.name2+" "+this.first_last_name+" "+this.sec_last_name;
	}
	public Calendar fechaNacCalendario() {
		Calendar calendario = null;
		try{
			calendario = new GregorianCalendar();
			calendario.setTime(this.birth);
		}catch(Exception e)
		{
			LOGGER.info("Error al obtener la fecha de nacimiento "+e );
		}		
		return calendario;
	}
	
	
}

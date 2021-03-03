package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import vo.AdressVO;
import vo.BankVO;
import vo.ClientVO;
import vo.JobVO;
import vo.ProductVO;

/**
 * Servlet implementation class ReportePDF
 */
public class ReportePDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(ReportePDF.class.getName());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String resultJSON = null;
		HashMap<String, HashMap<String, String>> valorEtiquetas = null;
		ClientVO cliente = null;
		JobVO job = null;
		AdressVO direccionTrabajo = null;
		AdressVO direccionCasa = null;
		BankVO banco = null;
		ProductVO producto = null;
		try {
			// Obtiene el parametro cliente del request (sistema principal)
			String idCliente = request.getParameter("cliente");
			// Valida que al obtener el parametro traiga un valor diferente de "" o null
			if (!idCliente.equals("") || idCliente != null) {
				// Arma la url para realizar la petición concatenando el id recibido
				URL url = new URL("http://192.168.100.50:3333/getClientJSON/".concat(idCliente));
				// Realiza la peticion concatenando el id que recibe del request para obtener
				// los datos del cliente
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				// Si no se acepta la conexión muestra la excepción con el codio de error
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
				}
				// Si se acepta, obtiene el json y se desconecta
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				resultJSON = IOUtils.toString(in);
				conn.disconnect();
			}
		} catch (Exception e) {
			LOGGER.info("Error al realizar la conexión " + e);
		}
		// Valida que result sea diferente de null y obtiene los nodos del json
		try {
			if (resultJSON != null) {
				valorEtiquetas = new HashMap<String, HashMap<String, String>>();

				JSONObject jsonObject = new JSONObject(resultJSON);
				Map<String, Object> nodosJSON = jsonObject.toMap();

				ArrayList<Object> etiquetasList = null;
				for (Map.Entry<String, Object> entry : nodosJSON.entrySet()) {
					if (!entry.getKey().equals("references")) {
						valorEtiquetas.put(entry.getKey(), (HashMap<String, String>) entry.getValue());
					} else {
						etiquetasList = (ArrayList) entry.getValue();
						Integer i = 0;
						Iterator it = etiquetasList.iterator();
						while (it.hasNext()) {
							valorEtiquetas.put(entry.getKey().concat(i.toString()),
									(HashMap<String, String>) it.next());
							i = i + 1;
						}
					}
				}
			}
		}catch(Exception e) {
			LOGGER.info("Error al obtener la información del json" + e);
		}
		try {
				// Llena el objeto client
				cliente = llenaClientVO(valorEtiquetas, "client");
				// Llena el objeto adressC adressJ
				direccionCasa = llenaAdressVO(valorEtiquetas, "adressC");
				direccionTrabajo = llenaAdressVO(valorEtiquetas, "adressJ");
				// Llena el objeto job
				job = llenaJobVO(valorEtiquetas, "job");
				// Llena el objeto references
				// Llena el objeto credit
				// Llena el objeto product
				producto = llenaProductVO(valorEtiquetas, "product");
				// Llena el objeto bank
				banco = llenaBankVO(valorEtiquetas, "bank");

		} catch (Exception e) {
			LOGGER.info("Error al llenar los objetos del json a java" + e);
		}
		//Se llena el PDF con la información obtenida
		try {
			InputStream is = getClass().getResourceAsStream("/reports/AtnContract_rellenable.pdf");
			PdfReader reader = new PdfReader(is, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(reader, baos);
			AcroFields fields = stamper.getAcroFields();
			fields = llenarFieldsCliente(fields, cliente);
			fields = llenarFieldsAdressC(fields, direccionCasa);
			fields = llenarFieldsJob(fields, job);
			fields = llenarFieldsAdressJ(fields, direccionTrabajo);
			fields = llenarFieldsBank(fields, banco);
			//fields = llenarFieldsProduct(fields, producto);
			stamper.setFormFlattening(false);
			stamper.close();
			try {
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				response.addHeader("Access-Control-Allow-Origin", "true");
				OutputStream os = response.getOutputStream();
				baos.writeTo(os);
				os.flush();
				os.close();
			} catch (Exception e) {
				LOGGER.info("Error al mostrar el reporte PDF" + e);
			}
		} catch (Exception e) {
			LOGGER.info("Error al llenar el reporte PDF" + e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	/////////////////////////////////////////////////////////////////////////////////////LLENAR LOS VALORES DEL JSON
	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del cliente
	 * @param valorEtiquetas
	 * @param identificador
	 * @return ClientVO
	 */
	private ClientVO llenaClientVO(HashMap<String, HashMap<String, String>> valorEtiquetas, String identificador) {
		HashMap<String, String> valores = valorEtiquetas.get(identificador);
		ClientVO clientVO = new ClientVO();
		// clientVO.setId(Long.parseLong(valores.get("id")));
		// clientVO.setUser_id(Long.parseLong(valores.get("user_id")));
		clientVO.setBirth(convertirFecha(valores.get("birth")));
		clientVO.setCellphone(valores.get("cellphone"));
		clientVO.setContact_schedule(valores.get("contact_schedule"));
		clientVO.setCurp(valores.get("curp"));
		clientVO.setEmail(valores.get("email"));
		clientVO.setFirst_last_name(valores.get("first_last_name"));
		clientVO.setLiving_there_m(Long.parseLong(valores.get("living_there_m")));
		clientVO.setLiving_there_y(Long.parseLong(valores.get("living_there_y")));
		clientVO.setNacionality(valores.get("nacionality"));
		clientVO.setName(valores.get("name"));
		clientVO.setName2(valores.get("name2"));
		clientVO.setPhone(valores.get("phone"));
		clientVO.setRfc(valores.get("rfc"));
		clientVO.setSec_last_name(valores.get("sec_last_name"));
		clientVO.setType_housing(valores.get("type_housing"));
		clientVO.setCivil_status(valores.get("civil_status"));
		clientVO.setGender(valores.get("gender"));
		clientVO.setCountry(valores.get("country"));
		clientVO.setState(valores.get("state"));
		clientVO.setFiel(valores.get("fiel"));
		return clientVO;
	}
	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del job
	 * @param valorEtiquetas
	 * @param identificador
	 * @return JobVO
	 */
	private JobVO llenaJobVO(HashMap<String, HashMap<String, String>> valorEtiquetas, String identificador) {
		HashMap<String, String> valores = valorEtiquetas.get(identificador);
		JobVO jobVO = new JobVO();
		jobVO.setDependence(valores.get("dependence"));
		jobVO.setPlace(valores.get("place"));
		jobVO.setOccupation(valores.get("occupation"));
		jobVO.setJob(valores.get("job"));
        jobVO.setTime_working_y(valores.get("time_working_y"));
        jobVO.setTime_working_m(valores.get("time_working_m"));
        jobVO.setType(valores.get("type"));
        jobVO.setPhone(valores.get("phone"));
        jobVO.setExtension(valores.get("extension"));
        jobVO.setPayroll(valores.get("payroll"));
        jobVO.setIncome(Double.parseDouble(valores.get("income")));
		return jobVO;
	}
	
	/**
	 *Método para llenar el VO de acuerdo al nombre del objeto de la dirección
	 * @param valorEtiquetas
	 * @param identificador (adressC, adressJ)
	 * @return
	 */
	private AdressVO llenaAdressVO(HashMap<String, HashMap<String, String>> valorEtiquetas, String identificador) {
		HashMap<String, String> valores = valorEtiquetas.get(identificador);
		AdressVO adressVO = new AdressVO();
		adressVO.setStreet(valores.get("street"));
		adressVO.setNumber(valores.get("number"));
		adressVO.setInt_number(valores.get("int_number"));
		adressVO.setSuburb(valores.get("suburb"));
		adressVO.setCrosses(valores.get("crosses"));
		adressVO.setState(valores.get("state"));
		adressVO.setTown(valores.get("town"));
		adressVO.setContry(valores.get("contry"));
		adressVO.setPostal_code(Long.parseLong(valores.get("postal_code")));
		return adressVO;
	}
	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del job
	 * @param valorEtiquetas
	 * @param identificador
	 * @return BankVO
	 */
	private BankVO llenaBankVO(HashMap<String, HashMap<String, String>> valorEtiquetas, String identificador) {
		HashMap<String, String> valores = valorEtiquetas.get(identificador);
		BankVO bankVO = new BankVO();
		bankVO.setAccount(valores.get("account"));
		bankVO.setBank(valores.get("bank"));
		bankVO.setClabe(valores.get("clabe"));
		return bankVO;
	}
	private ProductVO llenaProductVO(HashMap<String, HashMap<String, String>> valorEtiquetas, String identificador) {
		HashMap<String, String> valores = valorEtiquetas.get(identificador);
		ProductVO productVO = new ProductVO();
		productVO.setPromotion(valores.get("promotion"));
		productVO.setTerm(valores.get("term"));
		productVO.setTasa(valores.get("tasa"));
		productVO.setFactor(valores.get("factor"));
		
		return productVO;
	}
	
	/////////////////////////////////////////////////////////////////////// LLENAR LOS FIELDS DEL REPORTE
	private AcroFields llenarFieldsCliente(AcroFields fields, ClientVO clientVO) throws IOException, DocumentException {

		fields.setField("nombre", clientVO.getName());
		fields.setField("nombre2", clientVO.getName2());
		//FECHA NACIMIENTO DIVIDIDA EN 3 AÑO, MES Y DÍA
		if(clientVO.fechaNacCalendario()!=null) {
			fields.setField("fecha_a",String.valueOf(clientVO.fechaNacCalendario().get(Calendar.YEAR)));
			fields.setField("fecha_m",String.valueOf(clientVO.fechaNacCalendario().get(Calendar.MONTH)+1));
			fields.setField("fecha_d",String.valueOf(clientVO.fechaNacCalendario().get(Calendar.DAY_OF_MONTH)));
		}
		fields.setField("apellido1", clientVO.getFirst_last_name());
		fields.setField("apellido2", clientVO.getSec_last_name());
		fields.setField("estado_civil", clientVO.getCivil_status());
		fields.setField("nacionalidad", clientVO.getNacionality());
		fields.setField("pais", clientVO.getCountry());
		fields.setField("estado", clientVO.getState());
		fields.setField("genero", clientVO.getFiel());
		fields.setField("tipo_vivienda", clientVO.getType_housing());
		if(clientVO.getLiving_there_y() != null) {
			fields.setField("cliente_arraigo_a", String.valueOf(clientVO.getLiving_there_y()));
		}
		if(clientVO.getLiving_there_m() != null) {
			fields.setField("cliente_arraigo_m", String.valueOf(clientVO.getLiving_there_m()));
		}
		fields.setField("celular", clientVO.getCellphone());
		fields.setField("telefono_casa", clientVO.getPhone());
		fields.setField("correo_electronico", clientVO.getEmail());
		fields.setField("horario_contacto", clientVO.getContact_schedule());
		fields.setField("rfc", clientVO.getRfc());
		fields.setField("curp", clientVO.getCurp());
		if(clientVO.getFiel() != null) {
			fields.setField("fiel", clientVO.getFiel());
		}
		fields.setField("cliente_nombre_completo", clientVO.toString());
		return fields;
	}
	private AcroFields llenarFieldsJob(AcroFields fields, JobVO jobVO) throws IOException, DocumentException {
		fields.setField("dependencia", jobVO.getDependence());
		fields.setField("centro_trabajo", jobVO.getPlace());
		fields.setField("ocupacion", jobVO.getOccupation());
		fields.setField("puesto", jobVO.getJob());
		fields.setField("antiguedad_a", jobVO.getTime_working_y());
		fields.setField("antiguedad_m", jobVO.getTime_working_m());
		if(jobVO.getPhone() != null) {
			fields.setField("telefono_empleo", jobVO.getPhone());
		}
		if(jobVO.getExtension() != null) {
			fields.setField("extension", jobVO.getExtension());
		}
		fields.setField("nomina", jobVO.getPayroll());
		fields.setField("ingreso_mensual", String.valueOf(jobVO.getIncome()));
		if(jobVO.getType().equals("B")) {
			fields.setField("empleo_b", "X");
		}
		if(jobVO.getType().equals("E")) {
			fields.setField("empleo_e", "X");
		}
		if(jobVO.getType().equals("J")) {
			fields.setField("empleo_J", "X");
		}
		if(jobVO.getType().equals("C")) {
			fields.setField("empleo_C", "X");
		}
		
		return fields;
	}
	private AcroFields llenarFieldsAdressC(AcroFields fields, AdressVO adressVO) throws IOException, DocumentException {
		fields.setField("cliente_calle", adressVO.getStreet());
		fields.setField("cliente_exterior", adressVO.getNumber());
		if(adressVO.getInt_number() != null) {
			fields.setField("cliente_interior", adressVO.getInt_number());
		}
		fields.setField("cliente_colonia", adressVO.getSuburb());
		fields.setField("cliente_cruzamientos", adressVO.getCrosses());
		fields.setField("cliente_estado", adressVO.getState());
		fields.setField("cliente_municipio", adressVO.getTown());
		fields.setField("cliente_pais", adressVO.getContry());
		fields.setField("cliente_codigo_postal", String.valueOf(adressVO.getPostal_code()));
		return fields;
	}
	private AcroFields llenarFieldsAdressJ(AcroFields fields, AdressVO adressVO) throws IOException, DocumentException {
		fields.setField("empleo_calle", adressVO.getStreet());
		fields.setField("empleo_exterior", adressVO.getNumber());
		if(adressVO.getInt_number() != null) {
			fields.setField("cliente_interior", adressVO.getInt_number());
		}
		fields.setField("empleo_colonia", adressVO.getSuburb());
		fields.setField("empleo_cruzamientos", adressVO.getCrosses());
		fields.setField("empleo_estado", adressVO.getState());
		fields.setField("empleo_ciudad", adressVO.getTown());
		fields.setField("empleo_pais", adressVO.getContry());
		fields.setField("empleo_codigo_postal", String.valueOf(adressVO.getPostal_code()));
		return fields;
	}
	private AcroFields llenarFieldsBank(AcroFields fields, BankVO bankVO) throws IOException, DocumentException {
		String cadena = bankVO.getClabe();
		char[] c = cadena.toCharArray();
		fields.setField("clabe1", String.valueOf(c[0]));
		fields.setField("clabe2", String.valueOf(c[1]));
		fields.setField("clabe3", String.valueOf(c[2]));
		fields.setField("clabe4", String.valueOf(c[3]));
		fields.setField("clabe5", String.valueOf(c[4]));
		fields.setField("clabe6", String.valueOf(c[5]));
		fields.setField("clabe7", String.valueOf(c[6]));
		fields.setField("clabe8", String.valueOf(c[7]));
		fields.setField("clabe9", String.valueOf(c[8]));
		fields.setField("clabe10", String.valueOf(c[9]));
		fields.setField("clabe11", String.valueOf(c[10]));
		fields.setField("clabe12", String.valueOf(c[11]));
		fields.setField("clabe13", String.valueOf(c[12]));
		fields.setField("clabe14", String.valueOf(c[13]));
		fields.setField("clabe15", String.valueOf(c[14]));
		fields.setField("clabe16", String.valueOf(c[15]));
		fields.setField("clabe17", String.valueOf(c[16]));
		fields.setField("clabe18", String.valueOf(c[17]));
		return fields;
	}
	private AcroFields llenarFieldsProduct(AcroFields fields, ProductVO productVO) throws IOException, DocumentException {
		
		return fields;
	}
	
	private Date convertirFecha(String fecha) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(fecha);
		} catch (Exception e) {
			LOGGER.info("Error al convertir fechas " + e);
		}
		return date;
	}
}

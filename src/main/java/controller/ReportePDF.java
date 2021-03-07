 package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import vo.CreditVO;
import vo.JobVO;
import vo.ProductVO;
import vo.ReferencesVO;

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
		HashMap<String, HashMap<String, Object>> valorEtiquetas = null;
		ClientVO cliente = null;
		JobVO job = null;
		AdressVO direccionTrabajo = null;
		AdressVO direccionCasa = null;
		BankVO banco = null;
		ProductVO producto = null;
		CreditVO credito = null;
		// Lista para saber el nombre de referencias, guardara reference0, reference1,
		// .. reference(n)
		ArrayList<String> referenciasNombre = new ArrayList<String>();
		// Lista de VO de referencias, para que en un ciclo posterior re itere para
		// ingresar en la seccion correspondiente del pdf
		ArrayList<ReferencesVO> referenciasValor = new ArrayList<ReferencesVO>();
		ReferencesVO referencia = null;

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
				valorEtiquetas = new HashMap<String, HashMap<String, Object>>();

				JSONObject jsonObject = new JSONObject(resultJSON);
				Map<String, Object> nodosJSON = jsonObject.toMap();

				ArrayList<Object> etiquetasList = null;
				for (Map.Entry<String, Object> entry : nodosJSON.entrySet()) {
					if (!entry.getKey().equals("references")) {
						valorEtiquetas.put(entry.getKey(), (HashMap<String, Object>) entry.getValue());
					} else {
						etiquetasList = (ArrayList) entry.getValue();
						Integer i = 0;
						Iterator it = etiquetasList.iterator();
						while (it.hasNext()) {
							String claveValor = entry.getKey().concat(i.toString());
							referenciasNombre.add(claveValor);
							valorEtiquetas.put(claveValor, (HashMap<String, Object>) it.next());
							i = i + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error al obtener la información del json" + e);
		}
		try {
			// Llena el objeto client
			cliente = llenaClientVO(valorEtiquetas, "client");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de cliente" + e);
		}
		try {
			// Llena el objeto adressC adressJ
			direccionCasa = llenaAdressVO(valorEtiquetas, "adressC");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de direccion casa" + e);
		}
		try {
			direccionTrabajo = llenaAdressVO(valorEtiquetas, "adressJ");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de direccion trabajo" + e);
		}
		try {
			// Llena el objeto job
			job = llenaJobVO(valorEtiquetas, "job");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de trabajo" + e);
		}
		try {
			// Llena los objetos references
			if (referenciasNombre.size() > 0) {
				for (String clave : referenciasNombre) {
					referencia = llenaReferencesVO(valorEtiquetas, clave);
					referenciasValor.add(referencia);
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de referencias" + e);
		}
		try {
			// Llena el objeto credit
			credito = llenaCreditVO(valorEtiquetas, "credit");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de credito" + e);
		}
		try {
			// Llena el objeto product
			producto = llenaProductVO(valorEtiquetas, "product");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de producto" + e);
		}
		try {
			// Llena el objeto bank
			banco = llenaBankVO(valorEtiquetas, "bank");
		} catch (Exception e) {
			LOGGER.info("Error al llenar la informacion de banco" + e);
		}
		// Se llena el PDF con la información obtenida
		try {
			InputStream is = getClass().getResourceAsStream("/reports/AtnContract_rellenable.pdf");
			PdfReader reader = new PdfReader(is, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(reader, baos);
			AcroFields fields = stamper.getAcroFields();
			if (cliente != null) {
				fields = llenarFieldsCliente(fields, cliente);
			}
			if (direccionCasa != null) {
				fields = llenarFieldsAdressC(fields, direccionCasa);
			}
			if (direccionTrabajo != null) {
				fields = llenarFieldsAdressJ(fields, direccionTrabajo);
			}
			if (job != null) {
				fields = llenarFieldsJob(fields, job);
			}
			if (banco != null) {
				fields = llenarFieldsBank(fields, banco);
			}
			if (referenciasValor.size() > 0) {
				// Metodo para llenar conyuge y referencias activas
				Long numReferencia = 1L;
				for (ReferencesVO referencias : referenciasValor) {
					if (referencias.isStatus()) {
						if (referencias.getRelationship().equals("CONYUGE")) {
							fields = llenarFieldsConyuge(fields, referencias);
						} else {
							if (numReferencia <= 2) {// Solo son 2 referencias
								fields = llenarFieldsReferencias(fields, referencias, numReferencia);
								numReferencia = numReferencia + 1;
							}
						}
					}
				}
			}
			if (credito != null) {
				fields = llenarFieldsCredit(fields, credito);
			}
			if (producto != null) {
				fields = llenarFieldsProduct(fields, producto, credito);
			}
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

	///////////////////////////////////////////////////////////////////////////////////// LLENAR
	///////////////////////////////////////////////////////////////////////////////////// LOS
	///////////////////////////////////////////////////////////////////////////////////// VALORES
	///////////////////////////////////////////////////////////////////////////////////// DEL
	///////////////////////////////////////////////////////////////////////////////////// JSON
	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del cliente
	 * 
	 * @param valorEtiquetas
	 * @param identificador
	 * @return ClientVO
	 */
	private ClientVO llenaClientVO(HashMap<String, HashMap<String, Object>> valorEtiquetas, String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		ClientVO clientVO = new ClientVO();
		// clientVO.setId(Long.parseLong(valores.get("id")));
		// clientVO.setUser_id(Long.parseLong(valores.get("user_id")));
		clientVO.setBirth(convertirFecha(String.valueOf(valores.get("birth"))));
		clientVO.setCellphone(String.valueOf(valores.get("cellphone")));
		clientVO.setContact_schedule(String.valueOf(valores.get("contact_schedule")));
		clientVO.setCurp(String.valueOf(valores.get("curp")));
		clientVO.setEmail(String.valueOf(valores.get("email")));
		clientVO.setFirst_last_name(String.valueOf(valores.get("first_last_name")));
		clientVO.setLiving_there_m(Long.valueOf(String.valueOf((valores.get("living_there_m")))));
		clientVO.setLiving_there_y(Long.valueOf(String.valueOf((valores.get("living_there_y")))));
		clientVO.setNacionality(String.valueOf(valores.get("nacionality")));
		clientVO.setName(String.valueOf(valores.get("name")));
		clientVO.setName2(String.valueOf(valores.get("name2")));
		clientVO.setPhone(String.valueOf(valores.get("phone")));
		clientVO.setRfc(String.valueOf(valores.get("rfc")));
		clientVO.setSec_last_name(String.valueOf(valores.get("sec_last_name")));
		clientVO.setType_housing(String.valueOf(valores.get("type_housing")));
		clientVO.setCivil_status(String.valueOf(valores.get("civil_status")));
		clientVO.setGender(String.valueOf(valores.get("gender")));
		clientVO.setCountry(String.valueOf(valores.get("country")));
		clientVO.setState(String.valueOf(valores.get("state")));
		clientVO.setFiel(String.valueOf(valores.get("fiel")));
		return clientVO;
	}

	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del job
	 * 
	 * @param valorEtiquetas
	 * @param identificador
	 * @return JobVO
	 */
	private JobVO llenaJobVO(HashMap<String, HashMap<String, Object>> valorEtiquetas, String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		JobVO jobVO = new JobVO();
		jobVO.setDependence(String.valueOf(valores.get("dependence")));
		jobVO.setPlace(String.valueOf(valores.get("place")));
		jobVO.setOccupation(String.valueOf(valores.get("occupation")));
		jobVO.setJob(String.valueOf(valores.get("job")));
		jobVO.setTime_working_y(String.valueOf(valores.get("time_working_y")));
		jobVO.setTime_working_m(String.valueOf(valores.get("time_working_m")));
		jobVO.setType(String.valueOf(valores.get("type")));
		jobVO.setPhone(String.valueOf(valores.get("phone")));
		jobVO.setExtension(String.valueOf(valores.get("extension")));
		jobVO.setPayroll(String.valueOf(valores.get("payroll")));
		jobVO.setIncome(Double.parseDouble(String.valueOf(valores.get("income"))));
		return jobVO;
	}

	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto de la dirección
	 * 
	 * @param valorEtiquetas
	 * @param identificador  (adressC, adressJ)
	 * @return
	 */
	private AdressVO llenaAdressVO(HashMap<String, HashMap<String, Object>> valorEtiquetas, String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		AdressVO adressVO = new AdressVO();
		adressVO.setStreet(String.valueOf(valores.get("street")));
		adressVO.setNumber(String.valueOf(valores.get("number")));
		adressVO.setInt_number(String.valueOf(valores.get("int_number")));
		adressVO.setSuburb(String.valueOf(valores.get("suburb")));
		adressVO.setCrosses(String.valueOf(valores.get("crosses")));
		adressVO.setState(String.valueOf(valores.get("state")));
		adressVO.setTown(String.valueOf(valores.get("town")));
		adressVO.setContry(String.valueOf(valores.get("contry")));
		adressVO.setPostal_code(Long.parseLong(String.valueOf(valores.get("postal_code"))));
		return adressVO;
	}

	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del job
	 * 
	 * @param valorEtiquetas
	 * @param identificador
	 * @return BankVO
	 */
	private BankVO llenaBankVO(HashMap<String, HashMap<String, Object>> valorEtiquetas, String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		BankVO bankVO = new BankVO();
		bankVO.setAccount(String.valueOf(valores.get("account")));
		bankVO.setBank(String.valueOf(valores.get("bank")));
		bankVO.setClabe(String.valueOf(valores.get("clabe")));
		return bankVO;
	}
	
	private CreditVO llenaCreditVO(HashMap<String, HashMap<String, Object>> valorEtiquetas, String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		CreditVO creditVO = new CreditVO();
		creditVO.setAmount(Double.parseDouble((String) valores.get("amount")));
		creditVO.setBranch_office(String.valueOf(valores.get("branch_office")));
		creditVO.setCity(String.valueOf(valores.get("city")));
		creditVO.setDate(convertirFecha(String.valueOf(valores.get("date"))));
		creditVO.setDebt(String.valueOf(valores.get("debt")));
		creditVO.setDestination(String.valueOf(valores.get("destination")));
		creditVO.setDisposing(String.valueOf(valores.get("disposing")));
		creditVO.setPeriodicity(String.valueOf(valores.get("periodicity")));
		creditVO.setPromotor_code(String.valueOf(valores.get("promotor_code")));
		creditVO.setPromotor_name(String.valueOf(valores.get("promotor_name")));
		creditVO.setQuestion(String.valueOf(valores.get("question")));
		creditVO.setStatus((String.valueOf(valores.get("status")).equals("1")) ? true : false);
		return creditVO;
	}

	private ProductVO llenaProductVO(HashMap<String, HashMap<String, Object>> valorEtiquetas, String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		ProductVO productVO = new ProductVO();
		productVO.setPromotion(String.valueOf(valores.get("promotion")));
		productVO.setTerm(String.valueOf(valores.get("term")));
		productVO.setTasa(String.valueOf(valores.get("tasa")));
		productVO.setFactor(String.valueOf(valores.get("factor")));
		productVO.setCat(String.valueOf(valores.get("cat")));

		return productVO;
	}

	private ReferencesVO llenaReferencesVO(HashMap<String, HashMap<String, Object>> valorEtiquetas,
			String identificador) {
		HashMap<String, Object> valores = valorEtiquetas.get(identificador);
		ReferencesVO referencias = new ReferencesVO();
		referencias.setBirth(Long.parseLong(String.valueOf(valores.get("birth"))));
		// referencias.setClient_id(Long.parseLong(valores.get("client_id")));
		referencias.setKnown(String.valueOf(valores.get("know")));
		referencias.setNacionality(String.valueOf(valores.get("nacionality")));
		referencias.setName(String.valueOf(valores.get("name")));
		referencias.setName2(String.valueOf(valores.get("name2")));
		referencias.setFirst_last_name(String.valueOf(valores.get("first_last_name")));
		referencias.setSec_last_name(String.valueOf(valores.get("sec_last_name")));
		referencias.setPhone(String.valueOf(valores.get("phone")));
		referencias.setRelationship(String.valueOf(valores.get("relationship")));
		referencias.setStatus((String.valueOf(valores.get("status")).equals("1")) ? true : false);
		return referencias;
	}

	/////////////////////////////////////////////////////////////////////// LLENAR
	/////////////////////////////////////////////////////////////////////// LOS
	/////////////////////////////////////////////////////////////////////// FIELDS
	/////////////////////////////////////////////////////////////////////// DEL
	/////////////////////////////////////////////////////////////////////// REPORTE
	private AcroFields llenarFieldsCliente(AcroFields fields, ClientVO clientVO) throws IOException, DocumentException {

		fields.setField("nombre", clientVO.getName());
		fields.setField("nombre2", clientVO.getName2());
		// FECHA NACIMIENTO DIVIDIDA EN 3 AÑO, MES Y DÍA
		if (clientVO.fechaNacCalendario() != null) {
			fields.setField("fecha_a", String.valueOf(clientVO.fechaNacCalendario().get(Calendar.YEAR)));
			fields.setField("fecha_m", String.valueOf(clientVO.fechaNacCalendario().get(Calendar.MONTH) + 1));
			fields.setField("fecha_d", String.valueOf(clientVO.fechaNacCalendario().get(Calendar.DAY_OF_MONTH)));
		}
		fields.setField("apellido1", clientVO.getFirst_last_name());
		fields.setField("apellido2", clientVO.getSec_last_name());
		fields.setField("estado_civil", clientVO.getCivil_status());
		fields.setField("nacionalidad", clientVO.getNacionality());
		fields.setField("pais", clientVO.getCountry());
		fields.setField("estado", clientVO.getState());
		fields.setField("genero", clientVO.getFiel());
		fields.setField("tipo_vivienda", clientVO.getType_housing());
		if (clientVO.getLiving_there_y() != null) {
			fields.setField("cliente_arraigo_a", String.valueOf(clientVO.getLiving_there_y()));
		}
		if (clientVO.getLiving_there_m() != null) {
			fields.setField("cliente_arraigo_m", String.valueOf(clientVO.getLiving_there_m()));
		}
		fields.setField("celular", clientVO.getCellphone());
		fields.setField("telefono_casa", clientVO.getPhone());
		fields.setField("correo_electronico", clientVO.getEmail());
		fields.setField("horario_contacto", clientVO.getContact_schedule());
		fields.setField("rfc", clientVO.getRfc());
		fields.setField("curp", clientVO.getCurp());
		if (clientVO.getFiel() != null) {
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
		if (jobVO.getPhone() != null) {
			fields.setField("telefono_empleo", jobVO.getPhone());
		}
		if (jobVO.getExtension() != null) {
			fields.setField("extension", jobVO.getExtension());
		}
		fields.setField("nomina", jobVO.getPayroll());
		fields.setField("ingreso_mensual", String.valueOf(jobVO.getIncome()));
		if (jobVO.getType().equals("B")) {
			fields.setField("empleo_b", "X");
		}
		if (jobVO.getType().equals("E")) {
			fields.setField("empleo_e", "X");
		}
		if (jobVO.getType().equals("J")) {
			fields.setField("empleo_J", "X");
		}
		if (jobVO.getType().equals("C")) {
			fields.setField("empleo_C", "X");
		}

		return fields;
	}

	private AcroFields llenarFieldsAdressC(AcroFields fields, AdressVO adressVO) throws IOException, DocumentException {
		fields.setField("cliente_calle", adressVO.getStreet());
		fields.setField("cliente_exterior", adressVO.getNumber());
		if (adressVO.getInt_number() != null) {
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
		if (adressVO.getInt_number() != null) {
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
		for(int i = 0; i < c.length; i++ ) {
			fields.setField("clabe" + (i+1), String.valueOf(c[i]));
		}
		
		return fields;
	}

	private AcroFields llenarFieldsProduct(AcroFields fields, ProductVO productVO, CreditVO creditVO)
			throws IOException, DocumentException {
		String str = productVO.getCat();
		String[] arrOfStr = str.split("\\."); 
		Double montoT, parcialides, iva, capital, interes, div = (double) 0;
		fields.setField("plazo", productVO.getTerm());
		fields.setField("cat", arrOfStr[0]);
		fields.setField("cat_decimal", arrOfStr[1]);
		capital = creditVO.getAmount();
		interes = ((Double.parseDouble(productVO.getFactor())/100)/2)*(capital)*(Double.parseDouble(productVO.getTerm()));
		iva = interes * 0.16;
		montoT = capital + interes + iva;
		montoT = round(montoT, 2);
		/*BigDecimal monto = new BigDecimal(montoT);
		monto.setScale(2, RoundingMode.HALF_UP);*/
		fields.setField("monto_total", String.valueOf(montoT));
		div = capital/Double.parseDouble(productVO.getTerm());
		interes = capital*((Double.parseDouble(productVO.getFactor())/100)/2);
		iva = interes * 0.16;
		parcialides =  div + interes + iva;
		parcialides = round(parcialides, 2);
		fields.setField("parcialidades", String.valueOf(parcialides));
		
		//
		
		//

		return fields;
	}
	
	private AcroFields llenarFieldsCredit(AcroFields fields, CreditVO creditVO)
			throws IOException, DocumentException {
		String cadenaSucursal = creditVO.getBranch_office();
		String cadenaCP = creditVO.getPromotor_code();
		char[] c = cadenaSucursal.toCharArray();
		char[] co = cadenaCP.toCharArray();
		
		fields.setField("nombre_promotor", creditVO.getPromotor_name());
		for(int i = 0; i < c.length; i++ ) {
			fields.setField("sucursal" + (i+1), String.valueOf(c[i]));
		}
		for(int i = 0; i < co.length; i++ ) {
			fields.setField("codigo_promotor" + (i+1), String.valueOf(co[i]));
		}
		fields.setField("pregunta_atn", creditVO.getQuestion());
		fields.setField("monto_maximo",String.valueOf( creditVO.getAmount()));
		fields.setField("monto",String.valueOf( creditVO.getAmount()));
		if(creditVO.getDisposing().equals("Orden de pago")) {
			fields.setField("disposicion_o", "X");
		}
		if(creditVO.getDisposing().equals("Transferencia")) {
			fields.setField("disposicion_t", "X");
		}
		if(creditVO.getDisposing().equals("Cheque")) {
			fields.setField("disposicion_c", "X");
		}
		fields.setField("destino", creditVO.getDestination());
		fields.setField("destino_descripcion", creditVO.getDebt());
		fields.setField("periodicidad", creditVO.getPeriodicity());
		fields.setField("ciudadyestado_fecha", creditVO.getCity());
		//fields.setField("", creditVO.getDate());
		
		return fields;
	}

	private AcroFields llenarFieldsConyuge(AcroFields fields, ReferencesVO referencesVO)
			throws IOException, DocumentException {
		fields.setField("conyuge_nombre", referencesVO.getName());
		fields.setField("conyuge_nombre2", referencesVO.getName2());
		fields.setField("conyuge_apellido1", referencesVO.getFirst_last_name());
		fields.setField("conyuge_apellido2", referencesVO.getSec_last_name());
		fields.setField("conyuge_nacionalidad", referencesVO.getNacionality());
		return fields;
	}

	private AcroFields llenarFieldsReferencias(AcroFields fields, ReferencesVO referencesVO, Long numReferencia)
			throws IOException, DocumentException {
		fields.setField("nombre_ref_".concat(numReferencia.toString()), (referencesVO.getName()+ " " + referencesVO.getName2() + " " + referencesVO.getFirst_last_name() + " " + referencesVO.getSec_last_name()));
		fields.setField("telefono_ref_".concat(numReferencia.toString()), referencesVO.getPhone());
		fields.setField("relacion_ref_".concat(numReferencia.toString()), referencesVO.getRelationship());
		fields.setField("tiempo_conocerse_ref_".concat(numReferencia.toString()),
				referencesVO.getBirth().toString().concat(" años"));
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
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
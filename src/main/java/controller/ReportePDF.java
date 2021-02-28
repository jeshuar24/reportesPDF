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
import vo.ClientVO;

/**
 * Servlet implementation class ReportePDF
 */
public class ReportePDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(ReportePDF.class.getName());
	public static HashMap<String, String> etiquetas;
	static {
		etiquetas = new HashMap<String, String>();
		etiquetas.put("client.name", "nombre");
		etiquetas.put("client.first_last_name", "apellido1");
		etiquetas.put("client.sec_last_name", "apellido2");
	}
	public static ArrayList<String> listaObjetos;
	static {
		listaObjetos = new ArrayList<String>();
		listaObjetos.add("client");
		listaObjetos.add("adressC");
		listaObjetos.add("job");
		listaObjetos.add("adressJ");
		listaObjetos.add("references");
		listaObjetos.add("product");
		listaObjetos.add("bank");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String resultJSON = null;
		HashMap<String, HashMap<String, String>> valorEtiquetas = null;
		ClientVO cliente = null;
		AdressVO direccionTrabajo = null;
		AdressVO direccionCasa = null;
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
					if (!entry.getKey().equals("product") && !entry.getKey().equals("references")) {
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
				// Llena el objeto adressC
				direccionTrabajo = llenaAdressVO(valorEtiquetas, "addresC");
				direccionCasa = llenaAdressVO(valorEtiquetas, "addresJ");
				// Llena el objeto job
				// Llena el objeto references
				// Llena el objeto credit
				// Llena el objeto product
				// Llena el objeto bank

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
				System.out.println("escritura");
				baos.writeTo(os);
				os.flush();
				System.out.println("confirmar");
				os.close();
			} catch (Exception de) {
				System.out.println("error escritura");
				System.out.println(de);
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
	/**
	 * Método para llenar el VO de acuerdo al nombre del objeto del cliente
	 * @param valorEtiquetas
	 * @param identificador
	 * @return ClientVO
	 */
	private ClientVO llenaClientVO(HashMap<String, HashMap<String, String>> valorEtiquetas, String identificador) {
		HashMap<String, String> valores = valorEtiquetas.get(identificador);
		ClientVO clientVO = new ClientVO();
		clientVO.setBirth(convertirFecha(valores.get("birth")));
		clientVO.setCellphone(valores.get("cellphone"));
		clientVO.setContact_schedule(valores.get("contact_schedule"));
		clientVO.setCurp(valores.get("curp"));
		clientVO.setEmail(valores.get("email"));
		clientVO.setFirst_last_name(valores.get("first_last_name"));
		// clientVO.setId(Long.parseLong(valores.get("id")));
		clientVO.setLiving_there_m(Long.parseLong(valores.get("living_there_m")));
		clientVO.setLiving_there_y(Long.parseLong(valores.get("living_there_y")));
		clientVO.setNacionality(valores.get("nacionality"));
		clientVO.setName(valores.get("name"));
		clientVO.setName2(valores.get("name2"));
		clientVO.setPhone(valores.get("phone"));
		clientVO.setRfc(valores.get("rfc"));
		clientVO.setSec_last_name(valores.get("sec_last_name"));
		clientVO.setType_housing(valores.get("type_housing"));
		// clientVO.setUser_id(Long.parseLong(valores.get("user_id")));
		clientVO.setCivil_status(valores.get("civil_status"));
		return clientVO;
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
		return adressVO;
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
		fields.setField("tipo_vivienda", clientVO.getType_housing());
		if(clientVO.getLiving_there_y() != null) {
			fields.setField("cliente_arraigo_a", String.valueOf(clientVO.getLiving_there_y()));
		}
		if(clientVO.getLiving_there_m() != null) {
			fields.setField("cliente_arraigo_m", String.valueOf(clientVO.getLiving_there_m()));
		}
		// pendiente living_there
		fields.setField("celular", clientVO.getCellphone());
		fields.setField("telefono_casa", clientVO.getPhone());
		fields.setField("correo_electronico", clientVO.getEmail());
		fields.setField("horario_contacto", clientVO.getContact_schedule());
		fields.setField("rfc", clientVO.getRfc());
		fields.setField("curp", clientVO.getCurp());
		fields.setField("cliente_nombre_completo", clientVO.toString());
		return fields;
	}
}

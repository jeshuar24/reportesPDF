package controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class reportePDF
 */
public class reportePDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static HashMap<String, String> etiquetas;
	static {
		etiquetas= new HashMap<String, String>();
		etiquetas.put("name", "nombre");
		etiquetas.put("product", "producto");
	}
	
	

	/**
	 * Default constructor.
	 */
	public reportePDF() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		System.out.println("INICIO");
		//Revisar que llegue el objeto json con client
	try {
		
		String jsonStr = getBody(request);
		System.out.println(jsonStr);
		/*JSONObject jsonObject = new JSONObject(jsonStr);
		Map<String, Object> map = jsonObject.toMap();
		HashMap<String, HashMap<String, String>> valorEtiquetas = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> etiquetasMap = null;
		ArrayList etiquetasList = null;
		
			if(!entry.getKey().equals("product") && !entry.getKey().equals("references")) {
				valorEtiquetas.put(entry.getKey(), (HashMap<String, String>) entry.getValue());
			}else {
				
				etiquetasList = (ArrayList) entry.getValue();
				Integer i = 0;
				Iterator it = etiquetasList.iterator();
				while(it.hasNext()) {
					valorEtiquetas.put(entry.getKey().concat(i.toString()), (HashMap<String, String>) it.next());
					i = i+1;;
				}
			}
		}*/
		InputStream is = getClass().getResourceAsStream("/reports/AtnContract.pdf");
		PdfReader reader = new PdfReader(is, null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, baos);
		AcroFields fields = stamper.getAcroFields();
		fields.setField("nombre1", "JESUS ANTONIO MOHEDANO ORTEGA");

		stamper.setFormFlattening(true);
		stamper.close();
		try {
			response.setHeader("Expires", "0");
			 response.setHeader("Cache-Control",
			 "must-revalidate, post-check=0, pre-check=0");
			 response.setHeader("Pragma", "public");
			 response.setContentType(
					 "application/vnd.adobe.pdf");
					 response.setHeader("Content-Disposition",
					 "attachment; filename=\"contrato.pdf\"");
			 response.setContentLength(baos.size());
			 response.addHeader("Access-Control-Allow-Origin", "true");
			 OutputStream os = response.getOutputStream();
			 System.out.println("escritura");
			 baos.writeTo(os);
			 os.flush();
			 System.out.println("confirmar");
			 os.close(); 
		}catch(Exception de) {
			System.out.println("error escritura");
			System.out.println(de);
		}
	} catch (Exception de) {
		System.out.println(de);
		//throw new IOException(de.getMessage());
	}
	System.out.println("FIN");
	
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		System.out.println("INICIO");
		//Revisar que llegue el objeto json con client
	try {
		
		String jsonStr = getBody(request);
		System.out.println(jsonStr);
		/*JSONObject jsonObject = new JSONObject(jsonStr);
		Map<String, Object> map = jsonObject.toMap();
		HashMap<String, HashMap<String, String>> valorEtiquetas = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> etiquetasMap = null;
		ArrayList etiquetasList = null;
		
			if(!entry.getKey().equals("product") && !entry.getKey().equals("references")) {
				valorEtiquetas.put(entry.getKey(), (HashMap<String, String>) entry.getValue());
			}else {
				
				etiquetasList = (ArrayList) entry.getValue();
				Integer i = 0;
				Iterator it = etiquetasList.iterator();
				while(it.hasNext()) {
					valorEtiquetas.put(entry.getKey().concat(i.toString()), (HashMap<String, String>) it.next());
					i = i+1;;
				}
			}
		}*/
		InputStream is = getClass().getResourceAsStream("/reports/AtnContract.pdf");
		PdfReader reader = new PdfReader(is, null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, baos);
		AcroFields fields = stamper.getAcroFields();
		fields.setField("nombre1", "JESUS ANTONIO MOHEDANO ORTEGA");

		stamper.setFormFlattening(true);
		stamper.close();
		try {
			response.setHeader("Expires", "0");
			 response.setHeader("Cache-Control",
			 "must-revalidate, post-check=0, pre-check=0");
			 response.setHeader("Pragma", "public");
			 response.setContentType(
					 "application/vnd.adobe.fdf");
					 response.setHeader("Content-Disposition",
					 "attachment; filename=\"subscribe.fdf\"");
			 response.setContentLength(baos.size());
			 response.addHeader("Access-Control-Allow-Origin", "true");
			 OutputStream os = response.getOutputStream();
			 System.out.println("escritura");
			 baos.writeTo(os);
			 os.flush();
			 System.out.println("confirmar");
			 os.close(); 
		}catch(Exception de) {
			System.out.println("error escritura");
			System.out.println(de);
		}
	} catch (Exception de) {
		System.out.println(de);
		//throw new IOException(de.getMessage());
	}
	System.out.println("FIN");
	
}
	public static String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}
}

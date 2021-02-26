package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class reportePDF
 */
public class reportePDF extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public reportePDF() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InputStream is = getClass().getResourceAsStream("/reports/etiquetas.pdf");
			PdfReader reader = new PdfReader(is, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(reader, baos);
			AcroFields fields = stamper.getAcroFields();
			fields.setField("atn_dependencia","JESUS MOHEDANO");
			stamper.setFormFlattening(true);
			stamper.close();
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
			} catch (DocumentException de) {
			throw new IOException(de.getMessage());
			}
		response.setContentType("application/pdf");
		response.setHeader("Cache-control","must-revalidate, post-check=0, pre-check=0");
		/*String idCliente = request.getParameter("idCliente");
		String urlString = "https://reqres.in/api/users/";
		if(idCliente.trim() == null ||idCliente.trim() == "") {
			urlString=urlString.concat("");
		}else {
			urlString=urlString.concat(idCliente);
		}   
	    
		InputStream is = getClass().getResourceAsStream("/reports/FormatoPreCaptura.jrxml");
	    Map parameters = new HashMap();
	      parameters.put("path", "/images/precaptura.jpg");
	      parameters.put(JsonQueryExecuterFactory.JSON_SOURCE, urlString);

		try {
			 JasperReport report = JasperCompileManager.compileReport(is);
			 JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters);
			 byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			 response.setContentType("application/pdf");
		      response.setContentLength(pdfByteArray.length);
		      ServletOutputStream ouputStream = response.getOutputStream();
		      ouputStream.write(pdfByteArray, 0, pdfByteArray.length);
		      ouputStream.flush();
		      ouputStream.close();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	    /*System.out.println(String.format("Status: %d, JSON Payload: %s", status, json));
		response.getWriter().append("Served at: ").append(json);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

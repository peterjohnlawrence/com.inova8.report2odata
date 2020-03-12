package report2odata;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import reportTemplating.ReportTemplate;

//Extend HttpServlet class to create Http Servlet
public class ReportService extends HttpServlet {

	private static final long serialVersionUID = -9076326979803690757L;
	private static final String INOVA8_DIRECTORY = ".." + File.separator + ".." + File.separator + "inova8";
	private static final String CONFIG_DIRECTORY = File.separator + "report2odata";
	private String reportTemplatePath;
	private ReportTemplate reportTemplate;

	public void init() throws ServletException {
		reportTemplatePath = getServletContext().getRealPath("/") + File.separator + INOVA8_DIRECTORY + CONFIG_DIRECTORY
				+ File.separator;
		reportTemplate = new ReportTemplate(reportTemplatePath);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String[] path =  request.getPathInfo().split("/");
		String reportTemplateName = path[1];
		HashMap<String, String[]> reportOptions = new HashMap<String, String[]>(request.getParameterMap());
		reportOptions.put("pathParts", Arrays.copyOfRange(path, 1 , path.length));
		Principal user = request.getUserPrincipal();

		String transformedReport = reportTemplate.process(reportTemplateName,reportOptions,user);
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(transformedReport);
	}
	public void destroy() {
	}
	
}
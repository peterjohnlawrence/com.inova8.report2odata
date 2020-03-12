package reportTemplating;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import queryOData.Query;
import transformData.Transform;

public class ReportTemplate {
	VelocityEngine velocityEngine;
	private String reportTemplatePath;
	Map<String, String[]> reportOptions;
	public ReportTemplate(String reportTemplatePath) {
		this.reportTemplatePath = reportTemplatePath;
		velocityEngine = new VelocityEngine();
		Properties props = new Properties();
		props.put("file.resource.loader.path", reportTemplatePath);
		props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		velocityEngine.init(props);

	}

	public String process(String reportTemplateName, Map<String, String[]> reportOptions, Principal user) throws ClientProtocolException, IOException {
		this.reportOptions = reportOptions;
		Template reportTemplate = null;
		File reportTemplateFile =  getReportTemplateFile(reportTemplateName + ".vm", user);
		reportTemplate= velocityEngine.getTemplate(reportTemplateFile.getName());
		
		StringWriter reportWriter = new StringWriter();
		VelocityContext reportContext = new VelocityContext();

		reportContext.put("this", this);
		reportTemplate.merge(reportContext, reportWriter);
		return reportWriter.toString();

	}
	public String queryTransformer(String query, String reportXSL) throws ClientProtocolException, IOException {
		File reportXSLFile = getReportTemplateFile(reportXSL);
		String queryResult = Query.execute(query.replaceAll("\\s+", ""));
		return Transform.execute("<data><![CDATA[" + queryResult + "]]></data>", reportXSLFile);	
	}
	protected File getReportTemplateFile(String fileName) {
		return getReportTemplateFile(fileName, null);
	}
	protected File getReportTemplateFile(String fileName, Principal user) {
		File file;
		String userPath= reportTemplatePath;
		if(user!=null) {
			userPath +=  user.getName() + File.separator; 
		}
		if (fileName.lastIndexOf("\\") >= 0) {
			file = new File(userPath + fileName.substring(fileName.lastIndexOf("\\")));
		} else {
			file = new File(userPath + fileName.substring(fileName.lastIndexOf("\\") + 1));
		}
		return file;
	}
	public Map<String, String[]> getReportOptions() {
		return reportOptions;
	}
	public String[] getReportOption(String key) {
		return reportOptions.get(key);
	}
	public String isoDateTimeNow() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		return nowAsISO;
	}
}

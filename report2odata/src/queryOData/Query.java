package queryOData;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public  class Query {
	public  static String execute(String serviceUrl) throws ClientProtocolException, IOException   {
		
		 final CloseableHttpClient httpClient = HttpClients.createDefault();
		 String result = null;
		
	     HttpGet request = new HttpGet(serviceUrl);

	        // add request headers
	       // request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

	        try (CloseableHttpResponse response = httpClient.execute(request)) {

	            // Get HttpResponse Status
	            System.out.println(response.getStatusLine().toString());

	            HttpEntity entity = response.getEntity();
	            Header headers = entity.getContentType();
	            System.out.println(headers);

	            if (entity != null) {
	                 result = EntityUtils.toString(entity);
	            }

	        }
	        httpClient.close();
	        return result;

	}
}



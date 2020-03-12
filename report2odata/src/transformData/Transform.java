package transformData;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.s9api.ExtensionFunction;
import net.sf.saxon.s9api.Processor;
import sigUtils.ConvertExpressionsToXML;

public  class Transform {
	public  static String execute(String inputXmlString,File xsltFile)   {
		
		TransformerFactoryImpl factory = new net.sf.saxon.TransformerFactoryImpl();
        
        // Get the currently used processor
        net.sf.saxon.Configuration saxonConfig = factory.getConfiguration();
        Processor processor = (Processor) saxonConfig.getProcessor();     
        // Here extension happens, test comes from class ConvertExpressionsToXML
        ExtensionFunction convertExpressionsToXML = new ConvertExpressionsToXML();
        processor.registerExtensionFunction(convertExpressionsToXML);       
        
        
        
        
        
        
        
        StreamSource xslt = new StreamSource(xsltFile);

        StreamSource text = new StreamSource(new StringReader(inputXmlString));
        StringWriter writer = new StringWriter();
        StreamResult textOP = new StreamResult(writer);

        try {
            Transformer transformer = factory.newTransformer(xslt);
            transformer.transform(text, textOP);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e2) {
            e2.printStackTrace();
        }
        String results = writer.toString();

        return results;	

	}
}



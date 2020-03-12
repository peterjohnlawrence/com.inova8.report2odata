package sigUtils;


import net.byteseek.compiler.CompileException;
import net.sf.saxon.s9api.ExtensionFunction;
import net.sf.saxon.s9api.ItemType;
import net.sf.saxon.s9api.OccurrenceIndicator;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SequenceType;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;

import uk.gov.nationalarchives.droid.core.signature.compiler.ByteSequenceAnchor;
import uk.gov.nationalarchives.droid.core.signature.compiler.ByteSequenceCompiler;
import uk.gov.nationalarchives.droid.core.signature.compiler.ByteSequenceSerializer;
import uk.gov.nationalarchives.droid.core.signature.compiler.SignatureType;

public class ConvertExpressionsToXML implements ExtensionFunction {

    @Override
    public QName getName() {
        return new QName("http://www.nationalarchives.gov.uk/PRONOM", "ConvertExpressionsToXML");
    }

    @Override
    public SequenceType getResultType() {
        return SequenceType.makeSequenceType(ItemType.STRING, OccurrenceIndicator.ONE);
    }

    @Override
    public net.sf.saxon.s9api.SequenceType[] getArgumentTypes() {
    	return new SequenceType[] {
    			  SequenceType.makeSequenceType(ItemType.STRING, OccurrenceIndicator.ONE),
    			  SequenceType.makeSequenceType(ItemType.STRING, OccurrenceIndicator.ZERO_OR_ONE),
    			  SequenceType.makeSequenceType(ItemType.STRING, OccurrenceIndicator.ZERO_OR_ONE)
    	};
    }

    @Override
    public XdmValue call( XdmValue[] arguments) throws SaxonApiException {
		/*    	
    	| compileType | Compile signatures for DROID. This can mean longer sequences to search for, which is usually faster. (Default)                                                                                      
    	| compileType | Compile signatures for PRONOM. PRONOM only allows bytes in the main search sequences.                                                                                                               
    	| sigType     | Render expressions as closely as possible to binary signature format. This attempts to make signatures compatible with older versions of DROID.                                                     
    	| sigType	  | Render expressions using the full container signature syntax.   This is more powerful and readable, but is not compatible with versions of DROID that don't support container signatures. (Default) 
    	| anchorType  | Specify whether an expression is anchored to BOFoffset, EOFoffset or Variable.  For example: "--anchor bofoffset"   															
		    	
		ByteSequenceCompiler.CompileType compileType =ByteSequenceCompiler.CompileType.PRONOM | ByteSequenceCompiler.CompileType.DROID (Default) ; P|D
		SignatureType sigType = SignatureType.BINARY | SignatureType.CONTAINER (Default) ;   B|C
		ByteSequenceAnchor anchorType = ByteSequenceAnchor.BOFOffset (Default) | ByteSequenceAnchor.EOFOffset | ByteSequenceAnchor.VariableOffset; B|E|V
		*/

        String expression = arguments[0].toString();
        String sCompileType = arguments[1].toString();
        String sSigType = arguments[2].toString();
    	ByteSequenceCompiler.CompileType compileType = sCompileType.toUpperCase().equals("P") ? ByteSequenceCompiler.CompileType.PRONOM : ByteSequenceCompiler.CompileType.DROID;
        SignatureType sigType = sSigType.toUpperCase().equals("B") ? SignatureType.BINARY :  SignatureType.CONTAINER  ;     
        ByteSequenceAnchor offset =  ByteSequenceAnchor.BOFOffset;
    	String xml;
    	try {
    		xml = ByteSequenceSerializer.SERIALIZER.toXML(expression, offset, compileType, sigType);
    	} catch (CompileException e) {
            throw new SaxonApiException(e.getMessage(), e);
        }			
        String result =  xml;
        return new XdmAtomicValue(result);
    }

}
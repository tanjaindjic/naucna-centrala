package master.naucnacentrala.model.elastic.analyzer;


import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class LatCyrFilter extends TokenFilter {
	
	private CharTermAttribute cta;
	
	public LatCyrFilter(TokenStream input) {
		super(input);
		this.cta = (CharTermAttribute) input.addAttribute(CharTermAttribute.class); 
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(input.incrementToken()){
			String text = cta.toString();
			cta.setEmpty();
			cta.append(LatCyrUtils.toLatin(text));
			return true;
		}
		
		return false;
	}

}

package test;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import scala.collection.Seq;
/**
 * OCR example
 * refer
 * https://stackabuse.com/tesseract-simple-java-optical-character-recognition/
 * 
 * @author user
 *
 */
public class TestMain {

	public static void main(String[] args) throws TesseractException {		
		TestMain main = new TestMain();
		String text = main.process("1.2.jpg", "kor");
		//System.out.println(text);		
		String nlpText = main.nlp(text);
		System.out.println(nlpText);
	}

	/**
	 * 출처: https://jjeong.tistory.com/1241 [jjeong]
	*/
	public String process(String imageFilePath, String language) {
		String datapath = "D://data//tessdata_kr";		
		String result="";
		
		File imageFile;
		ITesseract instance;

		instance = new Tesseract();
		instance.setDatapath(new File(datapath).getPath());
		//instance.setLanguage("eng+kor");
		instance.setLanguage(language);

		try {
			ImageIO.scanForPlugins();
			imageFile = new File(imageFilePath);
			result = instance.doOCR(imageFile);						
		} catch (TesseractException e) {
			System.out.println(e.getMessage());
		}		
		
		return result;
	}
	
	public String nlp(String inputText) {
		String result ;
		inputText = inputText.replaceAll("(\r\n|\r|\n|\n\r)", " ");
		inputText = inputText.replaceAll(" ","");
	    // Normalize
	    CharSequence normalized = OpenKoreanTextProcessorJava.normalize(inputText);
	    //System.out.println(normalized);
		result= normalized.toString();
		 // Tokenize
	    Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
	    //System.out.println(OpenKoreanTextProcessorJava.tokensToJavaStringList(tokens));
		

	    // Phrase extraction
	    StringBuffer strBuffer = new StringBuffer();
	    List<KoreanPhraseExtractor.KoreanPhrase> phrases = OpenKoreanTextProcessorJava.extractPhrases(tokens, true, true);
	    phrases.forEach(phrase->{
	    	strBuffer.append(phrase.text()+" ");	    	
	    });	    	    
		return strBuffer.toString();
	}

}

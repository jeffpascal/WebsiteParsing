package imobiliare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import interfata.Interfata;

/**
 * parse an image from a url
 * 
 * @author JeanPascal
 *
 *         todo: - find image - show it in ui
 */
public class ParseFromUrl {
	public static void main(String[] args) throws IOException {
		
			String url = 
					"https://www.imobiliare.ro/vanzare-apartamente/timisoara/complex-studentesc/apartament-de-vanzare-2-camere-X7630002C?lista=27555241";
			ImobiliareDataExtractor extracted = new ImobiliareDataExtractor(url);
			System.out.println(extracted);
			
			Interfata inte = new Interfata();
			inte.initialize(extracted.getImage());
			
	}
}

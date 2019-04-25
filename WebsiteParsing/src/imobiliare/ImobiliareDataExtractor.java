package imobiliare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImobiliareDataExtractor {
	String url;
	// done
	private Map<String, String> dataLayer;

	private List<String> imageUrls;

	private String name;
	private String description;

	public ImobiliareDataExtractor(String url) {
		this.url = url;
		imageUrls = new ArrayList<String>();
		dataLayer = new HashMap<String, String>();
		process();

	}
	
	public String getImage() {
		return imageUrls.get(2);
	}
	private void process() {
		try {
			URL imo = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(imo.openStream(), "UTF-8"));
			List<String> array = new ArrayList<String>();
			List<String> linkArray = new ArrayList<String>();
			String line = reader.readLine();
			boolean foundFirstData = false;

			while (line != null) {

				// look for dataLayer then get its contents (17 lines)
				if (line.contains("dataLayer") && foundFirstData == false) {
					foundFirstData = true;
					for (int i = 0; i < 17; i++) {
						line = reader.readLine();
						array.add(line.trim().replace(",", "").replace("'", ""));
					}

				}

				// look for links in the following script
				if (line.contains("<script type=\"application/ld+json\">")) {
					while (true) {
						if (line.contains("</script>"))
							break;
						if (line.contains("https://")) {
							linkArray.add(line.trim());
						}

						if (line.contains("name")) {
							this.name = line.trim();
						}
						if (line.contains("description")) {
							this.description = line.trim();
						}
						line = reader.readLine();
					}
				}
				line = reader.readLine();
			}

			try {
				extractDataLayer(array);
				extractImageLinks(linkArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private final Pattern urlPattern = Pattern.compile(
			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
					+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private void extractImageLinks(List<String> linkArray) {
		List<String> justLinks = new ArrayList<>();

		for (String each : linkArray) {
			Matcher matcher = urlPattern.matcher(each);
			while (matcher.find()) {
				int matchStart = matcher.start(1);
				int matchEnd = matcher.end();
				String formed = each.substring(matchStart, matchEnd);
				justLinks.add(formed);
			}
		}
		for (String each : justLinks) {
			if (each.contains("img"))
				imageUrls.add(each);
		}

	}

	@Override
	public String toString() {
		return String.format(
				"ImobiliareDataExtractor [url=%s, dataLayer=%s, imageUrls=%s, \n name=%s,\n description=%s]", url,
				dataLayer, imageUrls, name, description);
	}

	private void extractDataLayer(List<String> array) throws Exception {
		for (String each : array) {
			String[] line = each.split(":");
			if (line.length != 2)
				throw new Exception("Line problem when extractingDataLayer because of size");
			dataLayer.put(line[0].trim(), line[1].trim());
		}
	}
}

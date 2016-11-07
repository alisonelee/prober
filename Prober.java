import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
/**
 * A simple monitoring prober that lets you track the uptime and response codes
 * of a web server.
 *
 * Must be invoked with two command line arguments: <url> <sample_file>
 */
public class Prober {
	static URL page;
	static String url = "";
	static HttpURLConnection connection;
	static PrintWriter writer;
	static int PORT = 80;
	static int SECS = 30;
	static long startTime;
	static long currentTime;
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Error: Must have two arguments specified.");
			System.exit(0);
		}
		// Setup
		url = args[0];
		String fileName = args[1];

		// Checks
		if (!url.substring(0, 7).equals("http://")) {
			System.err.println("Error: Please enter a valid url with the http protocol");
			System.exit(0);
		}
		File file = new File(fileName);
		try {
			file.createNewFile();
			writer = new PrintWriter(new FileWriter(file), true);
			writer.println("URL = " + url);
			startTime = System.currentTimeMillis();
			while (true) {
				currentTime = System.currentTimeMillis();
				if (((currentTime - startTime) / 1000) % SECS == 0){
					probe();
					Thread.sleep(SECS * 1000);
				}
			}

		}
		catch (IOException | InterruptedException e) {}
	}
	public static void probe(){
		try {
			page = new URL(url + ":" + PORT);
			connection = (HttpURLConnection)page.openConnection();
			connection.setConnectTimeout(SECS * 1000);
			connection.setReadTimeout(SECS * 1000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent"," Mozilla/5.0");
			connection.connect();
			int statusCode = connection.getResponseCode();
			if (statusCode / 100 != 2 ) {
				if (statusCode / 100 == 3) // redirects
					writer.println(currentTime + ",  " + statusCode);
				else // not successful
					writer.println(currentTime + ", -1");
			}
			else // successful
				writer.println(currentTime + ", " + statusCode);
		}
		catch(IOException e) {
			writer.println(currentTime + ", -1");
		}
	}
}

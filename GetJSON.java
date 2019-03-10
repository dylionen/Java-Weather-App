package Pack;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFrame;


public class GetJSON {
	final String API_KEY = "6e15a72d22e5408494a161606181712";
	/*
	 * String Test = "http://api.worldweatheronline.com/premium/v1/" +
	 * "weather.ashx?key=6e15a72d22e5408494a161606181712&q=Szczecin" +
	 * "&format=json&num_of_days=2";
	 */
	final String URL = "http://api.worldweatheronline.com/premium/v1/weather.ashx";
	final String keyApi = "?key=";
	final String keyCity = "&q=";
	final String keyFormat = "&format=xml";
	final String keyNumOfDays = "&num_of_days=";

	String CityName;
	String NumberOfDays;
	String XMLData = "";
	String askURL;

	@Override
	public String toString() {
		return XMLData;
	}

	public GetJSON(String CityName, String NumberOfDays) {
		this.CityName = CityName;
		this.NumberOfDays = NumberOfDays;
		this.askURL = URL + keyApi + API_KEY + keyCity + CityName + keyFormat + keyNumOfDays + NumberOfDays;

		connect();
	}

	@SuppressWarnings("deprecation")
	public void connect() {
		try {

			URL url = new URL(askURL);
			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				InputStream inputStream = connection.getInputStream();

				BufferedInputStream reader = new BufferedInputStream(inputStream);
				DataInputStream dos = new DataInputStream(reader);
				String tmp;

				while ((tmp = dos.readLine()) != null) {
					// System.out.println(tmp);
					XMLData = XMLData + tmp;
				}
				// System.out.println(XMLData);

				dos.close();
				reader.close();
				inputStream.close();

			} catch (IOException io) {
				System.out.println("sa" + io);
			} finally {

			}
		} catch (MalformedURLException ma) {
			System.out.println(ma);
		}

	}

	public Boolean Validation() {
		if (XMLData.length() < 150) {
			return false;
		} else {
			return true;
		}

	}

	public void getDetailWindow() {
		DetailWindow dWin = new DetailWindow(XMLData);
		dWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dWin.setVisible(true);
	}
}

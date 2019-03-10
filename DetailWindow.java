package Pack;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


import java.io.StringReader;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DetailWindow extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String[] hourlyData = new String[] { "time", "tempC", "tempF", "windspeedMiles", "windspeedKmph" };
	String XMLData;
	JLabel lTempC, lTempF, lTime, lType, lQuery, lTempCF, lTempFF, lPressure, lWindspeedMiles, lWindspeedKmph,
			lWinddirDegree, lWinddir16Point, img;
	JButton bNext, bPrev;
	Document doc;
	String type, query;
	JLabel[][] tablica = new JLabel[8][6];
	JLabel[] tablicaNames = new JLabel[6];
	NodeList nListWethter;
	Node[] weather;

	Short page = -1;
	int maxPage = 0;

	public DetailWindow(String XMLData) {
		this.XMLData = XMLData;
		setSize(750, 600);
		setTitle("Weather Details");
		setLayout(null);

		// labels
		lType = new JLabel("lType");
		lType.setBounds(300, 20, 250, 20);
		add(lType);

		lQuery = new JLabel("lQuery");
		lQuery.setBounds(300, 40, 250, 20);
		add(lQuery);

		lTime = new JLabel("Ltime");
		lTime.setBounds(300, 60, 250, 20);
		add(lTime);

		lTempC = new JLabel("Temp C: ");
		lTempC.setBounds(20, 40, 250, 20);
		add(lTempC);

		lTempF = new JLabel("Temp F: ");
		lTempF.setBounds(20, 60, 250, 20);
		add(lTempF);

		lTempCF = new JLabel("lTempCF C: ");
		lTempCF.setBounds(20, 80, 250, 20);
		add(lTempCF);

		lTempFF = new JLabel("lTempFF F: ");
		lTempFF.setBounds(20, 100, 250, 20);
		add(lTempFF);

		lPressure = new JLabel("lPressure: ");
		lPressure.setBounds(20, 120, 250, 20);
		add(lPressure);

		lWindspeedMiles = new JLabel("lWindspeedMiles: ");
		lWindspeedMiles.setBounds(20, 140, 200, 20);
		add(lWindspeedMiles);

		lWindspeedKmph = new JLabel("lWindspeedKmph: ");
		lWindspeedKmph.setBounds(20, 160, 200, 20);
		add(lWindspeedKmph);

		lWinddirDegree = new JLabel("lWinddirDegree: ");
		lWinddirDegree.setBounds(20, 180, 200, 20);
		add(lWinddirDegree);

		lWinddir16Point = new JLabel("lWinddir16Point: ");
		lWinddir16Point.setBounds(20, 200, 200, 20);
		add(lWinddir16Point);

		img = new JLabel();
		img.setBounds(200, 20, 80, 80);

		// buttons
		bNext = new JButton(">>");
		bNext.setBounds(330, 480, 50, 20);
		add(bNext);
		bNext.addActionListener(this);

		bPrev = new JButton("<<");
		bPrev.setBounds(280, 480, 50, 20);
		add(bPrev);
		bPrev.addActionListener(this);

		GetXMLData();
		ShowCurrentWeather();
		setActualPage(page);

	}

	public void setActualPage(Short page) {
		DeleteTable();
		DeleteNameTable();
		if (page == -1) {
			ShowCurrentWeather();
		} else
			ShowNextDayWeather(page);

		if (page == maxPage - 1)
			bNext.setEnabled(false);
		else
			bNext.setEnabled(true);

		if (page == -1)
			bPrev.setEnabled(false);
		else
			bPrev.setEnabled(true);

	}

	public void CreateHourTable(int x, int y, String value) {
		tablica[x][y] = new JLabel();
		tablica[x][y].setBounds((x + 2) * 65, (y + 5) * 40, 60, 30);
		tablica[x][y].setText(value);
		tablica[x][y].setVisible(true);
		add(tablica[x][y]);
	}

	public void CreateNameTable(int x, String value) {
		tablicaNames[x] = new JLabel();
		tablicaNames[x].setBounds(5, (x + 5) * 40, 120, 30);
		tablicaNames[x].setText(value);
		tablicaNames[x].setForeground(Color.RED);
		tablicaNames[x].setVisible(true);
		add(tablicaNames[x]);
	}

	public void WriteValuesToNameTable() {
		String[] str = new String[] { "Godzina:", "Temperatura C:", "Temperatura F:", "Prêdkoœæ wiatru:",
				"Kierunek wiatru:", "Ikona:" };
		for (int i = 0; i < str.length; i++) {
			CreateNameTable(i, str[i]);
		}
	}

	public void DeleteNameTable() {
		try {
			for (int i = 0; i < 8; i++) {
				if (tablicaNames[i] != null) {
					tablicaNames[i].setText("");
					tablicaNames[i].setVisible(false);
				}
			}
		} catch (Exception e) {

		}
	}

	public String CorrectHour(String hour) {
		if (hour.length() == 1)
			return "0:00";
		else if (hour.length() == 3)
			return hour.charAt(0) + ":" + hour.charAt(1) + "" + hour.charAt(2);
		else
			return hour.charAt(0) + "" + hour.charAt(1) + ":" + hour.charAt(2) + "" + hour.charAt(3);

	}

	public void CreateHourTable(String url, int x, int y) {
		tablica[x][y] = new JLabel();
		tablica[x][y].setBounds((x + 2) * 65, (y + 5) * 40, 65, 50);
		WriteImageToLabelFromUrl(url, tablica[x][y]);
		add(tablica[x][y]);
		tablica[x][y].setVisible(true);
	}

	public void DeleteTable() {
		try {
			for (int j = 0; j < 6; j++) {
				for (int i = 0; i < 8; i++) {
					if (tablica[i][j] != null) {
						tablica[i][j].setText("");
						tablica[i][j].setVisible(false);
					}

				}
			}
		} catch (Exception e) {
			System.out.println("Brak tablicy");
		}

	}

	public void GetXMLData() {
		try {
			doc = loadXMLFromString(XMLData);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("request");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;

			WriteValueToLabel(lType, "", "type", eElement);
			WriteValueToLabel(lQuery, "", "query", eElement);
			maxPage = doc.getElementsByTagName("weather").getLength();

			nListWethter = doc.getElementsByTagName("weather");

			weather = new Node[nListWethter.getLength()];
			for (int i = 0; i < weather.length; i++) {
				weather[i] = nListWethter.item(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String GetValueFromNode(Node tmp, String name) {
		try {
			Element eTmp = (Element) tmp;
			if (name == "time")
				return CorrectHour((eTmp.getElementsByTagName(name).item(0).getTextContent()));
			else
				return (eTmp.getElementsByTagName(name).item(0).getTextContent());
		} catch (DOMException e) {
			System.out.println("Brak drzewa?");
			return "";
		} catch (Exception f) {
			System.out.println("Bak pola");
			return "";
		}
	}

	public void ShowCurrentWeather() {
		try {

			NodeList nList = doc.getElementsByTagName("current_condition");
			Node nNode = nList.item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				WriteValueToLabel(lTime, "Czas obserwacji", "observation_time", nNode);

				WriteImageToLabelFromUrl(GetValueFromNode(nNode, "weatherIconUrl"), img);

				WriteValueToLabel(lTempC, "Temp C", "temp_C", nNode);
				WriteValueToLabel(lTempF, "Temp F", "temp_F", nNode);
				WriteValueToLabel(lTempCF, "Odczuwalna Temp C", "FeelsLikeC", nNode);
				WriteValueToLabel(lTempFF, "Odczuwalna Temp F", "FeelsLikeF", nNode);

				WriteValueToLabel(lPressure, "Ciœnienie", "pressure", nNode);
				WriteValueToLabel(lWindspeedMiles, "Prêdkoœæ wiatru(Mile)", "windspeedMiles", nNode);
				WriteValueToLabel(lWindspeedKmph, "Prêdkoœæ wiatru(Kmph" + ")", "windspeedKmph", nNode);
				WriteValueToLabel(lWinddirDegree, "Kierunek wiatru(Stopnie)", "winddirDegree", nNode);
				WriteValueToLabel(lWinddir16Point, "Kierunek wiatru", "winddir16Point", nNode);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Wpisano z³e miasto");
		}

	}

	public void WriteImageToLabelFromUrl(String Url, JLabel label) {

		try {
			URL imageURL = new URL(Url);
			BufferedImage netImage = ImageIO.read(imageURL);
			label.setIcon(new ImageIcon(netImage));
			add(label);
			label.setVisible(true);
		} catch (Exception e) {
			System.out.println("Brak");
		}
	}

	public void SetVisibleFalseAllLabels() {
		img.setVisible(false);
		lTempC.setVisible(false);
		lTempF.setVisible(false);
		lTime.setVisible(false);
		lTempCF.setVisible(false);
		lTempFF.setVisible(false);
		lPressure.setVisible(false);
		lWindspeedMiles.setVisible(false);
		lWindspeedKmph.setVisible(false);
		lWinddirDegree.setVisible(false);
		lWinddir16Point.setVisible(false);

	}

	public void WriteValueToLabel(JLabel tmp, String desc, String nodeValue, Node node) {
		tmp.setVisible(true);
		String doubleDots = ": ";
		if (desc.equals(""))
			doubleDots = "";
		tmp.setText(desc + doubleDots + GetValueFromNode(node, nodeValue));
	}

	public void ShowNextDayWeather(Short page) {
		SetVisibleFalseAllLabels();

		WriteValueToLabel(lTime, "Data", "date", weather[page]);
		WriteValueToLabel(lTempC, "Temperatura maksymalna C", "maxtempC", weather[page]);
		WriteValueToLabel(lTempF, "Temperatura maksymalna F", "maxtempF", weather[page]);
		WriteValueToLabel(lTempCF, "Temperatura minimalna C", "mintempC", weather[page]);
		WriteValueToLabel(lTempFF, "Temperatura minimalna F", "mintempF", weather[page]);
		WriteDataToTable(page);

	}

	public void WriteDataToTable(int day) {
		WriteValuesToNameTable();
		NodeList tmp = doc.getElementsByTagName("hourly");
		for (int temp = 0; temp < tmp.getLength(); temp++) {

			Node nNode = tmp.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				if (temp >= day * 8 && temp < day * 8 + 8) {
					CreateHourTable(temp % 8, 0, GetValueFromNode(nNode, "time"));
					CreateHourTable(temp % 8, 1, GetValueFromNode(nNode, "tempC"));
					CreateHourTable(temp % 8, 2, GetValueFromNode(nNode, "tempF"));
					CreateHourTable(temp % 8, 3, GetValueFromNode(nNode, "windspeedKmph"));
					CreateHourTable(temp % 8, 4, GetValueFromNode(nNode, "winddir16Point"));
					CreateHourTable(GetValueFromNode(nNode, "weatherIconUrl"), temp % 8, 5);

				}
			}

		}

	}

	public static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if (source == bNext) {
			page++;
			setActualPage(page);
		}
		if (source == bPrev) {
			page--;
			setActualPage(page);
		}

	}

}

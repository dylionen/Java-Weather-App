package Pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Window extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GetJSON jsonClass;

	JButton bSprawdz;
	JTextField tCity, tDays;
	JLabel lCity, lDays;

	public Window() {
		setSize(400, 250);
		setTitle("Pogoda");
		setLayout(null);

		lCity = new JLabel("Miasto: ");
		lCity.setBounds(80, 10, 60, 20);
		add(lCity);

		tCity = new JTextField();
		tCity.setBounds(130, 10, 120, 20);
		add(tCity);

		lDays = new JLabel("Iloœæ dni:");
		lDays.setBounds(80, 40, 60, 20);
		add(lDays);

		tDays = new JTextField();
		tDays.setBounds(130, 40, 100, 20);
		add(tDays);

		bSprawdz = new JButton("Sprawdz pogodê");
		bSprawdz.setBounds(110, 70, 140, 20);
		add(bSprawdz);
		bSprawdz.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == bSprawdz) {
			try {
				jsonClass = new GetJSON((java.net.URLEncoder.encode(tCity.getText(), "UTF-8").replace("+", "%20")), tDays.getText());
				if (jsonClass.Validation()) {
					jsonClass.getDetailWindow();
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Niepoprawne dane, wpisz jeszcze raz");
				}

			} catch (NumberFormatException n) {
				System.out.println("Coœ nie dzia³a");
			} catch (UnsupportedEncodingException us) {
				System.out.println("Brak");
			}
		}

	}

}

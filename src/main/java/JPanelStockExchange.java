import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;

public class JPanelStockExchange extends JPanel {
	static DefaultListModel<String> l1 = new DefaultListModel<>();
	JList<String> currencyRatesList = new JList<String>(l1);
	JCalendar calendarFrom = new JCalendar();
	JCalendar calendarTo = new JCalendar();
	JTextField currency = new JTextField();
	JButton calculate = new JButton("Calculate");
	JButton showExpectedRates = new JButton("Show");
	CurrencyRates ratesDisplay = new CurrencyRates();
	ExpectedRates expected = new ExpectedRates();
	
	public JPanelStockExchange(){
		setLayout(null);
		chooseStartDateAndCurrency();
		chooseEndDate(); 
		JScrollPane scroll = new JScrollPane(currencyRatesList);
		JLabel expectedRateLabel = new JLabel("Expected currency exchange rates for the next four weeks:");
		scroll.setBounds(350, 100, 300, 350);
		expectedRateLabel.setBounds(250, 460, 500, 30);
		calculate.setBounds(100, 500, 100, 50);
		showExpectedRates.setBounds(400, 500, 100, 50);
		expectedRateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		add(scroll);
		add(calculate);
		add(expectedRateLabel);
		add(showExpectedRates);
		submit();
		show();
	}
	
	private void chooseStartDateAndCurrency() {
		JLabel introduction = new JLabel("Show the rate of the cryptocurrency in the selected time period:");
		JLabel fromDateLabel = new JLabel("From:");
		JLabel chooseCurrency = new JLabel("Enter cryptocurrency:");
		introduction.setBounds(50, 0, 600, 30);
		fromDateLabel.setBounds(50, 50, 100, 30);
		calendarFrom.setBounds(50, 75, 200, 200);
		chooseCurrency.setBounds(325, 50, 200, 30);
		currency.setBounds(525, 50, 100, 30);
		introduction.setFont(new Font("Arial", Font.PLAIN, 20));
		fromDateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		chooseCurrency.setFont(new Font("Arial", Font.PLAIN, 20));
		add(introduction);
		add(fromDateLabel);
		add(chooseCurrency);
		add(currency);
		add(calendarFrom);
	}
	
	private void chooseEndDate() {
		JLabel toDateLabel = new JLabel("To:");
		toDateLabel.setBounds(50, 275, 100, 30);
		calendarTo.setBounds(50, 300, 200, 200);
		toDateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		add(toDateLabel);
		add(calendarTo);
	}
	
	private void submit() {
		calculate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long[] results = new long[2];
			try {
				l1.removeAllElements();
				results = getDates(results);
				String chosenCurrency = currency.getText();
				ratesDisplay.connectZonda(results[0], results[1], chosenCurrency);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			}
			
		});
	}
	
	public void show() {
		showExpectedRates.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					String chosenCurrency = currency.getText();
					expected.connectZonda(chosenCurrency);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				expected.porownanie();
				JPanelExpectedRates field = new JPanelExpectedRates();
				JFrame frame = new JFrame();
				frame.setVisible(true);
				frame.setTitle("Expected currency rates");
				frame.add(field);
				frame.pack();
				frame.add(field);
			}
		});
	}
	
	private long[] getDates(long[] results ) throws IOException {
		LocalDateTime timeStamp = LocalDateTime.of(1970, 1, 1, 0, 0);
		Date startDate = calendarFrom.getDate();
		LocalDateTime startDateConverted = startDate.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
		Date endDate = calendarTo.getDate();
		LocalDateTime endDateConverted = endDate.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
		long miliSecStartDifference = ChronoUnit.MILLIS.between(timeStamp, startDateConverted);
		long miliSecEndDifference = ChronoUnit.MILLIS.between(timeStamp, endDateConverted);
		results[0] = miliSecStartDifference;
		results[1] = miliSecEndDifference;
		return results;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 600);
	}

}

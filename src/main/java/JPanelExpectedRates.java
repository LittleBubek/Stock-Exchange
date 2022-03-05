import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPanelExpectedRates extends JPanel {
	LocalDateTime startDateConverted;
	LocalDateTime endDateConverted;
	public JPanelExpectedRates() {
		
		displayResults();
	}
	
	private void displayResults() {
		Timestamp start = new Timestamp(ExpectedRates.dates.get(ExpectedRates.index));
		Date dateStart = new Date(start.getTime());
		startDateConverted = dateStart.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
		Timestamp end = new Timestamp(ExpectedRates.dates.get(ExpectedRates.percentShort.size() + ExpectedRates.index));
		Date dateEnd = new Date(end.getTime());
		endDateConverted = dateEnd.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
		
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		LocalDate now = LocalDate.now();
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.drawString("In this period of time chart from the last 5 years was similar to the chart from the last 4 months:", 50, 50);
		g.drawString("Start date: "+startDateConverted, 50,  100);
		g.drawString("End date: "+endDateConverted, 50,  125);
		g.drawString("Expected changes for the next 4 weeks:", 50, 175);
		int y = 225;
		for(int i = ExpectedRates.index; i<(ExpectedRates.index+4); i++) {
			now = now.plusDays(7);
			double percent = ExpectedRates.percentLong.get(ExpectedRates.percentShort.size()+i);
			double roundPercent = Math.round(percent * 100.0) / 100.0;
			g.drawString(now+" "+roundPercent+" %", 50, y);
			y +=25;
		}
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 600);
	}

}

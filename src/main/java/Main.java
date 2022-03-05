import java.io.IOException;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) throws IOException {
		JPanelStockExchange myp = new JPanelStockExchange();
		JFrame window = new JFrame();
		window.setVisible(true);
		window.setTitle("Stock Exchange");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(myp);
		window.pack();
	}

}

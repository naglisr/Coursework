
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static JFrame frame = new JFrame();

	
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		frame.setSize(1200, 1000);
		frame.setResizable(true);
		frame.add(MainMenu.mainMenu);
		frame.setVisible(true);

		frame.add(MainMenu.menu);
		
		
	}
}

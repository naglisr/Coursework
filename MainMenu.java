import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu extends JPanel{
	 final static JPanel mainMenu = new JPanel();
		static JButton newSim = new JButton("New Simulation");
		JButton load = new JButton("Load Simulation");
		JButton exit = new JButton("Quit");
		static Draw draw = new Draw();
		static Simulation simulation;
		static MainMenu menu = new MainMenu();

		final static Options opt = new Options();
		
 public MainMenu(){

		mainMenu.setLayout(null);
		mainMenu.setSize(1200, 1000);

		newSim.setSize(200, 40);
		newSim.setLocation(500, 480);
		load.setSize(200,40);
		load.setLocation(500,530);
		exit.setSize(200, 40);
		exit.setLocation(500, 580);
		mainMenu.add(newSim);
		mainMenu.add(exit);
		mainMenu.add(load);
		
		opt.setListener(new Options.OptionsListener() {
			public void startSimulation(int nPlanets, double G, double D)
			{
				opt.setVisible(false);
				Main.frame.remove(opt);
				Main.frame.add(draw);
				simulation = new Simulation();
				simulation.setD(D);
				simulation.setG(G);
				simulation.generatePlanets(nPlanets);
				draw.setSimulation(simulation);
				draw.start();
			}
		});
	
		MainMenu.newSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(false);
				Main.frame.remove(mainMenu);
				Main.frame.add(opt);
			}
		});
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainMenu.setVisible(false);
				Main.frame.remove(mainMenu);
				Main.frame.add(draw);
				draw.load();
				draw.start();
			}
			
		});

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		Main.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
 }
}

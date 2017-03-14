import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Options extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public interface OptionsListener
	{
		public void startSimulation(int nPlanets, double G, double D);
	}
	
	OptionsListener listener;
	
	JSpinner spnr1 = new JSpinner(new SpinnerNumberModel(100, 0,1000,10));
	JSpinner spnr2 = new JSpinner(new SpinnerNumberModel(1, 0.1,10,0.1));
	JSpinner spnr3 = new JSpinner(new SpinnerNumberModel(0.1, 0.001,1,0.01));
	JLabel noOfPlanets = new JLabel("Number of planets");
	JLabel gravity = new JLabel("Strength of gravity");
	JLabel density = new JLabel("Planet size");
	JButton back = new JButton();
	JButton start = new JButton("Start");
		
	public Options() {
		noOfPlanets.setLocation(200, 180);
		noOfPlanets.setSize(200,20);
		gravity.setLocation(400,180);
		gravity.setSize(200,20);
		density.setLocation(600, 180);
		density.setSize(200,20);
		spnr1.setLocation(200, 200);
		spnr1.setSize(150, 30);
		spnr2.setLocation(400, 200);
		spnr2.setSize(150, 30);
		spnr3.setLocation(600, 200);
		spnr3.setSize(150, 30);
		start.setSize(100,40);
		start.setLocation(600,300);
		this.setLayout(null);
		this.add(noOfPlanets);
		this.add(density);
		this.add(gravity);
		this.add(spnr1);
		this.add(spnr2);
		this.add(spnr3);
		this.add(start);
		
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int spnr1val = (int)spnr1.getValue();
				double spnr2val = (double) spnr2.getValue();
				double spnr3val = (double) spnr3.getValue();
				
				System.out.println(spnr1val + ", " + spnr2val + ", " + spnr3val);
				
				if(listener!=null)
					listener.startSimulation(spnr1val, spnr2val, spnr3val);
			}
		});
		
	}

	public void setListener(OptionsListener ol)
	{
		listener = ol;
	}
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;




public class Draw extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean paused = false;
	boolean dispTrails = false;
	String[] modes = {"drag", "newPlanet", "delete", "select"};
	String mode = modes[0];
	Timer tm;
	Simulation simulation;
	int trailSize = 100;
	JLabel massLabel = new JLabel();
	JLabel velocityLabel = new JLabel();
	
	String[] columnNames= {"Planet", "Mass", "Velocity", "xPos", "yPos"}; 
	//JTable planetTable = new JTable(setTable(), columnNames);
	

	public Draw() {
		super();
		/*planetTable.setLocation(1000,300);
		planetTable.setSize(200, 300);
		this.add(planetTable);*/
		
		
		this.setLayout(null);
		final JButton start = new JButton("Start");
		start.setSize(100,30);
		start.setLocation(1090, 10);
		final JButton pause = new JButton("Pause");
		pause.setSize(100, 30);
		pause.setLocation(1090, 10);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(paused) paused = false;
				tm.start();
				pause.setVisible(true);
				start.setVisible(false);
		}

	});
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if(!paused) paused = true;
					pause.setVisible(false);
					start.setVisible(true);
			}

		});
		
		JButton save = new JButton("Save");
		save.setSize(100, 30);
		save.setLocation(1090, 50);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}

		});
		JButton trails = new JButton("Trails on/off");
		trails.setSize(100, 30);
		trails.setLocation(1090, 90);
		trails.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(dispTrails){
					for(Simulation.Planet p: simulation.planets){
						p.trail1.clear();
					}
					dispTrails=false;
				}
				else{
					dispTrails = true;
					for(Simulation.Planet p: simulation.planets){
						p.trailCount = 0;
					}
				}
			}
		});
		
		this.add(pause);
		this.add(start);
		this.add(save);
		this.add(trails);
		
		
		JButton drag = new JButton("Drag");
		JButton add = new JButton("Add");
		JButton delete = new JButton("Delete");
		JButton select = new JButton("Select");
		JButton selectAll = new JButton("Select/Deselect all");
		
		selectAll.setSize(100,30);
		selectAll.setLocation(1090,900);
		selectAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean allSelected = true;
				for(Simulation.Planet p: simulation.planets){
					if(!p.selected) allSelected = false;
				}
				if(allSelected)
					for(Simulation.Planet p: simulation.planets){
						p.selected = false;
					}
				else
					for(Simulation.Planet p: simulation.planets){
						p.selected = true;
					}
				repaint();
			}
		});
		
		
		drag.setSize(100,30);
		drag.setLocation(10, 900);
		drag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = modes[0];
			}
		});
		add.setSize(100,30);
		add.setLocation(120, 900);
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = modes[1];
			}
		});
		
		
		delete.setSize(100,30);
		delete.setLocation(230,900);
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				LinkedList<Simulation.Planet> processList = new LinkedList<Simulation.Planet>();
				for(Simulation.Planet p: simulation.planets){
					if(p.selected){
						processList.add(p);
					}
				}
				simulation.planets.removeAll(processList);
				processList.clear();
				if(paused) repaint();
			}
		});
		
		
		select.setSize(100,30);
		select.setLocation(340,900);
		select.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = modes[3];
			}
		});
		
		
		this.add(drag);
		this.add(add);
		this.add(delete);
		this.add(select);
		this.add(selectAll);
		
		JButton quit = new JButton("Quit");
		quit.setLocation(10,10);
		quit.setSize(100,30);
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
			}
		});
		
		addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double mFactor = 1.03;
				if(newPlanet!=null){
					newPlanet.setRadius(newPlanet.getRadius()*Math.pow(mFactor, e.getWheelRotation()));
					displayLabels();
				}
				repaint();
			}
			
		});
		addMouseListener(this);
		addMouseMotionListener(this);

	}
	
	/*public Object[][] setTable(){
		Object[][] data = new Object[5][simulation.planets.size()];
		for(int i = 0; i<simulation.planets.size(); i++){
			data[i] = new Object[]{
					i,
					simulation.planets.get(i).getMass(),
					simulation.planets.get(i).getVelocity(),
					simulation.planets.get(i).px,
					simulation.planets.get(i).py
			};
		}
		return data;
	}*/

	public void displayLabels(){
		double velocity = Math.sqrt((x2-x)*(x2-x)+(y2-y)*(y2-y));
		velocityLabel.setText(Integer.toString((int)velocity));
		velocityLabel.setLocation((x+x2)/2-15,(y+y2)/2-30); 
		velocityLabel.setSize(50,20);
		double mass = Math.pow(newPlanet.radius, 3);
		massLabel.setText(Integer.toString((int)mass));
		massLabel.setLocation(x+5, y);
		massLabel.setSize(50,20);
		
		this.add(velocityLabel);
		this.add(massLabel);
	}
	
	public void paintPlanet(Graphics g, Simulation.Planet p) {
		g.fillOval((int) (p.px - p.radius + totdx), (int) (p.py - p.radius + totdy), (int) (2 * p.radius), (int) (2 * p.radius));
	}
	
	public void paintGrid(Graphics g){
		g.setColor(Color.BLACK);
		for(int i = 0; i<getWidth()/100+2;i++){
			int initialX = totdx%100;
			g.drawLine(initialX+100*i, 0, initialX+100*i, getHeight());
			
			int initialY = totdy%100;
			g.drawLine(0, initialY+100*i, getWidth(), initialY+100*i);
		}
	}
	public void paintLines(Graphics g, Queue<Point> points){
		g.setColor(Color.RED);
					for(Point l: points){
						g.fillOval((int)l.x-1+totdx, (int)l.y-1+totdy, 2, 2);
				}
	
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintGrid(g);
		for (Simulation.Planet p : simulation.getPlanets()) {
			if(p!=null){
				g.setColor(Color.BLACK);
				if (p.selected)
					g.setColor(Color.RED);
				if(p.px+totdx>0 && p.px+totdx<getWidth() && p.py+totdy>0 && p.py+totdy<getHeight())
					paintPlanet(g, p);
				Point line = new Point((int)p.px, (int)p.py);
				
					if(dispTrails){
						p.trail1.add(line);
						paintLines(g, p.trail1);
						if(p.trailCount>trailSize){
							p.trail1.remove();
						}
						}
				
			}
			p.trailCount++;
		}
		
		if(mode == modes[1] && newPlanet!=null){
			g.setColor(Color.RED);
			g.drawLine(x, y, x2, y2);
			paintPlanet(g, newPlanet);
		}
		if(mousePressed && mode == modes[3]){
			g.setColor(Color.BLUE);
			g.drawRect(Math.min(x,x2), Math.min(y, y2), Math.abs(x2-x), Math.abs(y2-y));
			
			int i = 0;
			for(Simulation.Planet p: simulation.getPlanets()){
				if(p.px+totdx>Math.min(x,x2) && p.px+totdx<Math.max(x,x2) && p.py+totdy>Math.min(y,y2) &&p.py+totdy<Math.max(y, y2)){
					if(initSelected.get(i)){
						p.selected = false;
					}
					else{
						p.selected = true;
					}
				}
				//System.out.println(initSelected.get(i) + ", " + p.selected);
				i++;
			}
			if(paused) repaint();
		}
	}


	public void start() {
		tm = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (paused)
					tm.stop();
				if (simulation != null) {
					simulation.stepAnimation();
					repaint();
				}
			}
		});

		tm.start();
	}

	public void load() {
		JFileChooser fc = new JFileChooser();
		if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this))
			try {
				ObjectInputStream oin = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
				simulation = (Simulation) oin.readObject();
				oin.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void save() {
		JFileChooser fc = new JFileChooser();
		if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this))
			try {
				OutputStream out = new FileOutputStream(fc.getSelectedFile());
				ObjectOutputStream oout = new ObjectOutputStream(out);
				oout.writeObject(simulation);
				oout.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public Simulation getSimulation() {
		return simulation;
	}

	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}
	
	
	
	int x, y, x2, y2, dx, dy, totdx, totdy = 0;

	ArrayList<Boolean> initSelected = new ArrayList<Boolean>();
	
	Simulation.Planet newPlanet;
	
	boolean mousePressed = false;

	@Override
	public void mouseClicked(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		System.out.println("clicked");
		
		
	if(mode == "d" || mode == "c"){
		for (Simulation.Planet p : simulation.getPlanets()) {
			if (p.px + totdx + p.radius > x && p.px + totdx - p.radius < x && p.py + totdy + p.radius > y
					&& p.py + totdy - p.radius < y) {
				if(mode == "d"){
					if(!p.selected) p.selected = true;
					else p.selected = false;
				}
				else{
					simulation.getPlanets().remove(p);
				}
			}
		}
	}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

		x = e.getX();
		y = e.getY();
		x2=x;
		y2=y;
		
		mousePressed = true;
		
		switch(mode){
		case "drag":
			break;
		case "newPlanet":
			newPlanet = simulation.new Planet(x-totdx, y-totdy, 0, 0, 4, false, null, 0);
			break;
		case "delete":
			for(Simulation.Planet p: simulation.planets){
				if(x>p.px+totdx-p.radius && x<p.px+totdx+p.radius && y>p.py+totdy-p.radius && y<p.py+totdy+p.radius){
					simulation.planets.remove(p);
				}
			}
		break;
		case "select":
			System.out.println("select press");
			initSelected.clear();
			for(Simulation.Planet p: simulation.getPlanets()){
				initSelected.add(p.selected);
			}
		break;
		
		default:
		}
		
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		if(newPlanet!=null) {
			simulation.getPlanets().add(newPlanet);
			newPlanet = null;
			this.remove(massLabel);
			this.remove(velocityLabel);
			repaint();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		

		x2=e.getX();
		y2=e.getY();
		switch(mode){
		case "drag":
			dx = x2 - x;
			dy = y2 - y;
			// calculate overall displacement of space from original position
			totdx += dx;
			totdy += dy;
			x = x2;
			y = y2;
		break;
		case "newPlanet":
			if(newPlanet!=null){
				newPlanet.setSpeed((x-x2)*0.01, (y-y2)*0.01);
				newPlanet.setLocation(x-totdx, y-totdy);
				displayLabels();
			}
		break;
		case "delete":
		break;
		case "select":
			
		break;
		
		default:
		
		}
		
			repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
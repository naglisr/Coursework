import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JComponent;

public class Simulation implements Serializable {
	static final long serialVersionUID = 1L;


	
	public class Planet implements Serializable {
		static final long serialVersionUID = 1L;
		double px, py;
		double vx, vy;
		double radius;
		boolean selected;
		int trailCount;
		Queue<Point> trail1 = new LinkedList<Point>();
		//change trail1 name
		
		Planet(double px, double py, double vx, double vy, double radius, boolean selected, Queue<Point> trail, int trailCount) {
			trail = new LinkedList<Point>();
			this.trail1.addAll(trail);
			this.px = px;
			this.py = py;
			this.vx = vx;
			this.vy = vy;
			this.radius = radius;
			this.selected = selected;
			this.trailCount = trailCount;
		
		}

		public boolean Collide(Planet other) {
			// check if two planets are in collision
			double dx = px - other.px, dy = py - other.py;
			//double minDist = Math.max(radius, other.radius);
			double minDist = radius+other.radius;
			return (dx * dx + dy * dy) < minDist*minDist;
			
		}

		public void Move() {
			px += vx;
			py += vy;
		}

		// other pulls this
		public void Pull(Planet other) {
			// x and y distances between the two planets
			// respectively
			double dx = other.px - px;
			double dy = other.py - py;
			double otMass = D * Math.pow(other.radius, 3);
			// Calculation of acceleration components
			double base = G * simspeed * simspeed * otMass / Math.pow(dx * dx + dy * dy, 1.5);
			// incrementing velocity by acceleration components
			vx += dx * base;
			;
			vy += dy * base;
			;
		}

		public Planet Merge(Planet other) {
			double mass = D * Math.pow(radius, 3);
			double otMass = D * Math.pow(other.radius, 3);
			double newMass = mass + otMass;
			double newRadius = Math.pow(newMass / D, 1.0/3);

			double CoMx = (px * mass + other.px * otMass) / newMass;
			double CoMy = (py * mass + other.py * otMass) / newMass;
			double newvx = (vx * mass + other.vx * otMass) / newMass;
			double newvy = (vy * mass + other.vy * otMass) / newMass;
			return new Planet(CoMx, CoMy, newvx, newvy, newRadius, selected && other.selected, null, 0);
		}
		

		public double getMass(){
			return Math.pow(radius, 3)*D;
		}
		public double getVelocity(){
			return Math.sqrt(vx*vx+vy*vy);
		}
		
		public double getRadius() { return radius; }
		public void setRadius(double r) { radius = r; }
		public void setLocation(double px, double py) { this.px = px; this.py = py; }
		public void setSpeed(double vx, double vy) { this.vx = vx; this.vy = vy; }
	}
	

	LinkedList<Planet> planets;
	double G;
	double D;
	double simspeed = 1;

	public void generatePlanets(int noOfPlanets) {
		
				
		planets = new LinkedList<Planet>();
		for (int i = 0; i < noOfPlanets; i++) {
			double px;
			double py;
			double ux;
			double uy;
			
			px = 1200*Math.random();
			py = 1000*Math.random();
			ux = 1*(0.5-Math.random());
			uy = 1*(0.5-Math.random());
			
			double mass = 5 * Math.random();
			double radius = Math.pow(mass / D, 0.333);
			planets.add(new Planet(px, py, ux, uy, radius, false, null, 0));
		}
	}

	public void stepAnimation() { // increments planets
		// loop through the planets that are being acted on
		for (Planet p : planets) {
			// loop through the planets acting on planet p
			for (Planet q : planets) {
				// can only compare two distinct planets
				if (p != q)
					p.Pull(q);
			}

		}
		for (Planet p : planets) {
			p.Move();
		}
		// process collisions
		LinkedList<Planet> processList = new LinkedList<Planet>();
		processList.addAll(planets);

		while (!processList.isEmpty()) {
			Planet p = processList.pop();
			LinkedList<Planet> deleteList = new LinkedList<Planet>();
			for (Planet q : processList) {
				if (p.Collide(q)) {
					planets.remove(p);
					planets.remove(q);
					p = p.Merge(q);
					planets.add(p);
					deleteList.add(q);
				}
			}
			processList.removeAll(deleteList);
		}
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void setG(double g) {
		G = g;
	}

	public void setD(double d) {
		D = d;
	}

	public void setSimspeed(double simspeed) {
		this.simspeed = simspeed;
	}

	public void setPlanets(LinkedList<Planet> planets) {
		this.planets = planets;
	}
}

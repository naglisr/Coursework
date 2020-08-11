import java.awt.Color;

public class Planet {
	double px, py; //x and y positions
	double vx, vy; //x and y velocities
	double mass;
	double radius;
	Color colour;
	Planet(double pX, double pY, double vX, double vY, double m, double r, Color col){
		px = pX;
		py = pY;
		vx = vX;
		vy = vY;
		mass = m;
		radius = r;
		colour = col;
	}
}


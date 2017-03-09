package parcialPractica;

import processing.core.PApplet;

public class Ejecutable extends PApplet {

	public static void main(String[] args) {
		PApplet.main("parcialPractica.Ejecutable");
	}
	
	Logica log;
	
	public void settings() {
		size(350,350);
	}
	
	public void setup() {
		log = new Logica(this);
	}
	
	public void draw() {
		background(255);
		log.pintar();
	}
	
}

package parcialPractica;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer {

	private PApplet app;
	private Comunicacion com;
	private final String GROUP_ADDRESS = "226.1.2.3";

	private int x;
	private int y;
	private int yRect;
	private int id;
	private boolean start;
	private boolean nuevaBola;
	private float r, g, b;
	private int cantidad;

	public Logica(PApplet app) {
		this.app = app;
		com = new Comunicacion();
		com.addObserver(this);
		new Thread(com).start();

		id = com.getId();
		r = app.random(180);
		g = app.random(180);
		b = app.random(180);

		if (id == 0) {
			x = app.width / 2;
			y = app.height / 2;
		} else if (id >= 1) {
			x = app.width / 2;
			y = -50;
		}

		if (id == 2) {
			try {
				com.enviar(new Movimiento(id, 0, "start"), GROUP_ADDRESS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void update(Observable arg0, Object o) {
		if (o instanceof Movimiento) {
			Movimiento mov = (Movimiento) o;

			if (mov.getMensaje().contains("nuevaBola")) {
				cantidad += 1;
			}

			if (mov.getEmisor() == 2) {
				if (mov.getReceptor() == id) {
					if (mov.getMensaje().contains("start")) {
						if (y != app.height / 2) {
							y = app.height / 2;
						}
						start = true;
					}
				}
			}

			if (mov.getEmisor() == 0) {
				if (mov.getReceptor() == id) {
					if (mov.getMensaje().contains("start")) {
						start = true;
						y = -50;

					}
				}
			}

			if (mov.getEmisor() == 1) {
				if (mov.getReceptor() == id) {
					if (mov.getMensaje().contains("start")) {
						start = true;
						y = -50;
					}
				}
			}

		}
	}

	public void pintar() {

		if (start && id == 0) {
			if (x == app.width / 2 && y == app.height / 2 && nuevaBola) {
				try {
					com.enviar(new Movimiento(id, 0, "nuevaBola"), GROUP_ADDRESS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			y++;

			if (y >= app.height + 50) {
				try {
					com.enviar(new Movimiento(id, 1, "start"), GROUP_ADDRESS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				start = false;
				nuevaBola = true;
			}

		}

		if (start && id == 1) {
			y++;
			if (y >= app.height + 50) {
				try {
					com.enviar(new Movimiento(id, 2, "start"), GROUP_ADDRESS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				start = false;

			}

		}

		if (start && id == 2) {
			y++;
			if (y >= (app.height-yRect)) {
				try {
					yRect += 20;
					com.enviar(new Movimiento(id, 0, "start"), GROUP_ADDRESS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				start = false;

			}
		}

		app.fill(r, g, b);
		app.noStroke();
		app.rect(0, 0, 100, 30);
		app.rect(app.width - 100, app.height - 30, 100, 30);
		app.rect(0, (app.height - yRect), app.width, app.height);

		app.fill(255);
		app.text("Cantidad: " + cantidad, app.width - 80, app.height - 15);
		app.text("Mi id es: " + id, 5, 20);

		app.fill(r, g, b);
		app.ellipse(x, y, 40, 40);

	}

}

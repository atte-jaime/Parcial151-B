package parcialPractica;

import java.io.Serializable;

public class Movimiento implements Serializable {
	
	private int emisor;
	private int receptor;
	private String mensaje;
	
	public Movimiento(int emisor, int receptor, String mensaje) {
		this.emisor=emisor;
		this.receptor=receptor;
		this.mensaje=mensaje;
	}

	public int getEmisor() {
		return emisor;
	}

	public int getReceptor() {
		return receptor;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setEmisor(int emisor) {
		this.emisor = emisor;
	}

	public void setReceptor(int receptor) {
		this.receptor = receptor;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}

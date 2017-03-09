package parcialPractica;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {

	private MulticastSocket s;
	private final int PORT = 6000;
	private final String GROUP_ADDRESS = "226.1.2.3";
	private boolean identificado;
	private int id;
	private boolean life = true;

	public Comunicacion() {

		try {
			s = new MulticastSocket(PORT);
			InetAddress host = InetAddress.getByName(GROUP_ADDRESS);
			s.joinGroup(host);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			autoID();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void autoID() throws IOException {
		try {
			enviar(new MensajeID("Hola soy nuevo"), GROUP_ADDRESS);
			s.setSoTimeout(500);
			while (!identificado) {
				DatagramPacket dPacket = recibir();
				if (dPacket != null) {
					MensajeID msg = (MensajeID) deserialize(dPacket.getData());
					String contenido = msg.getContenido();

					if (contenido.contains("soy:")) {
						String[] division = contenido.split(":");
						int idLimite = Integer.parseInt(division[1]);
						if (idLimite >= id) {
							id = idLimite + 1;
						}
					}
				}
			}
		} catch (SocketTimeoutException e) {
			identificado = true;
			System.out.println("Mi id es:" + id);
			s.setSoTimeout(0);
		}
	}

	private byte[] serialize(Object o) {
		byte[] info = null;
		try {
			ByteArrayOutputStream baOut = new ByteArrayOutputStream();
			ObjectOutputStream oOut = new ObjectOutputStream(baOut);
			oOut.writeObject(o);
			info = baOut.toByteArray();

			oOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	private Object deserialize(byte[] b) {
		Object data = null;
		try {
			ByteArrayInputStream baOut = new ByteArrayInputStream(b);
			ObjectInputStream oOut = new ObjectInputStream(baOut);
			data = oOut.readObject();

			oOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void enviar(Object info, String ipAdrs) throws IOException {
		byte[] data = serialize(info);
		InetAddress host = InetAddress.getByName(ipAdrs);
		DatagramPacket dPacket = new DatagramPacket(data, data.length, host, PORT);

		s.send(dPacket);
	}

	private DatagramPacket recibir() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket dPacket = new DatagramPacket(data, data.length);
		s.receive(dPacket);
		return dPacket;
	}

	@Override
	public void run() {
		while (life) {
			if (s != null) {
				try {
					if (!s.isClosed()) {
						DatagramPacket packet = recibir();
						if (packet != null) {
							if (deserialize(packet.getData()) instanceof MensajeID) {
								MensajeID msg = (MensajeID) deserialize(packet.getData());
								String contenido = msg.getContenido();

								if (contenido.contains("soy nuevo")) {
									enviar(new MensajeID("soy:" + id), GROUP_ADDRESS);
								}
							}

							if (deserialize(packet.getData()) instanceof Movimiento) {
								setChanged();
								notifyObservers((Movimiento) deserialize(packet.getData()));
								clearChanged();
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public MulticastSocket getS() {
		return s;
	}

	public int getPORT() {
		return PORT;
	}

	public String getGROUP_ADDRESS() {
		return GROUP_ADDRESS;
	}

	public boolean isIdentificado() {
		return identificado;
	}

	public int getId() {
		return id;
	}

	public boolean isLife() {
		return life;
	}

	public void setS(MulticastSocket s) {
		this.s = s;
	}

	public void setIdentificado(boolean identificado) {
		this.identificado = identificado;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLife(boolean life) {
		this.life = life;
	}

}

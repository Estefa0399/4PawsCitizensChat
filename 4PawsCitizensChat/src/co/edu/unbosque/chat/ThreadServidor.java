package co.edu.unbosque.chat;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class ThreadServidor implements Runnable {

	private Socket socket = null;
	private File archivo = null;

	public ThreadServidor(Socket pSck) throws Exception {
		archivo = new File("rsc/fpchat-test.csv");
		socket = pSck;
	}

	@Override
	public void run() {

		try {
			Scanner entradaCliente = new Scanner(new InputStreamReader(socket.getInputStream()));
			PrintWriter salidaCliente = new PrintWriter(socket.getOutputStream(), true);

		} catch (Exception e) {
			System.exit(1);
		}
	}

	public void ciudadano(Scanner entradaCliente, PrintWriter salidaCliente) {
		{
			System.out.println("El cliente tiene una conexión con: " + socket);
			try {

				int input = 0;

				String mensaje = "Bienvenido a 4PawsCitizens" + "\n" + "Ingrese un número para continuar" + "\n"
						+ "[1] Crear Caso" + "\n" + "[2] Hablar con un agente";

				salidaCliente.println(mensaje);
				System.out.println("[" + socket + " | Menú principal]");

				try {
					input = Integer.parseInt(entradaCliente.nextLine());
				} catch (Exception e) {
					System.out.println("[" + socket + " | Algo salio mal por lo tanto te desconectaré]");
					socket.close();
				}

				System.out.println("[" + socket + " | Saliendo del menú principal]");

				if (input == 1) {
					System.out.println("[" + socket + " | Iniciando reporte]");
					reporteDeCaso(socket, entradaCliente, salidaCliente, archivo);
				} else if (input == 2) {
					System.out.println("[" + socket + " | Solicitud de chat]");
					chat(entradaCliente, salidaCliente);
					System.exit(0);
				}
			} catch (Exception e) {
				System.exit(1);
			}
		}
	}

	public static void reporteDeCaso(Socket socket, Scanner entradaCliente, PrintWriter salidaCliente, File archivo)
			throws IOException {

		System.out.println("[" + socket + " | Report initiated!]");

		String reporte = "";
		salidaCliente.println("¿Qué tipo de caso desea reportar?" + "\n" + "[1] Pérdida" + "\n" + "[2] Robo" + "\n"
				+ "[3] Abandono" + "\n" + "[4] Animal Peligroso" + "\n" + "[5] Manejo indebido en vía pública");

		System.out.println("[" + socket + " | Seleccione el tipo de caso]");

		String entradaAux = entradaCliente.nextLine();
		int tipoCaso = Integer.parseInt(entradaCliente.nextLine());

		switch (tipoCaso) {
		case 1:
			reporte += "Pérdida,";
			break;
		case 2:
			reporte += "Robo,";
			break;
		case 3:
			reporte += "Abandono,";
			break;
		case 4:
			reporte += "Animal peligroso,";
			break;
		case 5:
			reporte += "Manejo indebido,";
			break;

		default:
			reporte += "Caso Inexistente";
			break;
		}

		salidaCliente.println("¿Cuál es la especie del animal?" + "\n" + "[1] Felino" + "\n" + "[2] Canino");

		System.out.println("[" + socket + " | Seleccionando Especie]");

		entradaAux = entradaCliente.nextLine();

		if (entradaAux.equals("1")) {
			reporte += "Felino,";
		} else if (entradaAux.equals("2")) {
			reporte += "Canino,";
		}

		salidaCliente.println("¿Cuál de los siguientes tamaños describiría mejor al animal?" + "\n" + "[1] Miniatura"
				+ "\n" + "[2] Pequeño" + "\n" + "[3] Mediano" + "\n" + "[4] Grande");

		System.out.println("[" + socket + " | Selecting animal size...]");

		entradaAux = entradaCliente.nextLine();
		int tamaño = Integer.parseInt(entradaCliente.nextLine());

		switch (tamaño) {
		case 1:
			reporte += "Miniatura,";
			break;

		case 2:
			reporte += "Pequeño,";
			break;

		case 3:
			reporte += "Mediano,";
			break;

		case 4:
			reporte += "Grande,";
			break;

		default:
			break;
		}

		salidaCliente.println("¿Cuál es la localidad del animal? Ingrese su respuesta");

		System.out.println("[" + socket + " |Ingresando localidad]");

		reporte += entradaCliente.nextLine() + ",";

		salidaCliente.println("¿Cuál es la dirección exacta del animal? Ingrese su respuesta");

		System.out.println("[" + socket + " | Ingresando dirección]");

		reporte += entradaCliente.nextLine() + ",";

		salidaCliente.println("Ingrese su nombre");

		System.out.println("[" + socket + " | Ingresando nombre]");

		reporte += entradaCliente.nextLine() + ",";

		salidaCliente.println("Ingrese su número de teléfono");

		System.out.println("[" + socket + " | Ingresando numero de telefono]");

		reporte += entradaCliente.nextLine() + ",";

		salidaCliente.println("Ingrese su email");

		System.out.println("[" + socket + " | Ingresando email]");

		reporte += entradaCliente.nextLine() + ",";

		salidaCliente.println("¿Tiene algún comentario adicional? Ingrese su respuesta." + "\n"
				+ "Recuerde que la tecla enter envía el mensaje instantáneamente. No utilice comas.");

		System.out.println("[" + socket + " | Ingresando comentarios]");

		reporte += entradaCliente.nextLine();

		salidaCliente.println("Muchas gracias por su colaboración, el caso ha sido creado.");

		FileWriter salidaArchivo = new FileWriter(archivo, true);

		Date hoy = new Date();

		SimpleDateFormat hoyHoras = new SimpleDateFormat("HH:mm");
		SimpleDateFormat hoyDia = new SimpleDateFormat("dd-MM-yy");

		salidaArchivo.append("\n" + reporte + "," + hoyDia.format(hoy) + "," + hoyHoras.format(hoy));
		salidaArchivo.flush();
	}

	public void chat(Scanner entradaCliente, PrintWriter salidaCliente) {
		salidaCliente.println();
	}

}

package co.edu.unbosque.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	public static void main(String[] args) {
		try (

				ServerSocket serverSocket = new ServerSocket(5000);) {

			System.out.println("4PawsCitizensChat en linea");

			int input = 0;

			while (true) {
				System.out.println("Esperando la conexión del cliente");
				try (Socket socket = serverSocket.accept()) {
					System.out.println("Conexíon del cliente con: " + socket);
					Scanner entradaCliente = new Scanner(new InputStreamReader(socket.getInputStream()));
					PrintWriter salidaCliente = new PrintWriter(socket.getOutputStream(), true);

					// send options text to client
					String mm = "Bienvenido a 4PawsCitizens" + "\n" + "Ingrese un número para continuar" + "\n"
							+ "[1] Crear Caso" + "\n" + "[2] Hablar con un agente";

					salidaCliente.println(mm);
					System.out.println(mm + "\n" + "enviado, esperando respuesta");

					try {
						input = Integer.parseInt(entradaCliente.nextLine());
					} catch (Exception e) {
						socket.close();
					}

					if (input == 1) {
						reporteDeCaso(entradaCliente, salidaCliente);
					}

				}
			}

		} catch (IOException e1) {
			
			e1.printStackTrace();
		}

	}

	public static void reporteDeCaso(Scanner entrada, PrintWriter salida) {
		String reporte = "";
		salida.println("ingrese el tipo de caso que desea reportar" + "\n" + "[1] Pérdida" + "\n" + "[2] Robo" + "\n"
				+ "[3] Abandono" + "\n" + "[4] Animal Peligroso" + "\n" + "[5] Manejo indebido en vía pública");
		if (entrada.nextLine().equals("1")) {
			reporte += "Pérdida";
		}
		if (entrada.nextLine().equals("2")) {
			reporte += "Robo";
		}
		if (entrada.nextLine().equals("3")) {
			reporte += "Abandono";
		}
		if (entrada.nextLine().equals("4")) {
			reporte += "Animal Peligroso";
		}
		if (entrada.nextLine().equals("5")) {
			reporte += "Manejo indebido en vía pública";
		}
	}

}
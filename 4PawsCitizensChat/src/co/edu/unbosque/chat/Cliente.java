package co.edu.unbosque.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {

	public static void main(String[] args) {

		try {
			InetAddress netUsuario = InetAddress.getLocalHost();
			System.out.println("Conectando al servidor");
			try (Socket usuario = new Socket(netUsuario.getHostAddress(), 5000)) {

				System.out.println("Conexión establecida");

				Scanner entradaLocal = new Scanner(System.in);
				Scanner entradaServidor = new Scanner(usuario.getInputStream());
				PrintWriter salidaServidor = new PrintWriter(usuario.getOutputStream(), true);
				String mensajeServidor = "";

				mensajeServidor = entradaServidor.nextLine();
				while (entradaServidor.hasNextLine()) {
					mensajeServidor += "\n" + entradaServidor.nextLine();
				}

				System.out.println(mensajeServidor);

				salidaServidor.println(entradaLocal.nextLine());

				while (true) {
					mensajeServidor = "";
					mensajeServidor = entradaServidor.nextLine();

					if (mensajeServidor.equals("desconectar")) {
						usuario.close();
						System.exit(0);
					} else if (mensajeServidor.equals("chat")) {
						while (true) {

							if (mensajeServidor.equals("desconectar")) {
								usuario.close();
								System.exit(0);
							}
						}
					}

					while (entradaServidor.hasNextLine()) {
						mensajeServidor += "\n" + entradaServidor.nextLine();
					}

					salidaServidor.println(entradaLocal.nextLine());
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		} catch (Exception e1) {
			System.out.println(e1);
		}
	}
}

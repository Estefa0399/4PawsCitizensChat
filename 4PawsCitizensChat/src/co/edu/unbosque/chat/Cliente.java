package co.edu.unbosque.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
	public Cliente() throws Exception {
		InetAddress netUsuario = InetAddress.getLocalHost();

		System.out.println("-Conectando al servidor-");
		try (Socket usuario = new Socket(netUsuario.getHostAddress(), 5000)) {

			System.out.println("Conexión establecida");

			Scanner entradaLocal = new Scanner(System.in);
			PrintWriter salidaServidor = new PrintWriter(usuario.getOutputStream(), true);
			Scanner entradaServidor = new Scanner(usuario.getInputStream());

			salidaServidor.println("Ciudadano");

			String serverMsg = "";

			serverMsg = entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();

			if (serverMsg.equals("chat")) {

				usuario.close();

				chat(usuario.getLocalAddress() + "");
			}

			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();

			System.out.println(serverMsg);

			salidaServidor.println(entradaLocal.nextLine());

			serverMsg = entradaServidor.nextLine();
			serverMsg += "\n" + entradaServidor.nextLine();

			System.out.println(serverMsg);
		}

	}

	public static void chat(String pNetUsuario) throws Exception {
		InetAddress netUsuario = InetAddress.getLocalHost();
		Socket usuarioChat = new Socket(netUsuario, 5000);
	}

}

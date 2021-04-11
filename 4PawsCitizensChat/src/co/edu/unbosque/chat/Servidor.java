package co.edu.unbosque.chat;

import java.io.*;
import java.lang.*;
import java.net.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;

public class Servidor {

	public static void main(String[] args) {

		var pool = Executors.newCachedThreadPool();
		{
			{

				try (

						ServerSocket serverSocket = new ServerSocket(5000)) {

					System.out.println("4PawsCitizensChat en linea");

					while (true) {
						System.out.println("Esperando la conexión de cliente");

						pool.execute(new ThreadServidor(serverSocket.accept()));
					}

				} catch (IOException e) {

					e.printStackTrace();
					
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		}

	}
}
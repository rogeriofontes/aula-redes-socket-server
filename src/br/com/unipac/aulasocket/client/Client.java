package br.com.unipac.aulasocket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {

		String ip = getServerIP();
		if (ip != null) {
			try {
				int port = getServerPort();
				System.out.println("[Conectando ao Servidor..]");
				Socket socket = new Socket(ip, port);
				System.out.println("[Conexão aceita de: " + socket.getInetAddress().toString() + "]");

				sendDataToServer(socket);
				receiverDataFromServer(socket);
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println("Erro!\n" + e.getMessage());
			}
		} else {
			System.out.println("[Forneca um IP ou Nome de Dominio]");
			System.exit(0);
		}
	}

	private static void receiverDataFromServer(Socket cliente) throws IOException, ClassNotFoundException {
		System.out.println("[Recebendo Mensagens]");
		ObjectInputStream ios = new ObjectInputStream(cliente.getInputStream());
		String message = null;
		do {
			message = (String) ios.readObject();
			System.out.println(message);
		} while (!message.endsWith("EOT"));
		cliente.close();
		System.out.println("[Conexão encerrada]");
	}

	private static void sendDataToServer(Socket cliente) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
		System.out.println("Sending request to Socket Server");

		String name = getClientName();
		System.out.println("Digite sua mensagem:");
		String message = getScanner().next();
		oos.writeObject(name + ":" + message);
	}

	private static String getClientName() {
		System.out.println("Digite seu nome:");
		return "@" + getScanner().next();
	}

	private static String getServerIP() {
		System.out.println("Digite o IP do servidor:");
		return getScanner().next();
	}

	private static int getServerPort() {
		System.out.println("Digite a porta do servidor:");
		return getScanner().nextInt();
	}

	public static Scanner getScanner() {
		return new Scanner(System.in);
	}
}

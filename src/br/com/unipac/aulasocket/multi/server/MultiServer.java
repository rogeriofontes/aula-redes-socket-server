package br.com.unipac.aulasocket.multi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer implements Runnable {
	private static final int SERVER_PORT = 6888;
	static ServerSocket server = null;

	public static void main(String[] args) throws ClassNotFoundException {
		MultiServer multi = new MultiServer();
		Thread server = new Thread(multi);
		server.start();
	}

	private static void server() {
		System.out.println("[Criando Servidor]");
		try {
			// Aqui vamos chamar o servidor socket e passar uma para ele, para que ele possa
			// estar disponivel no host
			server = new ServerSocket(SERVER_PORT);
			System.out.println("[Servidor Operando na porta " + SERVER_PORT + "]");

			// Agora precisamos fazer um loop que seja sempre verdadeiro (loop infiito),
			// para fica aguardando as conexoes do cliente
			while (true) {
				System.out.println("[Aguardando Conexões o cliente]");
				// Nesse ponto quando o cliente pedir uma requisicao de conexao o servidor vai
				// aceita-la
				Socket client = server.accept();
				// Aqui vamos imprimir os dados do cliente conectado
				System.out.println("[Conexão aberta do cliente:" + client.getInetAddress().toString() + "]");

				String message = receiverData(client);
				sendDataToCliente(client, message);

				client.close();
				System.out.println("[Conexão encerrada]");
			}
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println("Erro: " + ex.getMessage());
		}
	}

	private static void sendDataToCliente(Socket client, String message) throws IOException {
		System.out.println("[Ënviando dados]");

		ObjectOutputStream exitData = new ObjectOutputStream(client.getOutputStream());

		exitData.flush(); // Envia o cabeçalho de preparo do outro endpoint
		exitData.writeObject("SOH"); // start of head

		exitData.writeObject("Servidor basico conectado");
		exitData.writeObject("Processando dados..." + message);
		exitData.writeObject("Dados de conexao" + client.toString());
		exitData.writeObject("bye!");

		System.out.println("[Dados Enviados]");
		exitData.writeObject("EOT");
	}

	private static String receiverData(Socket client) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

		// convert ObjectInputStream object to String
		String message = (String) ois.readObject();
		return "Message Received: " + message;
	}

	@Override
	public void run() {
		server();
	}
}

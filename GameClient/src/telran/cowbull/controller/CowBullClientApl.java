package telran.cowbull.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import telran.cowbull.action.CowBullActions;
import telran.cowbull.net.CowBullTcpClietn;
import telran.cowbull.service.CowBullService;
import telran.net.TcpClient;
import telran.net.UdpClient;
import telran.view.*;

public class CowBullClientApl {

	private static int port;
	private static String host;
	private static String protocol;
	private static final String PATH_PROPERTIES = "config_client.properties";


	public static void main(String[] args) {
		try {
			getProperties(PATH_PROPERTIES);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		InputOutput io = new ConsoleInputOutput();
		CowBullService client;
		try {
			client = getCowBullTcpClietn();
			Menu menu = new Menu("Cows and Bolls", CowBullActions.getActions(client, io));
			menu.perform(io);

		} catch (Exception e) {
			io.writeObjectLine(e.toString());
		}

	}

	private static CowBullService getCowBullTcpClietn() throws Exception {
		CowBullService client;
		if(protocol.equals("TCP")) {
			client = new CowBullTcpClietn(new TcpClient(host, port));
		} else {
			client = new CowBullTcpClietn(new UdpClient(host, port));
		}
		return client;
	}
	
	private static Properties getProperties(String pathProperties) throws IOException {
		FileInputStream input = new FileInputStream(pathProperties);
		Properties properties = new Properties();
		properties.load(input);
		System.out.println("version="+properties.getProperty("version"));
		port = 	   Integer.parseInt((String) properties.get("port")) ;
		host = (String) properties.get("host");
		protocol = (String) properties.get("protocol");
		return properties;
	}

}

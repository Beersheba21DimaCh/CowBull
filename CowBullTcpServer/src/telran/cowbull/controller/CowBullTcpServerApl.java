package telran.cowbull.controller;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.*;

import telran.cowbull.action.CowBullServerAction;
import telran.cowbull.net.CowBullProtocol;
import telran.cowbull.service.*;
import telran.net.*;
import telran.view.*;

public class CowBullTcpServerApl {
	
	private static final String PATH_BD = "CowBull.data";
	private static final String PATH_PROPERTIES = "config_server.properties";
	private static int port;
	private static String protocol;
	static CowBullService servise;
	static int portion;
	static int competitionPeriod;
	static int timeOutLimit;
	static JavaServer server;
	static ExecutorService executor;
	
	public static void main(String[] args) {
		
		try {
			getProperties(PATH_PROPERTIES);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		executor = Executors.newFixedThreadPool(2);
		servise = getCowBullService();
		Runnable []action = {
				CowBullTcpServerApl::startServer,
				CowBullTcpServerApl::startAdminMenu
		};
		
		for (int i = 0; i < action.length; i++) {
			executor.submit(action[i]);
		}
		
	}

	private static Properties getProperties(String pathProperties) throws IOException {
		FileInputStream input = new FileInputStream(pathProperties);
		Properties properties = new Properties();
		properties.load(input);
		System.out.println("version="+properties.getProperty("version"));
		port = 	   Integer.parseInt((String) properties.get("port")) ;
		protocol = (String) properties.get("protocol");
		portion = Integer.parseInt((String) properties.get("competition_portion"));
		competitionPeriod = Integer.parseInt((String) properties.get("competition_period"));
		timeOutLimit = Integer.parseInt((String) properties.get("competition_timeout_limit"));
		return properties;
	}

	private static CowBullService getCowBullService() {
		CowBullService res = null;
		try {
			res = (CowBullService) CowBullService.readFile(new File(PATH_BD));
		} catch (Exception e) {
			System.out.println("Unable to find file "+ PATH_BD +" Create new base.");
			res = new CowBullServiceImpl();
		}
		return res;
	}

	private static void startServer() {
		
		try {
			if(protocol.equals("TCP")) {
			server = new TcpJavaServer(port, new CowBullProtocol(servise));
			} else {
			server = new UdpServerJava(port, new CowBullProtocol(servise));
			}
			
			server.run();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	private static void startAdminMenu() {
		InputOutput io = new ConsoleInputOutput();
		Menu menu = new Menu("Administration", CowBullServerAction.getItem(servise, competitionPeriod, timeOutLimit, portion, io));
		menu.perform(io);
		System.out.println("Shutdown server");
		executor.shutdown();
		server.shutdown();
	}
		

}

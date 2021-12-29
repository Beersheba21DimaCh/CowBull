package telran.cowbull.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import telran.cowbull.service.Game;

public class TableWinnersAppl {

	private static final String PATCH = "./winners/competition";

	public static void main(String[] args) {
		
		
		
		try {
			System.out.println(TableWinners.dysplayWinners(PATCH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}

package telran.cowbull.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import telran.cowbull.dto.*;
import telran.cowbull.utils.CowBullUtils;
import telran.cowbull.utils.TableWinners;

public interface CowBullService {

	String FILEPATHBD = "CowBull.data";
	String PATHWINNERS_SINGLE_GAME = "./winners/single";
	String PATHWINNERS_COMPETITION_MODE = "./winners/competition";
	

	
	LocalDateTime getDateTimeCompetition() throws Exception;
	CowBullCode registrationToCompetition (UserCompetition userCompetition) throws Exception;
	Competition getCompetition() throws Exception;

	default CowBullCode pushPlayerToStackCompetition(long userId)throws Exception{
		throw new UnsupportedOperationException();
	}
	default Competition registrationNewCompetition(LocalDateTime time, int period, int timeOutLimit) {
		throw new UnsupportedOperationException();
	}
	boolean isCompetition() throws Exception;
	
	default Game setCurrentGame(long userId) {
		throw new UnsupportedOperationException();
	}
	default CowBullCode cleanCurrentGame(long userId) {
		throw new UnsupportedOperationException();
	}
	Long getGameId(long userId) throws Exception;
	CowBullCode checkUser(long userId) throws Exception;
	CowBullCode checkGame(long userId) throws Exception;
	
	long startNewGame(long userId) throws Exception;

	CowBullCode move(long gameId, String number) throws Exception;

	Iterable<CowsBulls> getCowsBolls(Long currentGameId) throws Exception;

	CowBullCode registration(long userId, String name) throws Exception;

	Game getGames(long gameId) throws Exception;

	User getUser(long userId) throws Exception;

	default Iterable<Game> previousGames( String from, String to) throws Exception {
		File file = new File(PATHWINNERS_SINGLE_GAME);
		if (!file.exists())
			throw new FileNotFoundException("No Games in the specified period");
		List<Game> list = Arrays.stream(file.listFiles()).filter(p -> {
			String[] date = p.getPath().split("_");
			String fileName = date[3] + "_" + date[4] + "_" + date[5] + "_" + date[6] + "_" + date[7];
			return fileName.compareTo(from) >= 0 && fileName.compareTo(to) <= 0;
		}).map(p -> {
			Game res = null;
			try {
				res = (Game) readFile(p);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return res;
		}).toList();
		if (list.size() == 0)
			throw new FileNotFoundException("No Games in the specified period");
		return list;
	}

	default CowBullCode saveBD() throws Exception {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILEPATHBD))) {
			output.writeObject(this);
		}
		return CowBullCode.OK;
	}

	static Object readFile(File filepath) throws Exception {
		Object res = null;
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filepath))) {
			res = reader.readObject();
		}
		return res;
	}
	default String getResultCompetition(LocalDateTime timeStartCompetition) throws Exception {
		var games = TableWinners.getGames(Path.of(PATHWINNERS_COMPETITION_MODE+"/"+CowBullUtils.parseLocalDateTime(timeStartCompetition)));
		return TableWinners.printWinners(games);
	}
		

}

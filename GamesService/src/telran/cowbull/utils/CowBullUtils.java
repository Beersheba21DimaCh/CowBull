package telran.cowbull.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import telran.cowbull.dto.Competition;
import telran.cowbull.dto.CowBullCode;
import telran.cowbull.dto.User;
import telran.cowbull.service.Game;
import telran.cowbull.service.GameCowsBulls;
import telran.view.*;

public class CowBullUtils {

	public static final String DATATIME_FORMAT = "YYYY-MM-dd-HH-mm";
	public static final String PATHWINNERS_SINGLE_GAME = "./winners/single";
	public static final String PATHWINNERS_COMPETITION_MODE = "./winners/competition";

	public static LocalDateTime getDateTime(String msg, InputOutput io) throws Exception {
		io.writeObjectLine("format date time " + DATATIME_FORMAT);
		String read = io.readString(msg);
		String[] arr = read.split("[\\s\\-\\_]+");
		String delimiter = "_";

		if (arr.length < 5)
			throw new Exception("Invalid value ");

		LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(parseInt(arr[0]), parseInt(arr[1]), parseInt(arr[2])),
				LocalTime.of(parseInt(arr[3]), parseInt(arr[4])));
		String res = dateTime.getYear() + delimiter + dateTime.getMonthValue() + delimiter + dateTime.getDayOfMonth()
				+ delimiter + dateTime.getHour() + delimiter + dateTime.getMinute();
		return dateTime;
	}

	private static int parseInt(String string) throws Exception {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			throw new Exception("Input error, should number " + string);
		}
	}

	public static long generateID() {
		return Math.abs(UUID.randomUUID().getMostSignificantBits());
	}

	public static String parseLocalDateTime(LocalDateTime ldt) {
		return ldt.format(DateTimeFormatter.ofPattern(DATATIME_FORMAT));
	}

	
	 public static CowBullCode saveWinnersGame(Game game) throws Exception {
		if(game.isCompetition) {
			saveWinnersGame(game, PATHWINNERS_COMPETITION_MODE+"/"+CowBullUtils.parseLocalDateTime(Competition.timeStart));
		} else {
		saveWinnersGame(game, PATHWINNERS_SINGLE_GAME);
		}
		return CowBullCode.OK;
	}

	private static CowBullCode saveWinnersGame(Game game, String path) throws Exception {
		LocalDateTime date = game.gameEndDate;

		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
		String res = String.format("%s//%d_%d_%s_%d_%d_%d_%d_%d", path,game.gameId ,game.user.userId, game.user.name,
				date.getYear(), date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute());

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(res))) {
			output.writeObject(game);
		}
		return CowBullCode.OK;
		
	}
	
	public static void saveGamesCompetition(List<Game>games) throws IOException {
		for (Game game : games) {
			saveGameCompetition(game);
		}
	}
	
	public static void saveGameCompetition(Game game) throws IOException {
		LocalDateTime date = game.gameEndDate;

		File file = new File("./winners/competition");
		if (!file.exists())
			file.mkdirs();
		String res = String.format("%s//%d_%d_%s_%d_%d_%d_%d_%d", "./winners/competition" ,game.gameId ,game.user.userId, game.user.name,
				date.getYear(), date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute());

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(res))) {
			output.writeObject(game);
		}

	}
	
	

	public static List<Game> createGames(int amountGames, int amountUsers) {
		List<User>users = getUsers(amountUsers);
		
		List<Game> games = Stream.generate(() -> 
		(Game)(new GameCowsBulls(generateID(), getRandomUser(users))))
		.limit(amountGames)
		.toList();
		int maxStep = 50;
		int minStep = 5;
		LocalDateTime endGame = LocalDateTime.now();
		games.forEach(p -> {
			System.out.println(endGame);
			p.step = (int) (Math.random()*(maxStep-minStep+1)) + minStep;
			p.gameEndDate = LocalDateTime.now().plus(p.step, ChronoUnit.MINUTES);

			
			p.isCompetition = true;
		});
		
		return games;
	}

	private static User getRandomUser(List<User> users) {
		return users.get((int) Math.round(Math.random()*(users.size()-1)));
	}

	private static List<User> getUsers(int amount) {
		
		return Stream.generate(() -> new User(generateID(), getRandomName()))
				.limit(amount).toList();
	}

	private static String getRandomName() {
		String[] userNames = { "Abraham", "Yackob", "Yosef", "Moses", "Ahser", "Naftaly" };
		return userNames[(int) Math.round(Math.random()*(userNames.length-1)) ];
	}
	
	public static void clrscr() {
		// Clears Screen in java
		try {
			if (System.getProperty("os.name").contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				Runtime.getRuntime().exec("clear");
		} catch (IOException | InterruptedException ex) {
		}
	}
}

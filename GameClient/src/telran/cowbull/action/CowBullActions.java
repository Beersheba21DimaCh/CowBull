package telran.cowbull.action;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import telran.cowbull.dto.*;
import telran.cowbull.service.*;
import telran.cowbull.utils.CowBullUtils;
import telran.view.*;

public class CowBullActions {

	static CowBullService cowBullService;
	static Long currentUserId;
	static Long currentGameId;

	public static Item[] getActions(CowBullService client, InputOutput io) {
		cowBullService = client;
		Item[] items = {
				Item.of("Registration", CowBullActions::registration),
				Item.of("Authorization", CowBullActions::authorization),
				Item.exit() };
		return items;
	}
	
	private static void authorization(InputOutput io) {
		try {
			long id = getId(io);
			CowBullCode code = cowBullService.checkUser(id);
			io.writeObjectLine(code);
			if(code == CowBullCode.OK) {
				currentUserId = id;
				Menu menu =  new Menu("Game menu", getActionsGameMenu(io));
				menu.perform(io);
			} 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static Item[] getActionsGameMenu(InputOutput io) {
		Item[] items = {
				Item.of("Start game", CowBullActions::startGame),
				Item.of("Previous games", CowBullActions::previousGames),
				new Menu("Competition", CowBullActions.competition(io)),
				Item.of("Rules of the game", CowBullActions::rulesGame),
				Item.exit() };
		return items;
	}

	private static void registration(InputOutput io) {
		try {
			long id = getId(io);
			String name = getName(io);
			CowBullCode code = cowBullService.registration(id, name);
			String res = code == CowBullCode.OK ? "OK"
					: "User with the given ID already exists";

			io.writeObjectLine(res);
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}

	}

	private static void previousGames(InputOutput io) {
		try {
//			Iterable<Game> iter = cowBullService.previousGames(
//				CowBullUtils.parseLocalDateTime(CowBullUtils.getDateTime("Enter datetime from ", io)),	
//				CowBullUtils.parseLocalDateTime(CowBullUtils.getDateTime("Enter datetime to ", io)));	
			Iterable<Game> iter = cowBullService.previousGames("2020_9_17_15_37", "2022_9_17_15_37");
			iter.forEach(p -> {
				io.writeObjectLine(String.format("User name: %s Date time end game: %s Steps: %d Number %s",
						p.getUser().name, p.getGameEndDate(), p.getStep(), p.getNumber()));
				p.getMoveHistory().forEach(e -> io.writeObjectLine(e.toString()));
			});
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	private static void startGame(InputOutput io) {
		try {
			try {
				currentGameId = cowBullService.startNewGame(currentUserId);
		
			} catch (ExceptionDontFinishGame e) {
				io.writeObjectLine(e.getMessage());
				currentGameId = cowBullService.getGameId(currentUserId);
				
			}
			io.writeObjectLine("game № " + currentGameId);
			io.writeObjectLine("find number " + cowBullService.getGames(currentGameId).getNumber());
			displayMove(io, currentGameId);
			enterNumber(io);
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	private static void enterNumber(InputOutput io) {
		if (currentGameId == null) {
			io.writeObjectLine("Game is not active!!");
			return;
		}
		try {
			while(true) {
				String msg = io.readString("Enter numer or press \"exit\" to exit the game");
				if(msg.equals("exit")) {
					break;
				}
				if (move(io, currentGameId, msg) == CowBullCode.GAME_FINISHED) {
					endGame(io, currentGameId, currentUserId);
					return;
				}
				displayMove(io, currentGameId);
			}
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}
	
	private static CowBullCode move(InputOutput io, Long currentGameId, String msg) throws Exception {
		return cowBullService.move(currentGameId, msg);
	}

	private static void endGame(InputOutput io, Long currentGameId, long userId) throws Exception {
		displayMove(io, currentGameId);
		io.writeObjectLine("You win. End Game.");
		currentGameId = null;
	}

	private static void displayMove(InputOutput io, Long currentGameId) throws Exception {
		cowBullService.getCowsBolls(currentGameId).forEach(p -> io.writeObjectLine(p));
	}

	private static String getName(InputOutput io) {
		return io.readString("Enter name");
	}

	private static long getId(InputOutput io) {
		return io.readInt("Enter user id");
	}

	private static void rulesGame(InputOutput io) {
		String msg = "Компьютер задумывает четыре различные цифры из 0,1,2,...9. Игрок делает ходы, чтобы узнать эти цифры и их порядок.\r\n"
				+ "\r\n" + "Каждый ход состоит из четырёх цифр, 0 может стоять на первом месте.\r\n" + "\r\n"
				+ "В ответ компьютер показывает число отгаданных цифр, стоящих на своих местах (число быков) и число отгаданных цифр, стоящих не на своих местах (число коров).\n"
				+ "\r\n"
				+ "Registration, регистрация нового пользователя, нужно ввести уникальный номер пользователя и имя\r\n"
				+ "\r\n" + "Start game, начать новую игру, требуется номер пользователя\r\n" + "\r\n"
				+ "Enter number, введите число, чтобы сделать ход\r\n" + "\r\n"
				+ "Previous games, исторя побед, требуется ввести дату в формате \"YYYY MM DD HH MM\"\r\n";

		io.writeObjectLine(msg);
	}

	private static Item[] competition(InputOutput io) {
		Item[] items = {
				Item.of("Find out the start time of the competition", CowBullActions::getDateTimeBeginning),
				Item.of("Participate in the competition", CowBullActions::participateToCompetition),
				Item.exit(),
				};
		return items;
	}
	
	private static void participateToCompetition(InputOutput io) {
		try {
			LocalDateTime timeStartCompetition = cowBullService.getDateTimeCompetition();
			CowBullCode code = cowBullService.registrationToCompetition(new UserCompetition(currentUserId ,
					timeStartCompetition));
			if(code == CowBullCode.COMPETITION_NOT_EXISTS) {
				io.writeObjectLine("no competition found");
				return;
			}
			String res = code == CowBullCode.OK ? 
					String.format("OK current time = %s, time beginning competition = %s The competition will start in %d sec", 
							CowBullUtils.parseLocalDateTime(LocalDateTime.now()), 
							CowBullUtils.parseLocalDateTime(timeStartCompetition),
							ChronoUnit.SECONDS.between(LocalDateTime.now(), timeStartCompetition))
					: code == CowBullCode.USER_NOT_EXISTS ? "User with the given ID not exsist" : "Local Date Time not correct";
			io.writeObjectLine(res);
			if(code == CowBullCode.OK) {
				processingGame(io, timeStartCompetition);
			}
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}
	
	private static void processingGame(InputOutput io, LocalDateTime timeStartCompetition) throws Exception {
		while(cowBullService.isCompetition()) {
			io.writeObjectLine("Please wait");
			
			while(true) {
				CowBullUtils.clrscr();
				io.writeObjectLine("Please wait " + displayTime(timeStartCompetition) + " sec");
				if(cowBullService.checkGame(currentUserId) ==CowBullCode.OK) {
					currentGameId = cowBullService.getGameId(currentUserId);
					System.out.println("Game="+currentGameId);
				}
				try {
					Thread.sleep(1000);
					if(currentGameId!=null) {
						gameCompetition(io);
						currentGameId = null;
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				if(!cowBullService.isCompetition()) {
					System.out.println(!cowBullService.isCompetition());
					//last step
					String result = cowBullService.getResultCompetition(timeStartCompetition);
					io.writeObjectLine(result);
					break;
				}
			}
	
		}
	}
	
	private static long displayTime(LocalDateTime timeStartCompetition) {
		LocalDateTime now = LocalDateTime.now();
		if(now.isBefore(timeStartCompetition)) {
			return ChronoUnit.SECONDS.between(LocalDateTime.now(), timeStartCompetition);
		} else {
			return ChronoUnit.SECONDS.between(timeStartCompetition, LocalDateTime.now());

		}
	}

	private static void gameCompetition(InputOutput io) {
		try {
			
			while(currentGameId.equals(cowBullService.getGameId(currentUserId))) {
				String msg = io.readString("Enter numer");
	
				if (move(io, currentGameId, msg) == CowBullCode.GAME_FINISHED) {
					endGame(io, currentGameId, currentUserId);
					return;
				}
				displayMove(io, currentGameId);
			}
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	private static void getDateTimeBeginning(InputOutput io) {
		try {
			io.writeObjectLine(CowBullUtils.parseLocalDateTime(cowBullService.getDateTimeCompetition()));
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}
	
	
}

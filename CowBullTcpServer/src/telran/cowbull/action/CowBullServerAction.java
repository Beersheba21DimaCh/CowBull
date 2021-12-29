package telran.cowbull.action;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.DataFormatException;
import telran.cowbull.mode.CowBullServerCompetitionMode;
import telran.cowbull.service.*;
import telran.cowbull.utils.*;
import telran.view.*;

public class CowBullServerAction {
	
	static CowBullService service;
	static int portion;
	static int competitionPeriod;
	static int timeOutLimit;

	public static Item[] getItem(CowBullService service, int competitionPeriod, int timeOutLimit, int portion, InputOutput io) {
		CowBullServerAction.service = service;
		CowBullServerAction.competitionPeriod = competitionPeriod;
		CowBullServerAction.timeOutLimit = timeOutLimit;
		CowBullServerAction.portion = portion;
		return new Item[] { new Menu("Competition",
				new Item[] { Item.of("Сreate a competition, set a start date for the competition", CowBullServerAction::createCompetition),
						Item.of("Сreate a competition in 1 minute", CowBullServerAction::createDefComp),
						Item.exit()
						}), Item.exit() };
	}

	static void createCompetition(InputOutput io) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		io.writeObjectLine("Current time " + currentDateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd-HH-mm")));
		try {
			LocalDateTime localDateTime = CowBullUtils.getDateTime("Enter the start date time of the competition", io);
			if (localDateTime.isBefore(currentDateTime)) {
				throw new DataFormatException(
						"the date time of the start of the competition, must be greater than the current date time");
			}
			System.out.println(service.registrationNewCompetition(localDateTime, competitionPeriod, timeOutLimit));
			Thread thread = new Thread(new CowBullServerCompetitionMode(service, portion));
			thread.start();
		} catch (DataFormatException e) {
			io.writeObjectLine(e.getMessage());
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	static void createDefComp(InputOutput io) {
		LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(1);
		service.registrationNewCompetition(localDateTime, competitionPeriod, timeOutLimit);
		Thread thread = new Thread(new CowBullServerCompetitionMode(service, portion));
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("error to createDefComp " + e.getMessage());
		}
	}

}

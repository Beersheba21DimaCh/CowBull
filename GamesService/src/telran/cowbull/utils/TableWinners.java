package telran.cowbull.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import telran.cowbull.dto.User;
import telran.cowbull.service.CowBullService;
import telran.cowbull.service.Game;
import telran.view.InputOutput;

public class TableWinners {

	
	public static String dysplayWinners(String path) throws FileNotFoundException, ClassNotFoundException, IOException {
//		List<Game> games = getGames("./winners/competition");
		List<Path> files = Files.list(Paths.get(path)).toList();
		StringBuilder builder = new StringBuilder();
		for (Path namePath : files) {
			String[] paths = namePath.toString().split("[\\\\]");
			
			builder.append("\n\n********      Date of competition: " + paths[paths.length-1]+"     **************\n");
			List<Game> games = getGames(namePath);
			builder.append(printWinners(games));
		}
		return builder.toString();
	}
	
	

	public static String printWinners(List<Game> games) {

		Map<User, Long> mapUserGames = games.stream()
				.collect(Collectors.groupingBy(p -> p.getUser(), Collectors.counting()));
		Map<User, Integer> mapUserSteps = games.stream()
				.collect(Collectors.groupingBy(p -> p.getUser(), Collectors.summingInt(p -> p.step)));


		List<Entry<User, Long>> listWin = mapUserGames.entrySet().stream().sorted((e1, e2) -> {
			return e2.getValue().compareTo(e1.getValue()) != 0 ? e2.getValue().compareTo(e1.getValue())
					: mapUserSteps.get(e2.getKey()).compareTo(mapUserSteps.get(e1.getKey()));
		}).toList();

		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("%-7s %-20s %-10s %-14s %-14s\n", "Postion", "User id", "Name", "Number of wins",
				"number of steps"));
		builder.append("-".repeat(70)+"\n");
		int pos = 1;

		for (int i = 0; i < listWin.size(); i++) {
			var entry = listWin.get(i);
			builder.append(String.format("%-7d %-20d %-10s %-14d %-14d\n", pos, entry.getKey().userId, entry.getKey().name,
					entry.getValue(), mapUserSteps.get(entry.getKey())));

			if (i < listWin.size()-1) {
				var entryNext = listWin.get(i + 1);
				if (entry.getValue() == entryNext.getValue()
						&& mapUserSteps.get(entry.getKey()) == mapUserSteps.get(entryNext.getKey())) {
				} else {
					pos++;
				}
			}
		}
		builder.append("-".repeat(70)+"\n");
		
		return builder.toString();


	}

	public static List<Game> getGames(Path path) throws FileNotFoundException, IOException, ClassNotFoundException {
		List<Game> list = new LinkedList<>();
		
//			File[] files = new File(path).listFiles();
			File[] files = path.toFile().listFiles();
			for (File file : files) {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
				Game game = (Game) input.readObject();
				list.add(game);
			}
		
		return list;
	}

}

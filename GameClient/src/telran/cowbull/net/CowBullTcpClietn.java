package telran.cowbull.net;

import telran.cowbull.dto.*;
import telran.cowbull.service.*;
import telran.cowbull.utils.CowBullUtils;
import telran.cowbull.utils.TableWinners;
import telran.net.JavaClient;
import telran.net.TcpJavaClient;
import static telran.cowbull.api.RequestCowBullApi.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class CowBullTcpClietn implements CowBullService {
	
	JavaClient client;

	public CowBullTcpClietn(JavaClient client) throws Exception {
		this.client = client;
	}

	@Override
	public long startNewGame(long userId) throws Exception {
		return client.send(START_GAME, userId);
	}

	@Override
	public CowBullCode move(long gameId, String number) throws Exception {

		return client.send(MOVE, new MoveStep(gameId, number));
	}

	@Override
	public CowBullCode registration(long userId, String name) throws Exception {
		return client.send(REGISTER, new User(userId, name));
	}

	@Override
	public Iterable<Game> previousGames(String from, String to) throws Exception {
		return client.send(GET_PREVIOUS_GAME, new PreviousGame(from, to));

	}

	@Override
	public CowBullCode saveBD() throws Exception {
		return client.send(SAVE_GAME, "");
	}

	@Override
	public Game getGames(long gameId) throws Exception {
		return client.send(GET_GAME, gameId);
	}

	@Override
	public User getUser(long userId) throws Exception {
		return client.send(GET_USER, userId);
	}

	@Override
	public Iterable<CowsBulls> getCowsBolls(Long gameId) throws Exception {

		return client.send(GET_COWBULLS, gameId);
	}

	@Override
	public CowBullCode registrationToCompetition(UserCompetition userCompetition) throws Exception {
		return client.send(COMPETITION_PLAYER_REGISTRATION, userCompetition);
	}

	@Override
	public LocalDateTime getDateTimeCompetition() throws Exception {
		return client.send(COMPETITION_GET_DATETIME_BEGINNING, "");
	}



	@Override
	public Competition getCompetition() throws Exception {
		return client.send(COMPETETION_GET_COMPETETION, "");
	}

	@Override
	public Long getGameId(long userId) throws Exception {
		return client.send(GET_GAME_ID, userId);
	}


	@Override
	public boolean isCompetition() throws Exception {
		return client.send(COMPETETION_IS_COMPETETION, "");
	}

	@Override
	public CowBullCode checkUser(long userId) throws Exception {
		return client.send(CHECK_USER, userId);
		}

	@Override
	public CowBullCode checkGame(long userId) throws Exception {
		return client.send(CHECK_GAME, userId);
	}
	
	public String getResultCompetition(LocalDateTime timeStartCompetition) throws Exception {
		return client.send(COMPETETION_GET_RESULT, timeStartCompetition);
		
	}


}

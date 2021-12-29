package telran.cowbull.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import telran.cowbull.dto.*;
import telran.cowbull.utils.CowBullUtils;

public class CowBullServiceImpl implements CowBullService, Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<Long, User> users = new HashMap<>();
	private HashMap<Long, Game> mapIdgames = new HashMap<>();
	private Competition competition;

	@Override
	public long startNewGame(long userId) throws Exception {
		User user = users.get(userId);
		Game game;
		if (user == null)
			throw new Exception("User with the given ID does not exist");
		if (user.getCurrentGame() != null) {
			game = user.getCurrentGame();
			throw new ExceptionDontFinishGame("User in not finished game");
		} else {
			game = setCurrentGame(user.userId);
		}
		return game.getGameId();
	}

	@Override
	public CowBullCode move(long gameId, String number) throws Exception {
		CowBullCode res = CowBullCode.GAME_NOT_FINISHED;
		try {
			if(mapIdgames.get(gameId).step(number)) {
				endGame(mapIdgames.get(gameId).user.userId, gameId);
				res = CowBullCode.GAME_FINISHED;
			}
			saveBD();
		} catch (Exception e) {
			res = CowBullCode.GAME_NOT_EXISTS;
			throw new Exception("Game is not active~");
		}
		return res;
	}

	@Override
	public CowBullCode registration(long userId, String name) throws Exception {
		CowBullCode code = addUser(new User(userId, name));
		saveBD();
		return code;
	}

	private CowBullCode addUser(User user) {
		User res = users.putIfAbsent(user.userId, user);
		if (res != null)
			return CowBullCode.USER_EXSITS;
		return CowBullCode.OK;
	}

	@Override
	public Game getGames(long gameId) {
		return mapIdgames.get(gameId);
	}

	@Override
	public User getUser(long userId) {
		return users.get(userId);
	}

	@Override
	public Iterable<CowsBulls> getCowsBolls(Long gameId) throws Exception {
		return mapIdgames.get(gameId).getMoveHistory();
	}

	@Override
	public CowBullCode registrationToCompetition(UserCompetition userCompetition) throws Exception {
		CowBullCode res = null;
		if (!competition.registration) {
			return CowBullCode.COMPETITION_NOT_EXISTS;
		}
		User user = users.get(userCompetition.userId);
		if(user!=null) {
		res = competition.addPlayer(user);
		}
		return res != null ? CowBullCode.OK : CowBullCode.USER_NOT_EXISTS;
	}

	@Override
	public LocalDateTime getDateTimeCompetition() throws Exception {
		
		if(competition == null) {
			throw new Exception("Competitions not found");
		}
		return  competition.timeStart;
	}

	@Override
	public Competition registrationNewCompetition(LocalDateTime time, int period, int timeOutLimit) {
		competition = new Competition(time, period, timeOutLimit);
		competition.registration = true;
		competition.isCompetition = true;
		return competition;
	}

	@Override
	public Competition getCompetition() {
		return competition;
	}


	@Override
	public Game setCurrentGame(long userId) {
		User user = users.get(userId);
		Game game = new GameCowsBulls(CowBullUtils.generateID(), user);
		mapIdgames.put(game.getGameId(), game);
		user.setCurrentGame(game);
		return game;
	}


	public CowBullCode cleanCurrentGame(long userId) {
		System.out.println("clear game to " + userId);
		User user = users.get(userId);
		user.removeGame();
		return CowBullCode.OK;
	}
	
	private CowBullCode endGame(long userId, long gameId) throws Exception {
		CowBullCode res = null;
		try {
		User user = users.get(userId);
		CowBullUtils.saveWinnersGame(getGames(gameId));
		user.removeGame();
		saveBD();
		res = CowBullCode.OK;
		} catch (Exception e) {
			System.out.println("error end game " + e.getMessage());
		}
		return res != null ? res : CowBullCode.USER_NOT_EXISTS;
	}

	@Override
	public Long getGameId(long userId) {
		User user = users.get(userId);
		Game game = user.getCurrentGame();
		return game != null ? game.gameId : null;
	}

	@Override
	public CowBullCode pushPlayerToStackCompetition(long userId) throws Exception {
		System.out.println("addToPlayerCompetition");
		return competition.addPlayer(users.get(userId));
		
	}

	@Override
	public boolean isCompetition() throws Exception {
		return competition.isCompetition;
	}

	@Override
	public CowBullCode checkUser(long userId) {
		return users.containsKey(userId) ? CowBullCode.OK : CowBullCode.USER_NOT_EXISTS;
	}

	@Override
	public CowBullCode checkGame(long userId) throws Exception {
		User user = users.get(userId);
		if(user==null) {
			return CowBullCode.USER_NOT_EXISTS;
		} 
		return user.getCurrentGame()!=null ? CowBullCode.OK : CowBullCode.GAME_NOT_EXISTS;
	}
}

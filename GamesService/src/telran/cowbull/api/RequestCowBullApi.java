package telran.cowbull.api;

public interface RequestCowBullApi {
	String START_GAME = "cowbull/start/newGame";
	String GET_PREVIOUS_GAME = "cowbull/get/previousGames";
	String MOVE = "cowbull/move";
	String REGISTER = "cowbull/registration";
	String SAVE_GAME = "cowbull/save/game";
	String SAVE_WINNERS = "cowbull/save/winners";
	String GET_USER = "cowbull/get/user";
	String GET_GAME = "cowbull/get/game";
	String CHECK_USER = "cowbull/check/user";
	String CHECK_GAME = "cowbull/check/game";
	String FINISH_GAME = "cowbull/finish/game";
	String GET_COWBULLS = "cowbull/get/cowulls";
	String COMPETITION_PLAYER_REGISTRATION = "cowbull/competition/registration";
	String COMPETITION_CREATE_NEW = "cowbull/competition/create";
	String COMPETITION_GET_DATETIME_BEGINNING = "cowbull/competition/get/datetime";
	String COMPETETION_GET_COMPETETION = "cowbull/competetion/get";
	String GET_GAME_ID = "cowbull/get/gameId";
	String COMPETETION_IS_COMPETETION = "cowbull/get/iscompetetion";
	String COMPETETION_GET_RESULT = "competition/get/result";
}

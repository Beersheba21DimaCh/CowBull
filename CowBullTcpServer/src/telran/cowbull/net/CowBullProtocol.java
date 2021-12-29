package telran.cowbull.net;

import telran.cowbull.dto.*;
import telran.cowbull.service.CowBullService;
import telran.cowbull.service.Game;
import telran.net.ApplProtocolJava;
import telran.net.dto.*;
import static telran.cowbull.api.RequestCowBullApi.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class CowBullProtocol implements ApplProtocolJava {

	CowBullService service;

	public CowBullProtocol(CowBullService cowBullServiceImpl) {
		service = cowBullServiceImpl;
	}

	@Override
	public ResponseJava getResponse(RequestJava request) {

		switch (request.requestType) {
		case START_GAME: {
			return _cowbull_start_newGame(request.date);
		}
		case GET_PREVIOUS_GAME: {
			return _cowbull_get_previousGame(request.date);
		}
		case MOVE: {
			return _cowbull_move(request.date);
		}
		case REGISTER: {
			return _cowbull_registration(request.date);
		}
		case GET_GAME: {
			return _cowbull_get_game(request.date);
		}
		case GET_USER: {
			return _cowbull_get_user(request.date);
		}
		case SAVE_GAME: {
			return _cowbull_save_game(request.date);
		}
//		case SAVE_WINNERS: {
//			return _cowbull_save_winners(request.date);
//		}
		case GET_COWBULLS: {
			return _cowbull_get_cowbulls(request.date);
		}
		case COMPETITION_GET_DATETIME_BEGINNING: {
			return _cowbull_competition_get_datetime(request.date);
		}
		case COMPETITION_PLAYER_REGISTRATION: {
			return _cowbull_competition_registration(request.date);
		}
		case COMPETETION_GET_COMPETETION: {
			return _cowbull_get_competition(request.date);
		}
		case GET_GAME_ID: {
			return _cowbull_get_gameId(request.date);
		}
		case COMPETETION_IS_COMPETETION: {
			return _cowbull_get_iscompetetion(request.date);
		}
		case CHECK_USER: {
			return _cowbull_check_user(request.date);
		}
		case CHECK_GAME: {
			return _cowbull_check_game(request.date);
		}
		case COMPETETION_GET_RESULT: {
			return _cowbull_competition_get_result(request.date);
		}
		default:
			return new ResponseJava(ResponseCode.WRONG_REQUEST_TYPE, request.requestType);
		}

	}

	private ResponseJava _cowbull_competition_get_result(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.getResultCompetition((LocalDateTime) date));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_check_game(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.checkGame((long) date));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_check_user(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.checkUser((long) date));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_get_iscompetetion(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.isCompetition());
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_get_competition(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.getCompetition());
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_competition_registration(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.registrationToCompetition((UserCompetition) date));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_competition_get_datetime(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.getDateTimeCompetition());
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_get_cowbulls(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, new LinkedList<CowsBulls>( (Collection<CowsBulls>) service.getCowsBolls((Long)date)));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

//	private ResponseJava _cowbull_save_winners(Serializable date) {
//		try {
//			return new ResponseJava(ResponseCode.OK, service.saveWinnersGame((Long)date));
//		} catch (Exception e) {
//			return getWrongRequestDate(e);
//		}
//	}

	private ResponseJava _cowbull_save_game(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.saveBD());
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_get_user(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.getUser((Long)date));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_get_game(Serializable date) {
		try {
			return new ResponseJava(ResponseCode.OK, service.getGames((Long)date) );
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_registration(Serializable date) {
		User user = (User) date;
		try {
			return new ResponseJava(ResponseCode.OK, service.registration(user.userId, user.name));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_move(Serializable date) {
		MoveStep moveStep = (MoveStep) date;
		try {
			return new ResponseJava(ResponseCode.OK, service.move(moveStep.gameId, moveStep.number));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_get_previousGame(Serializable date) {
		PreviousGame prvGame = (PreviousGame) date;
		try {
			return new ResponseJava(ResponseCode.OK, new LinkedList<Game>(
					(Collection<Game>) service.previousGames(prvGame.from, prvGame.to)));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava _cowbull_start_newGame(Serializable date) {
		Long userId = (Long) date;
		try {
			return new ResponseJava(ResponseCode.OK, service.startNewGame(userId));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}
	private ResponseJava _cowbull_get_gameId(Serializable date) {
		Long userId = (Long) date;
		try {
			return new ResponseJava(ResponseCode.OK, service.getGameId(userId));
		} catch (Exception e) {
			return getWrongRequestDate(e);
		}
	}

	private ResponseJava getWrongRequestDate(Exception e) {
		return new ResponseJava(ResponseCode.WRONG_REQUEST_DATA, e);
	}

}

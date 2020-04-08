package model;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import exception.IdenticalPseudoException;
import exception.TooMuchCharException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * This class manages the whole game.
 * @author ArRaLo
 * @see model.Deck
 * @see model.Question
 * @see model.Player
 */
public class Game {
	
	private List<Deck> decks;
	private List<Player> players;
	private int currentPlayer;
	private List<Deck> usedDecks;
	private int currentQuestion;
	private static Game instance;
	
	/**
	 * Constuctor.
	 * Private constructor so you can only create one instance of the class.
	 */
	private Game() {
		this.decks = new ArrayList<>();
		this.players = new ArrayList<>();
		this.usedDecks = new ArrayList<>();
	}
	
	/**
	 * The only way to get an instance of {@link Game}. 
	 * This instance will be initialize with available decks in the default directory.
	 * If you only need an instance of {@link Game}, you can still use removeAllDecks().
	 * @return {@link Game} instance. The only one instance of the class {@link Game}.
	 */
	public static Game getInstance() {
		if(instance==null) {
			instance = new Game();
			instance.addAllDeck();
		}
		return instance;
	}
	
	/**
	 * Reset the {@link Game} instance.
	 */
	public void reset() {
		instance = new Game();
	}
	
	/**
	 * Get the number of the decks available in the current game.
	 * @return {@link Integer}. The number of decks.
	 */
	public int getNumberOfDecks() {
		return decks.size();
	}
	
	/**
	 * Get the number of the players in the current game.
	 * @return {@link Integer}. The number of players.
	 */
	public int getNumberOfPlayers() {
		return players.size();
	}
	
	
	/**
	 * Gives a <code>pseudo-random</code> {@link List} of {@link Deck} from the current {@link Game} decks with a given size.
	 * @param nb : {@link Integer}. The <code>size-1</code> of the returned {@link List}.
	 * As it is made to play the game, it will give <code>nb</code> elements instead of <code>nb-1</code> elements.
	 * @return {@link List}<{@link Deck}>.
	 */
	public List<Deck> randomChoice(int nb){
		if(decks.size()<=nb) return null;
		List<Deck> ret = new ArrayList<>();
		List<Deck> tmp = this.getDecks();
		Random rand = new Random();
		for(int i=0;i<=nb;i++) {
			int index = rand.nextInt(tmp.size());
			ret.add(tmp.get(index));
			tmp.remove(index);
		}
		return ret;
	}
	
	
	public String getClues(int index) {
		return getUsingDeck().getQuestion(currentQuestion).getClues().get(index);
	}
	
	/**
	 * Checks if the deck d is finished,
	 * i.e. if the last question displayed is the last in the deck.
	 * @param d : {@link Deck}. 
	 * @return {@link Boolean}. <code>true</code> if finished.
	 */
	public boolean isFinished(Deck d) {
		return getCurrentQuestion() >= d.getSizeQuestions()-1 || getPlayer(getCurrentPlayer()).getScore() == 4;
	}
	
	/**
	 * Checks if the current game is finished,
	 * i.e. if all players have already played.
	 * @return {@link Boolean}. <code>true</code> if finished
	 */
	public boolean isFinished() {
		return getCurrentPlayer() >= getNumberOfPlayers() - 1;
	}
	
	
	/**
	 * 
	 * @param playerAnswer : {@link String}. The player input text.
	 * @return
	 */
	public boolean isRightAnswer(String playerAnswer) {
		return getUsingDeck().getQuestion(getCurrentQuestion()).getAnswer().equalsIgnoreCase(playerAnswer.trim());
	}
	
	/**
	 * Add 1 point in the score of the current player.
	 * @return {@link Boolean}. <code>true</code> if well added.
	 */
	public boolean addPoint() {
		return getPlayer().addPoint();
	}
	/**
	 * Defines if a {@link Deck} has already be used in the game,
	 * i.e. if any players has already been asked about this {@link Deck} d.
	 * @param d : {@link Deck}. The deck to check.
	 * @return {@link Boolean}. <code>true</code> if the deck has been used.
	 */
	public boolean hasBeenUsed(Deck d) {
		return usedDecks.contains(d);
	}
	
	/**
	 * Sort the players according to their score and their time.
	 * @see PlayersComparator
	 */
	public void sortPlayers() {
		Collections.sort(players, new PlayersComparator());
	}
	
	// JSON methods
	
	
	/**
	 * Allows to save all the decks in the current {@link Game}.
	 * Decks will be saved in json format file.
	 * @return {@link Boolean}. <code>true</code> when decks have been saved, <code>false</code> if any error occurred.
	 * @see model.Deck.toJson
	 */
	public boolean saveAllDecks() {
		for(Deck d : decks) {
			try {
				File f = new File("./src/resources/questions/deck_" + d.getTheme() + ".json");
				f.createNewFile();
				Deck.toJson(d, f);
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public boolean saveAllPlayers() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date d = new Date();
		String cleanDate = df.format(d);
		for(Player p : players) {
			try {
				File f = new File("./src/resources/user_scores/score_" + cleanDate + ".json");
				f.createNewFile();
				Player.toJson(p, f);
				//TODO Only the last in the list will be written atm (as we write multiple times the same file).
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	
	
	// ************
	// CRUD methods
	// ************
	
	
	
	// On decks
	
	
	
	/**
	 * Allows to add a {@link Deck} in the current {@link Game}.
	 * @param d : {@link Deck}. The {@link Deck} to add
	 * @return {@link Boolean}. <code>true</code> if the {@link Deck} is well added.
	 */
	public boolean addDeck(Deck d) {
		if(!decks.contains(d)) {
			return decks.add(d.clone());
		}
		return false;
	}
	
	/**
	 * Allows to read a json format file which contains a deck and add it to the game.
	 * @param f : {@link File}. The json file to read.
	 * @return {@link Boolean}. <code>true</code> if the {@link Deck} is well created and added to the {@link Game}.
	 */
	public boolean addDeck(File f) {
		Deck d = Deck.fromJson(f);
		return this.addDeck(d.clone());
	}
	
	/**
	 * Read all .json {@link File} in a predefined directory, convert them into {@link Deck}.
	 * All created decks will be added in the current {@link Game}
	 */
	public void addAllDeck() {
		for(File f : new File("./src/resources/questions").listFiles()) {
			this.addDeck(f);
		}
	}
	
	/**
	 * Allows to add decks from a {@link List}.
	 * @param newDecks : {@link List}<{@link Deck}>. Decks to be added.
	 */
	public void addAllDeck(List<Deck> newDecks) {
		for(Deck d : newDecks) {
			this.addDeck(d);
		}
	}
	
	/**
	 * Allows to remove a {@link Deck} from the Game.
	 * @param d : {@link Deck}. The {@link Deck} to remove.
	 * @return {@link Boolean}. <code>true</code> if the {@link Deck} is well removed.
	 */
	public boolean removeDeck(Deck d) {
		if(decks.contains(d)) {
			decks.remove(d);
			return true;
		}
		return false;
	}
	
	/**
	 * Allows to remove a {@link Deck} at a specific index.
	 * @param index : {@link Integer}. The index of the {@link Deck} to remove.
	 * @return {@link Boolean}. <code>true</code> if the {@link Deck} is well removed.
	 */
	public boolean removeDeck(int index) {
		if(decks.size()>index && index>=0) {
			decks.remove(index);
			return true;
		}
		return false;
	}
	
	/**
	 * Allows to remove all the {@link Deck} in the current {@link Game}.
	 */
	public void removeAllDecks() {
		decks.clear();
	}
	
	/**
	 * Allows to update all decks from the current game into a new list of decks.
	 * This will remove all decks available and replace them with the specified new decks.
	 * @param newDecks : {@link List}<{@link Deck}>. Decks to be added.
	 */
	public void updateAllDecks(List<Deck> newDecks) {
		if(newDecks == null || newDecks.isEmpty()) return;
		this.removeAllDecks();
		this.addAllDeck(newDecks);
	}
	
	/**
	 * Get a cloned list of decks included in the current {@link Game} decks.
	 * @return {@link List}<{@link Deck}>.
	 */
	public List<Deck> getDecks() {
		List<Deck> ret = new ArrayList<>();
		for(Deck d : decks) {
			ret.add(d.clone());
		}
		return ret;
	}
	
	/**
	 * Get a specific {@link Deck} d.
	 * @param d : {@link Deck}. The {@link Deck} to be returned.
	 * @return {@link Deck}. Return the {@link Deck} d if found or <code>null</code> if not.
	 */
	public Deck getDeck(Deck d) {
		if(!decks.contains(d)) return null;
		return decks.get(decks.indexOf(d)).clone();
	}
	
	/**
	 * Get a {@link Deck} from a specific theme.
	 * @param theme {@link String}. The theme of the {@link Deck} to be returned.
	 * @return {@link Deck}. Return the {@link Deck} d if found or <code>null</code> if not.
	 */
	public Deck getDeck(String theme) {
		for(Deck in : decks) {
			if(in.getTheme().equalsIgnoreCase(theme)) {
				return in.clone();
			}
		}
		return null;
	}
	
	/**
	 * Get a {@link Deck} at a specific position.
	 * @param index : {@link Integer}. The position of the {@link Deck} to be returned.
	 * @return {@link Deck}. Return the {@link Deck} d if found or <code>null</code> if not.
	 */
	public Deck getDeck(int index) {
		if(index<0 || index>=decks.size()) return null;
		return decks.get(index);
	}
	
	
	
	// On players
	
	
	
	/**
	 * Allows to add a {@link Player} in the current {@link Game}.
	 * @param p : {@link Player}. The {@link Player} to add
	 * @return {@link Boolean}. <code>true</code> if the {@link Player} is well added.
	 * @throws IdenticalPseudoException if pseudo already in the current game.
	 */
	public boolean addPlayer(Player p) throws IdenticalPseudoException {
		if(players.contains(p)) throw new IdenticalPseudoException(p.getPseudo());
		return players.add(p);
	}
	
	/**
	 * Allows to add a {@link Player} in the current {@link Game} by giving only a {@link String} pseudo.
	 * @param pseudo : {@link String}. The pseudo of the {@link Player} to add
	 * @return {@link Boolean}. <code>true</code> if the {@link Player} is well added.
	 * @throws IdenticalPseudoException if pseudo already in the current game.
	 * @throws TooMuchCharException 
	 */
	public boolean addPlayer(String pseudo) throws IdenticalPseudoException, TooMuchCharException {
		Player p = new Player(pseudo);
		return addPlayer(p);
	}
	
	/**
	 * Allows to add multiple players from a list.
	 * @param newPlayers : {@link List}<{@link Player}>. Players to be added.
	 * @return {@link Boolean}. <code>true</code> if players have been well added.
	 * @throws IdenticalPseudoException if pseudo already in the current game.
	 */
	public void addAllPlayers(List<Player> newPlayers) throws IdenticalPseudoException {
		for(Player p : newPlayers) {
			this.addPlayer(p);
		}
	}
	
	/**
	 * Allows to remove a {@link Player} from the Game.
	 * @param p : {@link Player}. The {@link Player} to remove.
	 * @return {@link Boolean}. <code>true</code> if the {@link Player} is well removed.
	 */
	public boolean removePlayer(Player p) {
		if(players.contains(p)) {
			players.remove(p);
			return true;
		}
		return false;
	}
	
	/**
	 * Allows to remove a {@link Player} at a specific index.
	 * @param index : {@link Integer}. The index of the {@link Player} to remove.
	 * @return {@link Boolean}. <code>true</code> if the {@link Player} is well removed.
	 */
	public boolean removePlayer(int index) {
		if(players.size()>index && index>=0) {
			players.remove(index);
			return true;
		}
		return false;
	}
	
	/**
	 * Allows to remove all the {@link Player} in the current {@link Game}.
	 * @return {@link Boolean}. <code>true</code> when all players have been removed.
	 */
	public boolean removeAllPlayers() {
		for(Player p : players) {
			removePlayer(p);
		}
		return true;
	}
	
	/**
	 * Get a cloned list of players included in the current {@link Game} decks.
	 * @return {@link List}<{@link Player}>.
	 */
	public List<Player> getPlayers() {
		List<Player> ret = new ArrayList<>();
		for(Player p : players) {
			ret.add(p.clone());
		}
		return ret;
	}
	
	/**
	 * Get a specific {@link Player} p.
	 * @param p : {@link Player}. The {@link Player} to be returned.
	 * @return {@link Player}. Return the {@link Player} p if found or <code>null</code> if not.
	 */
	public Player getPlayer(Player p) {
		if(!players.contains(p)) return null;
		return players.get(players.indexOf(p));
	}
	
	/**
	 * Get a {@link Player} from a specific pseudo.
	 * @param pseudo {@link String}. The pseudo of the {@link Player} to be returned.
	 * @return {@link Player}. Return the {@link Player} p if found or <code>null</code> if not.
	 */
	public Player getPlayer(String pseudo) {
		for(Player in : players) {
			if(in.getPseudo().equalsIgnoreCase(pseudo)) {
				return in;
			}
		}
		return null;
	}
	
	/**
	 * Get a {@link Player} from a specific position.
	 * @param index {@link Integer}. The position of the {@link Player} to be returned.
	 * @return {@link Player}. Return the {@link Player} p if found or <code>null</code> if not.
	 */
	public Player getPlayer(int index) {
		if(index<0 || index>=players.size()) return null;
		return players.get(index);
	}
	
	
	/**
	 * Get the current {@link Player}.
	 * This method gives the player supposed to be playing at the moment.
	 * @return {@link Player}.
	 */
	public Player getPlayer() {
		return getPlayer(getCurrentPlayer());
	}
	
	// On usedDecks -> Decks already used or being used
	
	
	
	/**
	 * Takes a deck available in the current ( i.e. in decks ) and add it to usedDecks.
	 * @param d : {@link Deck}. The {@link Deck} to add.
	 * @return {@link Boolean}. <code>true</code> if found and added, <code>false</code> otherwise.
	 */
	public boolean addUsedDeck(Deck d) {
		return usedDecks.add(this.getDeck(d));
	}
	
	/**
	 * Takes a deck available in the current ( i.e. in decks ) and add it to usedDecks.
	 * @param theme : {@link String}. The theme of the {@link Deck} to add.
	 * @return {@link Boolean}. <code>true</code> if found and added, <code>false</code> otherwise.
	 */
	public boolean addUsedDeck(String theme) {
		setCurrentQuestion(0);
		return usedDecks.add(this.getDeck(theme));
	}
	
	/**
	 * Get a specific {@link Deck} d in usedDecks.
	 * @param d : {@link Deck}. The {@link Deck} to be returned.
	 * @return {@link Deck}. Return the {@link Deck} d if found or <code>null</code> if not.
	 */
	public Deck getUsedDeck(Deck d) {
		if(!usedDecks.contains(d)) return null;
		return usedDecks.get(usedDecks.indexOf(d)).clone();
	}
	
	/**
	 * Get a {@link Deck} d from a specific theme in usedDecks.
	 * @param theme {@link String}. The theme of the {@link Deck} to be returned.
	 * @return {@link Deck}. Return the {@link Deck} d if found or <code>null</code> if not.
	 */
	public Deck getUsedDeck(String theme) {
		for(Deck in : usedDecks) {
			if(in.getTheme().equalsIgnoreCase(theme)) {
				return in.clone();
			}
		}
		return null;
	}
	
	/**
	 * Get a {@link Deck} d at a specific position in usedDecks.
	 * @param index : {@link Integer}. The position of the {@link Deck} to be returned.
	 * @return {@link Deck}. Return the {@link Deck} d if found or <code>null</code> if not.
	 */
	public Deck getUsedDeck(int index) {
		if(usedDecks.isEmpty() || index<0 || index>=usedDecks.size()) return null;
		return usedDecks.get(index).clone();
	}
	
	/**
	 * Get the last {@link Deck} d of the usedDecks which is the one played at this moment.
	 * @return {@link Deck}. Return the {@link Deck} d.
	 */
	public Deck getUsingDeck() {
		return getUsedDeck(usedDecks.size()-1);
	}
	
	
	
	// Basic getters and setters
	
	
	
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(int currentPlayer) {
		if(currentPlayer>=0 && currentPlayer<=getNumberOfPlayers()) this.currentPlayer = currentPlayer;
	}
	public void nextCurrentPlayer() {
		setCurrentPlayer(getCurrentPlayer()+1);
	}
	public int getCurrentQuestion() {
		return currentQuestion;
	}
	public void setCurrentQuestion(int currentQuestion) {
		if(currentQuestion>=0) this.currentQuestion = currentQuestion;
	}
	public void nextCurrentQuestion() {
		setCurrentQuestion(getCurrentQuestion()+1);
	}
	//Basic methods
	@Override
	public String toString() {
		String s="";
		for(Deck d : decks) {
			s += d.toString() + "---------\n"; 
		}
		return s;
	}
}
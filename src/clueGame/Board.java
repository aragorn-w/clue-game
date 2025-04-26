/*
 * Class: Board
 *
 * Purpose: The Board class is a singleton that represents the game board.
 *
 * Responsibilities: The Board class is responsible for loading the layout and setup config files, initializing the board, and storing the grid of cells and room map. It is also responsible for calculating the adjacency lists of each cell, dealing the cards, and calculating the targets for a player. The Board class is also responsible for storing the number of rows and columns in the grid, the walkway and unused initials, and the layout and setup config file paths. The Board class is also responsible for storing the targets and visited cells for a player.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 14, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private static final String
		ROOM_TYPE_LABEL = "Room",
		SPACE_TYPE_LABEL = "Space",
		PERSON_TYPE_LABEL = "Person",
		WEAPON_TYPE_LABEL = "Weapon",
		WALKWAY_LABEL = "Walkway",
		UNUSED_LABEL = "Unused";

	private static Board theInstance;

	private char walkwayInitial;
	private char unusedInitial;

	private int numRows;
	private int numCols;

	private List<List<BoardCell>> grid;

	private String layoutConfigFile;
	private String setupConfigFile;

	private Map<Character, Room> roomMap;

	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	private List<Player> players;

	private Player humanPlayer;

	private List<Card> nonAnswerCards;

	private Solution theAnswer;

	public Board() {
		super();
	}

	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
			calcAdjLists();
		} catch (BadConfigFormatException | FileNotFoundException exception) {
			System.err.println("Initialization failed: " + exception.getMessage());
		}
	}

	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		roomMap = new HashMap<>();
		
		List<Card> roomCards = new ArrayList<>();
		List<Card> playerCards = new ArrayList<>();
		List<Card> weaponCards = new ArrayList<>();
		
		players = new ArrayList<>();
		nonAnswerCards = new ArrayList<>();
		theAnswer = new Solution();

		File setupFile = new File(setupConfigFile);
		try (Scanner scanner = new Scanner(setupFile)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.substring(0, 2).equals("//")) {
					continue;
				}
				
				try {
					String[] markerInfo = line.split(", ");
					String infoType = markerInfo[0];
					Card card = null;
					switch (infoType) {
						case ROOM_TYPE_LABEL -> {
							String roomLabel = markerInfo[1];
							card = new Card(roomLabel, CardType.ROOM);
							roomCards.add(card);
							char initial = markerInfo[2].charAt(0);
							roomMap.put(initial, new Room(roomLabel));
							nonAnswerCards.add(card);
						}
						case SPACE_TYPE_LABEL -> {
							String roomLabel = markerInfo[1];
							char initial = markerInfo[2].charAt(0);
							if (roomLabel.equals(WALKWAY_LABEL)) {
								walkwayInitial = initial;
							} else if (roomLabel.equals(UNUSED_LABEL)) {
								unusedInitial = initial;
							}
							roomMap.put(initial, new Room(roomLabel));
						}
						case PERSON_TYPE_LABEL -> {
							card = new Card(markerInfo[1], CardType.PERSON);
							playerCards.add(card);							
							Player player;
							if (players.isEmpty()) {
								player = new HumanPlayer(
									markerInfo[1],
									markerInfo[2],
									Integer.parseInt(markerInfo[3]), Integer.parseInt(markerInfo[4])
								);
								humanPlayer = player;
							} else {
								player = new ComputerPlayer(
									markerInfo[1],
									markerInfo[2],
									Integer.parseInt(markerInfo[3]), Integer.parseInt(markerInfo[4])
								);
							}
							players.add(player);
							nonAnswerCards.add(card);
						}
						case WEAPON_TYPE_LABEL -> {
							card = new Card(markerInfo[1], CardType.WEAPON);
							weaponCards.add(card);
							nonAnswerCards.add(card);
						}
						default -> throw new Exception("Invalid type \"" + markerInfo[1] + "\" in setup config.");
					}
				} catch (Exception exception) {
					scanner.close();
					throw new BadConfigFormatException(exception.getMessage());
				}
			}
			
			Card randomCard = roomCards.get((int) (Math.random() * roomCards.size()));
			theAnswer.setRoomCard(randomCard);
			nonAnswerCards.remove(randomCard);
			
			randomCard = playerCards.get((int) (Math.random() * playerCards.size()));
			theAnswer.setPersonCard(randomCard);
			nonAnswerCards.remove(randomCard);
			
			randomCard = weaponCards.get((int) (Math.random() * weaponCards.size()));
			theAnswer.setWeaponCard(randomCard);
			nonAnswerCards.remove(randomCard);
		}
	}

	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		grid = new ArrayList<>();

		File layoutFile = new File(layoutConfigFile);
		try (Scanner scanner = new Scanner(layoutFile)) {
			int rowIndex = 0;
			int expectedNumCols = -1;
			
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(",");
				numCols = line.length;

				List<BoardCell> row = new ArrayList<>();

				try {
					int colIndex = 0;
					for (String marker: line) {
						char initial = marker.charAt(0);
						// if room is null, then the initial is not a valid room
						Room room = getRoom(initial);
						if (room == null) {
							// if initial is not a valid room, then the cell is invalid
							throw new Exception("Invalid room " + initial + " in layout config.");
						}

						BoardCell cell = new BoardCell(rowIndex, colIndex, initial);

						if (initial == walkwayInitial) {
							cell.setIsWalkway(true);
							cell.setIsRoom(false);
						} else if (initial == unusedInitial) {
							cell.setIsWalkway(false);
							cell.setIsRoom(false);
						} else {
							cell.setIsWalkway(false);
							cell.setIsRoom(true);
						}

						cell.setOccupied(false);

						if (marker.length() == 2) {
							char special = marker.charAt(1);

							switch (special) {
								case Room.LABEL_MARKER -> {
									cell.setIsRoomLabel(true);
									room.setLabelCell(cell);
									cell.setIsRoomCenter(false);
								}
								case Room.CENTER_MARKER -> {
									cell.setIsRoomLabel(false);
									cell.setIsRoomCenter(true);
									room.setCenterCell(cell);
								}
								default -> {
									try {
										cell.setDoorDirection(DoorDirection.getDirection(special));
									} catch (BadConfigFormatException exception) {
										if (roomMap.containsKey(special)) {
											cell.setSecretPassage(special);
										} else {
											// if special is not a valid room, then the cell is invalid
											throw new Exception("Invalid cell " + marker + " in layout config.");
										}
									}
								}
							}
						}

						row.add(cell);

						colIndex++;
					}

					if (expectedNumCols != -1 && expectedNumCols != numCols) {
						// if the number of columns is inconsistent, then the layout is invalid
						throw new Exception("Inconsistent number of columns in layout config found at row " + rowIndex + ".");
					}
					
					expectedNumCols = numCols;
				} catch (Exception exception) {
					throw new BadConfigFormatException(exception.getMessage());
				}

				grid.add(row);

				rowIndex++;
			}

			numRows = rowIndex;

			scanner.close();
		} catch (Exception exception) {
			throw new BadConfigFormatException(exception.getMessage());
		}
	}

	private void calcAdjLists() {
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			for (int colIndex = 0; colIndex < numCols; colIndex++) {
				BoardCell cell = getCell(rowIndex, colIndex);
				
				if (cell.isSecretPassage()) {
					handleSecretPassage(cell);
				} else if (cell.isWalkway()) {
					// Walkways are adjacent to walkways and doorway walkways
					
					// Even-indexed elements are row indices, odd-indexed elements are column indices
					int[] flatAdjIndices = {rowIndex - 1, colIndex, rowIndex, colIndex + 1,
						rowIndex + 1, colIndex, rowIndex, colIndex - 1};
					for (int index = 0; index < flatAdjIndices.length; index += 2) {
						if (flatAdjIndices[index] < 0 || flatAdjIndices[index] >= numRows ||
							flatAdjIndices[index + 1] < 0 || flatAdjIndices[index + 1] >= numCols) {
							continue;
						}
						
						BoardCell adjCell = getCell(flatAdjIndices[index], flatAdjIndices[index + 1]);
						if (adjCell.isWalkway()) {
							cell.addAdj(adjCell);
						}
					}

					if (cell.isDoorway()) {
						handleDoorway(cell);
					}
				}
			}
		}
	}

	private void handleSecretPassage(BoardCell cell) {
		BoardCell startingRoomCenter = getRoom(cell).getCenterCell();
		BoardCell endingRoomCenter = getRoom(cell.getSecretPassage()).getCenterCell();
		
		// Secret passages mean we need to add the two room centers as adjacent to each other
		startingRoomCenter.addAdj(endingRoomCenter);
		endingRoomCenter.addAdj(startingRoomCenter);
	}

	private void handleDoorway(BoardCell cell) {
		int doorwayRoomRowIndex = cell.getRow();
		int doorwayRoomColIndex = cell.getCol();
		
		switch (cell.getDoorDirection()) {
			case UP -> doorwayRoomRowIndex--;
			case RIGHT -> doorwayRoomColIndex++;
			case DOWN -> doorwayRoomRowIndex++;
			case LEFT -> doorwayRoomColIndex--;
		}

		BoardCell roomCenter = getRoom(getCell(doorwayRoomRowIndex, doorwayRoomColIndex)).getCenterCell();

		// Doorway walkways are adjacent to corresponding room center and walkways
		cell.addAdj(roomCenter);

		// Room centers are adjacent to corresponding doorway walkways
		roomCenter.addAdj(cell);
	}

	public void dealCards() {
		Collections.shuffle(nonAnswerCards);
		int playerIdx = 0;
		for (Card card: nonAnswerCards) {
			players.get(playerIdx % players.size()).updateHand(card);
			playerIdx = (playerIdx + 1) % players.size();
		}
	}

	public boolean checkAccusation(Solution proposedAnswer) {
		return proposedAnswer.equals(theAnswer);
	}

	public Card handleSuggestion(Player player, Solution proposedAnswer) {
		// Find player list index of player after the one making the suggestion
		int nextPlayerIdx = 0;
		for (Player otherPlayer: players) {
			if (otherPlayer.equals(player)) {
				nextPlayerIdx = (nextPlayerIdx + 1) % players.size();
				break;
			}
			nextPlayerIdx = (nextPlayerIdx + 1) % players.size();
		}

		// Return first encountered disproving card if any player can disprove the suggestion
		int playersProcessed = 0;
		int playerIdx = nextPlayerIdx;
		while (playersProcessed < players.size() - 1) {
			Card disprovingCard = players.get(playerIdx).disproveSuggestion(proposedAnswer);
			if (disprovingCard != null) {
				return disprovingCard;
			}
			playerIdx = (playerIdx + 1) % players.size();
			playersProcessed++;
		}

		// Null means no other player could disprove the suggestion
		return null;
	}

	public static Board getInstance() {
		if (theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public List<List<BoardCell>> getGrid() {
		return grid;
	}

	public BoardCell getCell(int row, int col) {
		return grid.get(row).get(col);
	}

	public Room getRoom(char cellInitial) {
		return roomMap.get(cellInitial);
	}

	public Room getRoom(BoardCell cell) {
		return getRoom(cell.getInitial());
	}

	public Card getRoomCard(BoardCell cell) {
		return new Card(getRoom(cell).getName(), CardType.ROOM);
	}

	public List<Room> getRooms() {
		return new ArrayList<>(roomMap.values());
	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return getCell(row, col).getAdjList();
	}

	public void calcTargets(BoardCell startCell, int pathLength) {
		visited = new HashSet<>();
		targets = new HashSet<>();
		findAllTargets(startCell, pathLength);
		if (players.get(ClueGame.getInstance().getPlayerTurnIndex()).isDragged() && startCell.isRoomCenter()) {
			targets.add(startCell);
		}
		if(targets.isEmpty()) {
			targets.add(startCell);
		}
	}

	private void findAllTargets(BoardCell startCell, int pathLength) {
		visited.add(startCell);

		for (BoardCell adjCell: startCell.getAdjList()) {
			if (visited.contains(adjCell)) {
				continue;
			}

			visited.add(adjCell);

			if (!adjCell.isOccupied()) {
				if (pathLength == 1 || adjCell.isRoomCenter()) {
					targets.add(adjCell);
				} else {
					if (adjCell.isRoom()) {
						targets.add(adjCell);
					}

					findAllTargets(adjCell, pathLength - 1);
				}
			} else if (adjCell.isRoomCenter()) {
				targets.add(adjCell);
			}

			visited.remove(adjCell);
		}

		visited.remove(startCell);
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Player getHumanPlayer() {
		return humanPlayer;
	}

	public List<Card> getNonAnswerCards() {
		return nonAnswerCards;
	}

	public Solution getTheAnswer() {
		return theAnswer;
	}

	public void setTheAnswer(Solution theAnswer) {
		this.theAnswer = theAnswer;
	}

	public List<Card> getTotalDeck() {
		List<Card> deck = new ArrayList<>(nonAnswerCards);
		deck.addAll(theAnswer.getCardSet());
		return deck;
	}
	
	public Player getPlayerFromCard(Card card) {
		if (card.getType() != CardType.PERSON) return null;
		for (Player player : players) {
			if (card.getName() == player.getName()) {
				return player;
			}
		}
		System.out.println("THIS SHOULD NOT PRINT");
		return null;
	}
}
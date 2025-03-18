/*
 * Class: Board
 * 
 * Purpose: The Board class is a singleton that represents the game board.
 * 
 * Responsibilities: The Board class is responsible for loading the layout and setup config files, initializing the board, and storing the grid of cells and room map.
 * 
 * Authors: Aragorn Wang, Anya Streit
 */

package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private static final String ROOM_TYPE_LABEL = "Room";
	private static final String SPACE_TYPE_LABEL = "Space";
	
	private static final String WALKWAY_LABEL = "Walkway";
	private static final String UNUSED_LABEL = "Unused";
	
	private static Board theInstance;
	
	private char walkwayInitial;
	private char unusedInitial;

	private int numRows;
	private int numCols;

	private ArrayList<ArrayList<BoardCell>> grid;

	private String layoutConfigFile;
	private String setupConfigFile;

	private Map<Character, Room> roomMap;
	
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	public Board() {
		super();
	}

	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
			calcAdjLists();
		} catch (BadConfigFormatException | FileNotFoundException exception) {
			
		}
	}

	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		roomMap = new HashMap<>();

		File setupFile = new File(setupConfigFile);
		try (Scanner scanner = new Scanner(setupFile)) {
			while (scanner.hasNextLine()) {
				String splitLine = scanner.nextLine();
				if (splitLine.substring(0, 2).equals("//")) {
					continue;
				}
				
				try {
					String[] markerInfo = splitLine.split(", ");
					String roomType = markerInfo[0];
					String roomLabel = markerInfo[1];
					char initial = markerInfo[2].charAt(0);
					
					switch (roomType) {
						case ROOM_TYPE_LABEL -> roomMap.put(initial, new Room(roomLabel));
						case SPACE_TYPE_LABEL -> {
							roomMap.put(initial, new Room(roomLabel));
							if (roomLabel.equals(WALKWAY_LABEL)) {
								walkwayInitial = initial;
							} else if (roomLabel.equals(UNUSED_LABEL)) {
								unusedInitial = initial;
							}
						}
						default -> throw new Exception("Invalid room type \"" + roomType + "\" in setup config file.");
					}
				} catch (Exception exception) {
					scanner.close();
					throw new BadConfigFormatException(exception.getMessage());
				}
			}
		}
	}

	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		grid = new ArrayList<>();
		
		File layoutFile = new File(layoutConfigFile);
		try (Scanner scanner = new Scanner(layoutFile)) {
			int rowIndex = 0;
			int oldNumCols = -1;
			while (scanner.hasNextLine()) {
				String[] splitLine = scanner.nextLine().split(",");
				numCols = splitLine.length;
				
				ArrayList<BoardCell> row = new ArrayList<>();
				
				try {
					int colIndex = 0;
					for (String marker: splitLine) {
						char initial = marker.charAt(0);
						// if room is null, then the initial is not a valid room
						Room room = getRoom(initial);
						if (room == null) {
							// if initial is not a valid room, then the cell is invalid
							throw new Exception("Invalid room " + initial + " in layout config file.");
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
									cell.setIsLabel(true);
									room.setLabelCell(cell);
									cell.setIsRoomCenter(false);
								}
								case Room.CENTER_MARKER -> {
									cell.setIsLabel(false);
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
											throw new Exception("Invalid cell " + marker + " in layout config file.");
										}
									}
								}
							}

						}
						
						row.add(cell);

						colIndex++;
					}
					
					if (oldNumCols != -1 && oldNumCols != numCols) {
						// if the number of columns is inconsistent, then the layout is invalid
						throw new Exception("Inconsistent number of columns in layout config file found at row " + rowIndex + ".");
					}
					oldNumCols = numCols;
				} catch (Exception exception) {
					throw new BadConfigFormatException(exception.getMessage());
				}
				
				grid.add(row);
				
				rowIndex++;
			}
			
			numRows = rowIndex;

			scanner.close();
		} catch (Exception exception) {
			throw new FileNotFoundException();
		}
	}
	
	private void calcAdjLists() {
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {			
			for (int colIndex = 0; colIndex < numCols; colIndex++) {
				BoardCell cell = getCell(rowIndex, colIndex);
				
				if (cell.isSecretPassage()) {
					BoardCell startingRoomCenter = getRoom(cell).getCenterCell();
					BoardCell endingRoomCenter = getRoom(cell.getSecretPassage()).getCenterCell();
					
					startingRoomCenter.addAdj(endingRoomCenter);
					endingRoomCenter.addAdj(startingRoomCenter);
				} else if (cell.isWalkway()) {
					// Walkways are adjacent to walkways and doorway walkways
					
					if (rowIndex > 0) {
						BoardCell cellAbove = getCell(rowIndex - 1, colIndex);
						if (cellAbove.isWalkway()) {
							cell.addAdj(cellAbove);
						}
					}
					
					if (colIndex < numCols - 1) {
						BoardCell cellToRight = getCell(rowIndex, colIndex + 1);
						if (cellToRight.isWalkway()) {
							cell.addAdj(cellToRight);
						}
					}
					
					if (rowIndex < numRows - 1) {
						BoardCell cellBelow = getCell(rowIndex + 1, colIndex);
						if (cellBelow.isWalkway()) {
							cell.addAdj(cellBelow);
						}
					}
					
					if (colIndex > 0) {
						BoardCell cellToLeft = getCell(rowIndex, colIndex - 1);
						if (cellToLeft.isWalkway()) {
							cell.addAdj(cellToLeft);
						}
					}
					
					if (cell.isDoorway()) {
						int doorwayRoomRowIndex = rowIndex;
						int doorwayRoomColIndex = colIndex;	
						switch (cell.getDoorDirection()) {
							case DoorDirection.UP -> doorwayRoomRowIndex--;
							case DoorDirection.RIGHT -> doorwayRoomColIndex++;
							case DoorDirection.DOWN -> doorwayRoomRowIndex++;
							case DoorDirection.LEFT -> doorwayRoomColIndex--;
						}
						
						BoardCell roomCenter = getRoom(getCell(doorwayRoomRowIndex, doorwayRoomColIndex)).getCenterCell();
						
						// Doorway walkways are adjacent to corresponding room center and walkways
						cell.addAdj(roomCenter);
						
						// Room centers are adjacent to corresponding doorway walkways
						roomCenter.addAdj(cell);
					}
				}
			}
		}
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

	public BoardCell getCell(int row, int col) {
		return grid.get(row).get(col);
	}

	public Room getRoom(char cellInitial) {
		return roomMap.get(cellInitial);
	}

	public Room getRoom(BoardCell cell) {
		return getRoom(cell.getInitial());
	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return getCell(row, col).getAdjList();
	}

	public void calcTargets(BoardCell cell, int roll) {
		visited = new HashSet<>();
		visited.add(cell);
		targets = new HashSet<>();
		findAllTargets(cell, roll);
	}
	
	private void findAllTargets(BoardCell cell, int roll) {
		for (BoardCell adjCell: cell.getAdjList()) {
			// If the cell is a room, add it to the targets and return early since the player can't move through rooms
//			if (cell.isRoom()) {
//				targets.add(cell);
//				return;
//			}
			
			if (visited.contains(adjCell)) {
				continue;
			}
			
			visited.add(adjCell);
			
			// We only add the cell to the targets if it is not occupied since the player can't move to or through an occupied cell
			if (!adjCell.isOccupied()) {
				if (roll == 1) {
					targets.add(adjCell);
				} else {
					if (adjCell.isRoom()) {
						targets.add(adjCell);
					}
					findAllTargets(adjCell, roll - 1);
				}
			}
			
			visited.remove(adjCell);
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}
}
import javax.swing.*;
import java.util.*;
import java.io.*; 


public class Group3LeagueProject
{
	public static int leagueNumber, winPoints, drawPoints;
	
	/**
	* main() method just kicks-off the program 
	* by calling the logIn method.
	*/
	public static void main (String[] args) throws IOException
	{
		logIn(); 
	}
	
	/**
	* logIn() method:
	* Requests username and password from user.
	* User is allowed three attempts to successfully log in
	* or else access is denied.
	* The log in details are checked against the Administrators.txt file.
	* The administrator ID is the passed to menuMain(administratorID) method.
	*/
	public static void logIn() throws IOException
	{
		String adminName   = "",      adminPassword   = "";
		String usernameMessage = "Please enter your username";
		String passwordMessage = "Please enter your password";
		String detailsNotFoundMessage = "Log-in details do not match records.\nPlease try again. You have " ;
		String deniedMessage = "3 failed log-ins.\nAccess denied.";
		Scanner in; 
		int chance = 1, attemptsRemaining = 3;
		boolean validInput  = false, validAccessDetails = false;

		File adminFile = new File("Administrators.txt");
		String fileElements[]; 
		
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> allUserDetails = new ArrayList<String>();
		String lineFromFile;
		String tempUser = "";
		in = new Scanner(adminFile);

		while (in.hasNext()) 
		{
		  lineFromFile = in.nextLine();
		  fileElements = lineFromFile.split(",");
		  users.add(fileElements[1]+","+fileElements[2]);	  
		  allUserDetails.add(lineFromFile);	
		}	
		in.close();
		while((!(validInput)) && (chance <= 3))
		{
			adminName = JOptionPane.showInputDialog(null, usernameMessage, "Username", 3);
			if (adminName != null)
			{
				adminPassword = JOptionPane.showInputDialog(null, passwordMessage, "Password", 3);  
				if (adminPassword != null)
				{
					
				   tempUser = adminName + "," + adminPassword;
				   if (users.contains(tempUser))
					{
								validInput = true;
								validAccessDetails = true;	
					}
					else
					{
						chance += 1; 
							if (chance <= 3)
							{
								attemptsRemaining--;
								JOptionPane.showMessageDialog(null, detailsNotFoundMessage + " " + attemptsRemaining + " attempts remaining", "Error", 2);	
							}
							else
							{
								JOptionPane.showMessageDialog(null, deniedMessage, "Access denied", 0);
								validInput = true;
							}
					}
				}
				else
				  validInput = true;
			}
			else
				validInput = true;
		}
			
		
		int position, adminID;
		String entryAtThisPosition;
		if (validAccessDetails)
		{
		   position = users.indexOf(tempUser);
		   entryAtThisPosition = allUserDetails.get(position);
		   position = entryAtThisPosition.indexOf(",");
		   adminID  = Integer.parseInt(entryAtThisPosition.substring(0,position));
		   JOptionPane.showMessageDialog(null, "Welcome " + adminName + "!", "Log-in Successful", 1);
		   menuMain(adminID);
		}
	}
	
	/**
	* menuMain(administratorID) method:
	* Passed the admin ID of the logged-in user.
	* Provides the user with two options: manage leagues or create a new league.
	* Depending on the selection, either the menuListOfLeagues(administratorID) method
	* or the createLeague(administratorID) method is called.
	* All the menu methods are used to navigate thorugh the program.
	*/
	public static void menuMain(int administratorID) throws IOException
	{
		String[] options = {"Manage Leagues", "Create New League"};
		boolean validInput  = false;
		String selectedOption;
		selectedOption = (String) JOptionPane.showInputDialog(null, "Choose option", "Main Menu", 3, null, options, options[0]);

		while((!(validInput)))
		{
			if (selectedOption != null) 
			{
				if (selectedOption == options[0])
				{
					menuListOfLeagues(administratorID); // display list of leagues this admin manages
					validInput = true;
				}
				else if (selectedOption == options[1])
				{
					createLeague(administratorID);
					validInput = true;
				}
			}
			else  
				validInput = true;
		}
	}	

	/**
	* menuListOfLeagues(administratorID) method:
	* Passed the admin ID of the logged-in user.
	* Provides the user with a list of leagues they manage.
	* If the user selects a league to manage, the menuLeagueOptions(administratorID)
	* method is called.
	* If the user does not have any leagues to manage, an error message is shown.
	* If the user hits cancel, they go back to the main menu.
	*/
	public static void menuListOfLeagues (int administratorID) throws IOException
	{
		boolean validInput  = false;
		String selectedLeague;
		
		String fileName = ("Leagues.txt");
		File inputFile = new File(fileName); 
	
		ArrayList<ArrayList<String>> leagueList = new ArrayList<ArrayList<String>>();

		leagueList.add(new ArrayList<String>()); 
		leagueList.add(new ArrayList<String>()); 
		leagueList.add(new ArrayList<String>()); 
		
		String[] fileItem; 
		
		
		if (!inputFile.exists()) 
		{
			JOptionPane.showMessageDialog(null, "No leagues exist", "Error", 2);
			menuMain(administratorID);
		}
		else
		{
			Scanner fileReader = new Scanner(inputFile);
			while (fileReader.hasNext())
			{
				fileItem = (fileReader.nextLine()).split(",");
				leagueList.get(0).add(fileItem[0]);
				leagueList.get(1).add(fileItem[1]);
				leagueList.get(2).add(fileItem[2]);
			}
			fileReader.close();
			
			// populate the options array with each league name
			ArrayList<String> optionsAdminLeaguesArrList = new ArrayList<String>();
			
			int adminIdCheck;
			for (int item = 0; item < leagueList.get(2).size(); item++)  
				{
					adminIdCheck = Integer.parseInt(leagueList.get(2).get(item));
					if(administratorID == adminIdCheck)
						{
						optionsAdminLeaguesArrList.add(leagueList.get(0).get(item) + "." + "       " + leagueList.get(1).get(item));
						}
				}
	
			if	(optionsAdminLeaguesArrList.size() == 0)
			{
				JOptionPane.showMessageDialog(null, "No leagues", "Error", 2);
				menuMain(administratorID);
			}
			
			String [] optionsAdminLeagues = new String[optionsAdminLeaguesArrList.size()];
			for (int item = 0; item < optionsAdminLeaguesArrList.size(); item++)  
				{
					optionsAdminLeagues[item] = optionsAdminLeaguesArrList.get(item);
				}
			selectedLeague = (String) JOptionPane.showInputDialog(null, "Choose League", "Select league to manage", 3, null, optionsAdminLeagues, optionsAdminLeagues[0]);
			
			if (selectedLeague != null)
			{
				String [] fileElements3;
				fileElements3 = selectedLeague.split("\\.");
				int selectedLeagueInt = Integer.parseInt(fileElements3[0]);
				leagueNumber = selectedLeagueInt; 						//Setting global variable for selected league to edit correct files
				menuLeagueOptions(selectedLeagueInt, administratorID);  // display options for that league
				validInput = true;
			}else // hit cancel
				{
					menuMain(administratorID); // if user hasn't selected a league, back to the main menu
					validInput = true;
				}
		}
	}
	
	
	
	
	/**
	* createLeague(administratorID) method:
	* Passed the admin ID of the logged-in user
	* Allows the user create a new league
	* They must input: league name, points for a win, points for a draw,
	* number of participants, and the names of all participants.
	* The leagues and participants text files are created/updated
	* The FixtureGenerator class is also called to create the fixtures 
	* for the league and write them to the fixtures text file, and also
	* create a results text file with the fixture number and initial null values.
	* There is a warning if the same league name has already been created.
	* A 'Bye' participant is added when an odd number of participants are entered by the user.
	* All fixtures against the Bye participant result in an automatic win for the other participant
	*/
	public static ArrayList<ArrayList<String>> leagues;
	public static void createLeague(int administratorID) throws IOException
	{
		String userInput ="";
		String leagueName = "";
		String adminNum = Integer.toString(administratorID);
		String fileElements2[] = null;
		
		String fileName = "Leagues.txt";
		File leaguesFile = new File(fileName);
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(leaguesFile, true));	
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = 0;
		
		//read data in leagues file into array lists
		leagues = new ArrayList<ArrayList<String>>();
		leagues.add(new ArrayList<String>()); //league number
	    leagues.add(new ArrayList<String>()); //league name
	    leagues.add(new ArrayList<String>()); //admin number
	   
	  	Scanner in2; 
	  	in2 = new Scanner(leaguesFile);
	 	while(in2.hasNext())
	  	{
		   fileElements2 = (in2.nextLine()).split(",");
		   leagues.get(0).add(fileElements2[0]);  
		   leagues.get(1).add(fileElements2[1]);  
		   leagues.get(2).add(fileElements2[2]);
	  	} 
	 	in2.close();
		
	    leagueNumber = 1;
	 	//for loop to set leagueNumber to highest number in leagues.txt for naming convention
		for(int i = 0; i < leagues.get(0).size(); i++) 
			{
				int tempLeagueParseInt = Integer.parseInt(leagues.get(0).get(i));
				
				if (leagueNumber <= tempLeagueParseInt)
				{
					leagueNumber = ((Integer.parseInt(leagues.get(0).get(i)))+1);
					
				}
			}
		
		boolean leagueInput = false;
		while(!leagueInput) //loop to check if league name input is blank
		{
			leagueName = JOptionPane.showInputDialog(null, "League Name", "Leagues", 1);
			if (leagueName != null && leagueName.equals(""))
			{
				JOptionPane.showMessageDialog(null, "League name cannot be blank");
				
			}
			else if (leagueName != null)
			{
				leagueInput = true; //breaks loop if user inputs a name	
			}
			else // hits cancel
			{
				leagueInput = true;
				menuMain(administratorID);
			}
				
		}
		
		boolean warning = true;
		while (warning)
		{
			boolean found = false;
			
			for (int i = 0; i < leagues.get(1).size(); i++)	//checks if list contains leagueName
			{
				if ((leagueName.equals(leagues.get(1).get(i))) && (adminNum.equals(leagues.get(2).get(i)))) 
				{
				  found = true;
				}
			}
			
			if(found == true){	//warns user if duplicate name was found
				dialogResult = JOptionPane.showConfirmDialog(null, "League name already exists. Would you like to continue?", "Warning", dialogButton);	
					if(dialogResult != 0) { //displays dialog box if user chooses not to continue
						leagueName = JOptionPane.showInputDialog(null, "League Name", "Leagues", 1);
					}else{
						warning = false; //breaks loop if user chooses to continue anyway
					}
			}
			else
			{
				warning=false;
			}
		}
		
		// assign number of points for a win for the league
		String winPointsString, drawPointsString;
		boolean validInput = false;
		String pattern = "[0-9]{1,2}";

		while (!validInput)
		{
			winPointsString = (String) JOptionPane.showInputDialog(null,"Please enter number of points for win", "Win", 3);		
				
			if (winPointsString != null)
			{
				if (!(winPointsString.matches(pattern)))
				{
					JOptionPane.showMessageDialog(null, "You must enter a 2 digit number", "Error",  2);
				}
				else 
				{	
					winPoints = Integer.parseInt(winPointsString);
					validInput = true;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "You must enter number of points\n allocated for a win", "Error",  2);
			}
		}
		
		// assign number of points for a draw for the league
		validInput = false;
		while (!validInput)
		{			
			drawPointsString = (String) JOptionPane.showInputDialog(null, "Please enter number of points for draw", "Draw", 3);

			if (drawPointsString != null) 
			{
				if (!(drawPointsString.matches(pattern)))
				{
					JOptionPane.showMessageDialog(null, "You must enter a 2 digit number", "Error",  2);
				}
				else 
				{	
					drawPoints = Integer.parseInt(drawPointsString);
					validInput = true;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "You must enter number of points\n allocated for a draw", "Error", 2);	
			}
		}

		String leagueNumberString = Integer.toString(leagueNumber);
		fileWriter.append(leagueNumberString + ',');		
		fileWriter.append(leagueName + ','); 
		fileWriter.append(adminNum ); 
		fileWriter.append(System.getProperty( "line.separator" )); //next line for next league
		fileWriter.close();
		
		FixtureGenerator fixGenerator = new FixtureGenerator();

		// Get number of Participants and their names
		int numOfParticipants;
		numOfParticipants = fixGenerator.fixGen(leagueNumber);
		String fileName2 = leagueNumber + "_Participants.txt"; //create participants file for league
		
		File participantsFile = new File(fileName2);
		FileWriter fileWriter2 = new FileWriter(participantsFile);
		
		boolean endOfInput = false;
		int numberOfParticipantsCounter = 0;
		
		while((!(endOfInput)) && (numberOfParticipantsCounter < numOfParticipants)) // loop for as many times as the number of participants
		{
			userInput=JOptionPane.showInputDialog(null, "Participant #" + (numberOfParticipantsCounter + 1) + " Name", "Participants", 1);
				
				if (userInput != null)
				{
						if (!(userInput.equals(""))) //if user does not click cancel and input is not empty, adds to file
						{
						  fileWriter2.write((numberOfParticipantsCounter + 1) + ",");
						  fileWriter2.write(userInput); 
						  fileWriter2.write(System.getProperty("line.separator")); 
						}
						else	// the user input is blank and hits ok
						{
							JOptionPane.showMessageDialog(null,"Participant name cannot be blank");
							numberOfParticipantsCounter--;
						}			
				}
				else // hits cancel
				{
					JOptionPane.showMessageDialog(null,"You have not entered all required participant names");
					numberOfParticipantsCounter--;		
				}
			numberOfParticipantsCounter++;
		}
		
		if(numOfParticipants % 2 == 1) // Adds 'Bye' team when an odd number of participants are entered by the user
		{
				fileWriter2.write((numOfParticipants+1) + ",");
			    fileWriter2.write("Bye"); 
			    fileWriter2.write(System.getProperty( "line.separator" )); 	
		}
		fileWriter2.close();
		JOptionPane.showMessageDialog(null, leagueName + " League created.");
		menuMain(administratorID);
	}
		   
	

	/**
	* menuLeagueOptions(selectedLeague, administratorID) method:
	* Passed the admin ID of the logged-in user and the selectedLeague
	* Provides the user with three options for that league: edit the results,
	* generate a league table, or delete the league and depending on the selection
	* the relevant method is called (resultsDisplay(selectedLeague, administratorID),
	* generateLeaderBoard(selectedLeague), deleteLeague(selectedLeague, administratorID)).
	* If cancel is hit, the menuMain(administratorID) method is called
	*/
	public static void menuLeagueOptions(int selectedLeague, int administratorID) throws IOException 
	{
		String[] options = {"Edit Results", "Generate League Table", "Delete League"};
		boolean validInput  = false;
		String selectedOption;
		selectedOption = (String) JOptionPane.showInputDialog(null, "Choose option", "League Options", 3, null, options, options[0]);
		
		while((!(validInput)))
		{
			if (selectedOption != null) // cancel button not clicked
			{
				if (selectedOption == options[0])
				{
					resultsDisplay(selectedLeague, administratorID); 
					validInput = true;
				}
				else if (selectedOption == options[1])
				{
					generateLeaderBoard(selectedLeague); 
					menuLeagueOptions(selectedLeague, administratorID); 
					validInput = true;
				}
				else 
				{
					deleteLeague(selectedLeague, administratorID);
					validInput = true;
				}
			}
			else // cancel button clicked
				menuMain(administratorID);
				validInput = true;
		}
		
	}	
	
	/**
	* generateLeaderBoard(int selectedLeague) method:
	* First calls the readFilesIntoArrayLists(selectedLeague) method -
	* if the participants, results and fixtures text files exist, it calls the following methods:
	* createEmptyLeaderBoard(selectedLeague);
	*	processResults(selectedLeague);
	*	  orderLeaderBoard(selectedLeague);
	*	  displayLeaderboard(selectedLeague);
	*/
	public static ArrayList<ArrayList<String>>  teams;
	public static ArrayList<ArrayList<Integer>> fixtures;	
	public static ArrayList<ArrayList<Integer>> results;
	public static int [][] leaderBoard;
	public static void generateLeaderBoard(int selectedLeague) throws IOException 
	{	
		boolean readFile; 
		readFile = readFilesIntoArrayLists(selectedLeague);
		if (!readFile)
		  JOptionPane.showMessageDialog(null, "One or more files do not exist.", "Error", 2);
		else
		{
		  createEmptyLeaderBoard(selectedLeague);
		  processResults(selectedLeague);
		  orderLeaderBoard(selectedLeague);
		  displayLeaderboard(selectedLeague);
		}
	}
  
	/**
	* readFilesIntoArrayLists(int selectedLeague) method:
	* If the participants, results and fixtures text files exist, 
	* they are all read into ArrayLists.
	*/
	public static boolean readFilesIntoArrayLists(int selectedLeague) throws IOException
	{

		String fileName1 = leagueNumber+"_Participants.txt"; 
		String fileName2 = leagueNumber+"_Fixtures.txt";    
		String fileName3 = leagueNumber+"_Results.txt"; 	
		
		String fileElements[];
		File inputFile1 = new File(fileName1);
		File inputFile2 = new File(fileName2);
		File inputFile3 = new File(fileName3);
		
		teams = new ArrayList<ArrayList<String>>();
		teams.add(new ArrayList<String>());
		teams.add(new ArrayList<String>());
	  
		fixtures = new ArrayList<ArrayList<Integer>>();
		fixtures.add(new ArrayList<Integer>());
		fixtures.add(new ArrayList<Integer>());
		fixtures.add(new ArrayList<Integer>());
		
		results = new ArrayList<ArrayList<Integer>>();
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>()); /* To handle Bye value (0 if an even number of teams entered, otherwise the number of the Bye team is entered.
												e.g. for a league with five participants, this arraylist will store 6 for the Bye team */
		results.add(new ArrayList<Integer>()); //To handle Game played flag (0 for game not played, 1 if the game was played)
		
		
		if (inputFile1.exists() && inputFile2.exists() && inputFile3.exists()) 
		{
		  
			Scanner in;
		  
			// read in participants
			in = new Scanner(inputFile1); 
			while(in.hasNext())
			  {
				fileElements = (in.nextLine()).split(","); 
				teams.get(0).add(fileElements[0]);  
				teams.get(1).add(fileElements[1]);  
			  } 
			  in.close();
			  
			  // read in fixtures
			  in = new Scanner(inputFile2);
			  while(in.hasNext())
			  {
				fileElements = (in.nextLine()).split(",");
				fixtures.get(0).add(Integer.parseInt(fileElements[0]));  
				fixtures.get(1).add(Integer.parseInt(fileElements[1]));  
				fixtures.get(2).add(Integer.parseInt(fileElements[2]));  
			  } 
			  in.close();
			  
			  // read in results
			  in = new Scanner(inputFile3);
			  while(in.hasNext())
				  {
				  fileElements = (in.nextLine()).split(",");
					// Changes Null to 0
					if (fileElements[1].equals("null"))
					{
						fileElements[1] = "0";
					}
					// Changes Null to 0
					if (fileElements[2].equals("null"))
					{
						fileElements[2]= "0";
					}
					
					
					//Adds results to results array
					results.get(0).add(Integer.parseInt(fileElements[0]));  
					results.get(1).add(Integer.parseInt(fileElements[1]));  
					results.get(2).add(Integer.parseInt(fileElements[2]));
					results.get(3).add(Integer.parseInt(fileElements[3])); //To handle Bye
					results.get(4).add(Integer.parseInt(fileElements[4])); //To handle Game played flag
				}
			in.close();
			return true; 
			
		}
		else
		{
			return false; 
		}
		
	}
  
  	/**
	* createEmptyLeaderBoard(int selectedLeague) method:
	* Determines the number of teams/players which will determine the number of rows. 
	* Places team numbers in column 0 of the leader board.
	*/
	public static void createEmptyLeaderBoard(int selectedLeague)
	{
	 
		int rows = teams.get(0).size();
		int columns = 14;  
		leaderBoard = new int[rows][columns];

		for (int i = 0; i < leaderBoard.length; i++)
		  leaderBoard[i][0] = Integer.parseInt(teams.get(0).get(i));
	}	  
  
	/**
	* processResults(int selectedLeague) method:
	* This processes each result depening on values of home team score and away team score.
	* The method uses the winPoints and drawPoints values assigned in the createLeague(administratorID) method.
	* The method calls the recordFixtureResultForHomeTeam and recordFixtureResultForAwayTeam methods.
	* It also checks whether games have been played against the Bye team, in which case the other team is
	* assigned a win and the relevant points for that win.
	* The scores for each game are pulled from the results arraylist, and the home team/ away team numbers 
	* are pulled from the fixtures arraylist
	*/
	public static void processResults(int selectedLeague)
	{
	
		
		int homeTeamScore, awayTeamScore, homeTeamNumber, awayTeamNumber;
		int position, byeNumber, gamePlayed;
		int countTestGamesPlayed = 1;  // Delete this after only for testing below
		for (int i = 0; i < results.get(0).size(); i++)  
		{
		  
		  homeTeamScore  = results.get(1).get(i);
		  awayTeamScore  = results.get(2).get(i);
		  byeNumber      = results.get(3).get(i);
		  gamePlayed 	 = results.get(4).get(i);
		  homeTeamNumber = fixtures.get(1).get(i);
		  awayTeamNumber = fixtures.get(2).get(i);
		  
		  
		  if((homeTeamNumber != byeNumber && awayTeamNumber != byeNumber && gamePlayed != 0))
			{     
				  if (homeTeamScore == awayTeamScore)
				  {
					recordFixtureResultForHomeTeam(homeTeamNumber,0,1,0,homeTeamScore,awayTeamScore,drawPoints);
					recordFixtureResultForAwayTeam(awayTeamNumber,0,1,0,homeTeamScore,awayTeamScore,drawPoints);
					countTestGamesPlayed++;
				  }  
				  else if (homeTeamScore > awayTeamScore)
				  {
					recordFixtureResultForHomeTeam(homeTeamNumber,1,0,0,homeTeamScore,awayTeamScore,winPoints);
					recordFixtureResultForAwayTeam(awayTeamNumber,0,0,1,homeTeamScore,awayTeamScore,0);
					countTestGamesPlayed++;
				  }  
				  else
				  {
					recordFixtureResultForHomeTeam(homeTeamNumber,0,0,1,homeTeamScore,awayTeamScore,0);
					recordFixtureResultForAwayTeam(awayTeamNumber,1,0,0,homeTeamScore,awayTeamScore,winPoints);  
					countTestGamesPlayed++;
				  }  
		  	}else if((homeTeamNumber == byeNumber || awayTeamNumber == byeNumber && gamePlayed == 0))
		  			{
			  		if (homeTeamNumber == byeNumber)
					  {
						recordFixtureResultForHomeTeam(homeTeamNumber,0,0,1,0,0,0);
						recordFixtureResultForAwayTeam(awayTeamNumber,1,0,0,0,0,winPoints);
						countTestGamesPlayed++;
					  }  
					  else if (awayTeamNumber == byeNumber) 
					  		{
						    recordFixtureResultForHomeTeam(homeTeamNumber,1,0,0,0,0,winPoints);
							recordFixtureResultForAwayTeam(awayTeamNumber,0,0,1,0,0,0);
							countTestGamesPlayed++;
					  		}
		  		}
		}
	}

	/**
	* recordFixtureResultForHomeTeam(int aTN, int w, int d, int l, int hTS, int aTS, int p) method:
	* This method is passed the relevant arguments from the process results method and records the result
	* on the leader board array
	*/
	public static void recordFixtureResultForHomeTeam(int hTN, int w, int d, int l, 
                                                       int hTS, int aTS, int p)
	{
		leaderBoard[hTN-1][1]++;        			// gamesPlayed
		leaderBoard[hTN-1][2]+= w;      			// homeWin
		leaderBoard[hTN-1][3]+= d;      			// homeDraw
		leaderBoard[hTN-1][4]+= l;      			// homeLoss
		leaderBoard[hTN-1][5]+= hTS;    			// homeTeamScore
		leaderBoard[hTN-1][6]+= aTS;    			// awayTeamScore
		leaderBoard[hTN-1][12] += (hTS - aTS);    	// goalDifference
		leaderBoard[hTN-1][13] += p;    			// points
	}
	
 	/**
	* recordFixtureResultForAwayTeam(int aTN, int w, int d, int l, int hTS, int aTS, int p) method:
	* This method is passed the relevant arguments from the process results method and records the result
	* on the leader board array
	*/
	public static void recordFixtureResultForAwayTeam(int aTN, int w, int d, int l, int hTS, int aTS, int p)
	{
		leaderBoard[aTN-1][1]++;        			// gamesPlayed
		leaderBoard[aTN-1][7]+= w;      			// awayWin
		leaderBoard[aTN-1][8]+= d;      			// awayDraw
		leaderBoard[aTN-1][9]+= l;      			// awayLoss
		leaderBoard[aTN-1][10]+= aTS;    			// awayTeamScore
		leaderBoard[aTN-1][11]+= hTS;    			// homeTeamScore
		leaderBoard[aTN-1][12] += (aTS - hTS);    	// goalDifference
		leaderBoard[aTN-1][13] += p;    			// points  
	}	
	
   	/**
	* orderLeaderBoard(int selectedLeague) method:
	* Orders the leader board according to the participants with the 
	* highest total points using a basic 'swap'. 
	*/
	public static void orderLeaderBoard(int selectedLeague)
	{
		int [][] temp = new int[leaderBoard.length][leaderBoard[0].length];
		boolean finished = false;
		while (!finished) 
		{
		  finished = true;
		  for (int i = 0; i < leaderBoard.length - 1; i++) 
		  {
			if (leaderBoard[i][13] < leaderBoard[i + 1][13])
			{
			  for (int j = 0; j < leaderBoard[i].length; j++) 
			  {
				temp[i][j]            = leaderBoard[i][j];
				leaderBoard[i][j]     = leaderBoard[i + 1][j];
				leaderBoard[i + 1][j] = temp[i][j];
			  }
			  finished = false;
			}
		  }
		}
	}	  
	  
	/**
	* displayLeaderboard(int selectedLeague) method:
	* Formats the displays of the leader board when it is printed.
	* Indents to ensure columns are aligned correctly when participant names vary in length.
	*/
	public static void displayLeaderboard(int selectedLeague)
	{
		int aTeamNumber;
		String aTeamName, formatStringTeamName;
		String longestTeamName       = teams.get(1).get(0);
		int    longestTeamNameLength = longestTeamName.length();
		
		for (int i = 1; i < teams.get(1).size(); i++)
		{
		  longestTeamName = teams.get(1).get(i);  
		  if (longestTeamNameLength < longestTeamName.length())
			longestTeamNameLength = longestTeamName.length();
		}
		formatStringTeamName = "%-" + (longestTeamNameLength + 5) + "s";
		System.out.printf(formatStringTeamName,"Team Name");
		System.out.println("  GP  HW  HD  HL  GF  GA  AW  AD  AL  GF  GA   GD   TP"); 
	   
		for (int i = 0; i < leaderBoard.length; i++)
		{
		  aTeamNumber       = leaderBoard[i][0];
		  aTeamName         = teams.get(1).get(aTeamNumber - 1);       
		  System.out.printf(formatStringTeamName, aTeamName);
		  System.out.printf("%4d", leaderBoard[i][1]);
		  System.out.printf("%4d", leaderBoard[i][2]);
		  System.out.printf("%4d", leaderBoard[i][3]);
		  System.out.printf("%4d", leaderBoard[i][4]);
		  System.out.printf("%4d", leaderBoard[i][5]);
		  System.out.printf("%4d", leaderBoard[i][6]);
		  System.out.printf("%4d", leaderBoard[i][7]);
		  System.out.printf("%4d", leaderBoard[i][8]);
		  System.out.printf("%4d", leaderBoard[i][9]);
		  System.out.printf("%4d", leaderBoard[i][10]);
		  System.out.printf("%4d", leaderBoard[i][11]);
		  System.out.printf("%5d", leaderBoard[i][12]);
		  System.out.printf("%5d", leaderBoard[i][13]);
		  System.out.println();
		}
		
	} 
	
	/**
	* resultsDisplay(int selectedLeague, int administratorID) method:
	* This displys the fixture number and participant names to the user so they can select for which 
	* one to enter results. If all results have been entered for a league a message is shown to
	* inform the user and they are given the option to edit a previously entered result. A list of results is then displayed,
	* including the original score the user entered. 
	* This method also calls the checkFix(String windowMessage, String windowTitle) to validate the inputted results.
	* Our logic for creating a new results array was to maintain the null values in the file to account for a 0/0 score
	*/
	public static void resultsDisplay(int selectedLeague, int administratorID) throws IOException
	{
		String selectedResult;
		String resultFile = leagueNumber + "_Results.txt";
		
		ArrayList<ArrayList<String>> resultsD = new ArrayList<ArrayList<String>>();
		resultsD.add(new ArrayList<String>()); // Add list to store Fixture number
		resultsD.add(new ArrayList<String>()); // Add list to store home team score
		resultsD.add(new ArrayList<String>()); // Add list to store away team score
		resultsD.add(new ArrayList<String>()); // Add list to store bye number
		resultsD.add(new ArrayList<String>()); // Add list to store game played flag
		
		File resultsFileVal = new File(resultFile); //This is the results filecreator
		
		if(!resultsFileVal.exists())
		{
		  System.out.println("Results file does not exist");
		}else
			{
			  Scanner in = new Scanner(resultsFileVal);
			  String [] aLineFromResults;
			  while(in.hasNext())
				{
				aLineFromResults = (in.nextLine()).split(",");
				resultsD.get(0).add((aLineFromResults[0])); // adds fixture number
				resultsD.get(1).add((aLineFromResults[1])); // adds null
				resultsD.get(2).add((aLineFromResults[2])); // adds null
				resultsD.get(3).add((aLineFromResults[3])); // adds bye
				resultsD.get(4).add((aLineFromResults[4])); // adds Game Played flag
				} 
				in.close();
				  
			}
			
		//Read from Fixtures file into multidimensional array list to match participants file
		ArrayList<ArrayList<String>> fixtureList = new ArrayList<ArrayList<String>>();
		fixtureList.add(new ArrayList<String>()); // Add to store Fixture number
		fixtureList.add(new ArrayList<String>()); // Add to store Home number
		fixtureList.add(new ArrayList<String>()); // Add to store Away number
		
		File fixtureListFileVal = new File(leagueNumber + "_Fixtures.txt"); 
		if(!fixtureListFileVal.exists())
		{
		  System.out.println("Fixture list file does not exist");
		}else
			{
			  Scanner in = new Scanner(fixtureListFileVal);
			  String [] aLineFromFixtureList;
			  while(in.hasNext())
				{
				aLineFromFixtureList = (in.nextLine()).split(",");
				fixtureList.get(0).add((aLineFromFixtureList[0])); 
				fixtureList.get(1).add((aLineFromFixtureList[1]));
				fixtureList.get(2).add((aLineFromFixtureList[2]));
				} 
				in.close();
				  
			}
		
		
		// reading participant.txt file
		ArrayList<ArrayList<String>> participantList = new ArrayList<ArrayList<String>>();
		participantList.add(new ArrayList<String>()); // Add to store Participant number
		participantList.add(new ArrayList<String>()); // Add to store Participant name
		
		File participantListFileVal = new File(leagueNumber+"_Participants.txt"); 
		if(!participantListFileVal.exists())
		{
		  System.out.println("Participant list file does not exist");
		}else
			{
			  Scanner in = new Scanner(participantListFileVal);
			  String [] aLineFromParticipantList;
			  while(in.hasNext())
				{
				aLineFromParticipantList = (in.nextLine()).split(",");
				participantList.get(0).add((aLineFromParticipantList[0])); 
				participantList.get(1).add((aLineFromParticipantList[1]));		 
				} 
				in.close();
				 
			}
		

		ArrayList<String> optionsArrList = new ArrayList<String>();
				
		// this links the participant number in the fixtures.txt to the correct participant name from participants.txt
		for (int item = 0; item < resultsD.get(1).size(); item++)  //For loop to populate options arrayList
			{
				if((resultsD.get(1).get(item).equals("null"))&&(resultsD.get(2).get(item).equals("null")))
					{
						int homeNumber =  Integer.parseInt(fixtureList.get(1).get(item));//Parsing Fixture List home value to int 
						int  awayNumber=Integer.parseInt(fixtureList.get(2).get(item)); //Parsing Fixture List away value to int
						String homeTeamName="";
						String awayTeamName="";
						
							for(int i=0; i<participantList.get(0).size();i++) //For loop to match participant names to  homeTeamName String
							{
								if((participantList.get(0).get(i)).equals(""+homeNumber))
								{
									homeTeamName=(""+(participantList.get(1).get(i)));
								}
								if((participantList.get(0).get(i)).equals(""+awayNumber))
								{
									awayTeamName=(""+(participantList.get(1).get(i)));
								}
							}
							if(!(homeTeamName.contains("Bye")|awayTeamName.contains("Bye")))  //If bye option does not present as a choice.
							{
								optionsArrList.add(resultsD.get(0).get(item) + ".  " + homeTeamName + " v " + awayTeamName); //Fixture number and teams
							}
					}
			}
		
		String [] options;
		if(optionsArrList.size()==0)  //To populate edit results if no null values available
			{
				JOptionPane.showMessageDialog(null, "Results have been entered for all fixtures.\nDo you wish to view/edit previously entered results?", "All Results Entered", 3);
				options=new String[resultsD.get(1).size()];
				for (int item = 0; item < resultsD.get(1).size(); item++)  //For loop to populate options arrayList
					{
					
							int homeNumber =  Integer.parseInt(fixtureList.get(1).get(item));//Parsing Fixture List home value to int 
							int  awayNumber=Integer.parseInt(fixtureList.get(2).get(item)); //Parsing Fixture List away value to int
							String homeTeamName="";
							String awayTeamName="";
							
							for(int i=0; i<participantList.get(0).size();i++) //For loop to match participant names to  homeTeamName String
							{
								if((participantList.get(0).get(i)).equals(""+homeNumber))
								{
									homeTeamName=(""+(participantList.get(1).get(i)));
								}
								if((participantList.get(0).get(i)).equals(""+awayNumber))
								{
									awayTeamName=(""+(participantList.get(1).get(i)));
								}
							}
							optionsArrList.add(resultsD.get(0).get(item) + ".  " + homeTeamName + " v " + awayTeamName 
								+ "  (Result: " + resultsD.get(1).get(item) + "-" + resultsD.get(2).get(item) + ")"); //Fixture number and teams
					}
				
			}else{
				options = new String[optionsArrList.size()]; //Declare Array size for Selection menu
				} 
		
		options = new String[optionsArrList.size()]; //Declare Array size for Selection menu
		
		
		for (int item = 0; item < optionsArrList.size(); item++)  //For loop to populate options array
			{
				
					options[item] = optionsArrList.get(item); //Removes Byes from Editable options
			}
		
		selectedResult = (String) JOptionPane.showInputDialog(null, "Fixtures", "Choose Fixture to edit", 3, null, options, options[0]);
		
		//Selection Menu 
		if (selectedResult != null) 
		{
			//Inputs for selecting fixtures and home/away teams
			String [] fileElement4;
			String splitSelResult=selectedResult.substring(0,2); //To capture selection as an int
			
			fileElement4=splitSelResult.split("\\.");
			int fixSelect = Integer.parseInt(fileElement4[0]);
			fixSelect = (fixSelect - 1); //To correct location in array
			
			String homeTeamScore = checkFix("Enter home team score", 
										 "Please enter the required number");	 //Home team score
			String awayTeamScore = checkFix("Enter Away Team score", 
										 "Please enter the required number");    //Away team score
			//Add values to array
				
				resultsD.get(1).add(fixSelect, homeTeamScore);
				resultsD.get(2).add(fixSelect, awayTeamScore);
				resultsD.get(4).add(fixSelect,"1"); // adds game played flag
			
			FileWriter aFileWriter = new FileWriter(resultFile);
			PrintWriter out = new PrintWriter(aFileWriter);
			
			for (int i=0; i<resultsD.get(0).size();i++)
			{
				out.println(resultsD.get(0).get(i)+","+resultsD.get(1).get(i)+","+resultsD.get(2).get(i)+","+resultsD.get(3).get(i)+","+resultsD.get(4).get(i));
			}
			out.close();
			aFileWriter.close();
			menuLeagueOptions(selectedLeague, administratorID); 
		}else{
				menuLeagueOptions(selectedLeague, administratorID);
				}
	
		
		
	
	} 
	
	/**
	* checkFix(String windowMessage, String windowTitle) method:
	* This method simply ensures that the results entered by the user are digits between 0-99
	* and displys an error message if this is not the case.
	*/
	public static String checkFix(String windowMessage, String windowTitle)
	{
		boolean validInput = false;
		String userInput = "", pattern = "[0-9]{1,2}";
		String errorMessage =  
			"Invalid input.\nYou must input a number 0-99.\nSelect OK to retry."; 
		while (!(validInput))
		{

			userInput = JOptionPane.showInputDialog(null, windowMessage, windowTitle, 3);
			
			if (userInput == null) 
				validInput = true;
			else if (!userInput.matches(pattern)) 
				JOptionPane.showMessageDialog(null, errorMessage, "Error in user input", 2);
			else
			{
				validInput = true;
			}	
		}  
		return userInput;
	}
	
	/**
	* deleteLeague(int leagueNum, int administratorID) method:
	* This method allows a user to delete a league 
	* The league is deleted from the Leagues.txt file and the relevant results, participants and 
	* fixtures text files are deleted. 
	* The user is requested to confirm deletion of a league to help ensure they do not
	* delete a league by accident. 
	*/
	public static void deleteLeague(int leagueNum, int administratorID) throws IOException
	{
		String filename = (leagueNum + "_Participants.txt");  
		String filename2 = (leagueNum + "_Results.txt");
		String filename3 = (leagueNum + "_Fixtures.txt");
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = 0;
		File aFile = new File(filename);
		aFile.delete();
		File bFile = new File(filename2);
		bFile.delete();
		File cFile = new File(filename3);
		cFile.delete();
	   
		dialogResult = JOptionPane.showConfirmDialog(null, "Please confirm you wish to delete league", "Confirm", 0, dialogButton);	
			
		if(dialogResult != 0) 
		{ 
			menuLeagueOptions(leagueNum, administratorID);
		}
		else
		{
			File inputFile = new File("Leagues.txt");
			File tempFile = new File("TempFile.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = ""+leagueNum;
			String currentLine;

			while((currentLine = reader.readLine()) != null) 
			{
				// trim newline when comparing with lineToRemove
				if(!currentLine.startsWith(lineToRemove))
				{
				writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			JOptionPane.showMessageDialog(null, "Your league has been deleted", "League Deleted", 1);
			writer.close(); 
			reader.close();
			inputFile.delete();
			tempFile.renameTo(inputFile);
			menuMain(administratorID);
		}
	}
}
  
  
  
  
  
  
  
  
  
  
 







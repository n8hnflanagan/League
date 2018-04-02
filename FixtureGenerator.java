import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FixtureGenerator 
{
	/**
	* fixGen(int fileNum) method:
	* Generates fixtures for the relevant file of participants.
	* The method begins by getting the number of teams for which to generate
	* fixtures by calling the getNumberOfTeams(String windowMessage, String windowTitle)
	* method, which asks the user to input the number of participants in the league and pattern checks them
	* The participants play each other twice so the fixtures are generated and then a mirrored version of
	* fixtures is generated for the second time they play. 
	* The fixtures are written to a fixtures text file and a results text file is 
	* created with the fixture number and initial null values for each result. 
	*/
	public static int fixGen(int fileNum) 
	{
		int numberOfTeams, totalNumberOfRounds, numberOfMatchesPerRound;
		int roundNumber, matchNumber, homeTeamNumber, awayTeamNumber, even, odd;
		boolean additionalTeamIncluded = false;
		String selection;
		String [][] fixtures;
		String [][] revisedFixtures;
		String [][] reversedFixtures;  //To mirror array
		String []   elementsOfFixture;
		String fixtureAsText;
		selection = getNumberOfTeams("Enter number of participants", 
									 "Please enter a number in the range 2 to 99");
		int returnNumParticipants=0;
		returnNumParticipants=Integer.parseInt(selection);

		if (selection != null)
		{
		   numberOfTeams = Integer.parseInt(selection);  
		   if (numberOfTeams % 2 == 1)
		   {
			 numberOfTeams++;
			 additionalTeamIncluded = true;
		   }
		   totalNumberOfRounds     = numberOfTeams - 1;
		   numberOfMatchesPerRound = numberOfTeams / 2;
		   fixtures = new String[totalNumberOfRounds][numberOfMatchesPerRound];  
			
		   for (roundNumber = 0; roundNumber < totalNumberOfRounds; roundNumber++) 
		   {
			 for (matchNumber = 0; matchNumber < numberOfMatchesPerRound; matchNumber++) 
			 {
			   homeTeamNumber = (roundNumber + matchNumber) % (numberOfTeams - 1);
			   awayTeamNumber = (numberOfTeams - 1 - matchNumber + roundNumber) % (numberOfTeams - 1);
			   if (matchNumber == 0) 
				 awayTeamNumber = numberOfTeams - 1;
			   fixtures[roundNumber][matchNumber] = (homeTeamNumber + 1) + " v " + (awayTeamNumber + 1);
			 }
		   } 
		   revisedFixtures = new String[totalNumberOfRounds][numberOfMatchesPerRound];
		   even = 0;
		   odd = numberOfTeams / 2;
		   for (int i = 0; i < fixtures.length; i++) 
		   {
			 if (i % 2 == 0) 	
			   revisedFixtures[i] = fixtures[even++];
			 else 				
			   revisedFixtures[i] = fixtures[odd++];
		   }
		   fixtures = revisedFixtures;
			
		   for (roundNumber = 0; roundNumber < fixtures.length; roundNumber++) 
		   {
			 if (roundNumber % 2 == 1) 
			 {
			   fixtureAsText = fixtures[roundNumber][0];
			   elementsOfFixture = fixtureAsText.split(" v ");
			   fixtures[roundNumber][0] = elementsOfFixture[1] + " v " + elementsOfFixture[0];
			 }
		   } 
		   
			
			
			reversedFixtures = new String[fixtures.length][fixtures[0].length]; //Initialising size of reversedFixtures for reversed Fixture values
			
			//Code for taking the v out of fixtures array and putting the values back with a comma
			
			for(int k=0;k<fixtures.length;k++)
						{
							for (int m=0;m<fixtures[k].length;m++)
							{
								fixtureAsText = fixtures[k][m];
								elementsOfFixture = fixtureAsText.split(" v ");
								fixtures[k][m] = elementsOfFixture[0] +","+ elementsOfFixture[1];	
							}
						}	
						
			
			
			//Loop to reverse fixtures values and put them  in reversedFixtures
			for(int k=0;k<reversedFixtures.length;k++)
						{
							for (int m=0;m<reversedFixtures[k].length;m++)
							{
								fixtureAsText = fixtures[k][m];
								elementsOfFixture = fixtureAsText.split(",");
								
								
								reversedFixtures[k][m] = elementsOfFixture[1] +","+ elementsOfFixture[0];	//Inserting comma
							}
						}
			
			String file="Fixtures.txt"; //Variable for filename that is constant
			String file2=fileNum+"_"+file; //Variable for creating filename matching league generated
			
			File fileCheck = new File(file2); 
			
			if(!fileCheck.exists())
			{	
				try{
					FileWriter aFileWriter = new FileWriter(file2);
					PrintWriter out = new PrintWriter(aFileWriter);
					int count = 1;
					for(int k=0;k<fixtures.length;k++)
						{
							for (int m=0;m<fixtures[k].length;m++)
							{
								out.println(count+","+fixtures[k][m]);
								count++;
							}
						}	
					
					for(int k=0;k<reversedFixtures.length;k++)
						{
							
							for (int m=0;m<reversedFixtures[k].length;m++)
							{
								out.println(count+","+reversedFixtures[k][m]);
								count++;
							}
						}
								out.close();
								aFileWriter.close();
					}catch(IOException ioex){
								System.out.println("IO Error"+ioex);
								}
			}
			
			// This is duplication of the code above to create the results.txt file for manipulation by the administrator

			String result="Results.txt"; //Variable for filename that is constant
			String result2=fileNum+"_"+result; //Variable for appending to file

			
			File resultCheck = new File(result2); 
			
			if(!resultCheck.exists())
			{	
				try{
						if(additionalTeamIncluded == true)
						{
							FileWriter rFileWriter = new FileWriter(result2);
							PrintWriter out = new PrintWriter(rFileWriter);
							int count = 1;
							for(int k=0;k<fixtures.length;k++)
								{
									for (int m=0;m<fixtures[k].length;m++)
									{
										out.println(count+",null,null,"+numberOfTeams+",0");
										count++;
									}
								}	
							
							for(int k=0;k<reversedFixtures.length;k++)
								{
									
									for (int m=0;m<reversedFixtures[k].length;m++)
									{
										out.println(count+",null,null,"+numberOfTeams+",0");
										count++;
									}
								}
							out.close();
							rFileWriter.close();
										
						}else {
								FileWriter rFileWriter = new FileWriter(result2);
								PrintWriter out = new PrintWriter(rFileWriter);
								int count = 1;					
								for(int k=0;k<fixtures.length;k++)
								{
									
									for (int m=0;m<fixtures[k].length;m++)
									{
										out.println(count+",null,null,"+"0"+",0");
										count++;
									}
								}	
							
							for(int k=0;k<reversedFixtures.length;k++)
								{
									
									for (int m=0;m<reversedFixtures[k].length;m++)
									{
										out.println(count+",null,null,"+"0"+",0");
										count++;
									}
								}
							out.close();
							rFileWriter.close();
						}
					}catch(IOException ioex){
								System.out.println("IO Error"+ioex);
								}
			}	
		}
		return returnNumParticipants;
	} 	
	  
	/**
	* getNumberOfTeams(String windowMessage, String windowTitle) method:
	* This method asks the user to input the number of participants in the league.
	* The input must be a number (odd or even) from 2-99 and is validated.
	*/
	public static String getNumberOfTeams(String windowMessage, String windowTitle)
	{
		boolean validInput = false;    int numberOfnumberOfTeams;
		String userInput = "", pattern = "[0-9]{1,2}";
		String errorMessage1 =  
			"Invalid input.\n\nYou are permitted to have 2 to 99 participants.\nSelect OK to retry."; 
			
		String errorMessage2 =  
			"You must enter a number of participants.\nSelect OK to retry."; 
			
			while (!(validInput))
			{
				userInput = JOptionPane.showInputDialog(null, windowMessage, windowTitle, 3);
				  
				// hits cancel
				if (userInput == null) 
				{
					JOptionPane.showMessageDialog(null, errorMessage2, "Error in user input", 2);
				}
				// input doesn't match pattern
				else if (!userInput.matches(pattern)) 
				{
						JOptionPane.showMessageDialog(null, errorMessage1, "Error in user input", 2);
				}
				 // the input is valid
				else 
				{
					numberOfnumberOfTeams = Integer.parseInt(userInput); // only reason you parse here is because you need to check if the inputted number (int) is valid (2-99)
					if (numberOfnumberOfTeams < 2) // if what user enters matches pattern but is zero or 1 (pattern will have caught if its a 3 digit number
					  JOptionPane.showMessageDialog(null, errorMessage1, "Error in user input", 2); // then show this error msg
					else 
					  validInput = true;
				}
		}  
		return userInput;
	}	  
}


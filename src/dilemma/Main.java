package dilemma;

import java.util.ArrayList;

public class Main {
	private static java.util.Random random;
	private static int maxPlayers = 3500000;

	/*********
	 * Main method.
	 * @param <b>-r</b> for random players; argument that follows is the number of random players E.g., <b>-r 30</b> will create 30 players with random behaviors
	 * @param <b>-rx</b> for random players but excluding some behaviors. <b>-rx 30 -x cbot</b> will create 30 random players who will not be cbots. Exclusions can be stacked: <b>-rx 30 -x cbot -x dbot -x random</b>
	 * @param <b>-p</b> adds a prisoner. <b>-p Jones true cbot</b> creates a prisoner/player named Jones whose first move is defect who will play as a cooperate bot. Multiple players can be created this way.
	 * @param <b>-n</b> adds a multi-stringed name for a player/prisoner. Terminate the multiple string by typing --n. <b>-n Bob Jones --n false cbias</b> creates a player called Bob Jones whose first move is cooperate who has a bias to cooperate.
	 * @param <b>-g</b> the number of iterations of the prisoner's dilemma. <b>-g 200</b> will play 200 iterations.
	 * **********/
	public static void main(String[] args) {
		random = new java.util.Random();
		ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();
		
		if(args.length > 0) {
			
			int numPrisoners=0, numGames=0, behavior=0;
			String name = "";
			boolean defect = false;
			
			for(int i=0; i<args.length; i++) {
				if(args[i].equalsIgnoreCase("-r")) {
					numPrisoners += Math.abs( Integer.parseInt(args[++i]) );
					numPrisoners = numPrisoners >= maxPlayers ? maxPlayers : numPrisoners;
					addRandomPlayers(prisoners, numPrisoners);
				}
				
				else if(args[i].equalsIgnoreCase("-rx")) {
					ArrayList<String> excludes = new ArrayList<String>();
					numPrisoners += Math.abs( Integer.parseInt(args[++i]) );
					numPrisoners = numPrisoners >= maxPlayers ? maxPlayers : numPrisoners;
					++i;
					while( args[i].equalsIgnoreCase("-x") ){
						excludes.add(args[++i]);
						
						if(i >= args.length - 1) break;
						else ++i;
						
					}
					if( !(i >= args.length - 1) ) i--;  
					addRandomPlayers(prisoners, numPrisoners, excludes.toArray( new String[excludes.size()] ));
				}
				
				else if(args[i].equalsIgnoreCase("-p")) {
					name = args[++i];
					defect = Boolean.parseBoolean(args[++i]);
					behavior = getPersonality(args[++i]);
					
					prisoners.add(new Prisoner(name, defect, behavior));
				}
				
				else if(args[i].equalsIgnoreCase("-n")) {
					name = args[++i];
					++i;
					while(!args[i].equalsIgnoreCase("--n")){
						name += " " + args[i];
						++i;
					}
					defect = Boolean.parseBoolean(args[++i]);
					behavior = getPersonality(args[++i]);
					
					prisoners.add(new Prisoner(name, defect, behavior));
					
				}
				
				else if(args[i].equalsIgnoreCase("-g")) {
					numGames += Math.abs( Integer.parseInt(args[++i]) );
				}
			}
			
			if(numGames != 0) {
				startGame(prisoners, numGames, false);
			}
			else{
				startGame(prisoners);
			}
			Stats.sort(prisoners, true);
			for(Prisoner p : prisoners) System.out.println(p.name + ", "+ Prisoner.getBehavior(p) + ": " + p.points);
			
		}
		
		
		
		else{
			
			prisoners.add(new Prisoner("HAL", false, Prisoner.BAYESIAN));
			prisoners.add(new Prisoner("THE BATMAN", false, Prisoner.TIT_FOR_TAT));
			prisoners.add(new Prisoner("Azrael", true, Prisoner.TIT_FOR_TAT));
			prisoners.add(new Prisoner("Lucifer Diabolos", true, Prisoner.DEFECT_BOT));
			prisoners.add(new Prisoner("Satan Baalzebub", true, Prisoner.DEFECT_BOT));
			prisoners.add(new Prisoner("God", true, Prisoner.RANDOM));
			prisoners.add(new Prisoner("Darth Vader", true, Prisoner.DEFECT_BIAS));
			//prisoners.add(new Prisoner("Elua", false, Prisoner.COOPERATE_BIAS));
			//prisoners.add(new Prisoner("Lloyd Christmas", false, Prisoner.COOPERATE_BOT));
			
			addRandomPlayers(prisoners, 2);
			
			startGame(prisoners, 1010, false);
			
			Stats.sort(prisoners, true);
			
			for(Prisoner p : prisoners) System.out.println(p.name + ", "+ Prisoner.getBehavior(p) + ": " + p.points);
			
		}
		

	}
	
	/***********
	 * Begins a single iteration of the prisoner's dilemma
	 * @param prisonerDilemma - an arraylist of {@link Prisoner} objects
	 * ***************/
	public static void startGame(java.util.ArrayList<Prisoner> prisonerDilemma){
		int count = 0;
		for(Prisoner p : prisonerDilemma){
			for(int j=count+1; j<prisonerDilemma.size(); j++){
				System.out.println(p.name + "("+ p.defect +") VERSUS " + prisonerDilemma.get(j).name + "("+ prisonerDilemma.get(j).defect +")");
				PrisonerTree.executeDilemma(p, prisonerDilemma.get(j));
				System.out.println(p.name + ": "+ p.points +" " + prisonerDilemma.get(j).name + ": "+ prisonerDilemma.get(j).points+"\n");
			}
			count++;
		}
		count = 0;
		
		System.out.println("Round 1:");
		for(Prisoner p : prisonerDilemma) {
			System.out.println(p.name + ", "+ p.points);
		}
	}
	
	/***********
	 * Begins multiple iterations of the prisoner's dilemma
	 * @param prisonerDilemma - an arraylist of {@link Prisoner} objects
	 * @param iterations - number of games to play
	 * @param printMatch - flag denoting whether to print the results of each match between two prisoners
	 * ***************/
	public static void startGame(java.util.ArrayList<Prisoner> prisonerDilemma, int iterations, boolean printMatch){
		int count = 0, round = 1;
		//multiple iterations
		for(int i=0; i<iterations; i++){
			if(i < 1){
				for(Prisoner p : prisonerDilemma){
					for(int j=count+1; j<prisonerDilemma.size(); j++){
						if(printMatch) System.out.println(p.name + "("+ p.defect +") VERSUS " + prisonerDilemma.get(j).name + "("+ prisonerDilemma.get(j).defect +")");
						PrisonerTree.executeDilemma(p, prisonerDilemma.get(j));
						if(printMatch) System.out.println(p.name + ": "+ p.points +" " + prisonerDilemma.get(j).name + ": "+ prisonerDilemma.get(j).points +"\n");
					}
					count++;
				}
				count = 0;
				
				if(printMatch) {
					System.out.println("Round " + round +": ");
					for(Prisoner p : prisonerDilemma) {
						System.out.println(p.name + ", "+ p.points);
					}
					System.out.println("\n");
				}
				round++;
			}
			
			else {
				for(Prisoner p : prisonerDilemma){
					for(int j=count+1; j<prisonerDilemma.size(); j++){
						p.defect = p.executeDilemma(prisonerDilemma.get(j));
						prisonerDilemma.get(j).defect = prisonerDilemma.get(j).executeDilemma(p);
						
						if(printMatch) System.out.println(p.name + "("+ p.defect +") VERSUS " + prisonerDilemma.get(j).name + "("+ prisonerDilemma.get(j).defect +")");
						PrisonerTree.executeDilemma(p, prisonerDilemma.get(j));
						if(printMatch) System.out.println(p.name + ": "+ p.points +" " + prisonerDilemma.get(j).name + ": "+ prisonerDilemma.get(j).points +"\n");
					}
					count++;
				}
				count = 0;
				
				if(printMatch) {
					System.out.println("Round " + round +": ");
					for(Prisoner p : prisonerDilemma) {
						System.out.println(p.name + ", "+ p.points);
					}
					System.out.println("\n");
				}
				round++;
				
			}
			
			
		}
		

	}
	/***********
	 * Adds random players to a game
	 * @param prisonerDilemma - an arraylist of {@link Prisoner} objects
	 * @param n - number of random players to add
	 * ***************/
	public static void addRandomPlayers(ArrayList<Prisoner> prisoners, int n) {
		String p;
		boolean defect;
		int personality;
		for(int i=0; i<n; i++) {
			p = "Prisoner #" + i;
			defect = random.nextBoolean();
			personality = random.nextInt(7);
			
			prisoners.add(new Prisoner(p,defect,personality));
		}
	}
	
	/***********
	 * Adds random players to a game
	 * @param prisonerDilemma - an arraylist of {@link Prisoner} objects
	 * @param n - number of random players to add
	 * @param ex - a String array of behaviors to exclude
	 * ***************/
	public static void addRandomPlayers(ArrayList<Prisoner> prisoners, int n, String [] ex) {
		String p;
		
		boolean defect, isExclude = false;
		int personality, i=0;
		int [] exclude = new int [ex.length];
		
		for(i=0; i<ex.length;i++) exclude[i] = getPersonality(ex[i]);
		
		for(i=1; i<=n; i++) {
			p = "Prisoner #" + i;
			defect = random.nextBoolean();
			do {
				personality = random.nextInt(7);
				for(int j=0; j<exclude.length; j++){
					isExclude = exclude[j] == personality ? true : false;
					j = isExclude ? exclude.length : j;
				}
			} while(isExclude);
			
			
			prisoners.add(new Prisoner(p,defect,personality));
			//++numberPlayers;
			//if(numberPlayers % 5 == 0) System.out.println(numberPlayers);
		}
	}
	
	public static int catchPersonality(NumberFormatException nfe){
		String personality = nfe.toString().substring("java.lang.NumberFormatException: For input string: ".length(), nfe.toString().length());
		personality = personality.substring(1, personality.length()-1);
		if(  personality.equalsIgnoreCase("bayesian") || personality.equalsIgnoreCase("bayes") ){
			return Prisoner.BAYESIAN;
		}
		
		else if(personality.equalsIgnoreCase("dbot")){
			return Prisoner.DEFECT_BOT;
		}
		
		else if(personality.equalsIgnoreCase("cbot")){
			return Prisoner.COOPERATE_BOT;
		}
		
		else if(personality.equalsIgnoreCase("tft")){
			return Prisoner.TIT_FOR_TAT;
		}
		
		else if(personality.equalsIgnoreCase("random")){
			return Prisoner.RANDOM;
		}
		
		else if(personality.equalsIgnoreCase("dbias")){
			return Prisoner.DEFECT_BIAS;
		}
		
		else if(personality.equalsIgnoreCase("cbias")){
			return Prisoner.COOPERATE_BIAS;
		}
		
		else{
			nfe.printStackTrace();
			System.err.println("\""+personality +"\" is not a valid input for personality. Correct inputs are: cbot, dbot, cbias, dbias, tft, random, or bayesian");
			return -1;
		}
	}
	
	public static int getPersonality(String input) {
		switch( input.toLowerCase() ){
		case "bayesian":
		case "bayes":
			return Prisoner.BAYESIAN;
		case "cbot":
			return Prisoner.COOPERATE_BOT;
		case "cbias":
			return Prisoner.COOPERATE_BIAS;
		case "dbot":
			return Prisoner.DEFECT_BOT;
		case "dbias":
			return Prisoner.DEFECT_BIAS;
		case "tft":
			return Prisoner.TIT_FOR_TAT;
		default:
			return Prisoner.RANDOM;
	}
	}

}

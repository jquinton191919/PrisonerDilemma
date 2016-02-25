package dilemma;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();
		
		if(args.length > 0) {
			//foo
		}
		else{
			prisoners.add(new Prisoner("J. Quinton", true, Prisoner.BAYESIAN));
			//prisoners.add(new Prisoner("Scott Alexander", false, Prisoner.TIT_FOR_TAT));
			//prisoners.add(new Prisoner("Robin Hanson", false, Prisoner.TIT_FOR_TAT));
			//prisoners.add(new Prisoner("Lucifer Diabolos", true, Prisoner.DEFECT_BOT));
			//prisoners.add(new Prisoner("Satan Asmodeus", true, Prisoner.DEFECT_BOT));
			//prisoners.add(new Prisoner("God", true, Prisoner.RANDOM));
			//prisoners.add(new Prisoner("Homeopathy", false, Prisoner.RANDOM));
			//prisoners.add(new Prisoner("Moldbug", true, Prisoner.DEFECT_BIAS));
			//prisoners.add(new Prisoner("Michael Anissimov", true, Prisoner.DEFECT_BIAS));
			//prisoners.add(new Prisoner("Ozy", false, Prisoner.COOPERATE_BIAS));
			//prisoners.add(new Prisoner("Leah", false, Prisoner.COOPERATE_BIAS));
			//prisoners.add(new Prisoner("Fooligan Harry", false, Prisoner.COOPERATE_BOT));
			//prisoners.add(new Prisoner("Lloyd Christmas", false, Prisoner.COOPERATE_BOT));
			
			System.out.println( Math.sqrt(12) * Math.sqrt(12) );
			System.out.println( Stats.sqrt(-81) );
		}
		

	}
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
	
	public static void startGame(java.util.ArrayList<Prisoner> prisonerDilemma, int iterations){
		int count = 0, round = 1;
		//multiple iterations
		for(int i=0; i<iterations; i++){
			if(i < 1){
				for(Prisoner p : prisonerDilemma){
					for(int j=count+1; j<prisonerDilemma.size(); j++){
						System.out.println(p.name + "("+ p.defect +") VERSUS " + prisonerDilemma.get(j).name + "("+ prisonerDilemma.get(j).defect +")");
						PrisonerTree.executeDilemma(p, prisonerDilemma.get(j));
						System.out.println(p.name + ": "+ p.points +" " + prisonerDilemma.get(j).name + ": "+ prisonerDilemma.get(j).points +"\n");
					}
					count++;
				}
				count = 0;
				
				System.out.println("Round " + round +": ");
				for(Prisoner p : prisonerDilemma) {
					System.out.println(p.name + ", "+ p.points);
				}
				System.out.println("\n");
				round++;
			}
			
			else {
				for(Prisoner p : prisonerDilemma){
					for(int j=count+1; j<prisonerDilemma.size(); j++){
						p.defect = p.executeDilemma(prisonerDilemma.get(j));
						prisonerDilemma.get(j).defect = prisonerDilemma.get(j).executeDilemma(p);
						
						System.out.println(p.name + "("+ p.defect +") VERSUS " + prisonerDilemma.get(j).name + "("+ prisonerDilemma.get(j).defect +")");
						PrisonerTree.executeDilemma(p, prisonerDilemma.get(j));
						System.out.println(p.name + ": "+ p.points +" " + prisonerDilemma.get(j).name + ": "+ prisonerDilemma.get(j).points +"\n");
					}
					count++;
				}
				count = 0;
				
				System.out.println("Round " + round +": ");
				for(Prisoner p : prisonerDilemma) {
					System.out.println(p.name + ", "+ p.points);
				}
				System.out.println("\n");
				round++;
			}
			
			
		}
		

	}
	
	public static void addRandomPlayers(ArrayList<Prisoner> prisoners, int n) {
		String p;
		boolean defect;
		int personality;
		for(int i=0; i<n; i++) {
			p = "Prisoner #" + Integer.toString(new java.util.Random().nextInt(1000));
			defect = new java.util.Random().nextBoolean();
			personality = new java.util.Random().nextInt(7);
			
			prisoners.add(new Prisoner(p,defect,personality));
		}
	}

}

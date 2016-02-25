package dilemma;
import java.util.ArrayList;

public class Sort {
	public static ArrayList<Prisoner> prisoners;

	public static void sort(ArrayList<Prisoner> input) {
		sort(input, 0, input.size()-1);
	}
	
	public static void sort(ArrayList<Prisoner> input, int left, int right) {
		if(right - left > 1){
			medianOf3(input, left, right);
			int pivot = input.get(right - 1).points, eLeft = left, eRight = right-1;
			while(true){
				while(input.get(++eLeft).points < pivot);
				while(input.get(--eRight).points > pivot);
				if(eLeft < eRight) swap(input, eLeft, eRight);
				else{
					break;
				}
			}
			
			swap(input, right - 1, eLeft);
			sort(input, left, eRight);
		    sort(input, eLeft, right);
		}
		
		else if (right - left == 1) {
			if(input.get(left).points > input.get(right).points) swap(input, left, right);
		}
		
	}
	
	public static void medianOf3(ArrayList<Prisoner> input, int left, int right) {
		int median = (right + left) / 2;
		if ( input.get(left).points > input.get(median).points ) swap(input, left, median);
		if ( input.get(left).points > input.get(right).points ) swap(input, left, right);
		if ( input.get(median).points > input.get(right).points ) swap(input, median, right);
		
		swap(input, median, right - 1);
	}
	
	public static void swap(ArrayList<Prisoner> input, int p1, int p2) {
		Prisoner temp=input.get(p1);
		input.set(p1, input.get(p2));
		input.set(p2, temp);
	}
	
	public static void main(String [] args) {
		prisoners = new ArrayList<Prisoner>();
		
		prisoners.add(new Prisoner("J. Quinton", true, Prisoner.BAYESIAN));
		prisoners.add(new Prisoner("Scott Alexander", false, Prisoner.TIT_FOR_TAT));
		prisoners.add(new Prisoner("Robin Hanson", false, Prisoner.TIT_FOR_TAT));
		prisoners.add(new Prisoner("Lucifer Diabolos", true, Prisoner.DEFECT_BOT));
		prisoners.add(new Prisoner("Satan Asmodeus", true, Prisoner.DEFECT_BOT));
		//prisoners.add(new Prisoner("God", true, Prisoner.RANDOM));
		//prisoners.add(new Prisoner("Homeopathy", false, Prisoner.RANDOM));
		//prisoners.add(new Prisoner("Moldbug", true, Prisoner.DEFECT_BIAS));
		//prisoners.add(new Prisoner("Michael Anissimov", true, Prisoner.DEFECT_BIAS));
		//prisoners.add(new Prisoner("Ozy", false, Prisoner.COOPERATE_BIAS));
		//prisoners.add(new Prisoner("Leah", false, Prisoner.COOPERATE_BIAS));
		//prisoners.add(new Prisoner("Fooligan Harry", false, Prisoner.COOPERATE_BOT));
		//prisoners.add(new Prisoner("Lloyd Christmas", false, Prisoner.COOPERATE_BOT));
		
		prisoners.get(0).points = 2;
		prisoners.get(1).points = 1;
		prisoners.get(2).points = 1001;
		prisoners.get(3).points = 11;
		prisoners.get(4).points = 1111;
		
		sort(prisoners);
		int count=0;
		for(Prisoner p : prisoners){
			System.out.println(p.name +", "+ p.points);
			count++;
		}
		System.out.println(count);
	}

}

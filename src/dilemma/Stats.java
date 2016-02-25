package dilemma;

public class Stats {
	
	/***
	 * Bayes Theorem for Pr(B | A) = Pr(A | B)Pr(B) / Pr(A) format
	 * 
	 * @param prior - prior probability
	 * @param conditional - conditional probability
	 * @param totalProbability - total probability
	 * */
	public static double bayes(double prior, double conditional, double totalProbability) {
	        return prior * conditional / totalProbability;
	    }

	/***
	 * Bayes Theorem long format
	 * 
	 * @param priors - array of prior probabilities
	 * @param conditionals - array of conditional probabilities
	 * @throws exception if the number of prior probabilities doesn't match the number of conditional probabilities
	 * @return single array of posterior probabilities
	 * */
	public double [] bayes(double [] priors, double [] conditionals) throws Exception {
		if(priors.length != conditionals.length) throw new Exception("Number of prior probabilities does not match number of conditional probabilities");
		
		else {
			
			double total = 0.0;
			for(int i=0; i<priors.length; i++) total += priors[i];
			
			double [] posteriors = new double [priors.length];
			
			if (total > 1.00) throw new Exception("Prior probabilities total "+ total +", greater than 100%");
			else {
				double totalProbability=0.0;
				
				for(int i=0; i<priors.length; i++) totalProbability += priors[i] * conditionals[i];
				
				for(int i=0; i<posteriors.length; i++) posteriors[i] =  checkProbability( priors[i] * conditionals[i] / totalProbability );
				
				//correct for doubles being inexact: The last prior to be updated will be assumed to be the one meant to exhaust the possible hypothesis space
				total = 0.0;
				for(int i=0; i<posteriors.length-1; i++) total += posteriors[i];
				posteriors[posteriors.length-1] = checkProbability(1.0 - total);
				
			}
				
			return posteriors;
		}
		
	}
	/****
	 * Bayes Theorem long format using a 2D array
	 * @param data - a 2D array: data[0][x] are prior probabilities and data[1][x] are the corresponding conditional probabilities
	 * @return posterior - a 1D array of posterior probabilities
	 * **/
	public double [] bayes(double [][] data) throws Exception {
		if(data[0].length != data[1].length) throw new Exception("Number of prior probabilities does not match number of conditional probabilities");
		
		else {
			
			double total = 0.0;
			for(int i=0; i<data[0].length; i++) total += data[0][i];
			
			double [] posteriors = new double [data.length];
			
			if (total > 1.00) throw new Exception("Prior probabilities total "+ total +", greater than 100%");
			else {
				double totalProbability=0.0;
				
				for(int i=0; i<data[0].length; i++) totalProbability += data[0][i] * data[1][i];
				
				for(int i=0; i<posteriors.length; i++) posteriors[i] =  checkProbability( data[0][i] * data[1][i] / totalProbability );
				
				//correct for doubles being inexact: The last prior to be updated will be assumed to be the one meant to exhaust the possible hypothesis space
				total = 0.0;
				for(int i=0; i<posteriors.length-1; i++) total += posteriors[i];
				posteriors[posteriors.length-1] = checkProbability(1.0 - total);
				
			}
				
			return posteriors;
		}
		
	}

	public static double checkProbability(double p) {
		if(p == 1.0) return 0.99999999999999991;
		else if (p == 0.0) return 0.00000000000000001;
		else return p;
	}
	
	public static String sqrt (double n) {
		if (n > 0) return Double.toString( Math.sqrt(n) );
		else return Double.toString( Math.sqrt( Math.abs(n) ) ) + "i";
	}

}

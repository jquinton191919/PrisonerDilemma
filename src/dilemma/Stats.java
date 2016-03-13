package dilemma;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;


public class Stats {
	static double z;
	
	
	
	
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
	public static double [] bayes(double [] priors, double [] conditionals) throws Exception {
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
	public static double [] bayes(double [][] data) throws Exception {
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

	//correct for double's imprecision
	private static double checkProbability(double p) {
		if(p == 1.0) return 0.99999999999999991;
		else if (p == 0.0) return 0.00000000000000001;
		else return p;
	}
	
	public static double mean(int [] input) {
        int total=0;
        for(int i=0; i<input.length; i++) {
            total += input[i];
        }
        return total / input.length;
    }
    
    public static double mean(double [] input) {
        double total=0;
        for(int i=0; i<input.length; i++) {
            total += input[i];
        }
        return total / input.length;
    }
    
    public static double stdDev(int [] input) {
        int stdev=0;
        for(int i=0; i< input.length; i++) {
            stdev += Math.pow(mean(input) - input[i], 2);
        }
        return Math.sqrt(stdev / input.length);
    }
    
    public static double stdDev(double [] input) {
    	double stdev=0.0;
    	for(int i=0; i<input.length; i++) stdev += Math.pow(mean(input) - input[i], 2);
    	return Math.sqrt(stdev / input.length);
    }
    
    /***
   	 * Finds Pearson's correlation coefficient
   	 * @param dataSetA - a data set
   	 * @param dataSetB - a separate data set
   	 * ***/
   	public static double correlation(double [] dataSetA, double [] dataSetB) {
   		double meanX = mean(dataSetA), meanY = mean(dataSetB), r;
   		double numerator=0.0, left=0.0, right=0.0;
   		int n = dataSetA.length;
   		
   		for(int i=0; i<n; i++) {
   			numerator += (dataSetA[i] - meanX) * (dataSetB[i] - meanY);
   			left += Math.pow(dataSetA[i] - meanX, 2);
   			right += Math.pow(dataSetB[i] - meanY, 2);
   		}
   		r = numerator / Math.sqrt(left * right);
   		
		z = Math.sqrt( 1 / (n-3) );
		
   		
   		return r;
   	}
   	
   	
	/**
   	 * Calculates the alpha for a correlation in a dataset using Fisher's P-value computation
   	 * @param a - array of x axis values
   	 * @param b - array of y axis values
   	 * @param decimalPlaces - number of decimal places for alpha
   	 * **/
   	public static double pValueFisher(int [] a, int [] b, int decimalPlaces) {
   		//a is the "top" array, b is the "bottom" array
   		//so that it should look like this:
   		//a[   first   ][   last  ][sum of first + last]
   		//b[   first   ][   last  ][sum of first + last]
   		//-[first+first][last+last][      total        ]
   		
   		
   		int sumRow1 = a[0] + a[a.length-1];
   		int sumRow2 = b[0] + b[b.length-1];
   		
   		int sumCol1 = a[0] + b[0];
   		int sumCol2 = a[a.length-1] + b[b.length-1];
   		int sumBoth = sumRow1 + sumRow2; //total number found adding a[first] + b[first] + a[last] + b[last]
   		
   		BigInteger numerator = factorial(sumRow1)
   			.multiply( factorial(sumRow2) )
   				.multiply( factorial(sumCol1) )
   					.multiply( factorial(sumCol2) );
   		
   		BigInteger denominator = factorial(sumBoth)
   			.multiply( factorial(a[0]) )
   				.multiply( factorial(a[a.length-1]) )
   					.multiply( factorial(b[0]) )
   						.multiply( factorial(b[b.length-1]) );
   		
   		return numerator.divide(denominator).doubleValue();
   	}
   	
   	/**
   	 * Calculates the alpha for a correlation in a dataset using Fisher's P-value computation
   	 * @param a - array of x axis values
   	 * @param b - array of y axis values
   	 * @param decimalPlaces - number of decimal places for alpha
   	 * **/
   	public static double pValueFisher(double [] a, double [] b, int decimalPlaces) {
   		int [] x = new int[a.length];
   		int [] y = new int[b.length];
   		for(int i=0; i<a.length; i++) {
   			x[i] = (int) a[i];
   			y[i] = (int) b[i];
   		}
   		
   		return pValueFisher(x,y,decimalPlaces);
   	}
	
	
	/********
	 * Find the factorial of an integer
	 * @param n input integer
	 * @return n!
	 * ************/
	public static BigInteger factorial(int n) {
   		BigInteger answer = BigInteger.ONE;
   		for(int i=2; i<=n; i++) {
   			answer = answer.multiply(BigInteger.valueOf(i));
   		}
   		return answer;
   	}
   	
	
	/********
	 * Find the factorial of a floating point number
	 * @param n input float
	 * @return n!
	 * ************/
   	public static BigDecimal factorial(double n) {
   		BigDecimal answer = BigDecimal.ONE;
   		for(int i=2; i<=n; i++) {
   			answer = answer.multiply(BigDecimal.valueOf(i));
   		}
   		return answer;
   	}
   	
   	/*****
   	 * Find the square root of a BigDecimal
   	 * @param value - Input BigDecimal
   	 * @return square root of the input BigDecimal
   	 * @author http://stackoverflow.com/users/2440756/tylovset
   	 * *****/
   	public static BigDecimal sqrt(BigDecimal value) {
   	    BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
   	    return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
   	}
   	
   	
   	/********
	 * Find the log base of n
	 * @param base desired base
	 * @param n input integer
	 * @return log(base) of n
	 * ************/
   	public static double log(double base, double n) {
   		return Math.log(n) / Math.log(base);
   	}
   	
   	/***
   	 * Permuations: How many combinations of k can be arranged out of set n
   	 * @param n - total set
   	 * @param k - max combination
   	 * **/
   	public static BigInteger permutations(int n, int k) { 
   		return factorial(n).divide(factorial(n - k));
   	}
   	
   	/*****
   	 * Combinations: How many combinations of k can be arranged out of set n without reuse
   	 * @param n - total set
   	 * @param k - max combination
   	 * *****/
   	public static BigInteger combinations(int n, int k) {
   		return permutations(n,k).divide(factorial(k));
   	}
	
	
	/*****
	 * Quicksort for sorting the arraylist of prisoner objects
	 * @param input - ArrayList of prisoner objects
	 * @param reverse - boolean variable denoting whether to do a reverse sort (i.e., prisoner with the largest number -- the winner -- is the first element)
	 * *************************/
	public static void sort(ArrayList<Prisoner> input, boolean reverse) {
		sort(input, 0, input.size() - 1, reverse);
	}
	
	private static void sort(ArrayList<Prisoner> input, int left, int right, boolean reverse) {
		if( right - left > 1 ) {
			int elemLeft, elemRight, pivot, pivotVal;
			pivot = medianOf3(input, left, right);
			pivotVal = input.get(pivot).points;
			
			
			if(!reverse){
				elemLeft = left;
				elemRight = right - 1;
			
				swap(input, pivot, elemRight);
			}
			
			else {
				swap(input, left, right);
				elemLeft = left + 1;
				elemRight = right;
			
				swap(input, pivot, elemLeft);
			}
			
			while(true) {
				if(!reverse) {
					while( input.get(++elemLeft).points < pivotVal );
					while( input.get(--elemRight).points > pivotVal );
				}
				
				else{
					while( input.get(++elemLeft).points > pivotVal );
					while( input.get(--elemRight).points < pivotVal );
				}
				
				if( elemLeft < elemRight ) swap(input, elemLeft, elemRight);
				else break;
			}
			if(!reverse) swap(input, right - 1, elemLeft);
			else swap(input, left + 1, elemRight);
			
			sort(input, left, elemRight, reverse);
			sort(input, elemLeft, right, reverse);
				
		}
		
		else if(right - left == 1) {
			if(!reverse) {
				if( input.get(left).points > input.get(right).points ) swap(input, left, right);
			}
			else {
				if( input.get(left).points < input.get(right).points ) swap(input, left, right);
			}
		}
		
		
	}
	
	private static int medianOf3(ArrayList<Prisoner> input, int left, int right) {
		int median = (left + right) / 2;
		if ( input.get(left).points > input.get(median).points ) swap(input, left, median);
		if ( input.get(left).points > input.get(right).points ) swap(input, left, right);
		if ( input.get(median).points > input.get(right).points ) swap(input, median, right);
		return median;
	}
	
	private static void swap(ArrayList<Prisoner> array, int p1, int p2) {
		Prisoner temp=array.get(p1);
		array.set(p1, array.get(p2));
		array.set(p2, temp);
	}

}

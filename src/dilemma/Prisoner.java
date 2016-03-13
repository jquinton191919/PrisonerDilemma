package dilemma;

public class Prisoner {
	public static final int DEFECT_BOT = 0, //defect bot always defects 
	COOPERATE_BOT = 1, //cooperate bot always cooperates
	TIT_FOR_TAT = 2, //tit for tat mirrors the other prisoner's previous behavior
	BAYESIAN = 3, //like tit for tat, except takes other prisoner's lifetime behavior into account; takes advantage of cooperate bots
	RANDOM = 4, //random behavior
	DEFECT_BIAS = 5, //bias towards defect
	COOPERATE_BIAS = 6; //bias towards cooperate
	
	public String name;
	public boolean defect;
	public int height, points;  
	protected int personality;
	public Prisoner left, right;
	private double bias;
	
	private PrisonerTree ptree;
	
	//for the bayesian
	public int numDefects = 0;
	public double [] priors, conditionals, pRetaliates, pEgivenRetaliates;
	public boolean retaliates = false;
	
	
	
	@SuppressWarnings("unused")
	private Prisoner() {}
	
	/*****
	 * Constructor for Prisoner object
	 * @param p - Prisoner object
	 * *********/
	public Prisoner(Prisoner p) {
		name = p.name;
		defect = p.defect;
		personality = p.personality;
		bias = p.bias;
		
		initBayesian();
	}
	
	/*****
	 * Constructor for Prisoner object
	 * @param fullName - string for the prisoner's name
	 * @param b - boolean to denote whether the prisoner's first action is to defect
	 * @param p - integer denoting prisoner's personality
	 * *********/
	public Prisoner(String fullName, boolean b, int p) {
		
		name = fullName;
		defect = b;
		personality = p;
		if ( (personality == Prisoner.BAYESIAN) || (personality == Prisoner.TIT_FOR_TAT) ) ptree = new PrisonerTree(true);
		bias = 0.65;
		
		initBayesian();
	}
	
	/*****
	 * Constructor for Prisoner object
	 * @param fullName - string for the prisoner's name
	 * @param b - boolean to denote whether the prisoner's first action is to defect
	 * @param p - integer denoting prisoner's personality
	 * @param bias - double denoting the amount of bias for COOPERATE_BIAS or DEFECT_BIAS
	 * *********/
	public Prisoner(String fullName, boolean d, int p, double bias) {
		
		name = fullName;
		defect = d;
		personality = p;
		if ( (personality == Prisoner.BAYESIAN) || (personality == Prisoner.TIT_FOR_TAT) ) ptree = new PrisonerTree(true);
		this.bias = bias;
		
		if( (personality == Prisoner.DEFECT_BIAS) || (personality == Prisoner.COOPERATE_BIAS) ){
			this.bias = bias;
		}
	}
	
	/**
	 * Method for updating the Prisoner's own tree of other Prisoner objects. This is only used by BAYESIAN and TIT_FOR_TAT personalities
	 * @param p - Prisoner object to be added to this Prisoner's tree
	*/
	public void updateSubjectiveTree(Prisoner p) {
		ptree.add( p ); 
	}
	
	
	/***
	 * Executes a prisoner's dilemma iteration between the current Prisoner object and another player. 
	 * @param p - the prisoner that this prisoner object is either cooperating with or defecting on. BAYESIAN and TIT_FOR_TAT use the argument Prisoner's previous behavior to determine how they should play this round 
	 * @param return - true if defecting, false if cooperating
	 * **/
	public boolean executeDilemma(Prisoner p) {
		switch(personality){
			case Prisoner.DEFECT_BOT:
				return true;
			case Prisoner.COOPERATE_BOT:
				return false;
			case Prisoner.RANDOM:
				return new java.util.Random().nextBoolean();
				
			case Prisoner.TIT_FOR_TAT:
				return ptree.getPrisoner(p.name).defect ? true : false;
			case Prisoner.BAYESIAN:
				Prisoner prisoner = ptree.getPrisoner(p.name);
				if(defect) prisoner.numDefects++;
				if (prisoner.defect) {
					
					//priors[0] = P ( defect player )
					//priors[1] = P ( co-op player )
					try{
						prisoner.conditionals[0] = .90; //assuming defect bot
						prisoner.conditionals[1] = .10; //assuming cooperate bot
						
						prisoner.priors = Stats.bayes(prisoner.priors, prisoner.conditionals);
						
						prisoner.retaliates = (prisoner.numDefects >= 1) ? true : false;
						
					}
					
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
				else {
					try{
						prisoner.conditionals[0] = .10; //assuming defect bot
						prisoner.conditionals[1] = .90; //assuming cooperate bot

						prisoner.priors = Stats.bayes(prisoner.priors, prisoner.conditionals);
						
						prisoner.retaliates = (prisoner.numDefects >= 1) ? false : true;
						
					}
					
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
				if (prisoner.priors[0] > .45) {
					return true;
				}
				else if ( (prisoner.priors[1] > .75) && (!prisoner.retaliates) ){
					//take advantage of cooperate bots
					prisoner.numDefects++;
					return true;
				}
				
				else if ( (prisoner.priors[1] > .75) && (prisoner.retaliates) ){
					prisoner.numDefects = 0;
					return false;
				}
				
				else if (prisoner.priors[0] <= .45) {
					return false;
				}
				
				
				
			case Prisoner.DEFECT_BIAS:
				return new java.util.Random().nextDouble() < bias ? true : false;
			case Prisoner.COOPERATE_BIAS:
				return new java.util.Random().nextDouble() < bias ? false : true;
			default:
				return false;
		}
	}
	

	/***
	 * Converts the integer behavior of the {@link Prisoner} object to a string
	 * @param p - Prisoner object 
	 * **/
    public static String getBehavior(Prisoner p) {
    	switch(p.personality){
    	case Prisoner.BAYESIAN:
    		return "Bayesian";
    	case Prisoner.COOPERATE_BIAS:
    		return "Cooperate Bias";
    	case Prisoner.COOPERATE_BOT:
    		return "Cooperate robot";
    	case Prisoner.DEFECT_BIAS:
    		return "Defect Bias";
    	case Prisoner.DEFECT_BOT:
    		return "Defect Robot";
    	case Prisoner.RANDOM:
    		return "Random";
    	case Prisoner.TIT_FOR_TAT:
    		return "Tit for Tat";
    	default:
    		return "No Input Behavior (?)";
    	}
    }
	
    /**
     * Initializes prior and conditional probabilities for the BAYESIAN Prisoner to use
     * */
	public void initBayesian(){
		priors = new double [2];
		conditionals = new double [2];
		
		priors[0] = 0.5;
		priors[1] = 0.5;
		
		//pRetaliates[0] = 0.5;
	}
	
	
	
	
}

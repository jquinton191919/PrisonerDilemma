package dilemma;

import java.util.ArrayList;

//class to keep a record of previous behavior of prisoners, i.e. a database
public class PrisonerTree {
	
	private boolean isAVL = false;
	
	
	//prisoner node that will function as the root of the tree
	private Prisoner root;
	
	public PrisonerTree() { root = null; }
	public PrisonerTree(boolean b) { root = null; isAVL = b; }
	
	//returns true if root is empty
	public boolean isEmpty() { return root == null;}
	
	public void makeEmpty() { root = null; }
	
	//add method to start recursion
	public void add(Prisoner p) { root = add(p.name, p.defect, p.points, p.personality, root); }
	
	//overloaded add method
	public Prisoner add(String name, boolean defect, int points, int personality, Prisoner p) {
		//if p is null, then p is become the prisoner info
		if (p == null) p = new Prisoner(name, defect, personality);
		
		else if (name.compareToIgnoreCase(p.name) < 0) {
			// if name comes before p.name alphabetically, then check the left node/prisoner and (recursively) compare
			p.left = add( name, defect, points, personality, p.left );
			
			//rotate if this is intended to be an AVL tree
			if(isAVL) {
				//check if left node's height is 2 higher than right node's height
				if( height(p.left) - height(p.right) == 2 ) {
					
					//if so, check whether the current name was inserted to the left
	            	if( name.compareToIgnoreCase(p.left.name) < 0 ) p = rotateRight(p);
	            	
	            	//if it was inserted on the right, then double rotation
	            	else p = rotateLeftRight(p);
	            }
			}
		}
		else if (name.compareToIgnoreCase(p.name) > 0) {
			// if name comes after p.name alphabetically, then check the right node/prisoner and (recursively) compare
			p.right = add( name, defect, points, personality, p.right );
			
			//rotate if this is intended to be an AVL tree
			if(isAVL) {
				//check if right node's height is 2 higher than left node's height
				if( height(p.right) - height(p.left) == 2 ) {
					
					//if so, check whether the current name was inserted to the right
	            	if( name.compareToIgnoreCase(p.right.name) > 0 ) p = rotateLeft(p);
	            	
	            	//if it was inserted on the left, then double rotation
	            	else p = rotateRightLeft(p);
	            }
			}
		}
		
		else if (name.compareToIgnoreCase(p.name) == 0) {
			// if name is equal to p.name alphabetically (ignoring case)...
			p.defect = defect;
			p.points = points;
		}
		
		//set height before returning
		if(isAVL) p.height = max( height( p.left ), height( p.right ) ) + 1;
		return p;
	}
	
	//returns the height
	private int height(Prisoner p) {
		return (p == null) ? -1 : p.height;
	}
	
	//finds the max height between the two inputs
	private int max(int left, int right) {
		return left > right ? left : right;
	}
	
	
	
	/********** LET'S HAVE SOME FUN ! ********************************************************************************************************/
	public static void executeDilemma(Prisoner p1, Prisoner p2) {
		//both cooperate
		if( (!p1.defect) && (!p2.defect) ) {
			p1.points++;
			p2.points++;
		}
		
		//only p1 cooperates
		else if( (!p1.defect) && (p2.defect) ) {
			p2.points = p2.points + 2;
		}
		
		//only p2 cooperates
		else if( (p1.defect) && (!p2.defect) ) {
			p1.points = p1.points + 2;
		}
		
		else { 
			//do nothing; both lose
		}
		
		if( (p1.personality == Prisoner.BAYESIAN) || (p1.personality == Prisoner.TIT_FOR_TAT) ) p1.updateSubjectiveTree(p2);
		if( (p2.personality == Prisoner.BAYESIAN) || (p2.personality == Prisoner.TIT_FOR_TAT) ) p2.updateSubjectiveTree(p1);
	}
	
	
	
	/**************** SEEK AND/OR DESTROY **************************/
	public String find(String name) {
		if (search(name)) return name + " is in this tree";
		else return name + " is NOT in this tree";
	}
	
	public Prisoner getPrisoner(String name){ return getPrisoner(root, name);}
	public Prisoner getPrisoner(Prisoner p, String name) {
		boolean found = false;
    	while ((p != null) && !found){
    		String prisonerName = p.name;
    		if (name.compareToIgnoreCase(prisonerName) < 0) p = p.left;

            else if (name.compareToIgnoreCase(prisonerName) > 0) p = p.right;

            else {
            	found = true;
            	return p;
            	}
    		return getPrisoner(p, name);
    		}
    	return p;

	}
	
	
	private boolean search(String name){ return search(root, name);}
    private boolean search(Prisoner p, String name){
    	boolean found = false;
    	while ((p != null) && !found){
    		String prisonerName = p.name;
    		if (name.compareToIgnoreCase(prisonerName) < 0) p = p.left;

            else if (name.compareToIgnoreCase(prisonerName) > 0) p = p.right;

            else {
            	found = true;
            	break;
            	}

            found = search(p, name);
    		}

        return found;

    }
    
    public int countNodes(){ return countNodes(root);}
    private int countNodes(Prisoner p){
    	if (p == null) return 0;
    	else {
    		int count = 1;
            count += countNodes(p.left);
            count += countNodes(p.right);
            return count;

        }

    }
	
	
	
	/**************** AVL ROTATIONS ******************************/
	//single rotation right
    private Prisoner rotateRight(Prisoner p) {
    	Prisoner pLeft = p.left;
    	p.left = pLeft.right;
        pLeft.right = p;

        p.height = max( height( p.left ), height( p.right ) ) + 1;
        pLeft.height = max( height( pLeft.left ), p.height ) + 1;

        return pLeft;
        }

    //single rotation left
    private Prisoner rotateLeft(Prisoner p) {
    	Prisoner pRight = p.right;
        p.right = pRight.left;
        pRight.left = p;

        p.height = max( height( p.left ), height( p.right ) ) + 1;
        pRight.height = max( height( pRight.right ), p.height ) + 1;

        return pRight;

    }

    //double rotation left>right
    private Prisoner rotateLeftRight(Prisoner p){
    	p.left = rotateLeft(p.left);
    	return rotateRight(p);
    	}

    //double rotation right>left
    private Prisoner rotateRightLeft(Prisoner p){
    	p.right = rotateRight( p.right );
    	return rotateLeft(p);
    	}
    
    
    
	/********************* PRINTING **********************************/
    //L-V-R
    public void printInOrder() {printInOrder(root);}
    private void printInOrder(Prisoner p){
    	if (p != null){    		
    		printInOrder(p.left);
    		System.out.println(p.name + ", "+ Prisoner.getBehavior(p) + ", SCORE: " + p.points);
    		printInOrder(p.right);
    		}
    	}

  
    //V-L-R
    public void printPreOrder(){printPreOrder(root);}
    private void printPreOrder(Prisoner p){
    	if (p != null){
    		System.out.println(p.name + ", "+ Prisoner.getBehavior(p));
    		printPreOrder(p.left);
    		printPreOrder(p.right);
    		}
    	}

    
    //L-R-V
    public void printPostOrder(){printPostOrder(root);}
    private void printPostOrder(Prisoner p){
    	if (p != null){
        	printPostOrder(p.left);             
            printPostOrder(p.right);
            System.out.println(p.name + ", "+ Prisoner.getBehavior(p));
            }
        }
    
    //L-x-R, return type of ArrayList<Prisoner> 
    public ArrayList<Prisoner> getPrisonerList() { 
    	 ArrayList<Prisoner> list = new ArrayList<Prisoner>();
    	 getPrisonerList(list, root);
    	 return list;
    	}
    public void getPrisonerList(ArrayList<Prisoner> list, Prisoner p){
    	if(p != null){
    		getPrisonerList(list, p.left);
    		list.add(p);
    		getPrisonerList(list, p.right);
    	}
    	
    }
    
    
}

/*
 * TCSS 342
 * Assignment 1 - Burger Baron 
 */

/**
 * Represents a burger at the Burger Baron. 
 *
 * @author Shannon Murphy
 * @version April 8, 2015
 *
 */
public class Burger {
	
	private int myPattyCount;
	
	/** The top half of the burger. */
	private final MyStack<String> myBurgerTop;
	
	/** The bottom half of the burger. */
	private final MyStack<String> myBurgerBottom;
	
	/** A temporary stack to move ingredients into when rearranging the other stacks. */
	private final MyStack<String> myTempBurger;
	
	/** Flag to keep track of whether there are veggies on the burger. */
	private boolean myVeggies;
	
	/** Flag to keep track of whether there is sauce on the burger. */
	private boolean mySauce;
	
	/** Flag to keep track of whether there is cheese on the burger. */
	private boolean myCheese;
		
	
	/**
	 * Constructs a new Burger that is either plain or has the works.
	 * A Baron Burger has the works, and a plain burger just has a bun and a patty.
	 * 
	 * @param theWorks true if the Burger is a Baron Burger, false if not.
	 */
	public Burger(boolean theWorks) {
		myPattyCount = 0;
		myBurgerTop = new MyStack<String>();
		myBurgerBottom = new MyStack<String>();
		myTempBurger = new MyStack<String>();
		
		myVeggies = false;
		mySauce = false;
		myCheese = false;
		myBurgerTop.push("Bun");
		myBurgerBottom.push("Bun");
		if (theWorks) {
			addCategory("Sauce");
			addCategory("Veggies");
			addPatty();
			addCategory("Cheese");
		} else {
			addPatty();
		}
	}
	
	/**
	 * Changes all of the patties to the passed patty type. 
	 * 
	 * @param thePattyType the type of patty to replace all patties on the Burger.
	 */
	public void changePatties(String thePattyType) {
		// change top patties
		if (myPattyCount == 2) {
			myBurgerTop.pop();
			myBurgerTop.push(thePattyType);
		} else if (myPattyCount == 3) {
			myBurgerTop.pop();
			myBurgerTop.pop();
			myBurgerTop.push(thePattyType);
			myBurgerTop.push(thePattyType);
		}
		// change bottom patty
		while (!searchBottom("Beef")) {
			myTempBurger.push(myBurgerBottom.pop());
		}
		myBurgerBottom.pop();
		myBurgerBottom.push(thePattyType);
		popTempBurger("Bottom");
	}
	
	/**
	 * Adds a patty to the Burger in the correct location up to the max of 3.
	 * Increments the patty count with each add.
	 */
	public void addPatty() {
		if (myPattyCount == 0) {	// add first patty
			if (myCheese) { 
				while (!searchBottom("Pepperjack")
						&& !searchBottom("Mozzarella")
						&& !searchBottom("Cheddar")) {
					myTempBurger.push(myBurgerBottom.pop());
				}
				myBurgerBottom.push("Beef");
				popTempBurger("Bottom");
			} else if (!myCheese) {
				while (!myBurgerBottom.isEmpty()) {
					myTempBurger.push(myBurgerBottom.pop());
				}
				myBurgerBottom.push("Beef");
				popTempBurger("Bottom");
			}
		} else if (myPattyCount == 1 || myPattyCount == 2) {	// add 2nd & 3rd patties
			myBurgerTop.push("Beef");
		} else if (myPattyCount >= 3) {
			System.err.println("You can only have 3 patties!");
		}
		myPattyCount++;
	}
	
	/**
	 * Removes one patty from the Burger down to the minimum of one.
	 * Decrements the patty count with each removal.
	 */
	public void removePatty() {
		if (myPattyCount > 1) {
			myBurgerTop.pop();
			myPattyCount--;
		} else {
			System.err.println("You must have at least one patty!");
		}	
	}
	
	/**
	 * Adds all items of the passed type to the Burger in the proper locations.
	 * 
	 * @param theType the category type to be added to the Burger.
	 */
	public void addCategory(String theType) {
		if ("Sauce".equals(theType)) {
			mySauce = true;
			addSauce();
		} else if ("Veggies".equals(theType)) {
			myVeggies = true;
			addVeggies();
		} else if ("Cheese".equals(theType)) {
			myCheese = true;
			addCheese();
		}
	}
	
	/**
	 * Removes all items of the passed type from the Burger.
	 * 
	 * @param theType the type of category to be removed from the Burger.
	 */
	public void removeCategory(String theType) {
		if ("Sauce".equals(theType)) {
			while (!searchTop("Baron-Sauce") 
					&& !searchTop("Mayonnaise")) {
				myTempBurger.push(myBurgerTop.pop());
			}
			while (searchTop("Baron-Sauce") 
					|| searchTop("Mayonnaise")) {
				myBurgerTop.pop();
			}
			popTempBurger("Top");
			myBurgerBottom.pop(); // remove bun
			while (searchBottom("Mustard") || searchBottom("Ketchup")) {
				myBurgerBottom.pop();
			}
			myBurgerBottom.push("Bun");
		} else if ("Veggies".equals(theType)) {
			
			if (myPattyCount == 2 || myPattyCount == 3) {
				while (!searchTop("Lettuce") && !searchTop("Tomato") && !searchTop("Onions")) {
					myTempBurger.push(myBurgerTop.pop());
				}
			}
			while (searchTop("Lettuce") || searchTop("Tomato") || searchTop("Onions")) {
				myBurgerTop.pop();
			}
			popTempBurger("Top");
			while (!searchBottom("Mushrooms")) {
				myTempBurger.push(myBurgerBottom.pop());
			}
			if (searchBottom("Mushrooms")) {
				myBurgerBottom.pop();
			}
			popTempBurger("Bottom");
		} else if ("Cheese".equals(theType)) {
			while (!searchBottom("Pepperjack") && !searchBottom("Mozzarella")
					&& !searchBottom("Cheddar")) {
				myTempBurger.push(myBurgerBottom.pop());
			}
			while (!myBurgerBottom.isEmpty()) {
				myBurgerBottom.pop();
			}
			popTempBurger("Bottom");
		}
	}
	
	/**
	 * Adds the passed ingredient type to the Burger in the proper location.
	 * 
	 * @param theType the ingredient to be added to the Burger.
	 */
	/**
	 * Adds the passed ingredient type to the Burger in the proper location.
	 * 
	 * @param theType the ingredient to be added to the Burger.
	 */
	
	public void addIngredient(String theType) {
		MyStack<String> fullBurger = createFullBurger();
		String next = ""; 
		boolean added = false;
		
		if (theType.equals("Pepperjack") || theType.equals("Cheddar") || theType.equals("Mozzarella")) {
			while (!myBurgerBottom.isEmpty()) { // empty stack to add cheese to bottom
				myTempBurger.push(myBurgerBottom.pop());
			}
			myBurgerBottom.push(theType);
			popTempBurger("Bottom");
		} else {
			while (!fullBurger.peek().equals(theType)) {
				fullBurger.pop();
			}
			fullBurger.pop(); // pop the type
			while (!added) {
				next = fullBurger.pop(); // the ingredient next to the type we want to add
//				System.err.println(next);
				while (!myBurgerBottom.isEmpty() && !searchBottom(next)) {
					myTempBurger.push(myBurgerBottom.pop());
				}
				if (!myBurgerBottom.isEmpty() && !searchTop(next)) {
					myBurgerBottom.push(theType);
					added = true;
				}
				popTempBurger("Bottom");
				
				if (!added) {
//					System.err.println(next);
					while (!myBurgerTop.isEmpty() && !searchTop(next)) {
						myTempBurger.push(myBurgerTop.pop());
					}
					if (!myBurgerTop.isEmpty()) {
						myBurgerTop.push(theType);
						added = true;
					}
					popTempBurger("Top");
				}
			}
		}
	}
	
//	public void addIngredient(String theType) {
//		MyStack<String> fullTop = createFullBurger("Top");
//		MyStack<String> fullBottom = createFullBurger("Bottom");
//		String next = "";
//		boolean topFound = false; 
//		boolean bottomFound = false; 
//		while (!fullTop.isEmpty()) {
//			if (!fullTop.peek().equals(theType)) {
//				fullTop.pop();
//			} else {
//				fullTop.pop();
//				topFound = true;
//				next = fullTop.peek();
//			}
//		}
//		if (topFound) {
//			while (!myBurgerTop.isEmpty() && !searchTop(next)) {
//				myTempBurger.push(myBurgerTop.pop());
//			}
//			myBurgerTop.push(theType);
//			popTempBurger("Top");
//		}
//		while (!fullBottom.isEmpty() && !topFound) {
//
//			if (!fullBottom.peek().equals(theType)) {
//				fullBottom.pop();
//			} else {
//				fullBottom.pop();
//				bottomFound = true;
//				next = fullBottom.peek();
//			}
//		}
//
//		if (bottomFound) {
//			while (!myBurgerBottom.isEmpty() && !searchBottom(next)) {
//				myTempBurger.push(myBurgerBottom.pop());
//			}
//			myBurgerBottom.push(theType);
//			popTempBurger("Bottom");
//		}
//	}

	
	/**
	 * Removes the passed ingredient type from the Burger.
	 * 
	 * @param theType the ingredient to be removed.
	 */
	public void removeIngredient(String theType) {
		boolean removed = false; // whether or not the type has been removed already
		while (!myBurgerTop.isEmpty() && !searchTop(theType)) {
			myTempBurger.push(myBurgerTop.pop());
		}
		if (!myBurgerTop.isEmpty() && searchTop(theType)) {
			myBurgerTop.pop();
			removed = true;
		}
		popTempBurger("Top");
		
		if (!removed) {
			while (!myBurgerBottom.isEmpty() && !searchBottom(theType)) {
				myTempBurger.push(myBurgerBottom.pop());
			}
			if (!myBurgerBottom.isEmpty() && searchBottom(theType)) {
				myBurgerBottom.pop();
			}
			popTempBurger("Bottom");
		}
	}
	
	/**
	 * Creates either a full top half or full bottom half to be used to check for proper ingredient locations.
	 * 
	 * @param theHalf the half of the burger being created.
	 * @return a burger with all ingredients.
	 */
	private MyStack<String> createFullBurger() {
//		
//		MyStack<String> fullStack = new MyStack<String>();
//		if ("Top".equals(theHalf)) {
//			fullStack.push("Onions");
//			fullStack.push("Tomato");
//			fullStack.push("Lettuce");
//			fullStack.push("Baron-Sauce");
//			fullStack.push("Mayonnaise");
//			fullStack.push("Bun");
//			fullStack.push("Pickle");
//		} else if ("Bottom".equals(theHalf)) {
//			fullStack.push("Pepperjack");
//			fullStack.push("Mozzarella");
//			fullStack.push("Cheddar");
//			fullStack.push("Beef");
//			fullStack.push("Mushrooms");
//			fullStack.push("Mustard");
//			fullStack.push("Ketchup");
//			fullStack.push("Bun");
//		}
//		return fullStack;
		MyStack<String> fullStack = new MyStack<String>();
			fullStack.push("Onions");
			fullStack.push("Tomato");
			fullStack.push("Lettuce");
			fullStack.push("Baron-Sauce");
			fullStack.push("Mayonnaise");
			fullStack.push("Bun");
			fullStack.push("Pickle");
			fullStack.push("Pepperjack");
			fullStack.push("Mozzarella");
			fullStack.push("Cheddar");
			fullStack.push("Beef");
			fullStack.push("Mushrooms");
			fullStack.push("Mustard");
			fullStack.push("Ketchup");
			fullStack.push("Bun");
		return fullStack;
	}
	
	/**
	 * Pops all of the items off of the temporary burger and pushes them
	 * onto either the top or bottom half of the main burger.
	 * 
	 * @param theHalf either the top half or bottom half of the burger.
	 */
	private void popTempBurger(String theHalf) {
		if ("Bottom".equals(theHalf)) {
			while (!myTempBurger.isEmpty()) {
				myBurgerBottom.push(myTempBurger.pop());
			}
		} else if ("Top".equals(theHalf)){
			while (!myTempBurger.isEmpty()) {
				myBurgerTop.push(myTempBurger.pop());
			}
		}
	}
	
	/**
	 * Searches the bottom half of the Burger for the passed type by calling peek().
	 * 
	 * @param theType the ingredient being searched for.
	 * @return true if peek() matches the desired ingredient, false otherwise.
	 */
	private boolean searchBottom(String theType) {
		return myBurgerBottom.peek().equals(theType);
	}
	
	/**
	 * Searches the top half of the Burger for the passed type by calling peek().
	 * 
	 * @param theType the ingredient being searched for.
	 * @return true if peek() matches the desired ingredient, false otherwise.
	 */
	private boolean searchTop(String theType) {
		return myBurgerTop.peek().equals(theType);
	}
	
	/** Adds all sauces to the burger. */
	private void addSauce() {
		// add top sauces
		if (!myVeggies) {
			myBurgerTop.push("Mayonnaise");
			myBurgerTop.push("Baron-Sauce");
		} else if (myVeggies) {
			while (searchTop("Lettuce")
					|| searchTop("Tomato")
					|| searchTop("Onions")) {
				myTempBurger.push(myBurgerTop.pop());
			}
			myBurgerTop.push("Mayonnaise");
			myBurgerTop.push("Baron-Sauce");
			popTempBurger("Top");
		}
		// add bottom sauces
		myBurgerBottom.pop(); // remove bun
		myBurgerBottom.push("Mustard");
		myBurgerBottom.push("Ketchup");
		myBurgerBottom.push("Bun"); // add bun
	}
	
	/** Adds all veggies to the burger. */
	private void addVeggies() {
		myBurgerTop.push("Lettuce");
		myBurgerTop.push("Tomato");
		myBurgerTop.push("Onions");
		
		myBurgerBottom.pop(); // remove bun
		if (mySauce) {
			if (searchBottom("Ketchup")) {
				myTempBurger.push(myBurgerBottom.pop());
			}
			if (searchBottom("Mustard")) {
				myTempBurger.push(myBurgerBottom.pop());
			}
			myBurgerBottom.push("Mushrooms");
		} else {
			myBurgerBottom.push("Mushrooms");
		}
		popTempBurger("Bottom");
		myBurgerBottom.push("Bun");
	}
	
	/** Adds all cheese to the burger. */
	private void addCheese() {
		while (!myBurgerBottom.isEmpty()) { // empty stack to add cheese to bottom
			myTempBurger.push(myBurgerBottom.pop());
		}
		myBurgerBottom.push("Pepperjack");
		myBurgerBottom.push("Mozzarella");
		myBurgerBottom.push("Cheddar");
		popTempBurger("Bottom");
	}
	
    @Override
    /** Returns a String representation of the Burger by combining the top half
     * and bottom half. */
	public String toString() {
		MyStack<String> finalOrder = new MyStack<String>();
		while (!myBurgerBottom.isEmpty()) {
			finalOrder.push(myBurgerBottom.pop());
		}
		while (!myBurgerTop.isEmpty()) {
			finalOrder.push(myBurgerTop.pop());
		}
		return finalOrder.toString();
	}
}

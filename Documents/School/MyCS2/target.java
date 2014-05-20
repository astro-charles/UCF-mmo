import java.util.ArrayList;
import java.util.Scanner;

public class target {
	static private ArrayList<Integer> listOfInts = new ArrayList<Integer>();
	static private int sumTarget = 0;
	
	/*
	  	2 <- test cases
	    5 14 <- n, target
		3 5 6 7 9 <- listOfInts, also end of inner loop
		3 11 <- next test case and (n, target)
		1 3 6 
	 */
	static public void gatherUserVars() {
			Scanner sc = new Scanner(System.in);
			int testCases = sc.nextInt();
			boolean exist;
			for(int i=0;i<testCases;i++) {
				int n =  sc.nextInt();
				sumTarget = sc.nextInt();
				for(int k=0;k<n;k++) {
					listOfInts.add(sc.nextInt());
				}
				exist = sumPairExist();
				if(exist) {
					System.out.println("YES");
				}else {
					System.out.println("NO");
				}
				
				listOfInts.clear();
			}
			sc.close();
	}
	
	static public boolean sumPairExist() {
		int indexDest = listOfInts.size()-1;
//		System.out.print(indexDest);
		/* this is to shorten the list increases it to O(n+n) but long run O(n) */
		for (int i=0;i<listOfInts.size();i++) {
			if(listOfInts.get(i) >= sumTarget) 
				/* if the element is bigger than our target because the list is sorted don't worry about anything
				 * after that index */
				indexDest = i - 1;
		}
		
		int i=0;
		int j=indexDest;
		for(int k=0;k<indexDest;k++) {
			int summationOfPair = listOfInts.get(i) + listOfInts.get(j);
			
			if(summationOfPair == sumTarget) { 
				return true;
			}else if(summationOfPair < sumTarget) { 
				i++;
			}else if(summationOfPair > sumTarget) {
				j--;
			}else if(i >= j) {
				return false;
			}
		}
		return false;
	}
	
	public static void main(String[] args) { 
		gatherUserVars();
	}

}

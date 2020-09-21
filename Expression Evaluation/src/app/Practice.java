package app;

import structures.Stack;

public class Practice {
	public static void main (String[] args) {
		Stack<Integer> a = new Stack<Integer>();
		
		a.push(1);
		a.push(2);
		a.push(3);
		a.push(4);
		a.push(5);
		
		a.pop();
		
		int test = a.peek();
		
		System.out.print(test);
	}
}
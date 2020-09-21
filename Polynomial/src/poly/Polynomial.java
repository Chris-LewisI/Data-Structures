package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author Chris Ibraheem (cli14)
 *
 */
public class Polynomial {

	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	public static Node add(Node poly1, Node poly2) {
		/*
		 * Create nodes that reference polynomial 1 & 2 so that we may be able to work through them
		 * Create a coefficient variable to help simplify the terms
		 * Create an addi node so that we can return this as the new linked lists (we cannot return nodes that are already used in the original linked lists)
		 * sptr node will be used to help carry out the two pointer method to help traverse through the LL
		 * The boolean identical will check whether or not the two polynomials are the same
		 */
		Node p1 = poly1;
		Node p2 = poly2;
		float coe;
		Node addi = null;
		Node sptr = null;
		boolean identical = false;

		while(p1 != null || p2 != null) {

			identical = false;

			if((p1 != null && p2 != null)) {
				identical = (p1.term.degree == p2.term.degree);
			}

			if(identical) {
				coe = p1.term.coeff + p2.term.coeff;
				
				if(addi == null) {
					addi = new Node(coe, p1.term.degree, null);
					sptr = addi;
				}
				
				else {
					sptr.next = new Node(coe, p1.term.degree, null);
					sptr = sptr.next;
				}
				
				p1 = p1.next;
				p2 = p2.next;
			}
			else {
				if(p1 != null && p2 != null) {
					if(addi == null) {
						if(p1.term.degree > p2.term.degree) {
							addi = new Node(p2.term.coeff, p2.term.degree, null);
							sptr = addi;
							p2 = p2.next;
						}
						else {
							addi = new Node(p1.term.coeff, p1.term.degree, null);
							sptr = addi;
							p1 = p1.next;
						}
					}
					else {
						if(p1.term.degree > p2.term.degree) {
							sptr.next = new Node(p2.term.coeff, p2.term.degree, null);
							sptr = sptr.next;
							p2 = p2.next;
						}
						else {
							sptr.next = new Node(p1.term.coeff, p1.term.degree, null);
							sptr = sptr.next;
							p1 = p1.next;
						}
					}
				}
				else {
					if(p1 == null) {
						if(addi == null) {
							addi = new Node(p2.term.coeff, p2.term.degree, null);
							sptr = addi;
						}
						else {
							sptr.next = new Node(p2.term.coeff, p2.term.degree, null);
							sptr = sptr.next;
						}
						p2 = p2.next;
					}
					else {
						if (addi == null) {
							addi = new Node(p1.term.coeff, p1.term.degree, null);
							sptr = addi;
						}
						else {
							sptr.next = new Node(p1.term.coeff, p1.term.degree, null);
							sptr = sptr.next;
						}
						p1 = p1.next;
					}
				}
			}
		}
		sptr = addi;
		Node pre = null;
		while(sptr != null) {
			if(addi.term.coeff == 0) {
				addi = addi.next;
			}
			else if(sptr.term.coeff == 0) {
				if(sptr.next == null) {
					pre.next = null;
					break;
				}
				pre.next = sptr.next;
			}
			pre = sptr;
			sptr = sptr.next;
		}
		return addi;
	}

	public static Node multiply(Node poly1, Node poly2) {
		
		/*
		 * make 4 new nodes
		 * 
		 * nested loop (for)
		 * take each node from the linked list and multiply by the entirety of the other linked list
		 * then simplify the two of them by using the same degrees but return the simplified nodes in a new linked list
		 * Must return a new linked lists because cannot use nodes from old linked list
		 * 
		 */
		
		Node multi = null;
		float coe;
		int degr;
		Node mptr = null;
		Node tmp = null;
		
		//two pointer method
		for(Node p1 = poly1; p1 != null; p1 = p1.next) {
			for(Node p2 = poly2; p2 != null; p2 = p2.next) {
				if(tmp == null) {
				coe = p1.term.coeff * p2.term.coeff;
				degr = p1.term.degree + p2.term.degree;
				
				tmp = new Node(coe, degr, null);
				mptr = tmp;
				}
				
				else {
				coe = p1.term.coeff * p2.term.coeff;
				degr = p1.term.degree + p2.term.degree;
				
				mptr.next = new Node(coe, degr, null);
				mptr = mptr.next;
				}
			}
			
			multi = add(tmp, multi);
			tmp = null;
		}
		
		return multi;
	}

	public static float evaluate(Node poly, float x) {
		float endResult = 0;
		
		while (poly != null) {
			endResult += poly.term.coeff * Math.pow(x, poly.term.degree);
			poly = poly.next;
		}
		
		return endResult;
	}

	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String returnValue = poly.term.toString();
		
		for (Node current = poly.next; current != null; current = current.next) {
			returnValue = current.term.toString() + " + " + returnValue;
		}
		
		return returnValue;
	}
}

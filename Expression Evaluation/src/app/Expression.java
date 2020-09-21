package app;
import java.io.*;
import java.util.*;
import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

    public static void 

    makeVariableLists(String exStr, ArrayList<Variable> vars, ArrayList<Array> arrays) {

        String operatorDel = "+-*/()]";
        
        String t = "";
        
    	for(int i = 0; i<exStr.length();i++) {

            System.out.println(exStr.charAt(i));
            
    		if (chara.isDigit(exStr.charAt(i))) {

                continue;
                
    		}
    		if (chara.isWhitespace(exStr.charAt(i))) {

                continue;
                
    		}
    		if((delims.contains(chara.toString(exStr.charAt(i))) != true)){ 
    			t += exStr.charAt(i);
    		}else if (operatorDel.contains(chara.toString(exStr.charAt(i)))){ 
    			if(t != "") {
    				Variable tp = new Variable(t);
    				if(vars.contains(tp) != true) {
		    			vars.add(tp);
    				}
		    	t = "";
    			}
    		}else if (exStr.charAt(i) == '[') {
    			if(t != "") {
	    			Array tp = new Array(t);
	    			arrays.add(tp);
	    			t = "";
    			}
    		}
    	}
    	Variable tp = new Variable(t);
    	if((vars.contains(tp) != true) && (delims.contains(t)!=true)) {
    	vars.add(tp);
    	}
    }
    
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 

    throws IOException {

        while (sc.hasNextLine()) {

            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    public static float 

    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    
        Stack <Integer> numOne = new Stack<Integer>();
        
        Stack <Integer> numTwo = new Stack<Integer>();
        
        Stack <Character>op = new Stack<Character>();
        
        Stack <Character>opp = new Stack<Character>();
        
        String opps = "+-/*";
        
        String temp = "";
        
        Integer tp1;
        
    	Character tp2 = ' ';
    	
    	String ans = varstring(expr, vars, arrays);
        System.out.println("string form is " + ans);
        
    	for( int i = 0; i < ans.length(); i++) {
    		
    		if(!delims.contains((Character.toString(ans.charAt(i))))) {

                temp += ans.charAt(i);
                
    		}else if(delims.contains((Character.toString(ans.charAt(i))))) {

                numOne.push(Integer.parseInt(temp));
                
                op.push(ans.charAt(i));
                
                temp = "";
                
            }
            
    	}
    	if (temp!="" && !delims.contains(temp)) {

            numOne.push(Integer.parseInt(temp));
            
    	}
    	while(!numOne.isEmpty()) {

            numTwo.push(numOne.pop());
            
    	}
        while(!op.isEmpty()) 
        {		

            opp.push(op.pop());
            
    	}
    	
        int tempp;
        
        char operation = ' ';
        
            System.out.println("hello world");
            
        	while (numTwo.size()>1) {

                operation = opp.pop();
                
	        	if(opps.contains(Character.toString(operation))){

	            	if(opp.isEmpty()) {

                        System.out.println("simple doop and operation is "+operation);
                        
                        tempp = doop(operation, numTwo.pop(), numTwo.pop());
                        
                        numTwo.push(tempp);
                        
	            	}else if((opp.peek()=='*' || opp.peek()=='/') && (operation == '+' || operation == '-')) {
                    
                        System.out.println("complicated doop");
                        
                        tp1 = numTwo.pop();
                        
                        System.out.println("num pop "+tp1);
                        
                        tp2 = operation;
                        
                        System.out.println("opp pop "+tp2);
                        
                        numTwo.push(doop(opp.pop(), numTwo.pop(), numTwo.pop()));
                        
                        System.out.println("numOne.push "+numTwo.peek());
                        
                        numTwo.push(tp1);
                        
                        System.out.println("num push "+tp1);
                        
                        opp.push(tp2);
                        
                        System.out.println("opp push "+tp2);
                        
	        		}else {

                        System.out.println("simple doop and numTwo peek "+ numTwo.peek());
                        
                    numTwo.push(doop(operation, numTwo.pop(), numTwo.pop()));
                    
                    }
                    
                }
                
        	}
                
        return numTwo.pop();
    	
    }
    
    private static String varstring(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {

    String opdelims = "+-*/()]";
    
    String paren = "[";
    
    String temp = "";
    
    String ans = "";
    
    Variable tempp = null;
    
	
	for(int i = 0; i<expr.length();i++) {

    	if(Character.isWhitespace(expr.charAt(i))) {

            continue;
            
        }
        
		if(Character.isDigit(expr.charAt(i))) {

            ans += expr.charAt(i);
            
            continue;
            
    	}else if(paren.contains(Character.toString(expr.charAt(i)))) {

            ans += temp;
            
            ans += expr.charAt(i);
            
            temp = "";
            
            continue;
            
    	}else if(opdelims.contains(Character.toString(expr.charAt(i))) != true) {
            
            temp += expr.charAt(i);
            
    	}else if (opdelims.contains(Character.toString(expr.charAt(i)))){

	    		if(temp != "") {

                    tempp = vars.get(vars.indexOf(new Variable(temp)));
                    
                    ans += tempp.value;
                    
	    		} 
             ans += expr.charAt(i);
             
             tempp = null;
             
             temp = "";
             
     	}else if(expr.charAt(i)=='[') {

             ans += temp;
             
             ans += expr.charAt(i);
             
             temp = "";
             
         }
 	}
	if (temp != "") {

        tempp = vars.get(vars.indexOf(new Variable(temp)));
        
        ans += tempp.value;
        
	}
	
    System.out.println("string ans is " + ans);
    
    return ans;
    

    }
    private static int doop(char operation, int x, int y) {
    	
    	if(operation == '*') {
    		System.out.println("x is "+x+"y is "+y);
        	return x*y;
		}else if (operation == '/') {
			System.out.println("x is "+x+"y is "+y);
			return x/y;
		}else if(operation == '-') {
			System.out.println("x is "+x+"y is "+y);
			return x-y;
		}else if (operation == '+') {
			System.out.println("x is "+x+"y is "+y);
			return x+y;
		}
    	return 0;
    }
}
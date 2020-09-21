package friends;

import structures.Queue;

import structures.Stack;

import java.util.*;


public class Friends {

	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		if (g == null) {
			
			return null;
			
		}
		
		if (p1 == null) {
			
			return null;
			
		}
		
		if (p2 == null) {
			
			return null;
			
		}
		
		
		ArrayList<String> shortestPath = new ArrayList<String>();
		
		boolean[] visit = new boolean[g.members.length];
		
		Queue<Person> shortestPathQueue = new Queue<Person>();
		
		Person[] visitCompleted = new Person[g.members.length];
		
		int index = g.map.get(p1);
		
		shortestPathQueue.enqueue(g.members[index]);
		
		visit[index] = true;
		
		while (shortestPathQueue.isEmpty() == false) {
			
			Person pivot = shortestPathQueue.dequeue();
			
			int pivotIndex = g.map.get(pivot.name);
			
			visit[pivotIndex] = true;
			
			Friend neighbor = pivot.first;
			
			if (neighbor == null) {
				
				return null;
				
			}
			
			while (neighbor != null) {
				
				if (visit[neighbor.fnum] == false) {
					
					visit[neighbor.fnum] = true;
					
					visitCompleted[neighbor.fnum] = pivot; 
					
					shortestPathQueue.enqueue(g.members[neighbor.fnum]);
					
					
					if (g.members[neighbor.fnum].name.equals(p2)) {
						
						pivot = g.members[neighbor.fnum];
						
						while (pivot.name.equals(p1) == false) {
							
							shortestPath.add(0, pivot.name);
							
							pivot = visitCompleted[g.map.get(pivot.name)];
							
						}
						
						shortestPath.add(0, p1);
						
						return shortestPath;
						
					}
					
				}
				
				neighbor = neighbor.next;
				
			}
			
		}
		
		return null;
		
	}
	
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		if (g == null) {
			
			return null;
			
		}
		
		if (school == null) {
			
			return null;
			
		}
		
		ArrayList<ArrayList<String>> listOfCliques= new ArrayList<ArrayList<String>>();
		
		boolean[] visited = new boolean[g.members.length];
		
		return BFS(g, g.members[0], listOfCliques, visited, school);
		
	}
	
	private static ArrayList<ArrayList<String>> BFS(Graph g, Person start, ArrayList<ArrayList<String>> listOfCliques, boolean[] visited, String school){
		
		ArrayList<String> cliquesProduct = new ArrayList<String>();
		
		Queue<Person> cliqueQueue = new Queue<Person>();
		
		cliqueQueue.enqueue(start);
		
		visited[g.map.get(start.name)] = true;
		
		Person pivot = new Person();
		
		Friend neighbor;
		
		if (start.school == null || start.school.equals(school) == false) {
			
			cliqueQueue.dequeue();
			
			for (int j = 0; j < visited.length; j++) {
				
				if (visited[j] == false) {
					
					return BFS(g, g.members[j], listOfCliques, visited, school);
					
				}
				
			}
			
		}
		
		while (cliqueQueue.isEmpty() == false) {
			
			pivot = cliqueQueue.dequeue();
			
			neighbor = pivot.first;
			
			cliquesProduct.add(pivot.name);
			
			while (neighbor != null) {
				
				if (visited[neighbor.fnum] == false) {
					
					if (g.members[neighbor.fnum].school == null) {
						
					}
					
					else {
						
						if (g.members[neighbor.fnum].school.equals(school)) {
							
							cliqueQueue.enqueue(g.members[neighbor.fnum]);
							
						}
						
					}
					
					visited[neighbor.fnum] = true;
					
				}
				
				neighbor = neighbor.next;
				
			}
			
		}
		
		if (listOfCliques.isEmpty() == false && cliquesProduct.isEmpty()) {
			
		} 
		
		else {
			
			listOfCliques.add(cliquesProduct);
			
		}
		
		for (int i = 0; i < visited.length; i++) {
			
			if (visited[i] == false) {
				
				return BFS(g, g.members[i], listOfCliques, visited, school);
				
			}
			
		}
		
		return listOfCliques;
		
	}

		public static ArrayList<String> connectors(Graph g) {
		
		if (g == null) {
			
			return null;
			
		}
		
		ArrayList<String> connectors = new ArrayList<String>();
		
		boolean[] visited = new boolean[g.members.length];
		
		ArrayList<String> predecessor = new ArrayList<String>();
		
		int[] numbersOfDFS= new int[g.members.length];
		
		int[] before = new int[g.members.length];
		
		
		for (int i = 0; i < g.members.length; i++){
			
			if (visited[i] == false) {
				
				connectors = DFS(connectors, g, g.members[i], visited, new int[] {0,0}, numbersOfDFS, before, predecessor, true);
				
			}
			
		}
		
		return connectors;
		
	}
	
	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person start, boolean[] visited, int[] count, int[] numbersOfDFS, int[] back, ArrayList<String> backward, boolean started){
		
		visited[g.map.get(start.name)] = true;
		
		Friend neighbor = start.first;
		
		numbersOfDFS[g.map.get(start.name)] = count[0];
		
		back[g.map.get(start.name)] = count[1];
		
		while (neighbor != null) {
			
			if (visited[neighbor.fnum] == false) {
				
				count[0]++;
				
				count[1]++;
				
				connectors = DFS(connectors, g, g.members[neighbor.fnum], visited, count, numbersOfDFS, back, backward, false);
				
				if (numbersOfDFS[g.map.get(start.name)] <= back[neighbor.fnum]) {
					
					if ((connectors.contains(start.name) == false && backward.contains(start.name)) || (connectors.contains(start.name) == false && started == false)) {
						
						connectors.add(start.name);
						
					}
					
				}
				
				else {
					
					int first = back[g.map.get(start.name)];
					
					int second = back[neighbor.fnum];
					
					if (first < second) {
						
						back[g.map.get(start.name)] = first;
						
					}
					
					else {
						
						back[g.map.get(start.name)] = second;
						
					} 
					
				}		
				
			backward.add(start.name);
			
			}
			
			else {
				
				int third = back[g.map.get(start.name)];
				
				int fourth = numbersOfDFS[neighbor.fnum];
				
				if (third < fourth) {
					
					back[g.map.get(start.name)] = third;
					
				}
				
				else {
					
					back[g.map.get(start.name)] = fourth;
					
				}
				
			}
			
			neighbor = neighbor.next;
			
		}
		
		return connectors;
		
	}
	
}

//Chris Ibraheem

package trie;

import java.util.ArrayList;

public class Trie {
	
	private Trie () { }
	
	public static TrieNode buildTrie(String[] allWords) {
		
		TrieNode trieRoot = new TrieNode(null, null, null);
		
		if(allWords.length == 0) return trieRoot;
		
		trieRoot.firstChild = new TrieNode(new Indexes(0, (short)(0), (short)(allWords[0].length() - 1)), null, null);
		
		TrieNode pointer = trieRoot.firstChild, lastSeenVal = trieRoot.firstChild;
		
		int similarTo = -1, startIndex = -1, endIndex = -1, wordIndex = -1;
		
		for(int index = 1; index < allWords.length; index++) {
			
			String phrase = allWords[index];
			
			while(pointer != null) {
				
				startIndex = pointer.substr.startIndex;
				
				endIndex = pointer.substr.endIndex;
				
				wordIndex = pointer.substr.wordIndex;
				
				if(startIndex > phrase.length()) {
					
					lastSeenVal = pointer;
					
					pointer = pointer.sibling;
					
					continue;
						
				}
				
				similarTo = similarTill(allWords[wordIndex].substring(startIndex, endIndex+1), phrase.substring(startIndex));
				
				if(similarTo != -1)
					
					similarTo += startIndex;
				
				if(similarTo == -1) {
					
					lastSeenVal = pointer;
					
					pointer = pointer.sibling;
					
				}
				
				else {
					
					if(similarTo == endIndex) {
						
						lastSeenVal = pointer;
						
						pointer = pointer.firstChild;
						
					}
					
					else if (similarTo < endIndex) {
						
						lastSeenVal = pointer;
						
						break;
						
					}
					
				}
				
			}
			
			if(pointer == null) {
				
				Indexes indexes = new Indexes(index, (short)startIndex, (short)(phrase.length()-1));
				
				lastSeenVal.sibling = new TrieNode(indexes, null, null);
				
			} else {
				
				Indexes currentIndexes = lastSeenVal.substr;
				
				TrieNode currFirstChild = lastSeenVal.firstChild;
				
				Indexes currentWordNewIndexes = new Indexes(currentIndexes.wordIndex, (short)(similarTo+1), currentIndexes.endIndex);
				
				currentIndexes.endIndex = (short)similarTo;
				
				lastSeenVal.firstChild = new TrieNode(currentWordNewIndexes, null, null);
				
				lastSeenVal.firstChild.firstChild = currFirstChild;
				
				lastSeenVal.firstChild.sibling = new TrieNode(new Indexes((short)index, (short)(similarTo+1), (short)(phrase.length()-1)), null, null);
				
			}
			
			pointer = lastSeenVal = trieRoot.firstChild;
			
			similarTo = startIndex = endIndex = wordIndex = -1;
			
		}
		
		return trieRoot;
		
	}
	
	
	
	private static int similarTill(String containTrie, String insertVal) {
		
		int untill = 0;
		
		while(untill < containTrie.length() && untill < insertVal.length() && containTrie.charAt(untill) == insertVal.charAt(untill)) untill++;
		
		return (untill-1);
	}
	
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		if(root == null) return null;
		
		ArrayList<TrieNode> matches = new ArrayList<>();
		
		TrieNode pointer = root;
		
		while(pointer != null) {
			
			if(pointer.substr == null) pointer = pointer.firstChild;
			
			String s1 = allWords[pointer.substr.wordIndex];
			
			String a1 = s1.substring(0, pointer.substr.endIndex+1);
			
			if(s1.startsWith(prefix) || prefix.startsWith(a1)) {
				
				if(pointer.firstChild != null) {
					
					matches.addAll(completionList(pointer.firstChild, allWords, prefix));
					
					pointer = pointer.sibling;
					
				} else {
					
					matches.add(pointer);
					
					pointer = pointer.sibling;
					
				}
				
			} else {
				
				pointer = pointer.sibling;
				
			}
			
		}
		
		return matches;
		
	}
	
	public static void print(TrieNode root, String[] allWords) {
		
		System.out.println("\nTRIE\n");
		
		print(root, 1, allWords);
		
	}
	
	private static void print(TrieNode root, int indent, String[] phrases) {
		
		if (root == null) {
			
			return;
			
		}
		
		for (int i=0; i < indent-1; i++) {
			
			System.out.print("    ");
			
		}
		
		if (root.substr != null) {
			
			String pre = phrases[root.substr.wordIndex].substring(0, root.substr.endIndex+1);
			
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			
			System.out.print("    ");
			
		}
		
		System.out.print(" ---");
		
		if (root.substr == null) {
			
			System.out.println("root");
			
		} else {
			
			System.out.println(root.substr);
			
		}
		
		for (TrieNode pointer=root.firstChild; pointer != null; pointer=pointer.sibling) {
			
			for (int i=0; i < indent-1; i++) {
				
				System.out.print("    ");
				
			}
			
			System.out.println("     |");
			
			print(pointer, indent+1, phrases);
			
		}
		
	}
	
 }

package lse;

import java.io.*;

import java.util.*;

public class LittleSearchEngine {
	
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	HashSet<String> noiseWords;
	
	public LittleSearchEngine() {

		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);

		noiseWords = new HashSet<String>(100,2.0f);

	}
	
	/**
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */

	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {

		if (docFile == null) {

			throw new FileNotFoundException();

		}

		HashMap<String,Occurrence> finalMap = new HashMap<String,Occurrence>();

		Scanner scan1 = new Scanner(new File(docFile));

		while (scan1.hasNext()) {
			
			String access = getKeyword(scan1.next());

			if (access != null) {
				
				if(finalMap.containsKey(access)) {
					
					Occurrence first = finalMap.get(access); 

					first.frequency++;
					
				}
				
				else {

					Occurrence order = new Occurrence(docFile,1);

					finalMap.put(access, order);
					
				}
			}
		}
		return finalMap;
	}
	
	/**
	 * @param kws
	 */

	public void mergeKeywords(HashMap<String,Occurrence> kws) {

		for (String key: kws.keySet()) {
			
			ArrayList<Occurrence> combined = new ArrayList<Occurrence>();
			
			if(keywordsIndex.containsKey(key)) {
				
				combined = keywordsIndex.get(key);
				
			}
			
			combined.add(kws.get(key));

			insertLastOccurrence(combined);

			keywordsIndex.put(key,combined);

		}
	}
	
	/**
	 * @param word
	 * @return
	 */

	private String punctuatuionRemove(String word) {

		int number = 0;

		while (number <word.length()) {
			
			char r = word.charAt(number);

			if (!(Character.isLetter(r))) {
				
				break;

			}

			number++;

		}
		
		return word.substring(0,number);

	}
	
	private boolean charFilter (String word) {

		boolean filter1 = false;

		int number = 0;

		while(number < word.length()) {

			char r  = word.charAt(number);
			
			if(!(Character.isLetter(r))) {

				filter1 = true;

			}
			
			if((filter1) && (Character.isLetter(r))) {

				return true;

			}

			number++;

		}
		
		return false;

	}
	
	public String getKeyword(String word) {

		if (word == null || word.equals(null)) {
			
			return null;
		
		}

		word = word.toLowerCase();

		if (charFilter(word)) {
			
			return null;
			
		}

		word = punctuatuionRemove(word);

		if (noiseWords.contains(word)) {
			
			return null;
			
		}

		if (word.length() <= 0) {
			
			return null;
			
		}
		
		return word;

	}
	
	/**
	 * @param occs
	 * @return
	 */

	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {	

		if(occs.size() == 1)

		{

			return null;

		}

		ArrayList<Integer> centerArrayList = new ArrayList<Integer>();

		Occurrence temp = occs.get(occs.size()-1);

		occs.remove(occs.size()-1);

		int up = 0; 

		int low = occs.size()-1;

		int cent = 0;

		int centFreq;
		
		while(up <= low)
		{
			cent = (up + low)/2;

			centFreq = occs.get(cent).frequency;
				
			if(centFreq == temp.frequency)

			{

				centerArrayList.add(cent);

				break;

			}
			
			if(centFreq < temp.frequency)

			{

				low = cent - 1;

				centerArrayList.add(cent);

			}
			
			if(centFreq > temp.frequency)

			{

				up = cent + 1;

				centerArrayList.add(cent);

				cent++;

			}
		}

		occs.add(cent, temp);

		return centerArrayList;

	}
	
	/**
	 * @param docsFile 
	 * @param noiseWordsFile 
	 * @throws FileNotFoundException
	 */

	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {

		Scanner scan2 = new Scanner(new File(noiseWordsFile));

		while (scan2.hasNext()) {

			String word = scan2.next();

			noiseWords.add(word);

		}
		
		scan2 = new Scanner(new File(docsFile));

		while (scan2.hasNext()) {

			String docFile = scan2.next();

			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);

			mergeKeywords(kws);

		}

		scan2.close();

	}
	
	/**
	 * @param kw1
	 * @param kw1
	 * @return
	 */

	public ArrayList<String> top5search(String kw1, String kw2) {

		ArrayList<String> out = new ArrayList<String>();

		ArrayList<Occurrence> List1 = keywordsIndex.get(kw1);

		ArrayList<Occurrence> List2 = keywordsIndex.get(kw2);
		

		if(List1 == null && List2 == null) {

			return out;

			}
		
		boolean firstSearch = false;

		boolean secondSearch = false;

		int kw1length = 0;

		int kw2length = 0;
		
		if (List1 == null) {

			secondSearch = true;

			kw2length =(keywordsIndex.get(kw2)).size();

			}
		
		if(List2 == null){

			firstSearch = true;

			kw1length=(keywordsIndex.get(kw1)).size();

			}

		
		if(keywordsIndex.containsKey(kw2) && keywordsIndex.containsKey(kw1)){

			kw1length = (keywordsIndex.get(kw1)).size();

			kw2length = (keywordsIndex.get(kw2)).size();

			}
		
		int num = 0;

		int x = 0;

		int y = 0;	
		
		while(firstSearch == false && secondSearch == false && num < 10){

				int kw1freq = keywordsIndex.get(kw1).get(x).frequency;

				int kw2freq = keywordsIndex.get(kw2).get(y).frequency;

				
				if(kw1freq >= kw2freq){

					out.add(keywordsIndex.get(kw1).get(x).document);

					num++;x++;

					}

				if(kw2freq > kw1freq){

					out.add(keywordsIndex.get(kw2).get(y).document);

					num++;y++;

					}

				if(x == kw1length) {

					secondSearch = true;

					}

				if(y == kw2length) {

					firstSearch = true;

					}

		}
		
		if(num < 10 && firstSearch && x < kw1length){

			while(num < 10 && x < kw1length){

				out.add(keywordsIndex.get(kw1).get(x).document);

				num++;

				x++;

				}

			}
		
		if(num < 10 && secondSearch && y < kw2length){

			while(num < 10 && y < kw2length){

				out.add(keywordsIndex.get(kw2).get(y).document);

				num++;

				y++;

				}

		}
	
		ArrayList<String> topsearches= new ArrayList<String>();

		int i = 0;

		while( i < out.size()){

			if(!topsearches.contains(out.get(i)) && topsearches.size() < 5) {
				
				topsearches.add(out.get(i));

				}

			i++;

			}

		return topsearches;
	
	}
	
}
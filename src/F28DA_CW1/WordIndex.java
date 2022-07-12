package F28DA_CW1;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/** Main class for the Word Index program */
public class WordIndex {

	static final File textFilesFolder = new File("TextFiles_Shakespeare");
	static final FileFilter commandFileFilter = (File file) -> file.getParent()==null;
	static final FilenameFilter txtFilenameFilter = (File dir, String filename) -> filename.endsWith(".txt");
	
	public static void main(String[] argv) {
		argv = new String[1];
		argv[0] = "commands.txt";
		if (argv.length != 1 ) {
			System.err.println("Usage: WordIndex commands.txt");
			System.exit(1);
		}
		try{
			File commandFile = new File(argv[0]);
			if (commandFile.getParent()!=null) {
				System.err.println("Use a command file in current directory");
				System.exit(1);
			}

			// creating a command reader from a file
			WordTxtReader commandReader = new WordTxtReader(commandFile);

			// initialise map
//			IWordMap wordPossMap = new ListWordMap();
			IWordMap wordPossMap = new HashWordMap();
			
			// reading the content of the command file
			while(commandReader.hasNextWord()) {
				// getting the next command
				String command = commandReader.nextWord().getWord();
				int numberOfEntries = 0;
				
				// Reference -> https://stackoverflow.com/questions/4927856/how-can-i-calculate-a-time-difference-in-java
				Instant start;
				Instant end;
				
				switch (command) {
				case "addall":
					// get initial time
					start = Instant.now();
					assert(textFilesFolder.isDirectory());
					File[] listOfFiles = textFilesFolder.listFiles(txtFilenameFilter);
					Arrays.sort(listOfFiles);
					numberOfEntries = 0;
					
					for (File textFile : listOfFiles) {
						WordTxtReader wordReader = new WordTxtReader(textFile);
						
						// get name of file without directory's name
						String name = textFile.getName();
						// variable for counting number of words per file
						int numberOfEntriesFile = 0;
						
						while (wordReader.hasNextWord()) {
							WordPosition wordPos = wordReader.nextWord();
							// get word and add it with it's position into the map
							wordPossMap.addPos(wordPos.getWord(), wordPos);
							
							// increment the entries
							numberOfEntries++;
							numberOfEntriesFile++;
						}
						
						// final time at which each file's words are added in the map
						end = Instant.now();
						System.out.printf("%d entries have been indexed from \"%s\" \n\n", numberOfEntriesFile, name);
						System.out.printf("Time taken for %s : %d milliseconds\n\n", name, Duration.between(start, end).toMillis());
					}
					
					System.out.printf("%d entries have been indexed from %d files \n\n", numberOfEntries, listOfFiles.length);
					
					// final time at which all the words from every file is added in the map
					end = Instant.now();
					System.out.printf("Time taken for addall : %d milliseconds\n\n", Duration.between(start, end).toMillis());
					break;

				case "add":
					// get initial time
					start = Instant.now();
					File textFile = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					WordTxtReader wordReader = new WordTxtReader(textFile);
					numberOfEntries = 0; 
					
					while (wordReader.hasNextWord()) {
						WordPosition word = wordReader.nextWord();
						// get word and add it with it's position into the map
						wordPossMap.addPos(word.getWord(), word);
						// increment the entries
						numberOfEntries++;
					}
					
					System.out.printf("%d entries have been indexed from file \"%s\" \n\n", numberOfEntries, textFile.getName());
					// final time at which all the words from the file is added in the map
					end = Instant.now();
					System.out.printf("Time taken for add : %d milliseconds\n\n", Duration.between(start, end).toMillis());
					break;

				case "search":
					// get initial time
					start = Instant.now();
					int nb = Integer.parseInt(commandReader.nextWord().getWord());
					String word = commandReader.nextWord().getWord();
					
					// 2d array to store search result entries 
					ArrayList<ArrayList<String>> searchResults = new ArrayList<>();
					// to store each search result as an entry
					ArrayList<String> entry = null;
					String lines = "(lines ";
					int numberOfOccurs = 0;
					String checkFile = null;
					
					try {
						// get the positions of the word being searched for
						Iterator<IPosition> poss = wordPossMap.positions(word);
						int i = 0;
						while(poss.hasNext()) {
							IPosition pos = poss.next();
							// current file
							String currentFile = pos.getFileName();
							
							// condition to check if it is a new file
							if (currentFile != checkFile) {
								// condition to check if the previous file is not null
								if (checkFile != null) {
									// search results of the previous file added to an entry
									entry.add(Integer.toString(numberOfOccurs));
									entry.add(checkFile);
									entry.add(lines.substring(0, lines.length()-2) + ") \n");
									// entry is added to the final results array 
									searchResults.add(entry);
								}
								
								// reinitialize back to 0 for new file
								numberOfOccurs = 0;
								checkFile = currentFile;
								// reinitialize entry array and lines for new file
								entry = new ArrayList<String>();
								lines = "(lines ";
							}
							
							// get the position value and convert it to a string
							String line = Integer.toString(pos.getLine());
							// add the line position into the lines string if it is not present
							if (lines.indexOf(line) == -1) {
								lines = lines + line + ", ";
							}
							i++;
							numberOfOccurs++;
						}
						
						// add the search results of the last resulted file
						entry.add(Integer.toString(numberOfOccurs));
						entry.add(checkFile);
						entry.add(lines.substring(0, lines.length()-2) + ") \n");
						searchResults.add(entry);
						entry = null;
						lines = null;
						
						// sort search results in descending order
						//References -> https://stackoverflow.com/questions/20480723/how-to-sort-2d-arrayliststring-by-only-the-first-element
						// and https://stackoverflow.com/questions/15452429/java-arrays-sort-2d-array
						Collections.sort(searchResults, new Comparator<ArrayList<String>>() {    
					        @Override
					        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
					        	return Integer.compare(Integer.valueOf(o2.get(0)), Integer.valueOf(o1.get(0))); 
					        }               
					});
						
						System.out.printf("The word \"%s\" occurs %d times in %d files: \n\n", word, i, searchResults.size());
						
						// clear out any results after the limit of results mentioned by the user in commands
						searchResults.subList(nb, searchResults.size()).clear();
						
						// display the results
						for (int j = 0; j < nb; j++) {
							System.out.printf("%s times in %s: \n %s \n", searchResults.get(j).get(0), searchResults.get(j).get(1), searchResults.get(j).get(2));
						}
					} catch (WordException e) {
						System.err.println("Word cannot be found");
					}
					// final time at which the search for the word in the map is completed
					end = Instant.now();
					System.out.printf("Time taken for search : %d milliseconds\n\n", Duration.between(start, end).toMillis());
					break;

				case "remove":
					// get initial time
					start = Instant.now();
					// get file to remove
					File textFileToRemove = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					wordReader = new WordTxtReader(textFileToRemove);
					numberOfEntries = 0;
					
					// loop until the end of the file
					while (wordReader.hasNextWord()) {
						// get the next word to remove
						WordPosition wordPos = wordReader.nextWord();
						try {
							// remove the word positions of the file being removed
							wordPossMap.removePos(wordPos.getWord(), wordPos);
						} catch (WordException e) {}
						numberOfEntries++;
					}
					System.out.printf("%d entries have been removed from file \"%s\" \n\n", numberOfEntries, textFileToRemove.getName());
					// final time at which the file is removed from the map
					end = Instant.now();
					System.out.printf("Time taken for remove : %d milliseconds\n\n", Duration.between(start, end).toMillis());
					break;

				case "overview":
					// get initial time
					start = Instant.now();
					// print overview
					int numberOfPositions = 0;
					// to store present files in the map
					ArrayList<String> files = new ArrayList<>();
					// word iterator
					Iterator<String> wordIterator = wordPossMap.words();
					
					// loop if the next word is present
					while (wordIterator.hasNext())
					{		
							// get next word
							String wrd = wordIterator.next();
							try {
								// positions iterator
								Iterator<IPosition> posIterator = wordPossMap.positions(wrd);
								// loop if the next position is present
								while (posIterator.hasNext()) {
									// get next position
									IPosition position = posIterator.next();
									// get the name of the file at that position
									String file = position.getFileName();
									// if file isnt present in the arraylist
									if (files.indexOf(file) == -1) {
										// then add it
										files.add(file);
									}
									// increment the number of positions
									numberOfPositions++;
								}
							} catch (WordException e) {}
					}
					
					// display the overview
					System.out.printf("Overview: \n number of words: %d\n number of positions: %d\n number of files: %d\n\n", 
							wordPossMap.numberOfEntries(), numberOfPositions, files.size());
					// final time at which the process for the overview of the map is complete
					end = Instant.now();
					System.out.printf("Time taken for overview : %d milliseconds\n\n", Duration.between(start, end).toMillis());
					break;

				default:
					break;
				}

			}

		}
		catch (IOException e){ // catch exceptions caused by file input/output errors
			System.err.println("Check your file name");
			System.exit(1);
		}  
	}
}
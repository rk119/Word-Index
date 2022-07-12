package F28DA_CW1;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class HashWordMapTest {
	
	/** My Test 1: To check if two exact same words are stored in one entry with their respective positions */
	
	@ Test
	public void myTest1() {
		float maxLF = 0.5f;
		HashWordMap h = new HashWordMap(maxLF);
		String word1 = "same";
		String word2 = "same";
		String file = "f1";
		WordPosition pos1 = new WordPosition(file, 1, word1);
		h.addPos(word1, pos1);
		WordPosition pos2 = new WordPosition(file, 2, word2);
		h.addPos(word1, pos2);
		try {
			Iterator<IPosition> it = h.positions(word2);
			assertEquals(it.next().getLine(), 1);
			assertEquals(it.next().getLine(), 2);
		} catch (WordException e) {
			fail();
		}
		assertEquals(h.numberOfEntries(), 1);
	}
	

	/** My Test 2: Add 100000 entries and delete a 100000 entries from the  Map */
	
	@Test
	public void myTest2() {
		try	{
			float maxLF = 0.5f;
			HashWordMap h = new HashWordMap(maxLF);
			String word;
			int line;
			String file;
			WordPosition pos;
			for (int k = 0; k < 100000; ++k) {
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				h.addPos(word, pos);
			}
			assertEquals(h.numberOfEntries(), 100000);
			for ( int k = 0; k < 100000; ++k )
			{
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				h.removePos(word, pos);
			}
			assertEquals(h.numberOfEntries(), 0);
			for (int k = 0; k < 100000; ++k) {
				word = "w" + k;
				try {
					h.positions(word);
					fail();
				} catch (WordException e) {
				}
			}
		} catch (WordException e) {
			fail();
		}
	}

	
	@Test
	public void signatureTest() {
        try {
            IWordMap map = new HashWordMap(0.5f);
            String word1 = "test1";
            String word2 = "test2";
            IPosition pos1 = new WordPosition("test.txt", 4, word1);
            IPosition pos2 = new WordPosition("test.txt", 5, word2);      
            map.addPos(word1, pos1);
            map.addPos(word2, pos2);
            map.words();
            map.positions(word1);
            map.numberOfEntries();
            map.removePos(word1, pos1);
            map.removeWord(word2);
        } catch (Exception e) {
            fail("Signature of solution does not conform");
        }
	}

}

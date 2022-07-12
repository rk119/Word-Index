package F28DA_CW1;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class ListWordMapTest {

	/** Test 1: add an entry and number of entries is 1 */
	@Test
	public void test1() {
		ListWordMap linkedListMap = new ListWordMap();
		String word = "abc";
		String file = "f1";
		int line = 2;
		WordPosition pos = new WordPosition(file, line, word);
		linkedListMap.addPos(word, pos);
		assertTrue(linkedListMap.numberOfEntries() == 1);
	}

	/** Test 2: add and find an entry */
	@Test
	public void test2() {
		ListWordMap linkedListMap = new ListWordMap();
		String word = "abc";
		String file = "f1";
		int line = 2;
		WordPosition pos = new WordPosition(file, line, word);
		linkedListMap.addPos(word, pos);
		Iterator<IPosition> possOut;
		try {
			possOut = linkedListMap.positions(word);
			IPosition posOut = possOut.next();
			assertTrue(posOut.getFileName().equals(file) && posOut.getLine() == line);
		} catch (WordException e) {
			fail();
		}
	}

	/** Test 3: look for an inexistent key */
	@Test
	public void test3() {
		ListWordMap linkedListMap = new ListWordMap();
		String word = "abc";
		String file = "f1";
		int line = 2;
		WordPosition pos = new WordPosition(file, line, word);
		linkedListMap.addPos(word, pos);
		try {
			linkedListMap.positions(word);
		} catch (WordException e) {
			assertTrue(true);
		}
	}

	/** Test 4: try to delete a nonexistent entry. Should throw an exception. */
	@Test
	public void test4() {
		ListWordMap linkedListMap = new ListWordMap();
		String word = "abc";
		String file = "f1";
		int line = 2;
		WordPosition pos = new WordPosition(file, line, word);
		linkedListMap.addPos(word, pos);
		try {
			linkedListMap.removeWord("other");
			fail();
		} catch (WordException e) {
			assertTrue(true);
		}
	}

	/** Test 5: delete an actual entry. Should not throw an exception. */
	@Test
	public void test5() {
		try {
			ListWordMap linkedListMap = new ListWordMap();
			String word1 = "abc";
			String word2 = "bcd";
			String file = "f1";
			int line = 2;
			WordPosition pos1 = new WordPosition(file, line, word1);
			WordPosition pos2 = new WordPosition(file, line, word2);
			linkedListMap.addPos(word1, pos1);
			linkedListMap.addPos(word2, pos2);
			linkedListMap.removePos(word2, pos2);
			assertTrue(linkedListMap.numberOfEntries() == 1);
			Iterator<IPosition> possOut = linkedListMap.positions(word1);
			assertTrue(possOut.hasNext());
			assertEquals(possOut.next(), pos1);
		} catch (WordException e) {
			fail();
		}
	}


	/** Test 6: insert 200 different values into the Map and check that all these values are in the Map */
	@Test
	public void test6() {
		ListWordMap linkedListMap = new ListWordMap();
		String word;
		int line;
		String file;
		WordPosition pos;
		for (int k = 0; k < 200; k++) {
			word = "w" + k;
			line = k + 1;
			file = "f" + k;
			pos = new WordPosition(file, line, word);
			linkedListMap.addPos(word, pos);
		}
		assertEquals(linkedListMap.numberOfEntries(), 200);
		for (int k = 0; k < 200; ++k) {
			word = "w" + k;
			try {
				Iterator<IPosition> poss = linkedListMap.positions(word);
				assertTrue(poss.hasNext());
			} catch (WordException e) {
				fail();
			}

		}
	}

	/** Test 7: Delete a lot of entries from the  Map */
	@Test
	public void test7() {
		try	{
			ListWordMap linkedListMap = new ListWordMap();
			String word;
			int line;
			String file;
			WordPosition pos;
			for (int k = 0; k < 200; ++k) {
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				linkedListMap.addPos(word, pos);
			}
			assertEquals(linkedListMap.numberOfEntries(), 200);
			for ( int k = 0; k < 200; ++k )
			{
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				linkedListMap.removePos(word, pos);
			}
			assertEquals(linkedListMap.numberOfEntries(), 0);
			for (int k = 0; k < 200; ++k) {
				word = "w" + k;
				try {
					linkedListMap.positions(word);
					fail();
				} catch (WordException e) {
				}
			}
		} catch (WordException e) {
			fail();
		}
	}

	/** Test 8: Get iterator over all keys */
	@Test
	public void test8() {
		ListWordMap linkedListMap = new ListWordMap();
		String word;
		int line;
		String file;
		WordPosition pos;
		try {
			for (int k = 0; k < 100; k++) {
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				linkedListMap.addPos(word, pos);
			}

			for (int k = 10; k < 30; k++) {
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				linkedListMap.removePos(word, pos);
			}
		} catch(WordException e) {
			fail();
		}
		Iterator<String> it = linkedListMap.words();
		int count = 0;

		while (it.hasNext()) {
			count++;
			it.next();
		}
		assertEquals(linkedListMap.numberOfEntries(),80);
		assertEquals(count,80);
	}

	@Test
	public void signatureTest() {
        try {
            IWordMap map = new ListWordMap();
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

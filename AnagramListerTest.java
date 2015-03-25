package main;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import junit.framework.Assert;

import org.junit.Test;

public class AnagramListerTest {

	@Test
	public void testIsWord() {
		
		Assert.assertEquals(true, Main.isWord("hello"));
		Assert.assertEquals(false, Main.isWord("flarg"));
	}
	
	@Test
	public void testWordFinder() {
		
		Queue<Character> lList = new LinkedList<Character>();
		lList.add('h');
		lList.add('e');
		lList.add('l');
		lList.add('l');
		lList.add('o');
		
		Assert.assertTrue(Main.wordFinder(lList, "").contains("he"));
	}

}

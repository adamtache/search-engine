/**
 * 
 */
package controller.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import controller.WikiPhilosophy;

/**
 * @author downey
 *
 */
public class WikiPhilosophyTest {


	/**
	 * Test method for {@link controller.WikiPhilosophy#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		// Because this lab is more open-ended than others, we can't provide unit
		// tests.  Instead, we just check that you've modified WikiPhilosophy.java
		// so it doesn't throw an exception.
		String[] args = {};
		try {
			WikiPhilosophy.main(args);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
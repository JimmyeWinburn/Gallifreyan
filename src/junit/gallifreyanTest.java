package junit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import db.GenericDB;
import db.GenericfDBFactory;

/**
 * this unit test suite is here included for completeness sake.  Unfortunately, the graphics nature of
 * the app makes it difficult to test.
 * 
 * @author Jimmye
 *
 */
public class gallifreyanTest {

	static GenericDB db;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		db = GenericfDBFactory.getDB();
		db.openSU();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.close();
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}

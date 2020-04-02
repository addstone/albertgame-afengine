package albertgame.afengine.util;

import albertgame.afengine.util.math.IDGenerator;
import org.testng.annotations.Test;

/**
 *
 * @author Albert Flex
 */
public class IDTest {

    @Test
    public void testID() {
        IDGenerator idcreator = new IDGenerator();
        long time1 = System.currentTimeMillis();
        for (int i = 0; i != 1000000; ++i) {
            idcreator.createId();
        }
        long time2 = System.currentTimeMillis();
        long delttime = time2 - time1;
        System.out.println("generate 1 million id cosume time:" + delttime + " millseconds");

        for (int i = 0; i != 10; ++i) {
            System.out.println(idcreator.createId());
        }
    }
}

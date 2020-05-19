package albertgame.afengine.core.util;

import albertgame.afengine.core.util.math.IDGenerator;
import org.testng.annotations.Test;

/**
 *
 * @author Albert Flex
 */
public class IDTest {

    @Test
    public void testID() {
        long time1 = System.currentTimeMillis();
        for (int i = 0; i != 1000000; ++i) {
            IDGenerator.createId();
        }
        long time2 = System.currentTimeMillis();
        long delttime = time2 - time1;
        System.out.println("generate 1 million id cosume time:" + delttime + " millseconds");

        for (int i = 0; i != 10; ++i) {
            System.out.println(IDGenerator.createId());
        }
    }
}

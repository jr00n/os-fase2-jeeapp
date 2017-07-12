package nl.jrOOn.javaee7angular;

import nl.jrOOn.javaee7angular.CPULoad;
import org.junit.experimental.categories.Category;

/**
 * Created by jr00n on 20/01/17.
 */
@Category(nl.jrOOn.javaee7angular.category.IntegrationTest.class)
public class LoadTest {
    //@org.junit.Test
    public void cpuLoadTest() throws Exception {
        CPULoad load = new CPULoad();
        load.run(new Byte("20"));

    }
}

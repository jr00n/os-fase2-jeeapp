package nl.jrOOn.javaee7angular;

/**
 * Created by jr00n on 23/01/17.
 */
public class CPULoad {
    public void run(Byte a) throws Exception{
        new Thread() {
            public void run() {
                for (;;)
                    ;
            }
        }.start();
        Thread.sleep(a * 1000);
        //System.exit(0);
    }
}

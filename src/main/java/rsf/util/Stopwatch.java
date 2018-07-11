package rsf.util;

/**
 * @author Thomas
 */
public final class Stopwatch {

    private long time = System.currentTimeMillis();

    public void reset() {
        time = System.currentTimeMillis();
    }

    public long getElapsed() {
        return System.currentTimeMillis() - time;
    }

}

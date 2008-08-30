package org.uncommons.util;

/**
 * Displays information on the console about the progress of tasks.
 * @author Daniel Dyer
 */
public class ConsoleProgressDisplay
{
    private long startTime;
    private int charCount;

    public void start(String caption)
    {
        clear();
        if (startTime != 0) // If something else wasn't finished.
        {
            System.out.println();
        }
        startTime = System.currentTimeMillis();
        System.out.print(caption);
    }


    public void update(int percentComplete)
    {
        if (startTime != 0) // If there is something to update.
        {
            if (percentComplete < 0 || percentComplete > 100)
            {
                throw new IllegalArgumentException("Invalid percentage: " + percentComplete);
            }
            clear();
            String s = " " + percentComplete + "%";
            charCount = s.length();
            System.out.print(s);
        }
    }


    public void finish(boolean success)
    {
        if (startTime != 0) // If there is something to finish.
        {
            long elapsed = System.currentTimeMillis() - startTime;
            clear();
            if (success)
            {
                System.out.println(" [OK] (" + formatTime(elapsed) + ")");
            }
            else
            {
                System.out.println(" [FAILED]");
            }
            startTime = 0;
        }
    }


    private void clear()
    {
        for (int i = 0; i < charCount; i++)
        {
            System.out.print('\u0008'); // Backspace.
        }
        charCount = 0;
    }


    private String formatTime(long ms)
    {
        long seconds = ms / 1000;
        if (seconds > 1)
        {
            return seconds + " seconds";
        }
        else
        {
            return ms + "ms";
        }
    }
}

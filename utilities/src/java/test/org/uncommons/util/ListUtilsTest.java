package org.uncommons.util;

import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Test;

/**
 * @author Daniel Dyer
 */
public class ListUtilsTest
{
    @Test
    public void testShiftLeft()
    {
        List<String> strings = Arrays.asList("AAA", "BBB", "CCC", "DDD", "EEE");

        ListUtils.shiftLeft(strings, 3, 2, 2);
        assert strings.get(0).equals("AAA") : "Wrong item in position 0: " + strings.get(0);
        assert strings.get(1).equals("DDD") : "Wrong item in position 1: " + strings.get(1);
        assert strings.get(2).equals("EEE") : "Wrong item in position 2: " + strings.get(2);
        assert strings.get(3).equals("BBB") : "Wrong item in position 3: " + strings.get(3);
        assert strings.get(4).equals("CCC") : "Wrong item in position 4: " + strings.get(4);
    }
}

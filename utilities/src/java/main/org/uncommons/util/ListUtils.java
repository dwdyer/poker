package org.uncommons.util;

import java.util.List;
import java.util.Collections;

/**
 * Utility methods for manipulating lists.
 * @author Daniel Dyer
 */
public class ListUtils
{
    private ListUtils()
    {
        // Prevents instantiation.
    }


    /**
     * @param list The list to manipulate.
     * @param start The index at which the sub list starts.
     * @param length The length of the sub list.
     * @param distance The number of positions to move the sub list.
     */
    public static void shiftLeft(List<?> list,
                                 int start,
                                 int length,
                                 int distance)
    {
        for (int i = 0; i < distance; i++)
        {
            for (int j = 0; j < length; j++)
            {
                int index = (start - i) + j;
                Collections.swap(list, index, index - 1);
            }
        }
    }
}

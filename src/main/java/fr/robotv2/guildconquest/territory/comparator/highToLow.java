package fr.robotv2.guildconquest.territory.comparator;

import fr.robotv2.guildconquest.object.Guild;

import java.util.Comparator;
import java.util.Map;

public class highToLow implements Comparator<Map.Entry<Guild, Integer>> {
    @Override
    public int compare(Map.Entry<Guild, Integer> o1, Map.Entry<Guild, Integer> o2) {
        return (o2.getValue() < o1.getValue() ? -1 : 1);
    }
}

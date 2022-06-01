package com.example.moviepicker;

import java.util.HashMap;
import java.util.Map;

public enum Genres {

    Action(28),
    Adventure(12),
    Animation(16),
    Comedy(35),
    Crime(80),
    Documentary(99),
    Drama(18),
    Family(10751),
    Fantasy(14),
    History(36),
    Horror(27),
    Music(10402),
    Mystery(9648),
    Romance(10749),
    ScienceFiction(878),
    TVMovie(10770),
    Thriller(53),
    War(10752),
    Western(37);

    private final int id;
    
    Genres(int id) {
        this.id = id;
    }

    public static String findByKey(int i) {
        return map.get(i);
    }

    private static final Map<Integer,String> map;

    static {
        map = new HashMap<>();
        for (Genres v : Genres.values()) {
            map.put(v.id, v.name());
        }
    }
}

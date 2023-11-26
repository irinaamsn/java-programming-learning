package com.gradle.boot.fintech.cache;

import com.gradle.boot.fintech.models.City;
import com.gradle.boot.fintech.models.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LRUCacheTest {
    private LruCache<Weather> cache;

    @BeforeEach
    void setUp() {
        cache = new LruCache<>();
        cache.setCapacity(3);
    }

    @Test
    void getWeather_whenCacheHasData() {
        String key1 = "city1";
        String key2 = "city2";
        Weather weather1 = new Weather(new City(key1), 1.0);
        Weather weather2 = new Weather(new City(key2), 2.0);
        cache.put(key1, weather1);
        cache.put(key2, weather2);

        assertEquals(weather1, cache.get(key1));
        assertEquals(weather2, cache.get(key2));
    }

    @Test
    void putWeather_whenCapacityFull() {
        String lastKey = "last";
        cache.put(lastKey, new Weather(new City(lastKey), 1.0));
        for (int i = 1; i < cache.getCapacity(); i++) {
            String key = "city" + i;
            Weather weather = new Weather(new City(key), 1.0);
            cache.put(key, weather);
        }
        assertTrue(cache.getCapacity() == cache.getSize());

        String key = "newKey";
        Weather weather = new Weather(new City(key), 1.0);
        cache.put(key, weather);

        assertNull(cache.get(lastKey));
        assertTrue(cache.getCapacity() == cache.getSize());
        assertNotNull(cache.get(key));
    }

    @Test
    void thenGetWeather_moveToFirst() {
        for (int i = 1; i < cache.getCapacity(); i++) {
            String key = "city" + i;
            cache.put(key, new Weather(new City(key), 1.0));
        }
        String moveKey = "city1";

        assertNotNull(cache.get(moveKey));
        assertTrue(cache.getQueue().getFirst().key.equals(moveKey));
    }

    @Test
    void thenUpdateWeather_dataRemove() {
        String key = "city";
        Weather weather =  new Weather(new City(key), 1.0);
        cache.put(key,weather);
        assertNotNull(cache.get(key));
        assertTrue(cache.get(key).equals(weather));

        Weather updatedWeather =  new Weather(new City(key), 2.0);
        cache.put(key, updatedWeather);
        assertNotNull(cache.get(key));
        assertTrue(cache.get(key).equals(updatedWeather));
    }
}
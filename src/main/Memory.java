package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Memory {

    private static Memory instance;
    private Map<Integer, Integer> memoryData;

    private Memory() {
        // Initialize the memory hashmap with random values
        memoryData = new HashMap<>();
        Random random = new Random();

        // Initialize memory with random values between 0 and 101
        for (int i = 0; i < 100; i++) {
            memoryData.put(i, random.nextInt(102));
        }
    }

    public static Memory getInstance() {
        if (instance == null) {
            synchronized (Memory.class) {
                if (instance == null) {
                    instance = new Memory();
                }
            }
        }
        return instance;
    }

    public int getData(int index) {
        return memoryData.get(index);
    }

    public int getSize() {
        return memoryData.size();
    }

    // Additional methods can be added as needed

    public void setData(int index, int value) {
        memoryData.put(index, value);
    }
}

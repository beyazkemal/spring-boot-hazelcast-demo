package com.kemalbeyaz.hazelcast.controller;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Kemal BEYAZ
 * @created 25/05/2020
 */
@RestController
@RequestMapping("/")
public class MainController {

    private static final String MAP_NAME = "demo-map";

    private HazelcastInstance hazelcastInstance;

    @Autowired
    public MainController(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @GetMapping
    public String hello() {
        return "Hello Hazelcast Demo!";
    }

    @GetMapping("write")
    public String writeToHazelcast(@RequestParam String key, @RequestParam String value) {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap(MAP_NAME);
        hazelcastMap.put(key, value);
        return "Saved!";
    }

    @GetMapping("read")
    public String readFromHazelcast(@RequestParam String key) {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap(MAP_NAME);
        return hazelcastMap.get(key);
    }

    @GetMapping("read/all")
    public Map<String, String> readAllData() {
        return hazelcastInstance.getMap(MAP_NAME);
    }

    /* Test concurrent hash map. */
    @GetMapping("write/concurrent")
    public String writeConcurrentMap(@RequestParam String key, @RequestParam String value) {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap(MAP_NAME);

        return hazelcastMap.computeIfAbsent(key, k -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        });
    }

}

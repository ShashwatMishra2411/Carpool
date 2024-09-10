package com.vit.carpool.controllers;

import com.vit.carpool.entities.Pool;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pools")
public class PoolController {
    private final Map<Long, Pool> poolEntries = new HashMap<>();

    @GetMapping
    public ArrayList<Pool> getAll() {
        return new ArrayList<>(poolEntries.values());
    }

    @PostMapping
    public boolean create(@RequestBody Pool mypool) {
        poolEntries.put(mypool.getID(), mypool);
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody Pool updatedPool) {
        if (poolEntries.containsKey(id)) {
            poolEntries.put(id, updatedPool);
            return true;
        } else {
            return false;
        }
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        if (poolEntries.containsKey(id)) {
            poolEntries.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/{id}")
    public Pool findById(@PathVariable long id) {
        return poolEntries.get(id);
    }
}

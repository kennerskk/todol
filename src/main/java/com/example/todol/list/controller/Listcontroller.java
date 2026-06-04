package com.example.todol.list.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.todol.list.model.Listmodel;
import com.example.todol.list.service.Listservice;
import com.example.todol.util.JwtUtil;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/lists")
public class Listcontroller {
    @Autowired
    private Listservice listService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<Listmodel> getMyLists() {
        String userId = jwtUtil.getCurrentUserIdFromToken();
        return listService.getListsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listmodel> getListById(@PathVariable String id) {
        String userId = jwtUtil.getCurrentUserIdFromToken();
        return listService.getListByIdForUser(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Listmodel createList(@RequestBody Listmodel list) {
        String userId = jwtUtil.getCurrentUserIdFromToken();
        list.setUserId(userId);
        return listService.createList(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Listmodel> updateList(@PathVariable String id, @RequestBody Listmodel updatedList) {
        String userId = jwtUtil.getCurrentUserIdFromToken();
        return listService.updateListForUser(id, updatedList, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable String id) {
        String userId = jwtUtil.getCurrentUserIdFromToken();
        boolean deleted = listService.deleteListForUser(id, userId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

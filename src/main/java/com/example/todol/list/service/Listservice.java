package com.example.todol.list.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.todol.list.model.Listmodel;
import com.example.todol.list.repository.Listrepository;
@Service
public class Listservice {
    @Autowired
    private Listrepository listRepository;

    public List<Listmodel> getAllLists() {
        return listRepository.findAll();
    }

    public List<Listmodel> getListsByUserId(String userId) {
        return listRepository.findByUserId(userId);
    }

    public Optional<Listmodel> getListById(String id) {
        return listRepository.findById(id);
    }

    public Optional<Listmodel> getListByIdForUser(String id, String userId) {
        return listRepository.findById(id)
                .filter(list -> list.getUserId().equals(userId));
    }

    public Listmodel createList(Listmodel list) {
        return listRepository.save(list);
    }

    public Optional<Listmodel> updateList(String id, Listmodel updatedList) {
        return listRepository.findById(id).map(list -> {
            list.setTitle(updatedList.getTitle());
            list.setDescription(updatedList.getDescription());
            list.setPositionorder(updatedList.getPositionorder());
            return listRepository.save(list);
        });
    }

    public Optional<Listmodel> updateListForUser(String id, Listmodel updatedList, String userId) {
        return listRepository.findById(id)
                .filter(list -> list.getUserId().equals(userId))
                .map(list -> {
                    list.setTitle(updatedList.getTitle());
                    list.setDescription(updatedList.getDescription());
                    list.setPositionorder(updatedList.getPositionorder());
                    return listRepository.save(list);
                });
    }

    public void deleteList(String id) {
        listRepository.deleteById(id);
    }

    public boolean deleteListForUser(String id, String userId) {
        return listRepository.findById(id)
                .filter(list -> list.getUserId().equals(userId))
                .map(list -> {
                    listRepository.delete(list);
                    return true;
                }).orElse(false);
    }
}
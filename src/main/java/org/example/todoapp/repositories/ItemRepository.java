package org.example.todoapp.repositories;

import java.util.*;

import org.example.todoapp.models.*;
import org.springframework.data.jpa.repository.*;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> getItemsByListId(Long listId);
}

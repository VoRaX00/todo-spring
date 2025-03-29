package org.example.todoapp.repositories;

import java.util.*;

import org.example.todoapp.models.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findByUserId(Long userId);
    Optional<TodoList> findTodoListByIdAndUserId(Long id, Long userId);
}

package org.example.todoapp.repositories;

import org.example.todoapp.models.*;
import org.springframework.data.jpa.repository.*;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {

}

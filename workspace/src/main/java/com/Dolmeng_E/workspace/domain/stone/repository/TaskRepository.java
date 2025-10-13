package com.Dolmeng_E.workspace.domain.stone.repository;

import com.Dolmeng_E.workspace.domain.stone.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}

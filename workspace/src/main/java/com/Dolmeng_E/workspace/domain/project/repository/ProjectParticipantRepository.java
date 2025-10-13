package com.Dolmeng_E.workspace.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipantRepository,String> {
}

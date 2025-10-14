package com.Dolmeng_E.workspace.domain.workspace.repository;

import com.Dolmeng_E.workspace.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, String> {
    Workspace findByUserIdAndWorkspaceName(UUID userId, String workspaceName);
}

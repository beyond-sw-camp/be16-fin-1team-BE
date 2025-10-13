package com.Dolmeng_E.workspace.domain.stone.service;

import com.Dolmeng_E.workspace.domain.stone.repository.ChildStoneListRepository;
import com.Dolmeng_E.workspace.domain.stone.repository.StoneRepository;
import com.Dolmeng_E.workspace.domain.stone.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoneService {
    private final ChildStoneListRepository childStoneListRepository;
    private final TaskRepository taskRepository;
    private final StoneRepository stoneRepository;
}

package com.Dolmeng_E.user.domain.sharedCalendar.repository;

import com.Dolmeng_E.user.domain.sharedCalendar.entity.SharedCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedCalendarRepository extends JpaRepository<SharedCalendar, String> {
}

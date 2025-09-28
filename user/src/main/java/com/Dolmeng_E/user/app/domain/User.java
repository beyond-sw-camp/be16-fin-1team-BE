package com.Dolmeng_E.user.app.domain;

import com.Dolmeng_E.user.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length=50)
    private String name;
    @Column(unique = true, nullable = false, length=100)
    private String email;
    @Column(nullable = false, length=100)
    private String password;
    @Column(length=30)
    private String phoneNumber;
    private String profileImageUrl;
    @Column(nullable = false)
    @Builder.Default
    private boolean isDelete = false;
}

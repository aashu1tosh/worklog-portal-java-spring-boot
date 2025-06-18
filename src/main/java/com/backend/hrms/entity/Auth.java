package com.backend.hrms.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

Entity@Table(
    name = "auth",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
@Getter @Setter @NoArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email @Column(nullable = false)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private String passwordHash;   // store ONLY the hash

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
}

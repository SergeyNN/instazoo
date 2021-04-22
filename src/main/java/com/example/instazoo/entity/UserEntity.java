package com.example.instazoo.entity;

import com.example.instazoo.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "user_name", unique = true, updatable = true)
    private String userName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "bio")
    private String bio;

    @Column(name = "password", length = 3000)
    private String password;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> role = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Post> post = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(unique = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}

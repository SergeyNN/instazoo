package com.example.instazoo.repository;

import com.example.instazoo.entity.Post;
import com.example.instazoo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOOrderByCreatedDateDesc(UserEntity userEntity);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUser(Long id, UserEntity userEntity);
}

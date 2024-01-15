package com.restapiblog.restapiblog.repository;

import com.restapiblog.restapiblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository <Post, Long> {
    List<Post> findPostByTitleIgnoreCaseContains(String title);
}

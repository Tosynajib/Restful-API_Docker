package com.restapiblog.restapiblog.controller;

import com.restapiblog.restapiblog.exception.PostNotFoundException;
import com.restapiblog.restapiblog.model.Post;
import com.restapiblog.restapiblog.serviceImpl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class PostController {

    private PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping("/save-post")
    public ResponseEntity<Post> savePost(@RequestBody Post post){
        return postService.savePost(post);
    }

    @GetMapping("/all-post")
    public ResponseEntity<List<Post>> getAllPost(){
        return postService.getAllPost();
    }

    @PutMapping("/edit-post/{id}")
    // @PathVariable binds the user Id to the method
    public ResponseEntity<Post> editPostById(@PathVariable Long id, @RequestBody Post newPost) {
        return postService.editPostById(id, newPost);
    }

    @GetMapping("/get-post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        return postService.getPointById(id);
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id){
        return postService.deletePostById(id);
    }

    @GetMapping("/get-post-title/{title}")
    public ResponseEntity<List<Post>> getPostByPostTitle(@PathVariable String title){
        return postService.getPostByPostTitle(title);
    }

}

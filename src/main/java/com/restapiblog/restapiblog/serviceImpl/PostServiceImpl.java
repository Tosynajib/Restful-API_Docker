package com.restapiblog.restapiblog.serviceImpl;

import com.restapiblog.restapiblog.exception.PostNotFoundException;
import com.restapiblog.restapiblog.model.Post;
import com.restapiblog.restapiblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public ResponseEntity<Post> savePost(Post newPost) {
        Post post1 = new Post(newPost.getTitle(), newPost.getContent());
        postRepository.save(post1);
        return new ResponseEntity<>(post1, HttpStatus.CREATED);
    }

    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> postList = postRepository.findAll();
        return new ResponseEntity<>(postList, HttpStatus.FOUND);
    }

    public ResponseEntity<Post> editPostById(Long id, Post newPost) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isPresent()){
            Post post1 = post.get();
            post1.setTitle(newPost.getTitle());
            post1.setContent(newPost.getContent());
            postRepository.save(post1);
            return new ResponseEntity<>(post1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            throw new PostNotFoundException("Post not found");
        }

    }

    public ResponseEntity<Post> getPointById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()){
            Post post1 = post.get();
            return new ResponseEntity<>(post1, HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Void> deletePostById(Long id) {
       Optional<Post> post = postRepository.findById(id);

        if (post.isPresent()){
            Post post1 = post.get();
            postRepository.deleteById(post1.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Post>> getPostByPostTitle(String title) {
        List<Post> postList = postRepository.findPostByTitleIgnoreCaseContains(title);

        if (postList.isEmpty()){
            return new ResponseEntity<>(postList, HttpStatus.NO_CONTENT);

        }else {
            return new ResponseEntity<>(postList, HttpStatus.FOUND);
        }
    }


}

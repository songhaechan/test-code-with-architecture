package com.example.demo.post.service.port;

import com.example.demo.post.infrastructure.PostEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository {

    Optional<PostEntity> findById(long id);

    PostEntity save(PostEntity postEntity);
}

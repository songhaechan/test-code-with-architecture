package com.example.demo.post.infrastructure;

import com.example.demo.post.service.port.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final PostJpaRepository jpaRepository;

    @Override
    public Optional<PostEntity> findById(final long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public PostEntity save(final PostEntity postEntity) {
        return jpaRepository.save(postEntity);
    }
}

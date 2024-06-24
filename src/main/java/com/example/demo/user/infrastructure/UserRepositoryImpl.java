package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public Optional<UserEntity> findByIdAndStatus(final long id, final UserStatus userStatus) {
        return jpaRepository.findByIdAndStatus(id,userStatus);
    }

    @Override
    public Optional<UserEntity> findByEmailAndStatus(final String email,
        final UserStatus userStatus) {
        return jpaRepository.findByEmailAndStatus(email,userStatus);
    }

    @Override
    public UserEntity save(final UserEntity userEntity) {
        return jpaRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findById(final long id) {
        return jpaRepository.findById(id);
    }
}

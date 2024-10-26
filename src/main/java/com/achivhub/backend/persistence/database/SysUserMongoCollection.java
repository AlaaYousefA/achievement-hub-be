package com.achivhub.backend.persistence.database;

import com.achivhub.backend.persistence.documents.SysUserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserMongoCollection extends MongoRepository<SysUserDocument, String> {
    Optional<SysUserDocument> findByUsername(String username);
}

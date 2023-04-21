package com.verify.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query(value = "SELECT * FROM images ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Image findRandomImage();
}

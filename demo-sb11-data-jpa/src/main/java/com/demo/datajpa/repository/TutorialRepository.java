package com.demo.datajpa.repository;

import com.demo.datajpa.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    //已有：save(), findOne(), findById(), findAll(), count(), delete(), deleteById()
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
}

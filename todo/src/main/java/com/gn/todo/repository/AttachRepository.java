package com.gn.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.todo.Entity.Attach;

public interface AttachRepository extends JpaRepository<Attach, Long> {

}

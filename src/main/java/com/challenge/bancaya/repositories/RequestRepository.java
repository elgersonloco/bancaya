package com.challenge.bancaya.repositories;

import com.challenge.bancaya.entities.RequestTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<RequestTracking, Long> {



}

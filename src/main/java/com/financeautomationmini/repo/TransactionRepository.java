package com.financeautomationmini.repo;

import com.financeautomationmini.entity.TransactionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionData, Long> {

}

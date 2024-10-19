package com.nemetschek.nemetschek_api.repo;

import com.nemetschek.nemetschek_api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


}

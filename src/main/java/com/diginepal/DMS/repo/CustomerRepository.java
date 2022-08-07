package com.diginepal.DMS.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diginepal.DMS.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	@Query("from Customer as c where c.user.id = :userId")
	public Page<Customer> findCustomersByUser(@Param("userId") int userId, Pageable pePageable);
}

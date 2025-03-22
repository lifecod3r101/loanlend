package com.credable.loanlend.DataRepositories;

import com.credable.loanlend.Models.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDataRepository extends CrudRepository<CustomerInfo, String> {
    CustomerInfo findByCustomerNumber(String customerNumber);
}

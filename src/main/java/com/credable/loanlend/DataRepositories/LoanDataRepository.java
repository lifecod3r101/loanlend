package com.credable.loanlend.DataRepositories;

import com.credable.loanlend.Models.LoanInfo;
import org.springframework.data.repository.CrudRepository;

public interface LoanDataRepository extends CrudRepository<LoanInfo, String> {
}

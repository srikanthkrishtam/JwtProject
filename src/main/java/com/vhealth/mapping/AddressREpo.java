package com.vhealth.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AddressREpo extends JpaRepository<Address, Long>{

}

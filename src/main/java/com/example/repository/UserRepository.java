package com.example.repository;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by PatrykGrudnik on 06.12.2016.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByLastName(String lastName);
    User findByMail(String email);
}


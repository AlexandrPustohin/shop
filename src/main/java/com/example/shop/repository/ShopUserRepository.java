package com.example.shop.repository;

import com.example.shop.model.ShopUser;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopUserRepository extends JpaRepository<ShopUser, Long> {
    List<ShopUser> findAll();
    Optional<ShopUser> findByEmail(String email);
    Optional<ShopUser> findByUserNameOrEmail(String userName, String email);
    Optional<ShopUser> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
}

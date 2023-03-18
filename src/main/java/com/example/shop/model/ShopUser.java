package com.example.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor

@Table(name = "shop_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userName"}),
        @UniqueConstraint(columnNames = {"email"})
       })
public class ShopUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name = "user_name")
    @NotBlank(message = "Имя не должно быть пустым!")
    String userName;

    @Column(name = "email")
    @NotBlank(message = "E-mail не должен быть пустым!")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Пароль не должен быть пустым!")
    private String password;

    @Column(name = "balance")
    private long balance;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;



}

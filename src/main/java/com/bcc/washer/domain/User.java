package com.bcc.washer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String first_name;

    @Column(nullable=false)
    private String last_name;

    @Column(nullable=false,unique=true)
    private String email;
    @Column(nullable=false,unique=true)
    private String phone;
    private String nr_matricol;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }



}


//User entity -->
//username: String (unic) - Identificator al utilizatorului
//password: String - Parola stocata sub forma de hash (folosind BCrypt).
//email: String (unic) - Adresa de email pentru notificari ?i recuperare parola.
//phone_nr: String - Numar de telefon al studentului(pentru Reminder appointment)
//nr_matricol: string - Numar matricol al studentului.
//first_name: String prenume.
//last_name: String - nume.
//role: ENUM(ADMIN,USER,EMPLOYEE) - rol pentru a defini accesul la resurse
//last_login: Date - Data si ora ultimei autentificari(pentru metrics ).



//university: String - Facultatea la care studentul este inscris.
//campus_name: String - Numele campusului universitar.
//campus_room_nr: String - Camera din campus.
//active: Boolean - Indica daca contul este activ (true) sau dezactivat (false).
//credits: Integer - Numarul de credite disponibile (opional, pentru plata serviciilor).
//registration_date: Date - data primei inregistrari

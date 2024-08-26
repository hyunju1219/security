package com.study.SpringSecurity.domain.entity;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity //table
@Data
@Builder
public class User {
    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AI
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

//    @OneToMany(mappedBy = "user") //UserRole 엔티티의 변수명
//    private Set<UserRole> userRoles = new HashSet<>(); //초기화

    //fetch : 엔티티를 조인했을 때 연관된 데이터를 언제 가ㅋ져올지 결정(EAGER - 당장, LAZY - 나중에 사용할 때)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //여러명의 유저가 여러개의 권한을 가질 수 있음
    @JoinTable(
            name = "user_roles", //조인해서 새로 만들 테이블명
            joinColumns = @JoinColumn(name = "user_id"), //조인 키값
            inverseJoinColumns = @JoinColumn(name = "role_id") ///외래키
    )
    private Set<Role> roles; //중복 제거를 위함, 1-a, a-1

    public PrincipalUser toPrincipalUser() {
        return PrincipalUser.builder()
                .userId(id)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }

}

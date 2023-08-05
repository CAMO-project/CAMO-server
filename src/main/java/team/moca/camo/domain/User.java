package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.moca.camo.controller.dto.request.SignUpRequest;
import team.moca.camo.domain.value.Domain;
import team.moca.camo.domain.value.UserType;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Table(name = "User")
@Entity
public class User extends BaseEntity implements UserDetails {

    @Column(name = "email", unique = true, nullable = false, updatable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "phone", nullable = false, unique = true, length = 15)
    private String phone;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "kakao_id", length = 50)
    private String kakaoId;

    @Column(name = "withdrawn")
    private boolean withdrawn;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_type", length = 15)
    private UserType userType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Cafe> cafes;

    protected User() {
        super(Domain.USER);
        withdrawn = false;
        userType = UserType.CUSTOMER;
    }

    @Builder
    protected User(String email, String password, String phone, String nickname, String kakaoId) {
        this();
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
    }

    public static User signUp(SignUpRequest signUpRequest, String encodedPassword) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .phone(signUpRequest.getPhone())
                .nickname(signUpRequest.getNickname())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

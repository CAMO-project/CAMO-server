package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
public class User extends BaseEntity implements UserDetails {

    @Column(name = "email", unique = true, nullable = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "withdrawn")
    private boolean withdrawn;

    protected User() {
        super(Domain.USER);
    }

    @Builder
    protected User(String email, String password, String phone, String nickname, String kakaoId) {
        super(Domain.USER);
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
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

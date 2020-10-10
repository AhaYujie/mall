package online.ahayujie.mall.portal.mms.bean.dto;

import lombok.Data;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author aha
 * @since 2020/10/9
 */
@Data
public class MemberUserDetailsDTO implements UserDetails {
    private Member member;

    public MemberUserDetailsDTO(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
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
        return Member.Status.ENABLED.value().equals(member.getStatus());
    }
}

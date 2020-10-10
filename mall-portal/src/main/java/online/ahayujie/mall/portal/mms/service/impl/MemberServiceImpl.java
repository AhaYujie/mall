package online.ahayujie.mall.portal.mms.service.impl;

import io.jsonwebtoken.Claims;
import online.ahayujie.mall.portal.mms.bean.dto.MemberLoginDTO;
import online.ahayujie.mall.portal.mms.bean.dto.MemberLoginParam;
import online.ahayujie.mall.portal.mms.bean.dto.MemberRegisterParam;
import online.ahayujie.mall.portal.mms.bean.dto.MemberUserDetailsDTO;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.exception.DuplicatePhoneException;
import online.ahayujie.mall.portal.mms.exception.DuplicateUsernameException;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-09
 */
@Service
public class MemberServiceImpl implements MemberService {
    private TokenProvider tokenProvider;

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(MemberRegisterParam param) throws DuplicateUsernameException, DuplicatePhoneException {
        if (memberMapper.selectByUsername(param.getUsername()) != null) {
            throw new DuplicateUsernameException("用户名已存在");
        }
        if (memberMapper.selectByPhone(param.getPhone()) != null) {
            throw new DuplicatePhoneException("手机号已存在");
        }
        Member member = new Member();
        BeanUtils.copyProperties(param, member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setCreateTime(new Date());
        memberMapper.insert(member);
    }

    @Override
    public MemberLoginDTO login(MemberLoginParam param) throws UsernameNotFoundException, BadCredentialsException, DisabledException {
        MemberUserDetailsDTO userDetails = (MemberUserDetailsDTO) loadUserByUsername(param.getUsername());
        if (!passwordEncoder.matches(param.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        if (!userDetails.isEnabled()) {
            throw new DisabledException("用户被禁用");
        }
        Map<String, Object> claims = getClaims(userDetails);
        String accessToken = tokenProvider.createAccessToken(userDetails.getUsername(), claims);
        String refreshToken = tokenProvider.createRefreshToken(userDetails.getUsername(), claims);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MemberLoginDTO memberLoginDTO = new MemberLoginDTO();
        memberLoginDTO.setAccessToken(accessToken);
        memberLoginDTO.setRefreshToken(refreshToken);
        memberLoginDTO.setExpireIn(tokenProvider.getAccessTokenValidityInSeconds());
        return memberLoginDTO;
    }

    @Override
    public MemberLoginDTO refreshAccessToken(String refreshToken) throws IllegalArgumentException {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("refreshToken不合法");
        }
        Claims refreshTokenClaims = tokenProvider.getClaimsFromRefreshToken(refreshToken);
        String username = refreshTokenClaims.getSubject();
        MemberUserDetailsDTO userDetails = (MemberUserDetailsDTO) loadUserByUsername(username);
        Map<String, Object> claims = getClaims(userDetails);
        String accessToken = tokenProvider.createAccessToken(username, claims);
        MemberLoginDTO memberLoginDTO = new MemberLoginDTO();
        memberLoginDTO.setAccessToken(accessToken);
        memberLoginDTO.setRefreshToken(refreshToken);
        memberLoginDTO.setExpireIn(tokenProvider.getAccessTokenValidityInSeconds());
        return memberLoginDTO;
    }

    private Map<String, Object> getClaims(MemberUserDetailsDTO memberUserDetailsDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", memberUserDetailsDTO.getMember().getId());
        claims.put("username", memberUserDetailsDTO.getUsername());
        return claims;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(Claims claims) {
        return Collections.emptyList();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Member member = memberMapper.selectByUsername(s);
        if (member == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return new MemberUserDetailsDTO(member);
    }

    @Autowired
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
}

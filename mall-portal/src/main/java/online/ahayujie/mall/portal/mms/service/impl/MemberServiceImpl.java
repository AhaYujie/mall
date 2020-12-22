package online.ahayujie.mall.portal.mms.service.impl;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.portal.mms.bean.dto.*;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.exception.DuplicatePhoneException;
import online.ahayujie.mall.portal.mms.exception.DuplicateUsernameException;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-09
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    private TokenProvider tokenProvider;

    @Value("${jwt.header}")
    private String jwtHeader;
    @Value("${jwt.header-prefix}")
    private String jwtHeaderPrefix;

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

    @Override
    public Member getMemberFromToken() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String bearerToken = request.getHeader(jwtHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtHeaderPrefix)) {
            return getMemberFromToken(bearerToken.substring(jwtHeaderPrefix.length()));
        }
        return null;
    }

    @Override
    public Member getMemberFromToken(String token) {
        Claims claims;
        try {
            claims = tokenProvider.getClaimsFromAccessToken(token);
        } catch (Exception e) {
            try {
                claims = tokenProvider.getClaimsFromRefreshToken(token);
            } catch (Exception e1) {
                return null;
            }
        }
        Member member = new Member();
        member.setId(claims.get("id", Long.class));
        member.setUsername(claims.get("username", String.class));
        return member;
    }

    @Override
    public MemberDTO getInfo() {
        Member member = memberMapper.selectById(getMemberFromToken().getId());
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(member, memberDTO);
        return memberDTO;
    }

    @Override
    public void updateInfo(UpdateMemberParam param) {
        if (param.getGender() != null) {
            if (!Arrays.stream(Member.Gender.values())
                    .map(Member.Gender::value)
                    .collect(Collectors.toList())
                    .contains(param.getGender())) {
                throw new IllegalArgumentException("性别不合法");
            }
        }
        Member member = getMemberFromToken();
        Member update = new Member();
        update.setId(member.getId());
        BeanUtils.copyProperties(param, update);
        update.setUpdateTime(new Date());
        memberMapper.updateById(update);
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        Member member = new Member();
        member.setId(id);
        member.setIntegration(integration);
        memberMapper.updateById(member);
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

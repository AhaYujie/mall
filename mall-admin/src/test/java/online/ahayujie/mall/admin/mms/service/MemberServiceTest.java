package online.ahayujie.mall.admin.mms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.mapper.MemberMapper;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;

    @Test
    void list() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setUsername(getRandomString(40));
            member.setPhone(getRandomNum(11));
            members.add(member);
        }
        members.forEach(memberMapper::insert);
        CommonPage<Member> result = memberService.list(1L, 5L);
        assertEquals(5, result.getData().size());
        CommonPage<Member> result1 = memberService.list(2L, 5L);
        assertEquals(5, result1.getData().size());
    }

    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private static String getRandomNum(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(str.length()));
        }
        return stringBuilder.toString();
    }

    @Test
    void queryByUsername() {
        String username = getRandomString(40);
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setUsername(username + ":" + i);
            member.setPhone(getRandomNum(11));
            members.add(member);
        }
        members.forEach(memberMapper::insert);
        CommonPage<Member> result = memberService.queryByUsername(1L, 5L, username);
        assertEquals(5, result.getData().size());
        CommonPage<Member> result1 = memberService.queryByUsername(2L, 5L, username);
        assertEquals(5, result1.getData().size());
    }

    @Test
    void queryByPhone() {
        String phone = "1234";
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setUsername(getRandomString(40));
            member.setPhone(phone + getRandomNum(11 - phone.length()));
            members.add(member);
        }
        members.forEach(memberMapper::insert);
        CommonPage<Member> result = memberService.queryByPhone(1L, 5L, phone);
        assertEquals(5, result.getData().size());
        CommonPage<Member> result1 = memberService.queryByPhone(2L, 5L, phone);
        assertEquals(5, result1.getData().size());
    }
}
package com.example.ordering_lecture.common;

import com.example.ordering_lecture.member.domain.Gender;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.domain.Role;
import com.example.ordering_lecture.member.repository.MemberRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadAdminAccount();
    }
    private void loadAdminAccount() {
        String adminEmail = "admin@test.com";
        if (!memberRepository.findByEmail(adminEmail).isPresent()) {
            Member admin = Member.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("1234"))
                    .name("Admin")
                    .role(Role.ADMIN)
                    .age((byte) 29)
                    .gender(Gender.MALE)
                    .phoneNumber("000-0000-0000")
                    .build();
            memberRepository.save(admin);
        }
    }
}
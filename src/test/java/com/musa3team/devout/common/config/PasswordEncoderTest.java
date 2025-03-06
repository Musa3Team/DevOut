package com.musa3team.devout.common.config;

import com.musa3team.devout.common.config.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드_정상작동(){
        //given
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        //when
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        //then
        assertTrue(matches);
    }


}

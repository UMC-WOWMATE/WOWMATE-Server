package com.wowmate.server.user.service.impl;

import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService{

    //private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("[loadUserByUsername] loadUserByUsername 수행: username = {}", userEmail);

        User user = null;

        try {
            user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new BaseException(ResponseStatus.NOT_FOUND_USER));
        } catch (BaseException e) {

            throw new RuntimeException(e);
        }

        return user;
    }

}


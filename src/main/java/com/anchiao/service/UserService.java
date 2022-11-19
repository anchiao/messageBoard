package com.anchiao.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchiao.entity.User;
import com.anchiao.entity.security.UserRole;
import com.anchiao.repository.RoleRepository;
import com.anchiao.repository.UserRepository;
import com.anchiao.repository.UserRoleRepository;

@Service
public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurityService userSecurityService;

    @Transactional
    public Map<String, Object> createUser(User user, Set<UserRole> userRoles) throws Exception {
        User localUser = userRepository.findByUsername(user.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        
        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
            data.put("HttpStatus", HttpStatus.BAD_REQUEST);
           
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            localUser = userRepository.save(user);

            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
                ur.setUser(localUser);
                userRoleRepository.save(ur);
            }

            user.getUserRoles().addAll(userRoles);

            data.put("HttpStatus", HttpStatus.OK);
            data.put("username", localUser.getUsername());
        }

		return data;
    }

    public HttpStatus validateToken(String token) throws IOException, JSONException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token);
        CloseableHttpResponse response = client.execute(httpGet);

        System.out.println(response);
        System.out.println(response.getStatusLine());
        String result = response.getStatusLine().toString();

        if (result.equalsIgnoreCase("HTTP/1.1 200 OK")) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public void setUserSession (String username) {
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

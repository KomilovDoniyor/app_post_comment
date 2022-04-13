package test.rest_template_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import test.rest_template_test.model.LoginUserData;
import test.rest_template_test.model.RegisterUserData;
import test.rest_template_test.response.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ap/v1/third_party")
@RequiredArgsConstructor
public class ThirdPartyController {
    private final RestTemplate restTemplate;

    private static final String REGISTER_API = "http://localhost:8080/api/auth/auth/register";
    private static final String LOGIN_API = "http://localhost:8080/api/auth/auth/login";
    private static final String CATEGORY_LIST_API = "http://localhost:8080/api/v1/categories";



    @GetMapping("/getResponse")
    public List<Object> getResponse() throws JsonProcessingException {
        Set<Long> roleIdSet = new HashSet<>();
        roleIdSet.add(1L);
        RegisterUserData registerUser = new RegisterUserData(
                "Doniyor",
                "Komilov",
                "doniyor@gmail.com",
                "doniyor123",
                roleIdSet
        );
        String body = makeBodyWithUser(registerUser);
        HttpHeaders headers = makeHeader();

        HttpEntity<String> registrationEntity = new HttpEntity<>(body,headers);
        ResponseEntity<String> response =
                restTemplate.exchange(REGISTER_API, HttpMethod.POST, registrationEntity, String.class);
        if(response.getStatusCode() == HttpStatus.OK){
            LoginUserData loginData = new LoginUserData(
                    "doniyor@gmail.com",
                    "doniyor123"
            );
            String loginBody = makeBodyWithUser(loginData);
            HttpHeaders loginHeaders = makeHeader();
            HttpEntity<String> loginEntity = new HttpEntity<>(loginBody, loginHeaders);
            ResponseEntity<Response> loginResponse =
                    restTemplate.exchange(LOGIN_API, HttpMethod.POST, loginEntity, Response.class);
            if(loginResponse.getStatusCode() == HttpStatus.OK){
                String token = "Bearer " + loginResponse.getBody().getData();
                HttpHeaders httpHeaders = makeHeader();
                httpHeaders.set("Authorization", token);
                HttpEntity<?> jwtEntity = new HttpEntity<>(httpHeaders);
                ResponseEntity<Response> exchange =
                        restTemplate.exchange(CATEGORY_LIST_API, HttpMethod.GET, jwtEntity, Response.class);
                if(exchange.getStatusCode() == HttpStatus.OK){
                    List<Object> dataList = exchange.getBody().getDataList();
                    return dataList;
                }
            }
        }
        return null;
    }

    private HttpHeaders makeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String makeBodyWithUser(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}

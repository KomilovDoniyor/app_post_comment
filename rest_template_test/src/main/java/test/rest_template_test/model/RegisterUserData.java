package test.rest_template_test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Set<Long> roleIdSet;
}

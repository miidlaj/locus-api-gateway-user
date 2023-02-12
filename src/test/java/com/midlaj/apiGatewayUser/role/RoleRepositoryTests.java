package com.midlaj.apiGatewayUser.role;

import com.midlaj.apiGatewayUser.model.Role;
import com.midlaj.apiGatewayUser.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repo;


    @Test
    public void testCreateRestRoles(){
        Role roleClient = new Role("ROLE_USER", "Can reserve, book and do payments.");
        Role roleOwner = new Role("ROLE_OWNER", "Can manage resort under them");

        repo.saveAll(List.of(roleClient,roleOwner));
    }
}

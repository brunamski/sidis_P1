package com.example.projeto.usermanagement.api;

import com.example.projeto.usermanagement.models.Role;
import com.example.projeto.usermanagement.models.User;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-06T16:05:56+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class UserEditMapperImpl extends UserEditMapper {

    @Override
    public User create(CreateUserRequest request) {
        if ( request == null ) {
            return null;
        }

        String username = null;
        String password = null;

        username = request.getUsername();
        password = request.getPassword();

        User user = new User( username, password );

        user.setAuthorities( stringToRole( request.getAuthorities() ) );

        return user;
    }

    @Override
    public void update(UpdateUserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( user.getAuthorities() != null ) {
            Set<Role> set = stringToRole( request.getAuthorities() );
            if ( set != null ) {
                user.getAuthorities().clear();
                user.getAuthorities().addAll( set );
            }
        }
        else {
            Set<Role> set = stringToRole( request.getAuthorities() );
            if ( set != null ) {
                user.setAuthorities( set );
            }
        }
    }
}

package example.auth;

import example.jooq.tables.records.UsersRecord;
import example.model.LoginAccount;
import org.jooq.DSLContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import static example.jooq.Tables.*;

@Repository
public class LoginAccountRepository implements UserDetailsService {

    private DSLContext dsl;

    public LoginAccountRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersRecord record = dsl
                .select()
                .from(USERS)
                .where(USERS.EMAIL.eq(username))
                .fetchOptionalInto(UsersRecord.class)
                .orElseThrow(() -> new UsernameNotFoundException("user not found."));
        return new LoginAccount(record.getId(), username, record.getPasswordDigest(), record.getIsAdmin());
    }

}

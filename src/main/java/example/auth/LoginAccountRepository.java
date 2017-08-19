package example.auth;

import example.jooq.tables.records.UsersRecord;
import example.model.LoginAccount;
import example.util.Digest;
import example.util.DigestFactory;
import org.jooq.DSLContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;

import static example.jooq.Tables.*;

@Repository
public class LoginAccountRepository implements UserDetailsService {

    private DSLContext dsl;

    public LoginAccountRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public LoginAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersRecord record = dsl
                .select()
                .from(USERS)
                .where(USERS.EMAIL.eq(username))
                .fetchOptionalInto(UsersRecord.class)
                .orElseThrow(() -> new UsernameNotFoundException("user not found."));
        return new LoginAccount(
                record.getId(),
                username,
                record.getPasswordDigest(),
                record.getIsAdmin(),
                record.getActivated(),
                DigestFactory.fromDigest(record.getActivationDigest()),
                DigestFactory.fromDigest(record.getResetDigest()));
    }

    public LoginAccount updateActivate(LoginAccount loginAccount, Instant activatedAt) {
        dsl.update(USERS)
                .set(USERS.ACTIVATED, true)
                .set(USERS.ACTIVATED_AT, Timestamp.from(activatedAt))
                .where(USERS.ID.eq(loginAccount.getUserId()))
                .execute();
        return new LoginAccount(
                loginAccount.getUserId(),
                loginAccount.getUsername(),
                loginAccount.getPassword(),
                loginAccount.getIsAdmin(),
                true,
                loginAccount.getActivationDigest(),
                loginAccount.getResetDigest());
    }

    public LoginAccount updatePasswordReset(LoginAccount loginAccount, Digest resetDigest, Instant resetSentAt) {
        dsl.update(USERS)
                .set(USERS.ACTIVATED, true)
                .set(USERS.RESET_DIGEST, resetDigest.getValue())
                .set(USERS.RESET_SENT_AT, Timestamp.from(resetSentAt))
                .where(USERS.ID.eq(loginAccount.getUserId()))
                .execute();
        return new LoginAccount(
                loginAccount.getUserId(),
                loginAccount.getUsername(),
                loginAccount.getPassword(),
                loginAccount.getIsAdmin(),
                true,
                loginAccount.getActivationDigest(),
                resetDigest);
    }

}

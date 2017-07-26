package example.repositories;

import example.auth.PasswordDigestFactory;
import example.jooq.tables.records.UsersRecord;
import example.auth.PasswordDigest;
import example.model.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static example.jooq.Tables.*;

@Repository
public class UserRepository {

    private DSLContext dsl;
    private PasswordDigestFactory passwordDigestFactory;

    public UserRepository(DSLContext dsl, PasswordDigestFactory passwordDigestFactory) {
        this.dsl = dsl;
        this.passwordDigestFactory = passwordDigestFactory;
    }

    public User add(String name, String email, String password) {
        PasswordDigest passwordDigest = passwordDigestFactory.create(password);
        UsersRecord record =
                dsl.insertInto(USERS, USERS.NAME, USERS.EMAIL, USERS.PASSWORD_DIGEST)
                    .values(name, email, passwordDigest.getValue())
                    .returning(USERS.ID)
                    .fetchOne();
        return User.from(record.getId(), name, email);
    }

    public User update(User user, String name, String email, String password) {
        if (password != null) {
            PasswordDigest passwordDigest = passwordDigestFactory.create(password);
            dsl.update(USERS)
                    .set(USERS.NAME, name)
                    .set(USERS.EMAIL, email)
                    .set(USERS.PASSWORD_DIGEST, passwordDigest.getValue())
                    .where(USERS.ID.eq(user.getId()))
                    .execute();
        } else {
            dsl.update(USERS)
                    .set(USERS.NAME, name)
                    .set(USERS.EMAIL, email)
                    .where(USERS.ID.eq(user.getId()))
                    .execute();
        }
        return User.from(user.getId(), name, email);
    }

    public Optional<User> findById(int id) {
        Optional<UsersRecord> record = dsl.select().from(USERS).where(USERS.ID.eq(id)).fetchOptionalInto(UsersRecord.class);
        return record.map(r ->
                User.from(
                        r.getId(),
                        r.getName(),
                        r.getEmail()
                ));
    }

    public List<User> select(int limit, int offset) {
        return dsl.select().from(USERS).limit(limit).offset(offset)
                .fetch()
                .into(UsersRecord.class)
                .stream()
                .map(r -> User.from(
                        r.getId(),
                        r.getName(),
                        r.getEmail()))
                .collect(Collectors.toList());
    }

    public Integer count() {
        return dsl.selectCount().from(USERS).fetchOne(0, Integer.class);
    }

    public void remove(User user) {
        dsl.deleteFrom(USERS).where(USERS.ID.eq(user.getId())).execute();
    }
}

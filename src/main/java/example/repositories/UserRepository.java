package example.repositories;

import example.util.DigestFactory;
import example.jooq.tables.records.UsersRecord;
import example.util.Digest;
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

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public User add(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("user is already exists.");
        }
        UsersRecord record =
                dsl.insertInto(USERS, USERS.NAME, USERS.EMAIL, USERS.PASSWORD_DIGEST, USERS.ACTIVATED, USERS.ACTIVATION_DIGEST)
                    .values(user.getName(), user.getEmail(), user.getPasswordDigest().getValue(), false, user.getActivationDigest().getValue())
                    .returning(USERS.ID)
                    .fetchOne();
        return User.from(record.getId(), user.getName(), user.getEmail());
    }

    public User update(User user, String name, String email, String password) {
        if (password != null) {
            Digest digest = DigestFactory.create(password);
            dsl.update(USERS)
                    .set(USERS.NAME, name)
                    .set(USERS.EMAIL, email)
                    .set(USERS.PASSWORD_DIGEST, digest.getValue())
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

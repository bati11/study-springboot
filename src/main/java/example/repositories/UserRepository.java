package example.repositories;

import example.jooq.tables.pojos.Users;
import example.model.PasswordDigest;
import example.model.User;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static example.jooq.Tables.*;

@Repository
public class UserRepository {

    @Autowired
    private DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<User> findById(int id) {
        Users record = dsl.select().from(USERS).where(USERS.ID.eq(id)).fetchAny().into(Users.class);
        if (record == null) {
            return Optional.empty();
        } else {
            User user = User.from(
                    record.getId(),
                    record.getName(),
                    record.getEmail(),
                    PasswordDigest.fromDigest(record.getPasswordDigest())
            );
            return Optional.of(user);
        }
    }
}

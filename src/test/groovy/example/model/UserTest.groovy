package example.model

import example.auth.PasswordDigestFactory
import spock.lang.Shared
import spock.lang.Specification

class UserTest extends Specification {

    @Shared
    User user

    def setup() {
        user = User.create('hoge', 'fuga@example.com', new PasswordDigestFactory().create('password'))
    }

    def 'users properties'() {
        expect:
        user.getName() == 'hoge'
        user.getPasswordDigest() != 'password'
    }
}

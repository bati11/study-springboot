package example.model

import spock.lang.Shared
import spock.lang.Specification

class UserTest extends Specification {

    @Shared
    User user

    def setup() {
        user = new User('hoge', 'fuga@example.com', new PasswordDigest('password'))
    }

    def 'users properties'() {
        expect:
        user.getName() == 'hoge'
        user.getPasswordDigest() != 'password'
    }

    def 'authentication test'() {
        expect:
        user.authenticate(null) == false
        user.authenticate('') == false
        user.authenticate('hogehoge') == false
        user.authenticate('password') == true
    }
}

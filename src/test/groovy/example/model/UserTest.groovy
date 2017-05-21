package example.model

import spock.lang.Shared
import spock.lang.Specification

class UserTest extends Specification {

    @Shared
    User user

    def setup() {
        user = User.create('hoge', 'fuga@example.com', PasswordDigest.create('password'))
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

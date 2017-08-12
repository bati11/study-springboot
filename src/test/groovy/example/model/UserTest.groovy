package example.model

import spock.lang.Shared
import spock.lang.Specification

class UserTest extends Specification {

    @Shared
    User user

    def setup() {
        user = User.from(1, 'hoge', 'fuga@example.com')
    }

    def 'users properties'() {
        expect:
        user.getName() == 'hoge'
    }
}

package example.model

import spock.lang.Shared
import spock.lang.Specification

class UserTest extends Specification {

    @Shared
    User user

    def setup() {
        user = new User(1, 'hoge', 'fuga@example.com')
    }

    def 'sample test'() {
        expect:
        user.getName() == 'hoge'
    }
}

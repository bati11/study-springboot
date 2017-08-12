import example.model.User
import example.util.DigestFactory
import integrationtestutils.AbstractSpecification

class SessionsTest extends AbstractSpecification {

    def "should get input"() {
        expect:
        with(get("/login")) {
            viewName == "sessions/input"
        }
    }

    def "login with valid information"() {
        setup:
        def user = userRepository.add(
                User.create("hoge1", "hoge1@example.com"),
                DigestFactory.create("123456"),
                DigestFactory.create("hoge1_activation_token")
        )

        when:
        def result = login(user.email, "123456")

        then:
        result.redirectLocation ==~ /\/login\/success/

        when:
        def loginAccount = loginAccountRepository.loadUserByUsername(user.email)
        result = redirect(result.redirectLocation, loginAccount)

        then:
        result.redirectLocation ==~ /\/users\/\d/

        when:
        result = redirect(result.redirectLocation, loginAccount)

        then:
        result.select('.login-user-menu').size() == 1

        when:
        result = get('/logout')

        then:
        result.select('.login-user-menu').size() == 0
    }
}

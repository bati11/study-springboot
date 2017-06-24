import example.auth.LoginAccountRepository
import example.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired

class SessionsControllerIntegrationTest extends AbstractSpecification {
    @Autowired
    UserRepository userRepository

    @Autowired
    LoginAccountRepository loginAccountRepository

    def "should get input"() {
        expect:
        with(get("/login")) {
            viewName == "sessions/input"
        }
    }

    def "login with valid information"() {
        setup:
        def user = userRepository.add("hoge1", "hoge1@example.com", "123456")

        when:
        def result = login(user.email, "123456")

        then:
        result.redirectLocation ==~ /\/login\/success/

        when:
        def loginAccount = loginAccountRepository.loadUserByUsername(user.email)
        result = redirect(loginAccount, result.redirectLocation)

        then:
        result.redirectLocation ==~ /\/users\/\d/
    }
}

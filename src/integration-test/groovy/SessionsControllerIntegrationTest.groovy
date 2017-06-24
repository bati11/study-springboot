import example.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired

class SessionsControllerIntegrationTest extends AbstractSpecification {
    @Autowired
    UserRepository userRepository

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
        result = redirectAfterLogin(result)

        then:
        result.redirectLocation ==~ /\/users\/\d/
    }
}

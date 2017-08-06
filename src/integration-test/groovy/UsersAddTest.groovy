import example.auth.LoginAccountRepository
import integrationtestutils.AbstractSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders

class UsersAddTest extends AbstractSpecification {

    @Autowired
    LoginAccountRepository loginAccountRepository

    def "should get input"() {
        expect:
        with(get("/signup")) {
            viewName == "users/input"
        }
    }

    def "invalid signup information"() {
        setup:
        HttpHeaders params = new HttpHeaders();
        params.put("name", [""])
        params.put("email", ["invalid email"])
        params.put("password", ["foo"])
        params.put("passwordConfirmation", ["bar"])

        expect:
        with(post("/users", params)) {
            viewName == "users/input"
            select('.has-error').size() != 0
            select('.user-name .text-danger').size() == 1
            select('.user-email .text-danger').size() == 1
            select('.user-password .text-danger').size() == 1
            select('.user-password-confirmation .text-danger').size() == 1
        }
    }

    def "invalid password confirmation"() {
        setup:
        HttpHeaders params = new HttpHeaders();
        params.put("name", ["hoge"])
        params.put("email", ["hoge@example.com"])
        params.put("password", ["foofoofoo"])
        params.put("passwordConfirmation", ["barbarbar"])

        expect:
        with(post("/users", params)) {
            viewName == "users/input"
            select('.has-error').size() != 0
            select('.user-name .text-danger').size() == 0
            select('.user-email .text-danger').size() == 0
            select('.user-password .text-danger').size() == 1
            select('.user-password-confirmation .text-danger').size() == 0
        }
    }

    def "valid signup information"() {
        setup:
        HttpHeaders params = new HttpHeaders();
        params.put("name", ["AAA"])
        params.put("email", ["aaa@example.com"])
        params.put("password", ["foofoofoo"])
        params.put("passwordConfirmation", ["foofoofoo"])

        when:
        def result = post("/users", params)

        then:
        result.redirectLocation ==~ /\/users\/\d+/

        when:
        def loginAccount = loginAccountRepository.loadUserByUsername(params.get("email"))
        result = redirect(result.redirectLocation, loginAccount)

        then:
        result.select('.login-user-menu').size() == 1
    }
}

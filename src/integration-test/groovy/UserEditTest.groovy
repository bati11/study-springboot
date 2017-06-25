import integrationtestutils.AbstractSpecification
import org.springframework.http.HttpHeaders
import org.springframework.security.test.context.support.WithMockUser

class UserEditTest extends AbstractSpecification {

    @WithMockUser
    def "should get edit"() {
        expect:
        with(get("/users/${testUser.id}/edit")) {
            viewName == "users/edit"
        }
    }

    @WithMockUser
    def "unsuccessful edit"() {
        setup:
        HttpHeaders params = new HttpHeaders();
        params.put("name", [""])
        params.put("email", ["invalid email"])
        params.put("password", ["foo"])
        params.put("passwordConfirmation", ["bar"])

        expect:
        with(post("/users/${testUser.id}/update", params)) {
            viewName == "users/edit"
        }
    }

    @WithMockUser
    def "successful edit"() {
        setup:
        HttpHeaders params = new HttpHeaders();
        params.put("name", ["Foo Bar"])
        params.put("email", ["foo@bar.com"])
        params.put("password", [""])
        params.put("passwordConfirmation", [""])

        when:
        def result = post("/users/${testUser.id}/update", params)

        then:
        result.redirectLocation ==~ /\/users\/${testUser.id}/

        when:
        result = redirect(result.redirectLocation)

        then:
        result.viewName == "users/show"
    }

    def "should redirect update when logged in as wrong user"() {
        setup:
        def otherUser = userRepository.add("other_user", "other_user@example.com", "222")
        def loginAccount = loginAccountRepository.loadUserByUsername(otherUser.email)

        expect:
        with(get("/users/${testUser.id}/edit", loginAccount)) {
            status == 403
        }
    }
}

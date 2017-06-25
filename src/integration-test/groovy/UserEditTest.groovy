import integrationtestutils.AbstractSpecification
import org.springframework.http.HttpHeaders
import org.springframework.security.test.context.support.WithMockUser

@WithMockUser
class UserEditTest extends AbstractSpecification {

    def "should get edit"() {
        expect:
        with(get("/users/${testUser.id}/edit")) {
            viewName == "users/edit"
        }
    }

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
}

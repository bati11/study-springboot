import org.springframework.http.HttpHeaders
import org.springframework.util.MultiValueMap

class UsersControllerIntegrationTest extends AbstractSpecification {

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
        params.put("email", ["user@invalid"])
        params.put("password", ["foo"])
        params.put("passwordConfirm", ["bar"])

        expect:
        with(post("/users", params)) {
            viewName == "users/input"
            select('.has-error').size() != 0
        }
    }
}

import example.model.User
import integrationtestutils.AbstractSpecification
import org.springframework.security.test.context.support.WithMockUser

class UsersPageTest extends AbstractSpecification {

    def "should redirect index when not logged in"() {
        expect:
        with(get("/users")) {
            status == 302
        }
    }

    def "should redirect destroy when not logged in"() {
        def addedUser = userRepository.add(User.create("hogehoge", "hoge@example.com", "123456"))
        expect:
        with(post("/users/${addedUser.id}/destroy")) {
            status == 302
            userRepository.findById(addedUser.id).get() != null
        }
    }

    @WithMockUser(authorities = "USER")
    def "should redirect destroy when logged in as a non-admin"() {
        def addedUser = userRepository.add(User.create("hogehoge", "hoge@example.com", "123456"))
        expect:
        with(post("/users/${addedUser.id}/destroy")) {
            status == 403
            userRepository.findById(addedUser.id).get() != null
        }
    }
}

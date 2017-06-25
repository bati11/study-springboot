import integrationtestutils.AbstractSpecification

class UsersPageTest extends AbstractSpecification {

    def "should redirect index when not logged in"() {
        expect:
        with(get("/users")) {
            status == 302
        }
    }
}

class UsersControllerIntegrationTest extends AbstractSpecification {

    def "should get input"() {
        expect:
        with(get("/users/input")) {
            viewName == "users/input"
        }
    }
}

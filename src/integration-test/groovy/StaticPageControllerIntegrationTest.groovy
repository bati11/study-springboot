class StaticPageControllerIntegrationTest extends AbstractSpecification {

    def baseTitle;

    def setup() throws Exception {
        baseTitle = "Spring Boot Sample App"
    }

    def "should get home"() {
        expect:
        with(get("/")) {
            viewName == "static_pages/home"
            select('title').text() == "Home | $baseTitle"
        }
    }

    def "should get help"() {
        expect:
        with(get("/help")) {
            viewName == "static_pages/help"
            select('title').text() == "Help | $baseTitle"
        }
    }

    def "should get about"() {
        expect:
        with(get("/about")) {
            viewName == "static_pages/about"
            select('title').text() == "About | $baseTitle"
        }
    }

    def "should get contact"() {
        expect:
        with(get("/contact")) {
            viewName == "static_pages/contact"
            select('title').text() == "Contact | $baseTitle"
        }
    }
}

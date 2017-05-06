import example.Application
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.web.WebDelegatingSmartContextLoader
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WebAppConfiguration
@TestPropertySource("/application-integrationtest.properties")
@ContextConfiguration(loader = WebDelegatingSmartContextLoader, classes = Application)
class StaticPageControllerIntegrationTest extends Specification {

    @Autowired
    WebApplicationContext context;

    @Shared
    MockMvc mvc;

    def baseTitle;

    def setup() throws Exception {
        mvc = webAppContextSetup(context).build()
        baseTitle = "Spring Boot Sample App"
    }

    def "should get home"() {
        expect:
        def result = mvc.perform(get("/")).andReturn()
        result.modelAndView.viewName == "static_pages/home"

        def doc = Jsoup.parse(result.response.contentAsString)
        doc.select('title').text() == "Home | $baseTitle"
    }

    def "should get help"() {
        expect:
        def result = mvc.perform(get("/help")).andReturn()
        result.modelAndView.viewName == "static_pages/help"

        def doc = Jsoup.parse(result.response.contentAsString)
        doc.select('title').text() == "Help | $baseTitle"
    }

    def "should get about"() {
        expect:
        def result = mvc.perform(get("/about")).andReturn()
        result.modelAndView.viewName == "static_pages/about"

        def doc = Jsoup.parse(result.response.contentAsString)
        doc.select('title').text() == "About | $baseTitle"
    }

    def "should get contact"() {
        expect:
        def result = mvc.perform(get("/contact")).andReturn()
        result.modelAndView.viewName == "static_pages/contact"

        def doc = Jsoup.parse(result.response.contentAsString)
        doc.select('title').text() == "Contact | $baseTitle"
    }
}

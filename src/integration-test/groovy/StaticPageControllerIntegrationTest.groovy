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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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
        def content = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("static_pages/home"))
                .andReturn().response.contentAsString
        def doc = Jsoup.parse(content)
        doc.select('title').text() == "Home | $baseTitle"
    }

    def "should get help"() {
        expect:
        def content = mvc.perform(get("/help"))
                .andExpect(status().isOk())
                .andExpect(view().name("static_pages/help"))
                .andReturn().response.contentAsString
        def doc = Jsoup.parse(content)
        doc.select('title').text() == "Help | $baseTitle"
    }

    def "should get about"() {
        expect:
        def content = mvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("static_pages/about"))
                .andReturn().response.contentAsString
        def doc = Jsoup.parse(content)
        doc.select('title').text() == "About | $baseTitle"
    }

    def "should get contact"() {
        expect:
        def content = mvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("static_pages/contact"))
                .andReturn().response.contentAsString
        def doc = Jsoup.parse(content)
        doc.select('title').text() == "Contact | $baseTitle"
    }
}

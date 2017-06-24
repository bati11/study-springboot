import example.Application
import example.auth.LoginAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.MultiValueMap
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [Application.class])
@AutoConfigureMockMvc
@TestPropertySource("/application-integrationtest.properties")
abstract class AbstractSpecification extends Specification {

    @Autowired
    private LoginAccountRepository loginAccountRepository

    @Autowired
    private MockMvc mockMvc

    def get(String path) {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(path)
        ).andReturn()
        return new MyMvcResult(mvcResult)
    }

    def post(String path, MultiValueMap<String, String> params) {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(path)
                        .params(params)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andReturn()
        return new MyMvcResult(mvcResult)
    }

    def login(String email, String password) {
        MvcResult mvcResult = mockMvc.perform(
                SecurityMockMvcRequestBuilders.formLogin()
                        .userParameter("email")
                        .passwordParam("password")
                        .user(email)
                        .password(password)
        )
        .andExpect(MockMvcResultMatchers.status().isFound())
        .andExpect(SecurityMockMvcResultMatchers.authenticated())
        .andReturn()
        def result = new MyMvcResult(mvcResult)
        result.lastLoginUserEmail = email
        result.lastLoginUserPassword = password
        return result
    }

    def redirectAfterLogin(MyMvcResult mvcResult) {
        def loginAccount = loginAccountRepository.loadUserByUsername(mvcResult.lastLoginUserEmail)
        def location = mvcResult.redirectLocation
        def result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(location)
                        .with(SecurityMockMvcRequestPostProcessors.user(loginAccount))
        )
        .andReturn()
        return new MyMvcResult(result)
    }

    def view(MyMvcResult myMvcResult) {
        return myMvcResult.mvcResult.modelAndView.viewName
    }
}

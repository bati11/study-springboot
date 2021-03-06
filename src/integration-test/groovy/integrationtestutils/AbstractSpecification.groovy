package integrationtestutils

import example.Application
import example.auth.LoginAccountRepository
import example.model.LoginAccount
import example.model.User
import example.repositories.UserRepository
import example.util.DigestFactory
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
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import spock.lang.Specification

import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = [Application.class])
@AutoConfigureMockMvc
@TestPropertySource("/application-integrationtest.properties")
@Transactional
abstract class AbstractSpecification extends Specification {

    static String TEST_USER_EMAIL = "test_user@example.com"
    static String TEST_USER_PASSWORD = "111"

    @Autowired
    UserRepository userRepository

    @Autowired
    LoginAccountRepository loginAccountRepository

    @Autowired
    private MockMvc mockMvc

    User testUser

    def setup() {
        testUser = createActivatedUser("test_user", TEST_USER_EMAIL, TEST_USER_EMAIL)
    }

    def createActivatedUser(String name, String email, String password) {
        def user = userRepository.add(
                User.create(name, email),
                DigestFactory.create(password),
                DigestFactory.create("activation_token")
        )
        def loginAccount = loginAccountRepository.loadUserByUsername(user.getEmail())
        loginAccountRepository.updateActivate(loginAccount, Instant.now())
        return user
    }

    def get(String path) {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(path)
        ).andReturn()
        return new MyMvcResult(mvcResult)
    }

    def get(String path, LoginAccount loginAccount) {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(path)
                        .with(SecurityMockMvcRequestPostProcessors.user(loginAccount))
        ).andReturn()
        return new MyMvcResult(mvcResult)
    }

    def post(String path) {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(path)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
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

    def post(String path, MultiValueMap<String, String> params, LoginAccount loginAccount) {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(path)
                        .params(params)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(loginAccount))
        ).andReturn()
        return new MyMvcResult(mvcResult)
    }
    def redirect(String redirectLocation) {
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get(redirectLocation)
        ).andReturn()
        return new MyMvcResult(result)
    }

    def redirect(String redirectLocation, LoginAccount loginAccount) {
        def result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(redirectLocation)
                        .with(SecurityMockMvcRequestPostProcessors.user(loginAccount))
        ).andReturn()
        return new MyMvcResult(result)
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

    def view(MyMvcResult myMvcResult) {
        return myMvcResult.mvcResult.modelAndView.viewName
    }
}

package com.twentiecker.storyapp.authentication.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.LoginResponse
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.utils.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val email: String = "twentiecker@gmail.com"
    private val pass: String = "123456"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userPreference: UserPreference

    @Mock
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(userPreference, loginRepository)
    }

    @Test
    fun `when Get LoginResponse Should Not Null and Return Success`() {
        val expectedLogin = MutableLiveData<ApiResult<LoginResponse>>()
        expectedLogin.value = ApiResult.Success(dummyLoginResponse)
        `when`(loginRepository.loginService(email, pass)).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.loginService(email, pass).getOrAwaitValue()

        Mockito.verify(loginRepository).loginService(email, pass)
        assertNotNull(actualLogin)  // Memastikan data tidak null.
        assertTrue(actualLogin is ApiResult.Success)   // Memastikan mengembalikan Result.Success.
        assertEquals(
            dummyLoginResponse, (actualLogin as ApiResult.Success).data
        )  // Memastikan data sesuai dengan yang diharapkan.
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLogin = MutableLiveData<ApiResult<LoginResponse>>()
        expectedLogin.value = ApiResult.Error("Error")
        `when`(loginRepository.loginService(email, pass)).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.loginService(email, pass).getOrAwaitValue()

        Mockito.verify(loginRepository).loginService(email, pass)
        assertNotNull(actualLogin)  // Memastikan data tidak null.
        assertTrue(actualLogin is ApiResult.Error) // Memastikan mengembalikan Result.Error.
    }
}
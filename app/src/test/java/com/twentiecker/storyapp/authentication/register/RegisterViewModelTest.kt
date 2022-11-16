package com.twentiecker.storyapp.authentication.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.RegisterResponse
import com.twentiecker.storyapp.utils.DataDummy
import com.twentiecker.storyapp.utils.getOrAwaitValue
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
class RegisterViewModelTest {
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val name: String = "joko"
    private val email: String = "twentiecker@gmail.com"
    private val pass: String = "123456"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var registerRepository: RegisterRepository

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(registerRepository)
    }

    @Test
    fun `when Get RegisterResponse Should Not Null and Return Success`() {
        val expectedRegister = MutableLiveData<ApiResult<RegisterResponse>>()
        expectedRegister.value = ApiResult.Success(dummyRegisterResponse)
        `when`(registerRepository.registerService(name, email, pass))
            .thenReturn(expectedRegister)

        val actualRegister = registerViewModel.registerService(name, email, pass).getOrAwaitValue()

        Mockito.verify(registerRepository).registerService(name, email, pass)
        assertNotNull(actualRegister)  // Memastikan data tidak null.
        assertTrue(actualRegister is ApiResult.Success)   // Memastikan mengembalikan Result.Success.
        assertEquals(
            dummyRegisterResponse, (actualRegister as ApiResult.Success).data
        )  // Memastikan data sesuai dengan yang diharapkan.
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedRegister = MutableLiveData<ApiResult<RegisterResponse>>()
        expectedRegister.value = ApiResult.Error("Error")
        `when`(registerRepository.registerService(name, email, pass)).thenReturn(expectedRegister)

        val actualRegister = registerViewModel.registerService(name, email, pass).getOrAwaitValue()

        Mockito.verify(registerRepository).registerService(name, email, pass)
        assertNotNull(actualRegister)  // Memastikan data tidak null.
        assertTrue(actualRegister is ApiResult.Error)   // Memastikan mengembalikan Result.Error.
    }
}
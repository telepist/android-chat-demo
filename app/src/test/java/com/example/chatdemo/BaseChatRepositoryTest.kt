package com.example.chatdemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chatdemo.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

@RunWith(JUnit4::class)
abstract class BaseChatRepositoryTest : KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(module {
            single { repository }
        })
    }

    @MockK
    lateinit var repository: Repository

    @Before
    fun setUpBase() = MockKAnnotations.init(this, relaxUnitFun = true)

    @After
    open fun tearDownBase() = stopKoin()

}
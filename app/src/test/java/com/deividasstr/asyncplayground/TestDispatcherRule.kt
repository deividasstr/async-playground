package com.deividasstr.asyncplayground

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalStdlibApi::class)
@ExperimentalCoroutinesApi
class TestDispatcherRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    fun test(context: CoroutineContext = EmptyCoroutineContext, testBody: suspend TestScope.() -> Unit) {
        val actualContext = testDispatcher + context
        val dispatcher = requireNotNull(actualContext[CoroutineDispatcher])
        if (dispatcher != testDispatcher) Dispatchers.setMain(dispatcher)
        runTest(context = actualContext, testBody = testBody)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun TestScope.getDispatcher(): TestDispatcher {
    return coroutineContext[CoroutineDispatcher] as TestDispatcher
}

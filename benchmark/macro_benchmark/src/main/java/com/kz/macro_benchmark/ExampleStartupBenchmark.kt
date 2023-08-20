package com.kz.macro_benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.kz.coding",
        metrics = listOf(StartupTimingMetric()),
        iterations = 2,
        startupMode = StartupMode.COLD,
    ) {
        pressHome()
        startActivityAndWait()

        device.waitForIdle()
        device.findObject(By.res("txtSearch")).text = ""

        sleep(1000)
        val list = device.findObject(By.res("item_list"))
        list.setGestureMargin(device.displayHeight / 5)
        list.fling(Direction.DOWN)
    }
}
package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveReminder_GetById() = runBlockingTest {
        val reminder = ReminderDTO("Sleep", "Sleep At Home", "Al-Obour", 30.226714049202812, 31.480876645769275)

        database.reminderDao().saveReminder(reminder)

        val result = database.reminderDao().getReminderById(reminder.id)

        assertThat(result as ReminderDTO, notNullValue())
        assertThat(result.id, `is`(reminder.id))
        assertThat(result.title, `is`(reminder.title))
        assertThat(result.description, `is`(reminder.description))
        assertThat(result.location, `is`(reminder.location))
        assertThat(result.latitude, `is`(reminder.latitude))
        assertThat(result.longitude, `is`(reminder.longitude))
    }

    @Test
    fun getAllRemindersFromDb() = runBlockingTest {
        val reminder1 = ReminderDTO("Grand Mom", "Visit My Mom", "Giza", 30.014094236697332, 31.211961710736638)
        val reminder2 = ReminderDTO("Cousin", "Visit My Cousin", "Giza",  30.014094236697332, 31.211961710736638)
        val reminder3 = ReminderDTO("My Gym", "Get to the Gym", "Al-Obour Gym", 30.226714049202812, 31.480876645769275)
        val reminder4 = ReminderDTO("Playing Tennis", "Get to the Tennis in 9:00", "Al-Obour Club", 30.226714049202812, 31.480876645769275)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)
        database.reminderDao().saveReminder(reminder4)

        val remindersList = database.reminderDao().getReminders()

       assertThat(remindersList, `is`(notNullValue()))
    }

    @Test
    fun insertReminders_deleteAllReminders() = runBlockingTest {
        val reminder1 = ReminderDTO("Grand Mom", "Visit My Mom", "Giza", 30.014094236697332, 31.211961710736638)
        val reminder2 = ReminderDTO("Cousin", "Visit My Cousin", "Giza",  30.014094236697332, 31.211961710736638)
        val reminder3 = ReminderDTO("My Gym", "Get to the Gym", "Al-Obour Gym", 30.226714049202812, 31.480876645769275)
        val reminder4 = ReminderDTO("Playing Tennis", "Get to the Tennis in 9:00", "Al-Obour Club", 30.226714049202812, 31.480876645769275)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)
        database.reminderDao().saveReminder(reminder4)

        database.reminderDao().deleteAllReminders()

        val remindersList = database.reminderDao().getReminders()

        assertThat(remindersList, `is`(emptyList()))
    }
}
package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.*;
import task.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TaskTest {
    Task task1;
    Task task2;
    Task task3;
    Duration duration1 = Duration.ofMinutes(100);
    LocalDateTime startTime1 = LocalDateTime.of(LocalDate.of(2025, 2, 4),
            LocalTime.of(10, 0));
    private Object StatusTask;
    
    @BeforeEach
    void setUp() {
        task1 = new Task("TestName1", "TestDescription1", Status.NEW, 1, startTime1, duration1);
        task2 = new Task("TestName2", "TestDescription2", Status.NEW, 2, startTime1, duration1);
        task3 = new Task("TestName3", "TestDescription3", Status.NEW, 1, startTime1, duration1);

    }

    @Test
    void shouldEqualsIsSameIds() {
        Assertions.assertEquals(task1, task3);
    }

    @Test
    void shouldNotEqualsIsDifferentIds() {
        Assertions.assertNotEquals(task1, task2);
    }

    @Test
    void taskToString() {
        Assertions.assertEquals("1,TASK,TestName1,NEW,TestDescription1,PT1H40M,10:00 04.02.25,11:40 04.02.25", task1.toString());
    }
    
}
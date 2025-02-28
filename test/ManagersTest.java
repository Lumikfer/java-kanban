package solid;

import manager.*;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void shouldCreateDefaultManager() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldCreateDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistory());
    }
}
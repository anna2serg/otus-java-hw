package homework;

import testframework.annotation.After;
import testframework.annotation.Before;
import testframework.annotation.DisplayName;
import testframework.annotation.Test;

class LifeCycleTest {

    @Before
    public void before1() {
        System.out.print("@Before: before1 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @Before
    public void before2() {
        System.out.print("@Before: before2 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @Test
    @DisplayName("Хорошо когда выполняется anyTest1")
    void anyTest1() {
        System.out.print("@Test: anyTest1 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @Test
    @DisplayName("Должен выполниться anyTest2, но что-то может пойти не так")
    void anyTest2() {
        System.out.print("@Test: anyTest2 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
        throw new UnsupportedOperationException("Нарочно бросаем исключение");
    }

    @Test
    @DisplayName("Тут написано что должен проверить anyTest3")
    void anyTest3() {
        System.out.print("@Test: anyTest3 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @Test
    void anyTestWithoutDisplayName() {
        System.out.print("@Test: anyTestWithoutDisplayName ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @After
    public void after1() {
        System.out.print("@After: after1 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @After
    public void after2() {
        System.out.print("@After: after2 ");
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

}

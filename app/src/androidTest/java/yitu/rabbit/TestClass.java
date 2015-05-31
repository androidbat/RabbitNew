package yitu.rabbit;

import android.test.InstrumentationTestCase;

/**
 * Created by wg on 2015/5/24.
 */
public class TestClass extends InstrumentationTestCase {

    public void test() throws Exception {
        UserDao ud = new UserDao();
        ud.getCount();
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, ud.getCount());
    }

}

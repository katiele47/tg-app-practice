package com.tgapp.mytranscribeglass;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private ImplicitIntent mImplicit;
    @Before
    public void setUp() {
        mImplicit = new ImplicitIntent();
    }
    @Test
    public void addTwoNumbers() {
        assertEquals(.3d, .1d+.2d, .03d);//relaxationß
    }
    @Test
    public void addTwoNumbersFloat() {
        double result = mImplicit.add(2.55f,3.77f);
        assertThat("hey there", result, is(equalTo(6.32)));
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 4);
    }
}
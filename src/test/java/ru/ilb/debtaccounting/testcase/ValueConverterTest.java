/*
 * Copyright 2019 slavb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.debtaccounting.testcase;

import ru.ilb.testcase.ValueConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author slavb
 */
public class ValueConverterTest {

    public ValueConverterTest() {
    }

    /**
     * Test of convertValue method, of class ValueConverter.
     */
    @Test
    public void testConvertValue() {
        System.out.println("convertValue");
        Double value = 10.0;
        Integer expResult = 10;
        Object result = ValueConverter.convertValue(value);
        assertEquals(expResult, result);
    }

       /**
     * Test of convertValue method, of class ValueConverter.
     */
    @Test
    public void testConvertValue2() {
        System.out.println("convertValue");
        Double value = 10.1;
        Double expResult = 10.1;
        Object result = ValueConverter.convertValue(value);
        assertEquals(expResult, result);
    }

}

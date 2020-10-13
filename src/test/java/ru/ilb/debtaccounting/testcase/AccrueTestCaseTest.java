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

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author slavb
 */
public class AccrueTestCaseTest {

    private static final String TESTCASE_SOURCE = "testcases/createloan/accrueinterests.ods";

    public AccrueTestCaseTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws IOException {

    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getInputData method, of class TestCase.
     * @throws java.io.IOException
     */
    @Test
    public void testGetInputData() throws IOException {
        System.out.println("getInputData");
        InputStream testData = this.getClass().getClassLoader().getResourceAsStream(TESTCASE_SOURCE);
        AccrueTestCase instance = new AccrueTestCase(testData);
        Map<String, Object> expResult = new HashMap<>();
        expResult.put("amount", 561744.19);
        expResult.put("period", 72);
        expResult.put("rate", 0.145);
        expResult.put("disbursementDate", LocalDate.of(2019, 05, 10));
        expResult.put("eventDate", LocalDate.of(2019, 06, 17));
        Map<String, Object> result = instance.getInputData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInputDataJson method, of class TestCase.
     * @throws java.io.IOException
     */
    @Test
    public void testGetInputDataJson() throws IOException {
        System.out.println("getInputDataJson");
        InputStream testData = this.getClass().getClassLoader().getResourceAsStream(TESTCASE_SOURCE);
        AccrueTestCase instance = new AccrueTestCase(testData);
        String expResult = "{\"amount\":561744.19,\"period\":72,\"rate\":0.145,\"disbursementDate\":\"2019-05-10\",\"eventDate\":\"2019-06-17\"}";
        String result = instance.getInputDataJson();

        assertEquals(expResult, result);
    }

    /**
     * Test of getCalculationTable method, of class TestCase.
     * @throws java.io.IOException
     */
    @Test
    public void testGetCalculationTable() throws IOException {
        System.out.println("getCalculationTable");
        InputStream testData = this.getClass().getClassLoader().getResourceAsStream(TESTCASE_SOURCE);
        AccrueTestCase instance = new AccrueTestCase(testData);
        Map<String, Object[]> result = instance.getCalculationTable();
        int expectedRows=73;
        assertEquals(expectedRows, result.get("PAYMENTDATE").length);
        assertEquals(expectedRows, result.get("PAYMENT").length);
        assertEquals(expectedRows, result.get("PAYMENTDEBT").length);
        assertEquals(expectedRows, result.get("PAYMENTINT").length);
        assertEquals(expectedRows, result.get("DEBTREMAINDER").length);
        //assertEquals(0, result.get("T")[0]);
    }

}

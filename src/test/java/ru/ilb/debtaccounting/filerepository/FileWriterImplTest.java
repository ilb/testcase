/*
 * Copyright 2020 MrazotaOne.
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
package ru.ilb.debtaccounting.filerepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.ilb.debtaccounting.paymentschedule.Payment;

/**
 *
 * @author MrazotaOne
 */
public class FileWriterImplTest {

    public FileWriterImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of toOds method, of class FileWriterImpl.
     */
    @Test
    public void testToOds() {
        System.out.println("toOds");
        List<Payment> payments = new ArrayList<>();
        Payment payment = new Payment();
        payment.setAmountDelayedInterests(BigDecimal.ONE);
        payment.setAmountInterestOnCreditHolidays(BigDecimal.ONE);
        payment.setAmountInterestsOnOverdue(BigDecimal.ONE);
        payment.setAmountInterests(BigDecimal.ONE);
        payment.setAmountPrincipal(BigDecimal.ONE);
        payment.setDate(LocalDate.now());
        payments.add(payment);
        payments.add(payment);
        payments.add(payment);
        FileWriterImpl instance = new FileWriterImpl();
        //instance.toOds(payments);

    }

    /**
     * Test of toXhtml method, of class FileWriterImpl.
     */
    @Test
    public void testToXhtml() {
        /*System.out.println("toXhtml");
        List<Payment> payemnts = null;
        FileWriterImpl instance = new FileWriterImpl();
        instance.toXhtml(payemnts);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");*/
    }

}

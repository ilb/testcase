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
import java.util.List;
import ru.ilb.debtaccounting.model.Transaction;
import ru.ilb.debtaccounting.paymentschedule.Payment;
/**
 *
 * @author safargalin
 */
public interface FileWriter {
    public void toOds(List<Payment> payment);
    public void transactionsToOds(List<Transaction> transactions);
    public void toXhtml(List<Payment> payemnts);
}

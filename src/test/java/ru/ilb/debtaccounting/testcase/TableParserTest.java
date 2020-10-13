/*
 * Copyright 2020 safargalin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * /home/safargalin/work/debtaccounting/trunk/debtaccounting/debtaccounting-testcase/src/test/resources/testcases/createloan
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ilb.debtaccounting.testcase;

import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.ilb.debtaccounting.filerepository.TableParser;
import ru.ilb.debtaccounting.filerepository.TableParserFactory;
import ru.ilb.debtaccounting.filerepository.TableParserFactoryImpl;

/**
 *
 * @author safargalin
 */
public class TableParserTest {
    private final String TESTCASE_SOURCE = "/testcases/interestsstatement/interestsstatement.ods";


    @Test
    public void testIt() throws IOException {
        TableParserFactory parserFactory = new TableParserFactoryImpl();
        TableParser parser = parserFactory.getTableParser(this.getClass().getResourceAsStream(TESTCASE_SOURCE));
        Map<String, Object[]> vTable = parser.getTable(0, "ratesByDate");
        Map<String, Object[]> hTable = parser.getTable(0, "tableNameThree");
    }

}

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
package ru.ilb.testcase;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author slavb
 */
public class ODSTestCase extends TestCase {

    private static final String INPUT_DATA_SHEET_NAME = "INPUT DATA";
    private static final int INPUT_DATA_VARIABLE_NAME_COLUMN = 0;
    private static final int INPUT_DATA_VARIABLE_VALUE_COLUMN = 1;

    private static final String CALCULATION_TABLE_SHEET_NAME = "CALCULATION TABLE";

    public ODSTestCase(File file) throws IOException {
        this(new SpreadSheet(file));
    }

    public ODSTestCase(InputStream in) throws IOException {
        this(new SpreadSheet(in));
    }

    public ODSTestCase(SpreadSheet spreadSheet) throws IOException {

        Sheet inputSheet = spreadSheet.getSheet(INPUT_DATA_SHEET_NAME);
        if (inputSheet == null) {
            throw new InputDataNotFoundException();
        }
        inputData = loadInputData(inputSheet);

        Sheet calcTableSheet = spreadSheet.getSheet(CALCULATION_TABLE_SHEET_NAME);
        if (calcTableSheet == null) {
            throw new CalculationTableNotFoundException();
        }

        calculationTable = loadCalculationTable(calcTableSheet);

    }

    private static Map<String, Object> loadInputData(Sheet sheet) {
        Map<String, Object> inputData = new HashMap<>();

        // skip first row
        for (int i = 1; i < sheet.getMaxRows(); i++) {
            Range rangeName = sheet.getRange(i, INPUT_DATA_VARIABLE_NAME_COLUMN);
            Range rangeValue = sheet.getRange(i, INPUT_DATA_VARIABLE_VALUE_COLUMN);

            String varName = rangeName.getValue().toString();
            Object varValue = rangeValue.getValue();
            inputData.put(varName, ValueConverter.convertValue(varValue));
        }
        return inputData;
    }

    private static Map<String, Object[]> loadCalculationTable(Sheet sheet) {
        Map<String, Object[]> columns = new HashMap<>();

        Range rangeNames = sheet.getRange(0, 0, 1, sheet.getMaxColumns());
        Object[] columnNames = rangeNames.getValues()[0];

        for (int j = 0; j < sheet.getMaxColumns(); j++) {
            Range rangeColumn = sheet.getRange(1, j, sheet.getMaxRows() - 1, 1);

            String colName = columnNames[j].toString();
            Object[][] colValues2D = rangeColumn.getValues();
            Object[] colValues = new Object[sheet.getMaxRows() - 1];
            for (int i = 0; i < sheet.getMaxRows() - 1; i++) {
                colValues[i] = ValueConverter.convertValue(colValues2D[i][0]);
            }
            columns.put(colName, colValues);
        }
        return columns;
    }

}

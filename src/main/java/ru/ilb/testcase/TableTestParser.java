/*
 * Copyright 2020 safargalin.
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
import java.util.List;
import java.util.Map;

/**
 *
 * @author safargalin
 */
public class TableTestParser extends TestCase {

    public TableTestParser(File file) throws IOException {
        this(new SpreadSheet(file));
    }

    public TableTestParser(InputStream in) throws IOException {
        this(new SpreadSheet(in));
    }

    public TableTestParser(SpreadSheet spreadSheet) throws IOException {
        Sheet inputSheet = spreadSheet.getSheet(0);

        if (inputSheet == null) {
            throw new InputDataNotFoundException();
        }

        for (int i = 1; i < inputSheet.getMaxRows(); i++) {
            Range rangeName = inputSheet.getRange(i, 0);
            if (rangeName.getValue() == null) {
                continue;
            }
            /*if(rangeName.getValue().toString().startsWith("###")){
                inputData = getVerticalTable(i+1, inputSheet);
            }*/

            if (rangeName.getValue().toString().startsWith("&&&")) {
                Map<String, List<Object>> q = getHorisontalTable(i + 1, inputSheet);
                break;
            }
        }
    }

    private static Map<String, Object> getVerticalTable(int index, Sheet sheet) {
        Map<String, Object> inputData = new HashMap<>();
        // skip first row
        for (int i = index; i < sheet.getMaxRows(); i++) {
            Range rangeName = sheet.getRange(i, 0);
            Range rangeValue = sheet.getRange(i, 1);
            if (rangeName.getValue() == null) {
                continue;
            }
            String varName = rangeName.getValue().toString();
            Object varValue = rangeValue.getValue();
            if (varName.startsWith("&&&") || varName.startsWith("###")) {
                break;
            }
            inputData.put(varName, ValueConverter.convertValue(varValue));
        }
        return inputData;
    }

    private static Map<String, List<Object>> getHorisontalTable(int index, Sheet sheet) {
        Map<String, List<Object>> listMaps = new HashMap();
        for (int i = index; i < sheet.getMaxRows(); i++) {
            Range rangeName = sheet.getRange(i, sheet.getMaxColumns() - 1);
            if (i == index) {
                for (Object value : rangeName.getValues()) {
                    listMaps.put(value.toString(), null);
                }
                continue;
            }
            int ind = 0;
            for (Map.Entry<String, List<Object>> entry : listMaps.entrySet()) {
                entry.getValue().add(rangeName.getValues()[i]);
                i++;
            }
        }
        return listMaps;
    }

}

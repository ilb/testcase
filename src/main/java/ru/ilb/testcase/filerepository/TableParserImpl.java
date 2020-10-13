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
package ru.ilb.testcase.filerepository;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.ilb.testcase.InputDataNotFoundException;

/**
 *
 * @author safargalin
 */
public class TableParserImpl implements TableParser{

    private final SpreadSheet spreadSheet;
    private static final String VERTICAL_TABLE_KEY = "/";
    private static final String HORIZONTAL_TABLE_KEY = "#";
    private static final String COMMENT = "*";

    public TableParserImpl(File file) throws IOException{
        this.spreadSheet = new SpreadSheet(file);
    }
    public TableParserImpl(InputStream in) throws IOException{
        this.spreadSheet = new SpreadSheet(in);
    }
    public TableParserImpl(SpreadSheet sheet){
        this.spreadSheet = sheet;
    }

    @Override
    public int getMaxPages() {
        return spreadSheet.getNumSheets();
    }

    @Override
    public int getSize(Map<String, Object[]> table) {
        return table.entrySet().iterator().next().getValue().length;
    }

    @Override
    public Map<String, Object[]> getTable(int pageNumber, String TableName){
        Sheet inputSheet = spreadSheet.getSheet(pageNumber);
        if (inputSheet == null) {
            throw new InputDataNotFoundException();
        }
        return getTable(inputSheet.getName(), TableName);
    }

    @Override
    public Map<String, Object[]> getTable(String pageName, String tableName){
        Sheet inputSheet = spreadSheet.getSheet(pageName);
        if (inputSheet == null) {
            throw new InputDataNotFoundException();
        }
        Map<String, Object>  map = new HashMap<>();
        for(int i = 1; i < inputSheet.getMaxRows(); i++){
            Range rangeName = inputSheet.getRange(i, 0);
            if (rangeName.getValue() == null)
                continue;
            if(rangeName.getValue().toString().contains(tableName)){
                if(rangeName.getValue().toString().startsWith(VERTICAL_TABLE_KEY + tableName + VERTICAL_TABLE_KEY)){
                    return getVerticalTable(i+1, inputSheet);
                }
                if(rangeName.getValue().toString().startsWith(HORIZONTAL_TABLE_KEY + tableName + HORIZONTAL_TABLE_KEY)){
                   return getHorizontalTable(i+1, inputSheet);
                }
            }
        }
        return null;
    }

    private static Map<String, Object[]> getVerticalTable(int index, Sheet sheet){
        Map<String, Object[]> inputData = new HashMap<>();
        Object[][] range = sheet.getRange(index, 1, 1, sheet.getMaxColumns()-1).getValues();
        List<Integer> list = new ArrayList<>();
        for(int j=0;j<range[0].length;j++){
            if(range[0][j] == null){
                list.add(j);
                continue;
            }
            if(range[0][j].toString().endsWith(COMMENT))
                list.add(j);
        }
        for (int i = index+1; i<sheet.getMaxRows(); i++){
            Range rangeName = sheet.getRange(i, 0);
            if(rangeName.getValue() == null)
                continue;
            if(rangeName.getValue().toString().endsWith(COMMENT))
                continue;
            if(isNewTable(rangeName.getValues()))
                break;
            Range rangeValue = sheet.getRange(i, 1, 1, sheet.getMaxColumns() - 1);
            String varName = rangeName.getValue().toString();
            Object[][] varValue = rangeValue.getValues();
            if(!varName.endsWith(COMMENT))
                inputData.put(varName, getVerticalArray(list, varValue));
        }
        return inputData;
    }

    private static Map<String, Object[]> getHorizontalTable(int index, Sheet sheet){
        Map<String, Object[]> table = new HashMap<>();
        Object[][] rangeRows = sheet.getRange(index+2, 0, sheet.getMaxRows()-index-2, 1).getValues();
        List<Integer> list = new ArrayList<>();
        int lastRow = sheet.getMaxRows()-index-2;
        for(int j=0;j<rangeRows.length;j++){
            if(rangeRows[j][0] == null){
                list.add(j);
                continue;
            }
            if(rangeRows[j][0].toString().endsWith(COMMENT)){
                list.add(j);
                continue;
            }

            if(rangeRows[j][0].toString().startsWith(HORIZONTAL_TABLE_KEY) || rangeRows[j][0].toString().startsWith(VERTICAL_TABLE_KEY)){
                lastRow = j-1;
                break;
            }
        }
        Object[][] rangeNames = sheet.getRange(index, 0, 2, sheet.getMaxColumns()).getValues();
        int lenght = getHorizontalArray(list, Arrays.copyOf(rangeRows, lastRow)).length;
        if(lenght == 0)
            return table;
        for (int j = 0; j < sheet.getMaxColumns(); j++) {
            if(rangeNames[0][j]!= null){
                if(!rangeNames[0][j].toString().endsWith(COMMENT) && rangeNames[1][j]!= null){
                    Object[][] rangeColumn = sheet.getRange(index+2, j, lastRow, 1).getValues();
                    Object[] value = Arrays.copyOfRange(getHorizontalArray(list, rangeColumn), 0, lenght);
                    table.put(rangeNames[1][j].toString(), value);
                }
            }
        }
        return table;
    }

    private static Object[] getVerticalArray(List<Integer> list, Object[][] arr){
        int count = 0;
        for(int i = 0; i<arr[0].length;i++){
            if(list.contains(i) || arr[0][i] == null){
                arr[0][i] = null;
                count++;
            }
        }
        Object[] newArr = new Object[arr[0].length-count];
        int j = 0;
        for(Object obj : arr[0]){
            if(obj!=null){
                newArr[j]=obj;
                j++;
            }
        }
        return newArr;
    }

    private static Object[] getHorizontalArray(List<Integer> list, Object[][] arr){
        List<Object> arrayList = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            if(!list.contains(i) && arr[i][0]!=null)
                arrayList.add(arr[i][0]);
        }
        return arrayList.toArray();

    }

    private static boolean isNewTable(Object[][] arr){
        for (Object[] arr1 : arr) {
            for (Object item : arr1) {
                if (item != null) {
                    String varName = item.toString();
                    if(varName.startsWith(HORIZONTAL_TABLE_KEY) || varName.startsWith(VERTICAL_TABLE_KEY))
                        return true;
                }
            }
        }
        return false;
    }
}

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

import com.github.miachm.sods.OfficePercentage;

/**
 * Converter of spreadsheet values
 * @author slavb
 */
public class ValueConverter {

    public static Object convertValue(Object value) {

        if (value instanceof Double) {
            value = convertValue((Double) value);
        }
        if (value instanceof OfficePercentage){
            value = convertValue((OfficePercentage) value);
        }
        return value;
    }

    /**
     * Convert double values to int in case of no fractional portion
     * @param value
     * @return
     */
    private static Object convertValue(Double value) {
        int intValue = value.intValue();
        if (Double.valueOf(intValue).equals(value)) {
            return intValue;
        }
        return value;
    }

    private static Object convertValue(OfficePercentage value) {
        return convertValue(value.getValue());
    }

}

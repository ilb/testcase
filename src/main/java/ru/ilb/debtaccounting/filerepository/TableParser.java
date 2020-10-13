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
package ru.ilb.debtaccounting.filerepository;

import java.util.Map;

/**
 *
 * @author safargalin
 */
public interface TableParser {

    public Map<String, Object[]> getTable(int pageNumber, String TableName);

    public Map<String, Object[]> getTable(String pageName, String TableName);

    public int getMaxPages();

    public int getSize(Map<String, Object[]> table);

}

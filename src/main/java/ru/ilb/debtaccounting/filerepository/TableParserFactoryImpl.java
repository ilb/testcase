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

import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author safargalin
 */
public class TableParserFactoryImpl implements TableParserFactory {

    @Override
    public TableParser getTableParser(File file) throws IOException{
            return new TableParserImpl(file);
    }

    @Override
    public TableParser getTableParser(InputStream in) throws IOException{
         return new TableParserImpl(in);
    }

    @Override
    public TableParser getTableParser(SpreadSheet sheet){
         return new TableParserImpl(sheet);
    }
}

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

import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import ru.ilb.debtaccounting.model.Transaction;
import ru.ilb.debtaccounting.enums.TransactionType;
import ru.ilb.debtaccounting.paymentschedule.Payment;
import ru.ilb.debtaccounting.paymentschedule.Payments;

/**
 *
 * @author safargalin
 */
public class FileWriterImpl implements FileWriter {

    @Override
    public void toOds(List<Payment> payments) {

        int rows = payments.size() + 1;
        int columns = 6;
        Sheet sheet = new Sheet("PaymentList", rows, columns);

        Object[][] obj = new Object[rows][columns];
        obj[0][0] = "Дата";
        obj[0][1] = "ОД";
        obj[0][2] = "%%";
        obj[0][3] = "Отложенные проценты";
        obj[0][4] = "Проценты за ЛП";
        obj[0][5] = "Просроченные проценты";
        for (int i = 0; i < payments.size(); i++) {
            obj[i + 1][0] = payments.get(i).getDate().format(DateTimeFormatter.ISO_DATE);
            obj[i + 1][1] = payments.get(i).getAmountPrincipal();
            obj[i + 1][2] = payments.get(i).getAmountInterests();
            obj[i + 1][3] = payments.get(i).getAmountDelayedInterests();
            obj[i + 1][4] = payments.get(i).getAmountInterestOnCreditHolidays();
            obj[i + 1][5] = payments.get(i).getAmountInterestsOverdue();
        }
        sheet.getDataRange().setValues(obj);

        SpreadSheet spread = new SpreadSheet();
        spread.appendSheet(sheet);
        try {
            spread.save(new File("Out.ods"));
        } catch (IOException ex) {
            Logger.getLogger(FileWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void toXhtml(List<Payment> payemnts) {
        try {
            StreamSource text = new StreamSource(new StringReader(jaxbObjectToXML(payemnts.get(0))));
            StreamSource xlsStreamSource = new StreamSource(Paths.get("src/test/resources/xslthtml/stylesheet.xsl").toAbsolutePath().toFile());
            StreamSource xmlStreamSource = new StreamSource(Paths.get("src/test/resources/xslthtml/payment.xml").toAbsolutePath().toFile());
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Path pathToHtmlFile = Paths.get("src/test/resources/xslthtml/myfile.html");
            StreamResult result = new StreamResult(pathToHtmlFile.toFile());
            Transformer transformer = transformerFactory.newTransformer(xlsStreamSource);
            //transformer.transform(text, result);

//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            StreamResult result = new StreamResult(baos);
//            transformerFactory.newTransformer(xlsStreamSource).transform(xmlStreamSource, result);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(FileWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException | JAXBException ex) {
            Logger.getLogger(FileWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Payment> convert(List<Payments> paymentList) {
        List<Payment> payments = new ArrayList<>();
        paymentList.stream().forEach(x -> {
            x.getPayments().stream().forEach(y -> {
                payments.add(y);
            });
        });
        return payments;
    }

    private String jaxbObjectToXML(Payment payments) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Payments.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(payments, sw);
        return sw.toString();
    }

    @Override
    public void transactionsToOds(List<Transaction> transactions) {
        List<LocalDate> distinctElements = transactions.stream()
                .filter(distinctByKey(p -> p.getDate()))
                .map(Transaction::getDate)
                .collect(Collectors.toList());
        int rows = distinctElements.size() + 1;
        int columns = 6;
        Sheet sheet = new Sheet("PaymentList", rows, columns);

        Object[][] obj = new Object[rows][columns];
        obj[0][0] = "Дата";
        obj[0][1] = "ОД";
        obj[0][2] = "%%";
        obj[0][3] = "Отложенные проценты";
        obj[0][4] = "Проценты за ЛП";
        obj[0][5] = "Просроченные проценты";

        for (int i = 0; i < distinctElements.size(); i++) {

            obj[i + 1][0] = distinctElements.get(i).format(DateTimeFormatter.ISO_DATE);
            obj[i + 1][1] = 0;
            obj[i + 1][2] = 0;
            obj[i + 1][3] = 0;
            obj[i + 1][4] = 0;
            obj[i + 1][5] = 0;
            LocalDate date = distinctElements.get(i);
            Transaction trPrincipal = transactions.stream()
                    .filter(y -> y.getDate().equals(date))
                    .filter(x -> x.getType().equals(TransactionType.REPAYMENT_PRINCIPAL))
                    .findFirst().get();
            if (trPrincipal != null) {
                obj[i + 1][1] = trPrincipal.getAmount().getAmount();
            }

            Transaction trInterests = transactions.stream()
                    .filter(y -> y.getDate().equals(date))
                    .filter(x -> x.getType().equals(TransactionType.REPAYMENT_INTERESTS))
                    .findFirst().get();
            if (trInterests != null) {
                obj[i + 1][2] = trInterests.getAmount().getAmount();
            }

            Transaction trDelayedOverdue = transactions.stream()
                    .filter(y -> y.getDate().equals(date))
                    .filter(x -> x.getType().equals(TransactionType.REPAYMENT_DELAYED_INTERESTS))
                    .findFirst().get();
            if (trDelayedOverdue != null) {
                obj[i + 1][3] = trDelayedOverdue.getAmount().getAmount();
            }

            Transaction trCreditHolidays = transactions.stream()
                    .filter(y -> y.getDate().equals(date))
                    .filter(x -> x.getType().equals(TransactionType.REPAYMENT_CREDIT_HOLIDAYS))
                    .findFirst().get();
            if (trCreditHolidays != null) {
                obj[i + 1][4] = trCreditHolidays.getAmount().getAmount();
            }

            Transaction trInterestsOverdue = transactions.stream()
                    .filter(y -> y.getDate().equals(date))
                    .filter(x -> x.getType().equals(TransactionType.REPAYMENT_INTERESTS_OVERDUE))
                    .findFirst().get();
            if (trInterestsOverdue != null) {
                obj[i + 1][5] = trInterestsOverdue.getAmount().getAmount();
            }

        }

        sheet.getDataRange().setValues(obj);

        SpreadSheet spread = new SpreadSheet();
        spread.appendSheet(sheet);
        try {
            spread.save(new File("Out.ods"));
        } catch (IOException ex) {
            Logger.getLogger(FileWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

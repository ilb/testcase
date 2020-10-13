<?xml version="1.0"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xpil="http://www.together.at/2006/XPIL1.0"
    xmlns:xpdl="http://www.wfmc.org/2002/XPDL1.0"
    exclude-result-prefixes="xsl xpil xpdl"
    version="1.0">

    <xsl:output
        media-type="application/xhtml+xml"
        method="xml"
        encoding="UTF-8"
        indent="yes"
        omit-xml-declaration="no"
        doctype-public="-//W3C//DTD XHTML 1.1//EN"
        doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" />

    <xsl:template match="payments*">
        <xsl:variable name="hyperlink">
            <xsl:value-of select="root/data/link"/>
        </xsl:variable>
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
                <title>Repayments</title>
                <style type="text/css">
                    table {
                    width: auto;
                    border-collapse: collapse;
                    }
                    td, th {
                    padding: 3px;
                    border: 1px solid black;
                    }
                </style>
            </head>
            <body>
                <h2>Repayments</h2>

                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>N</th>
                            <th>Date</th>
                            <th>DateFact</th>
                            <th>Amount</th>
                            <th>AmountInterests</th>
                            <th>AmountPrincipal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="Payment">
                            <tr>
                                <td>
                                    <xsl:value-of select="number" />
                                </td>
                                <td>
                                    <xsl:value-of select="date" />
                                </td>
                                <td>
                                    <xsl:value-of select="dateFact" />
                                </td>
                                <td>
                                    <xsl:value-of select="amount" />
                                </td>
                                <td>
                                    <xsl:value-of select="amountPrincipal" />
                                </td>
                                <td>
                                    <xsl:value-of select="amountInterests" />
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
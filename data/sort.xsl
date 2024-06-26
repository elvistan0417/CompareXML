<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- for well formatted output -->
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="*">
        <!-- copy this node (but this does not copy the attributes ...) -->
        <xsl:copy>
            <!-- so, copy the attributes as well -->
            <xsl:copy-of select="@*"/>
            <!-- recurse on sorted (by tag name) list of child nodes -->
            <xsl:apply-templates>
                <xsl:sort select="name()"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
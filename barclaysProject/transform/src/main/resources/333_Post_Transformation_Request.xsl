<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<idFactura><xsl:value-of select="root/serviceId"/></idFactura>
		<valorFactura><xsl:value-of select="root/billValue"/></valorFactura>
	</xsl:template>
</xsl:stylesheet>
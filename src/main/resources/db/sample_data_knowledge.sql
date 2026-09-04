-- =====================================================================
-- Sample seed data for AI Reporting Studio - table: knowledge
-- Database: ai_reporting (MariaDB)
--
-- This script is executed automatically by Spring Boot on every startup
-- (spring.sql.init.mode=always). It is IDEMPOTENT: rows are inserted with
-- INSERT IGNORE, so an existing title (unique constraint) is skipped.
-- Data below was exported from the local MariaDB (ai_reporting) on 2026-08-31.
--
-- Manual execution:
--   mysql -u ai_reporting -p ai_reporting < src/main/resources/db/sample_data_knowledge.sql
-- =====================================================================

-- ---------------------------------------------------------------------
-- Knowledge (single example JRXML template - JasperReports 7.0.3)
-- ---------------------------------------------------------------------
INSERT IGNORE INTO knowledge (knowledge_id, title, content, created_at, updated_at, is_deleted) VALUES ('20000000-0000-0000-0000-000000000001', 'JasperReports 7.0.3 Example Template', '<jasperReport name="Sample Jasper Report" language="java" pageWidth="595" pageHeight="842" whenNoDataType="BlankPage" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="f6e14b18-9bd6-4036-aa69-53a127f64729">
	<title height="15">
		<element kind="textField" uuid="d2eb7d1e-a310-4a64-a6c4-138979b006eb" positionType="Float" x="0" y="0" width="555" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" blankWhenNull="true" hTextAlign="Right" vTextAlign="Top">
			<expression><![CDATA["This is title"]]></expression>
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<box topPadding="1" bottomPadding="1"/>
		</element>
	</title>
	<pageHeader height="15">
		<element kind="textField" uuid="a09123f1-98ab-461a-9563-6bfa2676c8e7" positionType="Float" x="0" y="0" width="555" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" blankWhenNull="true" hTextAlign="Right" vTextAlign="Top">
			<expression><![CDATA["This is page header"]]></expression>
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<box topPadding="1" bottomPadding="1"/>
		</element>
	</pageHeader>
	<detail>
		<band height="16">
			<element kind="textField" uuid="4655d9e0-6469-44d7-b41d-7540bfc03698" positionType="Float" mode="Opaque" x="0" y="0" width="555" height="16" backcolor="#B8B4B4" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Left" vTextAlign="Top">
				<expression><![CDATA["Table 1 Header"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2">
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<property name="com.jaspersoft.studio.layout" value="com.jaspersoft.studio.editor.layout.HorizontalRowLayout"/>
			<property name="com.jaspersoft.studio.unit.height" value="pixel"/>
		</band>
		<band height="16">
			<element kind="frame" uuid="fb0958e3-8cfc-458a-8444-d3b109595832" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="0" y="0" width="111" height="16" backcolor="#E6DFDF">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="32bdf59c-699c-4340-a8c5-c0f814ecdff3" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="111" y="0" width="111" height="16" backcolor="#E6DFDF">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="fc94ce1f-a7b1-4684-acc8-8760fe42d7c2" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="222" y="0" width="111" height="16" backcolor="#E6DFDF">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="d1209cfe-923f-4c60-9b6c-aad6c3e8ceaa" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="333" y="0" width="111" height="16" backcolor="#E6DFDF">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="62976240-410e-4bd2-a587-34461940ae37" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="444" y="0" width="111" height="16" backcolor="#E6DFDF">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="textField" uuid="a733ecc7-5960-4828-8e0d-9c8e6885492f" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 1"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="2308d6b3-f9a8-40be-9ff2-a08a2e1190a4" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 2"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="ba0ff14b-6ef1-4f96-b5f0-d2b1c731ae39" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 3"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="bb6b97f2-0130-419b-9d1e-ece9db4f237f" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 4"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="7f1ca3fa-bf4f-49eb-8e84-3b1be2f8b3f5" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 5"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
		</band>
		<band height="15">
			<element kind="frame" uuid="da37501b-840f-459e-9232-1ef27d1990e3" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="ae59d766-803d-48f6-b090-dffeea2b7766" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="d5e2ea17-a7f8-499e-8f75-6a0f18080a54" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="20d0b7f9-0283-4f79-b0b3-bde035a34478" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="f36d12a2-88c1-4614-8cb6-f302ff2fed87" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="textField" uuid="6ead0968-8974-41a7-899e-19645d3c2d99" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 1"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="51786e2c-2b23-4c66-9c3e-011dbeedfe42" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 2"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="1fc1139f-ddc2-4dc8-8095-688d22175ed9" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 3"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="4da9153b-6f28-41a0-9168-ef4d57b9a97b" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 4"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="58a4d304-d5ba-4226-9205-e86b4fbbe0e7" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 5"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
		</band>
		<band height="15">
			<element kind="frame" uuid="32abf3da-1e4c-403a-a263-3e6f9655c878" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="697de10b-f47a-45f4-83b8-12d96bfa2069" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="d0884e9c-2cbc-4790-860a-e0e9137eeb92" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="6c4b6e6c-3d7f-4b6d-a93b-a41c38c2ad28" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="1ffd7cc1-c654-47be-a88a-5a4f5e47f986" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="textField" uuid="b6c7b604-a101-492a-9e2a-9ae7d262bdb8" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 1"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="1e72fdfa-e869-423a-a52b-1f89742aec6d" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 2"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="9a0f287c-15fe-4ea4-b6cd-0256cb1be8d3" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 3"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="15b73d8c-c11c-4f9e-998d-7b284af35765" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 4"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="2f94c256-b657-4d01-acc5-7afe0b98a19f" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 5"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
		</band>
		<band height="15">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
		</band>
		<band height="16">
			<element kind="textField" uuid="a5a4a552-1fdc-4930-be28-bbfe58b145ff" positionType="Float" mode="Opaque" x="0" y="0" width="555" height="16" backcolor="#B2AEAE" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Left" vTextAlign="Top">
				<expression><![CDATA["Table 2 Header"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2">
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
		</band>
		<band height="16">
			<element kind="frame" uuid="874dbaf8-a693-4264-a3fb-c648f6dc08ef" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="0" y="0" width="111" height="16" backcolor="#D5CECE">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="071b36f7-ce35-4235-a4de-d47c17565026" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="111" y="0" width="111" height="16" backcolor="#D5CECE">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="c61aa052-f3ba-498d-a84b-f51eb669bc9e" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="222" y="0" width="111" height="16" backcolor="#D5CECE">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="d34a3395-e93b-4574-9561-148b6ab929e7" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="333" y="0" width="111" height="16" backcolor="#D5CECE">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="b2da89f9-0a3f-4e30-90fd-2f20091687d6" positionType="Float" stretchType="ElementGroupHeight" mode="Opaque" x="444" y="0" width="111" height="16" backcolor="#D5CECE">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="textField" uuid="f122cf73-0569-42bb-8d48-275c4d5c8f51" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 1"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="1c8186ae-1baf-49ac-af6e-692f0414b3a1" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 2"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="4985d86c-4ee1-44da-b973-9eb166494b73" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 3"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="244b7596-9b61-463f-b0fe-4df5d6e272ce" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 4"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="e9d738ec-f441-450c-b20f-b7d6d973ab6c" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="16" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" bold="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Header 5"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
			</element>
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
		</band>
		<band height="15">
			<element kind="frame" uuid="e3118626-2848-4807-a1e9-f1d5ba14aaef" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="cfb9feea-9c4a-4045-9a6c-49f93802f64e" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="2804fc92-7b36-4cc6-b497-c3baca458c05" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="cd9f7508-0d7a-40d0-8c2e-3cdda490d5dc" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="c4de30cb-04a0-4a8a-b4a1-4c6dd7ffac5f" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="textField" uuid="1ad1577e-27ac-4df0-ba84-0625107adcfe" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 1"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="793a103f-59db-43fe-ac3c-3c8fd60b8582" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 2"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="57f80b2c-d9a9-4548-a7f6-af56617b838d" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 3"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="f0bf81d1-123a-4b58-ba10-3c6051c1e5ad" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 4"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="a5ce845e-cba3-4534-9edd-13095ca59ae4" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 1 Data 5"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
		</band>
		<band height="15">
			<element kind="frame" uuid="16fec01e-29be-4c55-bf32-408b0a01bd1d" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="31cc7c67-af2c-4bcf-b676-4567b913c5e8" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="2c8a8c15-70b1-41ab-a516-acf02f78ccd4" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="da149d31-225d-491d-94a6-0938d888cdbf" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="frame" uuid="2b4eb3b1-d714-4cdb-bc3a-d51e661d7179" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15">
				<borderSplitType>DrawBorders</borderSplitType>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
			</element>
			<element kind="textField" uuid="f2afe9f5-f3df-4c26-aa0a-0a907fbdafbf" positionType="Float" stretchType="ElementGroupHeight" x="0" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 1"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="483cd60c-65de-4daa-af79-fc6a231e8618" positionType="Float" stretchType="ElementGroupHeight" x="111" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 2"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="995f2481-8eff-49c8-b206-6db997320e70" positionType="Float" stretchType="ElementGroupHeight" x="222" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 3"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="05dfed11-a507-4c17-97aa-6a0d72d2ed1d" positionType="Float" stretchType="ElementGroupHeight" x="333" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 4"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
			<element kind="textField" uuid="0970dcdf-2a6f-4d83-9e97-d2068d8b1d63" positionType="Float" stretchType="ElementGroupHeight" x="444" y="0" width="111" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" textAdjust="StretchHeight" blankWhenNull="true" hTextAlign="Center" vTextAlign="Top">
				<expression><![CDATA["Column Row 2 Data 5"]]></expression>
				<property name="com.jaspersoft.studio.unit.height" value="px"/>
				<box topPadding="1" leftPadding="2" bottomPadding="1" rightPadding="2"/>
			</element>
		</band>
	</detail>
	<pageFooter height="50">
		<element kind="textField" uuid="9b5899c5-a15c-4e0b-a7fb-3c52430aae2f" positionType="Float" x="0" y="35" width="555" height="15" fontName="Arial&#xd;&#xa;" fontSize="10.0" blankWhenNull="true" hTextAlign="Right" vTextAlign="Top">
			<expression><![CDATA[$V{PAGE_NUMBER}]]></expression>
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<box topPadding="1" bottomPadding="1"/>
		</element>
	</pageFooter>
</jasperReport>', '2026-08-22 12:20:09.000000', '2026-08-23 15:53:49.168709', 0);

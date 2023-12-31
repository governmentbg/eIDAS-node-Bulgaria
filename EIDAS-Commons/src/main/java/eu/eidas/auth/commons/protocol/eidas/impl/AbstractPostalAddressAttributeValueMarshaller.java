/*
 * Copyright (c) 2020 by European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/page/eupl-text-11-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.eidas.auth.commons.protocol.eidas.impl;

import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeValue;
import eu.eidas.auth.commons.attribute.AttributeValueMarshaller;
import eu.eidas.auth.commons.attribute.AttributeValueMarshallingException;
import eu.eidas.auth.commons.lang.Canonicalizers;
import eu.eidas.auth.commons.lang.EnumMapper;
import eu.eidas.auth.commons.lang.KeyAccessor;
import eu.eidas.util.Preconditions;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Base AttributeValueMarshaller implementation for PostalAddress values.
 *
 * @since 1.1
 */
public abstract class AbstractPostalAddressAttributeValueMarshaller implements AttributeValueMarshaller<PostalAddress> {

    public static final String SEPARATOR = "::";
    public static final String NEW_LINE = "\n";
    public static final String SPLITTER = "=";

    public enum Tag {

        PO_BOX("PoBox") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getPoBox();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.poBox(getTagValue(xmlAddress));
            }
        },

        LOCATOR_DESIGNATOR("LocatorDesignator") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getLocatorDesignator();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.locatorDesignator(getTagValue(xmlAddress));
            }
        },

        LOCATOR_NAME("LocatorName") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getLocatorName();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.locatorName(getTagValue(xmlAddress));
            }
        },

        CV_ADDRESS_AREA("CvaddressArea") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getCvAddressArea();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.cvAddressArea(getTagValue(xmlAddress));
            }
        },

        THOROUGHFARE("Thoroughfare") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getThoroughfare();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.thoroughfare(getTagValue(xmlAddress));
            }
        },

        POST_NAME("PostName") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getPostName();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.postName(getTagValue(xmlAddress));
            }
        },

        ADMIN_UNIT_FIRST_LINE("AdminunitFirstline") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getAdminUnitFirstLine();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.adminUnitFirstLine(getTagValue(xmlAddress));
            }
        },

        ADMIN_UNIT_SECOND_LINE("AdminunitSecondline") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getAdminUnitSecondLine();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.adminUnitSecondLine(getTagValue(xmlAddress));
            }
        },

        POST_CODE("PostCode") {
            @Override
            public String getTagValue(@Nonnull PostalAddress postalAddress) {
                return postalAddress.getPostCode();
            }

            @Override
            public void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress) {
                builder.postCode(getTagValue(xmlAddress));
            }
        },

        // put the ; on a separate line to make merges easier
        ;

        private static final EnumMapper<String, Tag> MAPPER =
                new EnumMapper<String, Tag>(new KeyAccessor<String, Tag>() {

                    @Nonnull
                    @Override
                    public String getKey(@Nonnull Tag tag) {
                        return tag.getTagName();
                    }
                }, Canonicalizers.trimLowerCase(), values());

        @Nullable
        public static Tag fromString(@Nonnull String value) {
            return MAPPER.fromKey(value);
        }

        public static EnumMapper<String, Tag> mapper() {
            return MAPPER;
        }

        @Nonnull
        private final transient String tagName;

        @Nonnull
        private final transient Pattern tagPattern;

        Tag(@Nonnull String tagName) {
            this.tagName = tagName;
            tagPattern = Pattern.compile("<(.+:)?" + tagName + ">(.+)</\\1?" + tagName + ">",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }

        public void addTag(@Nonnull StringBuilder result,
                           @Nonnull String prefix,
                           @Nonnull PostalAddress postalAddress) {
            String tagValue = getTagValue(postalAddress);
            if (tagValue != null && StringUtils.isNotBlank(tagValue)) {
                result.append("<").append(prefix).append(":");
                result.append(tagName);
                result.append(">");
                result.append(tagValue);
                result.append("</").append(prefix).append(":");
                result.append(tagName);
                result.append(">");
                result.append("\n");
            }
        }

        @Nonnull
        public String getTagName() {
            return tagName;
        }

        @Nonnull
        public Pattern getTagPattern() {
            return tagPattern;
        }

        @Nullable
        public String getTagValue(@Nonnull String xmlAddress) {
            Matcher matcher = tagPattern.matcher(xmlAddress);
            if (matcher.find()) {
                return matcher.group(2);
            }
            return null;
        }

        public abstract String getTagValue(@Nonnull PostalAddress postalAddress);

        public abstract void setTagValue(@Nonnull PostalAddress.Builder builder, @Nonnull String xmlAddress);

        @Nonnull
        @Override
        public String toString() {
            return tagName;
        }
    }

    @Nonnull
    private final String prefix;

    protected AbstractPostalAddressAttributeValueMarshaller(@Nonnull String prefix) {
        Preconditions.checkNotBlank(prefix, "prefix");
        this.prefix = prefix;
    }

    @Nonnull
    public String getPrefix() {
        return prefix;
    }

    @Nonnull
    @Override
    public String marshal(@Nonnull AttributeValue<PostalAddress> value) throws AttributeValueMarshallingException {
        return EidasStringUtil.encodeToBase64(marshalToXML(value));
    }

    @Nonnull
    public String marshalToXML(@Nonnull AttributeValue<PostalAddress> value) throws AttributeValueMarshallingException {
        PostalAddress postalAddress = value.getValue();

        // TODO use a proper XML parser

        /*
        <xsd:complexType>
            <xsd:sequence>
                <xsd:element name="AddressID" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="PoBox" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="LocatorDesignator" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="LocatorName" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="CvaddressArea" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="Thoroughfare" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="PostName" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="AdminunitFirstline" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="AdminunitSecondline" type="xsd:string" minOccurs="0" maxOccurs="1"/>
                <xsd:element name="PostCode" type="xsd:string" minOccurs="0" maxOccurs="1"/>
            </xsd:sequence>
        </xsd:complexType>
         */

        StringBuilder result = new StringBuilder(200);
        Tag.PO_BOX.addTag(result, prefix, postalAddress);
        Tag.LOCATOR_DESIGNATOR.addTag(result, prefix, postalAddress);
        Tag.LOCATOR_NAME.addTag(result, prefix, postalAddress);
        Tag.CV_ADDRESS_AREA.addTag(result, prefix, postalAddress);
        Tag.THOROUGHFARE.addTag(result, prefix, postalAddress);
        Tag.POST_NAME.addTag(result, prefix, postalAddress);
        Tag.ADMIN_UNIT_FIRST_LINE.addTag(result, prefix, postalAddress);
        Tag.ADMIN_UNIT_SECOND_LINE.addTag(result, prefix, postalAddress);
        Tag.POST_CODE.addTag(result, prefix, postalAddress);

        if (result.length() == 0) {
            return StringUtils.EMPTY;
        }

        // Result must be the base64 encoding of the XML representation of the PostalAddress
        return result.toString();
    }

    @Nonnull
    @Override
    public AttributeValue<PostalAddress> unmarshal(@Nonnull String value, boolean isNonLatinScriptAlternateVersion)
            throws AttributeValueMarshallingException {
        // TODO use a proper XML parser

        String xmlAddress = EidasStringUtil.decodeStringFromBase64(value);

        PostalAddress.Builder builder = PostalAddress.builder();

        Tag.PO_BOX.setTagValue(builder, xmlAddress);
        Tag.LOCATOR_DESIGNATOR.setTagValue(builder, xmlAddress);
        Tag.LOCATOR_NAME.setTagValue(builder, xmlAddress);
        Tag.CV_ADDRESS_AREA.setTagValue(builder, xmlAddress);
        Tag.THOROUGHFARE.setTagValue(builder, xmlAddress);
        Tag.POST_NAME.setTagValue(builder, xmlAddress);
        Tag.ADMIN_UNIT_FIRST_LINE.setTagValue(builder, xmlAddress);
        Tag.ADMIN_UNIT_SECOND_LINE.setTagValue(builder, xmlAddress);
        Tag.POST_CODE.setTagValue(builder, xmlAddress);

        // Result must be the base64 decoding of the Java-XML binding of the PostalAddress
        return new PostalAddressAttributeValue(builder.build());
    }

    public AttributeValue<PostalAddress> unmarshallAddressWithoutDecode(String addressAsString) {
        return createPostalAddressFromString(addressAsString);
    }

    public String getAddressAsString(AttributeValue attributeValue) {
        AttributeValue<PostalAddress> value = attributeValue;
        return value.toString().replaceAll(NEW_LINE, SEPARATOR).replaceAll(": ", SPLITTER);
    }

    private PostalAddressAttributeValue createPostalAddressFromString(String addressAsString) {
        String[] address = StringUtils.remove(addressAsString, '_').split(SEPARATOR);
        PostalAddress.Builder builder = PostalAddress.builder();

        Map<String, String> mapAddress = Arrays.stream(address).
                collect(
                        Collectors.toMap(
                                s -> s.split(SPLITTER)[0],
                                s -> s.split(SPLITTER)[1]
                        )
                );

        mapAddress.forEach((k, v) -> {
            addValuesToBuilder(builder, k, v);

        });
        return new PostalAddressAttributeValue(builder.build());
    }

    private void addValuesToBuilder(PostalAddress.Builder builder, String key, String value) {
        if (Tag.PO_BOX.getTagName().equalsIgnoreCase(key)) {
            builder.poBox(value);
        }
        if (Tag.LOCATOR_DESIGNATOR.getTagName().equalsIgnoreCase(key)) {
            builder.locatorDesignator(value);
        }
        if (Tag.LOCATOR_NAME.getTagName().equalsIgnoreCase(key)) {
            builder.locatorName(value);
        }
        if (Tag.CV_ADDRESS_AREA.getTagName().equalsIgnoreCase(key)) {
            builder.cvAddressArea(value);
        }
        if (Tag.THOROUGHFARE.getTagName().equalsIgnoreCase(key)) {
            builder.thoroughfare(value);
        }
        if (Tag.POST_NAME.getTagName().equalsIgnoreCase(key)) {
            builder.postName(value);
        }
        if (Tag.ADMIN_UNIT_FIRST_LINE.getTagName().equalsIgnoreCase(key)) {
            builder.adminUnitFirstLine(value);
        }
        if (Tag.ADMIN_UNIT_SECOND_LINE.getTagName().equalsIgnoreCase(key)) {
            builder.adminUnitSecondLine(value);
        }
        if (Tag.POST_CODE.getTagName().equalsIgnoreCase(key)) {
            builder.postCode(value);
        }
    }
}

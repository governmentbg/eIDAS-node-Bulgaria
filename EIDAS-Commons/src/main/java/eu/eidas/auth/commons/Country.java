/*
 * Copyright (c) 2019 by European Commission
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
 *
 */
package eu.eidas.auth.commons;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * This class is a bean used to store the information relative to the Country.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 *         luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 *         paulo.ribeiro@multicert.com
 * @version $Revision: 1.10 $, $Date: 2010-11-17 05:15:28 $
 */
public final class Country implements Serializable {
   private static final long serialVersionUID = 1135994036496370993L;

  /**
   * Country Id.
   */
  private String countryId;

  /**
   * Country Name.
   */
  private String countryName;

  /**
   * Country Constructor.
   *
   * @param cId Id of the Country.
   * @param cName Name of the Country.
   */
  public Country(final String cId, final String cName) {

    this.countryId = cId;
    this.countryName = cName;
  }

  /**
   * Getter for the countryId value.
   *
   * @return The countryId value.
   */
  public String getCountryId() {

    return countryId;
  }

  /**
   * Setter for the countryId value.
   *
   * @param cId Id of the Country.
   */
  public void setCountryId(final String cId) {

    this.countryId = cId;
  }

  /**
   * Getter for the countryName value.
   *
   * @return The countryName value.
   */
  public String getCountryName() {

    return countryName;
  }

  /**
   * Setter for the countryName value.
   *
   * @param name Name of the Country.
   */
  public void setCountryName(final String name) {

    this.countryName = name;
  }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        Country country = (Country) o;
        return this.getCountryId().equals(country.getCountryId()) && this.getCountryName().equals(country.getCountryName());
    }

    @Override public int hashCode(){
        final int prime = 31;
        return prime * (countryId.hashCode() + countryName.hashCode());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

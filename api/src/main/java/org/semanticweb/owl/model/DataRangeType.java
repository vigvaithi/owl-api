package org.semanticweb.owl.model;/*
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Matthew Horridge<br> The University of Manchester<br> Information Management Group<br>
 * Date: 21-Nov-2008
 */
public enum DataRangeType {

    DATATYPE("Datatype"),

    DATA_ONE_OF("DataOneOf"),

    DATATYPE_RESTRICTION("DatatypeRestriction"),

    DATA_COMPLEMENT_OF("DataComplementOf"),

    DATA_UNION_OF("DataUnionOf"),

    DATA_INTERSECTION_OF("DataIntersectionOf");


    private String name;

    DataRangeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

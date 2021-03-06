/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.manchester.cs.owl.owlapi;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.semanticweb.owlapi.model.OWLAnnotationValueVisitor;
import org.semanticweb.owlapi.model.OWLAnnotationValueVisitorEx;
import org.semanticweb.owlapi.model.OWLDataVisitor;
import org.semanticweb.owlapi.model.OWLDataVisitorEx;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/** Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 26-Oct-2006<br>
 * <br> */
public class OWLLiteralImplNoCompression extends OWLObjectImpl implements OWLLiteral {
    private static final long serialVersionUID = 30402L;
    static final String utf_8 = "UTF-8";
    private static final OWLDatatype RDF_PLAIN_LITERAL = OWL2DatatypeImpl
            .getDatatype(OWL2Datatype.RDF_PLAIN_LITERAL);
    private final byte[] literal;
    private final OWLDatatype datatype;
    private final String lang;
    private final int hashcode;

    /** @param literal
     *            the lexical form
     * @param lang
     *            the language; can be null or an empty string, in which case
     *            datatype can be any datatype but not null
     * @param datatype
     *            the datatype; if lang is null or the empty string, it can be
     *            null or it MUST be RDFPlainLiteral */
    public OWLLiteralImplNoCompression(String literal, String lang, OWLDatatype datatype) {
        this(getBytes(literal), lang, datatype);
    }

    /** @param bytes
     * @param lang
     * @param datatype */
    public OWLLiteralImplNoCompression(byte[] bytes, String lang, OWLDatatype datatype) {
        super();
        literal = new byte[bytes.length];
        System.arraycopy(bytes, 0, literal, 0, bytes.length);
        OWLDatatype rdfplainlit = RDF_PLAIN_LITERAL;
        if (lang == null || lang.length() == 0) {
            this.lang = "";
            if (datatype == null) {
                this.datatype = rdfplainlit;
            } else {
                this.datatype = datatype;
            }
        } else {
            if (datatype != null && !datatype.isRDFPlainLiteral()) {
                // ERROR: attempting to build a literal with a language tag and
                // type different from plain literal
                throw new OWLRuntimeException("Error: cannot build a literal with type: "
                        + datatype.getIRI() + " and language: " + lang);
            }
            this.lang = lang;
            this.datatype = rdfplainlit;
        }
        hashcode = getHashCode();
    }

    private static byte[] getBytes(String literal) {
        try {
            return literal.getBytes(utf_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported UTF 8 encoding: broken JVM", e);
        }
    }

    @Override
    public String getLiteral() {
        try {
            return new String(literal, utf_8);
        } catch (UnsupportedEncodingException e) {
            throw new OWLRuntimeException("Unsupported UTF 8 encoding: broken JVM", e);
        }
    }

    @Override
    public boolean isRDFPlainLiteral() {
        return datatype.equals(getOWLDataFactory().getRDFPlainLiteral());
    }

    @Override
    public boolean hasLang() {
        return !lang.equals("");
    }

    @Override
    public boolean isInteger() {
        return datatype.equals(getOWLDataFactory().getIntegerOWLDatatype());
    }

    @Override
    public int parseInteger() throws NumberFormatException {
        return Integer.parseInt(getLiteral());
    }

    @Override
    public boolean isBoolean() {
        return datatype.equals(getOWLDataFactory().getBooleanOWLDatatype());
    }

    @Override
    public boolean parseBoolean() throws NumberFormatException {
        final String literal2 = getLiteral();
        if (literal2.equals("0")) {
            return false;
        }
        if (literal2.equals("1")) {
            return true;
        }
        if (literal2.equals("true")) {
            return true;
        }
        if (literal2.equals("false")) {
            return false;
        }
        return Boolean.parseBoolean(literal2);
    }

    @Override
    public boolean isDouble() {
        return datatype.equals(getOWLDataFactory().getDoubleOWLDatatype());
    }

    @Override
    public double parseDouble() throws NumberFormatException {
        return Double.parseDouble(getLiteral());
    }

    @Override
    public boolean isFloat() {
        return datatype.equals(getOWLDataFactory().getFloatOWLDatatype());
    }

    @Override
    public float parseFloat() throws NumberFormatException {
        String literal2 = getLiteral();
        if ("inf".equalsIgnoreCase(literal2)) {
            return Float.POSITIVE_INFINITY;
        }
        if ("-inf".equalsIgnoreCase(literal2)) {
            return Float.NEGATIVE_INFINITY;
        }
        return Float.parseFloat(literal2);
    }

    @Override
    public String getLang() {
        return lang;
    }

    @Override
    public boolean hasLang(String l) {
        // XXX this was missing null checks: a null lang is still valid in the
        // factory, where it becomes a ""
        if (l == null && lang == null) {
            return true;
        }
        if (l == null) {
            l = "";
        }
        return lang != null && lang.equalsIgnoreCase(l.trim());
    }

    @Override
    public OWLDatatype getDatatype() {
        return datatype;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    private int getHashCode() {
        int hashCode = 277;
        hashCode = hashCode * 37 + getDatatype().hashCode();
        hashCode = hashCode * 37;
        try {
            if (isInteger()) {
                hashCode += parseInteger() * 65536;
            } else if (isDouble()) {
                hashCode += (int) parseDouble() * 65536;
            } else if (isFloat()) {
                hashCode += (int) parseFloat() * 65536;
            } else if (isBoolean()) {
                hashCode += parseBoolean() ? 65536 : 0;
            } else {
                hashCode += getLiteral().hashCode() * 65536;
            }
        } catch (NumberFormatException e) {
            // it is possible that a literal does not have a value that's valid
            // for its datatype; not very useful for a consistent ontology but
            // some W3C reasoner tests use them
            hashCode += getLiteral().hashCode() * 65536;
        }
        if (hasLang()) {
            hashCode = hashCode * 37 + getLang().hashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof OWLLiteral)) {
                return false;
            }
            OWLLiteral other = (OWLLiteral) obj;
            if (other instanceof OWLLiteralImplNoCompression) {
                return Arrays.equals(literal,
                        ((OWLLiteralImplNoCompression) other).literal)
                        && datatype.equals(other.getDatatype())
                        && lang.equals(other.getLang());
            }
            return getLiteral().equals(other.getLiteral())
                    && datatype.equals(other.getDatatype())
                    && lang.equals(other.getLang());
        }
        return false;
    }

    @Override
    public void accept(OWLDataVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLDataVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(OWLAnnotationValueVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLAnnotationValueVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected int compareObjectOfSameType(OWLObject object) {
        OWLLiteral other = (OWLLiteral) object;
        int diff = getLiteral().compareTo(other.getLiteral());
        if (diff != 0) {
            return diff;
        }
        diff = datatype.compareTo(other.getDatatype());
        if (diff != 0) {
            return diff;
        }
        return lang.compareTo(other.getLang());
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
}

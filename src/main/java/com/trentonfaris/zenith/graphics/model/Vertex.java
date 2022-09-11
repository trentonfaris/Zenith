package com.trentonfaris.zenith.graphics.model;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.model.attribute.Attribute;
import com.trentonfaris.zenith.utility.Copyable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A {@link Vertex} stores data associated with a {@link Mesh}.
 *
 * @author Trenton Faris
 */
public final class Vertex implements Copyable {
    private final List<Attribute> attributes;

    public Vertex(List<Attribute> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            String errorMsg = "Cannot create a Vertex from a null or empty list of attributes.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.attributes = attributes;
    }

    @Override
    public Vertex copy() {
        List<Attribute> attributesCopy = new ArrayList<>();
        for (Attribute attribute : attributes) {
            attributesCopy.add(attribute.copy());
        }

        return new Vertex(attributesCopy);
    }

    public List<Attribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(attributes, vertex.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }
}

package com.trentonfaris.zenith.graphics.model;

import com.trentonfaris.zenith.Zenith;
import com.trentonfaris.zenith.graphics.material.Material;
import com.trentonfaris.zenith.graphics.model.attribute.*;
import com.trentonfaris.zenith.utility.Copyable;
import com.trentonfaris.zenith.utility.Disposable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link Mesh} is a list of vertices with a list of indices that correspond
 * to those vertices. It can be drawn as points, lines, or triangles. It may
 * also have a {@link Material} which defines how it is drawn.
 *
 * @author Trenton Faris
 */
public final class Mesh implements Copyable, Disposable {
    /**
     * The list of vertices in this {@link Mesh}.
     */
    private final List<Vertex> vertices;

    /**
     * The list of indices in this {@link Mesh}.
     */
    private final List<Integer> indices;

    /**
     * The {@link PrimitiveType} of this {@link Mesh}.
     */
    private PrimitiveType primitiveType;

    /**
     * The {@link Material} used to draw this {@link Mesh}.
     */
    private Material material;

    /**
     * The vertex array object used to draw this {@link Mesh}.
     */
    private final int vao;

    /**
     * The vertex buffer object of the {@link #vao}.
     */
    private final int vbo;

    /**
     * The index buffer object of the {@link #vao}.
     */
    private final int ibo;

    /**
     * Creates and builds a new {@link Mesh} with the specified vertices, indices,
     * {@link PrimitiveType}, and {@link Material}.
     *
     * @param vertices The list of vertices of this {@link Mesh}
     * @param indices  The list of indices of this {@link Mesh}
     * @param material The {@link Material} used to draw this {@link Mesh}
     */
    public Mesh(List<Vertex> vertices, List<Integer> indices, PrimitiveType primitiveType, Material material) {
        if (vertices == null || vertices.isEmpty()) {
            String errorMsg = "Cannot create a Mesh from a null or empty list of vertices.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        // Validate that all vertices have the same attributes.
        List<Attribute> attributes = vertices.get(0).getAttributes();
        for (int i = 1; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);

            List<Attribute> vertexAttributes = vertex.getAttributes();
            if (attributes.size() != vertexAttributes.size()) {
                String errorMsg = "Cannot create a Mesh from a list of vertices with different numbers of attributes. Create a separate Mesh for each set of vertices with different numbers of attributes.";
                Zenith.getLogger().error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            for (int j = 0; j < vertexAttributes.size(); j++) {
                Attribute attribute = attributes.get(j);
                Attribute vertexAttribute = vertexAttributes.get(j);

                if (!((attribute instanceof FloatAttribute && vertexAttribute instanceof FloatAttribute) ||
                        (attribute instanceof Vec2Attribute && vertexAttribute instanceof Vec2Attribute) ||
                        (attribute instanceof Vec3Attribute && vertexAttribute instanceof Vec3Attribute) ||
                        (attribute instanceof Vec4Attribute && vertexAttribute instanceof Vec4Attribute))) {
                    String errorMsg = "Cannot create a Mesh from a list of vertices with different attribute types. Create a separate Mesh for each set of vertices with different attribute data types.";
                    Zenith.getLogger().error(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        }

        if (indices == null || indices.isEmpty()) {
            String errorMsg = "Cannot create a Mesh from a null list of indices.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (primitiveType == null) {
            String errorMsg = "Cannot create a Mesh from a null primitiveType.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.vertices = vertices;
        this.indices = indices;
        this.primitiveType = primitiveType;
        this.material = material;

        // Generate the vao, vbo, and ibo
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pVao = stack.mallocInt(1);
            IntBuffer pVbo = stack.mallocInt(1);
            IntBuffer pIbo = stack.mallocInt(1);

            GL30.glGenVertexArrays(pVao);
            GL15.glGenBuffers(pVbo);
            GL15.glGenBuffers(pIbo);

            this.vao = pVao.get();
            this.vbo = pVbo.get();
            this.ibo = pIbo.get();
        }

        update();
    }

    public void update() {
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        List<Integer> attributeSizes = vertices.get(0).getAttributes().stream().mapToInt(Attribute::getSize).boxed()
                .collect(Collectors.toList());

        int vertexSize = attributeSizes.stream().mapToInt(Integer::intValue).sum();

        // Populate the vbo with vertices data
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer pVertices = stack.mallocFloat(vertices.size() * vertexSize);

            FloatBuffer pVector2f = stack.mallocFloat(2);
            FloatBuffer pVector3f = stack.mallocFloat(3);
            FloatBuffer pVector4f = stack.mallocFloat(4);

            for (Vertex vertex : vertices) {
                for (Attribute attribute : vertex.getAttributes()) {
                    if (attribute instanceof FloatAttribute floatAttribute) {
                        pVertices.put(floatAttribute.value);
                    } else if (attribute instanceof Vec2Attribute vec2Attribute) {
                        pVertices.put((vec2Attribute.value).get(pVector2f));
                        pVector2f.clear();
                    } else if (attribute instanceof Vec3Attribute vec3Attribute) {
                        pVertices.put((vec3Attribute.value).get(pVector3f));
                        pVector3f.clear();
                    } else if (attribute instanceof Vec4Attribute vec4Attribute) {
                        pVertices.put((vec4Attribute.value).get(pVector4f));
                        pVector4f.clear();
                    }
                }
            }

            pVertices.flip();

            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, pVertices, GL15.GL_STATIC_DRAW);
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        // Populate the ibo with the indices data
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pIndices = stack.mallocInt(indices.size());

            int[] indicesArr = indices.stream().mapToInt(Integer::intValue).toArray();
            pIndices.put(indicesArr).flip();

            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, pIndices, GL15.GL_STATIC_DRAW);
        }

        int offset = 0;
        for (int i = 0; i < attributeSizes.size(); i++) {
            GL20.glEnableVertexAttribArray(i);
            GL20.glVertexAttribPointer(i, attributeSizes.get(i), GL11.GL_FLOAT, false, vertexSize * Float.BYTES,
                    (long) offset * Float.BYTES);

            offset += attributeSizes.get(i);
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public Mesh copy() {
        List<Vertex> verticesCopy = new ArrayList<>();
        for (Vertex vertex : this.vertices) {
            verticesCopy.add(vertex.copy());
        }

        return new Mesh(verticesCopy, new ArrayList<>(this.indices), PrimitiveType.valueOf(primitiveType.name()),
                material.copy());
    }

    public void dispose() {
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
    }

    /**
     * Draws this {@link Mesh}.
     */
    public void draw() {
        if (material != null) {
            material.preDraw();
        }

        GL30.glBindVertexArray(vao);
        GL11.glDrawElements(primitiveType.getValue(), indices.size(), GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
    }

    /**
     * Gets the list of the {@link #vertices}. If the list of vertices is modified,
     * it is recommended to {@link #update()} the {@link Mesh}.
     *
     * @return The list of {@link #vertices}.
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Gets the {@link #indices}. If the list of indices is modified, it is
     * recommended to {@link #update()} the {@link Mesh}.
     *
     * @return The list {@link #indices}.
     */
    public List<Integer> getIndices() {
        return indices;
    }

    /**
     * Gets the {@link #primitiveType}.
     *
     * @return The {@link #primitiveType} value.
     */
    public PrimitiveType getPrimitiveType() {
        return primitiveType;
    }

    /**
     * Sets the {@link #primitiveType}.
     *
     * @param primitiveType The target {@link PrimitiveType}
     */
    public void setPrimitiveType(PrimitiveType primitiveType) {
        if (primitiveType == null) {
            String errorMsg = "Cannot set primitiveType to null.";
            Zenith.getLogger().error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        this.primitiveType = primitiveType;
    }

    /**
     * Gets the {@link #material}.
     *
     * @return The {@link #material} value.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the {@link #material}.
     *
     * @param material The target {@link Material}
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ibo;
        result = prime * result + ((indices == null) ? 0 : indices.hashCode());
        result = prime * result + ((material == null) ? 0 : material.hashCode());
        result = prime * result + ((primitiveType == null) ? 0 : primitiveType.hashCode());
        result = prime * result + vao;
        result = prime * result + vbo;
        result = prime * result + ((vertices == null) ? 0 : vertices.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mesh other = (Mesh) obj;
        if (ibo != other.ibo)
            return false;
        if (indices == null) {
            if (other.indices != null)
                return false;
        } else if (!indices.equals(other.indices))
            return false;
        if (material == null) {
            if (other.material != null)
                return false;
        } else if (!material.equals(other.material))
            return false;
        if (primitiveType != other.primitiveType)
            return false;
        if (vao != other.vao)
            return false;
        if (vbo != other.vbo)
            return false;
        if (vertices == null) {
            return other.vertices == null;
        } else return vertices.equals(other.vertices);
    }
}

package checkers.flow.cfg.node;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import checkers.flow.util.HashCodeUtils;
import checkers.util.InternalUtils;

import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;

/**
 * A node for new object creation
 * 
 * <pre>
 *   <em>new constructor(arg1, arg2, ...)</em>
 * </pre>
 * 
 * @author Stefan Heule
 * @author Charlie Garrett
 * 
 */
public class ObjectCreationNode extends Node {

    protected NewClassTree tree;
    protected Node constructor;
    protected List<Node> arguments;

    public ObjectCreationNode(NewClassTree tree,
            Node constructor,
            List<Node> arguments) {
        this.tree = tree;
        this.type = InternalUtils.typeOf(tree);
        this.constructor = constructor;
        this.arguments = arguments;
    }

    public Node getConstructor() {
        return constructor;
    }

    public List<Node> getArguments() {
        return arguments;
    }

    public Node getArgument(int i) {
        return arguments.get(i);
    }

    @Override
    public Tree getTree() {
        return tree;
    }

    @Override
    public <R, P> R accept(NodeVisitor<R, P> visitor, P p) {
        return visitor.visitObjectCreation(this, p);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("new " + constructor + "(");
        boolean needComma = false;
        for (Node arg : arguments) {
            if (needComma) {
                sb.append(", ");
            }
            sb.append(arg);
            needComma = true;
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ObjectCreationNode)) {
            return false;
        }
        ObjectCreationNode other = (ObjectCreationNode) obj;
        if (constructor == null && other.getConstructor() != null) {
            return false;
        }

        return getConstructor().equals(other.getConstructor())
                && getArguments().equals(other.getArguments());
    }

    @Override
    public int hashCode() {
        int hash = HashCodeUtils.hash(constructor);
        for (Node arg : arguments) {
            hash = HashCodeUtils.hash(hash, arg.hashCode());
        }
        return hash;
    }

    @Override
    public Collection<Node> getOperands() {
        LinkedList<Node> list = new LinkedList<Node>();
        list.addAll(arguments);
        return list;
    }
}

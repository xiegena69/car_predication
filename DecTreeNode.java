import java.util.ArrayList;
import java.util.List;

public class DecTreeNode {
	String quality;			// which class it belongs to.
	int attribute;			// classfied by which attribute.
	String parentAttributeValue;
	boolean terminal;
	boolean pure;
	List<DecTreeNode> children;

	DecTreeNode(String _quality, int _attribute, String _parentAttributeValue,boolean _terminal,boolean _pure) {
		quality = _quality;
		attribute = _attribute;
		parentAttributeValue = _parentAttributeValue;
		terminal = _terminal;
		pure = _pure;
		if (_terminal) {
			children = null;
		} else {
			children = new ArrayList<DecTreeNode>();
		}
	}

	/**
	 * Add child to the node.
	 */
	public void addChild(DecTreeNode child) {
		if (children != null) {
			children.add(child);
		}
	}
}
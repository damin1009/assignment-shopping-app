import java.util.*;

// �θ� ���� �ڽ� ��带 �� ���� return�ϱ� ���� �ʿ��� Ŭ����
class NodePair {
	private Node parentNode;
	private Node childNode;
	public NodePair(Node parentNode, Node childNode) {
		this.parentNode = parentNode;
		this.childNode = childNode;
	}
	public Node getParent() {
		return parentNode;
	}
	public Node getChild() {
		return childNode;
	}
}

class BST {
	public Node T;
	
	public BST() {
		T = null;
	}
	// �� ��带 ����
	public Node getNode() {
		return new Node();
	}
	
	// ����Լ��� ������ inorder
	public void inorderBST(Node node) {
		if (node == null)
			return;
		inorderBST(node.left);
		System.out.print(node.key + " ");
		inorderBST(node.right);
	}
	
	// ���ο� Ű ����
	public void insertBST(int newKey) {
		Node q = null, p = T;
		while (p != null) {
			if (newKey == p.key)
				return;
			q = p;
			if (newKey < p.key)
				p = p.left;
			else
				p = p.right;
		}
		Node newNode = getNode();
		newNode.key = newKey;
		newNode.right = null;
		newNode.left = null;
		if (T == null)
			T = newNode;
		else if (newKey < q.key)
			q.left = newNode;
		else
			q.right = newNode;
		return;
	}
	
	// Ű�� �ش��ϴ� ��带 ã�Ƽ� �� �θ�� �Բ� ��ȯ
	public NodePair searchParentBST(Node T, int searchKey) {
		Node q = null, p = T;
		while (p != null) {
			if (searchKey == p.key)
				return new NodePair(q, p);
			q = p;
			if (searchKey < p.key)
				p = p.left;
			else
				p = p.right;
		}
		return null;
	}
	
	// �ش� ��� �Ʒ��� �ִ� ���� ū ��� Ž��
	public Node maxNode(Node T) {
		Node p = T;
		if (p == null)
			return null;  // �� ���� ����� �� ��. p�� �θ��� ������ 2�� ��Ȳ���� ����Ǵ� �޼����̹Ƿ�.
		while (p.right != null) {
			p = p.right;
		}
		return p;
	}
	
	// �ش� ��� �Ʒ��� �ִ� ���� ���� ��� Ž��
	public Node minNode(Node T) {
		Node p = T;
		if (p == null)
			return null;  // �� ���� ����� �� ��. p�� �θ��� ������ 2�� ��Ȳ���� ����Ǵ� �޼����̹Ƿ�.
		while (p.left != null) {
			p = p.left;
		}
		return p;
	}
	
	// ����Լ��� ���� ����.
	public int height(Node T) {
		if (T == null)
			return 0;
		int leftHeight = height(T.left);
		int rightHeight = height(T.right);
		// �ϴ� ���� �����Ѵٸ� ���� 1�� Ȯ���̸�, ������ ���ٸ� ������ ���̸�ŭ ���ϱ�, �������� ���ٸ� �������� ���̸�ŭ ���ϱ�.
		return 1 + ((leftHeight > rightHeight) ? leftHeight : rightHeight);
	}
	
	// number of nodes, ����� ���� ����. ����Լ��� ����.
	public int noNodes(Node T) {
		if (T == null)
			return 0;
		// 1(��) + ������ ��� �� + �������� ��� ��
		return 1 + noNodes(T.left) + noNodes(T.right);
	}
	
	// Ű ����
	public void deleteBST(Node startNode, int deleteKey) {
		// ������ ���� �� ����� �θ� ��������
		NodePair nodePair = searchParentBST(startNode, deleteKey);
		if (nodePair == null)  // Ű�� �ش��ϴ� ��尡 ����.
			return;
		Node p = nodePair.getChild();
		Node q = nodePair.getParent();
		if (p == null)
			return;
		if (q == null) {
			// ������ ��尡 ��Ʈ ����� ����� ��. �θ� ã�� ��������.
			// �ٵ� �ƴ� ���� ����. �� �� �� Ȯ��.
			if (p != T) {
				// ��Ʈ�������� �ٽ� deleteBST ����. �̷��� ������ ��Ʈ�ų�, q�� null�� �ƴϰų�.
				// �������� �ٽ� �� ���� ����. ���ѹݺ����� ���� ��.
				deleteBST(T, deleteKey);
				return;
			}
		}
		
		// ������ ��尡 ��Ʈ ����� ��쿡�� �Ȱ��� ������ 0�� ��, 1�� ��, 2�� ���� �� ����ؾ� ��.
		// �ڵ��� ȿ������ ���� ��Ʈ ��带 �����ϴ� ��쵵 �Ʒ� �ڵ忡 ����.
		if (p.left == null && p.right == null) {  // ������ ����� ������ 0
			if (q == null)
				T = null;  // ��Ʈ ��带 �����ϴµ� �ڽĵ� ������ ������ null.
			else if (q.left == p)
				q.left = null;
			else
				q.right = null;
		} else if (p.left == null || p.right == null) {  // ������ ����� ������ 1
			Node childNode = (p.left != null) ? p.left : p.right;
			if (q == null)
				T = childNode;  // ������ �ڽ��� ��Ʈ ���� �����.
			else if (q.left == p)
				q.left = childNode;
			else
				q.right = childNode;
		} else {  // ������ ����� ������ 2
			Node r;
			String flag = "LEFT";
			int leftHeight = height(p.left);
			int rightHeight = height(p.right);
			
			// ���̰� ���� ������ ����
			if (leftHeight > rightHeight) {
				r = maxNode(p.left);
				flag = "LEFT";
			} else if (leftHeight < rightHeight) {
				r = minNode(p.right);
				flag = "RIGHT";
			} else {  // ���� ���̰� ���ٸ�, ��尡 ���� ������ ����
				if (noNodes(p.left) >= noNodes(p.right)) {
					r = maxNode(p.left);
					flag = "LEFT";
				} else {
					r = minNode(p.right);
					flag = "RIGHT";
				}
			}
			
			
			if (flag == "LEFT")
				deleteBST(p, r.key);
			else
				deleteBST(p, r.key);
			// ������ ������ flag�� ���� p.left, p.right�� ����ؾ� ������,
			// c�� �ƴ϶� �ڹٷ� �����߱� ������
			// ��带 �����Ϸ��� �� �θ��� left �Ǵ� right�� ���� �����ؾ� �ϹǷ�
			// p.left�� p.right�� �ƴ϶� �� �ܰ� ���� p �ܰ迡�� deleteBST�� �����ϵ��� �Ͽ���.
			// ���� ����� p�� �������� �ڽ��� r�� ������ �� ������ ���� �� ����.
			// ���ʿ� searchParentBST�� �̿��� �θ�� �ڽ��� ���ÿ� �ҷ����µ�,
			// �̷��� �ϸ� p�� �������� �ڽ��� r�� �� searchParentBST���� parent�� ã�� ���ϰ� null�� ����.
			// �׷��� ��ġ ���ַ��� ��� r�� ��Ʈ ����� �� �Ǿ����. �׷��� �� ��.
			
			// ���� �� �ܰ� ���� p �ܰ迡�� deleteBST�� �̿��� ���̹Ƿ�, p�� Ű ���� ������Ʈ �ϴ� ���� �������� �̷�.
			p.key = r.key;
		}
	}

}

class Node {
	public int key;
	public Node left;
	public Node right;
	
	public Node() {
		
	}
}

public class BST20191635 {
	// inorder�� ������ �� ���� �ȳ����� ����ϰ�, ���� �� ���൵ �ϱ� ���� inorder ���� �Լ�
	public static void inorder(BST bst) {
		System.out.print("Inorder BST: ");
		bst.inorderBST(bst.T);
		System.out.print("\n");
	}
	
	public static void main(String[] args) {
		System.out.println("20191635 �̰��� �Դϴ�.");
		BST bst = new BST();
		int[] nums = new int[] {25, 500, 33, 49, 17, 403, 29, 105, 39, 66, 305, 44, 19, 441, 390, 12, 81, 50, 100, 999};
		
		// ���� 1
		for (int i = 0; i < nums.length; i++) {
			bst.insertBST(nums[i]); inorder(bst);
		}
		
		// ���� 1
		for (int i = 0; i < nums.length; i++) {
			bst.deleteBST(bst.T, nums[i]); inorder(bst);
		}
		
		// ���� 2
		for (int i = 0; i < nums.length; i++) {
			bst.insertBST(nums[i]); inorder(bst);
		}
		
		// ���� 2
		for (int i = nums.length - 1; i >= 0; i--) {
			bst.deleteBST(bst.T, nums[i]); inorder(bst);
		}
	}
}

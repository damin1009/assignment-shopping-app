import java.util.*;

// 부모 노드와 자식 노드를 한 번에 return하기 위해 필요한 클래스
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
	// 새 노드를 생성
	public Node getNode() {
		return new Node();
	}
	
	// 재귀함수로 구현한 inorder
	public void inorderBST(Node node) {
		if (node == null)
			return;
		inorderBST(node.left);
		System.out.print(node.key + " ");
		inorderBST(node.right);
	}
	
	// 새로운 키 삽입
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
	
	// 키에 해당하는 노드를 찾아서 그 부모와 함께 반환
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
	
	// 해당 노드 아래에 있는 가장 큰 노드 탐색
	public Node maxNode(Node T) {
		Node p = T;
		if (p == null)
			return null;  // 이 경우는 생기면 안 됨. p의 부모의 차수가 2인 상황에서 실행되는 메서드이므로.
		while (p.right != null) {
			p = p.right;
		}
		return p;
	}
	
	// 해당 노드 아래에 있는 가장 작은 노드 탐색
	public Node minNode(Node T) {
		Node p = T;
		if (p == null)
			return null;  // 이 경우는 생기면 안 됨. p의 부모의 차수가 2인 상황에서 실행되는 메서드이므로.
		while (p.left != null) {
			p = p.left;
		}
		return p;
	}
	
	// 재귀함수로 높이 측정.
	public int height(Node T) {
		if (T == null)
			return 0;
		int leftHeight = height(T.left);
		int rightHeight = height(T.right);
		// 일단 내가 존재한다면 높이 1은 확정이며, 왼쪽이 높다면 왼쪽의 높이만큼 더하기, 오른쪽이 높다면 오른쪽의 높이만큼 더하기.
		return 1 + ((leftHeight > rightHeight) ? leftHeight : rightHeight);
	}
	
	// number of nodes, 노드의 개수 세기. 재귀함수로 구현.
	public int noNodes(Node T) {
		if (T == null)
			return 0;
		// 1(나) + 왼쪽의 노드 수 + 오른쪽의 노드 수
		return 1 + noNodes(T.left) + noNodes(T.right);
	}
	
	// 키 삭제
	public void deleteBST(Node startNode, int deleteKey) {
		// 삭제할 노드와 그 노드의 부모 가져오기
		NodePair nodePair = searchParentBST(startNode, deleteKey);
		if (nodePair == null)  // 키에 해당하는 노드가 없음.
			return;
		Node p = nodePair.getChild();
		Node q = nodePair.getParent();
		if (p == null)
			return;
		if (q == null) {
			// 삭제할 노드가 루트 노드인 경우일 것. 부모를 찾지 못했으니.
			// 근데 아닐 수도 있음. 한 번 더 확인.
			if (p != T) {
				// 루트에서부터 다시 deleteBST 시작. 이러면 무조건 루트거나, q가 null이 아니거나.
				// 이쪽으로 다시 올 일은 없음. 무한반복되지 않을 것.
				deleteBST(T, deleteKey);
				return;
			}
		}
		
		// 삭제할 노드가 루트 노드인 경우에도 똑같이 차수가 0일 때, 1일 때, 2일 때를 다 고려해야 함.
		// 코드의 효율성을 위해 루트 노드를 삭제하는 경우도 아래 코드에 포함.
		if (p.left == null && p.right == null) {  // 삭제할 노드의 차수가 0
			if (q == null)
				T = null;  // 루트 노드를 삭제하는데 자식도 없으면 완전히 null.
			else if (q.left == p)
				q.left = null;
			else
				q.right = null;
		} else if (p.left == null || p.right == null) {  // 삭제할 노드의 차수가 1
			Node childNode = (p.left != null) ? p.left : p.right;
			if (q == null)
				T = childNode;  // 유일한 자식을 루트 노드로 만든다.
			else if (q.left == p)
				q.left = childNode;
			else
				q.right = childNode;
		} else {  // 삭제할 노드의 차수가 2
			Node r;
			String flag = "LEFT";
			int leftHeight = height(p.left);
			int rightHeight = height(p.right);
			
			// 높이가 높은 곳에서 제거
			if (leftHeight > rightHeight) {
				r = maxNode(p.left);
				flag = "LEFT";
			} else if (leftHeight < rightHeight) {
				r = minNode(p.right);
				flag = "RIGHT";
			} else {  // 둘의 높이가 같다면, 노드가 많은 곳에서 제거
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
			// 원래는 위에서 flag에 따라 p.left, p.right를 사용해야 하지만,
			// c언어가 아니라 자바로 구현했기 때문에
			// 노드를 삭제하려면 그 부모의 left 또는 right를 직접 수정해야 하므로
			// p.left나 p.right가 아니라 한 단계 높은 p 단계에서 deleteBST를 시작하도록 하였음.
			// 기존 방식은 p의 직접적인 자식이 r과 동일할 때 문제가 생길 수 있음.
			// 애초에 searchParentBST를 이용해 부모와 자식을 동시에 불러오는데,
			// 이렇게 하면 p의 직접적인 자식이 r일 때 searchParentBST에서 parent를 찾지 못하고 null로 배출.
			// 그러면 마치 없애려는 노드 r이 루트 노드인 양 되어버림. 그러면 안 됨.
			
			// 또한 한 단계 높은 p 단계에서 deleteBST를 이용할 것이므로, p의 키 값을 업데이트 하는 것을 나중으로 미룸.
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
	// inorder을 시작할 때 시작 안내문을 출력하고, 끝날 때 개행도 하기 위한 inorder 시작 함수
	public static void inorder(BST bst) {
		System.out.print("Inorder BST: ");
		bst.inorderBST(bst.T);
		System.out.print("\n");
	}
	
	public static void main(String[] args) {
		System.out.println("20191635 이강민 입니다.");
		BST bst = new BST();
		int[] nums = new int[] {25, 500, 33, 49, 17, 403, 29, 105, 39, 66, 305, 44, 19, 441, 390, 12, 81, 50, 100, 999};
		
		// 삽입 1
		for (int i = 0; i < nums.length; i++) {
			bst.insertBST(nums[i]); inorder(bst);
		}
		
		// 삭제 1
		for (int i = 0; i < nums.length; i++) {
			bst.deleteBST(bst.T, nums[i]); inorder(bst);
		}
		
		// 삽입 2
		for (int i = 0; i < nums.length; i++) {
			bst.insertBST(nums[i]); inorder(bst);
		}
		
		// 삭제 2
		for (int i = nums.length - 1; i >= 0; i--) {
			bst.deleteBST(bst.T, nums[i]); inorder(bst);
		}
	}
}

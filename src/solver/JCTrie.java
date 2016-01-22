package solver;
//Copied from AlgorithmsII TrieST class

public class JCTrie {
	public JCTrie(String[] dictionary) {
		String top = dictionary[dictionary.length / 2];
		put(top, points(top));
		for (int i = 0; i < dictionary.length; i++) {
			put(dictionary[i], points(dictionary[i]));
		}
	}

    private static final int R = 26;        // extended ASCII

    private Node root = new Node();
    
    private static class Node {
        private Integer val;
        private Node[] next = new Node[R];
    }

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean contains(String key) {
    	return get(key) != null;
    }
    
    public boolean containsPrefix(String key) {
    	return isPrefix(root, key, 0);
    }
    
    public Integer get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = (char) (key.charAt(d) - 'A');
        return get(x.next[c], key, d+1);
    }

    private boolean isPrefix(Node x, String key, int d) {
        if (x == null) return false;
        if (d == key.length()) {
        	return true;
        }
        char c = (char) (key.charAt(d) - 'A');
        return isPrefix(x.next[c], key, d+1);
    }
	public boolean collect(String prefix) {
		Node x = root;
		if (x == null)
			return false;
		for (int c = 0; c < prefix.length(); c++) {
			if (x.next[prefix.charAt(c) - 'A'] != null)
				x = x.next[prefix.charAt(c) - 'A'];
			else
				return false;
		}
		return true;
	}
   /****************************************************
    * Insert key-value pair into the symbol table.
    ****************************************************/
    public void put(String key, int val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = (char) (key.charAt(d) - 'A');
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }
    
	public int points(String s) {
		if (s.length() < 3) {
			return 0;
		}
		if (s.length() < 5) {
			return 1;
		}
		if (s.length() == 5) {
			return 2;
		}
		if (s.length() == 6) {
			return 3;
		}
		if (s.length() == 7) {
			return 5;
		}
		return 11;
	}
}

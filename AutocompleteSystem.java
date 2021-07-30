import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

class AutocompleteSystem {
	class TrieNode {
		Map<Character, TrieNode> children;
		Map<String, Integer> count;

		TrieNode() {
			this.children = new HashMap<>();
			this.count = new HashMap<>();
		}
	}

	TrieNode root;
	String prefix;

	public AutocompleteSystem(String[] sentences, int[] times) {

		prefix = "";
		root = new TrieNode();
		for (int i = 0; i < sentences.length; i++) {
			addToTrie(sentences[i], times[i]);
		}
	}

	public void addToTrie(String sentence, int times) {
		TrieNode node = root;
		for (char c : sentence.toCharArray()) {
			if (!node.children.containsKey(c))
				node.children.put(c, new TrieNode());

			node = node.children.get(c);
			node.count.put(sentence, node.count.getOrDefault(sentence, 0) + times);
		}
	}

	public List<String> input(char c) {
		List<String> retList = new ArrayList<>();

		if (c == '#') {
			addToTrie(prefix, 1);
			prefix = "";
			return retList;
		}

		prefix = prefix + c;

		TrieNode node = root;
		for (char ch : prefix.toCharArray()) {
			if (node.children.containsKey(ch)) {
				node = node.children.get(ch);
			} else
				return retList;
		}

		Queue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a,
				b) -> a.getValue() == b.getValue() ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue());
		pq.addAll(node.count.entrySet());
		int count = 3;
		while (!pq.isEmpty() && count > 0) {
			retList.add(pq.poll().getKey());
			count--;
		}
		return retList;
	}
}
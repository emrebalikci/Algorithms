package tree.bst;

import java.util.*;

/**
 * Problem 14.21: Design a data structure that implements the following methods:
 *   -- insert(s,c), which adds client s with credit c, overwriting any existing entry for s.
 *   -- remove(s), which removes client s
 *   -- lookup(s), which returns the number of credits associated with client s, or -1 if s
 *                 is not present
 *   -- addAll(C), the effect of which is to increment the number of credits for each client
 *                 currently present by C
 *   -- max(), which returns the client with the highest number of credits
 *     The insert(s,c), remove(s), and lookup(s) methods should run in time O(logn), where
 *     n is the number of clients. The remaining methods should run in time O(1).
 *  Solution: P333
 */
public class AddingCredits {
    // @include
    public static class ClientsCreditsInfo {
        private int offset = 0;
        private Map<String, Integer> clientToCredit = new HashMap<>();
        private NavigableMap<Integer, Set<String>> creditToClients = new TreeMap<>();

        public void insert(String clientID, int c) {
            remove(clientID);
            clientToCredit.put(clientID, c - offset);
            Set<String> set = creditToClients.get(c - offset);
            if (set == null) {
                set = new HashSet<>();
                creditToClients.put(c - offset, set);
            }
            set.add(clientID);
        }

        public boolean remove(String clientID) {
            Integer clientCredit = clientToCredit.get(clientID);
            if (clientCredit != null) {
                creditToClients.get(clientCredit).remove(clientID);
                if (creditToClients.get(clientCredit).isEmpty()) {
                    creditToClients.remove(clientCredit);
                }
                clientToCredit.remove(clientID);
                return true;
            }
            return false;
        }

        public int lookup(String clientID) {
            Integer clientCredit = clientToCredit.get(clientID);
            return clientCredit == null ? -1 : clientCredit + offset;
        }

        public void addAll(int C) { offset += C; }

        public String max() {
            return creditToClients.isEmpty()
                    ? ""
                    : creditToClients.lastEntry().getValue().iterator().next();
        }
    }
    // @exclude

    public static void main(String[] args) {
        ClientsCreditsInfo a = new ClientsCreditsInfo();
        assert(a.max().isEmpty());
        assert(!a.remove("foo"));
        a.insert("foo", 10);
        a.insert("foo", 1);
        a.insert("bar", 2);
        a.addAll(5);
        a.insert("widget", 3);
        a.addAll(5);
        a.insert("dothis", 4);
        assert(11 == a.lookup("foo"));
        assert(12 == a.lookup("bar"));
        assert(8 == a.lookup("widget"));
        assert(4 == a.lookup("dothis"));
        assert(a.remove("foo"));
        assert(-1 == a.lookup("foo"));
        assert(a.max().equals("bar"));
        a.insert("xyz", 13);
        assert(a.max().equals("xyz"));
        a.insert("dd", 15);
        assert(a.max().equals("dd"));
    }
}

import java.util.Iterator;
import java.util.ArrayList;

public class Iter {
	public static void main(String[] args) {
		ArrayList<Integer> ref = new ArrayList<>();
		ref.add(5);
		ref.add(8);
		ref.add(3);
		ref.add(1);
		ref.add(2);
		ref.add(6);
		WackList<Integer> wl = new WackList<>(ref);
		Iterator<Integer> it = wl.iterator();
		it.next();
		it.next();
		System.out.println(it.next());
		for (Object x : wl) {
			System.out.println(x);
		}
	}
	private static class WackList<T> implements Iterable {
		ArrayList<T> ref;
		public WackList(ArrayList<T> ref) {
			this.ref = ref;
		}
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				private int i = 0;
				public boolean hasNext() {
					if (i >= ref.size()) {
						return false;
					}
					return true;
				}
				public T next() {
					return ref.get(i++);
				}
			};
		}
	}
}

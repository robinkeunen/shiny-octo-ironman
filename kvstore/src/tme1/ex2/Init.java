package tme1.ex2;

import oracle.kv.*;

/**
 * TME avec KVStore : Init
 */
public class Init{

	private final KVStore store;

	/**
	 * Runs Init
	 */
	public static void main(String args[]) {
		try {
			Init a = new Init(args);
			a.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses command line args and opens the KVStore.
	 */
	private Init(String[] argv) {

		String storeName = "kvstore";
		String hostName = "localhost";
		String hostPort = "5000";

		final int nArgs = argv.length;
		int argc = 0;

		while (argc < nArgs) {
			final String thisArg = argv[argc++];

			if (thisArg.equals("-store")) {
				if (argc < nArgs) {
					storeName = argv[argc++];
				} else {
					usage("-store requires an argument");
				}
			} else if (thisArg.equals("-host")) {
				if (argc < nArgs) {
					hostName = argv[argc++];
				} else {
					usage("-host requires an argument");
				}
			} else if (thisArg.equals("-port")) {
				if (argc < nArgs) {
					hostPort = argv[argc++];
				} else {
					usage("-port requires an argument");
				}
			} else {
				usage("Unknown argument: " + thisArg);
			}
		}

		store = KVStoreFactory.getStore
				(new KVStoreConfig(storeName, hostName + ":" + hostPort));
	}

	private void usage(String message) {
		System.out.println("\n" + message + "\n");
		System.out.println("usage: " + getClass().getName());
		System.out.println("\t-store <instance name> (default: kvstore) " +
				"-host <host name> (default: localhost) " +
				"-port <port number> (default: 5000)");
		System.exit(1);
	}

	/**
	 * Initialisation
	 */
	void go() throws Exception {
		System.out.println("Initialisation...");

		for (int i = 0; i < 200; i++) {
			String major = "C" + i/10;
			String minor = "P" + i;
			String v = "1";
			store.put(Key.createKey(major, minor),
					  Value.createValue(v.getBytes()));
		}
		System.out.println("Fin d'initialisation");
		store.close();
	}
}

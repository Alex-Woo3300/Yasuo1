/*************************************************************************
 *  Compilation:  javac LZWmod.java
 *  Compression: java LZWmod - < code.txt > code.lzw
 *  Expansion: java LZWmod + < code.lzw > code.rec
 *  Checking if the expanded file is the same as the original one: diff code.txt code.rec
 *
 *  In the previous commands, replace code.txt with other files inside the Test Files folder. Make sure that you either 
 *  place the file in the same folder as the java file or include the path to the file in the command (e.g., "Test Files\code.txt"). 
 *  The input files must be in the lab’s root folder (i.e., not inside the Code folder) for debugging.
 *  Dependencies: BinaryStdIn.java BinaryStdOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/

public class LZWmod {

    private static final int R = 256;
    private static final int L = 4096;
    private static final int W = 12;

    public static void compress() {
        TSTmod<Integer> st = new TSTmod<Integer>();

        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);

        int code = R + 1;

        StringBuilder current = new StringBuilder();
        current.append(BinaryStdIn.readChar());

        while (!BinaryStdIn.isEmpty()) {
            char next = BinaryStdIn.readChar();

            StringBuilder temp = new StringBuilder(current);
            temp.append(next);

            if (st.contains(temp)) {
                current.append(next);
            } else {
                BinaryStdOut.write(st.get(current), W);

                if (code < L) {
                    st.put(temp, code++);
                }

                current = new StringBuilder();
                current.append(next);
            }
        }

        BinaryStdOut.write(st.get(current), W);
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    public static void expand() {
        String[] st = new String[L];
        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;

            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);

            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }
}

/* Generated By:JavaCC: Do not edit this line. GMLParser.java */
package visidia.gml;

import visidia.graph.SimpleGraph;

public class GMLParser implements GMLParserConstants {
	public static void main(String[] args) throws ParseException {
		GMLParser parser = new GMLParser(System.in);
		parser.Input().print();
	}

	final public SimpleGraph Input() throws ParseException {
		SimpleGraph graph;
		graph = this.GML();
		this.jj_consume_token(0);
		{
			if (true) {
				return graph;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public SimpleGraph GML() throws ParseException {
		GMLList list;
		list = this.List();
		{
			if (true) {
				return GMLGraphExtractor.extractGraph(list);
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public GMLList List() throws ParseException {
		GMLList list = new GMLList();
		label_1: while (true) {
			switch ((this.jj_ntk == -1) ? this.jj_ntk() : this.jj_ntk) {
			case KEY:
				;
				break;
			default:
				this.jj_la1[0] = this.jj_gen;
				break label_1;
			}
			this.Pair(list);
		}
		{
			if (true) {
				return list;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public void Pair(GMLList list) throws ParseException {
		Token token;
		Object value;
		token = this.jj_consume_token(GMLParserConstants.KEY);
		value = this.Value();
		list.add(token.image, value);
	}

	final public Object Value() throws ParseException {
		Object value;
		switch ((this.jj_ntk == -1) ? this.jj_ntk() : this.jj_ntk) {
		case INTEGER:
			value = this.IntegerValue();
			{
				if (true) {
					return value;
				}
			}
			break;
		case REAL:
			value = this.DoubleValue();
			{
				if (true) {
					return value;
				}
			}
			break;
		case STRING:
			value = this.StringValue();
			{
				if (true) {
					return value;
				}
			}
			break;
		case 18:
			this.jj_consume_token(18);
			value = this.List();
			this.jj_consume_token(19);
			{
				if (true) {
					return value;
				}
			}
			break;
		default:
			this.jj_la1[1] = this.jj_gen;
			this.jj_consume_token(-1);
			throw new ParseException();
		}
		throw new Error("Missing return statement in function");
	}

	final public Integer IntegerValue() throws ParseException {
		this.jj_consume_token(GMLParserConstants.INTEGER);
		// System.out.println("integer ="+token.image);
		Integer val = null;
		try {
			val = new Integer(this.token.image);
		} catch (NumberFormatException e) {
			// throw new ParseException(" bad numerique format at line
			// "+inputStream.getEndLine()+", column
			// "+inputStream.getEndColumn()+" : "+token.image);
		}
		{
			if (true) {
				return val;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public Double DoubleValue() throws ParseException {
		this.jj_consume_token(GMLParserConstants.REAL);
		// System.out.println("real ="+token.image);
		Double val = null;
		try {
			val = new Double(this.token.image);
		} catch (NumberFormatException e) {
			// throw new ParseException(" bad numerique format at line
			// "+inputStream.getEndLine()+", column
			// "+inputStream.getEndColumn()+" : "+token.image);
		}
		{
			if (true) {
				return val;
			}
		}
		throw new Error("Missing return statement in function");
	}

	final public String StringValue() throws ParseException {
		this.jj_consume_token(GMLParserConstants.STRING);
		// System.out.println("string ="+token.image);
		{
			if (true) {
				return this.token.image;
			}
		}
		throw new Error("Missing return statement in function");
	}

	public GMLParserTokenManager token_source;

	ASCII_CharStream jj_input_stream;

	public Token token, jj_nt;

	private int jj_ntk;

	private int jj_gen;

	final private int[] jj_la1 = new int[2];

	final private int[] jj_la1_0 = { 0x10000, 0x4a200, };

	public GMLParser(java.io.InputStream stream) {
		this.jj_input_stream = new ASCII_CharStream(stream, 1, 1);
		this.token_source = new GMLParserTokenManager(this.jj_input_stream);
		this.token = new Token();
		this.jj_ntk = -1;
		this.jj_gen = 0;
		for (int i = 0; i < 2; i++) {
			this.jj_la1[i] = -1;
		}
	}

	public void ReInit(java.io.InputStream stream) {
		this.jj_input_stream.ReInit(stream, 1, 1);
		this.token_source.ReInit(this.jj_input_stream);
		this.token = new Token();
		this.jj_ntk = -1;
		this.jj_gen = 0;
		for (int i = 0; i < 2; i++) {
			this.jj_la1[i] = -1;
		}
	}

	public GMLParser(java.io.Reader stream) {
		this.jj_input_stream = new ASCII_CharStream(stream, 1, 1);
		this.token_source = new GMLParserTokenManager(this.jj_input_stream);
		this.token = new Token();
		this.jj_ntk = -1;
		this.jj_gen = 0;
		for (int i = 0; i < 2; i++) {
			this.jj_la1[i] = -1;
		}
	}

	public void ReInit(java.io.Reader stream) {
		this.jj_input_stream.ReInit(stream, 1, 1);
		this.token_source.ReInit(this.jj_input_stream);
		this.token = new Token();
		this.jj_ntk = -1;
		this.jj_gen = 0;
		for (int i = 0; i < 2; i++) {
			this.jj_la1[i] = -1;
		}
	}

	public GMLParser(GMLParserTokenManager tm) {
		this.token_source = tm;
		this.token = new Token();
		this.jj_ntk = -1;
		this.jj_gen = 0;
		for (int i = 0; i < 2; i++) {
			this.jj_la1[i] = -1;
		}
	}

	public void ReInit(GMLParserTokenManager tm) {
		this.token_source = tm;
		this.token = new Token();
		this.jj_ntk = -1;
		this.jj_gen = 0;
		for (int i = 0; i < 2; i++) {
			this.jj_la1[i] = -1;
		}
	}

	final private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = this.token).next != null) {
			this.token = this.token.next;
		} else {
			this.token = this.token.next = this.token_source.getNextToken();
		}
		this.jj_ntk = -1;
		if (this.token.kind == kind) {
			this.jj_gen++;
			return this.token;
		}
		this.token = oldToken;
		this.jj_kind = kind;
		throw this.generateParseException();
	}

	final public Token getNextToken() {
		if (this.token.next != null) {
			this.token = this.token.next;
		} else {
			this.token = this.token.next = this.token_source.getNextToken();
		}
		this.jj_ntk = -1;
		this.jj_gen++;
		return this.token;
	}

	final public Token getToken(int index) {
		Token t = this.token;
		for (int i = 0; i < index; i++) {
			if (t.next != null) {
				t = t.next;
			} else {
				t = t.next = this.token_source.getNextToken();
			}
		}
		return t;
	}

	final private int jj_ntk() {
		if ((this.jj_nt = this.token.next) == null) {
			return (this.jj_ntk = (this.token.next = this.token_source
					.getNextToken()).kind);
		} else {
			return (this.jj_ntk = this.jj_nt.kind);
		}
	}

	private java.util.Vector jj_expentries = new java.util.Vector();

	private int[] jj_expentry;

	private int jj_kind = -1;

	final public ParseException generateParseException() {
		this.jj_expentries.removeAllElements();
		boolean[] la1tokens = new boolean[20];
		for (int i = 0; i < 20; i++) {
			la1tokens[i] = false;
		}
		if (this.jj_kind >= 0) {
			la1tokens[this.jj_kind] = true;
			this.jj_kind = -1;
		}
		for (int i = 0; i < 2; i++) {
			if (this.jj_la1[i] == this.jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((this.jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 20; i++) {
			if (la1tokens[i]) {
				this.jj_expentry = new int[1];
				this.jj_expentry[0] = i;
				this.jj_expentries.addElement(this.jj_expentry);
			}
		}
		int[][] exptokseq = new int[this.jj_expentries.size()][];
		for (int i = 0; i < this.jj_expentries.size(); i++) {
			exptokseq[i] = (int[]) this.jj_expentries.elementAt(i);
		}
		return new ParseException(this.token, exptokseq,
				GMLParserConstants.tokenImage);
	}

	final public void enable_tracing() {
	}

	final public void disable_tracing() {
	}

}

class ISO_8859_1 {
	/**
	 * return the character named by teh string <code> spec </code>. if any
	 * character isn't named by <code> spec </code>, it return -1.
	 */
	static int specToChar(String name) {
		return '*';
	}

	/**
	 * return the character name. If the characater does not have any name it
	 * return null.
	 */
	static String charToSpec(char c) {
		return "no yet implemented";
	}
}

// Generated from D:/2021SEwork/Compile/src/frontend\sysy.g4 by ANTLR 4.9.1
package frontend;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class sysyLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, Ident=2, RETURN=3, L_PAREN=4, R_PAREN=5, L_BRACE=6, R_BRACE=7, 
		SEMICOLON=8, DECIMAL_CONST=9, OCTAL_CONST=10, HEXADECIMAL_CONST=11, DIVIDE=12, 
		SINGLE_COMMENT=13, MULTI_COMMENT=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "Ident", "RETURN", "L_PAREN", "R_PAREN", "L_BRACE", "R_BRACE", 
			"SEMICOLON", "DECIMAL_CONST", "OCTAL_CONST", "HEXADECIMAL_CONST", "DIVIDE", 
			"SINGLE_COMMENT", "MULTI_COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'int'", "'main'", "'return'", "'('", "')'", "'{'", "'}'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "Ident", "RETURN", "L_PAREN", "R_PAREN", "L_BRACE", "R_BRACE", 
			"SEMICOLON", "DECIMAL_CONST", "OCTAL_CONST", "HEXADECIMAL_CONST", "DIVIDE", 
			"SINGLE_COMMENT", "MULTI_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public sysyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "sysy.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20x\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t"+
		"\3\t\3\n\3\n\3\n\6\n=\n\n\r\n\16\n>\5\nA\n\n\3\13\3\13\3\13\6\13F\n\13"+
		"\r\13\16\13G\5\13J\n\13\3\f\3\f\3\f\3\f\5\fP\n\f\3\f\6\fS\n\f\r\f\16\f"+
		"T\3\r\6\rX\n\r\r\r\16\rY\3\r\3\r\3\16\3\16\3\16\3\16\7\16b\n\16\f\16\16"+
		"\16e\13\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\7\17o\n\17\f\17\16"+
		"\17r\13\17\3\17\3\17\3\17\3\17\3\17\3p\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\3\2\b\3\2\63;\3\2\62;\3\2\62"+
		"9\5\2\62;CHch\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2\u0080\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33"+
		"\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5#\3\2\2\2\7(\3\2\2\2\t/\3\2\2\2\13"+
		"\61\3\2\2\2\r\63\3\2\2\2\17\65\3\2\2\2\21\67\3\2\2\2\23@\3\2\2\2\25I\3"+
		"\2\2\2\27O\3\2\2\2\31W\3\2\2\2\33]\3\2\2\2\35j\3\2\2\2\37 \7k\2\2 !\7"+
		"p\2\2!\"\7v\2\2\"\4\3\2\2\2#$\7o\2\2$%\7c\2\2%&\7k\2\2&\'\7p\2\2\'\6\3"+
		"\2\2\2()\7t\2\2)*\7g\2\2*+\7v\2\2+,\7w\2\2,-\7t\2\2-.\7p\2\2.\b\3\2\2"+
		"\2/\60\7*\2\2\60\n\3\2\2\2\61\62\7+\2\2\62\f\3\2\2\2\63\64\7}\2\2\64\16"+
		"\3\2\2\2\65\66\7\177\2\2\66\20\3\2\2\2\678\7=\2\28\22\3\2\2\29A\t\2\2"+
		"\2:<\t\2\2\2;=\t\3\2\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3\2\2"+
		"\2@9\3\2\2\2@:\3\2\2\2A\24\3\2\2\2BJ\7\62\2\2CE\7\62\2\2DF\t\4\2\2ED\3"+
		"\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2HJ\3\2\2\2IB\3\2\2\2IC\3\2\2\2J\26"+
		"\3\2\2\2KL\7\62\2\2LP\7z\2\2MN\7\62\2\2NP\7Z\2\2OK\3\2\2\2OM\3\2\2\2P"+
		"R\3\2\2\2QS\t\5\2\2RQ\3\2\2\2ST\3\2\2\2TR\3\2\2\2TU\3\2\2\2U\30\3\2\2"+
		"\2VX\t\6\2\2WV\3\2\2\2XY\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z[\3\2\2\2[\\\b\r"+
		"\2\2\\\32\3\2\2\2]^\7\61\2\2^_\7\61\2\2_c\3\2\2\2`b\n\7\2\2a`\3\2\2\2"+
		"be\3\2\2\2ca\3\2\2\2cd\3\2\2\2df\3\2\2\2ec\3\2\2\2fg\7\f\2\2gh\3\2\2\2"+
		"hi\b\16\2\2i\34\3\2\2\2jk\7\61\2\2kl\7,\2\2lp\3\2\2\2mo\13\2\2\2nm\3\2"+
		"\2\2or\3\2\2\2pq\3\2\2\2pn\3\2\2\2qs\3\2\2\2rp\3\2\2\2st\7,\2\2tu\7\61"+
		"\2\2uv\3\2\2\2vw\b\17\2\2w\36\3\2\2\2\f\2>@GIOTYcp\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
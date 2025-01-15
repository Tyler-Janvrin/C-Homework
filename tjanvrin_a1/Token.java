class Token {

  public final static int OPEN_TAG = 0;
  public final static int CLOSE_TAG = 1;
  public final static int OPEN_DOC = 2;
  public final static int CLOSE_DOC = 3;
  public final static int OPEN_TEXT = 4;
  public final static int CLOSE_TEXT = 5;
  public final static int OPEN_DATE = 6;
  public final static int CLOSE_DATE = 7;
  public final static int OPEN_DOCNO = 8;
  public final static int CLOSE_DOCNO = 9;
  public final static int OPEN_HEADLINE = 10;
  public final static int CLOSE_HEADLINE = 11;
  public final static int OPEN_LENGTH = 12;
  public final static int CLOSE_LENGTH = 13;
  public final static int WORD = 14;
  public final static int NUMBER = 15;
  public final static int APOSTROPHIZED = 16;
  public final static int HYPHENATED = 17;
  public final static int PUNCTUATION = 18;
  

  public int m_type;
  public String m_value;
  public int m_line;
  public int m_column;
  
  Token (int type, String value, int line, int column) {
    m_type = type;
    m_value = value;
    m_line = line;
    m_column = column;
  }

  public String toString() {
    switch (m_type) {
      case OPEN_TAG:
        return "OPEN-TAG";
      case CLOSE_TAG:
        return "CLOSE-TAG";
      case OPEN_DOC:
        return "OPEN-DOC";
      case CLOSE_DOC:
        return "CLOSE-DOC";
      case OPEN_TEXT:
        return "OPEN-TEXT";
      case CLOSE_TEXT:
        return "CLOSE-TEXT";
      case OPEN_DATE:
        return "OPEN-DATE";
      case CLOSE_DATE:
        return "CLOSE-DATE";
      case OPEN_DOCNO:
        return "OPEN-DOCNO";
      case CLOSE_DOCNO:
        return "CLOSE-DOCNO";
      case OPEN_HEADLINE:
        return "OPEN-HEADLINE";
      case CLOSE_HEADLINE:
        return "CLOSE-HEADLINE";
      case OPEN_LENGTH:
        return "OPEN-LENGTH";
      case CLOSE_LENGTH:
        return "CLOSE-LENGTH";
      case WORD:
        return "WORD(" + m_value + ")";
      case NUMBER:
        return "NUMBER(" + m_value + ")";
      case APOSTROPHIZED:
        return "APOSTROPHIZED(" + m_value + ")";
      case HYPHENATED:
        return "HYPHENATED(" + m_value + ")";
      case PUNCTUATION:
        return "PUNCTUATION(" + m_value + ")";
      default:
        return "UNKNOWN(" + m_value + ")";
    }
  }
}

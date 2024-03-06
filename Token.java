import java.util.ArrayList;

public class Token {

	public int fila;
	public int columna;

	public String lexema;
	public static final ArrayList<String> nombreToken = new ArrayList<String>();

	static{
		nombreToken.add("(");
		nombreToken.add(")");
		nombreToken.add(",");
		nombreToken.add(":");
		nombreToken.add("[");
		nombreToken.add("]");
		nombreToken.add(":=");
		nombreToken.add(";");
		nombreToken.add("..");
		nombreToken.add("+ -");
		nombreToken.add("* / // %");
		nombreToken.add("'funcion'");
		nombreToken.add("'var'");
		nombreToken.add("'fvar'");
		nombreToken.add("'entero'");
		nombreToken.add("'real'");
		nombreToken.add("'tabla'");
		nombreToken.add("'de'");
		nombreToken.add("'puntero'");
		nombreToken.add("'blq'");
		nombreToken.add("'fblq'");
		nombreToken.add("'escribe'");
		nombreToken.add("identificador");
		nombreToken.add("numero entero");
		nombreToken.add("numero real");
		nombreToken.add("fin de fichero");
	}

	public int tipo;		// tipo es: ID, ENTERO, REAL ...

	public static final int
		PARI 		= 0,
		PARD		= 1,
		COMA		= 2,
		DOSP            = 3,
		CORI            = 4,
		CORD            = 5,
		ASIG		= 6,
		PYC		= 7,
		PTOPTO		= 8,
		OPAS		= 9,
		OPMUL		= 10,
		FUNCION		= 11,
		VAR		= 12,
		FVAR		= 13,
		ENTERO		= 14,
		REAL		= 15,
		TABLA		= 16,
		DE		= 17,
		PUNTERO		= 18,
		BLQ		= 19,
		FBLQ		= 20,
		ESCRIBE		= 21,
		ID		= 22,
		NUMENTERO	= 23,
		NUMREAL		= 24,
		EOF		= 25;

	public Token(){
		lexema = new String();
	}	
	public String toString(){
	    return nombreToken.get(tipo);
	}

	public void setTipo(int status){
		tipo = status;
	}
}


import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class AnalizadorLexico {

    private RandomAccessFile entrada;
    private Queue<Character> cola;
    private int fila;
    private int columna;
    private int ultimoCaracterLeido;
    private String lexema;

    public static int ERROR = -2;
    public static int FINAL = -1;
    public static int COMENTARIO = -3;
    private static ArrayList<String> palabras = new ArrayList<>(Arrays.asList("funcion", "var", "fvar", "entero", "real", "tabla", "de", "puntero", "blq", "fblq", "escribe"));

    public AnalizadorLexico(RandomAccessFile in) {
        entrada = in;
        fila = 1;
        columna = 0;
        cola = new LinkedList<Character>();
        ultimoCaracterLeido = -1; // Valor inicial
    }

    public Token siguienteToken() {
        Token token = new Token();
        int status = 0;
        char c;

        do {
            c = leerCaracter();
            columna += 1;

            if (c == '\n') {
                fila += 1;
                columna = 0;
            } else if (c == Token.EOF) {
                token.tipo = Token.EOF;
                return token;
            } else {
                int newStatus = delta(status, c);
                if (newStatus == ERROR) {
                    errorLexico(c); //CREO QUE SE ESTA PASANDO EL SIGUIENTE CARACTER, EN SU LUGAR SE DEBERIA DE PASAR EL CARACTER ANTERIOR.
                }
                if (esFinal(newStatus) || newStatus == FINAL){

                    if(newStatus == FINAL){
                        token.setTipo(status);
                    }
                    else{
                        token.setTipo(newStatus);
                    }
                    token.lexema = lexema;
                    token.fila = fila;
                    token.columna = columna;
                    lexema = "";
                    return token;

                } else {
                    status = newStatus;
                }
            }
        } while (true);
    }

    private int delta(int status, int c) {
        
        switch(status){
            case 0: 
                if (c == ')') {
                    lexema += c;
                    return Token.PARI;
                }
                if (c == '(') {
                    lexema += c;
                    return Token.PARD;
                }
                if (c == ',') {
                    lexema += c;
                    return Token.COMA;
                }
                if (c == ':') {
                    lexema += c;
                    return Token.DOSP;
                }
                if (c == '[') {
                    lexema += c;
                    return Token.CORI;
                }
                if (c == ']') {
                    lexema += c;
                    return Token.CORD;
                }
                if (c == ';') {
                    lexema += c;
                    return Token.PYC;
                }
                if (c == '.') {
                    lexema += c;
                    return Token.PTOPTO;
                }
                if (c == '+' || c == '-') {
                    lexema += c;
                    return Token.OPAS;
                }
                if (c == '*' || c == '/' || c == '%') {
                    lexema += c;
                    return Token.OPMUL;
                }
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                    lexema += c;
                    return Token.ID;
                }
                if (c >= '0' && c <= '9') {
                    lexema += c;
                    return Token.NUMENTERO;
                }
                else 
                    return ERROR;

            case Token.DOSP:
                if (c == '='){
                    lexema += c;
                    return Token.ASIG;
                }
                else{
                    columna -= 1;
                    retrocederPuntero(1);
                    return FINAL;
                }
                
            case Token.PTOPTO:
                if(c == '.'){
                    lexema += c;
                    return FINAL;
                }
                else{
                    columna -=1;
                    retrocederPuntero(1);
                    return ERROR;
                }
            case Token.OPMUL:
                if(lexema.equals("*") || lexema.equals("%")){
                    columna -=1;
                    retrocederPuntero(1);
                    return FINAL;
                }
                else{
                    if(c != '/' || c != '*' ){
                        columna -=1;
                        retrocederPuntero(1);
                        return FINAL;
                    }
                    else{
                        lexema += c;
                        return COMENTARIO; //TERMINAR COMENTARIO
                    }
                }
            

        }
         
        return 0;
    }

 
    public char leerCaracter() {
        char currentChar;
        try {
            if (cola.isEmpty()) {
                if (ultimoCaracterLeido != -1) {
                    entrada.seek(ultimoCaracterLeido);
                }
                currentChar = (char) entrada.readByte();
                ultimoCaracterLeido = (int) entrada.getFilePointer();
            } else {
                currentChar = cola.poll();
            }
            return currentChar;
        } catch (EOFException e) {
            return Token.EOF;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return ' ';
    }

    private boolean esFinal(int status) {
        return ((status >= Token.PARI && status <= Token.OPAS) && (status != Token.DOSP && status != Token.PTOPTO));
    }

    private void errorLexico(char c) {
        System.err.println("Error lexico ( " + fila + "," + columna + "): caracter '" + c + "' incorrecto");
        System.exit(-1);
    }

    private void retrocederPuntero(int cantidad) {
        try {
            entrada.seek(ultimoCaracterLeido - cantidad);
            ultimoCaracterLeido = (int) entrada.getFilePointer();
        } catch (IOException e) {
            System.err.println("Error al retroceder el puntero de lectura.");
            System.exit(-1);
        }
    }

    private static boolean esPalabraRegistrada(String str) {
        return palabras.contains(str);
    }

}

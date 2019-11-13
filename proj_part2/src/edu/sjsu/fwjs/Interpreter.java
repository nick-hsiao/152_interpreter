package edu.sjsu.fwjs;

public class Interpreter {

    public static void main(String[] args) throws Exception {
        Expression prog = new BinOpExpr(Op.ADD,
                new ValueExpr(new IntVal(3)),
                new ValueExpr(new IntVal(4)));
        System.out.println("'3 + 4;' evaluates to " + prog.evaluate(new Environment()));
        
        
//        Environment env = new Environment();
//        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
//        FunctionDeclExpr f = new FunctionDeclExpr(new ArrayList<String>(),
//                new VarExpr("x"));
//        SeqExpr seq = new SeqExpr(newVar, new FunctionAppExpr(f, new ArrayList<Expression>()));
//        Value v = seq.evaluate(env);
//        
//        System.out.println(v);
    }
}

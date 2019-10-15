package edu.sjsu.fwjs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class Interpreter {

    public static void main(String[] args) throws Exception {
//        Expression prog = new BinOpExpr(Op.ADD,
//                new ValueExpr(new IntVal(3)),
//                new ValueExpr(new IntVal(4)));
//        System.out.println("'3 + 4;' evaluates to " + prog.evaluate(new Environment()));
//        
        
        Environment env = new Environment();
        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
        FunctionDeclExpr f = new FunctionDeclExpr(new ArrayList<String>(),
                new VarExpr("x"));
        SeqExpr seq = new SeqExpr(newVar, new FunctionAppExpr(f, new ArrayList<Expression>()));
        //Value v = seq.evaluate(env);
        
        FunctionAppExpr n = new FunctionAppExpr(f, new ArrayList<Expression>());
        
        System.out.println(n.evaluate(env));
        
        ///////////////////////////////////////////////////
        Environment env1 = new Environment();
        VarDeclExpr newVar1 = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
        FunctionDeclExpr f1 = new FunctionDeclExpr(new ArrayList<String>(),
                new SeqExpr(new VarDeclExpr("x", new ValueExpr(new IntVal(42))),
                        new VarExpr("x")));
        SeqExpr seq1 = new SeqExpr(newVar1, new FunctionAppExpr(f1, new ArrayList<Expression>()));
        //Value v1 = seq1.evaluate(env1);
        
        FunctionAppExpr n1 = new FunctionAppExpr(f1, new ArrayList<Expression>());
        System.out.println(newVar1.evaluate(env1));


    }
}

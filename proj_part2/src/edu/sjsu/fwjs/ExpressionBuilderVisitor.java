package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.sjsu.fwjs.parser.FeatherweightJavaScriptBaseVisitor;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptParser;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ExpressionBuilderVisitor extends FeatherweightJavaScriptBaseVisitor<Expression>{
    @Override
    public Expression visitProg(FeatherweightJavaScriptParser.ProgContext ctx) {
        List<Expression> stmts = new ArrayList<Expression>();
        for (int i=0; i<ctx.stat().size(); i++) {
            Expression exp = visit(ctx.stat(i));
            if (exp != null) stmts.add(exp);
        }
        return listToSeqExp(stmts);
    }

    @Override
    public Expression visitbareExpr(FeatherweightJavaScriptParser.BareExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitifThenElse(FeatherweightJavaScriptParser.IfThenElseContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression thn = visit(ctx.block(0));
        Expression els = visit(ctx.block(1));
        return new IfExpr(cond, thn, els);
    }

    @Override
    public Expression visitifThen(FeatherweightJavaScriptParser.IfThenContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression thn = visit(ctx.block());
        return new IfExpr(cond, thn, null);
    }

    @Override
    public Expression visitint(FeatherweightJavaScriptParser.IntContext ctx) {
        int val = Integer.valueOf(ctx.INT().getText());
        return new ValueExpr(new IntVal(val));
    }

    // Add implementation to our statements in the order they appear in our grammar file from part 2:
    @Override
    public Expression visitwhileExprBlock(FeatherweightJavaScriptParser.WhileContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression body = visit(ctx.block());
        return new WhileExpr(cond, body);
    }

    public Expression visitprintExpr(FeatherweightJavaScriptParser.PrintContext ctx) {
        Expression exp = visit(ctx.expr());
        return new PrintExpr(exp);
    }

    public Expression visitbool(FeatherweightJavaScriptParser.BoolContext ctx) {
        boolean val = Boolean.valueOf(ctx.BOOL().getText());
        return new ValueExpr(new BoolVal(val));
    }

    public Expression visitnull(FeatherweightJavaScriptParser.NullContext ctx) {
        return new ValueExpr(new NullVal());
    }

    public Expression visitiden(FeatherweightJavaScriptParser.VarRefContext ctx) {
        String name = String.valueOf(ctx.IDENTIFIER().getText());
        return new VarExpr(name);
    }

    public Expression visitFunctionDec(FeatherweightJavaScriptParser.FunctionDeclContext ctx) {
        ArrayList<String> params = new ArrayList<>();
        List<TerminalNode> listOfNodes = ctx.params().IDENTIFIER();
        for(int i = 0; i < listOfNodes.size(); i ++)
        {
            String par = String.valueOf(listOfNodes.get(i));
            params.add(par);
        }
        Expression body = visit(ctx.block());
        return new FunctionDeclExpr(params, body);
    }

    public Expression visitFuncAppl(FeatherweightJavaScriptParser.FuncApplContext ctx) {

        Expression expression = visit(ctx.expr());
        ArrayList<Expression> argList = new ArrayList<>();
        // accumulate nodes/args of a function into a list

        if(ctx.args() != null)
        {
            argList = (ArrayList<Expression>)ctx.args().expr().stream().map(node -> visit(node)).collect(Collectors.toList());

        }
		/*
		else
			System.out.println("no args");

		for(Expression e : argList)
		{
			System.out.println("arg:"+e.getClass());
		}*/



        return new FunctionAppExpr(expression, argList);
    }///

    @Override
    public Expression visitArgs(FeatherweightJavaScriptParser.ArgsContext ctx) {

        System.out.println(ctx.expr().getClass());
        return super.visitArgs(ctx);
    }///

    public Expression visitDeclaration(FeatherweightJavaScriptParser.VarDeclContext ctx) {
        String name = String.valueOf(ctx.IDENTIFIER().getText());
        Expression exp = visit(ctx.expr());
        return new VarDeclExpr(name, exp);
    }

    public Expression visitAssign(FeatherweightJavaScriptParser.AssignContext ctx) {
        String name = String.valueOf(ctx.IDENTIFIER().getText());
        Expression exp = visit(ctx.expr());
        return new AssignExpr(name, exp);
    }

    public Expression visitMulDivMod(FeatherweightJavaScriptParser.MulDivModContext ctx) {
        Expression e1 = visit(ctx.expr(0));
        Expression e2 = visit(ctx.expr(1));
        Op oper = getEnumVal(String.valueOf(ctx.op.getText()));
        return new BinOpExpr(oper, e1, e2);
    }

    public Op getEnumVal(String str)   {
        if(str.equals("+"))
            return Op.ADD;
        else if(str.equals("-"))
            return Op.SUBTRACT;
        else if(str.equals("*"))
            return Op.MULTIPLY;
        else if(str.equals("/"))
            return Op.DIVIDE;
        else if(str.equals("%"))
            return Op.MOD;
        else if(str.equals(">"))
            return Op.GT;
        else if(str.equals(">="))
            return Op.GE;
        else if(str.equals("<"))
            return Op.LT;
        else if(str.equals("<="))
            return Op.LE;
        else if(str.equals("=="))
            return Op.EQ;
        return null;
    }

    public Expression visitAddSub(FeatherweightJavaScriptParser.AddSubContext ctx) {
        Expression e1 = visit(ctx.expr(0));
        Expression e2 = visit(ctx.expr(1));
        Op oper = getEnumVal(String.valueOf(ctx.op.getText()));
        return new BinOpExpr(oper, e1, e2);
    }

    public Expression visitEquality(FeatherweightJavaScriptParser.ComparatorsContext ctx) {
        Expression e1 = visit(ctx.expr(0));
        Expression e2 = visit(ctx.expr(1));
        Op oper = getEnumVal(String.valueOf(ctx.op.getText()));
        return new BinOpExpr(oper, e1, e2);
    }

    @Override
    public Expression visitparens(FeatherweightJavaScriptParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitfullBlock(FeatherweightJavaScriptParser.FullBlockContext ctx) {
        List<Expression> stmts = new ArrayList<Expression>();
        for (int i=1; i<ctx.getChildCount()-1; i++) {
            Expression exp = visit(ctx.getChild(i));
            stmts.add(exp);
        }
        return listToSeqExp(stmts);
    }

    /**
     * Converts a list of expressions to one sequence expression,
     * if the list contained more than one expression.
     */
    private Expression listToSeqExp(List<Expression> stmts) {
        if (stmts.isEmpty()) return null;
        Expression exp = stmts.get(0);
        for (int i=1; i<stmts.size(); i++) {
            exp = new SeqExpr(exp, stmts.get(i));
        }
        return exp;
    }



    @Override
    public Expression visitsimpBlock(FeatherweightJavaScriptParser.SimpBlockContext ctx) {
        return visit(ctx.stat());
    }
}